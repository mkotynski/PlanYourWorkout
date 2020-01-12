package com.mkt.plan4workout;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mkt.plan4workout.Exercise.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExercisePickViewModel extends AndroidViewModel {
    private List<Exercise> allExercisesPick = new ArrayList<>();

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

    public List<Exercise> getAllPickedExercises() {
        return allExercisesPick;
    }
}
