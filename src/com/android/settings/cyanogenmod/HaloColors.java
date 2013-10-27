package com.android.settings.cyanogenmod;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.settings.cyanogenmod.colorpicker.ColorPickerPreference;

public class HaloColors extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String KEY_HALO_COLORS = "halo_colors"; 
    private static final String KEY_HALO_CIRCLE_COLOR = "halo_circle_color";
    private static final String KEY_HALO_EFFECT_COLOR = "halo_effect_color";
    private static final String KEY_HALO_NOTIF_TITLE_COLOR = "halo_notif_title_color";
    private static final String KEY_HALO_NOTIF_DESC_COLOR = "halo_notif_desc_color";
    private static final String KEY_HALO_SPEECH_BUBBLE_COLOR = "halo_speech_bubble_color";
    private static final String KEY_HALO_TEXT_COLOR = "halo_text_color";       

    private CheckBoxPreference mHaloColors; 
    private ColorPickerPreference mHaloCircleColor;
    private ColorPickerPreference mHaloEffectColor;
    private ColorPickerPreference mHaloNotifTitleColor;
    private ColorPickerPreference mHaloNotifDescColor;
    private ColorPickerPreference mHaloSpeechBubbleColor;
    private ColorPickerPreference mHaloTextColor;   
    
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);    

        addPreferencesFromResource(R.xml.halo_colors_settings);
        PreferenceScreen prefSet = getPreferenceScreen();
        mContext = getActivity();

	mHaloColors = (CheckBoxPreference) prefSet.findPreference(KEY_HALO_COLORS);
        mHaloColors.setChecked(Settings.System.getInt(mContext.getContentResolver(),
            Settings.System.HALO_COLOR, 0) == 1);

        mHaloCircleColor = (ColorPickerPreference) prefSet.findPreference(KEY_HALO_CIRCLE_COLOR);
        mHaloCircleColor.setOnPreferenceChangeListener(this);
        int color = Settings.System.getInt(mContext.getContentResolver(), 
        		Settings.System.HALO_CIRCLE_COLOR, 0xff33b5b3);
        String hex = ColorPickerPreference.convertToARGB(color);
    	mHaloCircleColor.setSummary(hex);
    	mHaloCircleColor.setEnabled(mHaloColors.isChecked());

	mHaloEffectColor = (ColorPickerPreference) prefSet.findPreference(KEY_HALO_EFFECT_COLOR);
        mHaloEffectColor.setOnPreferenceChangeListener(this);
        color = Settings.System.getInt(mContext.getContentResolver(),
                        Settings.System.HALO_EFFECT_COLOR, 0xff33b5b3);
        hex = ColorPickerPreference.convertToARGB(color);
        mHaloEffectColor.setSummary(hex);
	mHaloEffectColor.setNewPreviewColor(color); 
        mHaloEffectColor.setEnabled(mHaloColors.isChecked());
    	
	mHaloNotifTitleColor = (ColorPickerPreference) prefSet.findPreference(KEY_HALO_NOTIF_TITLE_COLOR);
        mHaloNotifTitleColor.setOnPreferenceChangeListener(this);
        color = Settings.System.getInt(mContext.getContentResolver(), 
            Settings.System.HALO_NOTIFICATION_TITLE_COLOR, 0xffffffff);
        hex = ColorPickerPreference.convertToARGB(color);
        mHaloNotifTitleColor.setSummary(hex);
        mHaloNotifTitleColor.setNewPreviewColor(color);
        mHaloNotifTitleColor.setEnabled(mHaloColors.isChecked());

        mHaloNotifDescColor = (ColorPickerPreference) prefSet.findPreference(KEY_HALO_NOTIF_DESC_COLOR);
        mHaloNotifDescColor.setOnPreferenceChangeListener(this);
        color = Settings.System.getInt(mContext.getContentResolver(), 
            Settings.System.HALO_NOTIFICATION_DESC_COLOR, 0xff999999);
        hex = ColorPickerPreference.convertToARGB(color);
        mHaloNotifDescColor.setSummary(hex);
        mHaloNotifDescColor.setNewPreviewColor(color);
        mHaloNotifDescColor.setEnabled(mHaloColors.isChecked());

	mHaloSpeechBubbleColor = (ColorPickerPreference) prefSet.findPreference(KEY_HALO_SPEECH_BUBBLE_COLOR);
        mHaloSpeechBubbleColor.setOnPreferenceChangeListener(this);
        color = Settings.System.getInt(mContext.getContentResolver(), 
            Settings.System.HALO_SPEECH_BUBBLE_COLOR, 0xff086a99);
        hex = ColorPickerPreference.convertToARGB(color);
        mHaloSpeechBubbleColor.setSummary(hex);
        mHaloSpeechBubbleColor.setNewPreviewColor(color);
        mHaloSpeechBubbleColor.setEnabled(mHaloColors.isChecked());
      
        mHaloTextColor = (ColorPickerPreference) prefSet.findPreference(KEY_HALO_TEXT_COLOR);
        mHaloTextColor.setOnPreferenceChangeListener(this);
        color = Settings.System.getInt(mContext.getContentResolver(), 
            Settings.System.HALO_TEXT_COLOR, 0xffffffff);
        hex = ColorPickerPreference.convertToARGB(color);
        mHaloTextColor.setSummary(hex);
        mHaloTextColor.setNewPreviewColor(color);
        mHaloTextColor.setEnabled(mHaloColors.isChecked());  
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
	if (preference == mHaloColors) {
                Settings.System.putInt(mContext.getContentResolver(),
                         Settings.System.HALO_COLOR, mHaloColors.isChecked() ? 1 : 0);
		mHaloCircleColor.setEnabled(mHaloColors.isChecked());
                mHaloEffectColor.setEnabled(mHaloColors.isChecked());
		mHaloNotifTitleColor.setEnabled(mHaloColors.isChecked());
                mHaloNotifDescColor.setEnabled(mHaloColors.isChecked());
		mHaloSpeechBubbleColor.setEnabled(mHaloColors.isChecked());
          	mHaloTextColor.setEnabled(mHaloColors.isChecked());   
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mHaloCircleColor) {
            String hex = ColorPickerPreference.convertToARGB(
        	Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
         	     Settings.System.HALO_CIRCLE_COLOR, ColorPickerPreference.convertToColorInt(hex));
	} else if (preference == mHaloEffectColor) {
            String hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                     Settings.System.HALO_EFFECT_COLOR, ColorPickerPreference.convertToColorInt(hex)); 
	} else if (preference == mHaloNotifTitleColor) {
            String hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                     Settings.System.HALO_NOTIFICATION_TITLE_COLOR, ColorPickerPreference.convertToColorInt(hex));
        } else if (preference == mHaloNotifDescColor) {
            String hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                     Settings.System.HALO_NOTIFICATION_DESC_COLOR, ColorPickerPreference.convertToColorInt(hex));
	} else if (preference == mHaloSpeechBubbleColor) {
            String hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                     Settings.System.HALO_SPEECH_BUBBLE_COLOR, ColorPickerPreference.convertToColorInt(hex));
        } else if (preference == mHaloTextColor) {
            String hex = ColorPickerPreference.convertToARGB(
                Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                     Settings.System.HALO_TEXT_COLOR, ColorPickerPreference.convertToColorInt(hex));          
	}
        return false;
    }
}
