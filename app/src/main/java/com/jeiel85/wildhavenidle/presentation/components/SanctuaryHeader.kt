package com.jeiel85.wildhavenidle.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jeiel85.wildhavenidle.core.design.WildHavenTheme
import com.jeiel85.wildhavenidle.core.format.NumberFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

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

    val trees = remember(sanctuaryLevel) { computeTrees(sanctuaryLevel) }
    val flowers = remember(sanctuaryLevel) { computeFlowers(sanctuaryLevel) }

    val floaters = remember { mutableStateListOf<Floater>() }
    var nextFloaterId by remember { mutableLongStateOf(0L) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight)
            .clip(RoundedCornerShape(16.dp))
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
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawScenery(size, phase, trees, flowers)
        }

        val animalSlotCount = protectedAnimalIds.size
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

private data class Tree(val xRel: Float, val scale: Float)
private data class Flower(val xRel: Float, val yRel: Float, val hue: Int)

private fun computeTrees(level: Int): List<Tree> {
    val base = listOf(
        Tree(0.12f, 0.75f),
        Tree(0.25f, 0.60f),
        Tree(0.38f, 0.72f),
        Tree(0.50f, 0.55f),
        Tree(0.62f, 0.68f),
        Tree(0.75f, 0.57f),
        Tree(0.88f, 0.70f),
    )
    val extraCount = min(level - 1, 10)
    if (extraCount <= 0) return base

    val rng = Random(level * 31L + 7L)
    val extras = List(extraCount) {
        Tree(
            xRel = 0.05f + rng.nextFloat() * 0.9f,
            scale = 0.5f + rng.nextFloat() * 0.5f,
        )
    }
    return base + extras
}

private fun computeFlowers(level: Int): List<Flower> {
    val count = min(level * 2, 24)
    if (count <= 0) return emptyList()
    val rng = Random(level * 53L + 11L)
    val hues = listOf(0xFFE89BB1.toInt(), 0xFFF5D76E.toInt(), 0xFFC8E6C9.toInt(), 0xFFB39DDB.toInt())
    return List(count) {
        Flower(
            xRel = 0.04f + rng.nextFloat() * 0.92f,
            yRel = 0.78f + rng.nextFloat() * 0.18f,
            hue = hues[rng.nextInt(hues.size)],
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawScenery(
    size: Size,
    phase: Float,
    trees: List<Tree>,
    flowers: List<Flower>,
) {
    val forestGreen = Color(0xFF3B6B48)
    val deepGreen = Color(0xFF2D5639)
    val paleSky = Color(0xFFE8F0E4)
    val warmSky = Color(0xFFF7E9C7)
    val treeBrown = Color(0xFF6B5037)
    val sunColor = Color(0xFFF5D76E)
    val grassColor = Color(0xFF7DAA65)
    val grassShade = Color(0xFF6A9356)

    val w = size.width
    val h = size.height
    val scale = w / 360f

    drawRect(color = paleSky, size = Size(w, h * 0.65f))
    drawRect(
        color = warmSky.copy(alpha = 0.4f),
        topLeft = Offset(0f, h * 0.35f),
        size = Size(w, h * 0.30f),
    )

    val sunBob = sin(phase * 0.5f) * 4f
    drawCircle(
        color = sunColor.copy(alpha = 0.85f),
        radius = 22f * scale,
        center = Offset(w * 0.85f, h * 0.22f + sunBob),
    )
    drawCircle(
        color = sunColor.copy(alpha = 0.25f),
        radius = 34f * scale,
        center = Offset(w * 0.85f, h * 0.22f + sunBob),
    )

    val cloudX1 = (phase / (2f * Math.PI.toFloat())) % 1f
    drawCloud(Offset(w * cloudX1, h * 0.18f), 30f * scale, Color.White.copy(alpha = 0.7f))
    val cloudX2 = ((phase / (2f * Math.PI.toFloat())) + 0.55f) % 1f
    drawCloud(Offset(w * cloudX2, h * 0.30f), 22f * scale, Color.White.copy(alpha = 0.55f))

    val ground = Path().apply {
        moveTo(0f, h * 0.60f)
        cubicTo(w * 0.25f, h * 0.55f, w * 0.5f, h * 0.65f, w * 0.75f, h * 0.58f)
        lineTo(w, h * 0.70f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    drawPath(ground, color = grassColor)

    val groundShade = Path().apply {
        moveTo(0f, h * 0.78f)
        cubicTo(w * 0.30f, h * 0.74f, w * 0.55f, h * 0.84f, w * 0.80f, h * 0.78f)
        lineTo(w, h * 0.88f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }
    drawPath(groundShade, color = grassShade.copy(alpha = 0.7f))

    for (flower in flowers) {
        val fx = w * flower.xRel
        val fy = h * flower.yRel
        drawCircle(
            color = Color(flower.hue).copy(alpha = 0.9f),
            radius = 2.2f * scale,
            center = Offset(fx, fy),
        )
    }

    for (tree in trees) {
        val tx = w * tree.xRel
        val ts = tree.scale
        val baseY = h * 0.65f
        val sway = sin(phase + tx * 0.01f) * 1.5f

        drawRect(
            color = treeBrown,
            topLeft = Offset(tx - 3f * ts, baseY - 30f * ts),
            size = Size(6f * ts, 30f * ts),
        )

        val foliageColor = if (tree.scale > 0.65f) forestGreen else deepGreen
        val foliage = Path().apply {
            moveTo(tx + sway, baseY - 55f * ts)
            lineTo(tx + 18f * ts + sway, baseY - 10f * ts)
            lineTo(tx - 18f * ts + sway, baseY - 10f * ts)
            close()
        }
        drawPath(foliage, color = foliageColor)

        val foliage2 = Path().apply {
            moveTo(tx + sway * 0.6f, baseY - 48f * ts)
            lineTo(tx + 13f * ts + sway * 0.6f, baseY - 22f * ts)
            lineTo(tx - 13f * ts + sway * 0.6f, baseY - 22f * ts)
            close()
        }
        drawPath(foliage2, color = foliageColor.copy(alpha = 0.85f))
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCloud(
    center: Offset,
    radius: Float,
    color: Color,
) {
    drawCircle(color = color, radius = radius, center = center)
    drawCircle(color = color, radius = radius * 0.8f, center = Offset(center.x + radius * 0.8f, center.y + radius * 0.1f))
    drawCircle(color = color, radius = radius * 0.7f, center = Offset(center.x - radius * 0.7f, center.y + radius * 0.15f))
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
