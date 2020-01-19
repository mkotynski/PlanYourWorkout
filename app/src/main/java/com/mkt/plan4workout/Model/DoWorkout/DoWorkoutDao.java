package com.mkt.plan4workout.Model.DoWorkout;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DoWorkoutDao {

    @Insert
    long insert(DoWorkout doWorkout);

    @Update
    void update(DoWorkout doWorkout);

    @Delete
    void delete(DoWorkout doWorkout);

    @Query("DELETE FROM do_workout_table")
    void deleteAllDoWorkouts();

    @Query("SELECT * FROM do_workout_table ORDER BY id DESC")
    LiveData<List<DoWorkout>> getAllDoWorkouts();

    @Query("SELECT * FROM do_workout_table WHERE id = :idWorkout")
    LiveData<List<DoWorkout>> getDoWorkouts(int idWorkout);

    @Query("SELECT * FROM do_workout_table WHERE exerciseId = :exerciseId")
    List<DoWorkout> getDoWorkoutsByExercise(int exerciseId);

}
