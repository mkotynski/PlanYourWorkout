package com.mkt.plan4workout.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class DoViewModel extends AndroidViewModel {

    private int workoutId;
    private int exerciseId;
    private String exerciseName;
    private List<String> kgs;
    private List<String> reps;
    private List<String> ids;
    private boolean isEditing = false;

    public DoViewModel(@NonNull Application application) {
        super(application);
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exercise_id) {
        if(exerciseId != -1) this.exerciseId = exercise_id;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        if(workoutId != -1) this.workoutId = workoutId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        if(exerciseName != null) this.exerciseName = exerciseName;
    }

    public List<String> getKgs() {
        return kgs;
    }

    public void setKgs(List<String> kgs) {
        this.kgs = kgs;
    }

    public List<String> getReps() {
        return reps;
    }

    public void setReps(List<String> reps) {
        this.reps = reps;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
}
