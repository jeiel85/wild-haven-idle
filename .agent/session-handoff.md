# Session Handoff

## 2026-05-23

### Current branch / state

- Branch: `main` (design/renew는 fast-forward 머지 완료)
- 최신 커밋: `275b685 Merge branch 'design/renew' into main: v0.6.0 디자인 셸 도입`
- 태그: `v0.6.0` 푸시 완료
- Working tree at handoff: clean

### v0.6.0 산출물 (모두 준비 완료, Play Store 업로드만 보류)

- AAB (서명·R8 활성): `app/build/outputs/bundle/release/app-release.aab` (4.89 MB, versionCode=7)
- APK (서명): `app/build/outputs/apk/release/app-release.apk` (3.2 MB)
- 출시 노트: `store-release-notes/v0.6.0.txt` (ko 281 / en 444)
- 바탕화면 복사본: `C:\Users\jeiel\OneDrive\바탕 화면\wild-haven-idle-v0.6.0.aab`, `wild-haven-idle-v0.6.0-release-notes.txt`
- 폰 실기기 6장 스크린샷: `store-graphics/v0.6.0-device-shots/` (1080×2340, 480dpi, Samsung 시리얼 R3CWC0KB53Z)
- GitHub Actions release workflow: v0.6.0 태그로 트리거 → 진행 상황은 `gh run list --limit 5`로 확인

### 🚧 차단된 항목 — Play Store 업로드 (사용자 결정 필요)

Chrome MCP로 https://play.google.com/console 직접 확인 결과:

- 로그인 계정: `jeiel85@gmail.com`
- 연결된 개발자 계정 셀렉터에 표시되는 유일한 항목: `Yongeun Park` (developer ID `4685898627432283006`)
- 정책 상태: **🔴 계정 해지** (2021-10-20 사용 중지, "활동이 없어 해지되었으며 재활성화할 수 없습니다")

→ 이 Google 계정으로는 Play Store 업로드 자체가 막힘. 자동 진행은 더 이상 불가.

### 사용자 결정이 필요한 옵션

1. 다른 Google 계정에 활성 Play 개발자 계정이 있는 경우 → 해당 계정으로 Chrome 로그인 후 다음 세션에서 재시도 (말씀해주시면 Chrome MCP로 바로 업로드 자동화 가능)
2. 새 Play 개발자 계정 등록 → https://play.google.com/console/signup ($25 + 본인 인증 + 정책 검토)
3. Play Store 출시 보류 → GitHub Release만 활용

### v0.5.1 Play Store 등재 여부 의문 (확인 필요)

- 폰의 v0.5.1 firstInstallTime이 2026-05-14 → Play Store가 아닌 ADB 사이드로드 또는 GitHub Release APK 직접 설치였을 가능성. v0.5.1까지 실제로 Play Store에 올라간 적 있는지 사용자 확인 필요.

### v0.6.1+ 권장 작업 (디자인 셸 → 기능 통합)

- mock `WildHavenViewModel` (`com.jeiel85.wildhavenidle.renew.MainActivity`)을 기존 `HomeViewModel`/`ArchiveViewModel`/`GameRepository`/`GameStateDataStore`로 교체
- `BalanceCalculator`/`DailyBonus`/`BuildHomeUiStateUseCase` 호출 복원
- `SanctuarySettingsScreen`의 mock 버전 표기 `v2.1.0` → `BuildConfig.VERSION_NAME` 바인딩
- 5종 PNG 일러스트(rabbit/fox/deer/owl/lynx) 및 헤더 PNG를 새 디자인에 통합할지 / `repurpose`할지 결정

## 2026-05-15

### Current branch

- Branch: `main`
- Base: `origin/main`
- Working tree at handoff: documentation-only design rescue notes expected

### Design rescue brief

사용자 피드백: 현재 디자인 인상이 좋지 않고, 이미 도입한 이미지 에셋을 어떻게 처리할지 다음 세션에서 이어서 판단해야 한다.

핵심 판단:

- 기존 이미지 에셋을 전부 폐기하지 않는다.
- 먼저 "에셋 자체 문제"와 "UI 배치/위계 문제"를 분리한다.
- 현재 도입된 동물 5종 PNG와 보호구역 헤더 PNG는 톤 일관성이 있어 우선 `keep` 후보로 본다.
- 다음 세션의 1순위는 새 에셋 생성이 아니라 홈 화면의 카드/버튼/정보 위계를 정리하는 Phase 0.5 P4다.
- 메인 화면에서 체감이 나빠 보이는 원인은 이미지보다 카드 밀도, 배경 대비, 버튼 위계, spacing, 진행률 타이포가 서로 경쟁하는 쪽일 가능성이 크다.

### Recommended next task

`Commercialization Phase 0.5 P4: Card/button micro-polish`

작업 목표:

- 홈 첫 화면에서 `현재 보호구역 상태`, `보호 포인트`, `추천 행동`, `일일 보상`, `다음 해금/회복`의 우선순위가 한눈에 읽히게 만든다.
- 기존 PNG 에셋은 유지하고, 카드 구조/spacing/색 대비/버튼 위계부터 정리한다.
- 전면 리디자인이나 전체 에셋 교체는 하지 않는다.

권장 진행 순서:

