package com.example.piotr.projectmanager.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.piotr.projectmanager.Component;
import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.Project;
import com.example.piotr.projectmanager.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectsActivity extends AppCompatActivity {

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    static final String STATE_PROJECTS = "projects";

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> projects_list;
    private String userMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*listView = (ListView) findViewById(R.id.listViewProjects);
        final String userMail = getIntent().getStringExtra("MAIL");
        UsersDatabaseHelper db = new UsersDatabaseHelper(this);
        List<Project> projects = db.getAllProjects(userMail);
        Collections.sort(projects, new Comparator<Project>() {
            @Override
            public int compare(final Project o1,final Project o2) {
                return o1.Id > o2.Id ? -1 : (o1.Id < o2.Id) ? 1 : 0;
            }
        });
        projects_list = new ArrayList<String>();
        final List<Integer> projects_id = new ArrayList<Integer>();
        arrayAdapter = new ArrayAdapter<String>
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
                Intent intent = new Intent(ProjectsActivity.this,ProjectMoreActivity.class);
                startActivity(intent);
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
        });*/
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        listView = (ListView) findViewById(R.id.listViewProjects);
        userMail = getIntent().getStringExtra("MAIL");
        settings = getSharedPreferences("prefs",MODE_PRIVATE);

        if(userMail != null){
            editor = settings.edit();
            editor.putString("mail",userMail);
            editor.commit();
        }
        else{
            userMail = settings.getString("mail","");
        }
        UsersDatabaseHelper db = new UsersDatabaseHelper(this);

        final List<Project> projects = db.getAllProjects(userMail);
        Collections.sort(projects, new Comparator<Project>() {
            @Override
            public int compare(final Project o1,final Project o2) {
                return o1.getDeadline().compareTo(o2.getDeadline());
            }
        });
        projects_list = new ArrayList<String>();
        final List<Integer> projects_id = new ArrayList<Integer>();
        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, projects_list);
        listView.setAdapter(arrayAdapter);
        for(int i = 0;i<projects.size();i++){
            projects_list.add(projects.get(i).getName().toString());
            projects_id.add(projects.get(i).getId());
            Component.setListViewHeight(listView);
            arrayAdapter.notifyDataSetChanged();
            addNotification(i+100);
        }
    db.close();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProjectsActivity.this,ProjectMoreActivity.class);
                //Toast.makeText(ProjectsActivity.this,""+projects_id.get(position)+"",Toast.LENGTH_LONG).show();
                for(int i = 0;i<projects.size();i++){
                    if(projects_id.get(i) == projects_id.get(position)){

                        intent.putExtra("PROJECT",projects.get(position));
                       /*intent.putExtra("PROJECT_ID",projects.get(i).getId());
                        intent.putExtra("PROJECT_NAME",projects.get(i).getName().toString());
                        intent.putExtra("PROJECT_DESC",projects.get(i).getDescription().toString());
                        intent.putExtra("PROJECT_DEADLINE",projects.get(i).getDeadline().toString());
                        intent.putExtra("PROJECT_OWNER",projects.get(i).getOwner());*/
                    }
                }
                startActivity(intent);
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
    @Override
    protected void onDestroy(){
        super.onDestroy();
        /*settings.edit().clear();
        settings.edit().commit();
        editor.clear();
        editor.commit();*/
    }
    void addNotification(int id) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("asd") // title for notification
                .setContentText("asdasdas")// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), ProjectsActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(id, mBuilder.build());
    }
}
