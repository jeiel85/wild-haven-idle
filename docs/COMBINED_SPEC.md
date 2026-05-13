# Wild Haven Idle 통합 개발 설계서

## 0. 문서 개요

- 문서 버전: v0.2
- 작성일: 2026-05-13
- 대상 프로젝트: 방치형 야생동물 보호구역 성장 시뮬레이션
- 개발 방식: 1인 개발자 + AI 에이전트 기반 바이브 코딩
- 대상 플랫폼: Android 우선
- 공개 저장소 전제: GitHub Public Repository
- 핵심 원칙: 라이선스 안전성, 윤리적 설계, 로컬 우선, 작은 MVP, 자동화 우선

> 주의: 이 문서는 개발 기획 및 리스크 저감 가이드이며, 법률 자문은 아니다. 출시 전 상표/저작권/스토어 정책 관련 최종 판단은 개발자가 별도로 확인한다.

---

# 1. 추천 레포지토리 이름

## 1.1 최우선 추천

```text
wild-haven-idle
```

### 추천 이유

- 기존 전자펫/다마고치류 IP를 직접 연상시키지 않는다.
- `wild`, `haven`, `idle` 조합이 게임의 핵심 정체성을 잘 설명한다.
- 공개 GitHub 저장소명으로 자연스럽고 검색 가능성이 있다.
- Android 외 플랫폼으로 확장해도 어색하지 않다.

## 1.2 Android 전용 레포명 후보

```text
wild-haven-idle-android
```

Android 앱만 독립적으로 운영할 계획이라면 이 이름이 더 명확하다.

## 1.3 최종 추천값

```text
Project Name: Wild Haven Idle
Repository: https://github.com/jeiel85/wild-haven-idle.git
Main Branch: main
Application ID: com.jeiel85.wildhavenidle
Primary Spec: docs/GAME_DESIGN.md
Technical Spec: docs/TECH_SPEC.md
Balance Spec: docs/BALANCE.md
Art Direction: docs/ART_DIRECTION.md
Ethics / IP Policy: docs/IP_AND_ETHICS.md
Task Document: .agent/tasks.md
Progress Document: .agent/progress.md
Decision Log: .agent/decisions.md
History Document: HISTORY.md
Changelog: CHANGELOG.md
Build/Test Commands: ./gradlew test, ./gradlew assembleDebug
Release Trigger: tag push
CI System: GitHub Actions
Expected Assets: APK, AAB
```

---

# 2. 제품 정의

## 2.1 장르

```text
방치형 야생동물 보호구역 성장 시뮬레이션
```

기존 표현인 “방치형 수집 육성”은 내부적으로는 맞지만, 외부 스토어 설명과 브랜딩에서는 “펫 키우기”처럼 보이지 않도록 다음 표현을 우선 사용한다.

- 야생동물 보호구역 운영
- 서식지 복원
- 구조 동물 회복
- 도감 수집
- 방치형 성장

## 2.2 한 줄 소개

```text
작은 야생동물 보호구역을 운영하며 구조 동물을 회복시키고, 서식지를 복원하고, 도감을 채워 나가는 방치형 성장 시뮬레이션.
```

## 2.3 핵심 가치

| 가치 | 설명 |
|---|---|
| 부담 없는 플레이 | 짧게 접속해도 보상과 성장이 느껴진다. |
| 보호구역 성장 | 단일 캐릭터가 아니라 보호구역 자체를 키운다. |
| 구조와 회복 | 동물을 소유하는 느낌보다 보호하고 회복시키는 느낌을 준다. |
| 도감 수집 | 다양한 야생동물을 발견하고 기록한다. |
| 윤리적 수익화 | 동물 보호 테마를 과금 압박으로 사용하지 않는다. |
| IP 안전성 | 기존 전자펫, 다마고치류, 특정 캐릭터 IP를 참조하거나 재현하지 않는다. |

---

# 3. 법적·윤리적 안전 지침

## 3.1 핵심 원칙

```text
이 프로젝트는 기존 전자펫, 다마고치류 제품, 특정 캐릭터 IP, 기존 게임의 UI/캐릭터/명칭/그래픽 표현을 참조하거나 재현하지 않는다.

게임의 정체성은 “펫 키우기”가 아니라 “야생동물 보호구역 운영과 서식지 복원”이다.

모든 캐릭터, 아이콘, UI, 문구, 사운드, 폰트는 직접 제작하거나 상업적 사용 권한이 명확한 리소스만 사용한다.

동물 보호 테마를 과금 압박이나 감정 착취 수단으로 사용하지 않는다.

실제 기부, 구조 활동, 환경 단체와의 연계를 암시하는 표현은 사실인 경우에만 사용한다.
```

## 3.2 금지 표현

외부 문서, README, 앱 설명, 스토어 문구, 마케팅 이미지, 코드 주석에서 다음 표현을 사용하지 않는다.

| 금지 표현 | 이유 |
|---|---|
| Tamagotchi | 타사 상표 직접 언급 위험 |
| 다마고치 / 타마고치 | 기존 IP 직접 연상 |
| 다마고치 스타일 | 상표/브랜드 연상 및 비교 광고 위험 |
| 전자펫 | 전자펫 장르 자체는 가능하지만 본 프로젝트 정체성과 충돌 가능 |
| 알에서 부화 | 기존 전자펫류 연상 강함 |
| 펫 키우기 | 소유/관리 중심으로 보일 수 있음 |
| 먹이 주기 | 기존 펫 육성 루프와 유사하게 보일 수 있음 |
| 진화 | 펫/몬스터 수집 장르와 가까워 보일 수 있음 |

