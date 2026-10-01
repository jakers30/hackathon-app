-- ===========================================================================
-- Study Room - Row Level Security (spec sections 9 & 10)
-- Run after schema.sql. Access is enforced on the server for every room
-- request, not just hidden in the UI.
-- ===========================================================================

-- ---- helper functions -----------------------------------------------------
-- SECURITY DEFINER so these checks do not recurse into RLS on the same tables.
create or replace function public.is_room_member(p_room uuid, p_user uuid)
returns boolean language sql security definer set search_path = public as $$
    select exists(
        select 1 from public.room_members m
        where m.room_id = p_room and m.user_id = p_user and m.deleted_at is null
    );
$$;

create or replace function public.is_room_host(p_room uuid, p_user uuid)
returns boolean language sql security definer set search_path = public as $$
    select exists(
        select 1 from public.rooms r where r.id = p_room and r.host_id = p_user
    ) or exists(
        select 1 from public.room_members m
        where m.room_id = p_room and m.user_id = p_user
          and m.role = 'HOST' and m.deleted_at is null
    );
$$;

create or replace function public.is_allowed_user(p_room uuid, p_user uuid)
returns boolean language sql security definer set search_path = public as $$
    select exists(
        select 1 from public.room_allowed_users a
        where a.room_id = p_room and a.user_id = p_user and a.deleted_at is null
    );
$$;

-- Host, member, allowed user, or a public room.
create or replace function public.can_access_room(p_room uuid, p_user uuid)
returns boolean language sql security definer set search_path = public as $$
    select public.is_room_member(p_room, p_user)
        or public.is_room_host(p_room, p_user)
        or public.is_allowed_user(p_room, p_user)
        or exists(
            select 1 from public.rooms r
            where r.id = p_room and r.visibility = 'PUBLIC' and r.deleted_at is null
        );
$$;

-- A caller may join a public room, a private room (with the code), or a
-- specific-user room they are listed on (spec section 3.3).
create or replace function public.can_join_room(p_room uuid, p_user uuid)
returns boolean language sql security definer set search_path = public as $$
    select exists(
        select 1 from public.rooms r
        where r.id = p_room and r.deleted_at is null
          and (
              r.visibility in ('PUBLIC', 'PRIVATE')
              or (r.visibility = 'SPECIFIC' and public.is_allowed_user(p_room, p_user))
          )
    );
$$;

-- ---- enable RLS -----------------------------------------------------------
alter table public.profiles           enable row level security;
alter table public.rooms              enable row level security;
alter table public.room_members       enable row level security;
alter table public.room_allowed_users enable row level security;
alter table public.resources          enable row level security;
alter table public.roadmap_items      enable row level security;
alter table public.roadmap_progress   enable row level security;
alter table public.reviewers          enable row level security;
alter table public.quizzes            enable row level security;
alter table public.quiz_questions     enable row level security;
alter table public.tasks              enable row level security;

-- ---- profiles -------------------------------------------------------------
drop policy if exists profiles_select on public.profiles;
create policy profiles_select on public.profiles
    for select to authenticated using (true);

drop policy if exists profiles_insert on public.profiles;
create policy profiles_insert on public.profiles
    for insert to authenticated with check (id = auth.uid());

drop policy if exists profiles_update on public.profiles;
create policy profiles_update on public.profiles
    for update to authenticated using (id = auth.uid()) with check (id = auth.uid());

-- ---- rooms ----------------------------------------------------------------
drop policy if exists rooms_select on public.rooms;
create policy rooms_select on public.rooms
    for select to authenticated
    using (
        deleted_at is null and (
            host_id = auth.uid()
            or visibility = 'PUBLIC'
            or public.is_room_member(id, auth.uid())
            or public.is_allowed_user(id, auth.uid())
        )
    );

drop policy if exists rooms_insert on public.rooms;
create policy rooms_insert on public.rooms
    for insert to authenticated with check (host_id = auth.uid());

drop policy if exists rooms_update on public.rooms;
create policy rooms_update on public.rooms
    for update to authenticated
    using (host_id = auth.uid()) with check (host_id = auth.uid());

drop policy if exists rooms_delete on public.rooms;
create policy rooms_delete on public.rooms
    for delete to authenticated using (host_id = auth.uid());

-- ---- room_members ---------------------------------------------------------
drop policy if exists room_members_select on public.room_members;
create policy room_members_select on public.room_members
    for select to authenticated
    using (
        public.is_room_member(room_id, auth.uid())
        or public.is_room_host(room_id, auth.uid())
    );

drop policy if exists room_members_insert on public.room_members;
create policy room_members_insert on public.room_members
    for insert to authenticated
    with check (
        (user_id = auth.uid() and public.can_join_room(room_id, auth.uid()))
        or public.is_room_host(room_id, auth.uid())
    );

drop policy if exists room_members_update on public.room_members;
create policy room_members_update on public.room_members
    for update to authenticated
    using (public.is_room_host(room_id, auth.uid()))
    with check (public.is_room_host(room_id, auth.uid()));

