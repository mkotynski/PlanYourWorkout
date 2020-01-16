package com.mkt.plan4workout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mkt.plan4workout.Exercise.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExercisePickViewModel extends AndroidViewModel {
    private List<Exercise> allExercisesPick = new ArrayList<>();
    private String exercisesList = "";
    private List<Exercise> allExercises = new ArrayList<>();

    public ExercisePickViewModel(@NonNull Application application) {
        super(application);
    }

    public void addExercise(Exercise i) {
        allExercisesPick.add(i);
    }

    public void delExercise(Exercise i) {
        allExercisesPick.remove(i);
    }

    public boolean isChoosen(Exercise i) {
        return allExercisesPick.contains(i);
    }

    public String idListToString() {
        String idList = "";
        for (Exercise exercise : allExercisesPick) {
            idList += exercise.getId();
            idList += ",";
        }

        return idList;
    }

    public String getExercisesList() {
        return exercisesList;
    }

    public void setExercisesList(String exercisesList) {
        if(exercisesList != null) {
            this.exercisesList = exercisesList;
            allExercisesPick = makeExercisesList(exercisesList);
        }
    }

    public void setAllExercises(List<Exercise> exercises){
        allExercises = exercises;
    }

    private List<Exercise> makeExercisesList(String exercisesList) {
        String[] data = exercisesList.split(",");
        List<Exercise> ret = new ArrayList<>();
        if(exercisesList.length() > 0) {
            for (String id : data) {
                for(Exercise e: allExercises){
                    if(Integer.valueOf(id) == e.getId()){
                        ret.add(e);
                    }
                }
            }
        }
        return ret;
    }

    public List<Integer> getExercisesInteger() {
        String[] data = exercisesList.split(",");
        List<Integer> ret = new ArrayList<>();
        if(exercisesList.length() > 0) {
            for (String id : data) {
                ret.add(Integer.valueOf(id));
            }
        }
        return ret;
    }

    public List<Exercise> getAllPickedExercises() {
        return allExercisesPick;
    }
}
