package com.jeiel85.wildhavenidle.renew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeiel85.wildhavenidle.BuildConfig
import com.jeiel85.wildhavenidle.core.time.SystemTimeProvider
import com.jeiel85.wildhavenidle.data.local.GameStateDataStore
import com.jeiel85.wildhavenidle.data.repository.GameRepository
import com.jeiel85.wildhavenidle.domain.balance.BalanceCalculator
import com.jeiel85.wildhavenidle.domain.definitions.AnimalDefinitions
import com.jeiel85.wildhavenidle.renew.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

// Data Models representing the "Observation & Sanctuary Restore" philosophy
data class WildlifeSubject(
    val id: String,
    val name: String,
    val englishName: String,
    val rescueStory: String,
    val currentRecovery: Int, // Out of 100
    val maxRecovery: Int = 100,
    val observationYield: Double, // points per second contributed
    val currentStage: RecoveryStage,
    val cardIcon: ImageVector,
    val discoverCost: Long,
    val isDiscovered: Boolean = false
)

enum class RecoveryStage(val label: String, val color: Color) {
    RESCUED("긴급 구조됨", Color(0xFFE57373)),
    REHABILITATING("보호소 회복 중", Color(0xFFC5A059)),
    READY_TO_RETURN("자연 복귀 대기", Color(0xFF6B8E23)),
    RETURNED_TO_WILD("야생 복귀 완료", Color(0xFF4A6B53))
}

data class ShelterRestoration(
    val id: String,
    val name: String,
    val currentLevel: Int,
    val baseCost: Long,
    val costMultiplier: Double = 1.35,
    val rateAddition: Double, // added points/sec per level
    val description: String,
    val icon: ImageVector
)

// Active Screen States in the cozy nature-inspired app dashboard
enum class Screen(val title: String, val icon: ImageVector) {
    SANCTUARY("보호구역", Icons.Default.Terrain),
    ANIMALS("관찰 기록", Icons.Default.Favorite),
    RESTORE("서식지 환경", Icons.Default.FilterHdr),
    EXPLORE("도감", Icons.Default.Book),
    SETTINGS("설정", Icons.Default.Settings)
}

