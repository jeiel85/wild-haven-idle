# CHANGELOG.md

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
