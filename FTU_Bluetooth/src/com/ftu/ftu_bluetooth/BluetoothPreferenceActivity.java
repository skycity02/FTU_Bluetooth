package com.ftu.ftu_bluetooth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 *
 * @author deanna.yu
 */
public class BluetoothPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    private static final String PWD = "pwd";
    private EditTextPreference mPWDFilterPreference;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mPWDFilterPreference = (EditTextPreference) findPreference(PWD);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setup the initial values
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        
        mPWDFilterPreference.setSummary(this.getString(
                R.string.pwd_summary,
                sharedPreferences.getString(PWD, getString(R.string.pwd_preference))));
        // Set up a listener whenever a key changes
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedpreferences, String key) {
        if (PWD.equals(key)) {
        	mPWDFilterPreference.setSummary(this.getString(
                    R.string.pwd_summary,
                    sharedpreferences.getString(PWD, getString(R.string.pwd_preference))));
        }
    }
    
    public static String getPWD(Context context) {
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);//getFloat(key, -1)
    	String pwd = (String)settings.getString(PWD, context.getString(R.string.pwd_preference));
    	return pwd;
    }
    
    public static void setPWD(Context context, String size) {
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.putString(PWD, size);
    	editor.commit();
    }
    
}