// Game State Management through MVVM ViewModel — v0.6.1부터 기존 도메인(GameRepository)과 연결
class WildHavenViewModel(
    private val gameRepository: GameRepository,
) : ViewModel() {

    val points: StateFlow<Double> = gameRepository.gameState
        .map { it.carePoint }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), 0.0)

    val pointsPerSec: StateFlow<Double> = gameRepository.gameState
        .map { state ->
            BalanceCalculator.calculateTotalProductionPerSecond(
                sanctuaryLevel = state.sanctuaryLevel,
                protectedAnimals = state.protectedAnimals,
                definitions = AnimalDefinitions.mvpAnimals,
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), 0.0)

    val wildlifeList: StateFlow<List<WildlifeSubject>> = gameRepository.gameState
        .map { DomainMapping.buildWildlifeList(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    val restorations: StateFlow<List<ShelterRestoration>> = gameRepository.gameState
        .map { DomainMapping.buildRestorations(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    private val _offlineRewardPoints = MutableStateFlow(0L)
    val offlineRewardPoints: Long get() = _offlineRewardPoints.value

    private val _showOfflineReward = MutableStateFlow(false)
    val showOfflineReward: StateFlow<Boolean> = _showOfflineReward.asStateFlow()

    init {
        // 진입 시점에 한 번 오프라인 보상을 정산하고, 보상이 있으면 팝업 노출
        viewModelScope.launch {
            val reward = gameRepository.applyOfflineRewardIfNeeded()
            if (reward > 0.0) {
                _offlineRewardPoints.value = reward.toLong()
                _showOfflineReward.value = true
            }
        }
    }

    /**
     * 1초마다 도메인의 carePoint에 현재 생산량을 더하는 패시브 틱.
     * `MainActivity`가 `LaunchedEffect`에서 한 번만 호출한다.
     */
    suspend fun runPassiveTick() {
        while (true) {
            delay(1000)
            val rate = pointsPerSec.value
            if (rate > 0.0) {
                gameRepository.addCarePoint(rate)
            }
        }
    }

    fun claimOfflineReward() {
        // 보상 자체는 진입 시점 applyOfflineRewardIfNeeded에서 이미 carePoint에 추가됨.
        // UI 입장에선 팝업만 닫으면 됨.
        _showOfflineReward.value = false
    }

    fun closeOfflineRewardOnly() {
        _showOfflineReward.value = false
    }

    fun supportRehabilitation(animalId: String) {
        viewModelScope.launch {
            gameRepository.supportAnimalRecovery(animalId)
        }
    }

    fun discoverWildlife(animalId: String) {
        // 기존 도메인은 조건(`UnlockCondition`) 충족 시 자동으로 보호 동물에 합류한다.
        // 새 UI의 "흔적 발견" 버튼은 v0.6.1에서 표시만 유지하고 동작은 no-op으로 둔다.
        // v0.7+에서 도메인에 수동 unlock 옵션을 추가하면 그때 연결.
    }

    fun purchaseRestoration(upgradeId: String) {
        if (!DomainMapping.isRestorationEnabled(upgradeId)) return
        viewModelScope.launch {
            gameRepository.upgradeSanctuary()
        }
    }

    fun getRestorationCost(upgrade: ShelterRestoration): Long {
        return if (DomainMapping.isRestorationEnabled(upgrade.id)) {
            upgrade.baseCost
        } else {
            Long.MAX_VALUE
        }
    }
}

class WildHavenViewModelFactory(
    private val gameRepository: GameRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(WildHavenViewModel::class.java)) {
            "Unknown ViewModel class: ${modelClass.name}"
        }
        return WildHavenViewModel(gameRepository) as T
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val timeProvider = SystemTimeProvider
        val gameRepository = GameRepository(
            localDataStore = GameStateDataStore(
                context = this,
                defaultNowMillis = timeProvider::nowMillis,
            ),
            timeProvider = timeProvider,
        )

        setContent {
            MyApplicationTheme {
                val gameViewModel: WildHavenViewModel = viewModel(
                    factory = WildHavenViewModelFactory(gameRepository),
                )

                // Register real-time ticker loop using Coroutines
                LaunchedEffect(Unit) {
                    gameViewModel.runPassiveTick()
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainGameLayout(viewModel = gameViewModel)
                }
            }
        }
    }
}

@Composable
fun MainGameLayout(viewModel: WildHavenViewModel) {
    val points by viewModel.points.collectAsStateWithLifecycle()
    val pointsPerSec by viewModel.pointsPerSec.collectAsStateWithLifecycle()
    val showOfflineReward by viewModel.showOfflineReward.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf(Screen.SANCTUARY) }

    Scaffold(
        topBar = {
            HeaderDashboard(
                points = points,
                pointsPerSec = pointsPerSec
            )
        },
        bottomBar = {
            BottomNavigationTabBar(
                currentScreen = currentScreen,
                onScreenSelected = { currentScreen = it }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        )
                    )
                )
        ) {
            // Screen transition animation container
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                },
                label = "ScreenTransition"
            ) { targetScreen ->
                when (targetScreen) {
                    Screen.SANCTUARY -> SanctuaryDashboardScreen(viewModel)
                    Screen.ANIMALS -> AnimalRecoveryScreen(viewModel)
                    Screen.RESTORE -> HabitatRestorationScreen(viewModel)
                    Screen.EXPLORE -> JournalEncyclopediaScreen(viewModel)
                    Screen.SETTINGS -> SanctuarySettingsScreen()
                }
            }

            // High-fidelity Warm Offline Rewards Dialog popup
            if (showOfflineReward) {
                OfflineRewardPopup(
                    rewardPoints = viewModel.offlineRewardPoints,
                    onClaim = { viewModel.claimOfflineReward() },
                    onDismiss = { viewModel.closeOfflineRewardOnly() }
                )
            }
        }
    }
}

