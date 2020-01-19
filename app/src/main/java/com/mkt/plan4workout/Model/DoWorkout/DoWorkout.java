package com.mkt.plan4workout.Model.DoWorkout;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "do_workout_table")
public class DoWorkout {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int workoutId;

    private int exerciseId;

    private int series;

    public DoWorkout(int workoutId, int exerciseId, int series) {
        this.workoutId = workoutId;
        this.exerciseId = exerciseId;
        this.series = series;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public int getSeries() {
        return series;
    }

}
