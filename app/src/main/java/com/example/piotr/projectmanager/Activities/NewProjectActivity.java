package com.example.piotr.projectmanager.Activities;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.piotr.projectmanager.Component;
import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.Contributor;
import com.example.piotr.projectmanager.Model.Project;
import com.example.piotr.projectmanager.R;

import java.util.List;


public class NewProjectActivity extends AppCompatActivity {

    private String userMail;
    private List<String> contributors_list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userMail = getIntent().getStringExtra("MAIL");
        final ListView lv = (ListView) findViewById(R.id.listViewContributors);
        final Button btn = (Button) findViewById(R.id.buttonAddContributor);
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
        editText = (EditText) findViewById(R.id.editTextDeadline);
        newProject.Deadline = editText.getText().toString();
        Contributor newContributor = new Contributor();
        newProject.Owner = db.getUserId(userMail);
        newContributor.idProj = (int)db.addOrUpdateProject(newProject);
        for(int i =0;i<contributors_list.size();i++){
            newContributor.idUser = db.getUserId(contributors_list.get(i));
            db.addContributor(newContributor);
        }
        finish();
    }
}
