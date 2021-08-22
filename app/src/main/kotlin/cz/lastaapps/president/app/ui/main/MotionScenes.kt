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

package cz.lastaapps.president.app.ui.main

import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.MotionScene

object MotionScenes {

    object Tags {
        const val clock = "clock"
        const val message = "message"
        const val switches = "switches"
        const val options = "options"
    }

    @Composable
    fun getMainScene() = MotionScene(
        """
{
  ConstrainSets: {
    expanded: {
      ${Tags.clock}: {
        top: ["parent", "top", 24],
        centerHorizontally: true
      },
      ${Tags.message}: {
        
      }
    },
    closed: {
    
    }
  },
  Transitions: {
    default: {
      from: "closed",
      to: "expanded",
      pathMotionArc: "startHorizontal",
      KeyAttributes: [
        {
          target: [${Tags.message}],
          frames: [0, 100],
          alpha: [1.0, 0.0]
        },
        {
          target: [${Tags.switches}, ${Tags.options}],
          frames: [0, 100],
          alpha: [0.0, 1.0]
        }
      ]
    }
  }
}
"""
    )
}

