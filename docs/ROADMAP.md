# ROADMAP.md

# Wild Haven Idle 개발 로드맵

## Phase 0: 저장소 초기화

### 목표

공개 GitHub 저장소에 안전한 문서 구조와 에이전트 규칙을 먼저 만든다.

### 작업

- GitHub repository 생성: `wild-haven-idle`
- README.md 작성
- AGENTS.md 작성
- docs 구조 생성
- IP_AND_ETHICS.md 작성
- ASSET_LICENSES.md 작성
- .agent 문서 생성
- CHANGELOG.md / HISTORY.md 생성

### 완료 기준

- 저장소만 봐도 프로젝트 정체성과 금지 사항을 이해할 수 있다.
- 에이전트가 작업을 시작하기 전에 반드시 확인할 문서가 존재한다.

---

## Phase 1: Android 프로젝트 생성

### 작업

- Kotlin Android Compose 프로젝트 생성
- Application ID 설정: `com.jeiel85.wildhavenidle`
- 기본 Theme 구성
- HomeScreen 더미 UI
- GitHub Actions 기본 빌드 추가

### 완료 기준

- `./gradlew assembleDebug` 성공
- GitHub Actions 빌드 성공

---

## Phase 2: Core Logic

### 작업

- GameState
- AnimalDefinition
- ProtectedAnimal
- BalanceCalculator
- AnimalDefinitions
- 단위 테스트

### 완료 기준

- 생산량, 회복 비용, 오프라인 보상 테스트 통과

---

## Phase 3: Game Loop MVP

### 작업

- 실시간 보호 포인트 증가
- 회복 지원 기능
- 보호구역 개선 기능
- 해금 조건 검사

### 완료 기준

- 홈 화면에서 숫자가 증가한다.
- 회복 지원 후 생산량이 증가한다.
- 보호구역 개선 후 생산량이 증가한다.

---

## Phase 4: Save / Offline Reward

### 작업

- DataStore 저장
- 앱 시작 시 로드
- 마지막 저장 시각 기록
- 오프라인 보상 팝업

### 완료 기준

- 앱 재실행 후 데이터 유지
- 60초 이상 이탈 후 복귀 보상 표시
- 최대 8시간 상한 적용

---

## Phase 5: Archive / UX

### 작업

- 도감 화면
- LOCKED/DISCOVERED/PROTECTED 상태
- 숫자 포맷터
- 설정 화면
- 데이터 초기화

### 완료 기준

- MVP 수동 QA 통과
- README 기준 실행 가능
