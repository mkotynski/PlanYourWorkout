package com.mkt.plan4workout.Model.Plan;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plan_table")
public class Plan {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String category; // for example push, fbw, pull, split(up)

    private String description;

    public Plan(String name, String category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription(){
        return description;
    }
}
