package com.jeiel85.wildhavenidle.presentation.settings

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jeiel85.wildhavenidle.core.design.WildHavenTheme
import com.jeiel85.wildhavenidle.data.repository.GameRepository
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    gameRepository: GameRepository,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showResetDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("데이터 초기화") },
            text = { Text("모든 진행 상황이 삭제되고 초기 상태로 되돌아갑니다. 계속하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        gameRepository.resetData()
                        showResetDialog = false
                    }
                }) {
                    Text("초기화", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("취소")
                }
            },
        )
    }

    val spacing = WildHavenTheme.spacing
    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(spacing.screenPadding),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "설정",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Button(onClick = onNavigateBack) {
                Text("닫기")
            }
        }

        Spacer(modifier = Modifier.height(spacing.xxl))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = WildHavenTheme.elevation.md,
        ) {
            Column(modifier = Modifier.padding(spacing.cardPadding)) {
                Text(
                    text = "게임 정보",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(spacing.sm))
                Text(
                    text = "Wild Haven Idle v0.5.0",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "야생동물 보호구역 방치형 시뮬레이션",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        Spacer(modifier = Modifier.height(spacing.md))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.errorContainer,
        ) {
            Column(modifier = Modifier.padding(spacing.cardPadding)) {
                Text(
                    text = "데이터 관리",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
                Spacer(modifier = Modifier.height(spacing.sm))
                Text(
                    text = "모든 저장 데이터를 초기화합니다. 이 작업은 되돌릴 수 없습니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
                Spacer(modifier = Modifier.height(spacing.md))
                Button(
                    onClick = { showResetDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("데이터 초기화")
                }
            }
        }
    }
}
