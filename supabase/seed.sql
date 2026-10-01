-- ===========================================================================
-- Study Room - seed data (spec section 8)
-- Run after schema.sql and policies.sql.
-- Creates a demo host account + a public demo room so the demo looks full.
--
-- Demo login:  demo@studyroom.app / demo1234
-- Demo code:   DEMO01
-- If the auth user insert is skipped (see NOTICE), create the account in the
-- dashboard and re-run from the block below with your own uuid.
-- ===========================================================================

do $$
declare
    v_host uuid := '11111111-1111-1111-1111-111111111111';
begin
    insert into auth.users (
        instance_id, id, aud, role, email, encrypted_password,
        email_confirmed_at, raw_app_meta_data, raw_user_meta_data,
        created_at, updated_at, confirmation_token, recovery_token,
        email_change_token_new, email_change
    ) values (
        '00000000-0000-0000-0000-000000000000', v_host, 'authenticated', 'authenticated',
        'demo@studyroom.app',
        extensions.crypt('demo1234', extensions.gen_salt('bf')),
        now(), '{"provider":"email","providers":["email"]}', '{"name":"Demo Host"}',
        now(), now(), '', '', '', ''
    ) on conflict (id) do nothing;

    insert into auth.identities (id, user_id, identity_data, provider, provider_id,
                                 last_sign_in_at, created_at, updated_at)
    values (gen_random_uuid(), v_host,
            jsonb_build_object('sub', v_host::text, 'email', 'demo@studyroom.app'),
            'email', v_host::text, now(), now(), now())
    on conflict do nothing;
exception when others then
    raise notice 'Demo auth user insert skipped (create it in the dashboard): %', sqlerrm;
end $$;

-- ---- demo room ------------------------------------------------------------
insert into public.profiles (id, name, email, created_at)
values ('11111111-1111-1111-1111-111111111111', 'Demo Host', 'demo@studyroom.app', now())
on conflict (id) do nothing;

insert into public.rooms (id, name, description, host_id, visibility, invite_code, created_at, updated_at)
values ('22222222-2222-2222-2222-222222222222', 'Intro to Statistics',
        'Probability, descriptive statistics, and basic distributions.',
        '11111111-1111-1111-1111-111111111111', 'PUBLIC', 'DEMO01', now(), now())
on conflict (id) do nothing;

insert into public.room_members (id, room_id, user_id, role, joined_at, updated_at)
values (gen_random_uuid(), '22222222-2222-2222-2222-222222222222',
        '11111111-1111-1111-1111-111111111111', 'HOST', now(), now())
on conflict (room_id, user_id) do nothing;

-- ---- demo resources (metadata only; the AI proxy reads the storage files) --
insert into public.resources (id, room_id, original_name, display_name, mime_type,
                              size_bytes, storage_path, uploaded_by, created_at, updated_at)
values
    ('33333333-3333-3333-3333-333333333331', '22222222-2222-2222-2222-222222222222',
     'week1_notes.pdf', 'Descriptive Statistics - Basics.pdf', 'application/pdf', 245000,
     '22222222-2222-2222-2222-222222222222/33333333-3333-3333-3333-333333333331/week1_notes.pdf',
     '11111111-1111-1111-1111-111111111111', now(), now()),
    ('33333333-3333-3333-3333-333333333332', '22222222-2222-2222-2222-222222222222',
     'scan0012.pdf', 'Probability - Basic Rules.pdf', 'application/pdf', 198000,
     '22222222-2222-2222-2222-222222222222/33333333-3333-3333-3333-333333333332/scan0012.pdf',
     '11111111-1111-1111-1111-111111111111', now(), now())
on conflict (id) do nothing;

