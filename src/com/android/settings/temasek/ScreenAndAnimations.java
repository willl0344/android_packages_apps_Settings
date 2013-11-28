package com.android.settings.temasek;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class ScreenAndAnimations extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_ANIMATION_OPTIONS = "category_animation_options";
    private static final String KEY_POWER_CRT_MODE = "system_power_crt_mode";
    private static final String KEY_LISTVIEW_ANIMATION = "listview_animation";
    private static final String KEY_LISTVIEW_INTERPOLATOR = "listview_interpolator";

    private ListPreference mCrtMode;
    private ListPreference mListViewAnimation;
    private ListPreference mListViewInterpolator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screen_and_animations);

	PreferenceScreen prefSet = getPreferenceScreen();

	// respect device default configuration
        // true fades while false animates
        boolean electronBeamFadesConfig = getResources().getBoolean(
                com.android.internal.R.bool.config_animateScreenLights);
        PreferenceCategory animationOptions =
            (PreferenceCategory) prefSet.findPreference(KEY_ANIMATION_OPTIONS);
        mCrtMode = (ListPreference) prefSet.findPreference(KEY_POWER_CRT_MODE);
        if (!electronBeamFadesConfig && mCrtMode != null) {
            int crtMode = Settings.System.getInt(getContentResolver(),
                    Settings.System.SYSTEM_POWER_CRT_MODE, 1);
            mCrtMode.setValue(String.valueOf(crtMode));
            mCrtMode.setSummary(mCrtMode.getEntry());
            mCrtMode.setOnPreferenceChangeListener(this);
        } else if (animationOptions != null) {
            prefSet.removePreference(animationOptions);
        }

        //ListView Animations
        mListViewAnimation = (ListPreference) prefSet.findPreference(KEY_LISTVIEW_ANIMATION);
        if (mListViewAnimation != null) {
           int listViewAnimation = Settings.System.getInt(getContentResolver(),
                    Settings.System.LISTVIEW_ANIMATION, 1);
           mListViewAnimation.setSummary(mListViewAnimation.getEntry());
           mListViewAnimation.setValue(String.valueOf(listViewAnimation));
        }
        mListViewAnimation.setOnPreferenceChangeListener(this);

        mListViewInterpolator = (ListPreference) prefSet.findPreference(KEY_LISTVIEW_INTERPOLATOR);
        if (mListViewInterpolator != null) {
           int listViewInterpolator = Settings.System.getInt(getContentResolver(),
                    Settings.System.LISTVIEW_INTERPOLATOR, 1);
           mListViewInterpolator.setSummary(mListViewInterpolator.getEntry());
           mListViewInterpolator.setValue(String.valueOf(listViewInterpolator));
        }
        mListViewInterpolator.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
	final String key = preference.getKey();

	if (KEY_POWER_CRT_MODE.equals(key)) {
            int value = Integer.parseInt((String) newValue);
            int index = mCrtMode.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.SYSTEM_POWER_CRT_MODE,
                    value);
            mCrtMode.setSummary(mCrtMode.getEntries()[index]);
        } else if (KEY_LISTVIEW_ANIMATION.equals(key)) {
            int value = Integer.parseInt((String) newValue);
            int index = mListViewAnimation.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LISTVIEW_ANIMATION,
                    value);
            mListViewAnimation.setSummary(mListViewAnimation.getEntries()[index]);
        } else if (KEY_LISTVIEW_INTERPOLATOR.equals(key)) {
            int value = Integer.parseInt((String) newValue);
            int index = mListViewInterpolator.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.LISTVIEW_INTERPOLATOR,
                    value);
            mListViewInterpolator.setSummary(mListViewInterpolator.getEntries()[index]);
        }
        return false;
    }
}
