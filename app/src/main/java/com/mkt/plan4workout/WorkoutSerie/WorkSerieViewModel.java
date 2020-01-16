package com.mkt.plan4workout.WorkoutSerie;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.Workout.Workout;
import com.mkt.plan4workout.Workout.WorkoutRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WorkSerieViewModel extends AndroidViewModel {
    private WorkoutSerieRepository repository;
    private LiveData<List<WorkoutSerie>> allWorkoutSeries;

    public WorkSerieViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutSerieRepository(application);
        allWorkoutSeries = repository.getAllWorkoutSeries();
    }

    public Long insert(WorkoutSerie workout) throws ExecutionException, InterruptedException {
        return repository.insert(workout);
    }

    public void update(WorkoutSerie workout) {
        repository.update(workout);
    }

    public void delete(WorkoutSerie workout) {
        repository.delete(workout);
    }

    public void deleteAllWorkoutSeries() {
        repository.deleteAllWorkoutSeries();
    }

    public LiveData<List<WorkoutSerie>> getAllWorkouts() {
        return allWorkoutSeries;
    }

    public List<WorkoutSerie> getWorkoutSeries(int id) throws ExecutionException, InterruptedException {
        return repository.getWorkoutSeries(id);
    }

}

