package com.jeiel85.wildhavenidle.presentation.animals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jeiel85.wildhavenidle.core.format.NumberFormatter
import com.jeiel85.wildhavenidle.presentation.components.AnimalIllustration
import com.jeiel85.wildhavenidle.presentation.components.RecoveryStageLabel

@Composable
fun AnimalListScreen(
    viewModel: AnimalListViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "구조 동물",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Button(onClick = onNavigateBack) {
                Text("닫기")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SanctuarySection(
            level = uiState.sanctuaryLevel,
            carePoint = uiState.carePoint,
            productionPerSecond = uiState.productionPerSecond,
            upgradeCost = uiState.sanctuaryUpgradeCost,
            onUpgrade = viewModel::upgradeSanctuary,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "보호 중인 동물",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(uiState.animals, key = { it.definition.id }) { item ->
                AnimalRecoveryCard(
                    item = item,
                    carePoint = uiState.carePoint,
                    onSupport = { viewModel.supportRecovery(item.definition.id) },
                )
            }
        }
    }
}

@Composable
private fun SanctuarySection(
    level: Int,
    carePoint: Double,
    productionPerSecond: Double,
    upgradeCost: Long,
    onUpgrade: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "보호구역 Lv.$level",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "생산량: ${NumberFormatter.perSecond(productionPerSecond)}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "보호 포인트: ${NumberFormatter.compact(carePoint)}",
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onUpgrade,
                enabled = carePoint >= upgradeCost && upgradeCost > 0L,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("보호구역 업그레이드 - ${NumberFormatter.compact(upgradeCost.toDouble())}")
            }
        }
    }
}

@Composable
private fun AnimalRecoveryCard(
    item: AnimalListItem,
    carePoint: Double,
    onSupport: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val animal = item.protectedAnimal ?: return
    val stage = animal.recoveryStage
    val stageLabel = RecoveryStageLabel.of(stage)
    val maxStage = item.definition.maxRecoveryStage
    val canSupport = carePoint >= item.recoveryCost && item.recoveryCost > 0L && stage < maxStage

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
                size = 72.dp,
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
                        text = item.definition.nameEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "회복 단계: Lv.$stage ($stageLabel)",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = NumberFormatter.perSecond(item.supportBonus),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                if (stage < maxStage) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onSupport,
                        enabled = canSupport,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                        ),
                    ) {
                        Text("회복 지원 - ${NumberFormatter.compact(item.recoveryCost.toDouble())}")
                    }
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
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