## 3.3 권장 표현

| 위험 표현 | 권장 표현 |
|---|---|
| 펫 | 구조 동물, 보호 동물 |
| 키우기 | 회복시키기, 보호하기, 적응 돕기 |
| 먹이 주기 | 돌봄 지원, 회복 지원 |
| 진화 | 회복 단계, 적응 단계, 보호 단계 |
| 알 | 구조 요청, 발견 흔적 |
| 부화 | 구조 완료 |
| 기분 | 안정도, 신뢰도 |
| 배고픔 | 회복도, 컨디션 |
| 펫 하우스 | 보호구역, 서식지 |

## 3.4 아트/IP 안전 규칙

- 흑백 LCD 전자 장난감 화면을 모방하지 않는다.
- 알 모양 디바이스 UI를 만들지 않는다.
- 도트 전자펫 감성보다 라인 아트, 자연 관찰 노트, 종이 질감, 보호구역 지도를 우선한다.
- 기존 캐릭터의 실루엣, 표정, 색 조합, 포즈를 참고하지 않는다.
- 모든 에셋은 다음 중 하나여야 한다.
  - 직접 제작
  - CC0
  - 상업적 사용 가능 라이선스
  - 유료 구매 후 게임 사용 허용 라이선스 확인 완료
- 에셋 출처는 `docs/ASSET_LICENSES.md`에 기록한다.

## 3.5 윤리적 설계 규칙

- 접속하지 않아도 동물이 죽거나 학대받는 구조를 만들지 않는다.
- “동물을 구하려면 결제하세요”처럼 감정 압박형 과금을 만들지 않는다.
- 보상형 광고는 선택형으로만 사용한다.
- 실제 기부를 하지 않는다면 기부나 현실 구조 활동을 암시하지 않는다.
- 도감 정보는 과학적 사실처럼 보이는 경우 검증 가능한 출처를 별도 기록한다.
- 아동 사용 가능성을 고려하여 결제, 광고, 개인정보 수집을 보수적으로 설계한다.

---

# 4. 세계관 및 게임 콘셉트

## 4.1 세계관

플레이어는 작은 야생동물 보호구역의 관리자가 된다. 보호구역은 처음에는 작은 숲 한 조각에 불과하지만, 시간이 지나며 보호 포인트가 쌓이고, 구조 요청이 들어오고, 동물들이 회복하며, 새로운 서식지가 복원된다.

게임의 목적은 동물을 소유하거나 생존을 강요하는 것이 아니라, 구조된 동물이 안정적으로 적응할 수 있도록 보호구역을 확장하고 개선하는 것이다.

## 4.2 핵심 플레이 문장

```text
보호 포인트를 모아 서식지를 복원하고, 구조 동물을 회복시키며, 야생동물 도감을 채워 나간다.
```

## 4.3 차별화 전략

| 구분 | 피해야 할 방향 | 본 프로젝트 방향 |
|---|---|---|
| 핵심 관계 | 펫 소유/관리 | 야생동물 보호/회복 지원 |
| 성장 목적 | 캐릭터 생존/진화 | 보호구역 복원/도감 완성 |
| 반복 행동 | 먹이 주기/청소 | 구조, 회복 지원, 서식지 개선 |
| UI 감성 | 전자 장난감/LCD | 자연 다이어리/보호구역 지도 |
| 실패 구조 | 방치하면 죽음 | 방치해도 보상 상한만 적용 |
| 수익화 | 구출 압박 결제 | 선택형 광고/꾸미기 중심 |

---

# 5. 핵심 게임 루프

## 5.1 기본 루프

```text
앱 접속
→ 오프라인 보상 확인
→ 보호 포인트 수령
→ 동물 회복 단계 상승 또는 보호구역 개선
→ 신규 구조 동물/서식지 해금
→ 도감 업데이트
→ 다시 방치
```

## 5.2 플레이 세션 구조

| 구간 | 사용자 행동 | 기대 감정 |
|---|---|---|
| 접속 직후 | 오프라인 보상 확인 | 돌아왔을 때 성장한 느낌 |
| 10초 이내 | 보호 포인트 확인 | 숫자가 쌓이는 만족감 |
| 30초 이내 | 회복 단계 상승 또는 보호구역 개선 | 즉시 성취감 |
| 1분 이내 | 도감/해금 조건 확인 | 다음 목표 인식 |
| 종료 전 | 다시 방치 | 부담 없는 이탈 |

---

# 6. MVP 범위

## 6.1 MVP에 반드시 포함

1. 보호 포인트 자동 생산
2. 오프라인 보상 계산
3. 구조 동물 5종
4. 회복 단계 또는 레벨 시스템
5. 보호구역 레벨업
6. 도감 화면
7. 로컬 저장
8. 기본 설정 화면
9. 데이터 초기화 기능
10. IP/윤리 정책 문서

## 6.2 MVP에서 제외

- 로그인
- 클라우드 동기화
- 서버 API
- 광고
- 결제
- 분석 SDK
- 푸시 알림
- 랭킹/친구
- 실시간 이벤트
- 실제 환경 단체 연동
- 실제 기부 표현

## 6.3 MVP 성공 기준

- 앱을 껐다 켜도 데이터가 유지된다.
- 마지막 저장 시각 기준으로 오프라인 보상이 계산된다.
- 보호 포인트로 보호구역과 동물 회복 단계를 올릴 수 있다.
- 5종 구조 동물이 조건에 따라 해금된다.
- 도감에서 잠김/발견/보호 중 상태가 구분된다.
- 기존 전자펫 IP를 연상시키는 앱명, UI, 캐릭터, 문구가 없다.
- README만 보고 프로젝트 목적과 개발 규칙을 이해할 수 있다.

