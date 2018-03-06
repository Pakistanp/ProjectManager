package com.example.piotr.projectmanager;

import android.content.Context;

import com.example.piotr.projectmanager.Database.UsersDatabaseHelper;
import com.example.piotr.projectmanager.Model.User;

/**
 * Created by Piotr on 03.03.2018.
 */

public class LoginChecker {
    Context context;

    public LoginChecker(Context context){this.context = context;}

    public boolean isCorrect(User user){
        //TESTS
        User sampleUser = new User();
        sampleUser.firstName = "Piotr";
        sampleUser.secondName = "Pap";
        sampleUser.Password = "12345";

        UsersDatabaseHelper databaseHelper = UsersDatabaseHelper.getsInstance(context);

        databaseHelper.addOrUpdateUser(sampleUser);
        //String test = databaseHelper.getPassword(user);
        if(user.Password.equals(databaseHelper.getPassword(user))){
            return true;
        }
        else{
            return false;
        }
    }
}
