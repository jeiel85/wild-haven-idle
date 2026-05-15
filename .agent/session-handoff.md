# Session Handoff

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
