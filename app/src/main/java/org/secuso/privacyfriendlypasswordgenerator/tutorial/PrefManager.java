/**
 * This file is part of Privacy Friendly Password Generator.

 Privacy Friendly Password Generator is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly Password Generator is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Password Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlypasswordgenerator.tutorial;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class structure taken from tutorial at http://www.androidhive.info/2016/05/android-build-intro-slider-app/
 * @author Karola Marky
 * @version 20170310
 */

public class PrefManager {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    // shared pref mode
    private final int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "pfa-pw-generator";
    public static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public static final String IS_TUTORIAL_LAUNCH = "IsTutorialLaunch";
    public static final String IS_FIRST_TIME_GEN = "IsFirstTimeGen";

    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setTutorialLaunch(boolean isTutorial) {
        editor.putBoolean(IS_TUTORIAL_LAUNCH, isTutorial);
        editor.commit();
    }

    public boolean isTutorialLaunch() {
        return pref.getBoolean(IS_TUTORIAL_LAUNCH, true);
    }

    public void setFirstTimeGen(boolean isTutorial) {
        editor.putBoolean(IS_FIRST_TIME_GEN, isTutorial);
        editor.commit();
    }

    public boolean isFirstTimeGen() {
        return pref.getBoolean(IS_FIRST_TIME_GEN, true);
    }


    public SharedPreferences getPref() {
        return pref;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}
