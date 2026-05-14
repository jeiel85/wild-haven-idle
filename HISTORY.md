# HISTORY.md

## 2026-05-14

- 작업: Phase 1 P2 — 홈에 단일 추천 다음 행동 카드 도입
- 변경 파일:
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/RecommendedAction.kt (신규)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeUiState.kt (recommendedAction 추가)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeScreen.kt (RecommendedActionCard composable + ActionableBody/WaitForNextBody, formatWaitDuration 헬퍼)
  - app/src/main/java/com/jeiel85/wildhavenidle/domain/usecase/BuildHomeUiStateUseCase.kt (recommendAction + waitForNext)
  - app/src/test/java/com/jeiel85/wildhavenidle/domain/usecase/BuildHomeUiStateUseCaseTest.kt (4건 추가)
  - CHANGELOG.md, .agent/tasks.md, .agent/progress.md
- 검증:
  - `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest` 성공
  - 신규 테스트 4건 모두 통과 (총 7건):
    - recommendsWaitWhenNothingAffordable
    - recommendsSanctuaryUpgradeWhenAffordable
    - recommendsRecoveryWhenUpgradeUnaffordableButRecoveryAvailable
    - upgradeBeatsRecoveryWhenBothAffordable
  - 실기기 시각 확인은 수행하지 않음
- 결과:
  - 홈 화면에 "지금 추천" 카드 추가 — 사용자가 가장 가치 있는 다음 한 가지 행동을 즉시 인지
  - 우선순위 알고리즘:
    1. 보호구역 확장 가능 → 무조건 추천 (기본 +1/sec ROI 가장 안정적)
    2. 회복 가능 동물 중 supportBonus / recoveryCost 비율 최고 → 그 동물 회복 추천
    3. 둘 다 불가 → 가장 싼 다음 행동까지 대기 시간(초/분/시간) 안내
  - 기존 카드(SanctuaryUpgradeCard, AnimalRecoveryCard)는 그대로 유지 — 추천은 *강조*이고 *대체*가 아님
- 후속 작업:
  - Phase 1 P3: 작은 화면(360dp) 가독성 정리
  - Phase 0.5 P4: 카드/버튼 마이크로 폴리시
  - Phase 0.5 P5: 모션 timing/easing 토큰 통일

## 2026-05-14

- 작업: v0.4.0 릴리즈 준비 — 디자인 토큰화 + 동물 5종 + 보호구역 헤더 PNG 전면 적용 묶음
- 변경 파일:
  - app/build.gradle.kts (versionCode 3 → 4, versionName 0.3.0 → 0.4.0)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/settings/SettingsScreen.kt (표기 v0.4.0)
  - CHANGELOG.md (Unreleased → v0.4.0 승격, Build/CI · Verification 항목 추가)
  - store-release-notes/v0.4.0.txt (신규, ko-KR 209자 / en-US 417자)
  - HISTORY.md, .agent/progress.md
- 산출물:
  - AAB: `D:\Project\wild-haven-idle\app\build\outputs\bundle\release\app-release.aab` (10.2 MB, versionName=0.4.0, versionCode=4)
  - 출시 노트: `store-release-notes/v0.4.0.txt`
  - 서명 인증서: `CN=Wild Haven Idle, OU=Release, O=Jeiel85, L=Seoul, ST=Seoul, C=KR` (RSA 4096)
- 검증:
  - `./gradlew :app:bundleRelease` 로컬 서명 성공 (release signing 환경변수 사용)
  - `jarsigner -verify` → `jar verified.`
  - AAB 매니페스트에서 `versionName=0.4.0` 확인
  - 출시 노트 글자수: ko-KR 209자, en-US 417자 (Play Store 500자 한도 안)
  - 실기기/Play Console 업로드는 사용자 수동 단계
- 결과:
  - v0.3.0 대비 차이점:
    1. WildHavenTheme 디자인 토큰 시스템 (컬러/타이포/Shape/Spacing/Elevation)
    2. 동물 5종 절차적 Canvas → AI 생성 PNG 전면 교체
    3. 보호구역 헤더 절차적 캔버스 → PNG 풀폭 풍경
    4. 5개 화면 토큰 참조로 정리 (RoundedCornerShape/tonalElevation 하드코딩 제거)
  - APK/AAB 크기 +1.4 MB (PNG 6장 도입 영향)