---

# 7. 시스템 설계

## 7.1 재화 시스템

### 기본 재화

| 재화명 | 코드명 | 설명 |
|---|---|---|
| 보호 포인트 | `carePoint` | 기본 성장 재화 |
| 복원 포인트 | `restorePoint` | 추후 서식지 복원 특화 재화 |
| 구조 티켓 | `rescueTicket` | 추후 구조 요청 수락 재화 |

MVP에서는 `carePoint`만 구현한다.

### 생산 공식

```text
초당 보호 포인트 생산량 = 보호구역 기본 생산량 + 구조 동물 회복 보너스 합계
```

### 보호구역 생산량

```text
보호구역 기본 생산량 = 보호구역 레벨 × 1.0 carePoint/sec
```

### 구조 동물 생산 보너스

```text
동물 보너스 = 기본 보너스 × 회복 단계^1.15
```

### 오프라인 보상

```text
오프라인 시간 초 = 현재 시각 - 마지막 저장 시각
보상 = 초당 생산량 × 오프라인 시간 초
최대 인정 시간 = 8시간
```

### 시간 조작 대응

MVP에서는 서버 시간을 사용하지 않는다.

- `현재 시각 < 마지막 저장 시각`이면 보상 0 처리
- 비정상적으로 큰 시간 차이는 최대 8시간으로 제한
- 추후 서버/클라우드 도입 전까지 완전한 시간 조작 방지는 목표로 하지 않음

---

## 7.2 구조 동물 시스템

### 7.2.1 데이터 필드

| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | String | 동물 고유 ID |
| `nameKo` | String | 한국어 이름 |
| `nameEn` | String | 영어 이름 |
| `species` | String | 종 이름 |
| `rarity` | Enum | 희귀도 |
| `baseSupportBonus` | Double | 기본 생산 보너스 |
| `maxRecoveryStage` | Int | 최대 회복 단계 |
| `habitatType` | Enum | 선호 서식지 |
| `descriptionKo` | String | 한국어 도감 설명 |
| `descriptionEn` | String | 영어 도감 설명 |
| `unlockCondition` | UnlockCondition | 해금 조건 |

### 7.2.2 희귀도

```kotlin
enum class Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY
}
```

MVP에서는 `COMMON`, `UNCOMMON`, `RARE`만 사용한다.

### 7.2.3 MVP 구조 동물 5종

| ID | 이름 | 희귀도 | 기본 보너스 | 해금 조건 |
|---|---|---|---:|---|
| `rabbit_001` | 숲토끼 | COMMON | 0.2/sec | 최초 시작 시 자동 등록 |
| `fox_001` | 붉은여우 | COMMON | 0.5/sec | carePoint 300 보유 |
| `deer_001` | 어린 사슴 | UNCOMMON | 1.2/sec | 숲토끼 회복 단계 5 달성 |
| `owl_001` | 밤부엉이 | UNCOMMON | 1.8/sec | 총 생산량 5/sec 달성 |
| `lynx_001` | 스라소니 | RARE | 3.5/sec | 보호 중 동물 4종 달성 |

### 7.2.4 회복 단계

기존 “진화” 용어를 쓰지 않고 다음 단계로 표현한다.

| 단계 | 명칭 | 설명 |
|---:|---|---|
| 1 | 구조 직후 | 보호구역에 막 도착한 상태 |
| 5 | 안정 | 기본적인 회복을 마친 상태 |
| 10 | 적응 | 서식지에 적응한 상태 |
| 20 | 보호 완료 | 보호구역에서 안정적으로 지내는 상태 |

### 7.2.5 회복 지원 비용

```text
회복 지원 비용 = 기본 비용 × 현재 회복 단계^1.45
```

기본 비용:

| 희귀도 | 기본 비용 |
|---|---:|
| COMMON | 100 |
| UNCOMMON | 250 |
| RARE | 700 |

---

## 7.3 보호구역 시스템

### 7.3.1 보호구역 속성

| 필드 | 타입 | 설명 |
|---|---|---|
| `level` | Int | 보호구역 레벨 |
| `baseProduction` | Double | 기본 생산량 |
| `upgradeCost` | Long | 다음 업그레이드 비용 |
| `restorationRate` | Double | 복원률 |
| `unlockedHabitatIds` | Set<String> | 해금된 서식지 ID |

### 7.3.2 업그레이드 비용

```text
보호구역 업그레이드 비용 = 200 × 현재 레벨^1.6
```

### 7.3.3 서식지 확장 계획

| 서식지 | MVP | 설명 |
|---|---|---|
| 숲 | 포함 | 기본 보호구역 |
| 습지 | 제외 | 수달, 두루미 등 추가 가능 |
| 초원 | 제외 | 사슴, 여우 확장 가능 |
| 산지 | 제외 | 스라소니, 곰 등 추가 가능 |
| 해안 | 제외 | 바다거북, 물새 등 추가 가능 |

---

## 7.4 도감 시스템

### 도감 상태

```kotlin
enum class ArchiveState {
    LOCKED,
    DISCOVERED,
    PROTECTED
}
```

| 상태 | 표시 방식 |
|---|---|
| LOCKED | 실루엣, 이름 비공개 |
| DISCOVERED | 이름, 발견 힌트 표시 |
| PROTECTED | 전체 정보, 회복 단계, 보너스 표시 |

### 도감 보너스

