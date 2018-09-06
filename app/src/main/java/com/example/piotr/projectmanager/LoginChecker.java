package com.example.piotr.projectmanager;

import android.content.Context;

import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.User;

/**
 * Created by Piotr on 03.03.2018.
 */

public class LoginChecker {
    private Context context;

    public LoginChecker(Context context){this.context = context;}

    public boolean isCorrect(User user) throws Exception {
        UsersDatabaseHelper databaseHelper = UsersDatabaseHelper.getsInstance(context);

        //String test = databaseHelper.getPassword(user);
        if(databaseHelper.getPassword(user) !=null && user.getPassword().equals(AESCrypt.decrypt(databaseHelper.getPassword(user)))){
            return true;
        }
        else{
            return false;
        }
    }
}
