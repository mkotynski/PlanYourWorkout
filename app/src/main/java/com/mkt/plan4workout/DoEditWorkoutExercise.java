package com.mkt.plan4workout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.mkt.plan4workout.utils.RepsAndKgEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoEditWorkoutExercise extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.mkt.plan4workout.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.mkt.plan4workout.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.mkt.plan4workout.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "com.mkt.plan4workout.EXTRA_ID";


    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText etNumberSeries;
    private NumberPicker numberPickerPriority;

    private int countOfSeries = 0;
    private List<RepsAndKgEditText> series = new ArrayList<>();
    private List<LinearLayout> serie = new ArrayList<>();

    private boolean isEditingData = false;
    DoViewModel doViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_edit_exercise);
        LinearLayout mRlayout = (LinearLayout) findViewById(R.id.series_details);

        doViewModel = ViewModelProviders.of(this).get(DoViewModel.class);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        //etNumberSeries = findViewById(R.id.edit_number_series);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        doViewModel.setExerciseId(getIntent().getExtras().getInt("exercise_id"));
        doViewModel.setWorkoutId(getIntent().getExtras().getInt("do_workout_id"));
        doViewModel.setExerciseName(getIntent().getExtras().getString("exercise_name"));

        if (getIntent().getStringArrayExtra("kgs") != null) {
            List<String> kgsList = Arrays.asList(getIntent().getStringArrayExtra("kgs"));
            doViewModel.setKgs(kgsList);
        }
        if (getIntent().getStringArrayExtra("reps") != null) {
            List<String> repsList = Arrays.asList(getIntent().getStringArrayExtra("reps"));
            doViewModel.setReps(repsList);
        }
        if (getIntent().getStringArrayExtra("ids") != null) {
            List<String> idsList = Arrays.asList(getIntent().getStringArrayExtra("ids"));
            doViewModel.setIds(idsList);
        }
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(9);
        numberPickerPriority.setValue(4);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams paramsInner = new LinearLayout.LayoutParams(
                400,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        mRlayout.setLayoutParams(params);

        if (doViewModel.getKgs() == null) {
            numberPickerPriority.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                    countOfSeries = i2;
                    System.out.println("i:" + i + "; i2:" + i2);
                    series.removeAll(series);
                    serie.removeAll(serie);
                    mRlayout.removeAllViews();
                    for (int j = 1; j <= i2; j++) {
                        serie.add(new LinearLayout(DoEditWorkoutExercise.this));
                        series.add(new RepsAndKgEditText(new EditText(DoEditWorkoutExercise.this), new EditText(DoEditWorkoutExercise.this), DoEditWorkoutExercise.this));
                        series.get(j - 1).getReps().setHint("Reps");
                        series.get(j - 1).getReps().setId(j + 20);
                        series.get(j - 1).getKg().setHint("Kilograms");
                        series.get(j - 1).getKg().setId(j + 120);

                        series.get(j - 1).getReps().setLayoutParams(paramsInner);
                        series.get(j - 1).getKg().setLayoutParams(paramsInner);

                        serie.get(j - 1).addView(series.get(j - 1).getReps());
                        serie.get(j - 1).addView(series.get(j - 1).getKg());
                        serie.get(j - 1).setLayoutParams(params);

                        mRlayout.addView(serie.get(j - 1));
                    }

                }
            });
        } else {
            isEditingData = true;
            numberPickerPriority.setVisibility(View.GONE);
            series.removeAll(series);
            serie.removeAll(serie);
            mRlayout.removeAllViews();
            for (int j = 1; j <= doViewModel.getKgs().size(); j++) {
                serie.add(new LinearLayout(DoEditWorkoutExercise.this));
                series.add(new RepsAndKgEditText(new EditText(DoEditWorkoutExercise.this), new EditText(DoEditWorkoutExercise.this), DoEditWorkoutExercise.this));
                series.get(j - 1).getReps().setText(doViewModel.getReps().get(j - 1));
                series.get(j - 1).getReps().setId(j + 20);
                series.get(j - 1).getKg().setText(doViewModel.getKgs().get(j - 1));
                series.get(j - 1).getKg().setId(j + 120);

                series.get(j - 1).getReps().setLayoutParams(paramsInner);
                series.get(j - 1).getKg().setLayoutParams(paramsInner);

                serie.get(j - 1).addView(series.get(j - 1).getReps());
                serie.get(j - 1).addView(series.get(j - 1).getKg());
                serie.get(j - 1).setLayoutParams(params);

                mRlayout.addView(serie.get(j - 1));
            }
        }

        getSupportActionBar().

                setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra("do_workout_id")) {
            setTitle("Edit workout exercise");
            editTextTitle.setText(intent.getStringExtra("exercise_name"));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle("Set workout exercise");
        }

    }

    private void saveExercise() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

//        if (title.trim().isEmpty() || description.trim().isEmpty()) {
//            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        if (isEditingData) countOfSeries = doViewModel.getKgs().size();
        String[] reps = new String[countOfSeries];
        String[] kg = new String[countOfSeries];
        for (int p = 0; p < countOfSeries; p++) {
            System.out.println(series.get(p).getReps().getText().toString());
            System.out.println(series.get(p).getKg().getText().toString());
            reps[p] = series.get(p).getReps().getText().toString();
            kg[p] = series.get(p).getKg().getText().toString();
        }

        if(doViewModel.getIds() != null) data.putExtra("ids", doViewModel.getIds().toArray(new String[0]));
        data.putExtra("reps", reps);
        data.putExtra("kg", kg);
        data.putExtra("series", countOfSeries);
        data.putExtra("do_workout_id", doViewModel.getWorkoutId());
        data.putExtra("exercise_id", doViewModel.getExerciseId());

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_exercise_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_exercise:
                saveExercise();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