-- ---- room_allowed_users (host only) ---------------------------------------
drop policy if exists allowed_users_all on public.room_allowed_users;
create policy allowed_users_all on public.room_allowed_users
    for all to authenticated
    using (public.is_room_host(room_id, auth.uid()))
    with check (public.is_room_host(room_id, auth.uid()));

-- ---- resources (members read; host uploads/deletes) -----------------------
drop policy if exists resources_select on public.resources;
create policy resources_select on public.resources
    for select to authenticated
    using (deleted_at is null and public.can_access_room(room_id, auth.uid()));

drop policy if exists resources_insert on public.resources;
create policy resources_insert on public.resources
    for insert to authenticated with check (public.is_room_host(room_id, auth.uid()));

drop policy if exists resources_update on public.resources;
create policy resources_update on public.resources
    for update to authenticated
    using (public.is_room_host(room_id, auth.uid()))
    with check (public.is_room_host(room_id, auth.uid()));

drop policy if exists resources_delete on public.resources;
create policy resources_delete on public.resources
    for delete to authenticated using (public.is_room_host(room_id, auth.uid()));

-- ---- roadmap / reviewer (per-student copies) ------------------------------
drop policy if exists roadmap_owner_all on public.roadmap_items;
create policy roadmap_owner_all on public.roadmap_items
    for all to authenticated
    using (owner_id = auth.uid())
    with check (owner_id = auth.uid() and public.can_access_room(room_id, auth.uid()));

drop policy if exists reviewers_owner_all on public.reviewers;
create policy reviewers_owner_all on public.reviewers
    for all to authenticated
    using (owner_id = auth.uid())
    with check (owner_id = auth.uid() and public.can_access_room(room_id, auth.uid()));

drop policy if exists progress_owner_all on public.roadmap_progress;
create policy progress_owner_all on public.roadmap_progress
    for all to authenticated
    using (user_id = auth.uid())
    with check (user_id = auth.uid());

-- ---- quizzes (drafts + answer keys are host-only) -------------------------
drop policy if exists quizzes_select on public.quizzes;
create policy quizzes_select on public.quizzes
    for select to authenticated
    using (
        public.is_room_host(room_id, auth.uid())
        or (status = 'PUBLISHED' and public.can_access_room(room_id, auth.uid()))
    );

drop policy if exists quizzes_write on public.quizzes;
create policy quizzes_write on public.quizzes
    for all to authenticated
    using (public.is_room_host(room_id, auth.uid()))
    with check (public.is_room_host(room_id, auth.uid()));

-- Students never receive quiz questions until a later release adds a
-- answer-free projection; the answer key stays on the host only.
drop policy if exists quiz_questions_select on public.quiz_questions;
create policy quiz_questions_select on public.quiz_questions
    for select to authenticated
    using (
        exists (
            select 1 from public.quizzes q
            where q.id = quiz_id and public.is_room_host(q.room_id, auth.uid())
        )
    );

drop policy if exists quiz_questions_write on public.quiz_questions;
create policy quiz_questions_write on public.quiz_questions
    for all to authenticated
    using (
        exists (
            select 1 from public.quizzes q
            where q.id = quiz_id and public.is_room_host(q.room_id, auth.uid())
        )
    )
    with check (
        exists (
            select 1 from public.quizzes q
            where q.id = quiz_id and public.is_room_host(q.room_id, auth.uid())
        )
    );

-- ---- tasks (owner only) ---------------------------------------------------
drop policy if exists tasks_owner_all on public.tasks;
create policy tasks_owner_all on public.tasks
    for all to authenticated
    using (user_id = auth.uid())
    with check (user_id = auth.uid());

-- ---- storage: resources bucket -------------------------------------------
-- Objects are stored as {roomId}/{resourceId}/{filename}.
drop policy if exists storage_resources_read on storage.objects;
create policy storage_resources_read on storage.objects
    for select to authenticated
    using (
        bucket_id = 'resources'
        and public.can_access_room(((storage.foldername(name))[1])::uuid, auth.uid())
    );

drop policy if exists storage_resources_write on storage.objects;
create policy storage_resources_write on storage.objects
    for insert to authenticated
    with check (
        bucket_id = 'resources'
        and public.is_room_host(((storage.foldername(name))[1])::uuid, auth.uid())
    );

drop policy if exists storage_resources_update on storage.objects;
create policy storage_resources_update on storage.objects
    for update to authenticated
    using (
        bucket_id = 'resources'
        and public.is_room_host(((storage.foldername(name))[1])::uuid, auth.uid())
    );

drop policy if exists storage_resources_delete on storage.objects;
create policy storage_resources_delete on storage.objects
    for delete to authenticated
    using (
        bucket_id = 'resources'
        and public.is_room_host(((storage.foldername(name))[1])::uuid, auth.uid())
    );

-- ---- grants ---------------------------------------------------------------
grant usage on schema public to anon, authenticated;
grant select, insert, update, delete on all tables in schema public to authenticated;
grant execute on all functions in schema public to authenticated;
