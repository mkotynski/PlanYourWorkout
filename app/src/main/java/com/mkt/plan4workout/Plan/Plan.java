package com.mkt.plan4workout.Plan;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "plan_table")
public class Plan {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String cateogry; // for example push, fbw, pull, split(up)

    private String description;

    public Plan(String name, String cateogry, String description) {
        this.name = name;
        this.cateogry = cateogry;
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

    public String getCateogry() {
        return cateogry;
    }

    public String getDescription(){
        return description;
    }
}
