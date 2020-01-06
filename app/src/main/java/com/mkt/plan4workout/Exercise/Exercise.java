package com.mkt.plan4workout.Exercise;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercise_table")
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    private String category; // back, chest, legs, shoulder, arm, ... , other (7 types)

    private String type; // pull, push or other

    private String description;

    public Exercise(String name, String category, String type, String description) {
        this.name = name;
        this.category = category;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
