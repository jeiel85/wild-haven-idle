# HISTORY.md

## 2026-05-23 (오후) — v0.6.1 도메인 통합

- 작업: v0.6.0의 디자인 셸을 기존 도메인과 연결해 실제 게임 진행 상태가 새 UI에 반영되도록 통합.
- 변경 파일:
  - `app/build.gradle.kts` (versionCode 7 → 8, versionName 0.6.0 → 0.6.1, `buildFeatures.buildConfig = true`)
  - `app/src/main/java/com/jeiel85/wildhavenidle/renew/MainActivity.kt` (WildHavenViewModel 교체 — GameRepository 주입, runPassiveTick → DataStore.addCarePoint 호출, supportRehabilitation → supportAnimalRecovery, purchaseRestoration → upgradeSanctuary, discoverWildlife no-op; ViewModelFactory 추가; onCreate에서 GameRepository 생성; hero emoji 매핑 5종 확장; 설정 버전 → BuildConfig.VERSION_NAME; HabitatRestorationScreen에서 비활성 카드 "준비 중" 표시)
  - `app/src/main/java/com/jeiel85/wildhavenidle/renew/DomainMapping.kt` (신규 — `buildWildlifeList` / `buildRestorations` / `heroEmojiFor` / `cardIconFor` / `isRestorationEnabled` / `SANCTUARY_MAIN_ID`)
  - `CHANGELOG.md` (v0.6.1 추가, Known Limitations 명시)
  - `store-release-notes/v0.6.1.txt` (신규, ko 277 / en 439)
  - `HISTORY.md`, `.agent/progress.md`
- 산출물:
  - AAB: `D:\Project\wild-haven-idle\app\build\outputs\bundle\release\app-release.aab`
  - 출시 노트: `store-release-notes/v0.6.1.txt`
  - 바탕화면 복사본: `wild-haven-idle-v0.6.1.aab`, `wild-haven-idle-v0.6.1-release-notes.txt`
- 검증:
  - `./gradlew :app:compileDebugKotlin` 성공 (HelpOutline/VolumeUp AutoMirrored warning 2건 — 원본 디자인 코드 보존)
  - `./gradlew :app:testDebugUnitTest` 성공
  - `./gradlew :app:assembleDebug` 성공
  - `./gradlew :app:assembleRelease` 성공 (R8 활성)
  - `jarsigner -verify -strict app-release.aab` → 통과
  - 실기기 검증 (Samsung 1080×2340, 480dpi): 5개 탭 정상, 보호 포인트 1초마다 +1.2 누적, 숲토끼 hero 5%, 도감 5종 분기, 설정 버전 v0.6.0 표시(현재 빌드 기준)
- 결과 (사용자 관점):
  1. v0.6.0의 mock 화면들이 진짜 GameRepository를 읽고 쓴다 — 보호 포인트·동물 회복도·sanctuaryLevel 모두 실제 저장 데이터
  2. 앱 종료 후 재시작해도 진행 상태 유지, 오프라인 보상 자동 정산
  3. 회복 지원 / 보호구역 환경 정비(첫 번째 카드) 실제 동작
- Known Limitations (v0.6.2+ 예정):
  - 관찰 기록 카드의 "조용한 안식 제공 🐾 25" 라벨이 mock이라 실제 도메인 비용(stage 기반)과 다름
  - 잠긴 동물 카드의 "흔적 발견" 버튼 no-op (도메인은 조건 자동 unlock)
  - 일일 보호 활동 보상 카드 미표시 (도메인 자격은 보존)
  - 환경 정비 4개 카테고리 중 3개는 "준비 중" 비활성
- 후속 작업:
  - v0.6.1 태그 푸시 → Android Release workflow 자동 트리거 → GitHub Release 생성
  - Play Store `pedaiah85@gmail.com` 개발자 계정으로 업로드 (Chrome MCP로 자동화)

## 2026-05-23

