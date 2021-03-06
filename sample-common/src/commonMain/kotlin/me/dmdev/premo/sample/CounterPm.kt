/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020-2021 Dmitriy Gorbunov (dmitriy.goto@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.dmdev.premo.sample

import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import me.dmdev.premo.Action
import me.dmdev.premo.Command
import me.dmdev.premo.PresentationModel
import me.dmdev.premo.State

class CounterPm(
    private val maxCount: Int = 10
) : PresentationModel() {

    val count = State(0)

    val messages = Command<String>()

    val plusButtonEnabled = State(false) {
        count.flow().map { it < maxCount }
    }

    val minusButtonEnabled = State(false) {
        count.flow().map { it > 0 }
    }

    val plus = Action<Unit> {
        this.map { count.value + 1 }
            .filter { it <= maxCount }
            .onEach {
                if (it == maxCount) {
                    messages.emit("Max value reached")
                }
            }
            .consumeBy(count)
    }

    val minus = Action<Unit> {
        this.map { count.value - 1 }
            .filter { it >= 0 }
            .onEach {
                if (it == 0) {
                    messages.emit("Min value reached")
                }
            }
            .consumeBy(count)
    }
}