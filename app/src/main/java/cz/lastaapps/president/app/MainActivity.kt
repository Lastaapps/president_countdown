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

package cz.lastaapps.president.app

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import cz.lastaapps.president.app.ui.main.MainActivityRoot
import cz.lastaapps.president.firebase.MyAnalytics
import cz.lastaapps.president.widget.WidgetConfig
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.simpleName
    }

    private lateinit var analytics: MyAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "Creating the activity")

        //removes the splashscreen
        setTheme(cz.lastaapps.president.assets.R.style.Theme_President)

        //firebase analytics
        analytics = MyAnalytics(this)

        //system preferred theme switching
        AppCompatDelegate.setDefaultNightMode(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        )

        //restarts widgets if necessary
        lifecycleScope.launchWhenResumed {
            delay(3000)
            WidgetConfig.updateService(this@MainActivity)
        }

        //set the Main UI
        setContent {
            MainActivityRoot()
        }
    }
}