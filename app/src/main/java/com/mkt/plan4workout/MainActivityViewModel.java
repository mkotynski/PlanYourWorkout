package com.mkt.plan4workout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mkt.plan4workout.ExerciseToPlan.ExerciseToPlan;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private String pickExercises = "";
    private List<Integer> listPickExercises = new ArrayList<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public String getPickExercises() {
        return pickExercises;
    }

    public void setPickExercises(String pickExercises) {
        this.pickExercises = pickExercises;
    }

    public void makePickExercises() {
        String[] data = pickExercises.split(",");
        List<Integer> ret = new ArrayList<>();
        if (pickExercises.length() > 0) {
            for (String id : data) {
                ret.add(Integer.valueOf(id));
            }
        }

        listPickExercises = ret;
    }

    public String makeStringFromList(List<ExerciseToPlan> exerciseToPlans) {
        String ret = "";

        for (ExerciseToPlan exerciseToPlan : exerciseToPlans){
            ret+=exerciseToPlan.getExerciseId();
            ret+=",";
        }

        return ret;
    }

    public List<Integer> getListPickExercises() {
        return listPickExercises;
    }
}
