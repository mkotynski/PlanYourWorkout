package com.mkt.plan4workout.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;

public class PlanActivityViewModel extends AndroidViewModel {
    private String pickExercises = "";
    private List<Integer> listPickExercises = new ArrayList<>();

    public PlanActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public String getPickExercises() {
        return pickExercises;
    }

    public void setPickExercises(String pickExercises) {
        this.pickExercises = pickExercises;
    }



}