- 후속 작업:
  - 태그 v0.4.0 푸시 → Android Release 워크플로 자동 트리거 → GitHub Release 자동 생성
  - Play Console에 AAB 업로드 + 출시 노트 ko-KR/en-US 블록 붙여넣기
  - 실기기 시각 회귀 확인 (헤더 PNG 위 동물 산책 자연스러움)

## 2026-05-14

- 작업: Phase 0.5 P2 완료 — 보호구역 헤더(`wh_habitat_forest_001`) PNG 도입, SanctuaryHeader 절차적 캔버스 폐기
- 변경 파일:
  - app/src/main/res/drawable-nodpi/wh_habitat_forest_001.png (신규, 960×540, 395 KB)
  - docs/assets/source/wh_habitat_forest_001.png (신규, 1672×941 원본)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/components/SanctuaryHeader.kt
    (drawScenery/computeTrees/computeFlowers/drawCloud/Tree/Flower 제거, Image 교체)
  - docs/ART_DIRECTION.md §9.2 (DALL·E 3 실제 출력 사이즈 반영, 헤더 960×540 명시)
  - docs/ASSET_LICENSES.md §2 habitat 메타 한 줄 추가
  - CHANGELOG.md, .agent/tasks.md, .agent/progress.md
- 검증:
  - 사용자가 ChatGPT Plus(DALL·E 3)에서 1장 생성 (재시도 불필요)
  - 에이전트가 시각 검수: 5-게이트 모두 ✅, 동물 5종과 동일 작가 시리즈 인상
  - 제약 충족: 동물·사람·건물 없음, 좌상단 비움, 우상단 햇살, 중앙·하단 빈 풀밭 띠
  - 1672×941 → 960×540 LANCZOS 다운샘플 + 256-color quantize → 395 KB
  - `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest` 성공 (8건)
  - 양자화 후 PNG를 Read 툴로 재확인 → 수채화 톤·구도 보존
- 결과:
  - 홈 화면 상단 보호구역 헤더가 절차적 머큐리 캔버스에서 자연관찰노트 풍의 풀폭 풍경 PNG로 전환
  - 동물 산책 스프라이트는 PNG 위에서 그대로 좌우 산책 + 상하 보빙
  - 탭 보상 플로터, Lv 라벨, 탭 제스처 모두 보존
  - 절차적 풍경 코드 약 110줄 제거 — 매 프레임 Canvas 드로잉 부담 감소
- 트레이드오프:
  - 보호구역 Lv에 따라 *나무가 늘어나는* 시각 보상이 사라짐
  - 떠다니는 구름/태양 글로우/나무 흔들림 미세 모션도 사라짐
  - 후속 작업으로 Lv별 헤더 PNG 변형(묘목→성숙림 등) 또는 PNG 위 미세 오버레이 모션 검토 가능
- 후속 작업:
  - Phase 0.5 P4: 카드/버튼 마이크로 폴리시 (작은 spacing 토큰화, 그림자/위계 정리)
  - Phase 0.5 P5: 모션 timing/easing 토큰 통일
  - 또는 기능 트랙으로 전환: Phase 1 P2 (홈 단일 추천 다음 행동 카드)
  - 실기기에서 동물 스프라이트가 헤더 PNG 위에서 자연스럽게 산책하는지 수동 확인

## 2026-05-14

- 작업: Phase 0.5 P3 완료 — fox/deer/owl/lynx 4종 PNG 도입으로 동물 일러스트 5종 전면 교체
- 변경 파일:
  - app/src/main/res/drawable-nodpi/wh_animal_{fox,deer,owl,lynx}_001.png (신규)
  - docs/assets/source/wh_animal_{fox,deer,owl,lynx}_001.png (신규, 1254×1254 원본 보존)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/components/WildlifeIllustration.kt
    (animalDrawableMap에 4종 추가, color 파라미터 제거, 폴백 단순화)
  - docs/ASSET_LICENSES.md (§2에 4종 메타 한 줄씩 추가, placeholder 항목을 silhouettes로 대체)
  - CHANGELOG.md, .agent/tasks.md, .agent/progress.md
