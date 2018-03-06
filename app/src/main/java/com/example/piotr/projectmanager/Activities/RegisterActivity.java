package com.example.piotr.projectmanager.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.User;
import com.example.piotr.projectmanager.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText mail,firstN,secondN,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void clickRegister(View view) {
        mail = (EditText) findViewById(R.id.editTextMail);
        firstN = (EditText) findViewById(R.id.editTextFirstName);
        secondN = (EditText) findViewById(R.id.editTextSecondName);
        password = (EditText) findViewById(R.id.editTextPassword);
        UsersDatabaseHelper databaseHelper = new UsersDatabaseHelper(view.getContext());
        User u = new User();
        u.mail = mail.getText().toString();
        u.firstName = firstN.getText().toString();
        u.secondName = secondN.getText().toString();
        u.password = password.getText().toString();

        databaseHelper.addOrUpdateUser(u);
        finish();
    }
}