- 작업: v0.6.0 릴리즈 준비 — 디자인 전면 개편 셸 (`design/renew` 브랜치에서 진행 후 main 머지)
- 변경 파일:
  - `app/build.gradle.kts` (versionCode 6 → 7, versionName 0.5.1 → 0.6.0, 의존성 `material-icons-extended` + `lifecycle-runtime-compose` 추가)
  - `gradle/libs.versions.toml` (위 두 라이브러리 카탈로그 항목 추가)
  - `app/src/main/AndroidManifest.xml` (launcher Activity → `.renew.MainActivity`, 기존 `.MainActivity`는 `exported=false`로 비활성화·코드 보존)
  - `app/src/main/java/com/jeiel85/wildhavenidle/renew/MainActivity.kt` (신규, 5개 화면 + WildHavenViewModel mock + OfflineRewardPopup)
  - `app/src/main/java/com/jeiel85/wildhavenidle/renew/ui/theme/{Color,Theme,Type}.kt` (신규, Artistic 팔레트 + Serif/SansSerif Typography)
  - `CHANGELOG.md` (v0.6.0 - 2026-05-23 추가, Known Issue 명시)
  - `store-release-notes/v0.6.0.txt` (신규, ko-KR 281자 / en-US 444자, 500자 한도 내)
  - `HISTORY.md`, `.agent/progress.md`
- 산출물:
  - AAB: `D:\Project\wild-haven-idle\app\build\outputs\bundle\release\app-release.aab`
  - 출시 노트: `store-release-notes/v0.6.0.txt`
  - 바탕화면 복사본 (Play Console 업로드용): `wild-haven-idle-v0.6.0.aab`, `wild-haven-idle-v0.6.0-release-notes.txt`
- 검증:
  - `./gradlew :app:compileDebugKotlin` 성공 (HelpOutline/VolumeUp AutoMirrored 권고 warning 2건 — 원본 디자인 코드 보존)
  - `./gradlew :app:testDebugUnitTest` 성공 (회귀 없음)
  - `./gradlew :app:assembleDebug` 성공
  - `./gradlew :app:bundleRelease` 로컬 서명 성공 (R8 활성)
  - `jarsigner -verify -strict app-release.aab` → `jar verified.`
- 결과 (사용자 관점):
  1. 진입 시 새로운 5탭 구조(보호구역/관찰 기록/서식지 환경/도감/설정) + Sanctuary Status 헤더 카드 + 하단 NavigationBar
  2. Artistic 팔레트(올리브·세이지·아이보리·EarthySand) 전면 적용, Serif Italic 헤드라인
  3. 회복 단계 배지, 야생 복귀 동행 CTA, OfflineRewardPopup 새 디자인
