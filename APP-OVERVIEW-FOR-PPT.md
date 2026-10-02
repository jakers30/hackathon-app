# AI-ral — Presentation Outline (slide-by-slide)

> Everything below is taken from the actual repo (`hackathon-app/`), not from marketing copy.
> Each slide has **Title · Bullets · Visual · Speaker notes**. Keep ~30 words per bullet and one
> visual per slide: the bullets are what the judges read, the notes are what you say.

---

## Slide 1 — Title
**AI-ral: the AI study room that keeps working offline**

- RAITE 2026 Hackathon — native Android app
- Hosts upload study material → AI builds the roadmap, the reviewer and draft quizzes
- Offline-first: the phone's own database is the source of truth
- Kotlin + Jetpack Compose · Supabase (Postgres/Auth/Storage) · Ktor AI proxy

*Visual:* app icon + one phone mockup of the Dashboard.
*Notes:* one sentence on the name — the product is "AI-ral"; everything internal is still `com.raite.studyroom`.

---

## Slide 2 — The problem
**Study groups run on messy group chats**

- Files scattered across a chat thread: no structure, nothing to revise from
- Reviewers and roadmaps are made by hand the night before an exam
- Campus Wi-Fi and mobile data are unreliable — cloud-only apps simply stop
- Handing every student an AI API key is neither safe nor affordable

*Visual:* three icons — chat pile · blank reviewer page · no-signal bar.
*Notes:* the pain is organisation **and** reliability, not "we need more AI".

---

## Slide 3 — The solution
**One room: resources in, structured study out**

- A **room** holds resources, a roadmap, a reviewer and quizzes for one subject
- AI reads the uploaded files and produces the material, always labelled AI-generated
- Quizzes stay **host-only drafts** until the host reviews and publishes them
- Students keep studying with no connection; writes are queued and synced later

*Visual:* horizontal pipeline `Resources → AI → Roadmap / Reviewer / Quiz → Students`.
*Notes:* stress the review gate — AI drafts, a human publishes.

---

## Slide 4 — Two personas, two jobs
**Host (teacher / group leader) · Student**

| Host | Student |
|---|---|
| Create room (public / private / specific users) | Join with an invite code |
| Upload resources, delete them | Download for offline reading |
| Generate + review + publish quizzes | Tick off roadmap topics |
| Delete the room (confirmed, irreversible) | — |

*Visual:* two phone mockups side by side (Create Room screen vs Materials tab).
*Notes:* same app; permissions come from the server (RLS), not from the UI.

---

## Slide 5 — Feature tour (what is actually built)
- **Auth** — register/login with name+email+password, in-app 6-digit email verification, forgot-password wizard, cached session opens offline
- **Dashboard** — today's tasks + your rooms in one screen, quick-add with Low/Medium/High priority
- **My Tasks** — add/toggle/delete, priority, date+time deadline pickers, Overdue / Due soon / Upcoming badges
- **Rooms** — create public/private/specific, join by invite code, host-only delete with confirmation
- **Room** — three tabs: Resources · Materials (Roadmap + Reviewer) · Quizzes
- **AI Assistant** — prompt bar that modifies the roadmap/reviewer; disabled while offline
- **Notifications** — in-app card + system notification + exact deep link
- **Settings** — theme, AI service address, sync now, test notification, log out

*Visual:* eight small screenshots in a grid.
*Notes:* this is the breadth slide — keep it to ~40 seconds.

---

## Slide 6 — The AI pipeline (the differentiator)
**Upload → understand → generate, with the key never on the device**

- The app sends its **Supabase token + room id**; the proxy verifies the token and re-checks room access before reading any file
- `POST /ai/rename` → a short descriptive display name for the upload
- `POST /ai/roadmap` → ordered topics + subtopics (a per-student copy)
- `POST /ai/reviewer` → sections: concepts, definitions, key points, examples, formulas, notes
- `POST /ai/quiz` → multiple choice / true-false / identification / short answer, each with an explanation
- `POST /ai/modify` → the "AI Assistant" prompt rewrites the roadmap/reviewer
- Providers are env-selected: **Gemini (default), Claude, DeepSeek** — swap models without shipping a new APK

*Visual:* sequence diagram `App → Proxy (auth + room check) → Model → JSON → App → Supabase`.
*Notes:* the AI key and the Supabase **service-role** key live only on the proxy.

---

## Slide 7 — Quizzes: AI drafts, the host decides
- AI creates a quiz in **DRAFT** — students cannot see it at all
- The host opens the quiz review screen, edits the questions, and must tick the review checkbox
- Publishing flips the status to **PUBLISHED** and notifies students with a deep link
- Answer keys are readable **only by the host** (RLS on `quiz_questions`)

*Visual:* two-panel before/after — "Draft (host only)" → "Published (students)".
*Notes:* this is the responsible-AI gate the spec asks for.

---

## Slide 8 — Offline-first, explained simply
**The database on the phone is the truth; the cloud catches up**