- 검증:
  - 사용자가 ChatGPT Plus(DALL·E 3)에서 4종 각 1장씩 생성 (재시도 불필요)
  - 에이전트가 4장을 직접 시각 검수: 5-게이트 모두 ✅, 시리즈 톤 일관성 매우 우수 (rabbit과 동일 작가 시리즈 인상)
  - PIL LANCZOS 다운샘플(1254→512) + 256-color quantize → 191~219 KB
  - `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest` 성공 (8건)
  - 양자화 후 수채화 톤·구도 보존 확인 (Read 툴로 fox/owl 재확인)
- 결과:
  - 동물 5종(rabbit/fox/deer/owl/lynx) 모두 절차적 Canvas → AI 생성 PNG 교체 완료
  - 홈/도감의 모든 동물 일러스트가 통일된 자연관찰노트 톤의 수채화로 표시
  - `AnimalIllustration`이 사실상 PNG 전용 컴포저블로 정리됨 (color 파라미터 제거)
  - `AnimalSilhouette` (LOCKED 상태)은 그레이 통일감을 위해 절차적 Canvas 의도적 유지
- 후속 작업:
  - Phase 0.5 P2: 보호구역 헤더 PNG (`wh_habitat_forest_001`, 1080×640) — 같은 시리즈 톤으로 풀폭 풍경 1장
  - 그 후 카드 배경 등 세부 에셋

## 2026-05-14

- 작업: Phase 0.5 P2 시작 — AI 서비스 잠금(DALL·E 3) + 첫 동물 일러스트(rabbit_001) PNG 도입
- 변경 파일:
  - docs/assets/bakeoff/rabbit_001-bakeoff.md (평가표 채움 + DALL·E 3 잠금 결정)
  - docs/ART_DIRECTION.md (§8.1 잠금 기록)
  - docs/ASSET_LICENSES.md (§2 rabbit_001 메타 한 줄, animal placeholder 항목 갱신)
  - docs/assets/source/wh_animal_rabbit_001.png (신규, 1254×1254 원본 보존)
  - app/src/main/res/drawable-nodpi/wh_animal_rabbit_001.png (신규, 512×512 quantized 218 KB)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/components/WildlifeIllustration.kt (animalDrawableMap, Image 분기)
  - docs/assets/prompts/wh_animal_fox_001.txt (신규)
  - docs/assets/prompts/wh_animal_deer_001.txt (신규)
  - docs/assets/prompts/wh_animal_owl_001.txt (신규)
  - docs/assets/prompts/wh_animal_lynx_001.txt (신규)
  - CHANGELOG.md, .agent/tasks.md, .agent/progress.md
- 검증:
  - 사용자가 ChatGPT Plus(DALL·E 3)와 Gemini Advanced(Imagen 3) 양쪽에서 같은 프롬프트 시연
  - 에이전트가 8장(DALL·E 4 + Imagen 4-in-1)을 직접 시각 검수해 5-게이트 평가
  - DALL·E 3 채택 근거: 톤 일치도 + 4장 사이 일관성 + Imagen은 4-in-1 그리드/액자 마진으로 카드 시스템 부적합
  - 채택본 dalle3-1을 PIL LANCZOS 다운샘플(1254→512) + 256-color quantize → 218 KB
  - `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest` 성공
  - 수동 시각 회귀 확인: 채택본 이미지를 Read 툴로 읽어 수채화 톤·구도 보존 확인
- 결과:
  - 본작 일러스트 서비스가 OpenAI gpt-image-1 / DALL·E 3 (ChatGPT Plus)로 잠금 — 추가 비용 0
  - rabbit_001이 절차적 Canvas → AI 생성 PNG로 1차 교체 (홈/도감의 토끼 카드에 즉시 반영)
  - `AnimalIllustration`이 ID → drawable 매핑으로 PNG/Canvas 폴백 — 나머지 4종은 매핑 추가만으로 점진 교체 가능
  - 4종 프롬프트 템플릿 (Subject 슬롯만 교체, 나머지 4슬롯은 rabbit_001과 동일) 사전 준비
- 후속 작업:
  - 사용자가 ChatGPT Plus에서 fox/deer/owl/lynx 프롬프트 4개를 순서대로 실행
  - 결과 PNG를 `docs/assets/source/bakeoff/{animal}_001/dalle3-1..N.png`로 저장 (재생성 후보 여러 장 가능)
  - 에이전트가 검수 → 채택 → 동일 절차로 도입
  - 이후 보호구역 헤더(`wh_habitat_forest_001`, 1080×640) 진행

