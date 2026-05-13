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
