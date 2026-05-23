# Progress

## 2026-05-23 (오후) — v0.6.1 도메인 통합

### Done

- `feat/v0.6.1-domain-integration` 브랜치 분기
- 새 디자인의 mock `WildHavenViewModel`을 `GameRepository` 기반 실 구현으로 교체
- `renew/DomainMapping.kt` 신규: `WildlifeSubject`/`ShelterRestoration` 매핑, hero emoji 5종, 환경 정비 활성 여부 판별
- `renew/MainActivity.kt`:
  - `WildHavenViewModel`: `points`/`pointsPerSec`/`wildlifeList`/`restorations`를 모두 `gameRepository.gameState` map으로 노출
  - `runPassiveTick`이 `addCarePoint(rate)`로 DataStore에 누적
  - 진입 시 `applyOfflineRewardIfNeeded` 결과를 OfflineRewardPopup 표시
  - `WildHavenViewModelFactory` 추가
  - `MainActivity.onCreate`에서 `GameRepository(GameStateDataStore, SystemTimeProvider)` 생성
  - hero 이모지 `when`을 `DomainMapping.heroEmojiFor`로 교체 (rabbit_001 등 5종 ID 매핑)
  - 설정 버전 표기 `v2.1.0` → `BuildConfig.VERSION_NAME`
  - `HabitatRestorationScreen`에서 `DomainMapping.isRestorationEnabled`로 4개 카드 분기 (첫 번째만 활성, 나머지 "준비 중")
- `app/build.gradle.kts`: versionCode 7 → 8, versionName 0.6.0 → 0.6.1, `buildConfig = true` 추가
- CHANGELOG v0.6.1 + Known Limitations
- 출시 노트 `store-release-notes/v0.6.1.txt` (ko 277 / en 439)
- 컴파일/테스트/assembleDebug/assembleRelease 모두 성공
- 실기기(Samsung) 5개 탭 + restore 화면 검증 — `store-graphics/v0.6.1-device-shots/wh61_01~05.png`

### Not Done

- 바탕화면 자동 복사 (커밋 후 자동 실행 예정)
- main 머지 + 태그 v0.6.1 푸시 (커밋 직후 자동 실행 예정)
- Play Store 업로드 (Chrome MCP로 pedaiah85 계정 전환 후 진행)

### v0.6.2+ 예정

- 회복 지원 버튼 텍스트 비용 표시 도메인 동기화
- 잠긴 동물 카드 흔적 발견 버튼 정리 또는 도메인 수동 unlock 추가
- 일일 보호 활동 보상 카드 새 디자인에 통합
- 환경 정비 4개 카테고리 도메인 확장

## 2026-05-23 — v0.6.0 릴리즈 준비 (디자인 전면 개편 셸)

### Done

- `design/renew` 브랜치 분기, 사용자가 별도 프로젝트(`D:\Project\wild-haven-idle-renew`)에서 만든 디자인 코드 4파일을 `com.jeiel85.wildhavenidle.renew` 서브패키지로 이식 (패키지 선언만 변경, 본문 그대로)
- AndroidManifest launcher Activity를 `.renew.MainActivity`로 교체, 기존 `.MainActivity`는 `exported=false`로 비활성화하고 코드는 보존
- 의존성 추가: `material-icons-extended`, `lifecycle-runtime-compose` (libs.versions.toml + app/build.gradle.kts)
- 버전 갱신: versionCode 6 → 7, versionName 0.5.1 → 0.6.0
- CHANGELOG v0.6.0 + Known Issue 섹션 (디자인 셸·기존 데이터 표시 보류 명시)
- 출시 노트 `store-release-notes/v0.6.0.txt` 신규 (ko 281 / en 444, 500자 한도 내)
- 로컬 서명 AAB 빌드 성공, `jarsigner -verify -strict` 통과
- 바탕화면 자동 복사 (`wild-haven-idle-v0.6.0.aab`, `wild-haven-idle-v0.6.0-release-notes.txt`)

### Known Risk (사용자 출시 강행 결정 후 진행)

