# Decisions

## 2026-05-13: Project identity

Decision:
Wild Haven Idle is defined as a wildlife sanctuary restoration idle simulation, not a virtual pet game.

Reason:
Reduce IP risk and establish an ethical, original theme.

Impact:
Avoid terminology such as Tamagotchi, egg, hatch, feed, evolution, and pet in user-facing text.

---

## 2026-05-13: MVP technical policy

Decision:
The MVP will be local-first and will not include login, cloud sync, ads, analytics, billing, external API, or network permission.

Reason:
Reduce implementation complexity, privacy risk, policy risk, and release friction for a solo developer.

Impact:
Use DataStore for local persistence. Do not add `android.permission.INTERNET` during MVP.

---

## 2026-05-13: Agent workflow

Decision:
Use an Automation First workflow with strict stop conditions.

Reason:
The project will be developed with AI coding agents, so explicit rules are required to avoid destructive commands, policy violations, secret leakage, and scope creep.

Impact:
AGENTS.md is the single source of truth for agent workflow rules.

---

## 2026-05-13: Android scaffold tooling

Decision:
Use Android Gradle Plugin 9.2.0 with Gradle Wrapper 9.4.1, JDK 17, and the stable Compose BOM 2026.04.01.

Reason:
These versions match the current official Android and Gradle compatibility guidance for a new Compose-based Android project.

Impact:
AGP 9 uses built-in Kotlin support, so the app module does not apply the deprecated `org.jetbrains.kotlin.android` plugin.

---

## 2026-05-13: MVP persistence implementation

Decision:
Use AndroidX Preferences DataStore 1.2.1 for the first MVP save/load implementation.

Reason:
The MVP state is small, local-only, and does not require schema migration before public release.

Impact:
GameState is persisted locally without network permission, login, cloud sync, or external API integration.

---

## 2026-05-13: Release signing storage

Decision:
Store the Android release keystore outside the repository and inject signing material through GitHub Actions Secrets.

Reason:
Release keys are sensitive and must not be committed to Git, while local and CI builds must use the same signing identity.

Impact:
Local backup path is `D:\Project\wild-haven-idle-release-backup\20260513-132122`. Repository secrets are `ANDROID_RELEASE_KEYSTORE_BASE64`, `ANDROID_RELEASE_KEYSTORE_PASSWORD`, `ANDROID_RELEASE_KEY_ALIAS`, and `ANDROID_RELEASE_KEY_PASSWORD`.

---

## 2026-05-13: App icon identity

Decision:
Use a forest sanctuary shield with leaf and rescued wildlife silhouettes as the first launcher icon.

Reason:
The icon communicates sanctuary restoration and wildlife protection without referencing electronic pet devices or third-party IP.

Impact:
The generated source image is tracked under `docs/assets/wild-haven-icon-source.png`, with launcher PNGs derived into `mipmap-*` resources.

---

## 2026-05-23: Simplified launcher icon

Decision:
Replace the detailed photo-like launcher icon with a simpler geometric sanctuary shield and single large leaf mark.

Reason:
The previous icon carried the right identity but had too many background and animal details for small Android launcher sizes. The simplified mark keeps the sanctuary/leaf identity while improving readability.

Impact:
`docs/assets/wild-haven-icon-source.png` and all `mipmap-*` launcher PNGs are regenerated from the simplified source. Animal silhouettes are no longer part of the launcher icon and remain represented inside the app UI instead.
