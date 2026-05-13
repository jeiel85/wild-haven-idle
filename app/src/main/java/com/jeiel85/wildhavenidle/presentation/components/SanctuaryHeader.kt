package com.jeiel85.wildhavenidle.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeiel85.wildhavenidle.core.design.WildHavenTheme

@Composable
fun SanctuaryHeader(
    modifier: Modifier = Modifier,
) {
    val forestGreen = Color(0xFF3B6B48)
    val paleSky = Color(0xFFE8F0E4)
    val treeBrown = Color(0xFF6B5037)
    val sunColor = Color(0xFFF5D76E)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
    ) {
        val w = size.width
        val h = size.height
        val scale = w / 360f

        // sky gradient (soft)
        drawRect(color = paleSky, size = Size(w, h * 0.65f))

        // ground
        val ground = Path().apply {
            moveTo(0f, h * 0.6f)
            cubicTo(w * 0.25f, h * 0.55f, w * 0.5f, h * 0.65f, w * 0.75f, h * 0.58f)
            lineTo(w, h * 0.7f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(ground, color = Color(0xFF7DAA65))

        // sun
        drawCircle(
            color = sunColor.copy(alpha = 0.7f),
            radius = 18f * scale,
            center = Offset(w * 0.85f, h * 0.22f),
        )

        // trees
        val trees = listOf(
            Pair(0.12f, 0.75f),
            Pair(0.25f, 0.60f),
            Pair(0.38f, 0.72f),
            Pair(0.50f, 0.55f),
            Pair(0.62f, 0.68f),
            Pair(0.75f, 0.57f),
            Pair(0.88f, 0.70f),
        )

        for ((xRel, treeScale) in trees) {
            val tx = w * xRel
            val ts = treeScale
            val baseY = h * 0.65f

            // trunk
            drawRect(
                color = treeBrown,
                topLeft = Offset(tx - 3f * ts, baseY - 30f * ts),
                size = Size(6f * ts, 30f * ts),
            )

            // foliage
            val foliage = Path().apply {
                moveTo(tx, baseY - 55f * ts)
                lineTo(tx + 18f * ts, baseY - 10f * ts)
                lineTo(tx - 18f * ts, baseY - 10f * ts)
                close()
            }
            drawPath(foliage, color = forestGreen)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F9F4)
@Composable
private fun SanctuaryHeaderPreview() {
    WildHavenTheme {
        SanctuaryHeader()
    }
}
