package com.mkt.plan4workout.WorkoutSerie;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_serie")
public class WorkoutSerie {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int workoutId;

    private int exerciseId;

    private int reps;

    private int kg;

    public WorkoutSerie(int workoutId, int exerciseId, int reps, int kg) {
        this.workoutId = workoutId;
        this.exerciseId = exerciseId;
        this.reps = reps;
        this.kg = kg;
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

    public int getReps() {
        return reps;
    }

    public int getKg() {
        return kg;
    }
}
