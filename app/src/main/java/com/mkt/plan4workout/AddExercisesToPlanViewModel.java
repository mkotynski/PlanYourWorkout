package com.mkt.plan4workout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mkt.plan4workout.Exercise.Exercise;

import java.util.ArrayList;
import java.util.List;

public class AddExercisesToPlanViewModel extends AndroidViewModel {
    List<Exercise> choosenExercises = new ArrayList<>();

    public AddExercisesToPlanViewModel(@NonNull Application application) {
        super(application);
    }

    public void addExercise(Exercise exercise) {
        choosenExercises.add(exercise);
    }

    public void delExercise(Exercise exercise) {
        choosenExercises.remove(exercise);
    }

    public boolean isChoosen(Exercise exercise) {
        return choosenExercises.contains(exercise);
    }

    public List<Exercise> getChoosenExercises() {
        return choosenExercises;
    }

}
