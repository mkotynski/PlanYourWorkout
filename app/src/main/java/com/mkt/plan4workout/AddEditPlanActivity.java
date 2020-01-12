package com.mkt.plan4workout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.mkt.plan4workout.Exercise.Exercise;
import com.mkt.plan4workout.Plan.Plan;

import java.util.List;

public class AddEditPlanActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.mkt.plan4workout.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.mkt.plan4workout.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.mkt.plan4workout.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "com.mkt.plan4workout.EXTRA_ID";

    public static final int ADD_EXERCISES_REQUEST = 1;

    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    private Button btnAddExercises;
    private TextView textViewChoosenExercises;
//    AddExercisesToPlanViewModel addExercisesToPlanViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);
        btnAddExercises = findViewById(R.id.button_add_exercises2plan);
        textViewChoosenExercises = findViewById(R.id.text_view_choosen_exercises);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(9);


//        addExercisesToPlanViewModel = ViewModelProviders.of(this).get(AddExercisesToPlanViewModel.class);
//        addExercisesToPlanViewModel.addExercise(new Exercise("name","cateogry", "type", "description"));
//        addExercisesToPlanViewModel.getCurrentExercises().setValue(addExercisesToPlanViewModel.getChoosenExercises());
//        addExercisesToPlanViewModel.getCurrentExercises().observe(this, new Observer<List<Exercise>>() {
//            String namesOfExercises = ""; //"You haven't choosen anything yet";
//            @Override
//            public void onChanged(List<Exercise> exercises) {
//                for (Exercise e : exercises) {
//                    namesOfExercises += namesOfExercises + e.getName();
//                }
//                System.out.println("GOT EXERCISES");
//                textViewChoosenExercises.setText(namesOfExercises);
//            }
//        });

        btnAddExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEditPlanActivity.this, AddExerciseToPlan.class);
                Bundle b = new Bundle();
//                b.putString("name", etName.getText().toString());
//                i.putExtras(b);
                startActivityForResult(intent, ADD_EXERCISES_REQUEST);
            }
        });


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle("Add note");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String namesOfExercises = "You haven't choosen anything yet";
        if (requestCode == ADD_EXERCISES_REQUEST && resultCode == RESULT_OK) {
            String exercises = data.getStringExtra(AddExerciseToPlan.EXTRA_EXERCISES);
            String exercisesId = data.getStringExtra(AddExerciseToPlan.EXTRA_EXERCISES_ID);

//            for (Exercise e : addExercisesToPlanViewModel.getChoosenExercises()) {
//                namesOfExercises += namesOfExercises + e.getName();
//            }

            if(exercises!=""){
                textViewChoosenExercises.setText(exercises +" "+ exercisesId);
            }
            Toast.makeText(this, "Exercises added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Plan not saved", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

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
