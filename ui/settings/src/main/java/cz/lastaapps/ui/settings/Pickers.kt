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

package cz.lastaapps.ui.settings

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.material.timepicker.MaterialTimePicker
import cz.lastaapps.ui.common.extencions.LocalActivity
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


/**
 * Opens Material time picker, shows time selected (+ timezone name)
 * */
@Composable
fun TimeSettings(
    text: String,
    time: LocalTime,
    modifier: Modifier = Modifier,
    onTimeChanged: (LocalTime) -> Unit,
    timezone: String = "",
) {

    var timeText = time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    if (timezone != "")
        timeText += " $timezone"


    val activity = LocalActivity.getCompat()!!

    //TODO migrate to a compose solution when available
    val onClick: () -> Unit = {
        val picker = MaterialTimePicker.Builder()
            .setHour(time.hour)
            .setMinute(time.minute)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTitleText(text)
            .build()

        activity.let {
            picker.show(it.supportFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                val newTime = LocalTime.of(picker.hour, picker.minute)
                onTimeChanged(newTime)
            }
        }
    }

    CustomSettings(
        text = text,
        onClick = onClick,
        modifier = modifier,
        divider = false,
    ) {
        Text(timeText)
    }
}
