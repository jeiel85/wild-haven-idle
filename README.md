# 🏕️ Wild Haven Idle — 와일드 헤이븐

[![Android CI](https://github.com/jeiel85/wild-haven-idle/actions/workflows/android.yml/badge.svg)](https://github.com/jeiel85/wild-haven-idle/actions/workflows/android.yml)

> **숲속 보호구역을 복원하고, 다친 야생동물을 돌보는 방치형 힐링 게임**

작은 야생동물 보호구역의 관리자가 되어 보호 포인트를 모으고, 구조 동물의 회복을 지원하며, 도감을 채워 나가는 Android 방치형 성장 시뮬레이션입니다.

Wild Haven Idle is a local-first Android idle simulation about restoring a wildlife sanctuary.

<p align="center">
  <img src="docs/assets/wild-haven-icon-source.png" alt="Wild Haven Idle app icon" width="220">
</p>

## 프로젝트 상태

| 항목 | 상태 |
|---|---|
| 개발 단계 | MVP 개발 중 |
| 플랫폼 | Android |
| Application ID | `com.jeiel85.wildhavenidle` |
| 현재 버전 | `0.1.0` |
| 저장 방식 | 로컬 우선 |
| CI | GitHub Actions |
| 릴리즈 산출물 | APK, AAB |

## 게임 소개

플레이어는 작은 보호구역의 관리자가 됩니다. 접속하지 않는 동안에도 보호 포인트가 쌓이고, 다시 돌아오면 오프라인 보상을 확인한 뒤 보호구역을 개선하거나 구조 동물의 회복 단계를 올릴 수 있습니다.

핵심 플레이 루프:

```text
앱 접속
→ 오프라인 보상 확인
→ 보호 포인트 수령
→ 회복 지원 또는 보호구역 개선
→ 신규 구조 동물/서식지 해금
→ 도감 업데이트
→ 다시 방치
```

## MVP 범위

| 기능 | MVP | 현재 상태 |
|---|---:|---|
| 보호 포인트 자동 생산 | 포함 | 완료 |
| 오프라인 보상 계산 | 포함 | 완료 |
| 구조 동물 5종 | 포함 | 완료 |
| 회복 단계 시스템 | 포함 | 완료 |
| 보호구역 레벨업 | 포함 | 완료 |
| 도감 화면 | 포함 | 완료 |
| 동물 회복 목록 | 포함 | 완료 |
| 로컬 저장 | 포함 | 완료 |
| 설정 화면 + 데이터 초기화 | 포함 | 완료 |
| 화면 간 이동 (Navigation) | 포함 | 완료 |
| 로그인/클라우드/서버 | 제외 | 제외 |
| 광고/결제/분석 SDK | 제외 | 제외 |
| 푸시 알림 | 제외 | 제외 |

## 핵심 시스템

### 보호 포인트

`carePoint`는 MVP의 기본 성장 재화입니다. 보호구역 레벨과 구조 동물의 회복 단계에 따라 초당 생산량이 증가합니다.

### 구조 동물

| ID | 이름 | 희귀도 | 기본 보너스 | 해금 조건 |
|---|---|---|---:|---|
| `rabbit_001` | 숲토끼 | COMMON | 0.2/sec | 최초 시작 |
| `fox_001` | 붉은여우 | COMMON | 0.5/sec | carePoint 300 보유 |
| `deer_001` | 어린 사슴 | UNCOMMON | 1.2/sec | 숲토끼 회복 단계 5 |
| `owl_001` | 밤부엉이 | UNCOMMON | 1.8/sec | 총 생산량 5/sec |
| `lynx_001` | 스라소니 | RARE | 3.5/sec | 보호 중 동물 4종 |

### 회복 단계

| 단계 | 명칭 | 의미 |
|---:|---|---|
| 1 | 구조 직후 | 보호구역에 막 도착한 상태 |
| 5 | 안정 | 기본적인 회복을 마친 상태 |
| 10 | 적응 | 서식지에 적응한 상태 |
| 20 | 보호 완료 | 보호구역에서 안정적으로 지내는 상태 |

## 기술 스택

| 영역 | 선택 |
|---|---|
| 언어 | Kotlin |
| UI | Jetpack Compose, Material 3 |
| 아키텍처 | MVVM 또는 MVI-lite |
| 비동기 | Kotlin Coroutines / Flow |
| 로컬 저장 | AndroidX DataStore |
| DI | 수동 DI 우선, 필요 시 Hilt 검토 |
| 빌드 | Gradle Kotlin DSL |
| 테스트 | JUnit |
| CI/CD | GitHub Actions |
| 최소 SDK | 26 |
| Target SDK | 36 |

## 저장소 구조

```text
app/
  src/main/java/com/jeiel85/wildhavenidle/
    core/           공통 시간, 포맷, 디자인 유틸
    data/           로컬 저장소와 데이터 모델
    domain/         밸런스 계산, 정의, 유스케이스
    presentation/   Compose 화면과 ViewModel
    navigation/     화면 전환 구조
docs/               기획, 기술, 밸런스, 아트, 윤리 정책 문서
.agent/             AI 에이전트 작업 상태와 의사결정 기록
.github/workflows/  Android CI와 release build workflow
```

## 빠른 시작

필수 환경:

- JDK 17 이상
- Android SDK Platform 36
- Android Build Tools 36.0.0
- Windows에서는 `gradlew.bat`, Linux/macOS/CI에서는 `./gradlew` 사용

로컬 SDK 경로가 자동 인식되지 않으면 저장소 루트의 `local.properties`에 개인 환경 경로를 설정합니다. 이 파일은 커밋하지 않습니다.

```properties
sdk.dir=C\:\\Users\\<you>\\AppData\\Local\\Android\\Sdk
```

검증:

```bash
./gradlew test
./gradlew assembleDebug
```

Windows PowerShell:

```powershell
.\gradlew.bat test
.\gradlew.bat assembleDebug
```

## 릴리즈 빌드

릴리즈 APK/AAB는 keystore를 저장소 밖에 보관하고, 로컬 환경 변수 또는 GitHub Actions Secrets로만 주입합니다.

필요한 환경 변수:

```text
ANDROID_RELEASE_KEYSTORE_PATH
ANDROID_RELEASE_KEYSTORE_PASSWORD
ANDROID_RELEASE_KEY_ALIAS
ANDROID_RELEASE_KEY_PASSWORD
```

로컬 release 빌드:

```bash
./gradlew assembleRelease bundleRelease
```

GitHub Actions에서는 다음 Secrets를 사용합니다.

```text
ANDROID_RELEASE_KEYSTORE_BASE64
ANDROID_RELEASE_KEYSTORE_PASSWORD
ANDROID_RELEASE_KEY_ALIAS
ANDROID_RELEASE_KEY_PASSWORD
```

현재 release workflow:

- `Android CI`: PR/main 검증, debug APK, signed release APK/AAB artifact 생성
- `Android Release`: 수동 실행 또는 `v*` 태그 푸시로 signed release artifact 생성

## 개발 원칙

Wild Haven Idle은 야생동물 보호구역 복원 게임입니다. 동물을 소유물처럼 다루거나 접속하지 않았다는 이유로 피해를 주는 구조를 만들지 않습니다.

MVP에서는 다음을 추가하지 않습니다.

- 네트워크 권한
- 로그인 또는 계정
- 클라우드 동기화
- 광고 SDK
- 결제 SDK
- 분석 SDK
- 푸시 알림
- 실제 기부 또는 현실 단체 연계 암시

자세한 정책은 [IP and Ethics](docs/IP_AND_ETHICS.md)를 확인하세요.

## 문서

| 문서 | 내용 |
|---|---|
| [AGENTS.md](AGENTS.md) | AI 코딩 에이전트 작업 규칙 |
| [GAME_DESIGN.md](docs/GAME_DESIGN.md) | 게임 기획과 MVP 범위 |
| [TECH_SPEC.md](docs/TECH_SPEC.md) | Android 기술 설계 |
| [BALANCE.md](docs/BALANCE.md) | 생산량, 비용, 해금 밸런스 |
| [ART_DIRECTION.md](docs/ART_DIRECTION.md) | 아트 방향과 금지 표현 |
| [ASSET_LICENSES.md](docs/ASSET_LICENSES.md) | 에셋 출처와 라이선스 기록 |
| [IP_AND_ETHICS.md](docs/IP_AND_ETHICS.md) | IP 안전성과 윤리 정책 |
| [ROADMAP.md](docs/ROADMAP.md) | 단계별 개발 계획 |
| [MIGRATION.md](docs/MIGRATION.md) | 저장 데이터 마이그레이션 정책 |

## 작업 흐름

1. `main`을 최신 상태로 동기화합니다.
2. 작업별 브랜치를 만듭니다.
3. 코드와 관련 문서를 함께 갱신합니다.
4. `./gradlew test`와 필요한 빌드를 실행합니다.
5. 변경 범위를 확인한 뒤 커밋합니다.
6. PR에서 GitHub Actions 결과를 확인합니다.

## 라이선스

코드 라이선스는 [LICENSE](LICENSE)를 확인하세요.

이미지, 아이콘, 사운드 등 에셋은 별도 출처와 사용 권한을 [ASSET_LICENSES.md](docs/ASSET_LICENSES.md)에 기록합니다.
