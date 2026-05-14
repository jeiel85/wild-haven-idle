package com.jeiel85.wildhavenidle.presentation.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.animation.core.animateFloat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeiel85.wildhavenidle.core.design.WildHavenTheme
import com.jeiel85.wildhavenidle.core.format.NumberFormatter
import com.jeiel85.wildhavenidle.presentation.components.AnimalIllustration
import com.jeiel85.wildhavenidle.presentation.components.RecoveryStageLabel
import com.jeiel85.wildhavenidle.presentation.components.SanctuaryHeader

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToArchive: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeContent(
        uiState = uiState,
        onOfflineRewardConfirmed = viewModel::clearOfflineReward,
        onUpgradeSanctuary = viewModel::upgradeSanctuary,
        onSupportRecovery = viewModel::supportRecovery,
        onTapSanctuary = viewModel::tapSanctuary,
        onNavigateToArchive = onNavigateToArchive,
        onNavigateToSettings = onNavigateToSettings,
        modifier = modifier,
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onOfflineRewardConfirmed: () -> Unit,
    onUpgradeSanctuary: () -> Unit,
    onSupportRecovery: (String) -> Unit,
    onTapSanctuary: () -> Double,
    onNavigateToArchive: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    uiState.offlineReward?.let { reward ->
        OfflineRewardDialog(
            reward = reward,
            onConfirm = onOfflineRewardConfirmed,
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SanctuaryHeader(
            sanctuaryLevel = uiState.sanctuaryLevel,
            protectedAnimalIds = uiState.protectedAnimalIds,
            onTap = onTapSanctuary,
        )

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Wild Haven Idle",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "보호구역 Lv.${uiState.sanctuaryLevel} · 도감 ${uiState.discoveredAnimalCount}/${uiState.totalAnimalCount}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }

        CarePointPanel(
            carePoint = uiState.carePoint,
            productionPerSecond = uiState.productionPerSecond,
            tapReward = uiState.tapReward,
        )

        uiState.nextUnlock?.let { nextUnlock ->
            MilestoneCard(progress = nextUnlock)
        }

        SanctuaryUpgradeCard(
            level = uiState.sanctuaryLevel,
            cost = uiState.sanctuaryUpgradeCost,
            carePoint = uiState.carePoint,
            onUpgrade = onUpgradeSanctuary,
        )

        if (uiState.animals.isNotEmpty()) {
            Text(
                text = "보호 중인 동물",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
            )

            uiState.animals.forEach { item ->
                AnimalRecoveryCard(
                    item = item,
                    carePoint = uiState.carePoint,
                    onSupport = { onSupportRecovery(item.definition.id) },
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = onNavigateToArchive,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
            ) {
                Text("도감")
            }
            Button(
                onClick = onNavigateToSettings,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                ),
            ) {
                Text("설정")
            }
        }

        Text(
            text = "화면 상단의 보호구역을 탭하면 추가 포인트를 얻을 수 있습니다.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
private fun CarePointPanel(
    carePoint: Double,
    productionPerSecond: Double,
    tapReward: Double,
    modifier: Modifier = Modifier,
) {
    val animatedCarePoint by animateFloatAsState(
        targetValue = carePoint.toFloat(),
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "care-point",
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "보호 포인트", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = NumberFormatter.compact(animatedCarePoint.toDouble()),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = NumberFormatter.perSecond(productionPerSecond),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "탭 +${NumberFormatter.compact(tapReward)}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun MilestoneCard(
    progress: NextUnlockProgress,
    modifier: Modifier = Modifier,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.progress,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "milestone",
    )
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "다음 해금",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = progress.animalNameKo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = progress.helperText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
private fun SanctuaryUpgradeCard(
    level: Int,
    cost: Long,
    carePoint: Double,
    onUpgrade: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val canUpgrade = carePoint >= cost && cost > 0L
    val pulse = pulseScale(active = canUpgrade)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "보호구역 확장",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Lv.$level → Lv.${level + 1}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                Text(
                    text = "+1/sec",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onUpgrade,
                enabled = canUpgrade,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { scaleX = pulse; scaleY = pulse },
            ) {
                Text("업그레이드 — ${NumberFormatter.compact(cost.toDouble())}")
            }
        }
    }
}

@Composable
private fun AnimalRecoveryCard(
    item: AnimalRecoveryItem,
    carePoint: Double,
    onSupport: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val animal = item.protectedAnimal
    val stage = animal.recoveryStage
    val stageLabel = RecoveryStageLabel.of(stage)
    val maxStage = item.definition.maxRecoveryStage
    val canSupport = carePoint >= item.recoveryCost && item.recoveryCost > 0L && stage < maxStage
    val pulse = pulseScale(active = canSupport)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AnimalIllustration(
                animalId = item.definition.id,
                size = 64.dp,
                modifier = Modifier,
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = item.definition.nameKo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = NumberFormatter.perSecond(item.supportBonus),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "회복 Lv.$stage · $stageLabel",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (stage < maxStage) {
                    Button(
                        onClick = onSupport,
                        enabled = canSupport,
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer { scaleX = pulse; scaleY = pulse },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                        ),
                    ) {
                        Text("회복 지원 — ${NumberFormatter.compact(item.recoveryCost.toDouble())}")
                    }
                } else {
                    Text(
                        text = "최대 회복 단계 도달",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun pulseScale(active: Boolean): Float {
    if (!active) return 1f
    val transition = rememberInfiniteTransition(label = "pulse")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse-scale",
    )
    return scale
}

@Composable
private fun OfflineRewardDialog(
    reward: Double,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onConfirm,
        title = {
            Text(text = "보호구역 활동 보고")
        },
        text = {
            Text(text = "자리를 비운 동안 보호 포인트 ${NumberFormatter.compact(reward)}를 모았습니다.")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "확인")
            }
        },
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    WildHavenTheme {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            HomeContent(
                uiState = HomeUiState(
                    carePoint = 124.0,
                    sanctuaryLevel = 1,
                    productionPerSecond = 1.224,
                    protectedAnimalCount = 1,
                    discoveredAnimalCount = 1,
                    totalAnimalCount = 5,
                    sanctuaryUpgradeCost = 200L,
                    tapReward = 1.2,
                    protectedAnimalIds = listOf("rabbit_001"),
                ),
                onOfflineRewardConfirmed = {},
                onUpgradeSanctuary = {},
                onSupportRecovery = {},
                onTapSanctuary = { 1.2 },
                onNavigateToArchive = {},
                onNavigateToSettings = {},
            )
        }
    }
}