## 2026-05-14

- 작업: Phase 0.5 P1 — `WildHavenTheme` 디자인 토큰 시스템 도입 및 호출부 정리
- 변경 파일:
  - app/src/main/java/com/jeiel85/wildhavenidle/core/design/Theme.kt (재작성, accessor object 추가)
  - app/src/main/java/com/jeiel85/wildhavenidle/core/design/WildHavenColor.kt (신규)
  - app/src/main/java/com/jeiel85/wildhavenidle/core/design/WildHavenTypography.kt (신규)
  - app/src/main/java/com/jeiel85/wildhavenidle/core/design/WildHavenShapes.kt (신규)
  - app/src/main/java/com/jeiel85/wildhavenidle/core/design/WildHavenSpacing.kt (신규)
  - app/src/main/java/com/jeiel85/wildhavenidle/core/design/WildHavenElevation.kt (신규)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeScreen.kt (Shapes/Elevation/Spacing 토큰 적용)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/onboarding/OnboardingOverlay.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/settings/SettingsScreen.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/archive/ArchiveScreen.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/components/SanctuaryHeader.kt
  - CHANGELOG.md, .agent/tasks.md, .agent/progress.md
- 검증:
  - `./gradlew :app:compileDebugKotlin` 성공
  - `./gradlew :app:testDebugUnitTest` 성공 (5+3=8건 통과)
  - 토큰 도입은 같은 값을 토큰 경유로 바꾸는 식으로 진행 — 시각 출력 회귀는 의도적으로 최소화
  - 실기기/에뮬레이터 수동 확인은 수행하지 않음
- 결과:
  - `WildHavenTheme` 진입점이 머티리얼3 토큰(ColorScheme/Typography/Shapes)과 본작 고유 토큰(WildHavenColors/Spacing/Elevation/TextStyles)을 함께 주입
  - 5개 화면이 `RoundedCornerShape(N.dp)` 하드코딩, 임의 `tonalElevation = N.dp`, 주요 padding/spacing 하드코딩에서 토큰 참조로 전환
  - 의미 색상(희귀도, 보호구역 톤)이 화면 코드와 분리돼, 이후 도감/희귀도 표시 통일 작업의 토대 마련
  - typography는 머티리얼 기본을 게임 톤(SemiBold/Bold, letterSpacing)에 맞춰 보정
  - "Kotlin은 함수와 object를 다른 네임스페이스로 본다"는 머티리얼 패턴을 그대로 적용 — `WildHavenTheme(...)` 함수와 `WildHavenTheme.spacing` 접근자 공존
- 후속 작업:
  - Phase 0.5의 다음 항목: AI 이미지 서비스 결정 → P2 (보호구역 헤더 PNG 1장)
  - 또는 Phase 1 P2: 홈 단일 추천 다음 행동 카드
  - Phase 0.5 P4 (마이크로 폴리시) 단계에서 작은 spacing(2/4/6 dp)도 토큰화 정리

## 2026-05-14

- 작업: 디자인 트랙 개시 — AI 에셋 생성 파이프라인 결정 문서화 (Phase 0.5 P0)
- 변경 파일:
  - docs/ART_DIRECTION.md (§8 AI 에셋 생성 파이프라인, §9 디렉터리 규약, §10 생성 후 워크플로 신설)
  - docs/ASSET_LICENSES.md (§2 AI 생성 에셋 메타 컬럼 추가, 라이선스 분류 정의, 프롬프트 기록 방법 명문화)
  - docs/COMMERCIALIZATION_PLAN.md (§0.5 Design Polish 트랙 5개 항목 추가, 현재 상태 갱신)
  - .agent/tasks.md (Phase 0.5 디자인 폴리시 태스크 6개 등록, 파이프라인 문서화는 완료 표시)
  - .agent/progress.md
- 검증:
  - 코드 변경 없음 — 빌드/테스트 영향 없음
  - 문서 상호 참조 일관성 확인 (ART_DIRECTION ↔ ASSET_LICENSES ↔ COMMERCIALIZATION_PLAN)
