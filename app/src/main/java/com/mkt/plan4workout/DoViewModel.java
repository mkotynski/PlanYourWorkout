package com.mkt.plan4workout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class DoViewModel extends AndroidViewModel {

    private int workoutId;
    private int exerciseId;
    private String exerciseName;

    public DoViewModel(@NonNull Application application) {
        super(application);
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        if(exerciseId != null) this.exerciseId = Integer.valueOf(exerciseId);
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        if(workoutId != null) this.workoutId = Integer.valueOf(workoutId);
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        if(exerciseName != null) this.exerciseName = exerciseName;
    }
}
