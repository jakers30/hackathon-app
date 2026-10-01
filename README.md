# AI-Powered Collaborative Study Room

Base app for the RAITE 2026 hackathon (spec: `studyroom-hackathon-spec (1).md`).
A native Android app where hosts upload study resources and an AI turns them into
a **study roadmap**, a **reviewer**, and **draft quizzes** that the host reviews
before publishing. Offline-first: Room (SQLite) is the source of truth.

## Stack

| Layer | Choice |
|---|---|
| UI | Kotlin + Jetpack Compose + Material 3 (60-30-10 palette, light/dark) |
| Architecture | MVVM, Hilt, Coroutines/Flow, Navigation Compose, WindowSizeClass-ready |
| Local DB | Room (SQLite) + outbox table |
| Background sync | WorkManager (network constraint + exponential backoff) |
| Cloud DB | Supabase PostgreSQL + Row Level Security |
| Auth | Supabase Auth (`supabase-kt`), session cached for offline open |
| Files | Supabase Storage (downloaded to app storage for offline use) |
| AI proxy | Ktor (Kotlin) service, deployable to Cloud Run |
| AI model | Gemini or Claude (env-selected) |
| Images | Coil · Settings | Jetpack DataStore |

**The AI key never ships in the app.** The app calls only the proxy, which holds
the key, verifies the Supabase token, and confirms room access before reading
any resource (spec section 2).

## Project structure

```
/app                      Android app (Kotlin, Jetpack Compose)
  src/main/java/com/raite/studyroom
    /ui/theme             Color, Type, Shape, Theme   (60-30-10 palette)
    /ui/components        Skeleton, OfflineBanner, RoomCard, TaskRow, ...
    /ui/screens           intro, auth, dashboard, tasks, rooms, room, settings
    /ui/navigation        Routes, AppNavHost
    /data/local           Room DB, entities, DAOs, outbox, mappers
    /data/remote          Supabase sources + AI proxy client + DTOs
    /data/repository      offline-first repositories
    /data/sync            SyncWorker + SyncScheduler
    /domain               models + enums
    /di                   Hilt modules
    /util                 Time, Ids, Result, FileMeta, AppJson
/ai-proxy                 Ktor AI service (routes, prompts, auth, providers)
/supabase                 schema.sql, policies.sql, seed.sql
.env.example              secrets template
```

## Prerequisites

- Android Studio (Ladybug or newer)
- **JDK 17 or 21** for both the app and the proxy. AGP 8.7 / Gradle 8.9 accept
  Java 17-22; **Android Studio's bundled JBR (25.x) is too new** and Gradle fails
  with a bare version-number error. Point `JAVA_HOME` at a JDK 17/21 instead, e.g.
  `JAVA_HOME=%USERPROFILE%\.jdks\jbr-21.0.11`.
- A Supabase project
- Node/Docker optional

## 1. Supabase setup

In the Supabase SQL editor, run in order:

1. `supabase/schema.sql` — tables, indices, storage bucket, new-user trigger
2. `supabase/policies.sql` — Row Level Security (room access enforced server-side)
3. `supabase/seed.sql` — demo host + a public "Intro to Statistics" room

> For the hackathon demo, disable **Authentication → Email confirmations** so
> registrations sign in immediately. The seed creates `demo@studyroom.app` /
> `demo1234` (if the auth insert is skipped, create the account in the dashboard
> and re-run the seed's data section with your own uuid).

## 2. Configure the app

Create `local.properties` in the project root (never committed):

```properties
SUPABASE_URL=https://YOUR-PROJECT.supabase.co
SUPABASE_ANON_KEY=your-anon-public-key
AI_PROXY_URL=http://10.0.2.2:8080        # emulator -> your local proxy; use the Cloud Run URL for the demo
```

The anon key is safe to ship — RLS protects the data. The **service-role key is
never** placed in the app.

## 3. Run the AI proxy

```bash
cd ai-proxy
cp .env.example .env          # fill SUPABASE_*_KEY and your AI key
gradle run                    # runs on http://localhost:8080 (health: GET /health)
```

Deploy to Cloud Run (Google Cloud is on RAITE's allowed list):

```bash
gcloud run deploy study-room-ai-proxy --source ai-proxy --allow-unauthenticated \
  --set-env-vars SUPABASE_URL=...,SUPABASE_ANON_KEY=...,SUPABASE_SERVICE_ROLE_KEY=...,AI_PROVIDER=gemini,GEMINI_API_KEY=...
```

Proxy endpoints: `POST /ai/rename`, `/ai/roadmap`, `/ai/reviewer`, `/ai/modify`, `/ai/quiz`.
Every request carries the user's Supabase token; the proxy verifies it and checks
room access before touching any file.

## 4. Build and run the app

Open the project root in Android Studio and run on a device/emulator (minSdk 24).
From the command line, after generating the Gradle wrapper
(`gradle wrapper --gradle-version 8.9`) or opening once in Android Studio:

```bash
./gradlew :app:assembleDebug      # build the demo APK
./gradlew :app:installDebug       # install on a connected device
```

> The `gradle-wrapper.jar` is binary and is not committed here; Android Studio
> generates it on first sync, or run `gradle wrapper` with a local Gradle install.

## Feature map (spec section 11)

- **Auth** — register/login with name, email, password; cached session opens offline
- **Dashboard** — My Tasks (add/toggle/delete, priorities, deadlines) + My Rooms
- **Rooms** — create (public / private / specific users, invite code), join by code
- **Room** — Resources (upload with AI auto-rename, download for offline, delete),
  Materials (roadmap + reviewer with "AI-generated" labels, roadmap completion),
  Quizzes (host generates drafts, reviews, confirms and publishes)
- **AI Assistant** — prompt bar that modifies the roadmap/reviewer; disabled offline
- **Offline-first** — Room is the source of truth; outbox + WorkManager sync;
  offline banner; per-item sync indicators; skeletons on first load and during AI

## Responsible AI & privacy

- Every generated roadmap/reviewer shows **"AI-generated, please verify with your sources"**
- Quiz drafts stay host-only and publishing requires the review checkbox
- Upload notice: "Only upload materials you own or have permission to share."
- RLS restricts every room request to its host, members, or allowed users
- Only name + email are collected; Supabase hashes passwords

## Known limitations / next steps

- Quiz-answer protection is enforced server-side by making `quiz_questions`
  readable **only by the host**. Student quiz-taking (optional, MEDIUM priority in
  the spec) needs an answer-free projection/RPC before it can ship.
- Resource files are uploaded by the host; the demo seed only creates metadata
  rows, so the proxy skips files it cannot download (rename falls back to the
  original name, per spec section 3.4).
- Documented offline scope (spec section 6): tasks and roadmap completion are
  fully editable offline; downloads, generation and publishing need a connection.
- TODO for polish: bundle Poppins/Inter `.ttf` files in `res/font` (the type scale
  is already wired in `ui/theme/Type.kt`).
