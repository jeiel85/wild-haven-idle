# CHANGELOG.md

## Unreleased - 2026-05-14

### Added
- 홈에 *지금 추천* 카드 추가: 사용자가 다음에 무엇을 하면 좋은지 한 가지를 강조 표시. 보호구역 확장이 가능하면 우선, 그 다음 회복 비용 대비 생산량 증가량(ROI)이 가장 좋은 동물, 둘 다 불가하면 가장 가까운 다음 행동까지 대기 시간 안내.
- `RecommendedAction` sealed 인터페이스 (`UpgradeSanctuary` / `SupportRecovery` / `WaitForNext`)와 `BuildHomeUiStateUseCase`의 추천 우선순위 계산.

### Changed
- `HomeUiState.recommendedAction` 추가, 홈에서 `CarePointPanel` 바로 아래·`MilestoneCard` 위에 추천 카드 노출.

### Verification
- `./gradlew :app:compileDebugKotlin` 성공
- `./gradlew :app:testDebugUnitTest` 성공 (BuildHomeUiStateUseCase 4건 추가, 총 7건)

## v0.4.0 - 2026-05-14

### Added
- AI 이미지 서비스 잠금: OpenAI gpt-image-1 / DALL·E 3 (ChatGPT Plus) — 사용자의 ChatGPT/Gemini 두 구독으로 시연(bake-off) 후 톤 일치도와 일관성에서 DALL·E 3가 우세하여 잠금 결정 (`docs/assets/bakeoff/rabbit_001-bakeoff.md`).
- 동물 5종 AI 생성 일러스트 전면 도입: `wh_animal_{rabbit,fox,deer,owl,lynx}_001.png` (각 512×512, 191~219 KB) — `AnimalIllustration`이 ID 매핑으로 모두 PNG 표시.
- 보호구역 헤더 일러스트 도입: `wh_habitat_forest_001.png` (가로 풀폭 960×540, 395 KB) — 동물 5종과 같은 시리즈 톤의 자연관찰노트 풍 풍경.
- 5종 프롬프트 파일 (`docs/assets/prompts/wh_animal_*_001.txt`) — Subject 슬롯만 다르고 Style/Palette/Negative는 동일하게 잠가 시리즈 톤 일관성 확보.

- 디자인 토큰 시스템: `WildHavenTheme`이 컬러/타이포그래피/Shape/Spacing/Elevation 토큰을 한 곳에서 주입
  - `WildHavenColors`: 의미 색상(보호구역 톤, 희귀도) — `WildHavenTheme.colors.rarityRare` 형태로 접근
  - `WildHavenTypography`: 큰 숫자/카드 타이틀/라벨 weight·letterSpacing 조정
  - `WildHavenShapes`: extraSmall(4) → extraLarge(20) 5단계 라운딩
  - `WildHavenSpacing`: T-shirt 스케일 + `cardPadding`/`cardGap`/`screenPadding` 의미 별칭
  - `WildHavenElevation`: none/sm/md/lg 4단계 tonalElevation 토큰

### Changed
- HomeScreen, OnboardingOverlay, SettingsScreen, ArchiveScreen, SanctuaryHeader가 토큰만 참조하도록 정리 (하드코딩 `RoundedCornerShape(N.dp)`, `tonalElevation = N.dp`, 주요 padding/spacing 제거)
- `presentation/components/WildlifeIllustration.kt`의 `AnimalIllustration`이 동물 ID → drawable 리소스 매핑(`animalDrawableMap`)으로 5종 모두 PNG 표시. 모든 호출부가 기본값만 사용해 `color` 파라미터를 제거. 매핑되지 않은 ID는 빈 박스로 폴백. `AnimalSilhouette`(LOCKED 상태)은 그레이 통일을 위해 절차적 Canvas 유지.
- `presentation/components/SanctuaryHeader.kt`의 절차적 풍경 캔버스(태양/구름/나무/풀/꽃을 매 프레임 그리던 `drawScenery`/`computeTrees`/`computeFlowers`/`drawCloud`/`Tree`/`Flower`)를 모두 제거하고 PNG 한 장으로 교체. 동물 산책 스프라이트, 보상 플로터, Lv 라벨, 탭 제스처는 그대로 유지. 결과: 헤더 시각 인상 대폭 향상 + Compose 매 프레임 캔버스 드로잉 부담 감소.

### Removed
- `SanctuaryHeader`의 절차적 풍경 코드 약 110줄 (헤더 PNG 도입으로 불필요).
- 보호구역 Lv별 *나무 수 동적 변화* 시각 효과는 사라짐 — 후속 작업으로 Lv별 헤더 PNG 변형(예: 묘목→성숙림) 검토 가능.

