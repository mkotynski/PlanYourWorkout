package com.mkt.plan4workout.Model.Exercise;

public class ExercisePick {
    Exercise exercise;
    Boolean picked;

    public ExercisePick(Exercise exercise, Boolean picked) {
        this.exercise = exercise;
        this.picked = picked;
    }

    public Boolean getPicked() {
        return picked;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setPicked(Boolean picked) {
        this.picked = picked;
    }
}
