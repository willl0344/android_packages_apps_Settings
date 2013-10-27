package com.android.settings.cyanogenmod;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface; 
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.widget.SeekBarPreference;

/**
 * Using the original CyanogenMod Color Picker instead of margaritov used 
 * in Slim roms
 */
import com.android.settings.cyanogenmod.colorpicker.ColorPickerPreference;

public class StatusBarTrafficSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String TAG = "NetworkSpeedColor";
    private static final String PREF_STATUS_BAR_TRAFFIC_ENABLE = "status_bar_traffic_enable";
    private static final String PREF_STATUS_BAR_TRAFFIC_HIDE = "status_bar_traffic_hide";
    private static final String STATUS_BAR_TRAFFIC_SUMMARY = "status_bar_traffic_summary";
    private static final String PREF_NETWORK_SPEED_COLOR = "network_speed_color";

    private CheckBoxPreference mStatusBarTrafficEnable;
    private CheckBoxPreference mStatusBarTrafficHide;
    private ListPreference mStatusBarTrafficSummary;
    private ColorPickerPreference mStatusBarTrafficColor;
    
    private static final int MENU_RESET = Menu.FIRST;

    static final int DEFAULT_TRAFFIC_METER_COLOR = 0xff33b5e5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshSettings();
    }

    public void refreshSettings() {
        PreferenceScreen prefSet = getPreferenceScreen();
        if (prefSet != null) {
            prefSet.removeAll();
        }

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.status_bar_traffic_settings);

        prefSet = getPreferenceScreen();

	mStatusBarTrafficEnable = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_TRAFFIC_ENABLE);
        mStatusBarTrafficEnable.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.STATUS_BAR_TRAFFIC_ENABLE, 0) == 1));

        mStatusBarTrafficHide = (CheckBoxPreference) prefSet.findPreference(PREF_STATUS_BAR_TRAFFIC_HIDE);
        mStatusBarTrafficHide.setChecked((Settings.System.getInt(getActivity().getApplicationContext().getContentResolver(),
                Settings.System.STATUS_BAR_TRAFFIC_HIDE, 1) == 1));  

	int StatusBarTrafficSummary = Settings.System.getInt(getContentResolver(), Settings.System.STATUS_BAR_TRAFFIC_SUMMARY, 3);
	mStatusBarTrafficSummary = (ListPreference) findPreference(STATUS_BAR_TRAFFIC_SUMMARY);
        mStatusBarTrafficSummary.setValue(String.valueOf(StatusBarTrafficSummary));
	mStatusBarTrafficSummary.setSummary(mStatusBarTrafficSummary.getEntry());
	mStatusBarTrafficSummary.setOnPreferenceChangeListener(this);

        mStatusBarTrafficColor = (ColorPickerPreference) findPreference(PREF_NETWORK_SPEED_COLOR);
        mStatusBarTrafficColor.setOnPreferenceChangeListener(this);
        int intColor = Settings.System.getInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_TRAFFIC_COLOR, 0xff000000);
        String hexColor = String.format("#%08x", (0xffffffff & intColor));
        mStatusBarTrafficColor.setNewPreviewColor(intColor);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.network_speed_color_reset)
                .setIcon(R.drawable.ic_settings_backup) // use the backup icon
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                resetToDefault();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void resetToDefault() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.network_speed_color_reset);
        alertDialog.setMessage(R.string.network_speed_reset_message);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                trafficMeterColorReset();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, null);
        alertDialog.create().show();
    } 

    private void trafficMeterColorReset() {
        Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.STATUS_BAR_TRAFFIC_COLOR, DEFAULT_TRAFFIC_METER_COLOR);
        
        mStatusBarTrafficColor.setNewPreviewColor(DEFAULT_TRAFFIC_METER_COLOR);
        String hexColor = String.format("#%08x", (0xffffffff & DEFAULT_TRAFFIC_METER_COLOR));
        mStatusBarTrafficColor.setSummary(hexColor);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
	boolean value;
	if (preference == mStatusBarTrafficEnable) {
            value = mStatusBarTrafficEnable.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_TRAFFIC_ENABLE, value ? 1 : 0);
            return true;
        } else if (preference == mStatusBarTrafficHide) {
            value = mStatusBarTrafficHide.isChecked();
            Settings.System.putInt(getActivity().getApplicationContext().getContentResolver(),
                    Settings.System.STATUS_BAR_TRAFFIC_HIDE, value ? 1 : 0);
            return true;
	}
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
	if (preference == mStatusBarTrafficSummary) {
            int val = Integer.valueOf((String) newValue);
            int index = mStatusBarTrafficSummary.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(), 
		    Settings.System.STATUS_BAR_TRAFFIC_SUMMARY, val);
            mStatusBarTrafficSummary.setSummary(mStatusBarTrafficSummary.getEntries()[index]);
            return true;        
	} else if (preference == mStatusBarTrafficColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_TRAFFIC_COLOR, intHex);
            return true;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