// Global visual header highlighting Sanctuary details and observation metrics
@Composable
fun HeaderDashboard(
    points: Double,
    pointsPerSec: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp), clip = false),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, ArtisticSage.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "SANCTUARY STATUS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ArtisticSage,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Wild Haven",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = ArtisticOlive,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.testTag("protection_points_dashboard")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "🍃",
                        style = TextStyle(fontSize = 18.sp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format(Locale.US, "%,.0f", points),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = ArtisticOlive
                        )
                    )
                }
                Text(
                    text = String.format(Locale.getDefault(), "+%.1f / 초당", pointsPerSec),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = ArtisticSage,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

// Navigation Tab Bar complying strictly with bottom margins and notch/bars
@Composable
fun BottomNavigationTabBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        containerColor = Color.White.copy(alpha = 0.9f),
        tonalElevation = 8.dp
    ) {
        Screen.values().forEach { screen ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onScreenSelected(screen) },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        tint = if (isSelected) ArtisticOlive else ArtisticCharcoal.copy(alpha = 0.5f)
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) ArtisticOlive else ArtisticCharcoal.copy(alpha = 0.6f)
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = ArtisticSage.copy(alpha = 0.2f)
                ),
                modifier = Modifier.testTag("nav_tab_${screen.name.lowercase(Locale.US)}")
            )
        }
    }
}

// SCREEN 1: THE HOME SANCTUARY OBSERVATION SCREEN
@Composable
fun SanctuaryDashboardScreen(viewModel: WildHavenViewModel) {
    val wildlifeList by viewModel.wildlifeList.collectAsStateWithLifecycle()
    val activeUnderCareCount = wildlifeList.count { it.isDiscovered && it.currentStage != RecoveryStage.RETURNED_TO_WILD }
    val returnedCount = wildlifeList.count { it.currentStage == RecoveryStage.RETURNED_TO_WILD }

    // Grab the first discovered animal for the beautiful center showcase frame (matching the Luna Red Fox theme)
    val heroAnimal = wildlifeList.firstOrNull { it.isDiscovered } ?: wildlifeList.firstOrNull()

    if (heroAnimal == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = ArtisticOlive)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // High Artistic Showcase Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(32.dp), clip = false)
                .background(Color.White, shape = RoundedCornerShape(32.dp))
                .border(2.dp, Color.White, shape = RoundedCornerShape(32.dp))
                .padding(24.dp)
        ) {
            // "Observation 042" Badge top right
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(100.dp))
                    .background(ArtisticIvory)
                    .border(1.dp, ArtisticSage.copy(alpha = 0.3f), RoundedCornerShape(100.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "OBSERVATION ${if (heroAnimal.isDiscovered) "042" else "🔒"}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ArtisticSage,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 16.0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Outer circle container
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .background(ArtisticEarthySand)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(136.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8E4DC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = DomainMapping.heroEmojiFor(heroAnimal.id),
                            style = TextStyle(fontSize = 58.sp)
                        )
                    }

                    // Absolute badge on bottom curve
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = (4).dp)
                            .shadow(2.dp, RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, ArtisticSage.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = heroAnimal.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = ArtisticOlive,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Recovery percentage bar using warm gradients
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "RECOVERY JOURNEY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ArtisticSage,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "${heroAnimal.currentRecovery}%",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = ArtisticOlive
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Smooth Gradient matching from sage to olive
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F0F0))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(heroAnimal.currentRecovery / 100f)
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(ArtisticSage, ArtisticOlive)
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "\"${heroAnimal.rescueStory}\"",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = ArtisticCharcoal.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Coined quick care shortcuts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ObservationCarePill(icon = "💧", label = "Fresh Water")
                    ObservationCarePill(icon = "🌿", label = "Forest Bed")
                    ObservationCarePill(icon = "🍎", label = "Native Diet")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // State summary dashboards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusSimpleCard(
                title = "구조 지원 야생동물",
                stat = "${activeUnderCareCount}마리",
                supportingText = "따뜻한 치유 연구 중",
                color = ArtisticEarthySand,
                modifier = Modifier.weight(1f)
            )

            StatusSimpleCard(
                title = "자연 복귀 완료한 품",
                stat = "${returnedCount}마리",
                supportingText = "숲의 수호자로 회귀",
                color = ArtisticSage.copy(alpha = 0.15f),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Guided Advice Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, ArtisticSage.copy(alpha = 0.15f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🍃", style = TextStyle(fontSize = 18.sp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "오늘의 자연 수호 일지",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ArtisticOlive
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "동물이 위협을 느끼지 않도록 관찰거리를 차분히 유지하며 서식지 환경 복원에 보호 포인트를 집중해 보세요. 맑은 여울터와 밀집 덤불숲이 갖춰질수록 야생동물들의 자립 성향이 한층 솟아납니다.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = ArtisticCharcoal.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                )
            }
        }
    }
}

