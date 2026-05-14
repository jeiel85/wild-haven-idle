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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeiel85.wildhavenidle.core.design.WildHavenTheme
import com.jeiel85.wildhavenidle.core.format.NumberFormatter
import com.jeiel85.wildhavenidle.presentation.components.AnimalIllustration
import com.jeiel85.wildhavenidle.presentation.components.RecoveryStageLabel
import com.jeiel85.wildhavenidle.presentation.components.SanctuaryHeader
import com.jeiel85.wildhavenidle.presentation.onboarding.OnboardingOverlay

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
        onCompleteOnboarding = viewModel::completeOnboarding,
        onClaimDailyBonus = viewModel::claimDailyBonus,
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
    onCompleteOnboarding: () -> Unit,
    onClaimDailyBonus: () -> Unit,
    onNavigateToArchive: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        HomeContentBody(
            uiState = uiState,
            onOfflineRewardConfirmed = onOfflineRewardConfirmed,
            onUpgradeSanctuary = onUpgradeSanctuary,
            onSupportRecovery = onSupportRecovery,
            onTapSanctuary = onTapSanctuary,
            onClaimDailyBonus = onClaimDailyBonus,
            onNavigateToArchive = onNavigateToArchive,
            onNavigateToSettings = onNavigateToSettings,
        )

        if (uiState.showOnboarding) {
            OnboardingOverlay(onComplete = onCompleteOnboarding)
        }
    }
}

@Composable
private fun HomeContentBody(
    uiState: HomeUiState,
    onOfflineRewardConfirmed: () -> Unit,
    onUpgradeSanctuary: () -> Unit,
    onSupportRecovery: (String) -> Unit,
    onTapSanctuary: () -> Double,
    onClaimDailyBonus: () -> Unit,
    onNavigateToArchive: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    uiState.offlineReward?.let { reward ->
        OfflineRewardDialog(
            reward = reward,
            onConfirm = onOfflineRewardConfirmed,
        )
    }

    val spacing = WildHavenTheme.spacing
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
            .padding(spacing.screenPadding),
        verticalArrangement = Arrangement.spacedBy(spacing.cardGap),
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "보호구역 Lv.${uiState.sanctuaryLevel} · 도감 ${uiState.discoveredAnimalCount}/${uiState.totalAnimalCount}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        CarePointPanel(
            carePoint = uiState.carePoint,
            productionPerSecond = uiState.productionPerSecond,
            tapReward = uiState.tapReward,
        )

        uiState.dailyBonus?.let { offer ->
            DailyBonusCard(
                offer = offer,
                onClaim = onClaimDailyBonus,
            )
        }

        RecommendedActionCard(
            action = uiState.recommendedAction,
            onUpgrade = onUpgradeSanctuary,
            onSupport = onSupportRecovery,
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

    val spacing = WildHavenTheme.spacing
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Column(modifier = Modifier.padding(spacing.xl)) {
            Text(text = "보호 포인트", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(spacing.xs + 2.dp))
            Text(
                text = NumberFormatter.compact(animatedCarePoint.toDouble()),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(spacing.xs))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = NumberFormatter.perSecond(productionPerSecond),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Text(
                    text = "탭 +${NumberFormatter.compact(tapReward)}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = spacing.sm),
                )
            }
        }
    }
}

