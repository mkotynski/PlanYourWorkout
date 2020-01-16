package com.mkt.plan4workout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditExerciseActivity extends AppCompatActivity {
    public static final String EXTRA_EXERCISE_NAME = "com.mkt.plan4workout.EXTRA_EXERCISE_NAME";
    public static final String EXTRA_EXERCISE_CATEGORY = "com.mkt.plan4workout.EXTRA_EXERCISE_CATEGORY";
    public static final String EXTRA_EXERCISE_TYPE = "com.mkt.plan4workout.EXTRA_EXERCISE_TYPE";
    public static final String EXTRA_EXERCISE_DESCRIPTION = "com.mkt.plan4workout.EXTRA_EXERCISE_DESCRIPTION";
    public static final String EXTRA_ID = "com.mkt.plan4workout.EXTRA_ID";


    private EditText etExerciseName;
    private EditText etExerciseCategory;
    private EditText etExerciseType;
    private EditText etExerciseDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        etExerciseName = findViewById(R.id.et_name);
        etExerciseCategory = findViewById(R.id.et_category);
        etExerciseType = findViewById(R.id.et_type);
        etExerciseDescription = findViewById(R.id.et_description);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit exercise");
            etExerciseName.setText(intent.getStringExtra(EXTRA_EXERCISE_NAME));
            etExerciseCategory.setText(intent.getStringExtra(EXTRA_EXERCISE_CATEGORY));
            etExerciseType.setText(intent.getStringExtra(EXTRA_EXERCISE_TYPE));
            etExerciseDescription.setText(intent.getStringExtra(EXTRA_EXERCISE_DESCRIPTION));
        } else {
            setTitle("Add exercise");
        }
    }

    private void saveExercise() {
        String exerciseName = etExerciseName.getText().toString();
        String exerciseCategory = etExerciseCategory.getText().toString();
        String exerciseType = etExerciseType.getText().toString();
        String exerciseDescription = etExerciseDescription.getText().toString();

        if (exerciseName.trim().isEmpty() ||exerciseCategory.trim().isEmpty() || exerciseType.trim().isEmpty() || exerciseDescription.isEmpty()) {
            Toast.makeText(this, "Please insert a name, category and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_EXERCISE_NAME, exerciseName);
        data.putExtra(EXTRA_EXERCISE_CATEGORY, exerciseCategory);
        data.putExtra(EXTRA_EXERCISE_TYPE, exerciseType);
        data.putExtra(EXTRA_EXERCISE_DESCRIPTION, exerciseDescription);

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
