package com.example.piotr.projectmanager.Activities;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.piotr.projectmanager.R;

public class SettingsActivity extends PreferenceActivity {
    public static final String
            KEY_PREF_NOTIFICATIONS_SWITCH = "notifications_switch";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
