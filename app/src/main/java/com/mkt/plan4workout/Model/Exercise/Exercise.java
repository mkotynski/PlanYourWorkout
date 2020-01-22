package com.mkt.plan4workout.Model.Exercise;

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

    private int archive;

    public Exercise(String name, String category, String type, String description, int archive) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.description = description;
        this.archive = archive;
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

    public int getArchive() {
        return archive;
    }
}