- 결과:
  - 사용 가능한 AI 이미지 서비스 3종(Adobe Firefly / OpenAI gpt-image-1 / Imagen on Vertex)을 상업 사용 가능 기준으로 명시, Firefly를 기본 추천
  - 모든 일러스트 프롬프트가 따라야 할 5-슬롯 템플릿(Subject/Setting/Style/Palette/Negative) 정의
  - 스타일 앵커와 네거티브 프롬프트를 고정 문자열로 잠금 (재생성 시 톤 일관성 확보)
  - 시드/Reference Image 운영, Play Console 공시 가이드, 사람 검수 게이트 5단계 명시
  - 에셋 디렉터리/파일명/해상도 가이드와 ASSET_LICENSES 기록 절차 표준화
  - 한 번에 하나의 AI 서비스만 사용한다는 "혼용 금지" 원칙 명문화
- 후속 작업:
  - Phase 0.5 P1: WildHavenTheme 토큰화 (AI 서비스 결정 없이도 시작 가능)
  - 또는 사용자가 AI 서비스를 결정하면 Phase 0.5 P2 (보호구역 헤더 PNG 1장) 시범 도입

## 2026-05-14

- 작업: v0.3.0 릴리즈 준비 — 버전 갱신, 한/영 Play Store 출시 노트 작성, 로컬 서명 AAB 빌드
- 변경 파일:
  - app/build.gradle.kts (versionCode 2 → 3, versionName 0.2.0 → 0.3.0)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/settings/SettingsScreen.kt (표기 v0.3.0)
  - CHANGELOG.md (Unreleased → v0.3.0 승격)
  - store-release-notes/v0.3.0.txt (신규, `<ko-KR>` / `<en-US>` 태그 형식)
  - HISTORY.md, .agent/progress.md
- 산출물:
  - AAB: `D:\Project\wild-haven-idle\app\build\outputs\bundle\release\app-release.aab` (8.8 MB)
  - 출시 노트: `store-release-notes/v0.3.0.txt` (한국어 493자, 영어 404자 — Play Store 500자 한도 내)
  - 서명 인증서: `CN=Wild Haven Idle, OU=Release, O=Jeiel85, L=Seoul, ST=Seoul, C=KR` (RSA 4096, SHA384withRSA)
- 검증:
  - `./gradlew :app:bundleRelease` 성공 (로컬, 환경변수 기반 release signing 사용)
  - `jarsigner -verify app-release.aab` → `jar verified.`
  - AndroidManifest.xml에서 `versionName=0.3.0` 확인
  - Play Store 업로드 및 실기기 설치 검증은 수행하지 않음
- 결과:
  - Play Console에 즉시 업로드 가능한 v0.3.0 AAB와 한/영 출시 노트 1세트 준비 완료
  - GitHub 태그(`v0.3.0`)는 사용자 명시 요청 시에만 생성 (자동 GitHub Release 트리거 방지)
- 후속 작업:
  - 사용자 승인 시 `git tag v0.3.0 && git push origin v0.3.0`로 GitHub Release 생성
  - Play Console에 AAB 업로드 + `store-release-notes/v0.3.0.txt`의 ko-KR/en-US 블록을 각 언어 출시 노트에 붙여넣기

## 2026-05-14

- 작업: 상용화 Phase 1 P1 — 첫 사용자 온보딩 오버레이 추가, 상용화 계획 문서화
- 변경 파일:
  - docs/COMMERCIALIZATION_PLAN.md (신규)
  - app/src/main/java/com/jeiel85/wildhavenidle/data/model/GameState.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/data/local/GameStateDataStore.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/data/repository/GameRepository.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/domain/usecase/BuildHomeUiStateUseCase.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeUiState.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeViewModel.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeScreen.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/onboarding/OnboardingOverlay.kt (신규)
  - app/src/test/java/com/jeiel85/wildhavenidle/domain/usecase/BuildHomeUiStateUseCaseTest.kt (신규)
  - .agent/tasks.md
  - .agent/progress.md
  - CHANGELOG.md
- 검증:
  - `./gradlew :app:compileDebugKotlin` 성공
  - `./gradlew :app:testDebugUnitTest` 성공 (신규 BuildHomeUiStateUseCase 테스트 3건 포함)
  - 실기기/에뮬레이터 수동 검증은 수행하지 않음
