package com.example.habittracker.habit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBar(
    size: Dp = 100.dp,
    strokeWidth: Dp = 10.dp,
    backgroundArcColor: Color = Color.DarkGray,
    backgroundArcColor2: Color = Color.White,
    angle: Float,
) {

    Canvas(modifier = Modifier.size(size)) {
        drawArc(
            color = backgroundArcColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            size = Size(size.toPx(), size.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )
        drawArc(
            color = backgroundArcColor2,
            startAngle = 270f,
            sweepAngle = angle,
            useCenter = false,
            size = Size(size.toPx(), size.toPx()),
            style = Stroke(width = strokeWidth.toPx())
        )
    }
}