```text
보호 중 동물 1종당 전체 생산량 +2%
```

---

# 8. 데이터 모델

## 8.1 GameState

```kotlin
data class GameState(
    val carePoint: Double = 0.0,
    val sanctuaryLevel: Int = 1,
    val lastSavedAt: Long = System.currentTimeMillis(),
    val protectedAnimals: List<ProtectedAnimal> = emptyList(),
    val discoveredAnimalIds: Set<String> = emptySet(),
    val unlockedHabitatIds: Set<String> = setOf("forest_001")
)
```

## 8.2 AnimalDefinition

```kotlin
data class AnimalDefinition(
    val id: String,
    val nameKo: String,
    val nameEn: String,
    val species: String,
    val rarity: Rarity,
    val baseSupportBonus: Double,
    val maxRecoveryStage: Int,
    val habitatType: HabitatType,
    val descriptionKo: String,
    val descriptionEn: String,
    val unlockCondition: UnlockCondition
)
```

## 8.3 ProtectedAnimal

```kotlin
data class ProtectedAnimal(
    val animalId: String,
    val recoveryStage: Int = 1,
    val protectedAt: Long = System.currentTimeMillis()
)
```

## 8.4 UnlockCondition

```kotlin
sealed class UnlockCondition {
    data object InitialAnimal : UnlockCondition()
    data class CarePointReached(val amount: Long) : UnlockCondition()
    data class RecoveryStageReached(val animalId: String, val stage: Int) : UnlockCondition()
    data class TotalProductionReached(val productionPerSecond: Double) : UnlockCondition()
    data class ProtectedAnimalCountReached(val count: Int) : UnlockCondition()
}
```

## 8.5 HabitatType

```kotlin
enum class HabitatType {
    FOREST,
    WETLAND,
    GRASSLAND,
    MOUNTAIN,
    COAST
}
```

---

# 9. 핵심 로직 사양

## 9.1 생산량 계산

```kotlin
fun calculateSanctuaryProduction(level: Int): Double {
    return level * 1.0
}

fun calculateAnimalSupportBonus(
    definition: AnimalDefinition,
    recoveryStage: Int
): Double {
    return definition.baseSupportBonus * recoveryStage.toDouble().pow(1.15)
}

fun calculateArchiveBonusMultiplier(protectedAnimalCount: Int): Double {
    return 1.0 + protectedAnimalCount * 0.02
}

fun calculateTotalProductionPerSecond(
    sanctuaryLevel: Int,
    protectedAnimals: List<ProtectedAnimal>,
    definitions: List<AnimalDefinition>
): Double {
    val sanctuaryProduction = calculateSanctuaryProduction(sanctuaryLevel)

    val animalBonus = protectedAnimals.sumOf { protected ->
        val definition = definitions.first { it.id == protected.animalId }
        calculateAnimalSupportBonus(definition, protected.recoveryStage)
    }

    val archiveMultiplier = calculateArchiveBonusMultiplier(protectedAnimals.size)

    return (sanctuaryProduction + animalBonus) * archiveMultiplier
}
```

## 9.2 오프라인 보상 계산

```kotlin
fun calculateOfflineReward(
    lastSavedAt: Long,
    now: Long,
    productionPerSecond: Double,
    maxOfflineMillis: Long = 8 * 60 * 60 * 1000L
): Double {
    if (now <= lastSavedAt) return 0.0

    val elapsedMillis = now - lastSavedAt
    val rewardedMillis = elapsedMillis.coerceAtMost(maxOfflineMillis)
    val elapsedSeconds = rewardedMillis / 1000.0

    return productionPerSecond * elapsedSeconds
}
```

## 9.3 회복 지원 비용 계산

```kotlin
fun calculateRecoverySupportCost(
    baseCost: Double,
    currentRecoveryStage: Int
): Long {
    return (baseCost * currentRecoveryStage.toDouble().pow(1.45)).roundToLong()
}
```

## 9.4 보호구역 업그레이드 비용

```kotlin
fun calculateSanctuaryUpgradeCost(currentLevel: Int): Long {
    return (200.0 * currentLevel.toDouble().pow(1.6)).roundToLong()
}
```

---

# 10. Android 기술 설계

## 10.1 기술 스택

| 영역 | 선택 |
|---|---|
| 언어 | Kotlin |
| UI | Jetpack Compose |
| 아키텍처 | MVVM 또는 MVI-lite |
| 비동기 | Kotlin Coroutines / Flow |
| 저장소 | DataStore |
| DI | 초기에는 수동 DI, 필요 시 Hilt 검토 |
| 빌드 | Gradle Kotlin DSL |
| 테스트 | JUnit, Kotlin test |
| CI | GitHub Actions |
| 최소 SDK | 26 이상 권장 |

## 10.2 권한 정책

MVP에서는 다음 권한을 추가하지 않는다.

```text
android.permission.INTERNET
android.permission.POST_NOTIFICATIONS
광고 SDK 관련 권한
분석 SDK 관련 권한
위치 권한
계정 권한
```

네트워크, 광고, 로그인, 클라우드 저장을 추가하려면 별도 문서와 사용자 승인이 필요하다.

## 10.3 패키지 구조

