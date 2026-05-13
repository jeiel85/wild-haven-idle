package com.jeiel85.wildhavenidle.presentation.archive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jeiel85.wildhavenidle.core.format.NumberFormatter
import com.jeiel85.wildhavenidle.data.model.ArchiveState
import com.jeiel85.wildhavenidle.presentation.components.AnimalIllustration
import com.jeiel85.wildhavenidle.presentation.components.AnimalSilhouette
import com.jeiel85.wildhavenidle.presentation.components.RecoveryStageLabel

@Composable
fun ArchiveScreen(
    viewModel: ArchiveViewModel,
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
                text = "도감",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Button(onClick = onNavigateBack) {
                Text("닫기")
            }
        }

        Text(
            text = "발견: ${uiState.discoveredCount}/${uiState.totalCount} · 보호 중: ${uiState.protectedCount}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(0.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(uiState.items, key = { it.definition.id }) { item ->
                ArchiveCard(item = item)
            }
        }
    }
}

@Composable
private fun ArchiveCard(
    item: ArchiveItem,
    modifier: Modifier = Modifier,
) {
    val color = when (item.state) {
        ArchiveState.PROTECTED -> MaterialTheme.colorScheme.primaryContainer
        ArchiveState.DISCOVERED -> MaterialTheme.colorScheme.secondaryContainer
        ArchiveState.LOCKED -> MaterialTheme.colorScheme.surfaceVariant
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = color,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val animalId = item.definition.id

            when (item.state) {
                ArchiveState.LOCKED -> {
                    AnimalSilhouette(animalId = animalId, size = 72.dp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "???",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
                ArchiveState.DISCOVERED -> {
                    AnimalIllustration(animalId = animalId, size = 72.dp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.definition.nameKo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = item.definition.descriptionKo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center,
                    )
                }
                ArchiveState.PROTECTED -> {
                    AnimalIllustration(animalId = animalId, size = 72.dp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.definition.nameKo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    val stage = item.protectedAnimal?.recoveryStage ?: 1
                    Text(
                        text = RecoveryStageLabel.of(stage),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "Lv.$stage",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = NumberFormatter.perSecond(item.supportBonus),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}
