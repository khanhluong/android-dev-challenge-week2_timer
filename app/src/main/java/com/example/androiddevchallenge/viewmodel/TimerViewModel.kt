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
package com.example.androiddevchallenge.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    val timerTick = 1000
    // set max time.
    val max = 90
    val maxTime = MutableLiveData(max)
    val pausedTime = MutableLiveData(true)

    val timer = object : CountDownTimer(max * 1000L, timerTick.toLong()) {
        override fun onTick(millisUntilFinished: Long) {
            val currentTime = maxTime.value ?: 0
            if (currentTime <= 0) {
                onFinish()
            } else {
                maxTime.value = currentTime - 1
            }
        }

        override fun onFinish() {
            pausedTime.value = true
            maxTime.value = max
        }
    }
}
