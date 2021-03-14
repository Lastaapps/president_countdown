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

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.android.material.timepicker.MaterialTimePicker
import cz.lastaapps.ui.common.components.VerticalDivider
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/*
* Settings components implementation
* */

/**
 * Text and switch
 * */
@Composable
fun SwitchSettings(
    text: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onChange: () -> Unit,
) {
    CustomSettings(text = text, onClick = onChange, modifier = modifier) {
        Switch(checked = checked, onCheckedChange = { onChange() })
    }
}

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

    //TODO migrate to a compose solution when available
    val context = LocalContext.current
    val onClick: () -> Unit = {
        val picker = MaterialTimePicker.Builder()
            .setHour(time.hour)
            .setMinute(time.minute)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTitleText(text)
            .build()

        (context as AppCompatActivity).let {
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

@Composable
fun <T> DropdownSettings(
    text: String,
    items: List<T>,
    selected: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemsText: @Composable (T) -> String = { it.toString() }
) {
    CustomSettings(
        text = text,
        modifier = modifier,
        divider = false,
    ) {

        var expanded by remember { mutableStateOf(false) }

        val onClick = { expanded = true }

        Row(
            modifier = Modifier.clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = itemsText(selected),
            )
            IconButton(
                onClick = onClick,
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp
                    else Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {

            for (item in items) {

                DropdownMenuItem(
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                    modifier = modifier
                ) {
                    Text(itemsText(item))
                }
            }
        }
    }
}

/**
 * Common alignment for settings components
 * */
@Composable
fun CustomSettings(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    divider: Boolean = true,
    content: @Composable () -> Unit,
) {
    ConstraintLayout(
        modifier = modifier.clickable(onClick = onClick)
    ) {

        val padding = 8.dp

        val (textConst, dividerConst, contentConst) = createRefs()

        Text(
            text = text,
            modifier = Modifier.constrainAs(textConst) {
                start.linkTo(parent.start)
                centerVerticallyTo(parent)
                end.linkTo(dividerConst.start, padding)
                width = Dimension.fillToConstraints
            }
        )

        Box(
            modifier = Modifier.constrainAs(dividerConst) {
                end.linkTo(contentConst.start, padding)
                centerVerticallyTo(parent)
                height = Dimension.fillToConstraints
            },
        ) {
            if (divider) {
                VerticalDivider()
            }
        }


        Box(
            modifier = Modifier.constrainAs(contentConst) {
                end.linkTo(parent.end)
                centerVerticallyTo(parent)

                width = Dimension.wrapContent
            },
        ) {
            content()
        }
    }
}

/**
 * Background for settings components
 * */
@Composable
fun SettingsGroup(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = backgroundColor,
        border = border,
    ) {
        Box(Modifier.padding(8.dp)) {
            content()
        }
    }
}

/**
 * Background for settings components
 * */
@Composable
fun SettingsGroupColumn(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    SettingsGroup(modifier, shape, backgroundColor, border) {
        Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            content()
        }
    }
}