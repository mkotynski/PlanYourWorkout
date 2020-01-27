package com.mkt.plan4workout.Model.WorkoutSerie;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutSerieDao {
    @Insert
    long insert(WorkoutSerie workoutSerie);

    @Update
    void update(WorkoutSerie workoutSerie);

    @Delete
    void delete(WorkoutSerie workoutSerie);

    @Query("DELETE FROM workout_serie")
    void deleteAllWorkouts();

    @Query("SELECT * FROM workout_serie ORDER BY id")
    LiveData<List<WorkoutSerie>> getAllWorkoutSeries();

    @Query("SELECT * FROM workout_serie WHERE workoutId = :id")
    List<WorkoutSerie> getWorkoutSeries(int id);
}