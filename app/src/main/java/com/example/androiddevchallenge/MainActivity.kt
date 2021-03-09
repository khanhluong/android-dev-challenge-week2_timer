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
package com.example.androiddevchallenge

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.TimerCircular
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.viewmodel.TimerViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                val timerViewModel: TimerViewModel = viewModel()
                val timeValue by timerViewModel.maxTime.observeAsState(timerViewModel.max)
                val isPaused by timerViewModel.pausedTime.observeAsState(true)
                val minutes = (timeValue / 60)
                val seconds = timeValue % 60
                CountdownScreen(timeValue, minutes, seconds, timerViewModel.max, isPaused) {
                    when (it) {
                        TimerState.Play -> {
                            timerViewModel.pausedTime.value = false
                            timerViewModel.timer.start()
                        }
                        TimerState.Pause -> {
                            timerViewModel.pausedTime.value = true
                            timerViewModel.timer.cancel()
                        }
                        TimerState.Stop -> {
                            timerViewModel.timer.cancel()
                            timerViewModel.timer.onFinish()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CountdownScreen(
    time: Int,
    minutes: Int,
    seconds: Int,
    max: Int,
    paused: Boolean,
    onAction: (TimerState) -> Unit
) {
    ConstraintLayout(Modifier.fillMaxSize()) {
        Log.d("minutes", "minutes $minutes")
        // it error when set minutes is int value...
        val strMinutes = minutes.toString()
        val (progress, hours, minutes, btnStart, btnStop) = createRefs()
        TimerCircular(
            Modifier
                .width(300.dp)
                .height(300.dp)
                .constrainAs(progress) { centerTo(parent) },
            progress = time.toFloat() * 100 / max,
            color = MaterialTheme.colors.primary,
            animationSpec = tween(
                durationMillis = if (time == max) 1000 else 100,
                easing = LinearEasing
            )
        )

        Row(
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(hours) { centerTo(progress) },
        ) {
            Text(
                text = strMinutes,
                style = MaterialTheme.typography.body2,
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = " : ",
                style = MaterialTheme.typography.body2,
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "$seconds",
                style = MaterialTheme.typography.body2,
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Button(
            onClick = { onAction.invoke(if (paused) TimerState.Play else TimerState.Pause) },
            modifier = Modifier
                .size(50.dp)
                .constrainAs(btnStart) {
                    bottom.linkTo(parent.bottom, 10.dp)
                    start.linkTo(parent.start, 10.dp)
                }
        ) {
            Image(
                painterResource(if (paused) R.drawable.ic_baseline_play_arrow_24 else R.drawable.ic_baseline_pause_24),
                stringResource(if (paused) R.string.btn_play else R.string.btn_pause)
            )
        }
        Button(
            onClick = { onAction.invoke(TimerState.Stop) },
            modifier = Modifier
                .size(50.dp)
                .constrainAs(btnStop) {
                    bottom.linkTo(parent.bottom, 10.dp)
                    end.linkTo(parent.end, 4.dp)
                }
        ) {
            Image(
                painterResource(R.drawable.ic_baseline_stop_24),
                stringResource(R.string.btn_stop)
            )
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        Text(text = "Ready... Set... GO!")
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}

sealed class TimerState {
    object Play : TimerState()
    object Pause : TimerState()
    object Stop : TimerState()
}
