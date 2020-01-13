package com.mkt.plan4workout.DoWorkout;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.Workout.Workout;
import com.mkt.plan4workout.Workout.WorkoutRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DoWorkoutViewModel extends AndroidViewModel {
    private DoWorkoutRepository repository;
    private LiveData<List<DoWorkout>> allDoWorkouts;

    public DoWorkoutViewModel(@NonNull Application application) {
        super(application);
        repository = new DoWorkoutRepository(application);
        allDoWorkouts = repository.getAllDoWorkouts();
    }

    public Long insert(DoWorkout doWorkout) throws ExecutionException, InterruptedException {
        return repository.insert(doWorkout);
    }

    public void update(DoWorkout doWorkout) {
        repository.update(doWorkout);
    }

    public void delete(DoWorkout doWorkout) {
        repository.delete(doWorkout);
    }

    public void deleteAllWorkouts() {
        repository.deleteAllWorkouts();
    }

    public LiveData<List<DoWorkout>> getAllWorkouts() {
        return allDoWorkouts;
    }
//
//    public Workout getWorkout(int id) throws ExecutionException, InterruptedException {
//        return repository.getPlan(id);
//    }
//
//    public Workout getWorkoutByDate(String date) throws ExecutionException, InterruptedException {
//        return repository.getPlanByDate(date);
//    }
}