- ⚠️ 데이터/기능 호환성 경고 (사용자 본인 출시 강행 결정으로 진행):
  - 새 MainActivity는 `GameRepository`/`GameStateDataStore` 등 기존 도메인을 *읽지 않음* — 보호 포인트, 동물 회복 진행, 일일 보너스가 화면에서 사라짐
  - 새 UI에서 한 행동은 인메모리 mock에만 반영되며 앱 종료 시 휘발
  - 저장된 DataStore preferences는 *삭제되지 않으므로* 다음 패치에서 새 UI에 연결 가능
  - 기존 `com.jeiel85.wildhavenidle.MainActivity` 및 presentation/* 코드는 모두 보존 (참조용)
- 후속 작업:
  - 다음 패치(v0.6.1+): mock `WildHavenViewModel`을 기존 `HomeViewModel`/`ArchiveViewModel`/`GameRepository`로 교체, DataStore 영속화 복원, 일일 보너스/오프라인 보상 실제 계산 연결
  - 실기기 사이드로드 검증 시나리오: 앱 시작 / 5개 탭 전환 / 오프라인 보상 팝업 / 회복 지원 / 서식지 환경 업그레이드
  - 기존 5종 동물 PNG/헤더 PNG 자산을 새 디자인에 통합할지(현재 이모지) 또는 분류 변경(`repurpose`)할지 결정

## 2026-05-15

- 작업: v0.5.1 릴리즈 준비 — 홈 마이크로 폴리시(Phase 0.5 P4 1차)를 사용자에게 전달
- 변경 파일:
  - app/build.gradle.kts (versionCode 5 → 6, versionName 0.5.0 → 0.5.1)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/settings/SettingsScreen.kt (표기 v0.5.1)
  - CHANGELOG.md (Unreleased → v0.5.1 - 2026-05-15 승격, Build/CI · Verification 추가)
  - store-release-notes/v0.5.1.txt (신규, ko-KR 269자 / en-US 427자, 한도 500자 내)
  - HISTORY.md, .agent/progress.md
- 산출물:
  - AAB: `D:\Project\wild-haven-idle\app\build\outputs\bundle\release\app-release.aab` (4.64 MB, versionName=0.5.1, versionCode=6)
  - 출시 노트: `store-release-notes/v0.5.1.txt`
  - 바탕화면 복사본 (Play Console 업로드용):
    - `C:\Users\jeiel\OneDrive\바탕 화면\wild-haven-idle-v0.5.1.aab`
    - `C:\Users\jeiel\OneDrive\바탕 화면\wild-haven-idle-v0.5.1-release-notes.txt`
- 검증:
  - `./gradlew :app:bundleRelease` 로컬 서명 성공 — `minifyReleaseWithR8` 통과, `signReleaseBundle` 통과
  - `jarsigner -verify app-release.aab` → `jar verified.` (self-signed 경고는 keystore 특성상 정상)
  - 출시 노트 글자수 (유니코드 codepoint): ko-KR 269 / en-US 427 (500자 한도 안)
  - 실기기/Play Console 업로드는 사용자 수동 단계
- 결과:
  - v0.5.0 대비 차이점 (사용자 관점):
    1. 홈 화면 카드 순서가 `보호구역 상태 → 보호 포인트 → 지금 추천 → 다음 해금 → 일일 보상 → 보호구역 확장 → 동물 목록`으로 정렬되어 첫 스크롤에서 한눈에 읽힘
    2. *지금 추천* 카드가 가장 강한 시각 위계로 강조됨 (primaryContainer + 더 진한 그림자 + primary 버튼)
    3. 보호구역 헤더 위 Lv.N 라벨이 어떤 배경에서도 가독 가능 (반투명 알약 + 짙은 텍스트)
    4. 하단 도감/설정 버튼이 OutlinedButton으로 톤 다운 — 메인 행동이 자연스럽게 더 강조됨
    5. ART_DIRECTION.md §11 에셋 분류 규칙 항구화
  - 코드 변경은 micro-polish만 (도메인/저장 데이터 변경 없음 → 마이그레이션 위험 0)
- ⚠️ 사용자 검증 권고:
  - release APK를 실기기에 사이드로드해서 한 번 돌려봐 주세요 (R8 활성, 카드 순서·강조·헤더 라벨 가독성 변경 후 첫 사용자 노출 릴리즈)
  - 시나리오: 앱 시작 / 홈 진입 (카드 순서, *지금 추천* 강조, 헤더 Lv.N 알약 가독성) / 보호구역 탭 / 회복 지원 / 보호구역 확장 / 도감 / 설정 / 일일 보상 수령
- 후속 작업:
  - 태그 v0.5.1 푸시 → Android Release 워크플로 자동 트리거 → GitHub Release 자동 생성
  - Play Console에 AAB 업로드 + 출시 노트 ko-KR/en-US 블록 붙여넣기
  - Phase 0.5 P4 후속 (MilestoneCard 진행률 타이포, SanctuaryHeader Lv 라벨에 도감 진척 결합) 또는 Phase 0.5 P5 (모션 토큰 통일)

## 2026-05-15

- 작업: Phase 0.5 P4 1차 — 홈 카드/버튼 마이크로 폴리시 (시각 위계 정리)
- 변경 파일:
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeScreen.kt (카드 순서 재정렬, 추천 카드 강조 격상, SanctuaryUpgradeCard 강조 하향, AnimalRecoveryCard elevation 통일, 하단 도감/설정 OutlinedButton 전환, `OutlinedButton` import 추가)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/components/SanctuaryHeader.kt (Lv.N 라벨 반투명 알약 배경 + 텍스트 색 짙은 톤 조정, `background`/`padding` import 추가)
  - docs/ART_DIRECTION.md (§11 에셋 분류 규칙 신설, 현재 분류표 + 의심 에셋 처리 절차)
  - .agent/tasks.md (P4 체크리스트 일부 체크, 후속 항목 추가)
  - .agent/progress.md
  - CHANGELOG.md (Unreleased에 Changed/Documentation/Verification 추가)
- 검증:
  - `./gradlew :app:compileDebugKotlin` 성공
  - `./gradlew :app:testDebugUnitTest` 성공 (회귀 없음, 신규 도메인 변경 없음)
  - 실기기/Compose Preview 시각 회귀 점검은 수행하지 않음 — 사용자 사이드로드 또는 Studio Preview에서 후속 확인 필요
- 결과:
  - 첫 스크롤 시각 위계: 보호구역 헤더 → 보호 포인트(primary 패널) → **지금 추천(primaryContainer + md elevation + primary 버튼)** → 다음 해금 → 일일 보상 → 보호구역 확장 → 동물 목록 → 보조 navigation
  - 사용자 요구 순서 `현재 보호구역 상태 → 보호 포인트 → 추천 행동 → 보상/진행`이 카드 순서로 정착됨
  - 추천 카드와 보호구역 확장 카드가 동시에 같은 색(primaryContainer)으로 경쟁하던 문제 해소 — 추천만 강조, 확장은 보조 카드로 톤 다운
  - 하단 도감/설정이 컬러풀한 채움 버튼에서 OutlinedButton으로 바뀌면서 *메인 행동이 자연스럽게 더 강조됨*
  - 보호구역 헤더 Lv.N 라벨이 잎 영역 위에서도 식별 가능 (반투명 알약 + 짙은 텍스트)
  - 에셋 처리 정책이 ART_DIRECTION.md §11로 항구화됨 — 의심 에셋이 보일 때 무작정 삭제·교체가 아니라 분류 먼저
- 후속 작업:
  - 사용자 사이드로드 또는 Android Studio에서 `HomeScreenPreview` / `HomeScreenSmallScreenPreview` 시각 회귀 점검 (특히 360dp에서 OutlinedButton 두 개 텍스트 잘림 여부, 헤더 Lv 알약 가독성)
  - Phase 0.5 P4 후속:
    - MilestoneCard 진행률 타이포 추가 정돈
    - SanctuaryHeader Lv 라벨에 도감 진척 등 결합 검토
  - Phase 0.5 P5: 모션 timing/easing 토큰 통일
  - 또는 기능 트랙으로 전환 (Phase 2 P2 첫 회복 마일스톤 축하 다이얼로그)

## 2026-05-15

- 작업: 다음 세션용 디자인 개선 인계 메모 작성
- 변경 파일:
  - .agent/session-handoff.md
  - .agent/tasks.md
  - .agent/progress.md
  - HISTORY.md
- 검증:
  - 문서 변경만 수행
  - Gradle 빌드/테스트는 실행하지 않음
- 결과:
  - 현재 디자인 문제를 "에셋 전면 교체"가 아니라 "홈 화면 위계/카드/버튼 마이크로 폴리시"로 먼저 다루기로 정리
  - 기존 동물 5종 PNG와 보호구역 헤더 PNG는 우선 유지 후보로 두고, 의심 에셋만 `keep` / `repurpose` / `replace`로 분류하도록 기록
  - 다음 세션의 우선 작업을 Commercialization Phase 0.5 P4로 명시
- 후속 작업:
  - 홈 화면 스크린샷 또는 Compose Preview를 기준으로 P4 카드/버튼 마이크로 폴리시 착수
  - 디자인 변경 후 `./gradlew :app:compileDebugKotlin` 및 `./gradlew :app:testDebugUnitTest` 실행

## 2026-05-14

- 작업: v0.5.0 릴리즈 준비 — 추천 카드 + 가독성 + R8 + 일일 보상을 한 릴리즈로 묶음
- 변경 파일:
  - app/build.gradle.kts (versionCode 4 → 5, versionName 0.4.0 → 0.5.0)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/settings/SettingsScreen.kt (표기 v0.5.0)
  - CHANGELOG.md (Unreleased → v0.5.0 승격, Build/CI · Verification 추가)
  - store-release-notes/v0.5.0.txt (신규, ko-KR 222자 / en-US 405자)
  - HISTORY.md, .agent/progress.md
- 산출물:
  - AAB: `D:\Project\wild-haven-idle\app\build\outputs\bundle\release\app-release.aab` (4.85 MB, versionName=0.5.0, versionCode=5)
  - 출시 노트: `store-release-notes/v0.5.0.txt`
  - 서명 인증서: `CN=Wild Haven Idle, OU=Release, O=Jeiel85` (RSA 4096)
- 검증:
  - `./gradlew :app:bundleRelease` 성공 — `minifyReleaseWithR8` 통과
  - `jarsigner -verify` → `jar verified.`
  - AAB 매니페스트 versionName=0.5.0 확인
  - `BUNDLE-METADATA/com.android.tools.build.obfuscation/proguard.map` (24 MB) 자동 포함 → Play Console 디오브퓨스케이션 준비 완료
  - `baseline.prof`도 자동 포함 (첫 실행 속도 보너스)
  - 출시 노트 글자수 (Python 유니코드 카운트): ko-KR 222 / en-US 405 (한도 500 안)
  - 실기기/Play Console 업로드는 사용자 수동 단계
- 결과:
  - v0.4.0 대비 차이점:
    1. 홈에 *지금 추천* 단일 다음 행동 카드 (Phase 1 P2)
    2. 360dp 좁은 화면 가독성 보호 + 큰 숫자 시나리오 Preview (Phase 1 P3)
    3. R8 코드/리소스 축소 활성화 — Play Console 가독화 파일 자동 인식, AAB 절반 크기 (10.2 MB → 4.85 MB)
    4. 로컬 일일 보호 활동 보상 카드 (Phase 2 P1) — 자정 경계 1일 1회, 광고/결제 없음
- ⚠️ 사용자 검증 권고:
  - 다음 출시 전에 release APK를 실기기에 사이드로드해서 한 번 돌려봐 주세요 (R8 활성화 후 첫 사용자 노출 릴리즈)
  - 시나리오: 앱 시작 / 홈 진입 / 보호구역 탭 / 회복 지원 / 보호구역 확장 / 도감 / 설정 / **일일 보상 수령** / **추천 카드 누르기**
- 후속 작업:
  - 태그 v0.5.0 푸시 → Android Release 워크플로 자동 트리거 → GitHub Release 자동 생성
  - Play Console에 AAB 업로드 + 출시 노트 ko-KR/en-US 블록 붙여넣기

## 2026-05-14

- 작업: Phase 2 P1 — 일일 보호 활동 보상 (로컬 자정 경계 1일 1회)
- 변경 파일:
  - app/src/main/java/com/jeiel85/wildhavenidle/data/model/GameState.kt (lastDailyBonusClaimedAtMillis 추가)
  - app/src/main/java/com/jeiel85/wildhavenidle/data/local/GameStateDataStore.kt (키 추가, 마이그레이션)
  - app/src/main/java/com/jeiel85/wildhavenidle/data/repository/GameRepository.kt (claimDailyBonus)
  - app/src/main/java/com/jeiel85/wildhavenidle/domain/dailybonus/DailyBonus.kt (신규: DailyBonusRules)
  - app/src/main/java/com/jeiel85/wildhavenidle/domain/usecase/BuildHomeUiStateUseCase.kt (자격 판정 + DailyBonusOffer 채움, invoke에 nowMillis 인자)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/DailyBonusOffer.kt (신규)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeUiState.kt (dailyBonus 필드)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeViewModel.kt (claimDailyBonus 메서드)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeScreen.kt (DailyBonusCard, 콜백 와이어링)
  - app/src/test/java/com/jeiel85/wildhavenidle/domain/dailybonus/DailyBonusRulesTest.kt (신규, 6건)
  - app/src/test/java/com/jeiel85/wildhavenidle/domain/usecase/BuildHomeUiStateUseCaseTest.kt (2건 추가)
  - CHANGELOG.md, .agent/tasks.md, .agent/progress.md
- 검증:
  - `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest` 성공
  - 총 20건 통과 (BalanceCalculator 5 + DailyBonusRules 6 + BuildHomeUiStateUseCase 9)
  - 신규 테스트: 자정 경계 자격, 시간대 영향, 생산량 스케일링, 0 생산량 하한 클램프, UiState 자격 통과/차단
  - 1차 시도에서 0.1/sec 케이스의 보상 가정이 잘못돼 한 건 실패 → 수정 후 재실행 성공
  - 실기기 동작 확인은 미수행 — 다음 새 버전 만들기 전 사이드로드 권장 (자정 직후 카드 노출 회복 시나리오 포함)
- 결과:
  - 광고/결제/네트워크 없는 순수 로컬 일일 접속 보상 도입
  - 보상량은 현재 생산량 × 3시간 (오프라인 보상 8시간 상한보다 작아 일관성), 매우 초반에는 50pt 하한
  - 자격 판정은 LocalDate 기반 자정 경계 비교 (시간대 변경에도 정확)
  - 다이얼로그가 아닌 카드 형태 — 사용자 자율성 존중, 무시 가능, 능동 수령 (감정 압박형 강제 회피)
  - 기존 사용자 마이그레이션: lastDailyBonusClaimedAtMillis가 키 없음 → null로 읽음 → 다음 진입 시 자동 자격 부여
- 후속 작업:
  - Phase 2 P2: 첫 회복 마일스톤 축하 다이얼로그
  - Phase 2 P3: 오프라인 보상 다이얼로그에 다음 행동 1줄 추가
  - 또는 Phase 0.5 P4 디자인 마이크로 폴리시
  - 다음 새 버전 만들기 시 v0.5.0 자연스러운 후보

## 2026-05-14

- 작업: Phase 1 P3 — 작은 화면(360dp) 가독성 정리 + 큰 숫자 시나리오 Preview
- 변경 파일:
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/home/HomeScreen.kt (모든 카드 텍스트에 maxLines/overflow, Row weight 보호, 360dp 큰 숫자 Preview 추가)
  - app/src/main/java/com/jeiel85/wildhavenidle/presentation/archive/ArchiveScreen.kt (도감 카드 텍스트 maxLines, 발견 라벨 maxLines)
  - CHANGELOG.md, .agent/tasks.md, .agent/progress.md
- 검증:
  - `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest` 성공 (총 7건 통과 — 회귀 없음)
  - 새 Compose Preview `HomeScreenSmallScreenPreview` (widthDp=360, heightDp=800) 추가로 Android Studio에서 시각 회귀 빠르게 점검 가능
  - 실기기 360dp 화면 수동 확인은 미수행
- 결과:
  - 모든 사용자 노출 텍스트가 maxLines + overflow=Ellipsis 보호 → 어떤 숫자/문자가 들어와도 카드가 깨지지 않음
  - 좌·우 분리 `Row(SpaceBetween)` 패턴은 좌측에 `weight(1f, fill = false)` + 우측에 `padding(start)` 적용으로 우측 라벨이 잘리지 않도록 보호
  - 도감 그리드 카드에서 동물 설명(긴 한글)도 `maxLines = 3`으로 제한해 카드 높이 일관성 유지
  - 큰 숫자 Preview는 보호구역 Lv.99, 1.5M 포인트, 999/sec 생산량 등 상한 시나리오를 한눈에 점검
- 후속 작업:
  - Phase 0.5 P4: 카드/버튼 마이크로 폴리시 (그림자, 모서리, 위계 정리)
  - Phase 0.5 P5: 모션 timing/easing 토큰 통일
  - Phase 2 (일일 보상, 회복 마일스톤 축하, 오프라인 다이얼로그 개선)

## 2026-05-14

- 작업: R8 코드/리소스 축소 활성화 — Play Console "가독화 파일 없음" 경고 대응
- 변경 파일:
  - app/build.gradle.kts (`isMinifyEnabled = true`, `isShrinkResources = true`)
  - app/proguard-rules.pro (줄 번호/소스 파일 보존 + 프로젝트 고유 메모)
  - CHANGELOG.md, .agent/progress.md
- 검증:
  - `./gradlew :app:bundleRelease` 성공 — `minifyReleaseWithR8` 통과
  - AAB 크기: 10.2 MB → 4.8 MB (약 53% 감소)
  - mapping.txt가 `BUNDLE-METADATA/com.android.tools.build.obfuscation/proguard.map`로 AAB에 자동 포함 (24 MB) → Play Console이 별도 업로드 없이 인식
  - baseline.prof도 함께 포함 (첫 실행 속도 개선 보너스)
  - jarsigner -verify → jar verified.
  - **⚠️ minified release APK 실기기 동작 확인 미수행** — 다음 릴리즈 전 필수
- 결과:
  - Play Console "이 App Bundle 유형과 연결된 가독화 파일이 없습니다" 경고가 다음 릴리즈부터 사라질 것으로 기대
  - 부수 효과: APK/AAB 크기 절반으로 축소 (사용자 다운로드 부담 감소)
- 주의 사항 (사용자 수동 확인 필요):
  - 다음 새 버전 만들기 전에 release APK를 실기기에 설치해 한 번 돌려볼 것
  - 의심 시나리오: 앱 시작, 홈 진입, 보호구역 탭, 회복 지원, 보호구역 확장, 도감 진입, 설정 진입
  - R8가 잘못 제거한 클래스로 인한 ClassNotFoundException/NoSuchMethodError가 출시 전에 잡혀야 함

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
