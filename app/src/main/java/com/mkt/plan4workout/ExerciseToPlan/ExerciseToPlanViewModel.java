package com.mkt.plan4workout.ExerciseToPlan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExerciseToPlanViewModel extends AndroidViewModel {
    private ExerciseToPlanRepository repository;
    private LiveData<List<ExerciseToPlan>> allE2Ps;

    public ExerciseToPlanViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseToPlanRepository(application);
        allE2Ps = repository.getAllE2p();
    }

    public void insert(ExerciseToPlan e2p) {
        repository.insert(e2p);
    }

    public void update(ExerciseToPlan e2p) {
        repository.update(e2p);
    }

    public void delete(ExerciseToPlan e2p) {
        repository.delete(e2p);
    }

    public void deleteAllE2Ps() {
        repository.deleteAllE2Ps();
    }

    public void deleteExercisesOfPlan(int id) {
        repository.deleteExercisesOfPlan(id);
    }

    public List<ExerciseToPlan> getExercisesOfPlan(int id) throws ExecutionException, InterruptedException {
        return repository.getExercisesOfPlan(id);
    }

    public LiveData<List<ExerciseToPlan>> getAllE2Ps() {
        return allE2Ps;
    }
}
