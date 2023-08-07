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

class SeedHelper {

    companion object {
        private val DEFAULT_SEED_VALUE = "SECUSO"
    }

    /**
     * Helper class for managing and accessing the values stored in the encrypted preferences.
     */
    class EncryptedSeedPreference {
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

        fun containsSeedValue(context: Context): Boolean {
            return initPreference(context).contains(PreferenceKeys.SEED_VALUE)
        }

        fun getSeedValue(context: Context): String? {
            return initPreference(context).getString(PreferenceKeys.SEED_VALUE, DEFAULT_SEED_VALUE)
        }

        fun setSeedValue(context: Context, newValue: String) {
            initPreference(context).edit().putString(PreferenceKeys.SEED_VALUE, newValue).commit()
        }
    }

    fun getSeed(context: Context): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
        val bindToDeviceEnabled = sharedPreferences.getBoolean(PreferenceKeys.BIND_TO_DEVICE_ENABLED, false)
        val seedPreference = EncryptedSeedPreference()

        return if (bindToDeviceEnabled) {
            if (!seedPreference.containsSeedValue(context)) {
                // Store the ANDROID_ID as the seed value
                seedPreference.setSeedValue(context, Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
            }
            seedPreference.getSeedValue(context) ?: DEFAULT_SEED_VALUE
        } else {
            DEFAULT_SEED_VALUE
        }
    }

    /**
     * Generates a random String and saves it in the {@see [EncryptedSeedPreference]} if it is not present already.
     */
    fun initializeSeed(context: Context, isFirstTimeLaunch: Boolean) {
        val seedPreference = EncryptedSeedPreference()

        if (seedPreference.containsSeedValue(context)) {
            // Seed already present, nothing to do
        } else {
            if (!isFirstTimeLaunch) {
                // Not a new installation, so we store the ANDROID_ID as the seed value for backwards compatibility
                seedPreference.setSeedValue(context, Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
            } else {
                seedPreference.setSeedValue(context, generateSeed())
            }
        }
    }

    private fun generateSeed(): String {
        val array = ByteArray(32)
        SecureRandom().nextBytes(array)
        return array.toHex()
    }

    private fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
}