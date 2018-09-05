package com.example.piotr.projectmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.piotr.projectmanager.Component;
import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.Project;
import com.example.piotr.projectmanager.Model.Task;
import com.example.piotr.projectmanager.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectMoreActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> tasks_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Project project = (Project)(getIntent().getSerializableExtra("PROJECT"));

        setTitle(project.getName());

        final TextView desc = (TextView) findViewById(R.id.textViewDescriptionText);
        final TextView deadline = (TextView) findViewById(R.id.textViewDeadlineDate);
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);

        final List<Integer> tasks_id = new ArrayList<Integer>();
        listView = (ListView) findViewById(R.id.listViewTasks);

        UsersDatabaseHelper db = new UsersDatabaseHelper(this);
        final List<Task> tasks = db.getTasks(project.getId());

        desc.setText(project.getDescription());
        deadline.setText(project.getDeadline());
        progress.setProgress(20);

        tasks_list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, tasks_list);
        listView.setAdapter(arrayAdapter);
        for(int i = 0;i<tasks.size();i++){
            tasks_list.add(tasks.get(i).getName().toString());
            tasks_id.add(tasks.get(i).getId());
            Component.setListViewHeight(listView);
            arrayAdapter.notifyDataSetChanged();
        }
        db.close();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProjectMoreActivity.this,TaskMoreActivity.class);
                //Toast.makeText(ProjectsActivity.this,""+projects_id.get(position)+"",Toast.LENGTH_LONG).show();
                for(int i = 0;i<tasks.size();i++){
                    if(tasks_id.get(i) == tasks_id.get(position)){
                        intent.putExtra("TASK",tasks.get(position));
                    }
                }
                startActivity(intent);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectMoreActivity.this,NewTaskActivity.class);
                intent.putExtra("PROJECT_ID",project.getId());
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
