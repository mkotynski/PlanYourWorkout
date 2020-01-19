package com.mkt.plan4workout.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.Model.Workout.Workout;
import com.mkt.plan4workout.Model.Workout.WorkoutRepository;

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

    public LiveData<Workout> getWorkoutByDateLD(String date) throws ExecutionException, InterruptedException {
        return repository.getWorkoutByDateLD(date);
    }

    public Workout getWorkoutByDate(String date) throws ExecutionException, InterruptedException {
        return repository.getWorkoutByDate(date);
    }

    public List<Workout> getWorkoutsByPlan(int i) throws ExecutionException, InterruptedException {
        return repository.getWorkoutsByPlan(i);
    }
}
