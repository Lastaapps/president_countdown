/*
 *   Copyright 2021, Petr Laštovička as Lasta apps, All rights reserved
 *
 *     This file is part of President Countdown.
 *
 *     This app is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This app is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this app.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package cz.lastaapps.ui.common.extencions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable


@Composable
fun <T : Any> rememberMutableSaveable(
    vararg inputs: Any?,
    saver: Saver<T, Any> = typedAutoSaver(),
    key: String? = null,
    init: () -> MutableState<T>
): MutableState<T> = rememberSaveable(
    inputs = inputs,
    saver = remember(saver) { mutableSaverConvertor(saver) },
    key = key,
    init = init,
)

private fun <T : Any, O : Any> mutableSaverConvertor(saver: Saver<T, O>) =
    Saver<MutableState<T>, O>(
        { with(saver) { this@Saver.save(it.value) } },
        { mutableStateOf(saver.restore(it) as T) }
    )

@Suppress("UNCHECKED_CAST")
fun <T : Any, O : Any> typedAutoSaver() = AutoSaver as Saver<T, O>

private val AutoSaver = Saver<Any, Any>(
    save = { it },
    restore = { it }
)