/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun TimerCircular(
    modifier: Modifier = Modifier,
    color: Color,
    progress: Float,
    animate: Boolean = true,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
) {
    val animatedProgress: Float by animateFloatAsState(targetValue = progress, animationSpec = animationSpec)
    Canvas(modifier) {
        val startingAngle = 180f
        val sweepAngle = (360 * if (animate) animatedProgress else progress) / 100
        // size of radius
        val waveRadius = size.minDimension * 0.15f
        val dotRadius = waveRadius / 4f

        drawArc(
            color = color,
            startingAngle, sweepAngle, true, Offset(0f, 2f), size, 0.2f, Stroke(dotRadius)
        )
    }
}
