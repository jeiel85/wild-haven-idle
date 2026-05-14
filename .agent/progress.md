# Progress

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
- 태그/푸시 자동화 없음(사용자 명시 요청 시에만 태그)