- 결과:
  - 상용화 단계별 우선순위와 비목표를 정리한 `docs/COMMERCIALIZATION_PLAN.md` 추가
  - 신규 데이터에서만 노출되는 3-step 온보딩 오버레이 추가 (환영 → 탭 안내 → 다음 해금 안내)
  - DataStore 마이그레이션 기본값을 통해 기존 사용자에게는 오버레이가 다시 뜨지 않도록 보장
- 후속 작업:
  - Phase 1 P2: 홈에 단일 추천 다음 행동 카드
  - Phase 1 P3: 작은 화면 가독성 정리
  - 실기기/에뮬레이터에서 신규/기존 사용자 시나리오 수동 확인

## 2026-05-14

- 작업: v0.2.0 홈 화면 통합과 스토어 그래픽 준비
- 변경 파일:
  - app/build.gradle.kts
  - app/src/main/java/com/jeiel85/wildhavenidle/MainActivity.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/domain/usecase/BuildHomeUiStateUseCase.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/navigation/AppNavGraph.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/navigation/Routes.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/archive/ArchiveScreen.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/components/SanctuaryHeader.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeScreen.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeUiState.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeViewModel.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/settings/SettingsScreen.kt
  - store-graphics/**
  - CHANGELOG.md
- 검증:
  - `.\gradlew.bat :app:compileDebugKotlin` 실행 성공
  - `.\gradlew.bat :app:testDebugUnitTest` 실행 성공
- 결과:
  - 홈 화면에서 보호구역 확장, 동물 회복 지원, 다음 해금 진척률, 탭 보상을 통합
  - 별도 구조 동물 목록 화면을 제거하고 홈 중심 플레이 흐름으로 정리
  - 앱 버전을 0.2.0으로 갱신하고 스토어 그래픽/릴리즈 노트 초안을 추가
- 후속 작업:
  - 상용화 개선 계획을 `docs/COMMERCIALIZATION_PLAN.md`와 `.agent/tasks.md`에 정리
  - 온보딩, 다음 목표 카드, 일일 과제 기초 구조 구현

## 2026-05-13

- 작업: Wild Haven Idle 프로젝트 기획 및 개발 규칙 초기화
- 변경 파일:
  - README.md
  - AGENTS.md
  - CHANGELOG.md
  - HISTORY.md
  - docs/GAME_DESIGN.md
  - docs/TECH_SPEC.md
  - docs/BALANCE.md
  - docs/ART_DIRECTION.md
  - docs/IP_AND_ETHICS.md
  - docs/ASSET_LICENSES.md
  - docs/ROADMAP.md
  - docs/MIGRATION.md
  - .agent/tasks.md
  - .agent/progress.md
  - .agent/decisions.md
  - .github/workflows/android.yml
- 검증:
  - 문서 파일 생성 확인
  - Android 프로젝트는 아직 생성하지 않았으므로 Gradle 빌드/테스트는 실행하지 않음
- 결과:
  - 초기 설계와 에이전트 작업 규칙 수립
- 후속 작업:
  - GitHub repository `wild-haven-idle` 생성
  - Android Compose 프로젝트 생성
  - BalanceCalculator 및 단위 테스트 구현

## 2026-05-13

- 작업: Android Compose 프로젝트 기초 골격 생성
- 변경 파일:
  - settings.gradle.kts
  - build.gradle.kts
  - gradle.properties
  - gradle/libs.versions.toml
  - gradle/wrapper/gradle-wrapper.properties
  - gradle/wrapper/gradle-wrapper.jar
  - gradlew
  - gradlew.bat
  - app/build.gradle.kts
  - app/src/main/**
  - app/src/test/**
  - .gitignore
  - .github/workflows/android.yml
  - .agent/tasks.md
  - .agent/progress.md
  - .agent/decisions.md
- 검증:
  - `.\gradlew.bat test` 실행
  - 실패 사유: 로컬 Android SDK 위치가 설정되어 있지 않음 (`ANDROID_HOME` 또는 `local.properties` 필요)
- 결과:
  - Android 프로젝트 골격, applicationId, MVP 모델, 밸런스 계산 로직, 단위 테스트, 홈 화면 초안 추가
  - CI에서 Android SDK 구성 단계를 명시하도록 워크플로 갱신
- 후속 작업:
  - Android SDK 설치 또는 local.properties 설정
  - `.\gradlew.bat test`, `.\gradlew.bat assembleDebug` 재실행
  - DataStore 저장/로드 구현

## 2026-05-13

- 작업: MVP 저장/로드 및 홈 화면 게임 루프 추가
- 변경 파일:
  - gradlew
  - app/build.gradle.kts
  - gradle/libs.versions.toml
  - app/src/main/java/com/jeiel85/wildhavenidle/MainActivity.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/core/time/TimeProvider.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/data/local/GameStateDataStore.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/data/model/GameState.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/data/repository/GameRepository.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/domain/usecase/BuildHomeUiStateUseCase.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeScreen.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeUiState.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeViewModel.kt
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeViewModelFactory.kt
  - .agent/tasks.md
  - .agent/progress.md
  - .agent/decisions.md
  - CHANGELOG.md
- 검증:
  - `.\gradlew.bat test` 실행 성공
  - `.\gradlew.bat assembleDebug` 실행 성공
  - GitHub Actions 1차 실행 실패: `./gradlew` 실행 권한 없음
  - GitHub Actions 2차 실행 성공
- 결과:
  - DataStore Preferences 기반으로 GameState를 저장/로드
  - 앱 시작 시 오프라인 보상을 계산해 보호 포인트에 반영
  - 홈 화면에서 초당 생산량만큼 보호 포인트가 증가
  - Linux CI에서 Gradle Wrapper를 실행할 수 있도록 `gradlew` 실행 비트 설정
- 후속 작업:
  - 구조 동물 회복 목록 구현
  - 보호구역 개선 기능 구현
  - 도감, 설정, 데이터 초기화 화면 구현

## 2026-05-13

- 작업: 릴리즈 서명, 릴리즈 빌드, 앱 아이콘 준비
- 변경 파일:
  - app/build.gradle.kts
  - app/src/main/AndroidManifest.xml
  - app/src/main/res/mipmap-*/ic_launcher.png
  - app/src/main/res/mipmap-*/ic_launcher_round.png
  - .github/workflows/android.yml
  - .github/workflows/android-release.yml
  - gradlew
  - docs/assets/wild-haven-icon-source.png
  - docs/ASSET_LICENSES.md
  - docs/ART_DIRECTION.md
  - docs/TECH_SPEC.md
  - .agent/tasks.md
  - .agent/progress.md
  - .agent/decisions.md
  - CHANGELOG.md
