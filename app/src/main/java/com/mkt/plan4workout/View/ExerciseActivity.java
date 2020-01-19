package com.mkt.plan4workout.View;

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
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mkt.plan4workout.Model.DoWorkout.DoWorkout;
import com.mkt.plan4workout.R;
import com.mkt.plan4workout.ViewModel.DoWorkoutViewModel;
import com.mkt.plan4workout.Model.Exercise.Exercise;
import com.mkt.plan4workout.View.Adapter.ExerciseAdapter;
import com.mkt.plan4workout.ViewModel.ExerciseViewModel;
import com.mkt.plan4workout.ViewModel.ExerciseToPlanViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExerciseActivity extends AppCompatActivity {

    public static final int ADD_EXERCISE_REQUEST = 1;
    public static final int EDIT_EXERCISE_REQUEST = 2;

    ExerciseViewModel exerciseViewModel;
    DoWorkoutViewModel doWorkoutViewModel;
    ExerciseToPlanViewModel exerciseToPlanViewModel;

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
        doWorkoutViewModel = ViewModelProviders.of(this).get(DoWorkoutViewModel.class);
        exerciseToPlanViewModel = ViewModelProviders.of(this).get(ExerciseToPlanViewModel.class);
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

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // drag and drop functionality
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Exercise item = adapter.getExerciseAt(position);
                boolean workoutsHere = false;
                try {
                    System.out.println(item.getName() + " " + item.getId());
                    if(doWorkoutViewModel.getDoWorkoutsByExercise(item.getId()).size() > 0) workoutsHere = true;
                    System.out.println(doWorkoutViewModel.getDoWorkoutsByExercise(item.getId()).size());
                    for(DoWorkout ex : doWorkoutViewModel.getDoWorkoutsByExercise(item.getId())){
                        System.out.println(ex.getExerciseId() +" " + ex.getWorkoutId());
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!workoutsHere) {
                    exerciseToPlanViewModel.deleteExercisesById(item.getId());
                    exerciseViewModel.delete(adapter.getExerciseAt(position));
                    adapter.removeItem(position);
                    Toast.makeText(ExerciseActivity.this, "Exercise deleted", Toast.LENGTH_SHORT).show();

                } else {
                    adapter.restoreItemNo(item,position);
                    clearView(recyclerView, viewHolder);
                    Toast.makeText(ExerciseActivity.this, "There are workouts in Calendar connected with this exercise", Toast.LENGTH_SHORT).show();
                }
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);

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

        if (requestCode == ADD_EXERCISE_REQUEST && resultCode == RESULT_OK) {
            String exerciseName = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_NAME);
            String exerciseCategory = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_CATEGORY);
            String exerciseType = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_TYPE);
            String exerciseDescription = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_DESCRIPTION);
            Exercise exercise = new Exercise(exerciseName, exerciseCategory, exerciseType, exerciseDescription);
            exerciseViewModel.insert(exercise);

            Toast.makeText(this, "Exercise saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_EXERCISE_REQUEST && resultCode == RESULT_OK) {
            String exerciseName = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_NAME);
            String exerciseCategory = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_CATEGORY);
            String exerciseType = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_TYPE);
            String exerciseDescription = data.getStringExtra(AddEditExerciseActivity.EXTRA_EXERCISE_DESCRIPTION);
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


}