- 새 MainActivity는 자체 인메모리 mock 상태만 사용 — 기존 `GameRepository`/`GameStateDataStore`/`BalanceCalculator`/`DailyBonus`와 단절
- v0.5.1 사용자가 v0.6.0 받으면 진행 상태(포인트, 동물 회복도, 일일 보너스)가 화면에서 사라짐
- DataStore preferences는 삭제되지 않으므로 다음 패치에서 복원 가능

### Not Done

- main 머지 + 태그 v0.6.0 푸시 (커밋 직후 자동 실행 예정)
- 실기기 사이드로드 검증 (사용자 수동 단계)
- Play Console 업로드 (사용자 수동 단계)
- 다음 패치: mock ViewModel을 기존 도메인/Repository로 교체

## 2026-05-15 (저녁) — v0.5.1 릴리즈 준비

### Done

- 버전 갱신: versionCode 5 → 6, versionName 0.5.0 → 0.5.1 (`app/build.gradle.kts`, SettingsScreen 표기)
- CHANGELOG Unreleased → v0.5.1 - 2026-05-15 승격, Build/CI · Verification 추가
- 출시 노트 신규: `store-release-notes/v0.5.1.txt` (ko-KR 269 / en-US 427자, 500자 한도 안)
- 로컬 서명 AAB 빌드 성공: `app-release.aab` 4.64 MB
  - R8 활성 유지 (`minifyReleaseWithR8` 통과), mapping.txt AAB BUNDLE-METADATA에 자동 포함
  - `jarsigner -verify` → `jar verified.`
- 바탕화면 자동 복사 (Play Console 업로드 가속):
  - `C:\Users\jeiel\OneDrive\바탕 화면\wild-haven-idle-v0.5.1.aab`
  - `C:\Users\jeiel\OneDrive\바탕 화면\wild-haven-idle-v0.5.1-release-notes.txt`

### Not Done

- 태그 v0.5.1 푸시 (커밋 직후 자동 실행 예정)
- 실기기 사이드로드 검증 (사용자 수동 단계)
- Play Console 업로드 (사용자 수동 단계)

## 2026-05-15 (오후) — Phase 0.5 P4 1차: 홈 카드/버튼 마이크로 폴리시

### Done

- 홈 화면 카드 순서 재정렬: `헤더 → 타이틀 → 보호 포인트 → 지금 추천 → 다음 해금 → 일일 보상 → 보호구역 확장 → 동물 목록 → 보조 navigation`
  - 사용자 요구 우선순위 `현재 보호구역 상태 → 보호 포인트 → 추천 행동 → 보상/진행`을 첫 스크롤에서 한눈에 읽히게 정렬
  - 기존 순서에서는 일일 보상이 추천 위에 있어 추천 행동의 강조가 약했다 — 추천을 보호 포인트 바로 아래로 끌어올림
- 추천 카드(`RecommendedActionCard`) 강조 격상
  - 컨테이너 색: `tertiaryContainer` → `primaryContainer`
  - tonal elevation: `sm` → `md`
  - 메인 버튼 색: `tertiary` → `primary` (기본 머티리얼 톤 사용)
  - `onPrimaryContainer` 텍스트 색으로 정합성 유지
- 보호구역 확장 카드(`SanctuaryUpgradeCard`) 강조 하향
  - 컨테이너 색: `primaryContainer` → `surface`, `tonalElevation = sm` 추가
  - 추천 카드와 동일 색이 경쟁하는 문제 해소
- 보호 중인 동물 카드(`AnimalRecoveryCard`) tonal elevation `md` → `sm` 통일
- 홈 하단 도감/설정 버튼: `Button(filled)` → `OutlinedButton` (낮은 강조)
  - 보조 행동이 메인 행동만큼 강하게 보이던 문제 해소
- 보호구역 헤더 `Lv.N` 라벨 가독성 보강
  - 반투명 흰색 알약 배경(`shapes.small` + 82% alpha)
  - 텍스트 색 `#3A4F3A` → `#1F3A28` (헤더 PNG의 잎 영역 위에서도 식별)
- 에셋 분류 규칙 항구화: `docs/ART_DIRECTION.md` §11 신설
  - `keep` / `repurpose` / `replace` 정의
  - 현재 분류표 (동물 5종 PNG + 헤더 PNG + 앱 아이콘 + AnimalSilhouette = `keep`, 스토어 그래픽 = `review`, `repurpose`/`replace` 후보 없음)
  - 의심 에셋 처리 절차 (분류 → 기록 → 새 에셋 생성 → 한 PR 교체)
