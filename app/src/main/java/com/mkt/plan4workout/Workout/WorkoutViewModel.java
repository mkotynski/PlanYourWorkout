package com.mkt.plan4workout.Workout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.Plan.Plan;
import com.mkt.plan4workout.Plan.PlanRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WorkoutViewModel extends AndroidViewModel {
    private WorkoutRepository repository;
    private LiveData<List<Workout>> allWorkouts;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
        allWorkouts = repository.getAllWorkouts();
    }

    public Long insert(Workout workout) throws ExecutionException, InterruptedException {
        return repository.insert(workout);
    }

    public void update(Workout workout) {
        repository.update(workout);
    }

    public void delete(Workout workout) {
        repository.delete(workout);
    }

    public void deleteAllWorkouts() {
        repository.deleteAllWorkouts();
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return allWorkouts;
    }

    public Workout getWorkout(int id) throws ExecutionException, InterruptedException {
        return repository.getPlan(id);
    }

    public Workout getWorkoutByDate(String date) throws ExecutionException, InterruptedException {
        return repository.getPlanByDate(date);
    }
}
