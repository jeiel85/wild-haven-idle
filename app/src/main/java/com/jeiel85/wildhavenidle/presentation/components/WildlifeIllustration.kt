package com.jeiel85.wildhavenidle.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import com.jeiel85.wildhavenidle.R
import com.jeiel85.wildhavenidle.core.design.WildHavenTheme

/**
 * AI 생성 PNG로 교체된 동물 ID 매핑.
 * 매핑에 있으면 [AnimalIllustration]은 PNG를, 없으면 절차적 Canvas를 사용한다.
 * AnimalSilhouette(LOCKED 상태)은 그레이 톤 통일을 위해 항상 절차적 Canvas 사용.
 */
private val animalDrawableMap: Map<String, Int> = mapOf(
    "rabbit_001" to R.drawable.wh_animal_rabbit_001,
)

private val InkColor = Color(0xFF3A4F3A)
private val WarmBrown = Color(0xFF6B5744)
private val SoftGray = Color(0xFF9CA89C)
private val AccentLeaf = Color(0xFF4F8A5B)
private val AccentGold = Color(0xFFC4A24E)
private val AccentBlue = Color(0xFF5B7F95)

@Composable
fun AnimalIllustration(
    animalId: String,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    color: Color = WarmBrown,
) {
    val drawableRes = animalDrawableMap[animalId]
    if (drawableRes != null) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier.size(size),
        )
        return
    }

    Box(modifier = modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            when (animalId) {
                "fox_001" -> drawFox(w, h, color)
                "deer_001" -> drawDeer(w, h, color)
                "owl_001" -> drawOwl(w, h, color)
                "lynx_001" -> drawLynx(w, h, color)
            }
        }
    }
}

@Composable
fun AnimalSilhouette(
    animalId: String,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
) {
    Box(modifier = modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            when (animalId) {
                "rabbit_001" -> drawRabbit(w, h, SoftGray)
                "fox_001" -> drawFox(w, h, SoftGray)
                "deer_001" -> drawDeer(w, h, SoftGray)
                "owl_001" -> drawOwl(w, h, SoftGray)
                "lynx_001" -> drawLynx(w, h, SoftGray)
            }
        }
    }
}

private fun DrawScope.drawRabbit(w: Float, h: Float, color: Color) {
    val cx = w / 2f
    val cy = h / 2f
    val scale = w / 100f
    val stroke = Stroke(width = 2.5f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)

    // body
    val body = Path().apply {
        moveTo(cx - 18f * scale, cy + 8f * scale)
        cubicTo(
            cx - 28f * scale, cy + 2f * scale,
            cx - 24f * scale, cy - 22f * scale,
            cx, cy - 24f * scale
        )
        cubicTo(
            cx + 24f * scale, cy - 22f * scale,
            cx + 28f * scale, cy + 2f * scale,
            cx + 18f * scale, cy + 8f * scale
        )
        cubicTo(
            cx + 14f * scale, cy + 20f * scale,
            cx - 14f * scale, cy + 20f * scale,
            cx - 18f * scale, cy + 8f * scale
        )
        close()
    }
    drawPath(body, color = color.copy(alpha = 0.12f))
    drawPath(body, color = color, style = stroke)

    // left ear
    val leftEar = Path().apply {
        moveTo(cx - 14f * scale, cy - 20f * scale)
        cubicTo(
            cx - 24f * scale, cy - 50f * scale,
            cx - 6f * scale, cy - 46f * scale,
            cx - 6f * scale, cy - 18f * scale
        )
        close()
    }
    drawPath(leftEar, color = color.copy(alpha = 0.08f))
    drawPath(leftEar, color = color, style = stroke)

    // right ear
    val rightEar = Path().apply {
        moveTo(cx + 14f * scale, cy - 20f * scale)
        cubicTo(
            cx + 24f * scale, cy - 50f * scale,
            cx + 6f * scale, cy - 46f * scale,
            cx + 6f * scale, cy - 18f * scale
        )
        close()
    }
    drawPath(rightEar, color = color.copy(alpha = 0.08f))
    drawPath(rightEar, color = color, style = stroke)

    // tail
    drawCircle(
        color = color.copy(alpha = 0.12f),
        radius = 7f * scale,
        center = Offset(cx + 16f * scale, cy + 14f * scale),
    )
    drawCircle(
        color = color,
        radius = 7f * scale,
        center = Offset(cx + 16f * scale, cy + 14f * scale),
        style = stroke,
    )

    // eyes
    drawCircle(color = color, radius = 1.8f * scale, center = Offset(cx - 5f * scale, cy - 10f * scale))
    drawCircle(color = color, radius = 1.8f * scale, center = Offset(cx + 5f * scale, cy - 10f * scale))

    // nose
    drawCircle(color = color.copy(alpha = 0.6f), radius = 1.2f * scale, center = Offset(cx, cy - 4f * scale))
}

