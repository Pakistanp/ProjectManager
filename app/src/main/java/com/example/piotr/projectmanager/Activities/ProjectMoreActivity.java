package com.example.piotr.projectmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.piotr.projectmanager.Model.Project;
import com.example.piotr.projectmanager.R;

public class ProjectMoreActivity extends AppCompatActivity {

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
        final Project project = new Project();
        project.setName(getIntent().getStringExtra("PROJECT_NAME"));
        project.setDeadline(getIntent().getStringExtra("PROJECT_DEADLINE"));
        project.setDescription(getIntent().getStringExtra("PROJECT_DESC"));

        setTitle(project.getName());

        final TextView desc = (TextView) findViewById(R.id.textViewDescriptionText);
        final TextView deadline = (TextView) findViewById(R.id.textViewDeadlineDate);
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);

        desc.setText(project.getDescription());
        deadline.setText(project.getDeadline());
        progress.setProgress(20);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectMoreActivity.this,NewTaskActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
