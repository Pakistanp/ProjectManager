package com.example.piotr.projectmanager.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.Proj_Task;
import com.example.piotr.projectmanager.Model.Task;
import com.example.piotr.projectmanager.R;

public class NewTaskActivity extends AppCompatActivity {

    private EditText name;
    private EditText description;
    //private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = (EditText)findViewById(R.id.editTextTName);
        description = (EditText)findViewById(R.id.editTextTDescription);
        //button = (Button)findViewById(R.id.buttonCreateTask);
    }

    public void clickCreateTask(View view) {
        int projectId = getIntent().getIntExtra("PROJECT_ID",-1);
        Task task = new Task();
        task.setName(name.getText().toString());
        task.setDescription(description.getText().toString());
        task.setStatus(false);
        task.setWhoFinish(-1);

        UsersDatabaseHelper db = new UsersDatabaseHelper(this);

        Proj_Task proj_task = new Proj_Task();
        proj_task.setId_proj(projectId);
        proj_task.setId_task((int)db.addOrUpdateTask(task));
        db.addProjTask(proj_task);
        finish();
    }
}
