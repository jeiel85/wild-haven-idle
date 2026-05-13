# HISTORY.md

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
- 결과:
  - DataStore Preferences 기반으로 GameState를 저장/로드
  - 앱 시작 시 오프라인 보상을 계산해 보호 포인트에 반영
  - 홈 화면에서 초당 생산량만큼 보호 포인트가 증가
- 후속 작업:
  - 구조 동물 회복 목록 구현
  - 보호구역 개선 기능 구현
  - 도감, 설정, 데이터 초기화 화면 구현