- Every write goes to Room (SQLite) first, then into an **outbox** table
- `SyncWorker` (WorkManager, network constraint, exponential backoff) pushes the queue
- Every queued write immediately asks for a sync; success deletes the outbox row **and** marks the local row `SYNCED`
- After 4 failed attempts the row is marked `FAILED` — a real error, never a forever "Waiting to sync"
- Pull side: refreshing a room upserts what the server returns and **drops + deletes** what it no longer returns
- The UI tells the truth per item — Synced / Waiting to sync / Sync failed — plus an offline banner

*Visual:* state diagram `SYNCED ⇄ PENDING → FAILED` next to the outbox → Supabase arrows.
*Notes:* this is why you can put the demo in airplane mode mid-flow.

---

## Slide 9 — System architecture
**Three tiers, three trust levels**

- **Android app** (Kotlin, Compose, MVVM + Hilt + Coroutines/Flow) — UI, Room DB, outbox, WorkManager
- **Supabase** — PostgreSQL + Row Level Security + Auth + Storage bucket
- **AI proxy** (Ktor, standalone Gradle build, Cloud Run-ready) — holds the AI key and the service-role key
- File bytes go straight from the app to Supabase Storage; the proxy only reads them to feed the model
- 97 Kotlin files in the app · 12 in the proxy · 11 tables · 27 RLS policies

*Visual:* three-box diagram with numbered arrows (1 upload, 2 queue, 3 sync, 4 proxy call, 5 notify).
*Notes:* say *why* the proxy exists — key safety, model portability, one place to enforce room access.

---

## Slide 10 — Tech stack (the receipts)
| Layer | Choice |
|---|---|
| UI | Kotlin 2.0.21, Jetpack Compose, Material 3, Navigation Compose |
| Architecture | MVVM, Hilt 2.52, Coroutines/Flow, repository pattern |
| Local DB | Room 2.6.1 + an outbox table |
| Background | WorkManager 2.9.1 (sync + notification check) |
| Cloud | Supabase 3.0.3 (Auth, PostgREST, Storage) + RLS |
| AI service | Ktor 3.0.3, deployable to Cloud Run |
| HTTP/JSON | Ktor client + kotlinx.serialization |
| Images / settings | Coil 2.7.0 / DataStore |
| Build | AGP 8.7.2, Gradle 8.9, minSdk 24, targetSdk 35 |

*Visual:* the table plus the app icon strip.
*Notes:* every version is the exact pin in `gradle/libs.versions.toml` — judges love specifics.

---

## Slide 11 — Data model
- 11 tables: `profiles`, `rooms`, `room_members`, `room_allowed_users`, `resources`, `roadmap_items`, `roadmap_progress`, `reviewers`, `quizzes`, `quiz_questions`, `tasks`
- Every deletable table carries **`deleted_at`** (soft delete) so other devices learn about the change
- List queries filter `deleted_at is null`; deleting a room cascades to all of its children
- `handle_new_user()` trigger creates a `profiles` row for every signup
- 11 indexes on the hot paths (room→resources, room+owner→roadmap/reviewers, user→tasks)
- ISO-8601 UTC timestamps everywhere, so Postgres and Kotlin agree without conversion

*Visual:* simple ERD with `rooms` in the centre.
*Notes:* soft delete is what makes both the sync model and the housekeeping slide possible.

---

## Slide 12 — Security & responsible AI
- **RLS enforces everything server-side** — 27 policies; the UI only mirrors the rules
- Helpers `is_room_member`, `is_room_host`, `is_allowed_user` are `SECURITY DEFINER` so the checks do not recurse
- Room access = host, member or explicitly allowed user; private rooms are invisible to outsiders
- Storage policies mirror the table rules, so files are not guessable either
- The **anon key ships** (safe, RLS-protected); the **service-role key never leaves the proxy**
- Every AI output is labelled "AI-generated, please verify with your sources"
- Upload notice ("only upload what you own or may share"), host review gate before publishing, only name + email collected
- The system prompt refuses offensive/harmful requests and will not do graded assignments or exams

*Visual:* lock icon + a real policy snippet from `policies.sql`.
*Notes:* one crisp line — "even with a rebuilt APK you cannot read another room's data".

---

## Slide 13 — Housekeeping / junk management (spec §10)
**Soft deletes are useful; leftovers are not**

- Deletes are soft (`deleted_at`), so dead rows and dead Storage files accumulate over time
- `junk_report()` — a read-only report of soft-deleted rows per table plus Storage objects no live row points at
- `purge_soft_deleted(days)` — deletes soft-deleted rows older than the retention window (default 30; children first, rooms last)
- `purge-storage-orphans.ps1` — finds and deletes orphaned bucket objects (dry-run by default, `-Apply`, `-RetentionDays`); objects are removed through the Storage API so the metadata stays consistent
- App side: deleting a resource removes the file and the stored object immediately; a delete queued while offline purges them when it replays
- On refresh, cached rows the server no longer returns are pruned together with their downloaded files
- Housekeeping SQL is **service_role-only** (EXECUTE revoked from public/anon/authenticated); optional nightly `pg_cron` schedule at 03:30
- Logging out wipes the local DB, downloaded files, notifications and settings, and cancels queued sync