1. 홈 화면 스크린샷 또는 Compose Preview 기준으로 현재 UI를 점검한다.
2. `HomeScreen.kt`의 카드 순서와 각 카드의 시각 위계를 확인한다.
3. `WildHavenTheme` 토큰만 사용해 카드 배경, elevation, 라운딩, padding, label/body/value 타이포를 정리한다.
4. 버튼은 주 행동 1개만 강하게 보이도록 하고, 보조 행동은 낮은 강조로 둔다.
5. 동물 PNG와 헤더 PNG는 유지한다. 문제가 확인된 개별 에셋만 `repurpose` 또는 `replace` 후보로 기록한다.
6. 360dp Preview와 일반 Preview에서 텍스트 겹침, 버튼 잘림, 이미지 과밀을 확인한다.

### Asset triage rule

다음 세션에서 에셋을 다룰 때는 모든 이미지를 아래 셋으로 분류한다.

- `keep`: 동물/서식지 정체성에 맞고, 스타일 일관성이 있으며, 메인 화면에 계속 사용 가능.
- `repurpose`: 메인 화면에서는 과하지만 도감, 보상, 보조 카드, 마케팅 그래픽으로 활용 가능.
- `replace`: 그림체가 튀거나, 전자펫/기존 IP 인상이 있거나, 작은 크기에서 읽히지 않거나, UI를 지저분하게 만드는 경우.

현재 잠정 분류:

- `keep`: `wh_animal_rabbit_001.png`, `wh_animal_fox_001.png`, `wh_animal_deer_001.png`, `wh_animal_owl_001.png`, `wh_animal_lynx_001.png`, `wh_habitat_forest_001.png`
- `review`: 앱 아이콘, GitHub Pages/스토어 그래픽은 실제 기기 화면과 함께 재점검
- `replace`: 아직 확정 없음

### Guardrails

- 금지 표현/전자펫 연상 표현을 추가하지 않는다.
- 네트워크, 광고, 분석, 결제, 로그인, 클라우드, 푸시 권한은 추가하지 않는다.
- 새 AI 에셋이 필요한 경우 `docs/ART_DIRECTION.md` §8~§10과 `docs/ASSET_LICENSES.md` 기록 절차를 따른다.
- P4는 UI 마이크로 폴리시 작업이므로 대규모 레이아웃 재작성, 전체 테마 교체, 기능 추가는 피한다.
- 디자인 변경 후 최소 `./gradlew :app:compileDebugKotlin`과 `./gradlew :app:testDebugUnitTest`를 실행한다.

## 2026-05-13

### Current branch

- Branch: `codex/release-signing-icon`
- Base: `main`
- PR: https://github.com/jeiel85/wild-haven-idle/pull/2
- Latest commit: `757bddf docs: GitHub README 개편`
- Working tree: clean at handoff

### Open PRs

1. PR #1: `codex/feat-datastore-game-loop`
   - URL: https://github.com/jeiel85/wild-haven-idle/pull/1
   - Scope: DataStore save/load, home ticking loop, offline reward dialog
   - CI: passed
   - Note: this branch is separate from PR #2.

2. PR #2: `codex/release-signing-icon`
   - URL: https://github.com/jeiel85/wild-haven-idle/pull/2
   - Scope: release signing, signed APK/AAB CI artifact, app icon, README rewrite, Node 24 Actions update
   - CI: passed
   - Artifact: `wild-haven-idle-release`

### Release signing backup

Release signing files were generated outside the repository and must stay private.

```text
D:\Project\wild-haven-idle-release-backup\20260513-132122
```

Important files:

```text
wild-haven-release.jks
release-signing.env
wild-haven-release.jks.base64.txt
```

GitHub Actions Secrets registered:

```text
ANDROID_RELEASE_KEYSTORE_BASE64
ANDROID_RELEASE_KEYSTORE_PASSWORD
ANDROID_RELEASE_KEY_ALIAS
ANDROID_RELEASE_KEY_PASSWORD
```

### Completed in this session

- Created Android release keystore outside Git.
- Registered release signing secrets in GitHub.
- Added signed release APK/AAB build to Android CI.
- Added manual/tag-based Android Release workflow.
- Generated and wired the app launcher icon.
- Updated GitHub Actions to newer Node 24-compatible action versions.
- Rewrote `README.md` for GitHub project presentation.
- Verified CI after the latest README commit.
- Added `docs/index.html` as a GitHub Pages branding page for the project.

### Suggested next steps

1. Decide whether to merge PR #1 first, then rebase or merge PR #2 after it.
2. After PR #1 is merged, continue MVP gameplay work:
   - animal recovery list
   - sanctuary upgrade action
   - archive screen
   - settings screen
   - data reset action
3. After core MVP screens exist, create final Play Store listing assets:
   - real screenshots
   - feature graphic
   - short and long descriptions
   - data safety / privacy policy materials
4. After PR #2 is merged, confirm the GitHub Pages branding page at:
   - https://jeiel85.github.io/wild-haven-idle/

### Cautions

- Do not commit files from `D:\Project\wild-haven-idle-release-backup`.
- Do not print or paste release keystore passwords into chat, logs, or docs.
- MVP policy still excludes network permission, login, ads, analytics, billing, cloud sync, and push notifications.
