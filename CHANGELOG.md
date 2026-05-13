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

### Verification
- 문서 패키지 생성 확인
- Android 빌드 및 테스트는 아직 실행하지 않음

## Unreleased - 2026-05-13

### Added
- Android Compose 프로젝트 초기 골격 추가
- MVP 도메인 모델, 구조 동물 정의, BalanceCalculator, 홈 화면 초안 추가
- BalanceCalculator 단위 테스트 추가
- Gradle Wrapper와 Android 프로젝트용 `.gitignore` 추가
- 게임 정체성을 드러내는 앱 런처 아이콘 추가

### Build / CI
- AGP 9.2.0, Gradle 9.4.1, Compose BOM 2026.04.01 기반으로 빌드 설정 구성
- GitHub Actions에서 Android SDK 플랫폼과 빌드 도구를 설치하도록 구성
- 환경 변수 기반 release signing config 추가
- GitHub Actions PR/main CI와 수동/태그 릴리즈 워크플로에서 signed release APK/AAB 생성 추가
- Linux CI에서 실행할 수 있도록 `gradlew` 실행 권한 설정

### Verification
- `.\gradlew.bat test assembleRelease bundleRelease` 실행 성공
- `apksigner verify --verbose --print-certs app-release.apk` 실행 성공
- `jarsigner -verify app-release.aab` 실행 성공
