package com.mkt.plan4workout.Model.Exercise;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Insert
    void insert(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Query("SELECT * FROM exercise_table")
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * FROM exercise_table WHERE archive = 0")
    LiveData<List<Exercise>> getAllExercisesA();

    @Query("DELETE FROM exercise_table")
    void deleteAllExercises();

    @Query("UPDATE exercise_table SET archive = 1 WHERE id = :id")
    void archive(int id);

    @Query("SELECT * FROM exercise_table WHERE id in (SELECT exerciseId FROM exercise_2_plan WHERE planId = :idOfPlan)")
    List<Exercise> getPlanExercises(int idOfPlan);
}
