package com.mkt.plan4workout.DoWorkout;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "do_workout_table")
public class DoWorkout {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int workoutId;

    private int exerciseId;

    public DoWorkout(int workoutId, int exerciseId) {
        this.workoutId = workoutId;
        this.exerciseId = exerciseId;
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
}
