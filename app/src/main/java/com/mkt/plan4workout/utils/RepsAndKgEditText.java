package com.mkt.plan4workout.utils;

import android.content.Context;
import android.widget.EditText;

public class RepsAndKgEditText {
    EditText reps;
    EditText kg;
    Context context;

    public RepsAndKgEditText(EditText reps, EditText kg, Context context) {
        this.reps = reps;
        this.kg = kg;
        this.context = context;
        reps = new EditText(context);
        kg = new EditText(context);

    }

    public EditText getReps() {
        return reps;
    }

    public void setReps(EditText reps) {
        this.reps = reps;
    }

    public EditText getKg() {
        return kg;
    }

    public void setKg(EditText kg) {
        this.kg = kg;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
