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

package cz.lastaapps.president.widget.widget

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cz.lastaapps.president.widget.WidgetConfig
import cz.lastaapps.president.widget.service.WidgetUpdateService

/**
 * Trampoline activity - started to start another activity
 * Restarts WidgetUpdateService and starts the activity from WidgetConfig
 * */
internal class WidgetClickedActivity : AppCompatActivity() {

    companion object {
        private val TAG = WidgetClickedActivity::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "Widget click received")

        //restarts widget service
        WidgetUpdateService.stopService(this)
        WidgetUpdateService.startService(this)


        //opens the app
        WidgetConfig.mainActivity?.let { activity ->
            startActivity(
                Intent(this, activity.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            )
        }

        finish()
    }
}