### Build / CI
- `versionCode` 3 → 4, `versionName` 0.3.0 → 0.4.0
- 한국어/영어 Play Store 출시 노트: `store-release-notes/v0.4.0.txt`

### Verification
- `./gradlew :app:compileDebugKotlin` 성공
- `./gradlew :app:testDebugUnitTest` 성공 (8건)
- `./gradlew :app:bundleRelease` 로컬 서명 성공
- `jarsigner -verify app-release.aab` → `jar verified.`
- 양자화 후 6장 PNG(동물 5종 + 헤더 1장) 시각 회귀 확인 (Read 툴)
- 실기기/에뮬레이터 수동 시각 확인은 수행하지 않음

### Documentation
- `docs/ART_DIRECTION.md` §8.1에 AI 서비스 잠금 결정(DALL·E 3) 명시.
- `docs/ASSET_LICENSES.md` §2에 rabbit_001 메타(서비스/모델/프롬프트 SHA-256/생성일/수정 내역) 한 줄 추가.
- `docs/assets/bakeoff/rabbit_001-bakeoff.md` 평가 결과·결정 기록.

### Verification
- `./gradlew :app:compileDebugKotlin` 성공
- `./gradlew :app:testDebugUnitTest` 성공 (8건)
- 시각 출력은 같은 값에서 토큰 경유로만 바뀜 — 회귀 위험 낮음

## v0.3.0 - 2026-05-14

### Added
- 첫 사용자 온보딩 오버레이: 신규 데이터에서만 1회 노출되는 3-step 환영 가이드 (보호 포인트 자동 누적, 보호구역 탭 보상, "다음 해금" 카드 안내) — `OnboardingOverlay`
- 상용화 개선 계획 문서 추가: `docs/COMMERCIALIZATION_PLAN.md` (Phase 1/2/3 우선순위와 비목표 명시)
- 한국어/영어 Play Store 출시 노트: `store-release-notes/v0.3.0.txt`

### Changed
- `GameState`에 `onboardingCompleted` 플래그 추가
- `HomeUiState.showOnboarding`을 추가하고, `BuildHomeUiStateUseCase`에서 신규 데이터일 때만 true로 전파
- `HomeViewModel.completeOnboarding()`이 `GameRepository.markOnboardingCompleted()`를 호출하도록 추가
- 설정 화면 표기를 v0.3.0으로 갱신

### Migration
- `GameStateDataStore`: `onboarding_completed` 키가 없는 기존 저장 데이터는 `onboardingCompleted = true`로 읽어 들여, 이미 게임에 익숙한 기존 사용자에게 환영 오버레이가 다시 뜨지 않도록 한다. 신규 설치(저장 데이터 없음)는 `false`로 시작해 한 번 노출 후 저장한다.

### Build / CI
- `versionCode` 2 → 3, `versionName` 0.2.0 → 0.3.0

### Verification
- `./gradlew :app:compileDebugKotlin` 성공
- `./gradlew :app:testDebugUnitTest` 성공 (신규 `BuildHomeUiStateUseCaseTest` 3건 포함)
- `./gradlew :app:bundleRelease` 로컬 서명 성공
- `jarsigner -verify -strict app-release.aab` 성공
- 실기기/에뮬레이터 수동 확인은 이번 작업에서 수행하지 않음

## v0.2.0 - 2026-05-13

### Added
- 메인 화면에 인라인 업그레이드 통합: 보호구역 확장과 동물 회복 지원을 한 화면에서 처리 (별도 "구조 동물" 화면 제거)
- 다음 해금 진척률 카드: 미해금 동물의 잠금 해제 조건(포인트/회복단계/생산량/보호종 수) 기반 진행률 표시
- 헤더 보호구역 탭 인터랙션: 탭 1회당 `max(1, 초당 생산량)` 만큼 즉시 보상 + 탭 위치에서 `+N` 플로팅 텍스트
- 보호한 동물 산책 애니메이션: 헤더에서 보호 동물 일러스트가 좌우 산책 + 상하 보빙 + 방향 반전
- 보호구역 성장 시각화: 보호구역 레벨에 따라 추가 나무·들꽃이 결정적으로 늘어남
- 헤더 환경 디테일: 태양 글로우, 흘러가는 구름, 나무 흔들림
- 보호 포인트 카운터 보간 애니메이션 (`animateFloatAsState`)
- 구매 가능한 업그레이드/회복 버튼 펄스 효과 (1.0 ↔ 1.04 스케일)

### Changed
- HomeUiState에 `sanctuaryUpgradeCost`, `animals`, `protectedAnimalIds`, `nextUnlock`, `tapReward` 통합
- BuildHomeUiStateUseCase에서 동물 회복 항목과 다음 해금 진척률 계산 일원화
- HomeViewModel에 `upgradeSanctuary()`, `supportRecovery(id)`, `tapSanctuary()` 액션 추가
- 네비게이션에서 `ANIMALS` 라우트 제거, 홈 → 도감/설정만 유지

