package com.example.piotr.projectmanager.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piotr.projectmanager.Component;
import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.Contributor;
import com.example.piotr.projectmanager.Model.Project;
import com.example.piotr.projectmanager.Model.Task;
import com.example.piotr.projectmanager.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectMoreActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> tasks_list;
    private Project currentProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Project project = (Project)(getIntent().getSerializableExtra("PROJECT"));
        if(project != null) {
            setTitle(project.getName());
            currentProject = project;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu_project,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK ) {
            Project object = (Project)(data.getSerializableExtra("PROJECT"));
            currentProject = object;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();


        final TextView desc = (TextView) findViewById(R.id.textViewDescriptionText);
        final TextView deadline = (TextView) findViewById(R.id.textViewDeadlineDate);
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);

        final List<Integer> tasks_id = new ArrayList<Integer>();
        listView = (ListView) findViewById(R.id.listViewTasks);

        UsersDatabaseHelper db = new UsersDatabaseHelper(this);
        final List<Task> tasks = db.getTasks(currentProject.getId());

        desc.setText(currentProject.getDescription());
        deadline.setText(currentProject.getDeadline());
        if(tasks.size()>0)
            progress.setProgress((Integer)(Component.getAllFinishedTasksCount(tasks)*100/tasks.size()));

        tasks_list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, tasks_list);
        listView.setAdapter(arrayAdapter);
        for(int i = 0;i<tasks.size();i++){
            if(tasks.get(i).isStatus()) {
                //dodac italic do (finished)
                tasks_list.add(tasks.get(i).getName().toString() + "(finished)");
            }
            else
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
                        intent.putExtra("PROJECT",currentProject);
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
                intent.putExtra("PROJECT_ID",currentProject.getId());
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void clickContributorsItem(MenuItem item) {
        Intent intent = new Intent(ProjectMoreActivity.this,ContributorsActivity.class);
        intent.putExtra("PROJECT",currentProject);
        startActivity(intent);
    }
    public void clickDeleteItem(MenuItem item) {
        LayoutInflater layoutInflater = LayoutInflater.from(ProjectMoreActivity.this);
        View promptView = layoutInflater.inflate(R.layout.delete_prompt, null);

        AlertDialog.Builder alertDialogBulider = new AlertDialog.Builder(ProjectMoreActivity.this);

        alertDialogBulider.setView(promptView);

        alertDialogBulider.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UsersDatabaseHelper db = new UsersDatabaseHelper(ProjectMoreActivity.this);
                db.close();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBulider.create();
        alertDialog.show();
    }
}