```text
app/src/main/java/com/jeiel85/wildhavenidle/
├── MainActivity.kt
├── core/
│   ├── time/
│   │   └── TimeProvider.kt
│   ├── format/
│   │   └── NumberFormatter.kt
│   └── design/
│       └── Theme.kt
├── data/
│   ├── local/
│   │   ├── GameStateDataStore.kt
│   │   └── GameStateSerializer.kt
│   ├── model/
│   │   ├── GameState.kt
│   │   ├── AnimalDefinition.kt
│   │   └── ProtectedAnimal.kt
│   └── repository/
│       └── GameRepository.kt
├── domain/
│   ├── balance/
│   │   └── BalanceCalculator.kt
│   ├── definitions/
│   │   └── AnimalDefinitions.kt
│   └── usecase/
│       ├── LoadGameUseCase.kt
│       ├── TickGameUseCase.kt
│       ├── SupportAnimalRecoveryUseCase.kt
│       ├── UpgradeSanctuaryUseCase.kt
│       └── CalculateOfflineRewardUseCase.kt
├── presentation/
│   ├── home/
│   │   ├── HomeScreen.kt
│   │   ├── HomeViewModel.kt
│   │   └── HomeUiState.kt
│   ├── animals/
│   │   ├── AnimalListScreen.kt
│   │   └── AnimalListViewModel.kt
│   ├── archive/
│   │   ├── ArchiveScreen.kt
│   │   └── ArchiveViewModel.kt
│   ├── settings/
│   │   └── SettingsScreen.kt
│   └── components/
│       ├── AnimalCard.kt
│       ├── CurrencyHeader.kt
│       └── OfflineRewardDialog.kt
└── navigation/
    └── AppNavGraph.kt
```

---

# 11. 화면 설계

## 11.1 화면 목록

| 화면 | MVP | 목적 |
|---|---|---|
| 홈 | 포함 | 재화, 생산량, 보호구역 상태 확인 |
| 구조 동물 목록 | 포함 | 회복 단계 상승, 동물 상태 확인 |
| 도감 | 포함 | 수집 진행도 확인 |
| 보호구역 개선 | 포함 | 보호구역 레벨업 |
| 오프라인 보상 팝업 | 포함 | 복귀 보상 수령 |
| 설정 | 포함 | 데이터 초기화, 버전 정보 |
| 상점 | 제외 | MVP 이후 검토 |
| 이벤트 | 제외 | MVP 이후 검토 |

## 11.2 홈 화면 와이어프레임

```text
┌────────────────────────────┐
│ Wild Haven Idle            │
│ 보호 포인트: 12.4K         │
│ 생산량: 15.2 / sec         │
├────────────────────────────┤
│                            │
│       [보호구역 지도]      │
│                            │
│  숲토끼  붉은여우  사슴    │
│                            │
├────────────────────────────┤
│ [보호구역 개선]            │
│ [구조 동물] [도감] [설정]  │
└────────────────────────────┘
```

## 11.3 오프라인 보상 팝업

```text
다시 오신 것을 환영해요!

자리를 비운 동안 보호구역이 천천히 회복되었어요.

오프라인 시간: 2시간 15분
획득한 보호 포인트: 18.2K

[받기]
```

금지 문구:

```text
동물이 굶고 있었어요
늦게 와서 동물이 아파요
지금 결제하지 않으면 구조할 수 없어요
```

---

# 12. 저장 구조

## 12.1 MVP 저장 방식

- 서버 없음
- 로그인 없음
- DataStore 기반 로컬 저장
- 민감 정보 저장 없음
- 사용자 식별자 생성 없음

## 12.2 저장 대상

| 데이터 | 저장 여부 |
|---|---|
| 보호 포인트 | 저장 |
| 보호구역 레벨 | 저장 |
| 마지막 저장 시각 | 저장 |
| 보호 중 동물 목록 | 저장 |
| 회복 단계 | 저장 |
| 발견한 동물 ID | 저장 |
| 해금된 서식지 ID | 저장 |
| 설정값 | 선택 저장 |

## 12.3 마이그레이션 원칙

저장 구조 변경 시 다음 내용을 `docs/MIGRATION.md` 또는 `Decision Log`에 기록한다.

```text
변경 전 구조:
변경 후 구조:
마이그레이션 방법:
실패 시 동작:
롤백 가능 여부:
검증 방법:
```

---

# 13. 밸런스 초안

## 13.1 초반 목표

| 플레이 시간 | 목표 경험 |
|---|---|
| 첫 1분 | 보호 포인트 증가 이해, 숲토끼 확인 |
| 첫 5분 | 첫 회복 지원, 보호구역 개선 조건 확인 |
| 첫 15분 | 붉은여우 해금 |
| 첫 1시간 | 보호구역 레벨 2~3 도달 |
| 첫 하루 | 도감 3~4종 등록 |

## 13.2 시작값

| 항목 | 값 |
|---|---:|
| 시작 보호 포인트 | 0 |
| 보호구역 Lv.1 생산량 | 1/sec |
| 숲토끼 기본 보너스 | 0.2/sec |
| 숲토끼 2단계 비용 | 약 100 |
| 붉은여우 해금 조건 | carePoint 300 |
| 보호구역 Lv.2 비용 | 약 200 |
| 오프라인 보상 최대 시간 | 8시간 |

## 13.3 숫자 표기

| 값 범위 | 표기 |
|---:|---|
| 0 ~ 999 | `999` |
| 1,000 ~ 999,999 | `1.2K` |
| 1,000,000 이상 | `1.2M` |

---

# 14. 수익화 정책

## 14.1 MVP 정책

MVP에는 광고, 결제, 분석 SDK를 넣지 않는다.

## 14.2 추후 허용 가능 수익화

