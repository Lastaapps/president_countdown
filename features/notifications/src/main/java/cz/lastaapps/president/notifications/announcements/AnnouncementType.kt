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

package cz.lastaapps.president.notifications.announcements

import android.content.Intent
import androidx.annotation.IntDef

/**
 * Type of announcement event
 * */
@IntDef(
    flag = true,
    value = [AnnouncementType.BIRTHDAY, AnnouncementType.NAMEDAY, AnnouncementType.ELECTED, AnnouncementType.REMAINS]
)
@Retention(AnnotationRetention.SOURCE)
internal annotation class AnnouncementType {
    companion object {

        private const val intentKey = "ANNOUNCEMENT_TYPE"

        //available even types + used as PendingIntent's requestCode
        const val BIRTHDAY = 46060
        const val NAMEDAY = 46061
        const val ELECTED = 46062
        const val REMAINS = 46063

        //extracts data from intent
        fun Intent.getAnnouncedTypeExtra(): Int {
            return getIntExtra(intentKey, 0)
        }

        //saves data into intent
        fun Intent.putAnnouncedTypeExtra(@AnnouncementType type: Int): Intent = this.apply {
            putExtra(intentKey, type)
        }
    }
}
