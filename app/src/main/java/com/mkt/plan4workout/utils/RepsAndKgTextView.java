package com.mkt.plan4workout.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

public class RepsAndKgTextView {
    TextView reps;
    TextView kg;

    public RepsAndKgTextView(TextView reps, TextView kg) {
        this.reps = reps;
        this.kg = kg;
    }

    public TextView getReps() {
        return reps;
    }

    public void setReps(TextView reps) {
        this.reps = reps;
    }

    public TextView getKg() {
        return kg;
    }

    public void setKg(TextView kg) {
        this.kg = kg;
    }

}
