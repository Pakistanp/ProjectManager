package com.example.piotr.projectmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.Project;
import com.example.piotr.projectmanager.Model.Task;
import com.example.piotr.projectmanager.R;

public class TaskMoreActivity extends AppCompatActivity {
    private Task taskU;
    private Project project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView desc = (TextView) findViewById(R.id.textViewDescription);
        final Button btn = (Button) findViewById(R.id.btnFinishTask);
        final Task task = (Task)(getIntent().getSerializableExtra("TASK"));
        project = (Project)(getIntent().getSerializableExtra("PROJECT"));
        taskU = task;
        setTitle(task.getName());
        desc.setText(task.getDescription());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void clickFinishTask(View view) {
        if(taskU.isStatus())
            Toast.makeText(TaskMoreActivity.this,"Task finished before",Toast.LENGTH_SHORT).show();
        else {
            taskU.setStatus(true);
            UsersDatabaseHelper databaseHelper = new UsersDatabaseHelper(view.getContext());
            databaseHelper.addOrUpdateTask(taskU);
            databaseHelper.close();
            Intent intent = getIntent();
            intent.putExtra("PROJECT",project);
            setResult(1, intent);
            finish();
        }
    }
}
