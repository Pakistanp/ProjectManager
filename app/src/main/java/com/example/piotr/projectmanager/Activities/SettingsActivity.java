package com.example.piotr.projectmanager.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.piotr.projectmanager.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings_layout, new SettingFragment())
                .commit();
    }
}
