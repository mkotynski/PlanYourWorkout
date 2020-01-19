package com.mkt.plan4workout.Model.Workout;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_table")
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int idOfPlan;

    private String date;

    private int done;

    public Workout(int idOfPlan, String date, int done) {
        this.idOfPlan = idOfPlan;
        this.date = date;
        this.done = done;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getIdOfPlan() {
        return idOfPlan;
    }

    public String getDate() {
        return date;
    }

    public int getDone() {
        return done;
    }
}
