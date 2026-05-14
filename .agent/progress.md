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
- 태그 v0.3.0 푸시 → Android Release 워크플로 성공 → GitHub Release 자동 생성
- "새 버전 만들기" 워크플로 메모리에 태그 푸시 자동화 단계 추가

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