| 방식 | 허용 여부 | 조건 |
|---|---|---|
| 보상형 광고 | 가능 | 선택형, 강제 없음, 동물 구조 압박 금지 |
| 광고 제거 IAP | 가능 | 광고 도입 이후 검토 |
| 꾸미기 아이템 | 가능 | 생산량 과금 유도 최소화 |
| 시즌 패스 | 신중 | 콘텐츠 부담 큼, 과금 압박 금지 |
| 구조 성공률 과금 | 금지 | 동물 보호 테마 감정 착취 위험 |
| 실제 기부 암시 | 금지 | 실제 계약/기부 없으면 금지 |

---

# 15. 공개 저장소 문서 구조

```text
wild-haven-idle/
├── README.md
├── AGENTS.md
├── CHANGELOG.md
├── HISTORY.md
├── LICENSE
├── docs/
│   ├── GAME_DESIGN.md
│   ├── TECH_SPEC.md
│   ├── BALANCE.md
│   ├── ART_DIRECTION.md
│   ├── IP_AND_ETHICS.md
│   ├── ASSET_LICENSES.md
│   ├── ROADMAP.md
│   └── MIGRATION.md
├── .agent/
│   ├── tasks.md
│   ├── progress.md
│   └── decisions.md
├── .github/
│   └── workflows/
│       └── android.yml
└── app/
    └── ...
```

---

# 16. README.md 초안 구조

```md
# Wild Haven Idle

Wild Haven Idle is a local-first Android idle simulation game about restoring a small wildlife sanctuary, supporting rescued animals, and completing a wildlife archive.

## Project Goals
- Build a simple, ethical idle collection game.
- Avoid imitation of existing electronic pet games or third-party IP.
- Keep MVP local-first with no login, no ads, no analytics, and no network dependency.

## MVP Features
- Care point generation
- Offline reward calculation
- Sanctuary upgrade
- Rescued animal recovery stages
- Wildlife archive
- Local save

## Non-Goals for MVP
- No login
- No cloud sync
- No ads
- No billing
- No analytics
- No external API

## Development
See `AGENTS.md` and `docs/TECH_SPEC.md`.

## IP and Ethics
See `docs/IP_AND_ETHICS.md`.
```

---

# 17. AGENTS.md 통합 규칙

## 17.1 프로젝트 설정값

```text
Project Name: Wild Haven Idle
Repository: https://github.com/jeiel85/wild-haven-idle.git
Main Branch: main
Primary Spec: docs/GAME_DESIGN.md
History Document: HISTORY.md
Changelog: CHANGELOG.md
Task Document: .agent/tasks.md
Decision Log: .agent/decisions.md
Version Files: app/build.gradle.kts, CHANGELOG.md
Build/Test Commands: ./gradlew test, ./gradlew assembleDebug
Release Trigger: tag push
CI System: GitHub Actions
Expected Assets: APK, AAB
```

## 17.2 Automation First 원칙

에이전트는 명시된 작업 범위 안에서 가능한 한 자동으로 진행한다.

자동 진행 가능:

- 최신 소스 동기화
- 작업 범위 분석
- 관련 문서 확인
- 코드 수정
- 문서 갱신
- `CHANGELOG.md`, `HISTORY.md`, `.agent/tasks.md`, `.agent/decisions.md` 갱신
- 가벼운 로컬 검증
- 커밋 생성
- 원격 저장소 푸시
- GitHub Actions 상태 확인
- CI 실패 시 로그 확인 및 수정 커밋
- 최종 작업 보고

단, 다음은 중단 후 보고한다.

- `git reset --hard`
- `git clean -fd`
- `git push --force`
- 원격 브랜치/태그 삭제
- 사용자 데이터 삭제 가능성이 있는 변경
- 롤백 어려운 데이터 마이그레이션
- 시크릿, 인증서, API 키, 릴리즈 키 관련 변경
- 유료 서비스, 외부 API, 로그인, 결제, 분석 도구 추가
- 프로젝트 정책과 충돌하는 의존성 추가
- 라이선스, 보안, 스토어 정책 위반 가능성이 있는 변경

## 17.3 기본 커뮤니케이션 규칙

- 작업 요약, 커밋 메시지, 이슈 코멘트는 한국어를 기본으로 한다.
- 기술 용어는 필요하면 영어를 병기한다.
- 불확실한 부분은 추측으로 단정하지 않는다.
- 수행한 작업과 확인하지 못한 작업을 구분한다.
- 검증하지 않은 테스트/빌드를 성공으로 기록하지 않는다.

## 17.4 작업 시작 절차

```bash
git fetch origin
git checkout main
git pull origin main
git status
```

그 다음 아래 문서를 순서대로 확인한다.

1. `AGENTS.md`
2. `docs/GAME_DESIGN.md`
3. `docs/TECH_SPEC.md`
4. `.agent/tasks.md`
5. `HISTORY.md`
6. `.agent/decisions.md`
7. `CHANGELOG.md`
8. 관련 `README.md`, `docs/`, CI/CD 설정 파일

## 17.5 금지 및 사전 승인 필요 항목

사용자가 명시적으로 승인하지 않으면 다음을 진행하지 않는다.

- 네트워크 권한 추가
- 신규 API 연동
- 로그인, 계정, 인증 기능
- 분석, 광고, 추적 SDK
- proprietary SDK, remote config, crash reporting SDK
- 민감 정보 수집 또는 외부 전송
- DRM 우회, 접근 제한 우회, 불법 다운로드, 보안 우회 기능
- 기존 앱/서비스의 이름, 아이콘, 색상, 문구, 화면 구성 복제
- 릴리즈 키, API 키, 토큰, 인증서 커밋
- 대규모 기술 스택 변경
- 실제 기부/환경 단체 연계 암시 문구 추가
- 기존 전자펫/다마고치류를 연상시키는 앱명, UI, 캐릭터, 설명 추가

