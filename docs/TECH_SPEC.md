# TECH_SPEC.md

# Wild Haven Idle Android 기술 설계서

## 1. 기술 스택

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

---

## 2. Android Application ID

```text
com.jeiel85.wildhavenidle
```

---

## 3. MVP 권한 정책

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

---

## 4. 패키지 구조

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

## 5. 데이터 모델

### GameState

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

### AnimalDefinition

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

### ProtectedAnimal

```kotlin
data class ProtectedAnimal(
    val animalId: String,
    val recoveryStage: Int = 1,
    val protectedAt: Long = System.currentTimeMillis()
)
```

### UnlockCondition

```kotlin
sealed class UnlockCondition {
    data object InitialAnimal : UnlockCondition()
    data class CarePointReached(val amount: Long) : UnlockCondition()
    data class RecoveryStageReached(val animalId: String, val stage: Int) : UnlockCondition()
    data class TotalProductionReached(val productionPerSecond: Double) : UnlockCondition()
    data class ProtectedAnimalCountReached(val count: Int) : UnlockCondition()
}
```

### Rarity

```kotlin
enum class Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY
}
```

### HabitatType

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

## 6. 핵심 로직

### 생산량 계산

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

### 오프라인 보상 계산

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

### 회복 지원 비용 계산

```kotlin
fun calculateRecoverySupportCost(
    baseCost: Double,
    currentRecoveryStage: Int
): Long {
    return (baseCost * currentRecoveryStage.toDouble().pow(1.45)).roundToLong()
}
```

### 보호구역 업그레이드 비용

```kotlin
fun calculateSanctuaryUpgradeCost(currentLevel: Int): Long {
    return (200.0 * currentLevel.toDouble().pow(1.6)).roundToLong()
}
```

---

## 7. 저장 구조

MVP 저장 방식:

- 서버 없음
- 로그인 없음
- DataStore 기반 로컬 저장
- 민감 정보 저장 없음
- 사용자 식별자 생성 없음

저장 대상:

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
