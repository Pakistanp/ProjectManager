package com.example.piotr.projectmanager.Model;

import java.io.Serializable;

/**
 * Created by Piotr on 05.03.2018.
 */

public class Project implements Serializable {
    private int Id;
    private String Name;
    private String Description;
    private String Deadline;
    private int Owner;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

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

    public String getDeadline() {
        return Deadline;
    }

    public void setDeadline(String deadline) {
        Deadline = deadline;
    }

    public int getOwner() {
        return Owner;
    }

    public void setOwner(int owner) {
        Owner = owner;
    }
}
