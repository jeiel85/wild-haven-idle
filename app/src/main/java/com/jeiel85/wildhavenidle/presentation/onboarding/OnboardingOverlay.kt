package com.jeiel85.wildhavenidle.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeiel85.wildhavenidle.core.design.WildHavenTheme

private data class OnboardingStep(
    val title: String,
    val body: String,
    val emoji: String,
)

private val onboardingSteps: List<OnboardingStep> = listOf(
    OnboardingStep(
        title = "보호구역에 오신 것을 환영합니다",
        body = "이곳은 작은 야생동물 보호구역입니다. 시간이 지날수록 보호 포인트가 자동으로 모이고, 모인 포인트로 보호구역을 키울 수 있습니다.",
        emoji = "🌿",
    ),
    OnboardingStep(
        title = "보호구역을 탭해 보세요",
        body = "화면 상단의 숲을 탭하면 보호 포인트를 추가로 얻을 수 있습니다. 가볍게 탭만 해도 보호구역이 조금씩 자랍니다.",
        emoji = "🌳",
    ),
    OnboardingStep(
        title = "다음 목표를 따라가 보세요",
        body = "“다음 해금” 카드는 다음에 합류할 동물과 진행률을 보여줍니다. 회복 지원과 보호구역 확장을 통해 새로운 동물을 만나보세요.",
        emoji = "🦊",
    ),
)

@Composable
fun OnboardingOverlay(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var stepIndex by remember { mutableIntStateOf(0) }
    val step = onboardingSteps[stepIndex]
    val isLast = stepIndex == onboardingSteps.lastIndex

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xCC0F1A12))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .padding(24.dp)
                .widthIn(max = 360.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = step.emoji,
                    style = MaterialTheme.typography.displaySmall,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = step.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = step.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(20.dp))
                StepIndicator(
                    total = onboardingSteps.size,
                    current = stepIndex,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onComplete) {
                        Text(text = "건너뛰기")
                    }
                    Button(
                        onClick = {
                            if (isLast) {
                                onComplete()
                            } else {
                                stepIndex++
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                    ) {
                        Text(text = if (isLast) "시작하기" else "다음")
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIndicator(
    total: Int,
    current: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(total) { index ->
            val isActive = index == current
            Box(
                modifier = Modifier
                    .size(if (isActive) 10.dp else 8.dp)
                    .background(
                        color = if (isActive) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        },
                        shape = CircleShape,
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingOverlayPreview() {
    WildHavenTheme {
        OnboardingOverlay(onComplete = {})
    }
}
