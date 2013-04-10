/*
 * Copyright (C) 2012 Slimroms
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

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;

public class NotificationDrawerSettings extends SettingsPreferenceFragment
            implements OnPreferenceChangeListener  {

    private static final String KEY_NOTIFICATION_BEHAVIOUR = "notifications_behaviour";

    ListPreference mNotificationsBehavior;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.power_widget_settings);

        PreferenceScreen prefs = getPreferenceScreen();

        int CurrentBehavior = Settings.System.getInt(getContentResolver(), Settings.System.NOTIFICATIONS_BEHAVIOUR, 0);
        mNotificationsBehavior = (ListPreference) findPreference(KEY_NOTIFICATION_BEHAVIOUR);
        mNotificationsBehavior.setValue(String.valueOf(CurrentBehavior));
        mNotificationsBehavior.setSummary(mNotificationsBehavior.getEntry());
        mNotificationsBehavior.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNotificationsBehavior) {
            String val = (String) newValue;
                     Settings.System.putInt(getContentResolver(), Settings.System.NOTIFICATIONS_BEHAVIOUR,
            Integer.valueOf(val));
            int index = mNotificationsBehavior.findIndexOfValue(val);
            mNotificationsBehavior.setSummary(mNotificationsBehavior.getEntries()[index]);
            return true;
        }
        return false;
    }

}