## 17.6 Dependency Rules

의존성 추가 전 확인:

- 표준 라이브러리나 기존 코드로 해결 가능한가?
- 라이선스가 공개 저장소와 앱 배포에 적합한가?
- 앱 크기와 빌드 시간 영향은 적절한가?
- 유지보수 상태가 양호한가?
- 보안 취약점은 없는가?
- Android 대상 플랫폼과 호환되는가?

MVP에서 피해야 할 의존성:

- 광고 SDK
- 분석 SDK
- 로그인 SDK
- 결제 SDK
- 원격 설정 SDK
- 클라우드 DB SDK
- 불필요한 대형 게임 엔진

## 17.7 문서화 규칙

코드가 바뀌면 관련 문서를 함께 갱신한다.

| 변경 유형 | 갱신 문서 |
|---|---|
| 주요 기능 | `docs/GAME_DESIGN.md`, `CHANGELOG.md` |
| 기술 구조 | `docs/TECH_SPEC.md`, `.agent/decisions.md` |
| 밸런스 | `docs/BALANCE.md` |
| 아트/에셋 | `docs/ART_DIRECTION.md`, `docs/ASSET_LICENSES.md` |
| IP/윤리 정책 | `docs/IP_AND_ETHICS.md` |
| 작업 이력 | `HISTORY.md`, `.agent/progress.md` |
| 후속 작업 | `.agent/tasks.md` |

## 17.8 CHANGELOG 작성 규칙

`CHANGELOG.md`는 사용자에게 공개 가능한 변경 요약으로 작성한다.

권장 섹션:

```md
## vX.Y.Z - YYYY-MM-DD

### Added
### Changed
### Fixed
### Removed
### Security
### Performance
### Documentation
### Build / CI
### Verification
```

금지:

- 커밋 메시지만 나열
- 검증하지 않은 테스트 성공 기록
- 릴리즈하지 않은 변경을 릴리즈 완료처럼 작성

## 17.9 GitHub Actions 중심 검증

- 최종 판단은 가능하면 GitHub Actions 결과를 우선한다.
- 로컬에서는 빠른 정적 검사, 단위 테스트, 디버그 빌드를 우선한다.
- CI 실패 시 로그를 확인하고 수정 후 재푸시한다.
- 실행하지 않은 검증은 성공으로 기록하지 않는다.

권장 명령:

```bash
./gradlew test
./gradlew assembleDebug
gh run list --limit 10
gh run view <RUN_ID> --log-failed
```

## 17.10 커밋 및 푸시 규칙

```bash
git status
git diff --stat
git diff
git add <changed files>
git commit -m "<type>: <변경 요약>"
git push origin <CURRENT_BRANCH>
```

권장 커밋 타입:

```text
feat: 기능 추가
fix: 오류 수정
docs: 문서 수정
refactor: 구조 개선
test: 테스트 추가 또는 수정
chore: 설정, 빌드, 정리
```

## 17.11 최종 보고 형식

```text
작업 요약:
- 

변경 파일:
- 

검증:
- 로컬:
- CI:
- 생략한 검증:

커밋:
- 

푸시:
- 

후속 작업:
- 
```

---

# 18. 초기 GitHub 파일별 내용 가이드

## 18.1 docs/IP_AND_ETHICS.md

```md
# IP and Ethics Policy

## Core Identity
Wild Haven Idle is a wildlife sanctuary restoration idle simulation. It is not an electronic pet clone and does not reference, imitate, or reproduce any existing virtual pet product or third-party IP.

## Prohibited References
- Tamagotchi or similar marks
- Existing electronic pet device UI
- Egg hatching mechanics that resemble existing products
- Third-party characters, icons, fonts, sound effects, or graphics without clear permission

## Ethical Design
- No animal death from inactivity
- No forced payment or ad view to rescue animals
- No donation claims unless actually implemented and documented
- No misleading conservation impact claims

## Assets
All assets must be self-made, CC0, or commercially licensed. Sources must be documented in `docs/ASSET_LICENSES.md`.
```

## 18.2 docs/ASSET_LICENSES.md

```md
# Asset Licenses

| Asset | File | Source | License | Commercial Use | Notes |
|---|---|---|---|---|---|
| App icon | TBD | Self-made | Owned | Yes | Initial placeholder |
```

## 18.3 .agent/tasks.md

```md
# Tasks

## Now
- [ ] Create Android Compose project
- [ ] Add domain models
- [ ] Add BalanceCalculator
- [ ] Add unit tests
- [ ] Add HomeScreen MVP

## Next
- [ ] Add DataStore save/load
- [ ] Add offline reward dialog
- [ ] Add animal recovery list
- [ ] Add archive screen

## Later
- [ ] Add habitat expansion
- [ ] Add art assets
- [ ] Add localization
```

## 18.4 .agent/decisions.md

```md
# Decisions

## 2026-05-13: Project identity
Decision: The game is defined as a wildlife sanctuary restoration idle simulation, not a virtual pet game.
Reason: Reduce IP risk and establish an ethical, original theme.
Impact: Avoid terminology such as Tamagotchi, egg, hatch, feed, evolution, and pet.
```

## 18.5 HISTORY.md

```md
# HISTORY.md

## 2026-05-13
- 작업: 프로젝트 기획 및 개발 규칙 초기화
- 변경 파일:
  - README.md
  - AGENTS.md
  - docs/GAME_DESIGN.md
  - docs/TECH_SPEC.md
  - docs/IP_AND_ETHICS.md
  - .agent/tasks.md
  - .agent/decisions.md
- 검증: 문서 작성 확인
- 결과: 초기 설계 수립
- 후속 작업: Android Compose 프로젝트 생성
```

