package com.example.piotr.projectmanager;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void clickSignIn(View view) {
        final EditText login = (EditText) findViewById(R.id.editTextLogin);
        final EditText pass = (EditText) findViewById(R.id.editTextPassword);

        ConnectionDetector cd = new ConnectionDetector(this);
        LoginChecker lc = new LoginChecker(this);
        User u = new User();
        u.uName = login.getText().toString();
        u.uPassword = pass.getText().toString();

        if(!cd.isConnected()){
            Toast.makeText(LoginActivity.this,"Check your internet connection!",Toast.LENGTH_SHORT).show();
        }
        else{
            if(!lc.isCorrect(u)){
                Toast.makeText(LoginActivity.this,"Wrong login or password!",Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent;
                intent = new Intent(LoginActivity.this,ProjectsActivity.class);
                startActivity(intent);
            }
        }
    }
}
