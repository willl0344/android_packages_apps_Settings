/*
* Copyright (C) 2012 Slimroms
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.settings.cyanogenmod;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;
import com.android.settings.Utils;


public class NavBarSettings extends SettingsPreferenceFragment {

    private static final String TAG = "NavBar";
    private static final String ENABLE_NAVIGATION_BAR = "enable_nav_bar";
    private static final String PREF_STYLE_DIMEN = "navbar_style_dimen_settings";
    private static final String ENABLE_NAVBAR_OPTIONS = "enable_navbar_option";
    private static final String KEY_MENU_ENABLED = "key_menu_enabled";
    private static final String KEY_BACK_ENABLED = "key_back_enabled";
    private static final String KEY_HOME_ENABLED = "key_home_enabled";

    private boolean mHasNavBarByDefault;

    CheckBoxPreference mEnableNavigationBar;
    PreferenceScreen mStyleDimenPreference;
    private CheckBoxPreference mMenuKeyEnabled;
    private CheckBoxPreference mBackKeyEnabled;
    private CheckBoxPreference mHomeKeyEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.navbar_settings);

        PreferenceScreen prefs = getPreferenceScreen();

	mStyleDimenPreference = (PreferenceScreen) findPreference(PREF_STYLE_DIMEN);

        mMenuKeyEnabled = (CheckBoxPreference) findPreference(KEY_MENU_ENABLED);
        mBackKeyEnabled = (CheckBoxPreference) findPreference(KEY_BACK_ENABLED);
        mHomeKeyEnabled = (CheckBoxPreference) findPreference(KEY_HOME_ENABLED);

        mHasNavBarByDefault = mContext.getResources().getBoolean(
                com.android.internal.R.bool.config_showNavigationBar);
        boolean enableNavigationBar = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVIGATION_BAR_SHOW, mHasNavBarByDefault ? 1 : 0) == 1;
        mEnableNavigationBar = (CheckBoxPreference) findPreference(ENABLE_NAVIGATION_BAR);
        mEnableNavigationBar.setChecked(enableNavigationBar);

        if (mEnableNavigationBar.isChecked()) {
            enableKeysPrefs();
        } else {
            resetKeys();
        }

        // don't allow devices that must use a navigation bar to disable it
        //if (mHasNavBarByDefault) {
        //    prefs.removePreference(mEnableNavigationBar);
        //}
	updateNavbarPreferences(enableNavigationBar);
    }

    public void enableKeysPrefs() {
        mMenuKeyEnabled.setEnabled(true);
        mBackKeyEnabled.setEnabled(true);
        mHomeKeyEnabled.setEnabled(true);
        mMenuKeyEnabled.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.KEY_MENU_ENABLED, 1) == 1));
        mBackKeyEnabled.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.KEY_BACK_ENABLED, 1) == 1));
        mHomeKeyEnabled.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.KEY_HOME_ENABLED, 1) == 1));
    }

    public void resetKeys() {
        mMenuKeyEnabled.setEnabled(false);
        mBackKeyEnabled.setEnabled(false);
        mHomeKeyEnabled.setEnabled(false);
        mMenuKeyEnabled.setChecked(true);
        mBackKeyEnabled.setChecked(true);
        mHomeKeyEnabled.setChecked(true);
        Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(), Settings.System.KEY_MENU_ENABLED, 1);
        Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(), Settings.System.KEY_BACK_ENABLED, 1);
        Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(), Settings.System.KEY_HOME_ENABLED, 1);
    }

    private void updateNavbarPreferences(boolean show) {
        if (mHasNavBarByDefault) {
            Settings.System.putInt(getContentResolver(),
                    Settings.System.UI_FORCE_OVERFLOW_BUTTON,
                    show ? 0 : 1);
        }
        mStyleDimenPreference.setEnabled(show);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        boolean value;

        if (preference == mEnableNavigationBar) {
            value = mEnableNavigationBar.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.NAVIGATION_BAR_SHOW, value ? 1 : 0);
            if (value) {
                enableKeysPrefs();
            } else {
                resetKeys();
            }
            return true;
        } else if (preference == mMenuKeyEnabled) {
            value = mMenuKeyEnabled.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.KEY_MENU_ENABLED, value ? 1 : 0);
            return true;
        } else if (preference == mBackKeyEnabled) {
            value = mBackKeyEnabled.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.KEY_BACK_ENABLED, value ? 1 : 0);
            return true;
        } else if (preference == mHomeKeyEnabled) {
            value = mHomeKeyEnabled.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.KEY_HOME_ENABLED, value ? 1 : 0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
