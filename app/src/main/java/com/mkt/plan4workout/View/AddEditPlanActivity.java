package com.mkt.plan4workout.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mkt.plan4workout.R;
import com.mkt.plan4workout.ViewModel.PlanActivityViewModel;

public class AddEditPlanActivity extends AppCompatActivity {
    public static final String EXTRA_PLAN_NAME = "com.mkt.plan4workout.EXTRA_PLAN_NAME";
    public static final String EXTRA_PLAN_CATEGORY = "com.mkt.plan4workout.EXTRA_PLAN_CATEGORY";
    public static final String EXTRA_PLAN_DESCRIPTION = "com.mkt.plan4workout.EXTRA_PLAN_DESCRIPTION";
    public static final String EXTRA_ID = "com.mkt.plan4workout.EXTRA_ID";
    public static final String EXTRA_EXERCISES = "com.mkt.plan4workout.EXTRA_EXERCISES";

    public static final int ADD_EXERCISES_REQUEST = 1;

    private EditText etPlanName;
    private EditText etPlanCategory;
    private EditText etPlanDescription;


    private Button btnAddExercises;
    private TextView textViewChoosenExercises;

    PlanActivityViewModel planViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        etPlanName = findViewById(R.id.et_name);
        etPlanCategory = findViewById(R.id.et_category);
        etPlanDescription = findViewById(R.id.et_description);
        btnAddExercises = findViewById(R.id.button_add_exercises2plan);
        textViewChoosenExercises = findViewById(R.id.text_view_choosen_exercises);


        planViewModel = ViewModelProviders.of(this).get(PlanActivityViewModel.class);


        btnAddExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEditPlanActivity.this, AddExerciseToPlan.class);
                intent.putExtra(EXTRA_EXERCISES, planViewModel.getPickExercises());
                startActivityForResult(intent, ADD_EXERCISES_REQUEST);
            }
        });


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit plan");
            etPlanName.setText(intent.getStringExtra(EXTRA_PLAN_NAME));
            etPlanCategory.setText(intent.getStringExtra(EXTRA_PLAN_CATEGORY));
            etPlanDescription.setText(intent.getStringExtra(EXTRA_PLAN_DESCRIPTION));
            planViewModel.setPickExercises(intent.getStringExtra(EXTRA_EXERCISES));
            textViewChoosenExercises.setText(planViewModel.getPickExercises());
        } else {
            setTitle("Add plan");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EXERCISES_REQUEST && resultCode == RESULT_OK) {
            String exercisesId = data.getStringExtra(AddExerciseToPlan.EXTRA_EXERCISES_ID);
            if (!exercisesId.isEmpty()) {
                textViewChoosenExercises.setText(exercisesId);
                planViewModel.setPickExercises(exercisesId);
            }
            Toast.makeText(this, "Exercises added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Plan not saved", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveNote() {
        String planName = etPlanName.getText().toString();
        String planCategory = etPlanCategory.getText().toString();
        String planDescription = etPlanDescription.getText().toString();

        if (planName.trim().isEmpty() || planCategory.trim().isEmpty()|| planDescription.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a name, category and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_PLAN_NAME, planName);
        data.putExtra(EXTRA_PLAN_CATEGORY, planCategory);
        data.putExtra(EXTRA_PLAN_DESCRIPTION, planDescription);
        data.putExtra(EXTRA_EXERCISES, planViewModel.getPickExercises());

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
        menuInflater.inflate(R.menu.add_plan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_plan:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
