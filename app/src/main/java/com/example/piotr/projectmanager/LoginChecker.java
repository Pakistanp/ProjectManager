package com.example.piotr.projectmanager;

import android.content.Context;

/**
 * Created by Piotr on 03.03.2018.
 */

public class LoginChecker {
    Context context;

    public LoginChecker(Context context){this.context = context;}

    public boolean isCorrect(User user){
        //TESTS
        User sampleUser = new User();
        sampleUser.uName = "Piotr";
        sampleUser.uPassword = "12345";

        UsersDatabaseHelper databaseHelper = UsersDatabaseHelper.getsInstance(context);

        databaseHelper.addOrUpdateUser(sampleUser);
        String test = databaseHelper.getPassword(user);
        if(user.uPassword.equals(databaseHelper.getPassword(user))){
            return true;
        }
        else{
            return false;
        }
    }
}