// Simple Helper for quick care pill icons
@Composable
fun ObservationCarePill(icon: String, label: String) {
    Box(
        modifier = Modifier
            .width(96.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(ArtisticIvory)
            .border(1.dp, ArtisticSage.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = icon, style = TextStyle(fontSize = 18.sp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.sp,
                    color = ArtisticSage,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

// SCREEN 2: ANIMAL RECOVERY (OBSERVATION CARDS) SCREEN
@Composable
fun AnimalRecoveryScreen(viewModel: WildHavenViewModel) {
    val wildlifeList by viewModel.wildlifeList.collectAsStateWithLifecycle()
    val points by viewModel.points.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "치유 및 복귀 관리 일지",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "구조된 동물들의 회복은 순수한 돌봄과 조용한 시간을 통해 성취됩니다.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Display active and unlocked animals
        val discoveredAnimals = wildlifeList.filter { it.isDiscovered }
        items(discoveredAnimals, key = { it.id }) { animal ->
            AnimalRehabCard(
                animal = animal,
                canAffordCare = points >= 25.0,
                onSupportClick = { viewModel.supportRehabilitation(animal.id) }
            )
        }

        // Locked animals section showing targets gracefully
        val lockedAnimals = wildlifeList.filter { !it.isDiscovered }
        if (lockedAnimals.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "주변 숲에서 발견된 흔적",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(lockedAnimals, key = { it.id }) { animal ->
                LockedAnimalDiscoveryCard(
                    animal = animal,
                    canAffordUnlock = points >= animal.discoverCost,
                    onDiscoverClick = { viewModel.discoverWildlife(animal.id) }
                )
            }
        }
    }
}

@Composable
fun AnimalRehabCard(
    animal: WildlifeSubject,
    canAffordCare: Boolean,
    onSupportClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("animal_card_${animal.id}")
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(28.dp), clip = false),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, ArtisticSage.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header: Name / Stage tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(ArtisticSage.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = animal.cardIcon,
                            contentDescription = null,
                            tint = ArtisticOlive,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = animal.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ArtisticCharcoal
                            )
                        )
                        Text(
                            text = animal.englishName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ArtisticSage,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                // Stage Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(animal.currentStage.color.copy(alpha = 0.12f))
                        .border(1.dp, animal.currentStage.color.copy(alpha = 0.25f), RoundedCornerShape(100.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = animal.currentStage.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = animal.currentStage.color,
                            fontSize = 9.sp,
                            letterSpacing = 0.2.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Short Narrative
            Text(
                text = animal.rescueStory,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = ArtisticCharcoal.copy(alpha = 0.85f),
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Progress Bar representing calm recovery rather than leveling
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "치유 및 자생 적응도",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ArtisticSage,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = "${animal.currentRecovery}%",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ArtisticOlive
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { animal.currentRecovery / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = ArtisticOlive,
                    trackColor = ArtisticSage.copy(alpha = 0.2f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Non-coercive Recovery Assist Action
            if (animal.currentStage != RecoveryStage.RETURNED_TO_WILD) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = String.format(Locale.getDefault(), "초당 +%.1f 보호 수치 회복", animal.observationYield),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ArtisticSage,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Button(
                        onClick = onSupportClick,
                        enabled = canAffordCare || animal.currentStage == RecoveryStage.READY_TO_RETURN,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (animal.currentStage == RecoveryStage.READY_TO_RETURN) ArtisticOlive else ArtisticTerraCotta,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .testTag("support_button_${animal.id}")
                            .height(44.dp)
                    ) {
                        Text(
                            text = if (animal.currentStage == RecoveryStage.READY_TO_RETURN) "야생 복귀 동행 🍃" else "조용한 안식 제공 (🐾 25)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(ArtisticSage.copy(alpha = 0.15f))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = ArtisticOlive,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "숲으로 완벽히 돌아갔지만, 여전히 보호구역을 따뜻하게 지켜줍니다.",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ArtisticOlive,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LockedAnimalDiscoveryCard(
    animal: WildlifeSubject,
    canAffordUnlock: Boolean,
    onDiscoverClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp), clip = false),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, ArtisticSage.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ArtisticSage.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "미발견 동물",
                        tint = ArtisticSage,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "희미한 발자국과 온기",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ArtisticCharcoal.copy(alpha = 0.7f)
                        )
                    )
                    Text(
                        text = "보호 포인트 🐾 ${animal.discoverCost}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ArtisticSage,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Button(
                onClick = onDiscoverClick,
                enabled = canAffordUnlock,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ArtisticOlive,
                    contentColor = Color.White,
                    disabledContainerColor = ArtisticCharcoal.copy(alpha = 0.08f)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.height(38.dp)
            ) {
                Text(
                    text = "흔적 발견",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (canAffordUnlock) Color.White else ArtisticCharcoal.copy(alpha = 0.3f)
                    )
                )
            }
        }
    }
}

// SCREEN 3: SANCTUARY RESTORATION / HABITAT SCREEN
@Composable
fun HabitatRestorationScreen(viewModel: WildHavenViewModel) {
    val restorations by viewModel.restorations.collectAsStateWithLifecycle()
    val points by viewModel.points.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "서식지 보호구역 환경 복원",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = ArtisticOlive
                ),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "보호구역 전반의 환경을 회복하여, 기틀이 되는 생물 다양성을 안전하게 넓힙니다.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = ArtisticSage
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        items(restorations, key = { it.id }) { restoration ->
            val enabled = DomainMapping.isRestorationEnabled(restoration.id)
            val cost = if (enabled) viewModel.getRestorationCost(restoration) else 0L
            val canAfford = enabled && points >= cost

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("restore_card_${restoration.id}")
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(28.dp), clip = false),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, ArtisticSage.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(ArtisticEarthySand),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = restoration.icon,
                                    contentDescription = null,
                                    tint = ArtisticOlive,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = restoration.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ArtisticCharcoal
                                    )
                                )
                                Text(
                                    text = "복원 진척도: 레벨 ${restoration.currentLevel}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = ArtisticSage,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.purchaseRestoration(restoration.id) },
                            enabled = canAfford,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ArtisticOlive,
                                contentColor = Color.White,
                                disabledContainerColor = ArtisticCharcoal.copy(alpha = 0.08f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .testTag("upgrade_button_${restoration.id}")
                                .height(40.dp)
                        ) {
                            Text(
                                text = if (enabled) "🐾 $cost" else "준비 중",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (canAfford) Color.White else ArtisticCharcoal.copy(alpha = 0.3f),
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = restoration.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = ArtisticCharcoal.copy(alpha = 0.8f),
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ArtisticIvory)
                            .border(1.dp, ArtisticSage.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "현재 추가 회복 기여",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ArtisticSage,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = String.format(Locale.getDefault(), "초당 +%.1f 보호 수치", restoration.currentLevel * restoration.rateAddition),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ArtisticOlive
                            )
                        )
                    }
                }
            }
        }
    }
}

