package com.example.piotr.projectmanager.Model;

import java.io.Serializable;

/**
 * Created by Piotr on 18.03.2018.
 */

public class Task implements Serializable {
    private String Name;
    private String Description;
    private boolean Status;
    private int WhoFinish;
    private int Id;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public int getWhoFinish() {
        return WhoFinish;
    }

    public void setWhoFinish(int whoFinish) {
        WhoFinish = whoFinish;
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

}
