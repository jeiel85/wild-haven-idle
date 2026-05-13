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
- DataStore Preferences 기반 GameState 저장/로드 추가
- 홈 화면 보호 포인트 자동 증가와 오프라인 보상 안내 추가

### Build / CI
- AGP 9.2.0, Gradle 9.4.1, Compose BOM 2026.04.01 기반으로 빌드 설정 구성
- GitHub Actions에서 Android SDK 플랫폼과 빌드 도구를 설치하도록 구성
- AndroidX DataStore 1.2.1과 Lifecycle ViewModel 2.10.0 의존성 추가

### Verification
- `.\gradlew.bat test` 실행 성공
- `.\gradlew.bat assembleDebug` 실행 성공
