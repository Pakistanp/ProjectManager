package com.example.piotr.projectmanager.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.piotr.projectmanager.Component;
import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.Contributor;
import com.example.piotr.projectmanager.Model.Project;
import com.example.piotr.projectmanager.Model.User;
import com.example.piotr.projectmanager.R;

import java.util.ArrayList;
import java.util.List;

public class ContributorsActivity extends AppCompatActivity {
    private Project project;
    private List<String> contributors_list = new ArrayList<String>();
    private List<Contributor> contributors = new ArrayList<Contributor>();
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        project = (Project) (getIntent().getSerializableExtra("PROJECT"));
        final ListView lv = (ListView) findViewById(R.id.listViewContributors);
        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, contributors_list);
        lv.setAdapter(arrayAdapter);
        registerForContextMenu(lv);

        UsersDatabaseHelper db = new UsersDatabaseHelper(this);
        contributors = db.getContributors(project.getId());
        //final List<Integer> contributors_id = new ArrayList<Integer>();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("PROJECT", project);
                setResult(1, intent);
                finish();
            }
        });
        if (contributors.size() > 0){
            for (int i = 0; i < contributors.size(); i++) {
                User user = new User();
                user = db.getUserNames(contributors.get(i).getIdUser());
                contributors_list.add(user.getFirstName()+ " " + user.getSecondName());
                //contributors_id.add(contributors.get(i).getId());
                Component.setListViewHeight(lv);
                arrayAdapter.notifyDataSetChanged();
            }
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(ContributorsActivity.this);
                View promptView = layoutInflater.inflate(R.layout.prompts,null);

                AlertDialog.Builder alertDialogBulider = new AlertDialog.Builder(ContributorsActivity.this);

                alertDialogBulider.setView(promptView);

                final EditText userInput = (EditText) promptView.findViewById(R.id.editTextDialogUserInput);

                alertDialogBulider.setCancelable(false).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UsersDatabaseHelper db = new UsersDatabaseHelper(ContributorsActivity.this);
                        if(!userInput.getText().toString().equals("") && db.getUserId(userInput.getText().toString()) != 0) {
                            contributors_list.add(userInput.getText().toString());
                            Component.setListViewHeight(lv);
                            arrayAdapter.notifyDataSetChanged();

                            Contributor newContributor = new Contributor();
                            newContributor.setIdProj(project.getId());
                            newContributor.setIdUser(db.getUserId(userInput.getText().toString()));
                            db.addContributor(newContributor);
                        }
                        else
                            Toast.makeText(ContributorsActivity.this,"First fill this field!",Toast.LENGTH_SHORT).show();
                        userInput.setText("");
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
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("PROJECT",project);
        setResult(1, intent);
        super.onBackPressed();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contributor_long_click, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if(item.getItemId()==R.id.action_delete_contributor){
            deleteContributor(info.position,contributors.get(info.position).getIdUser());
        }
        else{
            return false;
        }
        return true;
    }
    void deleteContributor(int positionToRemove, int id){
        contributors_list.remove(positionToRemove);
        arrayAdapter.notifyDataSetChanged();
        deleteContributorDatabase(id);
    }
    void deleteContributorDatabase(int id){
        UsersDatabaseHelper db = new UsersDatabaseHelper(ContributorsActivity.this);
        db.deleteContributor(id,project.getId());
        db.close();
    }
}
