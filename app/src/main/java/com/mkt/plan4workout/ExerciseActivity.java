package com.mkt.plan4workout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mkt.plan4workout.DoWorkout.DoWorkout;
import com.mkt.plan4workout.Exercise.Exercise;
import com.mkt.plan4workout.Exercise.ExerciseAdapter;
import com.mkt.plan4workout.Exercise.ExerciseViewModel;
import com.mkt.plan4workout.Plan.Plan;
import com.mkt.plan4workout.Plan.PlanAdapter;
import com.mkt.plan4workout.Plan.PlanViewModel;

import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    public static final int ADD_EXERCISE_REQUEST = 1;
    public static final int EDIT_EXERCISE_REQUEST = 2;

    ExerciseViewModel exerciseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        FloatingActionButton buttonAddExercise = findViewById(R.id.button_add_exercise);
        buttonAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExerciseActivity.this, AddEditExerciseActivity.class);
                startActivityForResult(intent, ADD_EXERCISE_REQUEST);
            }
        });
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExerciseAdapter adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);

        exerciseViewModel.getAllExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                adapter.setExercises(exercises);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // drag and drop functionality
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                exerciseViewModel.delete(adapter.getExerciseAt(viewHolder.getAdapterPosition()));
                Toast.makeText(ExerciseActivity.this, "Exercise deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new ExerciseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Exercise exercise) {
                Intent intent = new Intent(ExerciseActivity.this, AddEditExerciseActivity.class);
                intent.putExtra(AddEditExerciseActivity.EXTRA_ID, exercise.getId());
                intent.putExtra(AddEditExerciseActivity.EXTRA_EXERCISE_NAME, exercise.getName());
                intent.putExtra(AddEditExerciseActivity.EXTRA_EXERCISE_CATEGORY, exercise.getCategory());
                intent.putExtra(AddEditExerciseActivity.EXTRA_EXERCISE_TYPE, exercise.getType());
                intent.putExtra(AddEditExerciseActivity.EXTRA_EXERCISE_DESCRIPTION, exercise.getDescription());
                startActivityForResult(intent, EDIT_EXERCISE_REQUEST);
            }

            @Override
            public void onItemViewClick(View itemView, Exercise exercise) {
            }

            @Override
            public void onDoWorkoutClick(View itemView, Exercise exercise, DoWorkout doWorkout) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String exerciseName = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_NAME);
        String exerciseCategory = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_CATEGORY);
        String exerciseType = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_TYPE);
        String exerciseDescription = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_DESCRIPTION);

        if (requestCode == ADD_EXERCISE_REQUEST && resultCode == RESULT_OK) {
            Exercise exercise = new Exercise(exerciseName, exerciseCategory, exerciseType, exerciseDescription);
            exerciseViewModel.insert(exercise);

            Toast.makeText(this, "Exercise saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_EXERCISE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditExerciseActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Exercise can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            Exercise exercise = new Exercise(exerciseName, exerciseCategory, exerciseType, exerciseDescription);
            exercise.setId(id);
            exerciseViewModel.update(exercise);

            Toast.makeText(this, "Exercise updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Exercise not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_exercises:
                exerciseViewModel.deleteAllExercises();
                Toast.makeText(this, "All exercises deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
