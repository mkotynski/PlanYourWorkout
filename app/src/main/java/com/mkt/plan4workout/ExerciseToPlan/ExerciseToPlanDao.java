package com.mkt.plan4workout.ExerciseToPlan;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mkt.plan4workout.Exercise.Exercise;

import java.util.List;

@Dao
public interface ExerciseToPlanDao {

    @Insert
    void insert(ExerciseToPlan e2p);

    @Update
    void update(ExerciseToPlan e2p);

    @Delete
    void delete(ExerciseToPlan e2p);

    @Query("SELECT * FROM exercise_2_plan")
    LiveData<List<ExerciseToPlan>> getAlle2p();

    @Query("DELETE FROM exercise_2_plan")
    void deleteAlle2p();
}
