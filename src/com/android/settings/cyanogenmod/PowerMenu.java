/*
 * Copyright (C) 2012 CyanogenMod
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.cyanogenmod;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class PowerMenu extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {
    private static final String TAG = "PowerMenu";

    private static final String KEY_SCREENRECORD = "power_menu_screenrecord";
    private static final String KEY_PROFILES = "power_menu_profiles";
    private static final String KEY_USER = "power_menu_user";

    private CheckBoxPreference mScreenrecordPref;
    private CheckBoxPreference mProfilesPref;
    private CheckBoxPreference mUserPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.power_menu_settings);

        mScreenrecordPref = (CheckBoxPreference) findPreference(KEY_SCREENRECORD);
        mScreenrecordPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_SCREENRECORD_ENABLED, 0) == 1));

        mProfilesPref = (CheckBoxPreference) findPreference(KEY_PROFILES);
        mProfilesPref.setChecked((Settings.System.getInt(getContentResolver(),
                Settings.System.POWER_MENU_PROFILES_ENABLED, 1) == 1));

        mUserPref = (CheckBoxPreference) findPreference(KEY_USER);
        if (!UserHandle.MU_ENABLED
            || !UserManager.supportsMultipleUsers()) {
            getPreferenceScreen().removePreference(mUserPref);
        } else {
            mUserPref.setChecked((Settings.System.getInt(getContentResolver(),
                    Settings.System.POWER_MENU_USER_ENABLED, 0) == 1));
        }

    }

//    public boolean onPreferenceChange(Preference preference, Object newValue) {
//    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mScreenrecordPref) {
            value = mScreenrecordPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_SCREENRECORD_ENABLED,
                    value ? 1 : 0);
        } else if (preference == mProfilesPref) {
            value = mProfilesPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_PROFILES_ENABLED,
                    value ? 1 : 0);
       } else if (preference == mUserPref) {
            value = mUserPref.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.POWER_MENU_USER_ENABLED,
                    value ? 1 : 0);
        } else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        return true;
    }
}