- 로컬 백업:
  - 키스토어 백업 폴더: `D:\Project\wild-haven-idle-release-backup\20260513-132122`
  - 키스토어 파일: `D:\Project\wild-haven-idle-release-backup\20260513-132122\wild-haven-release.jks`
  - 백업 메모: `D:\Project\wild-haven-idle-release-backup\20260513-132122\release-signing.env`
- GitHub Secrets:
  - `ANDROID_RELEASE_KEYSTORE_BASE64`
  - `ANDROID_RELEASE_KEYSTORE_PASSWORD`
  - `ANDROID_RELEASE_KEY_ALIAS`
  - `ANDROID_RELEASE_KEY_PASSWORD`
- 앱 아이콘 생성 프롬프트:
  - Forest sanctuary shield icon for Wild Haven Idle, centered leaf shield with rescued rabbit/owl silhouettes, warm green haven clearing, no text, no existing IP resemblance, no electronic pet-device visual language.
- 검증:
  - `.\gradlew.bat test assembleRelease bundleRelease` 실행 성공
  - `apksigner verify --verbose --print-certs app\build\outputs\apk\release\app-release.apk` 실행 성공
  - `jarsigner -verify app\build\outputs\bundle\release\app-release.aab` 실행 성공
  - GitHub Actions Android CI 실행 성공: test, debug APK, signed release APK/AAB, artifact upload
- 결과:
  - 동일 release keystore로 로컬 release APK/AAB 생성
  - GitHub Actions Secrets에 동일 keystore와 비밀번호 등록
  - PR/main CI와 수동 실행 또는 `v*` 태그 푸시로 signed release artifact를 생성하는 워크플로 추가
  - 보호구역/잎 방패/구조 동물 실루엣 기반 앱 아이콘 추가