@Composable
private fun DailyBonusCard(
    offer: DailyBonusOffer,
    onClaim: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = WildHavenTheme.spacing
    val pulse = pulseScale(active = true)
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = WildHavenTheme.elevation.sm,
    ) {
        Column(modifier = Modifier.padding(spacing.cardPadding)) {
            Text(
                text = "오늘의 보호 활동 보상",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(spacing.xs + 2.dp))
            Text(
                text = "보호 포인트 +${NumberFormatter.compact(offer.rewardAmount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(spacing.xs))
            Text(
                text = "자정이 지나면 다시 받을 수 있습니다",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(spacing.md))
            Button(
                onClick = onClaim,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { scaleX = pulse; scaleY = pulse },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
            ) {
                Text(
                    text = "수령하기",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun RecommendedActionCard(
    action: RecommendedAction,
    onUpgrade: () -> Unit,
    onSupport: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = WildHavenTheme.spacing
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        tonalElevation = WildHavenTheme.elevation.sm,
    ) {
        Column(modifier = Modifier.padding(spacing.cardPadding)) {
            Text(
                text = "지금 추천",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            )
            Spacer(modifier = Modifier.height(spacing.xs + 2.dp))

            when (action) {
                is RecommendedAction.Actionable -> ActionableBody(
                    action = action,
                    onClick = {
                        when (action) {
                            is RecommendedAction.UpgradeSanctuary -> onUpgrade()
                            is RecommendedAction.SupportRecovery -> onSupport(action.animalId)
                        }
                    },
                )
                is RecommendedAction.WaitForNext -> WaitForNextBody(action)
            }
        }
    }
}

@Composable
private fun ActionableBody(
    action: RecommendedAction.Actionable,
    onClick: () -> Unit,
) {
    val spacing = WildHavenTheme.spacing
    Text(
        text = action.titleKo,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
    Spacer(modifier = Modifier.height(spacing.xs))
    Text(
        text = "${action.costLabel} · ${action.effectLabel}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
    Spacer(modifier = Modifier.height(spacing.md))
    val pulse = pulseScale(active = true)
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = pulse; scaleY = pulse },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
        ),
    ) {
        Text(
            text = action.ctaLabelKo,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun WaitForNextBody(action: RecommendedAction.WaitForNext) {
    val spacing = WildHavenTheme.spacing
    Text(
        text = action.targetLabelKo,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
    Spacer(modifier = Modifier.height(spacing.xs))
    val helper = if (action.secondsUntilNext == Long.MAX_VALUE) {
        "탭으로 보호 포인트를 모아보세요"
    } else if (action.secondsUntilNext <= 0L) {
        "곧 다음 행동이 가능합니다"
    } else {
        "약 ${formatWaitDuration(action.secondsUntilNext)} 후 가능"
    }
    Text(
        text = helper,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
    )
}

private fun formatWaitDuration(seconds: Long): String = when {
    seconds < 60L -> "${seconds}초"
    seconds < 3_600L -> "${seconds / 60}분 ${seconds % 60}초"
    else -> "${seconds / 3_600}시간 ${(seconds % 3_600) / 60}분"
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
    val spacing = WildHavenTheme.spacing
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = WildHavenTheme.elevation.sm,
    ) {
        Column(modifier = Modifier.padding(spacing.cardPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "다음 해금",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    modifier = Modifier.padding(start = spacing.sm),
                )
            }
            Spacer(modifier = Modifier.height(spacing.xs + 2.dp))
            Text(
                text = progress.animalNameKo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(spacing.sm))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(spacing.xs + 2.dp))
            Text(
                text = progress.helperText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
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

    val spacing = WildHavenTheme.spacing
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Column(modifier = Modifier.padding(spacing.cardPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "보호구역 확장",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "Lv.$level → Lv.${level + 1}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    text = "+1/sec",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    modifier = Modifier.padding(start = spacing.sm),
                )
            }
            Spacer(modifier = Modifier.height(spacing.md))
            Button(
                onClick = onUpgrade,
                enabled = canUpgrade,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { scaleX = pulse; scaleY = pulse },
            ) {
                Text(
                    text = "업그레이드 — ${NumberFormatter.compact(cost.toDouble())}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
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

    val spacing = WildHavenTheme.spacing
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = WildHavenTheme.elevation.md,
    ) {
        Row(
            modifier = Modifier.padding(spacing.md),
            horizontalArrangement = Arrangement.spacedBy(spacing.md),
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    Text(
                        text = NumberFormatter.perSecond(item.supportBonus),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        modifier = Modifier.padding(start = spacing.sm),
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "회복 Lv.$stage · $stageLabel",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(spacing.sm))

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
                        Text(
                            text = "회복 지원 — ${NumberFormatter.compact(item.recoveryCost.toDouble())}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                } else {
                    Text(
                        text = "최대 회복 단계 도달",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                onCompleteOnboarding = {},
                onClaimDailyBonus = {},
                onNavigateToArchive = {},
                onNavigateToSettings = {},
            )
        }
    }
}

/**
 * 360dp 좁은 화면 + 큰 숫자(보호구역 Lv.99, 1.5M 보호 포인트, 999/sec 생산) 시나리오.
 * 이 프리뷰가 잘림/오버플로 없이 보이면 v0.4.0 이후 도입한 카드 스택이 360dp에서
 * 안전하다는 신호. 디자인 회귀 점검용.
 */
@Preview(name = "Home / 360dp / 큰 숫자", widthDp = 360, heightDp = 800)
@Composable
private fun HomeScreenSmallScreenPreview() {
    WildHavenTheme {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            HomeContent(
                uiState = HomeUiState(
                    carePoint = 1_543_210.0,
                    sanctuaryLevel = 99,
                    productionPerSecond = 999.99,
                    protectedAnimalCount = 5,
                    discoveredAnimalCount = 5,
                    totalAnimalCount = 5,
                    sanctuaryUpgradeCost = 9_999_999L,
                    tapReward = 999.99,
                    protectedAnimalIds = listOf("rabbit_001", "fox_001", "deer_001", "owl_001", "lynx_001"),
                ),
                onOfflineRewardConfirmed = {},
                onUpgradeSanctuary = {},
                onSupportRecovery = {},
                onTapSanctuary = { 999.99 },
                onCompleteOnboarding = {},
                onClaimDailyBonus = {},
                onNavigateToArchive = {},
                onNavigateToSettings = {},
            )
        }
    }
}
