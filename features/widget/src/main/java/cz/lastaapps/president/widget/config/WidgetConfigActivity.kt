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

package cz.lastaapps.president.widget.config

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import cz.lastaapps.president.widget.R
import cz.lastaapps.president.widget.service.WidgetUpdateService
import cz.lastaapps.ui.common.themes.MainTheme

internal class WidgetConfigActivity : AppCompatActivity() {

    companion object {
        private val TAG get() = WidgetConfigActivity::class.simpleName

        const val REQUEST_CODE = 53800
    }

    private var widgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "Creating activity")

        //sets transparent background
        setTheme(R.style.Theme_President_Transparent)

        WidgetUpdateService.startService(applicationContext)

        //gets data inputted
        widgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID,
        )
        //no data entered
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Toast.makeText(this, R.string.config_invalid_input, Toast.LENGTH_LONG).show()
            failed()
            return
        }

        //Compose UI
        setContent {
            MainTheme {
                WidgetConfigRoot(
                    widgetId = widgetId,
                    onCancel = { failed() },
                    onSuccess = { updateAndClose() },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    /**
     * No data inputted or the cancel button pressed
     * */
    private fun failed() {
        setResult(Activity.RESULT_CANCELED)
        finishAndRemoveTask()
    }

    /**
     * OK pressed - all the data are already processed and saved into a database
     * */
    private fun updateAndClose() {
        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        }
        setResult(Activity.RESULT_OK, resultValue)
        finishAndRemoveTask()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            WidgetUpdateService.startService(applicationContext)
            finishAndRemoveTask()
        }
    }
}