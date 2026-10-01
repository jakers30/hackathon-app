package com.raite.studyroom.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.composed

/**
 * Shimmering placeholder (spec section 5.6). Colors come from the theme so it
 * looks right in light and dark mode. Static placeholders when animation is off
 * are handled by the system animation-scale setting automatically.
 */
fun Modifier.shimmer(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val x by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "x",
    )
    val base = MaterialTheme.colorScheme.surfaceVariant
    val highlight = MaterialTheme.colorScheme.surface
    background(
        Brush.linearGradient(
            colors = listOf(base, highlight, base),
            start = Offset(x - 300f, 0f),
            end = Offset(x, 0f),
        )
    )
}

@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
) {
    androidx.compose.foundation.layout.Box(modifier.clip(shape).shimmer())
}

/** A column of shimmering rows, shaped like list content. */
@Composable
fun SkeletonRows(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    height: Int = 64,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(rows) {
            SkeletonBox(
                Modifier
                    .fillMaxWidth()
                    .height(height.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}
