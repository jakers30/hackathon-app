package com.raite.studyroom.ui.screens.intro

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Intro animation (spec section 5.5):
 * fade in 400ms -> hold 600ms -> fade out slowly 1800ms -> cross-fade 300ms.
 * The session check runs in parallel while this plays.
 */
@Composable
fun IntroScreen(onFinished: () -> Unit) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, tween(400))
        delay(600)
        alpha.animateTo(0f, tween(1800, easing = LinearOutSlowInEasing))
        delay(300)
        onFinished()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Rounded.AutoStories,
            contentDescription = "App logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(140.dp)
                .graphicsLayer { this.alpha = alpha.value },
        )
    }
}