### Fixed
- Android 15+ 강제 edge-to-edge 환경에서 펀치홀/노치 유무에 따라 상단바와 콘텐츠가 겹치는 정도가 달라지던 문제: `MainActivity.enableEdgeToEdge()` 적용 + 모든 화면 루트에 `Modifier.windowInsetsPadding(WindowInsets.safeDrawing)` 적용

### Removed
- `AnimalListScreen`, `AnimalListViewModel`, `AnimalListViewModelFactory`, `AnimalListUiState` 4파일과 `presentation.animals` 패키지 제거 (홈 화면으로 통합됨)

### Verification
- `./gradlew :app:compileDebugKotlin` 성공
- `./gradlew :app:testDebugUnitTest` 성공

## v0.1.0 - 2026-05-13

### Added
- Wild Haven Idle 프로젝트 초기 설계 문서 추가
- IP 및 윤리 정책 문서 추가
- AI 에이전트 작업 규칙 추가
- MVP 범위와 Android 기술 구조 정의
- 공개 GitHub 저장소 문서 구조 정의

### Documentation
- README, AGENTS, GAME_DESIGN, TECH_SPEC, BALANCE, ART_DIRECTION, IP_AND_ETHICS 문서 구조 정의
- 작업 이력과 의사결정 기록을 위한 `.agent` 문서 구조 정의
- GitHub 공개 페이지용 README를 게임 소개, MVP 상태, 기술 스택, 빌드/릴리즈, 문서 링크 중심으로 개편

### Verification
- 문서 패키지 생성 확인
- Android 빌드 및 테스트는 아직 실행하지 않음

## Unreleased - 2026-05-13

### Added
- Android Compose 프로젝트 초기 골격 추가
- MVP 도메인 모델, 구조 동물 정의, BalanceCalculator, 홈 화면 초안 추가
- BalanceCalculator 단위 테스트 추가
- Gradle Wrapper와 Android 프로젝트용 `.gitignore` 추가
- DataStore Preferences 기반 GameState 저장/로드 추가
- 홈 화면 보호 포인트 자동 증가와 오프라인 보상 안내 추가
- 게임 정체성을 드러내는 앱 런처 아이콘 추가
- Compose Navigation (navigation-compose 2.9.0) 추가 및 화면 간 이동 구현
- 구조 동물 목록 화면: 회복 단계 상승, 보호구역 업그레이드 기능
- 도감 화면: LOCKED/DISCOVERED/PROTECTED 상태별 동물 정보 표시
- 설정 화면: 데이터 초기화 (확인 대화상자 포함), 버전 정보
- 해금 조건 자동 체크: 상태 변경 시마다 신규 동물 해금 확인
- GameRepository에 supportAnimalRecovery, upgradeSanctuary, resetData 메서드 추가

### Build / CI
- AGP 9.2.0, Gradle 9.4.1, Compose BOM 2026.04.01 기반으로 빌드 설정 구성
- GitHub Actions에서 Android SDK 플랫폼과 빌드 도구를 설치하도록 구성
- AndroidX DataStore 1.2.1과 Lifecycle ViewModel 2.10.0 의존성 추가
- Linux CI에서 `./gradlew`를 실행할 수 있도록 Gradle Wrapper 실행 권한 설정
- 환경 변수 기반 release signing config 추가
- GitHub Actions PR/main CI와 수동/태그 릴리즈 워크플로에서 signed release APK/AAB 생성 추가
- GitHub Actions Node.js 20 deprecation 경고 대응을 위해 workflow action 버전 갱신

### Documentation
- GitHub Pages용 정적 브랜딩 페이지 추가
- GitHub Pages 상단 내비게이션과 히어로에 APK 다운로드 CTA 추가

### Build / CI (Releases)
- 태그 `v*` 푸시 시 GitHub Release를 생성하고 signed APK/AAB를 자동 첨부하도록 Release workflow 확장

### Verification
- `.\gradlew.bat test` 실행 성공
- `.\gradlew.bat assembleDebug` 실행 성공
- `.\gradlew.bat test assembleRelease bundleRelease` 실행 성공
- `apksigner verify --verbose --print-certs app-release.apk` 실행 성공
- `jarsigner -verify app-release.aab` 실행 성공
- GitHub Actions 1차 실행은 `./gradlew` 실행 권한 문제로 실패하여 수정 후 재실행
- GitHub Actions 2차 실행 성공
- GitHub Actions Android CI에서 signed release APK/AAB 생성 및 artifact 업로드 성공
- 동물 회복 목록, 도감, 설정 화면 및 네비게이션 빌드/테스트 검증 완료