// SCREEN 4: JOURNAL & ENCYCLOPEDIA SCREEN (RECORD OF OBSERVATION)
@Composable
fun JournalEncyclopediaScreen(viewModel: WildHavenViewModel) {
    val wildlifeList by viewModel.wildlifeList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "야생 관찰 연구 노트",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = ArtisticOlive
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "도감은 동물을 가두는 감옥이 아니라, 우리 숲을 채운 따뜻한 인연들의 목격 기록입니다.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = ArtisticSage
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(wildlifeList, key = { it.id }) { animal ->
                JournalRecordCard(animal)
            }
        }
    }
}

@Composable
fun JournalRecordCard(animal: WildlifeSubject) {
    val isRevealed = animal.isDiscovered || animal.currentStage == RecoveryStage.RETURNED_TO_WILD

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp), clip = false),
        colors = CardDefaults.cardColors(
            containerColor = if (isRevealed) Color.White else Color.White.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, ArtisticSage.copy(alpha = if (isRevealed) 0.25f else 0.12f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isRevealed) ArtisticSage.copy(alpha = 0.15f) else ArtisticCharcoal.copy(alpha = 0.06f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRevealed) animal.cardIcon else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isRevealed) ArtisticOlive else ArtisticCharcoal.copy(alpha = 0.3f),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                if (isRevealed) {
                    Text(
                        text = animal.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = ArtisticCharcoal
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = animal.englishName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ArtisticSage,
                            fontSize = 10.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (animal.currentStage == RecoveryStage.RETURNED_TO_WILD) "완전히 자립하여 건강하게 자연 복귀 완료 🍃" else "현재 보호소 안에서 기력 치유 진행 중",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (animal.currentStage == RecoveryStage.RETURNED_TO_WILD) ArtisticOlive else ArtisticTerraCotta,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp
                        )
                    )
                } else {
                    Text(
                        text = "고요한 미발견 숲의 수호자",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ArtisticCharcoal.copy(alpha = 0.4f)
                        )
                    )
                    Text(
                        text = "보호구역을 더 정돈하면 흔적을 마주할 기회가 늘어납니다.",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ArtisticSage.copy(alpha = 0.7f),
                            fontSize = 9.sp
                        )
                    )
                }
            }
        }
    }
}

