package com.mkt.plan4workout.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.mkt.plan4workout.R;
import com.mkt.plan4workout.Utils.RepsAndKgEditText;
import com.mkt.plan4workout.ViewModel.DoViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoEditWorkoutExercise extends AppCompatActivity {
    public static final String EXTRA_NAME = "com.mkt.plan4workout.EXTRA_NAME";
    public static final String EXTRA_SERIES = "com.mkt.plan4workout.EXTRA_SERIES";
    public static final String EXTRA_REPS = "com.mkt.plan4workout.EXTRA_REPS";
    public static final String EXTRA_KG = "com.mkt.plan4workout.EXTRA_KG";
    public static final String EXTRA_EXERCISE_ID = "com.mkt.plan4workout.EXTRA_EXERCISE_ID";
    public static final String EXTRA_WORKOUT_ID = "com.mkt.plan4workout.EXTRA_WORKOUT_ID";
    public static final String EXTRA_IDS = "com.mkt.plan4workout.EXTRA_IDS";

    TextView tvExerciseName;
    LinearLayout linearLayout;
    LinearLayout lSeriesNumber;
    LinearLayout.LayoutParams paramsInner;
    ScrollView.LayoutParams params;
    Button btnMinus;
    Button btnPlus;
    TextView tvSeries;

    DoViewModel doViewModel;

    boolean isEditingData = false;
    int countOfSeries = 0;
    int numberChoosenSeries = 1;
    List<RepsAndKgEditText> series = new ArrayList<>();
    List<LinearLayout> serie = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_edit_exercise);

        linearLayout = findViewById(R.id.series_details);
        tvExerciseName = findViewById(R.id.tv_exercise_name);
        btnMinus = findViewById(R.id.btn_minus);
        btnPlus = findViewById(R.id.btn_plus);
        tvSeries = findViewById(R.id.tv_series_count);
        lSeriesNumber = findViewById(R.id.series_number);
        doViewModel = ViewModelProviders.of(this).get(DoViewModel.class);

        tvSeries.setText("" + numberChoosenSeries);
        setParams();
        getExtras();

        tvExerciseName.setText(doViewModel.getExerciseName());
        setSeriesPicker(1, false);

        if (doViewModel.getKgs() == null) {
            btnMinus.setOnClickListener(v -> {
                if (numberChoosenSeries > 0) numberChoosenSeries--;
                tvSeries.setText("" + numberChoosenSeries);
                setSeriesPicker(numberChoosenSeries, false);
            });
            btnPlus.setOnClickListener(v -> {
                if (numberChoosenSeries < 9) numberChoosenSeries++;
                tvSeries.setText("" + numberChoosenSeries);
                setSeriesPicker(numberChoosenSeries, false);
            });
        } else {
            tvSeries.setVisibility(View.GONE);
            btnMinus.setVisibility(View.GONE);
            btnPlus.setVisibility(View.GONE);
            setSeriesPicker(0, true);
        }

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }

    private void saveExercise() {
        String title = tvExerciseName.getText().toString();

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, title);
        data.putExtra(EXTRA_SERIES, numberChoosenSeries);

        String[] ids = new String[doViewModel.getIds().size()];
        int o = 0;
        for (String s : doViewModel.getIds()) {
            ids[o] = s;
            System.out.println("O: " + ids[o]);
            o++;
        }
        data.putExtra(EXTRA_IDS, ids);

        boolean isValid = true;

        if (isEditingData) countOfSeries = doViewModel.getKgs().size();
        String[] reps = new String[countOfSeries];
        String[] kg = new String[countOfSeries];
        for (int p = 0; p < countOfSeries; p++) {
            reps[p] = series.get(p).getReps().getText().toString();
            kg[p] = series.get(p).getKg().getText().toString();
            if (reps[p].isEmpty() || kg[p].isEmpty() || !TextUtils.isDigitsOnly(reps[p]) || !TextUtils.isDigitsOnly(kg[p])) {
                isValid = false;
            }

        }

        if (isValid) {

            data.putExtra(EXTRA_REPS, reps);
            data.putExtra(EXTRA_KG, kg);
            data.putExtra(EXTRA_SERIES, countOfSeries);
            data.putExtra(EXTRA_EXERCISE_ID, doViewModel.getExerciseId());
            data.putExtra(EXTRA_WORKOUT_ID, doViewModel.getWorkoutId());

            setResult(RESULT_OK, data);
            finish();
        } else {
            Toast.makeText(this, "Please insert all valid details (kg, reps)", Toast.LENGTH_SHORT).show();
        }
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

    private void setSeriesPicker(int i2, boolean isEditing) {
        if (isEditing == false) {
            countOfSeries = i2;
            series.removeAll(series);
            serie.removeAll(serie);
            linearLayout.removeAllViews();
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

                linearLayout.addView(serie.get(j - 1));
            }
        } else {
            lSeriesNumber.setVisibility(View.GONE);
            series.removeAll(series);
            serie.removeAll(serie);
            linearLayout.removeAllViews();
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

                linearLayout.addView(serie.get(j - 1));
            }
        }
    }

    private void setParams() {
        params = new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,
                ScrollView.LayoutParams.WRAP_CONTENT
        );
        paramsInner = new LinearLayout.LayoutParams(
                400,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayout.setLayoutParams(params);

    }

    private void getExtras() {
        doViewModel.setExerciseId(getIntent().getExtras().getInt(EXTRA_EXERCISE_ID));
        doViewModel.setWorkoutId(getIntent().getExtras().getInt(EXTRA_WORKOUT_ID));
        doViewModel.setExerciseName(getIntent().getExtras().getString(EXTRA_NAME));

        if (getIntent().getStringArrayExtra(EXTRA_KG) != null) {
            List<String> kgsList = Arrays.asList(getIntent().getStringArrayExtra(EXTRA_KG));
            doViewModel.setKgs(kgsList);
        }
        if (getIntent().getStringArrayExtra(EXTRA_REPS) != null) {
            List<String> repsList = Arrays.asList(getIntent().getStringArrayExtra(EXTRA_REPS));
            doViewModel.setReps(repsList);
        }
        if (getIntent().getStringArrayExtra(EXTRA_IDS) != null) {
            List<String> idsList = Arrays.asList(getIntent().getStringArrayExtra(EXTRA_IDS));
            doViewModel.setIds(idsList);
        }
    }

}