private fun DrawScope.drawFox(w: Float, h: Float, color: Color) {
    val cx = w / 2f
    val cy = h / 2f + 4f
    val scale = w / 100f
    val stroke = Stroke(width = 2.5f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val foxColor = Color(0xFFC46B3A)

    // body
    val body = Path().apply {
        moveTo(cx - 16f * scale, cy + 4f * scale)
        cubicTo(
            cx - 22f * scale, cy - 14f * scale,
            cx + 22f * scale, cy - 14f * scale,
            cx + 16f * scale, cy + 4f * scale
        )
        cubicTo(
            cx + 12f * scale, cy + 18f * scale,
            cx - 12f * scale, cy + 18f * scale,
            cx - 16f * scale, cy + 4f * scale
        )
        close()
    }
    drawPath(body, color = foxColor.copy(alpha = 0.14f))
    drawPath(body, color = foxColor, style = stroke)

    // head
    val head = Path().apply {
        moveTo(cx, cy - 24f * scale)
        lineTo(cx - 10f * scale, cy - 8f * scale)
        lineTo(cx + 10f * scale, cy - 8f * scale)
        close()
    }
    drawPath(head, color = foxColor.copy(alpha = 0.14f))
    drawPath(head, color = foxColor, style = stroke)

    // left ear
    val leftEar = Path().apply {
        moveTo(cx - 8f * scale, cy - 20f * scale)
        lineTo(cx - 16f * scale, cy - 42f * scale)
        lineTo(cx - 4f * scale, cy - 18f * scale)
        close()
    }
    drawPath(leftEar, color = foxColor.copy(alpha = 0.14f))
    drawPath(leftEar, color = foxColor, style = stroke)

    // right ear
    val rightEar = Path().apply {
        moveTo(cx + 8f * scale, cy - 20f * scale)
        lineTo(cx + 16f * scale, cy - 42f * scale)
        lineTo(cx + 4f * scale, cy - 18f * scale)
        close()
    }
    drawPath(rightEar, color = foxColor.copy(alpha = 0.14f))
    drawPath(rightEar, color = foxColor, style = stroke)

    // tail
    val tail = Path().apply {
        moveTo(cx + 14f * scale, cy + 12f * scale)
        cubicTo(
            cx + 36f * scale, cy + 6f * scale,
            cx + 34f * scale, cy - 10f * scale,
            cx + 22f * scale, cy - 4f * scale
        )
        cubicTo(
            cx + 28f * scale, cy + 6f * scale,
            cx + 22f * scale, cy + 16f * scale,
            cx + 14f * scale, cy + 16f * scale
        )
    }
    drawPath(tail, color = Color.White.copy(alpha = 0.7f))
    drawPath(tail, color = foxColor, style = stroke)

    // eyes
    drawCircle(color = color, radius = 1.6f * scale, center = Offset(cx - 4f * scale, cy - 15f * scale))
    drawCircle(color = color, radius = 1.6f * scale, center = Offset(cx + 4f * scale, cy - 15f * scale))

    // nose
    drawCircle(color = color, radius = 1.2f * scale, center = Offset(cx, cy - 9f * scale))
}

private fun DrawScope.drawDeer(w: Float, h: Float, color: Color) {
    val cx = w / 2f
    val cy = h / 2f
    val scale = w / 100f
    val stroke = Stroke(width = 2.5f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val deerColor = Color(0xFF8B6B4A)

    // body
    val body = Path().apply {
        moveTo(cx - 14f * scale, cy + 10f * scale)
        cubicTo(
            cx - 24f * scale, cy - 6f * scale,
            cx + 24f * scale, cy - 6f * scale,
            cx + 14f * scale, cy + 10f * scale
        )
        cubicTo(
            cx + 10f * scale, cy + 22f * scale,
            cx - 10f * scale, cy + 22f * scale,
            cx - 14f * scale, cy + 10f * scale
        )
        close()
    }
    drawPath(body, color = deerColor.copy(alpha = 0.14f))
    drawPath(body, color = deerColor, style = stroke)

    // neck
    val neck = Path().apply {
        moveTo(cx, cy - 6f * scale)
        lineTo(cx - 6f * scale, cy - 26f * scale)
        lineTo(cx + 6f * scale, cy - 26f * scale)
        lineTo(cx, cy - 6f * scale)
        close()
    }
    drawPath(neck, color = deerColor.copy(alpha = 0.14f))
    drawPath(neck, color = deerColor, style = stroke)

    // head
    drawOval(
        color = deerColor.copy(alpha = 0.14f),
        topLeft = Offset(cx - 11f * scale, cy - 38f * scale),
        size = Size(22f * scale, 16f * scale),
    )
    drawOval(
        color = deerColor,
        topLeft = Offset(cx - 11f * scale, cy - 38f * scale),
        size = Size(22f * scale, 16f * scale),
        style = stroke,
    )

    // left antler
    val leftAntler = Path().apply {
        moveTo(cx - 4f * scale, cy - 38f * scale)
        lineTo(cx - 10f * scale, cy - 58f * scale)
        moveTo(cx - 6f * scale, cy - 48f * scale)
        lineTo(cx - 14f * scale, cy - 44f * scale)
        moveTo(cx - 5f * scale, cy - 50f * scale)
        lineTo(cx - 2f * scale, cy - 56f * scale)
    }
    drawPath(leftAntler, color = deerColor, style = stroke)

    // right antler
    val rightAntler = Path().apply {
        moveTo(cx + 4f * scale, cy - 38f * scale)
        lineTo(cx + 10f * scale, cy - 58f * scale)
        moveTo(cx + 6f * scale, cy - 48f * scale)
        lineTo(cx + 14f * scale, cy - 44f * scale)
        moveTo(cx + 5f * scale, cy - 50f * scale)
        lineTo(cx + 2f * scale, cy - 56f * scale)
    }
    drawPath(rightAntler, color = deerColor, style = stroke)

    // spots on back
    drawCircle(color = deerColor.copy(alpha = 0.3f), radius = 2f * scale, center = Offset(cx - 8f * scale, cy))
    drawCircle(color = deerColor.copy(alpha = 0.3f), radius = 2f * scale, center = Offset(cx + 6f * scale, cy + 4f * scale))
    drawCircle(color = deerColor.copy(alpha = 0.3f), radius = 1.5f * scale, center = Offset(cx + 10f * scale, cy - 4f * scale))

    // eyes
    drawCircle(color = color, radius = 2f * scale, center = Offset(cx - 4f * scale, cy - 32f * scale))
    drawCircle(color = color, radius = 2f * scale, center = Offset(cx + 4f * scale, cy - 32f * scale))

    // nose
    drawOval(
        color = color.copy(alpha = 0.6f),
        topLeft = Offset(cx - 1.5f * scale, cy - 27f * scale),
        size = Size(3f * scale, 2f * scale),
    )
}

private fun DrawScope.drawOwl(w: Float, h: Float, color: Color) {
    val cx = w / 2f
    val cy = h / 2f
    val scale = w / 100f
    val stroke = Stroke(width = 2.5f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val owlColor = Color(0xFF5B6B55)

    // body (round)
    drawOval(
        color = owlColor.copy(alpha = 0.14f),
        topLeft = Offset(cx - 22f * scale, cy - 14f * scale),
        size = Size(44f * scale, 48f * scale),
    )
    drawOval(
        color = owlColor,
        topLeft = Offset(cx - 22f * scale, cy - 14f * scale),
        size = Size(44f * scale, 48f * scale),
        style = stroke,
    )

    // left eye (large)
    drawCircle(
        color = Color.White.copy(alpha = 0.6f),
        radius = 10f * scale,
        center = Offset(cx - 11f * scale, cy - 2f * scale),
    )
    drawCircle(
        color = color,
        radius = 10f * scale,
        center = Offset(cx - 11f * scale, cy - 2f * scale),
        style = stroke,
    )
    drawCircle(color = color, radius = 3f * scale, center = Offset(cx - 11f * scale, cy - 2f * scale))

    // right eye (large)
    drawCircle(
        color = Color.White.copy(alpha = 0.6f),
        radius = 10f * scale,
        center = Offset(cx + 11f * scale, cy - 2f * scale),
    )
    drawCircle(
        color = color,
        radius = 10f * scale,
        center = Offset(cx + 11f * scale, cy - 2f * scale),
        style = stroke,
    )
    drawCircle(color = color, radius = 3f * scale, center = Offset(cx + 11f * scale, cy - 2f * scale))

    // beak
    val beak = Path().apply {
        moveTo(cx - 3f * scale, cy + 6f * scale)
        lineTo(cx, cy + 14f * scale)
        lineTo(cx + 3f * scale, cy + 6f * scale)
        close()
    }
    drawPath(beak, color = AccentGold.copy(alpha = 0.4f))
    drawPath(beak, color = color, style = stroke)

    // ear tufts
    val leftTuft = Path().apply {
        moveTo(cx - 18f * scale, cy - 12f * scale)
        lineTo(cx - 24f * scale, cy - 26f * scale)
        lineTo(cx - 16f * scale, cy - 16f * scale)
    }
    drawPath(leftTuft, color = owlColor, style = stroke)

    val rightTuft = Path().apply {
        moveTo(cx + 18f * scale, cy - 12f * scale)
        lineTo(cx + 24f * scale, cy - 26f * scale)
        lineTo(cx + 16f * scale, cy - 16f * scale)
    }
    drawPath(rightTuft, color = owlColor, style = stroke)

    // wing lines
    drawArc(
        color = owlColor.copy(alpha = 0.3f),
        startAngle = 30f,
        sweepAngle = 120f,
        useCenter = false,
        topLeft = Offset(cx - 26f * scale, cy + 4f * scale),
        size = Size(20f * scale, 28f * scale),
        style = stroke,
    )
    drawArc(
        color = owlColor.copy(alpha = 0.3f),
        startAngle = 30f,
        sweepAngle = 120f,
        useCenter = false,
        topLeft = Offset(cx + 6f * scale, cy + 4f * scale),
        size = Size(20f * scale, 28f * scale),
        style = stroke,
    )
}

private fun DrawScope.drawLynx(w: Float, h: Float, color: Color) {
    val cx = w / 2f
    val cy = h / 2f
    val scale = w / 100f
    val stroke = Stroke(width = 2.5f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val lynxColor = Color(0xFF8B7355)

    // body
    val body = Path().apply {
        moveTo(cx - 18f * scale, cy + 10f * scale)
        cubicTo(
            cx - 25f * scale, cy - 8f * scale,
            cx + 25f * scale, cy - 8f * scale,
            cx + 18f * scale, cy + 10f * scale
        )
        cubicTo(
            cx + 14f * scale, cy + 24f * scale,
            cx - 14f * scale, cy + 24f * scale,
            cx - 18f * scale, cy + 10f * scale
        )
        close()
    }
    drawPath(body, color = lynxColor.copy(alpha = 0.14f))
    drawPath(body, color = lynxColor, style = stroke)

    // head (round)
    drawCircle(
        color = lynxColor.copy(alpha = 0.14f),
        radius = 16f * scale,
        center = Offset(cx, cy - 10f * scale),
    )
    drawCircle(
        color = lynxColor,
        radius = 16f * scale,
        center = Offset(cx, cy - 10f * scale),
        style = stroke,
    )

    // left ear with tuft
    val leftEar = Path().apply {
        moveTo(cx - 12f * scale, cy - 22f * scale)
        lineTo(cx - 16f * scale, cy - 40f * scale)
        lineTo(cx - 6f * scale, cy - 20f * scale)
        close()
    }
    drawPath(leftEar, color = lynxColor.copy(alpha = 0.14f))
    drawPath(leftEar, color = lynxColor, style = stroke)

    // ear tuft (left)
    drawLine(
        color = lynxColor,
        start = Offset(cx - 14f * scale, cy - 36f * scale),
        end = Offset(cx - 13f * scale, cy - 46f * scale),
        strokeWidth = 1.5f * scale,
        cap = StrokeCap.Round,
    )

    // right ear with tuft
    val rightEar = Path().apply {
        moveTo(cx + 12f * scale, cy - 22f * scale)
        lineTo(cx + 16f * scale, cy - 40f * scale)
        lineTo(cx + 6f * scale, cy - 20f * scale)
        close()
    }
    drawPath(rightEar, color = lynxColor.copy(alpha = 0.14f))
    drawPath(rightEar, color = lynxColor, style = stroke)

    // ear tuft (right)
    drawLine(
        color = lynxColor,
        start = Offset(cx + 14f * scale, cy - 36f * scale),
        end = Offset(cx + 13f * scale, cy - 46f * scale),
        strokeWidth = 1.5f * scale,
        cap = StrokeCap.Round,
    )

    // eyes
    drawCircle(color = color, radius = 2f * scale, center = Offset(cx - 6f * scale, cy - 14f * scale))
    drawCircle(color = color, radius = 2f * scale, center = Offset(cx + 6f * scale, cy - 14f * scale))

    // nose
    drawCircle(color = color.copy(alpha = 0.7f), radius = 1.5f * scale, center = Offset(cx, cy - 7f * scale))

    // whiskers
    val whiskerColor = lynxColor.copy(alpha = 0.5f)
    val whiskerW = 1f * scale
    // left whiskers
    drawLine(whiskerColor, Offset(cx - 10f * scale, cy - 6f * scale), Offset(cx - 26f * scale, cy - 10f * scale), whiskerW, cap = StrokeCap.Round)
    drawLine(whiskerColor, Offset(cx - 10f * scale, cy - 4f * scale), Offset(cx - 26f * scale, cy - 2f * scale), whiskerW, cap = StrokeCap.Round)
    // right whiskers
    drawLine(whiskerColor, Offset(cx + 10f * scale, cy - 6f * scale), Offset(cx + 26f * scale, cy - 10f * scale), whiskerW, cap = StrokeCap.Round)
    drawLine(whiskerColor, Offset(cx + 10f * scale, cy - 4f * scale), Offset(cx + 26f * scale, cy - 2f * scale), whiskerW, cap = StrokeCap.Round)

    // short tail
    val tail = Path().apply {
        moveTo(cx + 16f * scale, cy + 18f * scale)
        cubicTo(
            cx + 28f * scale, cy + 14f * scale,
            cx + 26f * scale, cy + 6f * scale,
            cx + 18f * scale, cy + 12f * scale
        )
        close()
    }
    drawPath(tail, color = lynxColor.copy(alpha = 0.14f))
    drawPath(tail, color = lynxColor, style = stroke)
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F9F4)
@Composable
private fun AnimalIllustrationsPreview() {
    WildHavenTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AnimalIllustration("rabbit_001", size = 80.dp)
            AnimalIllustration("fox_001", size = 80.dp)
            AnimalIllustration("deer_001", size = 80.dp)
            AnimalIllustration("owl_001", size = 80.dp)
            AnimalIllustration("lynx_001", size = 80.dp)
        }
    }
}