- 검증
  - `./gradlew :app:compileDebugKotlin` 성공
  - `./gradlew :app:testDebugUnitTest` 성공 (회귀 없음)

### Not Done

- 실기기/Compose Preview 시각 회귀 점검 (Android Studio 미사용 환경, 다음 세션 또는 사용자 사이드로드 시 확인 필요)
- 360dp 작은 화면에서 OutlinedButton 두 개의 텍스트 잘림 여부 시각 확인 (`maxLines=1` + Ellipsis는 적용됨)
- MilestoneCard 진행률 타이포 추가 정돈 (현재 % 라벨만 강조됨) — P4 후속
- SanctuaryHeader Lv 라벨에 도감 진척 등 추가 정보 결합 검토 — P4 후속
- Phase 0.5 P5 (모션 timing/easing 토큰 통일) — 미착수

## 2026-05-15

### Done

- 다음 세션용 디자인 개선 방향 정리
- 사용자 피드백 반영: 현재 디자인 인상이 좋지 않으며, 기존 이미지 에셋을 전부 대체할지 고민 중
- 판단 기록: 에셋 전면 폐기보다 `keep` / `repurpose` / `replace` 분류를 먼저 수행
- 현재 동물 5종 PNG와 보호구역 헤더 PNG는 우선 `keep` 후보로 유지
- 다음 작업 후보를 Phase 0.5 P4 카드/버튼 마이크로 폴리시로 좁힘
- P4 세부 체크리스트를 `.agent/tasks.md`에 추가
- 구체 인계 메모를 `.agent/session-handoff.md`에 추가

### Not Done

- 실제 UI 코드 변경 없음
- 새 이미지 에셋 생성/교체 없음
- 스크린샷 기반 시각 검수 없음
- Gradle 빌드/테스트 실행 없음 (문서 인계 메모만 변경)

## 2026-05-13

### Done

- Initial project direction defined
- Repository name recommended: `wild-haven-idle`
- IP and ethics policy drafted
- MVP scope defined
- Android technical structure drafted
- Agent workflow rules integrated
- Initial document package generated
- Android Compose project scaffold added
- Application ID set to `com.jeiel85.wildhavenidle`
- MVP domain models and animal definitions added
- BalanceCalculator and unit tests added
- Simple HomeScreen MVP added
- Local Android SDK path configured through untracked `local.properties`
- DataStore save/load added for MVP game state
- Home screen now ticks care points and shows offline reward on return
- GitHub Actions passed for PR #1 after fixing Gradle Wrapper execute permission
- Release keystore created outside the repository
- Release signing secrets registered in GitHub Actions
- Signed release APK and AAB generated locally
- Signed release APK and AAB generated in GitHub Actions Android CI for PR #2
- App launcher icon generated and wired into Android resources
- Release workflow publishes signed APK/AAB to GitHub Releases on `v*` tag push
- GitHub Pages page surfaces a Download APK call-to-action linking to latest release

### 2026-05-13 (afternoon)

- Compose Navigation dependency added (navigation-compose 2.9.0)
- AppNavGraph with 4 routes (Home, Animals, Archive, Settings) created
- AnimalListScreen + ViewModel: 구조 동물 목록, 회복 지원, 보호구역 업그레이드
- ArchiveScreen + ViewModel: 도감 화면 (LOCKED/DISCOVERED/PROTECTED 상태 표시)
- SettingsScreen: 데이터 초기화 (확인 대화상자 포함), 버전 정보
- GameRepository 확장: supportAnimalRecovery, upgradeSanctuary, resetData, applyUnlocks
- BalanceCalculator.getRecoveryBaseCost 추가 (희귀도별 회복 비용)
- CheckUnlockConditionsUseCase 추가: CarePoint/회복단계/생산량/보호동물수 조건 체크
- HomeScreen에 네비게이션 버튼 추가 (구조 동물, 도감, 설정)
- MainActivity에서 AppNavGraph 사용하도록 변경
- 해금 조건 자동 체크: 상태 변경 시마다 신규 동물 해금 확인
- Build + test 통과 확인