## 18.6 CHANGELOG.md

```md
# CHANGELOG.md

## v0.1.0 - 2026-05-13

### Added
- Wild Haven Idle 프로젝트 초기 설계 문서 추가
- IP 및 윤리 정책 문서 추가
- AI 에이전트 작업 규칙 추가
- MVP 범위와 Android 기술 구조 정의

### Documentation
- README, GAME_DESIGN, TECH_SPEC, IP_AND_ETHICS, BALANCE 문서 구조 정의
```

---

# 19. 개발 로드맵

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

---

# 20. 테스트 계획

## 20.1 단위 테스트

필수 테스트:

- 보호구역 생산량 계산
- 동물 회복 보너스 계산
- 도감 보너스 계산
- 총 생산량 계산
- 회복 지원 비용 계산
- 오프라인 보상 계산
- 오프라인 보상 8시간 상한
- `now <= lastSavedAt` 보상 0 처리
- 해금 조건 검사

## 20.2 수동 QA

- 첫 실행 시 숲토끼가 표시되는가?
- 보호 포인트가 증가하는가?
- 회복 지원 버튼이 비용 부족 시 비활성화되는가?
- 회복 단계 상승 후 생산량이 증가하는가?
- 앱 종료 후 재실행해도 데이터가 유지되는가?
- 오프라인 보상 팝업이 표시되는가?
- 도감 상태가 정상적으로 구분되는가?
- 앱 어디에도 금지 표현이 노출되지 않는가?

## 20.3 IP/윤리 QA

출시 전 반드시 확인:

- 앱명에 기존 IP를 연상시키는 표현이 없는가?
- 앱 아이콘이 기존 전자펫 기기를 닮지 않았는가?
- 스토어 설명에 타사 상표가 없는가?
- 캐릭터가 기존 캐릭터 실루엣을 닮지 않았는가?
- 광고/결제가 동물 구조 압박으로 설계되지 않았는가?
- 실제 기부처럼 오해될 문구가 없는가?
- 에셋 라이선스가 `ASSET_LICENSES.md`에 기록되었는가?

---

# 21. 첫 바이브 코딩 프롬프트

```text
You are an autonomous coding agent working on Wild Haven Idle.

Repository goal:
Build a local-first Android idle simulation game where the player restores a small wildlife sanctuary, supports rescued animals through recovery stages, upgrades the sanctuary, and completes a wildlife archive.

Project identity rules:
- This is not a virtual pet clone.
- Do not reference or imitate Tamagotchi, electronic pet devices, or third-party IP.
- Do not use terms such as Tamagotchi, egg, hatch, feed, evolution, or pet in user-facing text.
- Use sanctuary, rescued animal, recovery stage, habitat, archive, and restoration language instead.
- Do not add login, networking, ads, analytics, billing, or cloud sync for MVP.
- Do not add android.permission.INTERNET.

Initial implementation scope:
1. Create a Kotlin Android project using Jetpack Compose.
2. Use applicationId com.jeiel85.wildhavenidle.
3. Add the initial project documents:
   - README.md
   - AGENTS.md
   - CHANGELOG.md
   - HISTORY.md
   - docs/GAME_DESIGN.md
   - docs/TECH_SPEC.md
   - docs/BALANCE.md
   - docs/IP_AND_ETHICS.md
   - docs/ASSET_LICENSES.md
   - .agent/tasks.md
   - .agent/progress.md
   - .agent/decisions.md
4. Implement domain models:
   - GameState
   - AnimalDefinition
   - ProtectedAnimal
   - Rarity
   - HabitatType
   - UnlockCondition
5. Implement BalanceCalculator with pure functions for:
   - sanctuary production
   - animal support bonus
   - archive bonus multiplier
   - total production per second
   - recovery support cost
   - sanctuary upgrade cost
   - offline reward
6. Add unit tests for BalanceCalculator.
7. Add a simple HomeScreen showing:
   - carePoint
   - production per second
   - sanctuary level
   - placeholder buttons for rescued animals, archive, and settings.

Workflow rules:
- Read AGENTS.md first.
- Keep the implementation small and reviewable.
- Update HISTORY.md and CHANGELOG.md for user-visible or project-structure changes.
- Prefer pure Kotlin domain logic before UI complexity.
- Run available Gradle test/build commands.
- Do not claim a test or build passed unless it actually ran.
- Commit and push the completed changes if the repository is available.
```

---

# 22. 최종 MVP 정의

```text
Wild Haven Idle의 MVP는 사용자가 보호 포인트를 자동으로 얻고, 그 포인트로 보호구역을 개선하고 구조 동물의 회복 단계를 올리며, 앱을 꺼둔 시간만큼 복귀 보상을 받고, 도감을 채워 나갈 수 있는 로컬 우선 방치형 보호구역 성장 게임이다.
```

MVP의 핵심은 다음 3가지다.

1. 숫자가 증가하는 방치형 성장감
2. 회복 지원과 보호구역 개선으로 생산량이 증가하는 성취감
3. 구조 동물이 도감에 채워지는 수집감

동시에 반드시 지켜야 할 3가지다.

1. 기존 전자펫/타사 IP를 연상시키지 않는 독자 정체성
2. 광고·결제·로그인·네트워크 없는 로컬 우선 MVP
3. 동물 보호 테마를 감정 착취나 허위 공익성으로 사용하지 않는 윤리적 설계
