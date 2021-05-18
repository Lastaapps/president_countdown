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

package cz.lastaapps.president.widget.small

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cz.lastaapps.president.widget.core.config.*
import cz.lastaapps.ui.common.extencions.viewModelKt

class WidgetConfigActivityImpl : WidgetConfigActivity() {

    @Composable
    override fun getViewModel(): WidgetViewModel {
        return viewModelKt(WidgetViewModelImpl::class)
    }

    @Composable
    override fun OptionsComponents(
        state: WidgetState,
        onStateChanged: (WidgetState) -> Unit,
        isLightPreview: Boolean,
        onLightPreviewChanged: (Boolean) -> Unit,
    ) {
        UIModeSelection(
            state = state,
            onStateChanged = onStateChanged,
        )
        ThemedOptions(
            state = state,
            onStateChanged = onStateChanged,
            isLightPreview = isLightPreview,
            onLightPreviewChanged = onLightPreviewChanged,
        )
        EnableFrameSwitch(
            state = state,
            onStateChanged = onStateChanged,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}