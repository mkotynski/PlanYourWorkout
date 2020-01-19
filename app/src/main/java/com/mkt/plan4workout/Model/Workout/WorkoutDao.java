package com.mkt.plan4workout.Model.Workout;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WorkoutDao {

    @Insert
    long insert(Workout workout);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);

    @Query("DELETE FROM workout_table")
    void deleteAllWorkouts();

    @Query("SELECT * FROM workout_table ORDER BY id DESC")
    LiveData<List<Workout>> getAllWorkouts();

    @Query("SELECT * FROM workout_table WHERE id = :idWorkout")
    Workout getWorkout(int idWorkout);

    @Query("SELECT * FROM workout_table WHERE date = :date")
    Workout getWorkoutByDate(String date);

    @Query("SELECT * FROM workout_table WHERE date = :date")
    LiveData<Workout> getWorkoutByDateLD(String date);

    @Query("SELECT * FROM workout_table WHERE idOfPlan = :planId")
    List<Workout> getWorkoutsByPlan(int planId);
}