### Not Done

- Remote repository main branch not confirmed
- Separate manual/tag release workflow has not been triggered yet (first `v*` tag not pushed)
- Art assets are placeholder/generated; self-made art not yet created
- Localization resources (Korean/English) not yet added
- Unit tests for new screens/use cases not yet added

## 2026-05-14

### Done

- 상용화 개선 계획 문서 추가: `docs/COMMERCIALIZATION_PLAN.md` (Phase 1/2/3 우선순위와 비목표 정리)
- Phase 1 P1: 첫 사용자 온보딩 오버레이 구현
  - `GameState.onboardingCompleted` 플래그 추가
  - DataStore 키 `onboarding_completed` 추가, 기존 사용자 마이그레이션 기본값 = true (재노출 방지)
  - `GameRepository.markOnboardingCompleted()` 추가
  - `HomeUiState.showOnboarding` 추가, `BuildHomeUiStateUseCase`에서 플래그 전파
  - `OnboardingOverlay` (3-step) 컴포저블 추가, `HomeScreen`에서 신규 데이터 시 1회 노출
  - 단위 테스트 3개 추가: `BuildHomeUiStateUseCaseTest`
- 검증: `./gradlew :app:compileDebugKotlin` / `./gradlew :app:testDebugUnitTest` 모두 성공
  - 신규 테스트 3개 통과 확인 (`BuildHomeUiStateUseCaseTest`)
  - 실기기/에뮬레이터 시각 확인은 이번 작업에서 수행하지 않음

### Not Done

- 추천 다음 행동 단일 카드 (Phase 1 P2)
- 작은 화면 가독성 정리 (Phase 1 P3)
- 일일 접속 보상, 회복 마일스톤 축하 다이얼로그 (Phase 2)
- 일일 과제, 도감 진척 카드, 보호구역 마일스톤 보상 (Phase 3)

### 2026-05-14 (오후) — v0.3.0 릴리즈 준비

- versionCode 3 / versionName 0.3.0으로 갱신, 설정 화면 표기 동기화
- CHANGELOG의 Unreleased를 v0.3.0으로 승격
- `store-release-notes/v0.3.0.txt` 신규: `<ko-KR>`, `<en-US>` 태그 형식 (Play Console 다국어 출시 노트)
- 로컬 서명 AAB 빌드: `app/build/outputs/bundle/release/app-release.aab` (8.8 MB)
- `jarsigner -verify` 결과 `jar verified.`
- 태그 v0.3.0 푸시 → Android Release 워크플로 성공 → GitHub Release 자동 생성
- "새 버전 만들기" 워크플로 메모리에 태그 푸시 자동화 단계 추가

### 2026-05-14 (v0.5.0 묶음 릴리즈)

- versionCode 5 / versionName 0.5.0
- CHANGELOG Unreleased → v0.5.0 승격 (추천 카드 + 가독성 + R8 + 일일 보상 묶음)
- store-release-notes/v0.5.0.txt 신규 (ko 222 / en 405자)
- 로컬 서명 AAB: 4.85 MB (R8 활성, mapping/baseline.prof 자동 포함, jarsigner verified)
- 다음: 커밋·푸시 → 태그 v0.5.0 푸시 → Android Release 워크플로 자동 트리거 → GitHub Release 자동 생성

### 2026-05-14 (일일 보상) — Phase 2 P1: 일일 보호 활동 보상

- GameState에 lastDailyBonusClaimedAtMillis 추가 (nullable)
- DataStore 키 last_daily_bonus_claimed_at, 키 없는 기존 사용자는 자동 자격 부여
- domain/dailybonus/DailyBonusRules: 자정 경계 자격 판정 + 생산량 × 3시간 보상 (50pt 하한)
- GameRepository.claimDailyBonus(), HomeViewModel.claimDailyBonus()
- BuildHomeUiStateUseCase invoke에 nowMillis 인자 추가, dailyBonus 채움
- DailyBonusCard composable (RecommendedActionCard 위에 노출, 자격 있을 때만)
- 카드 형태로 사용자 자율성 존중 — 무시·능동 수령 가능 (다이얼로그 회피)
- 단위 테스트 8건 추가 (DailyBonusRules 6 + UiState 자격 2), 총 20건 통과
- compileDebugKotlin / testDebugUnitTest 성공