- 후속 작업:
  - GitHub Actions release workflow 수동 실행 검증
  - 스토어 등록 전 앱 아이콘 최종 QA 및 필요 시 개선

## 2026-05-13

- 작업: GitHub Actions Node.js 20 deprecation 경고 대응
- 변경 파일:
  - .github/workflows/android.yml
  - .github/workflows/android-release.yml
  - CHANGELOG.md
- 검증:
  - 최신 GitHub Actions 실행 로그 확인
  - GitHub API로 action 최신 릴리스 태그 확인
  - PR CI 실행 예정
- 결과:
  - `actions/checkout` v6, `actions/setup-java` v5, `gradle/actions/setup-gradle` v6, `android-actions/setup-android` v4, `actions/upload-artifact` v7로 갱신
- 후속 작업:
  - PR CI에서 deprecation annotation이 사라졌는지 확인

## 2026-05-13

- 작업: GitHub README 개편
- 변경 파일:
  - README.md
  - CHANGELOG.md
  - HISTORY.md
- 검증:
  - README 링크와 저장소 내 참조 경로 확인
- 결과:
  - 게임 소개, MVP 구현 상태, 핵심 시스템, 기술 스택, 저장소 구조, 빌드/릴리즈, 개발 원칙, 문서 링크를 한눈에 볼 수 있도록 README 재구성
- 후속 작업:
  - 주요 화면 구현 후 README에 실제 스크린샷 추가

## 2026-05-13

- 작업: GitHub Pages 브랜딩 페이지 추가
- 변경 파일:
  - docs/index.html
  - CHANGELOG.md
  - HISTORY.md
- 검증:
  - `docs/index.html`, `docs/assets/wild-haven-icon-source.png`, `docs/GAME_DESIGN.md` 존재 확인
  - Python `html.parser`로 `docs/index.html` 구문 확인
  - GitHub Pages 설정 확인: `main` 브랜치의 `/docs` 경로 사용
- 결과:
  - Wild Haven Idle의 게임 정체성, MVP 루프, 구조 동물 목록, 개발 원칙을 소개하는 정적 브랜딩 페이지 추가
- 후속 작업:
  - PR 병합 후 `https://jeiel85.github.io/wild-haven-idle/` 반영 확인

## 2026-05-13

- 작업: APK 다운로드 경로 노출 (Release workflow 자동 게시 + GitHub Pages CTA)
- 변경 파일:
  - .github/workflows/android-release.yml
  - docs/index.html
  - .agent/tasks.md
  - .agent/progress.md
  - CHANGELOG.md
  - HISTORY.md
- 검증:
  - `docs/index.html` HTML 구문이 깨지지 않는지 미리보기 패널에서 확인
  - Release workflow는 첫 `v*` 태그 푸시 전까지 실행되지 않음 — 머지 후 v0.1.0 태그 푸시 시 검증 예정
- 결과:
  - GitHub Pages 상단 내비게이션과 히어로 CTA에 "Download APK" 진입점 추가
  - 사이드로드 안내 문구를 CTA 하단에 추가
  - `v*` 태그 푸시 시 signed APK/AAB가 GitHub Release에 자동 첨부되도록 워크플로 확장
- 후속 작업:
  - `v0.1.0` 태그 푸시로 첫 GitHub Release 생성 검증
  - Release 생성 후 GitHub Pages CTA가 정상 다운로드로 연결되는지 확인

## 2026-05-13

- 작업: Release workflow `contents: write` 권한 추가
- 변경 파일:
  - .github/workflows/android-release.yml
  - HISTORY.md
- 배경:
  - v0.1.0 태그 푸시 시 Release workflow 빌드/서명/아티팩트 업로드는 성공했으나, "Publish GitHub Release" 단계가 403 `Resource not accessible by integration`으로 실패
  - 기본 `GITHUB_TOKEN`은 PR에서 release 생성 권한이 제한되어 있어 명시적 권한 부여 필요
- 검증:
  - 워크플로 YAML 구문 확인
  - 머지 후 v0.1.0 태그를 재푸시하여 Release 자동 생성 검증 예정
- 결과:
  - `release` job에 `permissions: contents: write` 추가
- 후속 작업:
  - 실패한 v0.1.0 태그 삭제 후 main의 최신 커밋에서 재태그
