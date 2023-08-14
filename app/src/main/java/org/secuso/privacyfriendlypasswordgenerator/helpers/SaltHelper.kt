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
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.provider.Settings
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom

class SaltHelper {

    companion object {
        private const val DEFAULT_SALT_VALUE = "SECUSO"
    }

    /**
     * Helper class for managing and accessing the values stored in the encrypted preferences.
     */
    class EncryptedSaltPreference {
        private val FILE_NAME = "preference_encrypted"

        fun initPreference(context: Context): SharedPreferences {
            val mainKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            return EncryptedSharedPreferences.create(
                context,
                FILE_NAME,
                mainKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        fun containsSaltValue(context: Context): Boolean {
            return initPreference(context).contains(PreferenceKeys.SALT_VALUE)
        }

        fun getSaltValue(context: Context): String? {
            return initPreference(context).getString(PreferenceKeys.SALT_VALUE, DEFAULT_SALT_VALUE)
        }

        fun setSaltValue(context: Context, newValue: String) {
            initPreference(context).edit().putString(PreferenceKeys.SALT_VALUE, newValue).commit()
        }
    }

    fun getSalt(context: Context): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        val bindToDeviceEnabled = sharedPreferences.getBoolean(PreferenceKeys.BIND_TO_DEVICE_ENABLED, false)
        val saltPreference = EncryptedSaltPreference()

        return if (bindToDeviceEnabled) {
            if (!saltPreference.containsSaltValue(context)) {
                // Store the ANDROID_ID as the salt value
                saltPreference.setSaltValue(context, Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
            }
            saltPreference.getSaltValue(context) ?: DEFAULT_SALT_VALUE
        } else {
            DEFAULT_SALT_VALUE
        }
    }

    /**
     * Generates a random String and saves it in the {@see [EncryptedSaltPreference]} if it is not present already.
     */
    fun initializeSalt(context: Context, isFirstTimeLaunch: Boolean) {
        val saltPreference = EncryptedSaltPreference()

        if (saltPreference.containsSaltValue(context)) {
            // Salt already present, nothing to do
        } else {
            if (!isFirstTimeLaunch) {
                // Not a new installation, so we store the ANDROID_ID as the salt value for backwards compatibility
                saltPreference.setSaltValue(context, Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
            } else {
                saltPreference.setSaltValue(context, generateSalt())
            }
        }
    }

    private fun generateSalt(): String {
        val array = ByteArray(32)
        SecureRandom().nextBytes(array)
        return array.toHex()
    }

    private fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
}