### 2026-05-14 (작은 화면) — Phase 1 P3: 360dp 가독성 보호

- 홈 + 도감의 모든 사용자 노출 텍스트에 maxLines + overflow=Ellipsis 적용
- 좌·우 Row(SpaceBetween) 패턴: 좌측에 weight(1f, fill=false) + 우측에 padding(start) → 우측 라벨이 절대 잘리지 않음
- 도감 카드 동물 설명: maxLines=3로 카드 높이 일관성
- 큰 숫자 시나리오 Preview 추가 (widthDp=360, Lv.99, 1.5M 포인트, 999/sec)
- compileDebugKotlin / testDebugUnitTest 성공 (회귀 없음)

### 2026-05-14 (R8) — R8 활성화: 가독화 파일 + 크기 절반

- Play Console "가독화 파일 없음" 경고 대응
- isMinifyEnabled=true + isShrinkResources=true
- proguard-rules.pro에 SourceFile/LineNumberTable 보존
- AAB 크기 10.2 → 4.8 MB (~53% 감소)
- mapping.txt 24 MB가 AAB BUNDLE-METADATA에 자동 포함 → Play Console 인식
- baseline.prof도 함께 포함 (보너스)
- bundleRelease 성공, jarsigner verified
- ⚠️ release APK 실기기 동작 확인 미수행 — 다음 릴리즈 전 필수

### 2026-05-14 (v0.4.0 이후) — Phase 1 P2: 단일 추천 다음 행동 카드

- 홈 `CarePointPanel` 아래에 "지금 추천" 카드 추가
- 추천 우선순위: 보호구역 확장 > ROI 최고 동물 회복 > 가장 싼 행동까지 대기 시간
- `RecommendedAction` sealed 인터페이스, `recommendAction`/`waitForNext` 헬퍼
- 기존 카드는 모두 유지 — 추천은 강조이고 대체가 아님
- 단위 테스트 4건 추가 (총 7건 통과)
- compileDebugKotlin / testDebugUnitTest 성공

### 2026-05-14 (심야 마무리) — v0.4.0 릴리즈 준비

- versionCode 4 / versionName 0.4.0으로 갱신, 설정 화면 표기 동기화
- CHANGELOG의 Unreleased를 v0.4.0으로 승격
- store-release-notes/v0.4.0.txt 신규: ko-KR 209자 / en-US 417자 (한도 내)
- 로컬 서명 AAB: app-release.aab (10.2 MB, versionName 0.4.0)
- jarsigner -verify → jar verified.
- 다음: 커밋·푸시 → 태그 v0.4.0 푸시 → Android Release 워크플로 자동 트리거 → GitHub Release 자동 생성

### 2026-05-14 (심야, 헤더) — Phase 0.5 P2 완료: 보호구역 헤더 PNG 도입

- 사용자가 ChatGPT Plus에서 헤더 풍경 1장 생성 (재시도 불필요)
- 5-게이트 모두 ✅, 동물 5종과 시리즈 일관성 매우 높음
- 1672×941 → 960×540 LANCZOS + 256-color quantize → 395 KB
- SanctuaryHeader 절차적 풍경 코드 약 110줄 제거, Image로 교체
- 동물 스프라이트/Floater/Lv 라벨/탭 제스처는 그대로 유지
- 트레이드오프: Lv별 나무 증가 시각 효과·미세 모션(구름/태양/나무 흔들림) 사라짐
- compileDebugKotlin / testDebugUnitTest 성공

### 2026-05-14 (심야 후속) — Phase 0.5 P3 완료: 동물 5종 PNG 전면 교체

- 사용자가 ChatGPT Plus에서 fox/deer/owl/lynx 각 1장 생성 (한 번에 통과)
- 에이전트 검수: 5-게이트 모두 ✅, rabbit과 동일 작가 시리즈로 보일 정도의 일관성
- PIL LANCZOS + 256-color quantize → 4장 모두 191~219 KB
- animalDrawableMap에 4개 추가, color 파라미터 제거(모든 호출부 기본값만 사용)
- AnimalSilhouette(LOCKED)은 그레이 통일감을 위해 절차적 Canvas 유지
- compileDebugKotlin / testDebugUnitTest 성공
- 다음: Phase 0.5 P2 (보호구역 헤더 PNG) → 카드 배경

