package com.example.piotr.projectmanager.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.piotr.projectmanager.Component;
import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.Project;
import com.example.piotr.projectmanager.R;

import java.util.List;


public class NewProjectActivity extends AppCompatActivity {

    private String userMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userMail = getIntent().getStringExtra("MAIL");
        final ListView lv = (ListView) findViewById(R.id.listViewContributors);
        final Button btn = (Button) findViewById(R.id.buttonAddContributor);
        final List<String> contributors_list = new ArrayList<String>();
        final EditText editText = (EditText) findViewById(R.id.editTextIdContributor);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, contributors_list);
        lv.setAdapter(arrayAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contributors_list.add(editText.getText().toString());
                Component.setListViewHeight(lv);
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public void clickCreateProject(View view) {
        UsersDatabaseHelper db = new UsersDatabaseHelper(this);
        EditText editText = (EditText) findViewById(R.id.editTextPName);
        Project newProject = new Project();
        newProject.Name = editText.getText().toString();
        editText = (EditText) findViewById(R.id.editTextDescription);
        newProject.Description = editText.getText().toString();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        editText = (EditText) findViewById(R.id.editTextDeadline);

        try {
            String date = editText.getText().toString();
            newProject.Deadline = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("DATE","Problem z konwersja do daty");
        }

        newProject.Owner = db.getUserId(userMail);
        db.addOrUpdateProject(newProject);
        finish();
    }
}