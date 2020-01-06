package com.mkt.plan4workout.ExerciseToPlan;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.mkt.plan4workout.Exercise.Exercise;
import com.mkt.plan4workout.Plan.Plan;

@Entity(tableName = "exercise_2_plan",
        foreignKeys = {
                @ForeignKey(entity = Plan.class,
                        parentColumns = "id",
                        childColumns = "planId"),
                @ForeignKey(entity = Exercise.class,
                        parentColumns = "id",
                        childColumns = "exerciseId")
        })
public class ExerciseToPlan {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int planId;

    private int exerciseId;

    public ExerciseToPlan(int planId, int exerciseId) {
        this.planId = planId;
        this.exerciseId = exerciseId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getPlanId() {
        return planId;
    }

    public int getExerciseId() {
        return exerciseId;
    }
}
