package com.example.piotr.projectmanager.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.piotr.projectmanager.Model.Contributor;
import com.example.piotr.projectmanager.Model.Project;
import com.example.piotr.projectmanager.Model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Piotr on 03.03.2018.
 */

public class UsersDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "usersDatabase";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_USERS = "users";

    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_MAIL = "mail";
    private static final String KEY_USER_FIRST_NAME = "first_name";
    private static final String KEY_USER_SECOND_NAME = "second_name";
    private static final String KEY_USER_PASSWORD = "password";

    private static final String TABLE_PROJECTS = "projects";

    private static final String KEY_PROJECT_ID = "id";
    private static final String KEY_PROJECT_NAME = "name";
    private static final String KEY_PROJECT_DESCRIPTION = "description";
    private static final String KEY_PROJECT_DEADLINE = "deadline";
    private static final String KEY_PROJECT_OWNER = "owner";

    private static final String TABLE_CONTRIBUTORS = "contributors";

    private static final String KEY_ID_USER = "id_user";
    private static final String KEY_ID_PROJ = "id_project";

    private static final String TABLE_TASKS = "tasks";

    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_NAME = "name";
    private static final String KEY_TASK_DESCRIPTION = "description";
    private static final String KEY_TASK_STATUS = "status";
    private static final String KEY_TASK_WHO_FINISH = "who_finish";

    private static final String TABLE_PROJ_TASK = "proj_task";

    private static final String KEY_PROJ_TASK_ID_TASK = "id_task";
    private static final String KEY_PROJ_TASK_ID_PROJ = "id_proj";

    private static UsersDatabaseHelper sInstance;

    public static synchronized UsersDatabaseHelper getsInstance(Context context){
        if(sInstance == null){
            sInstance = new UsersDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public UsersDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void  onConfigure(SQLiteDatabase db){
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_USER_MAIL + " TEXT," +
                KEY_USER_FIRST_NAME + " TEXT," +
                KEY_USER_SECOND_NAME + " TEXT," +
                KEY_USER_PASSWORD + " TEXT" +
                ")";
        String CREATE_PROJECTS_TABLE = "CREATE TABLE " + TABLE_PROJECTS +
                "(" +
                KEY_PROJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_PROJECT_NAME + " TEXT," +
                KEY_PROJECT_DESCRIPTION + " TEXT," +
                KEY_PROJECT_DEADLINE + " TEXT," +
                KEY_PROJECT_OWNER + " INTEGER, " +
                "FOREIGN KEY (" + KEY_PROJECT_OWNER + ") " +
                "REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + ")" +
                ")";
        String CREATE_CONTRIBUTORS_TABLE = "CREATE TABLE " + TABLE_CONTRIBUTORS +
                "(" +
                KEY_ID_USER + " INTEGER," +
                KEY_ID_PROJ + " INTEGER," +
                "FOREIGN KEY (" + KEY_ID_USER + ") " +
                "REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + ")," +
                "FOREIGN KEY (" + KEY_ID_PROJ + ") " +
                "REFERENCES " + TABLE_PROJECTS + "(" + KEY_PROJECT_ID + ")" +
                ")";
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS +
                "(" +
                KEY_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_TASK_NAME + " TEXT," +
                KEY_TASK_DESCRIPTION + " TEXT," +
                KEY_TASK_STATUS + " TEXT," +
                KEY_TASK_WHO_FINISH + " INTEGER, " +
                "FOREIGN KEY (" + KEY_TASK_WHO_FINISH + ") " +
                "REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + ")" +
                ")";
        String CREATE_PROJ_TASK_TABLE = "CREATE TABLE " + TABLE_PROJ_TASK +
                "(" +
                KEY_PROJ_TASK_ID_TASK + " INTEGER," +
                KEY_PROJ_TASK_ID_PROJ + " INTEGER," +
                "FOREIGN KEY (" + KEY_PROJ_TASK_ID_TASK + ") " +
                "REFERENCES " + TABLE_TASKS + "(" + KEY_TASK_ID + ")," +
                "FOREIGN KEY (" + KEY_PROJ_TASK_ID_PROJ + ") " +
                "REFERENCES " + TABLE_PROJECTS + "(" + KEY_PROJECT_ID + ")" +
                ")";
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PROJECTS_TABLE);
        db.execSQL(CREATE_CONTRIBUTORS_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
        db.execSQL(CREATE_PROJ_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTRIBUTORS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJ_TASK);
            onCreate(db);
        }
    }
    public String getPassword(User user){
        User selectUser = new User();

        String USER_SELECT_PASSWORD =
                String.format("SELECT %s FROM %s WHERE %s.%s='%s'",
                        KEY_USER_PASSWORD,
                        TABLE_USERS,
                        TABLE_USERS,
                        KEY_USER_MAIL,
                        user.mail
                        );

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_SELECT_PASSWORD,null);
        try{
            if(cursor.moveToFirst()){
                User newUser = new User();
                newUser.password = cursor.getString(0);
                selectUser = newUser;
            }
            else{
                Log.d("Database","nie trafil do ifa pass");
            }
        }catch (Exception e){
            Log.d("Database","Error while trying to get from database");
        }finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return selectUser.password;
    }

    public long addOrUpdateUser(User user){
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_USER_MAIL, user.mail);
            values.put(KEY_USER_FIRST_NAME, user.firstName);
            values.put(KEY_USER_SECOND_NAME, user.secondName);
            values.put(KEY_USER_PASSWORD, user.password);

            int rows = db.update(TABLE_USERS,values,KEY_USER_MAIL + "=?",new String[]{user.firstName});

            if(rows == 1){
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID,TABLE_USERS,KEY_USER_MAIL
                );
             Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.mail)});
             try{
                 if(cursor.moveToFirst()){
                     userId = cursor.getInt(0);
                     db.setTransactionSuccessful();
                 }
             }finally {
                 if(cursor != null && !cursor.isClosed()){
                     cursor.close();
                 }
             }
            } else {
                userId = db.insertOrThrow(TABLE_USERS,null,values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e){
            Log.d("Database", "Error while trying to add update user");
        }finally {
            db.endTransaction();
        }
        return userId;
    }
    public long addOrUpdateProject(Project project){
        SQLiteDatabase db = getWritableDatabase();
        long projectId = -1;
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_PROJECT_NAME,project.Name);
            values.put(KEY_PROJECT_DESCRIPTION,project.Description);
            values.put(KEY_PROJECT_DEADLINE, String.valueOf(project.Deadline));
            values.put(KEY_PROJECT_OWNER, String.valueOf(project.Owner));

            int rows = db.update(TABLE_PROJECTS,values,KEY_PROJECT_NAME + "=?",new String[]{project.Name});

            if (rows == 1){
                String projectSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_PROJECT_ID,TABLE_USERS,KEY_PROJECT_NAME
                );
                Cursor cursor = db.rawQuery(projectSelectQuery,new String[]{String.valueOf(project.Name)});
                try{
                    if(cursor.moveToFirst()){
                        projectId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                }finally {
                    if(cursor != null && !cursor.isClosed()){
                        cursor.close();
                    }
                }
            }else{
                projectId = db.insertOrThrow(TABLE_PROJECTS,null,values);
                db.setTransactionSuccessful();
            }
        }catch (Exception e){
            Log.d("Database","Error while trying to add update project");
        }finally {
            db.endTransaction();
        }
        return projectId;
    }

    public long addContributor(Contributor contributor){
        SQLiteDatabase db = getWritableDatabase();
        long idProj = -1;
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(KEY_ID_USER,contributor.idUser);
            values.put(KEY_ID_PROJ,contributor.idProj);

            idProj = db.insertOrThrow(TABLE_CONTRIBUTORS,null,values);
            db.setTransactionSuccessful();

        }catch (Exception e){
            Log.d("Database","Error while trying to add contributor");
        }finally {
            db.endTransaction();
        }
        return idProj;
    }
    public int getUserId(String userMail) {
        int selectId = 0;
        String USER_SELECT_ID =
                String.format("SELECT %s FROM %s WHERE %s.%s='%s'",
                        KEY_USER_ID,
                        TABLE_USERS,
                        TABLE_USERS,
                        KEY_USER_MAIL,
                        userMail
                );
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_SELECT_ID,null);
        try{
            if(cursor.moveToFirst()){
                selectId = cursor.getInt(0);
            }
            else{
                Log.d("Database","nie trafil do ifa user");
            }
        }catch (Exception e){
            Log.d("Database","Error while trying to get from database");
        }finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return selectId;
    }

    public List<Project> getAllProjects(String userMail) {
        List<Project> projects = new ArrayList<>();
        String PROJECT_ALL_SELECT =
                String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s FROM %s INNER JOIN %s ON %s.%s = %s.%s WHERE %s.%s = '%s'",
                        TABLE_PROJECTS,
                        KEY_PROJECT_ID,
                        TABLE_PROJECTS,
                        KEY_PROJECT_OWNER,
                        TABLE_PROJECTS,
                        KEY_PROJECT_NAME,
                        TABLE_PROJECTS,
                        KEY_PROJECT_DESCRIPTION,
                        TABLE_PROJECTS,
                        KEY_PROJECT_DEADLINE,
                        TABLE_PROJECTS,
                        TABLE_USERS,
                        TABLE_PROJECTS,
                        KEY_PROJECT_OWNER,
                        TABLE_USERS,
                        KEY_USER_ID,
                        TABLE_USERS,
                        KEY_USER_MAIL,
                        userMail
                        );
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(PROJECT_ALL_SELECT,null);
        try{
            if(cursor.moveToFirst()){
                do{
                    Project newProject = new Project();
                    newProject.Id = cursor.getInt(cursor.getColumnIndex(KEY_PROJECT_ID));
                    newProject.Name = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_NAME));
                    newProject.Description = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_DESCRIPTION));
                    newProject.Owner = cursor.getInt(cursor.getColumnIndex(KEY_PROJECT_OWNER));
                    newProject.Deadline = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_DEADLINE));
                    projects.add(newProject);
                }while(cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Database","Problem in getAllProjects");
        }finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        PROJECT_ALL_SELECT =
                String.format("SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s FROM %s INNER JOIN %s ON %s.%s = %s.%s" +
                        " INNER JOIN %s ON %s.%s = %s.%s" +
                                " WHERE %s.%s = '%s'",
                        TABLE_PROJECTS,
                        KEY_PROJECT_ID,
                        TABLE_PROJECTS,
                        KEY_PROJECT_OWNER,
                        TABLE_PROJECTS,
                        KEY_PROJECT_NAME,
                        TABLE_PROJECTS,
                        KEY_PROJECT_DESCRIPTION,
                        TABLE_PROJECTS,
                        KEY_PROJECT_DEADLINE,
                        TABLE_PROJECTS,
                        TABLE_CONTRIBUTORS,
                        TABLE_PROJECTS,
                        KEY_PROJECT_ID,
                        TABLE_CONTRIBUTORS,
                        KEY_ID_PROJ,
                        TABLE_USERS,
                        TABLE_USERS,
                        KEY_USER_ID,
                        TABLE_CONTRIBUTORS,
                        KEY_ID_USER,
                        TABLE_USERS,
                        KEY_USER_ID,
                        getUserId(userMail)
                );
        db = getReadableDatabase();
        cursor = db.rawQuery(PROJECT_ALL_SELECT,null);
        try{
            if(cursor.moveToFirst()){
                do{
                    Project newProject = new Project();
                    newProject.Id = cursor.getInt(cursor.getColumnIndex(KEY_PROJECT_ID));
                    newProject.Name = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_NAME));
                    newProject.Description = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_DESCRIPTION));
                    newProject.Owner = cursor.getInt(cursor.getColumnIndex(KEY_PROJECT_OWNER));
                    newProject.Deadline = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_DEADLINE));
                    projects.add(newProject);
                }while(cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Database","Problem in getAllProjects");
        }finally {
            if(cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return  projects;
    }
}
