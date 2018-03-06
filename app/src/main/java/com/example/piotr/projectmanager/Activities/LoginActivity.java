package com.example.piotr.projectmanager.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CheckBox;

import com.example.piotr.projectmanager.ConnectionDetector;
import com.example.piotr.projectmanager.LoginChecker;
import com.example.piotr.projectmanager.R;
import com.example.piotr.projectmanager.Model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText login,pass;
    private CheckBox rememberMeCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        login = (EditText) findViewById(R.id.editTextLogin);
        pass = (EditText) findViewById(R.id.editTextPassword);
        rememberMeCheckBox = (CheckBox) findViewById(R.id.checkBoxRememberMe);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        rememberMe = loginPreferences.getBoolean("rememberMe", false);
        if(rememberMe == true)
        {
            login.setText(loginPreferences.getString("mail",""));
            pass.setText(loginPreferences.getString("password",""));
            rememberMeCheckBox.setChecked(true);
        }
    }

    public void clickSignIn(View view) {
        ConnectionDetector cd = new ConnectionDetector(this);
        LoginChecker lc = new LoginChecker(this);
        User u = new User();
        u.mail = login.getText().toString();
        u.password = pass.getText().toString();

        if(!cd.isConnected()){
            Toast.makeText(LoginActivity.this,"Check your internet connection!",Toast.LENGTH_SHORT).show();
        }
        else{
            if(!lc.isCorrect(u)){
                Toast.makeText(LoginActivity.this,"Wrong login or password!",Toast.LENGTH_SHORT).show();
            }
            else{
                if(rememberMeCheckBox.isChecked()){
                    loginPrefsEditor.putBoolean("rememberMe", true);
                    loginPrefsEditor.putString("mail", u.mail);
                    loginPrefsEditor.putString("password", u.password);
                    loginPrefsEditor.commit();
                }
                else{
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                Intent intent;
                intent = new Intent(LoginActivity.this,ProjectsActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        }
    }
}
