-- ===========================================================================
-- Study Room - Supabase schema (spec section 10)
-- Run in the Supabase SQL editor. Then run policies.sql and seed.sql.
-- Columns match the app DTOs in data/remote/dto/*.
-- ===========================================================================

create extension if not exists "pgcrypto";

-- ---- profiles -------------------------------------------------------------
create table if not exists public.profiles (
    id          uuid primary key references auth.users (id) on delete cascade,
    name        text not null,
    email       text not null,
    created_at  timestamptz not null default now()
);

-- Creates a profile automatically for every new auth user.
create or replace function public.handle_new_user()
returns trigger
language plpgsql
security definer set search_path = public
as $$
begin
    insert into public.profiles (id, name, email, created_at)
    values (
        new.id,
        coalesce(new.raw_user_meta_data ->> 'name', 'Student'),
        coalesce(new.email, ''),
        now()
    )
    on conflict (id) do nothing;
    return new;
end;
$$;

drop trigger if exists on_auth_user_created on auth.users;
create trigger on_auth_user_created
    after insert on auth.users
    for each row execute function public.handle_new_user();

-- ---- rooms ----------------------------------------------------------------
create table if not exists public.rooms (
    id           uuid primary key default gen_random_uuid(),
    name         text not null,
    description  text not null default '',
    host_id      uuid not null references auth.users (id) on delete cascade,
    visibility   text not null default 'PRIVATE'
                 check (visibility in ('PUBLIC', 'PRIVATE', 'SPECIFIC')),
    invite_code  text not null unique,
    created_at   timestamptz not null default now(),
    updated_at   timestamptz not null default now(),
    deleted_at   timestamptz
);

create table if not exists public.room_members (
    id          uuid primary key default gen_random_uuid(),
    room_id     uuid not null references public.rooms (id) on delete cascade,
    user_id     uuid not null references auth.users (id) on delete cascade,
    role        text not null default 'STUDENT' check (role in ('HOST', 'STUDENT')),
    joined_at   timestamptz not null default now(),
    updated_at  timestamptz not null default now(),
    deleted_at  timestamptz,
    unique (room_id, user_id)
);

create table if not exists public.room_allowed_users (
    id          uuid primary key default gen_random_uuid(),
    room_id     uuid not null references public.rooms (id) on delete cascade,
    user_id     uuid not null references auth.users (id) on delete cascade,
    email       text not null,
    created_at  timestamptz not null default now(),
    updated_at  timestamptz not null default now(),
    deleted_at  timestamptz
);

-- ---- resources ------------------------------------------------------------
create table if not exists public.resources (
    id            uuid primary key default gen_random_uuid(),
    room_id       uuid not null references public.rooms (id) on delete cascade,
    original_name text not null,
    display_name  text,                        -- AI-suggested name (spec 3.4)
    mime_type     text not null,
    size_bytes    bigint not null default 0,
    storage_path  text not null,
    uploaded_by   uuid not null references auth.users (id) on delete cascade,
    created_at    timestamptz not null default now(),
    updated_at    timestamptz not null default now(),
    deleted_at    timestamptz
);

-- ---- study materials ------------------------------------------------------
-- Roadmaps/reviewers are per-student copies (owner_id) so one student's edit
-- never changes another's (spec section 4).
create table if not exists public.roadmap_items (
    id           uuid primary key default gen_random_uuid(),
    room_id      uuid not null references public.rooms (id) on delete cascade,
    owner_id     uuid not null references auth.users (id) on delete cascade,
    order_index  int not null default 0,
    topic        text not null,
    description  text not null default '',
    subtopics    jsonb not null default '[]'::jsonb,
    completed    boolean not null default false,
    created_at   timestamptz not null default now(),
    updated_at   timestamptz not null default now(),
    deleted_at   timestamptz
);

-- Reserved for shared (room-owned) roadmaps; per-student copies use the
-- roadmap_items.completed column above.
create table if not exists public.roadmap_progress (
    id          uuid primary key default gen_random_uuid(),
    item_id     uuid not null references public.roadmap_items (id) on delete cascade,
    user_id     uuid not null references auth.users (id) on delete cascade,
    completed   boolean not null default false,
    updated_at  timestamptz not null default now(),
    unique (item_id, user_id)
);

create table if not exists public.reviewers (
    id          uuid primary key default gen_random_uuid(),
    room_id     uuid not null references public.rooms (id) on delete cascade,
    owner_id    uuid not null references auth.users (id) on delete cascade,
    sections    jsonb not null default '[]'::jsonb,
    created_at  timestamptz not null default now(),
    updated_at  timestamptz not null default now(),
    deleted_at  timestamptz
);

-- ---- quizzes --------------------------------------------------------------
-- The answer key lives in quiz_questions.correct_answer and is protected by RLS
-- so students never receive it (spec section 3.6 / 9).
create table if not exists public.quizzes (
    id                uuid primary key default gen_random_uuid(),
    room_id           uuid not null references public.rooms (id) on delete cascade,
    title             text not null,
    type              text not null default 'quiz' check (type in ('quiz', 'exam')),
    status            text not null default 'DRAFT' check (status in ('DRAFT', 'PUBLISHED')),
    question_count    int not null default 0,
    created_by        uuid not null references auth.users (id) on delete cascade,
    review_confirmed  boolean not null default false,
    created_at        timestamptz not null default now(),
    updated_at        timestamptz not null default now(),
    deleted_at        timestamptz
);

create table if not exists public.quiz_questions (
    id                    uuid primary key default gen_random_uuid(),
    quiz_id               uuid not null references public.quizzes (id) on delete cascade,
    order_index           int not null default 0,
    type                  text not null default 'multiple_choice',
    prompt                text not null,
    options               jsonb not null default '[]'::jsonb,
    correct_answer        text not null default '',
    explanation           text not null default '',
    source_resource_id    uuid references public.resources (id) on delete set null,
    source_resource_name  text,
    created_at            timestamptz not null default now(),
    updated_at            timestamptz not null default now()
);

-- ---- tasks ----------------------------------------------------------------
create table if not exists public.tasks (
    id          uuid primary key default gen_random_uuid(),
    user_id     uuid not null references auth.users (id) on delete cascade,
    title       text not null,
    notes       text not null default '',
    priority    text not null default 'MEDIUM' check (priority in ('LOW', 'MEDIUM', 'HIGH')),
    due_at      timestamptz,
    completed   boolean not null default false,
    created_at  timestamptz not null default now(),
    updated_at  timestamptz not null default now(),
    deleted_at  timestamptz
);

-- ---- indices --------------------------------------------------------------
create index if not exists idx_rooms_host           on public.rooms (host_id);
create index if not exists idx_rooms_visibility     on public.rooms (visibility);
create index if not exists idx_members_room         on public.room_members (room_id);
create index if not exists idx_members_user         on public.room_members (user_id);
create index if not exists idx_allowed_room         on public.room_allowed_users (room_id);
create index if not exists idx_resources_room       on public.resources (room_id);
create index if not exists idx_roadmap_room_owner   on public.roadmap_items (room_id, owner_id);
create index if not exists idx_reviewers_room_owner on public.reviewers (room_id, owner_id);
create index if not exists idx_quizzes_room_status  on public.quizzes (room_id, status);
create index if not exists idx_questions_quiz       on public.quiz_questions (quiz_id);
create index if not exists idx_tasks_user           on public.tasks (user_id);

-- ---- storage bucket for resource files ------------------------------------
insert into storage.buckets (id, name, public)
values ('resources', 'resources', false)
on conflict (id) do nothing;