### 2026-05-14 (심야) — Phase 0.5 P2 시작: AI 서비스 잠금 + rabbit_001 PNG 도입

- 사용자의 ChatGPT Plus(DALL·E 3) + Gemini Advanced(Imagen 3) 두 구독으로 rabbit_001 시연 진행
- 에이전트가 8장 직접 시각 검수 → 5-게이트 평가 → DALL·E 3로 잠금 결정 (톤 일치/4장 일관성에서 우세, Imagen은 4-in-1 그리드/액자 마진으로 카드 시스템 부적합)
- ART_DIRECTION §8.1에 잠금 기록, ASSET_LICENSES §2에 rabbit_001 메타 추가, bakeoff 결정 기록 완료
- 채택본 dalle3-1 → PIL LANCZOS 다운샘플(1254→512) + 256-color quantize → 218 KB → `app/src/main/res/drawable-nodpi/wh_animal_rabbit_001.png`
- `AnimalIllustration`에 `animalDrawableMap` 추가 — 매핑 있으면 PNG, 없으면 절차적 Canvas (점진 교체 패턴)
- compileDebugKotlin / testDebugUnitTest 모두 성공 (8건)
- 4종 프롬프트(`wh_animal_{fox,deer,owl,lynx}_001.txt`) 사전 준비 — Subject 슬롯만 교체, 나머지 4슬롯 동일

### 2026-05-14 (밤) — Phase 0.5 P1 토큰 시스템 도입

- `core/design/`에 5개 토큰 파일 신설 (Color/Typography/Shapes/Spacing/Elevation) + Theme.kt 재작성
- 머티리얼3 토큰(ColorScheme/Typography/Shapes)과 본작 고유 토큰(WildHavenColors/Spacing/Elevation/TextStyles)을 한 번에 주입
- `WildHavenTheme` 함수와 같은 이름의 object 접근자 공존 (머티리얼3 패턴) — `WildHavenTheme.spacing.lg` 형태 접근
- 5개 화면(HomeScreen, OnboardingOverlay, SettingsScreen, ArchiveScreen, SanctuaryHeader)을 토큰 참조로 전환
- 시각 출력은 같은 값을 토큰 경유로만 바꿈 — 회귀 위험 최소화
- 검증: compileDebugKotlin / testDebugUnitTest 모두 성공 (8건)

### 2026-05-14 (저녁) — Phase 0.5 디자인 트랙 개시

- 비주얼 인상이 "절차적 Compose Canvas + 머티리얼 기본값"으로 졸업작품 톤에 머무름을 인지
- 에셋 확보 경로 비교 후 *AI 생성 + 사람 검수* 방식으로 결정 (Kenney 등 CC0 팩으로는 "야생동물 5종 + 보호구역" 일관 톤 확보 어려움)
- `docs/ART_DIRECTION.md`에 §8~§10 신설:
  - 사용 가능 서비스 3종 비교 (Adobe Firefly 추천), 한 번에 하나만 사용 원칙
  - 5-슬롯 프롬프트 템플릿, 고정 스타일 앵커, 고정 네거티브
  - 시드/Reference 운영, Play Console 공시, 사람 검수 5단계 게이트
  - 디렉터리 규약 (`docs/assets/source/`, `app/src/main/res/drawable-nodpi/`, `wh_*` 명명), 해상도 가이드
- `docs/ASSET_LICENSES.md`에 AI 생성 에셋 메타 컬럼(§2) 추가, 라이선스 분류 정의, 프롬프트 SHA-256 기록 방식 도입
- `docs/COMMERCIALIZATION_PLAN.md`에 §0.5 Design Polish 트랙 5개 항목(P1~P5) 추가
- `.agent/tasks.md`에 Phase 0.5 태스크 등록 (파이프라인 문서화는 완료 표시)
- 코드 변경 없음 — 빌드/테스트 영향 없음
