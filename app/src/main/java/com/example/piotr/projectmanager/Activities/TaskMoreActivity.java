package com.example.piotr.projectmanager.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.piotr.projectmanager.Model.Task;
import com.example.piotr.projectmanager.R;

public class TaskMoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView desc = (TextView) findViewById(R.id.textViewDescription);
        final Task task = (Task)(getIntent().getSerializableExtra("TASK"));

        setTitle(task.getName());
        desc.setText(task.getDescription());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
