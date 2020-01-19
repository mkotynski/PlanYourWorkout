package com.mkt.plan4workout.Model.Plan;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlanDao {

    @Insert
    long insert(Plan plan);

    @Update
    void update(Plan plan);

    @Delete
    void delete(Plan plan);

    @Query("DELETE FROM plan_table")
    void deleteAllPlans();

    @Query("SELECT * FROM plan_table ORDER BY id DESC")
    LiveData<List<Plan>> getAllPlans();

    @Query("SELECT * FROM plan_table WHERE id = :idOfPlan")
    LiveData<Plan> getPlan(int idOfPlan);

    @Query("SELECT * FROM plan_table WHERE id = :idOfPlan")
    Plan getxPlan(int idOfPlan);
}
