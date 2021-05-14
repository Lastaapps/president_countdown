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

package cz.lastaapps.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.android.play.core.review.ReviewManagerFactory

object PlayStoreReview {

    private val TAG get() = PlayStoreReview::class.simpleName

    fun doInAppReview(activity: Activity) {
        val manager = ReviewManagerFactory.create(activity)

        //redirects to the play store, required under LOLLIPOP 5.0 and when Google play API fails
        val oldRequest: () -> Unit by lazy {
            {
                Log.i(TAG, "Launching old Play Store review link")

                val url = "https://play.google.com/store/apps/details?id=${activity.packageName}"
                val uri = Uri.parse(url)
                activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }

        //version check
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            oldRequest()
            return
        }*/

        //Google play in app review
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { result ->
            if (result.isSuccessful) {

                Log.i(TAG, "A review dialog shown")

                val reviewInfo = result.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener {

                    Log.i(TAG, "A review dialog closed")

                    Toast.makeText(
                        activity,
                        R.string.thanks_review,
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                oldRequest()
            }
        }
    }
}