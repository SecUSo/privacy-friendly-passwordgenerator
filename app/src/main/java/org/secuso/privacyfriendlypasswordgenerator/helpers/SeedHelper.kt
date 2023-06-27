/**
 * This file is part of Privacy Friendly Password Generator.
 * <p>
 * Privacy Friendly Password Generator is free software:
 * you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or any later version.
 * <p>
 * Privacy Friendly Password Generator is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Privacy Friendly Password Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlypasswordgenerator.helpers

import android.content.Context
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log

class SeedHelper {
    fun getSeed(context: Context): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        val bindToDeviceEnabled = sharedPreferences.getBoolean("bindToDevice_enabled", false)

        return if (bindToDeviceEnabled) {
            val id = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID
            )
            Log.d(
                "DEVICE ID", id
            )
            return id
        } else {
            "SECUSO"
        }
    }
}