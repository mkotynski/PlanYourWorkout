package com.mkt.plan4workout.WorkoutSerie;

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

    @Query("SELECT * FROM workout_serie ORDER BY id DESC")
    LiveData<List<WorkoutSerie>> getAllWorkoutSeries();

    @Query("SELECT * FROM workout_serie WHERE id = :id")
    WorkoutSerie getWorkout(int id);
}