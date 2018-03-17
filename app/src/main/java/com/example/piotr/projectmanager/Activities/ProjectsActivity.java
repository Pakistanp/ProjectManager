package com.example.piotr.projectmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.piotr.projectmanager.Component;
import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.Project;
import com.example.piotr.projectmanager.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listView = (ListView) findViewById(R.id.listViewProjects);
        final String userMail = getIntent().getStringExtra("MAIL");

        UsersDatabaseHelper db = new UsersDatabaseHelper(this);
        List<Project> projects = db.getAllProjects(userMail);
        Collections.sort(projects, new Comparator<Project>() {
            @Override
            public int compare(final Project o1,final Project o2) {
                return o1.Id > o2.Id ? -1 : (o1.Id < o2.Id) ? 1 : 0;
            }
        });
        List<String> projects_list = new ArrayList<String>();
        final List<Integer> projects_id = new ArrayList<Integer>();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, projects_list);
        listView.setAdapter(arrayAdapter);
        for(int i = 0;i<projects.size();i++){
            projects_list.add(projects.get(i).Name.toString());
            projects_id.add(projects.get(i).Id);
            Component.setListViewHeight(listView);
            arrayAdapter.notifyDataSetChanged();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ProjectsActivity.this,projects_id.get((int)id).toString(),Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectsActivity.this,NewProjectActivity.class);
                intent.putExtra("MAIL",userMail);
                startActivity(intent);
            }
        });
    }

}