-- ---- demo roadmap (per-student copy owned by the demo host) ---------------
insert into public.roadmap_items (id, room_id, owner_id, order_index, topic, description, subtopics, completed)
values
    ('44444444-4444-4444-4444-444444444441', '22222222-2222-2222-2222-222222222222',
     '11111111-1111-1111-1111-111111111111', 0, 'Introduction to Statistics',
     'Why we study statistics and the vocabulary we use.', '["Populations and samples","Variables","Data types"]'::jsonb, true),
    ('44444444-4444-4444-4444-444444444442', '22222222-2222-2222-2222-222222222222',
     '11111111-1111-1111-1111-111111111111', 1, 'Mean, Median, Mode',
     'Measures of central tendency and when to use each.', '["Mean","Median","Mode","Outliers"]'::jsonb, false),
    ('44444444-4444-4444-4444-444444444443', '22222222-2222-2222-2222-222222222222',
     '11111111-1111-1111-1111-111111111111', 2, 'Probability',
     'Basic probability rules and worked examples.', '["Sample space","Addition rule","Multiplication rule"]'::jsonb, false),
    ('44444444-4444-4444-4444-444444444444', '22222222-2222-2222-2222-222222222222',
     '11111111-1111-1111-1111-111111111111', 3, 'Normal Distribution',
     'Properties of the normal curve and the empirical rule.', '["Z-scores","Empirical rule"]'::jsonb, false)
on conflict (id) do nothing;

-- ---- demo reviewer --------------------------------------------------------
insert into public.reviewers (id, room_id, owner_id, sections)
values ('55555555-5555-5555-5555-555555555551', '22222222-2222-2222-2222-222222222222',
        '11111111-1111-1111-1111-111111111111',
        jsonb_build_object('sections', jsonb_build_array(
            jsonb_build_object(
                'title', 'Important Concepts', 'kind', 'CONCEPTS',
                'content', jsonb_build_array(
                    'A population is the full set; a sample is a subset of it.',
                    'Descriptive statistics summarize data; inferential statistics generalize from a sample.'
                )),
            jsonb_build_object(
                'title', 'Definitions', 'kind', 'DEFINITIONS',
                'content', jsonb_build_array(
                    'Mean: the sum of values divided by how many there are.',
                    'Median: the middle value when the data is ordered.',
                    'Mode: the value that appears most often.'
                )),
            jsonb_build_object(
                'title', 'Formulas', 'kind', 'FORMULAS',
                'content', jsonb_build_array(
                    'Mean = (sum of x) / n',
                    'P(A or B) = P(A) + P(B) - P(A and B)',
                    'P(A and B) = P(A) * P(B) when A and B are independent'
                ))
        )))
on conflict (id) do nothing;

-- ---- demo published quiz + answer key (host-only via RLS) -----------------
insert into public.quizzes (id, room_id, title, type, status, question_count, created_by, review_confirmed)
values ('66666666-6666-6666-6666-666666666661', '22222222-2222-2222-2222-222222222222',
        'Probability Quick Check', 'quiz', 'PUBLISHED', 3,
        '11111111-1111-1111-1111-111111111111', true)
on conflict (id) do nothing;

insert into public.quiz_questions (id, quiz_id, order_index, type, prompt, options, correct_answer, explanation, source_resource_name)
values
    ('77777777-7777-7777-7777-777777777771', '66666666-6666-6666-6666-666666666661', 0, 'multiple_choice',
     'Which measure of central tendency is most affected by outliers?',
     '["Mean","Median","Mode"]'::jsonb, 'Mean',
     'The mean uses every value, so extreme values pull it away from the centre of the data.',
     'Descriptive Statistics - Basics.pdf'),
    ('77777777-7777-7777-7777-777777777772', '66666666-6666-6666-6666-666666666661', 1, 'true_false',
     'For independent events, P(A and B) = P(A) x P(B).',
     '["True","False"]'::jsonb, 'True',
     'Independence means the occurrence of one event does not change the probability of the other.',
     'Probability - Basic Rules.pdf'),
    ('77777777-7777-7777-7777-777777777773', '66666666-6666-6666-6666-666666666661', 2, 'identification',
     'What do we call the value that appears most frequently in a data set?',
     '[]'::jsonb, 'Mode',
     'The mode is the most frequent value; a data set can have more than one mode.',
     'Descriptive Statistics - Basics.pdf')
on conflict (id) do nothing;