*Visual:* before/after bar chart (dead rows + orphan objects → 0) with the two function names.
*Notes:* this is the "it still works after the demo" slide — operational maturity, not a checkbox.

---

## Slide 14 — Design system & UX details
- Soft-pink **60-30-10** palette: 60% neutral, 30% pink accent, 10% contrast text — no neon
- Light / dark / follow-system theme, persisted; one-tap toggle on the Dashboard
- Consistent ~10dp padding and rounded corners on cards, fields, dialogs and buttons
- Show/Hide password eye + Remember me (last email pre-filled) on the login form
- Skeletons on first load and while AI is thinking; per-item sync indicators; offline banner
- In-app notification card (auto-hides after 8s) + system notifications every 15 minutes, both with deep links
- A notification opens the **exact** content — quiz → room → quiz; reviewer/roadmap → room with that section focused — even from a cold start

*Visual:* light/dark screenshot pairs plus the notification card.
*Notes:* if asked about navigation: the side bar has Home, My Task, Rooms, Settings, Log out.

---

## Slide 15 — Live demo script (~3 minutes)
1. Log in with the seed account — `demo@studyroom.app` / `demo1234`
2. Open the public room "Intro to Statistics" (invite code `DEMO01`)
3. Resources tab: upload a file → AI renames it
4. Materials tab: ask the AI Assistant to extend the roadmap → watch it sync to "Synced"
5. Quizzes tab: generate drafts → review → tick the checkbox → publish → notification arrives → tap it (deep link)
6. Airplane mode: tick roadmap topics and add a task → offline banner, "Waiting to sync" → back online → flips to "Synced"
7. Settings: pending count, Sync now, test notification, dark mode, log out

*Visual:* a rehearsal checklist, not a slide to read aloud.
*Notes:* have a backup recording; the riskiest step is the AI call, so make sure the proxy answers `GET /health` with `ok` first.

---

## Slide 16 — Impact / what is built
- A complete offline-capable loop: auth → room → upload → AI material → quiz → notification
- 97 app source files + a 12-file Ktor service + SQL/ops scripts for schema, policies, seed, verification and cleanup
- Zero AI keys on the device — one proxy, three swappable providers
- Runs on Android 7.0+ (minSdk 24); emulator, USB phone (adb reverse) and Cloud Run are all supported
- Self-verifying infrastructure: `verify-remote.ps1` checks the live project, `junk_report()` measures the junk

*Visual:* three big numbers (97 files · 27 RLS policies · 11 tables) + the small pipeline diagram.
*Notes:* every number is from the repo — safe to quote.

---

## Slide 17 — Limitations & next steps
- Student quiz-taking is not shipped yet: it needs an answer-free projection/RPC, because answer keys are host-only by design
- The seed creates resource metadata only, so the proxy skips files it cannot download (the name falls back to the original)
- Offline scope (spec §6): tasks and roadmap completion are editable offline; downloads, AI generation and publishing need a connection
- DeepSeek has no PDF input, so the proxy turns a PDF into text with Apache PDFBox (DOCX with Apache POI, TXT/CSV as-is); only a scan with no text layer is skipped
- Polish left: bundle the Poppins/Inter fonts; wire `pg_cron` for unattended nightly housekeeping

*Visual:* an honest "done / next" checklist.
*Notes:* frame these as deliberate scope, then move on — do not read them as apologies.

---

## Slide 18 — Q&A prep (the three questions you will get)
- **"What if there is no internet?"** → Room is the source of truth; the outbox + WorkManager push later, and the UI shows the true per-item state.
- **"Is the AI key safe?"** → It never ships. The app sends a Supabase token; the proxy verifies it and re-checks room access against the database before reading any file.
- **"Can one room read another room's data?"** → No. 27 RLS policies with `SECURITY DEFINER` helpers enforce it server-side, and the same rules cover the Storage bucket.
- Bonus — **"What about deleted files?"** → Soft delete for sync; `purge_soft_deleted()` plus the storage-orphan script clean up; the app deletes on every delete path.

*Visual:* none — this is your cheat sheet.
*Notes:* answer in one sentence, then stop. Let the judge ask the follow-up.

---

## Abstract (paste into the intro slide or the submission form)

AI-ral is a native Android study-room app built for the RAITE 2026 hackathon. A host creates a room
and uploads study material; a Ktor proxy — which holds every secret — reads those files, verifies the
caller's Supabase token and room access, and asks an AI model (Gemini, Claude or DeepSeek, selected
by environment variable) for a study roadmap, a structured reviewer and draft quizzes. Nothing
AI-generated reaches students until the host reviews and publishes it, and answer keys always stay
host-only. The app is offline-first: every write lands in a local SQLite database and an outbox
first, and WorkManager pushes the queue to Supabase PostgreSQL — Row Level Security on all 27 policy
paths — with exponential backoff, so studying continues with no signal. Notifications deep-link to
the exact quiz or reviewer section, even from a cold start. Rounding it out, spec section 10's
housekeeping — soft-delete purging, orphaned Storage cleanup and sign-out wipes — keeps the database
and the bucket from growing forever.


