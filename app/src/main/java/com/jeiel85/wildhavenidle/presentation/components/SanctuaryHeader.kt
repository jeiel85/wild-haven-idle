package com.jeiel85.wildhavenidle.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jeiel85.wildhavenidle.R
import com.jeiel85.wildhavenidle.core.design.WildHavenTheme
import com.jeiel85.wildhavenidle.core.format.NumberFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

private const val ANIMATION_PERIOD_MS = 16_000

@Composable
fun SanctuaryHeader(
    sanctuaryLevel: Int,
    protectedAnimalIds: List<String>,
    onTap: () -> Double,
    modifier: Modifier = Modifier,
) {
    val headerHeight = 160.dp
    val groundFraction = 0.62f

    // 동물 스프라이트의 좌우 산책 + 상하 보빙에 사용. 풍경 자체는 PNG 정적.
    val infinite = rememberInfiniteTransition(label = "header")
    val phase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = ANIMATION_PERIOD_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "phase",
    )

    val floaters = remember { mutableStateListOf<Floater>() }
    var nextFloaterId by remember { mutableLongStateOf(0L) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight)
            .clip(MaterialTheme.shapes.large)
            .pointerInput(Unit) {
                detectTapGestures { tap ->
                    val reward = onTap()
                    floaters += Floater(
                        id = nextFloaterId++,
                        startOffset = tap,
                        value = reward,
                    )
                }
            },
    ) {
        Image(
            painter = painterResource(id = R.drawable.wh_habitat_forest_001),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        val groundY = headerHeight * groundFraction
        val spriteSize = 44.dp
        val sidePadding = 8.dp
        val travel = maxWidth - spriteSize - sidePadding * 2

        protectedAnimalIds.forEachIndexed { index, animalId ->
            val perAnimalPhase = phase + index * 1.37f
            val swing = sin(perAnimalPhase)
            val bob = sin(perAnimalPhase * 3.1f) * 2.5f
            val facingRight = cos(perAnimalPhase) > 0f
            val progress = (swing + 1f) / 2f
            val xDp = sidePadding + travel * progress
            val yDp = groundY - spriteSize + bob.dp

            AnimalIllustration(
                animalId = animalId,
                size = spriteSize,
                modifier = Modifier
                    .offset(x = xDp, y = yDp)
                    .graphicsLayer { scaleX = if (facingRight) 1f else -1f },
            )
        }

        floaters.forEach { floater ->
            CarePointFloater(
                floater = floater,
                onFinished = {
                    floaters.removeAll { it.id == floater.id }
                },
            )
        }

        Text(
            text = "Lv.$sanctuaryLevel",
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF3A4F3A),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .offset(x = 12.dp, y = 12.dp),
        )
    }
}

private data class Floater(
    val id: Long,
    val startOffset: Offset,
    val value: Double,
)

@Composable
private fun CarePointFloater(
    floater: Floater,
    onFinished: () -> Unit,
) {
    val rise = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(floater.id) {
        launch { alpha.animateTo(1f, tween(120)) }
        launch { rise.animateTo(140f, tween(900)) }
        delay(450)
        alpha.animateTo(0f, tween(450))
        onFinished()
    }

    Text(
        text = "+${NumberFormatter.compact(floater.value)}",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF2F7D57),
        modifier = Modifier
            .offset {
                IntOffset(
                    x = floater.startOffset.x.toInt(),
                    y = (floater.startOffset.y - rise.value).toInt(),
                )
            }
            .alpha(alpha.value),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F9F4)
@Composable
private fun SanctuaryHeaderPreview() {
    WildHavenTheme {
        Box(modifier = Modifier.fillMaxWidth()) {
            SanctuaryHeader(
                sanctuaryLevel = 3,
                protectedAnimalIds = listOf("rabbit_001", "fox_001", "deer_001"),
                onTap = { 1.0 },
            )
        }
    }
}