// SCREEN 5: SOOTHING & CALM SETTINGS SCREEN
@Composable
fun SanctuarySettingsScreen() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(
            text = "보호 환경 설정",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = ArtisticOlive
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp), clip = false),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, ArtisticSage.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "평화로운 관리 방침",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = ArtisticOlive
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "이 게임은 야생동물들을 감금하고 지배하는 것이 아닙니다. 우리는 다친 자연의 일부를 안식하고, 그들이 원초적인 활력을 되찾아 무사히 그들의 본래 터전인 야생으로 귀환할 수 있도록 거드는 평화로운 동伴자입니다.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = ArtisticCharcoal.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle-based silent options
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp), clip = false),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, ArtisticSage.copy(alpha = 0.2f))
        ) {
            Column {
                ToggleSettingRow(
                    label = "숲바람 배경음 소리",
                    initialValue = true,
                    icon = Icons.Default.VolumeUp
                )
                HorizontalDivider(color = ArtisticSage.copy(alpha = 0.15f))
                ToggleSettingRow(
                    label = "교감을 위한 미세 진동 발생",
                    initialValue = true,
                    icon = Icons.Default.Vibration
                )
                HorizontalDivider(color = ArtisticSage.copy(alpha = 0.15f))
                ToggleSettingRow(
                    label = "새벽 안개 알림 받기",
                    initialValue = false,
                    icon = Icons.Default.Notifications
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(24.dp), clip = false),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, ArtisticSage.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "보호 지침서 버전 정보",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ArtisticCharcoal
                        )
                    )
                    Text(
                        text = "와일드 헤이븐 아이들 v${BuildConfig.VERSION_NAME}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ArtisticSage,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ArtisticSage.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "최신 숲길",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ArtisticOlive,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ToggleSettingRow(
    label: String,
    initialValue: Boolean,
    icon: ImageVector
) {
    var checked by remember { mutableStateOf(initialValue) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ForestGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ForestGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = SoftSageGreen.copy(alpha = 0.6f)
            )
        )
    }
}

// COZY RECOVERY POPUP OVERLAY ON RETURN
@Composable
fun OfflineRewardPopup(
    rewardPoints: Long,
    onClaim: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("offline_reward_popup"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = WarmIvory),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Radiant Soothing Icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SoftSageGreen.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = ForestGreen,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "조용한 숲의 소식",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "당신이 없는 동안에도\n보호구역은 조용히 회복되고 있었습니다.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = CharcoalText,
                        lineHeight = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Accumulation details box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(WarmSand)
                        .padding(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "야생 보호 지지 세력 축적 포인트",
                            style = MaterialTheme.typography.labelSmall.copy(color = EarthBrown)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🐾 +$rewardPoints",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = ForestGreen
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onClaim,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen, contentColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("claim_offline_reward_button"),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "보호 활동 이어가기",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

// Minimal Simple Card helper inside screens
@Composable
fun StatusSimpleCard(
    title: String,
    stat: String,
    supportingText: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stat,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = supportingText,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
        }
    }
}
