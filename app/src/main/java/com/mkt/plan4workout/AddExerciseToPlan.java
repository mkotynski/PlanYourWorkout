package com.mkt.plan4workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mkt.plan4workout.Exercise.Exercise;
import com.mkt.plan4workout.Exercise.ExerciseAdapter;
import com.mkt.plan4workout.Exercise.ExerciseAdapterPick;
import com.mkt.plan4workout.Exercise.ExercisePick;
import com.mkt.plan4workout.Exercise.ExerciseViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddExerciseToPlan extends AppCompatActivity {

    public static final int ADD_PLAN_REQUEST = 1;
    public static final int EDIT_PLAN_REQUEST = 2;

    public static final String EXTRA_EXERCISES = "com.mkt.plan4workout.EXTRA_EXERCISES";
    public static final String EXTRA_EXERCISES_ID = "com.mkt.plan4workout.EXTRA_EXERCISES_ID";

    private Context context;
    ExerciseViewModel exerciseViewModel;
    ExercisePickViewModel exercisePickViewModel;
    Button btnAddExercisesToPlan;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercises_to_plan);

        btnAddExercisesToPlan = findViewById(R.id.button_save_choosen_exercises);
        textView = findViewById(R.id.texting);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        exercisePickViewModel = ViewModelProviders.of(this).get(ExercisePickViewModel.class);

        final ExerciseAdapterPick adapter = new ExerciseAdapterPick();
        recyclerView.setAdapter(adapter);

        final String values = getIntent().getExtras().getString("exercises");

        textView.setText(values);

        exercisePickViewModel.setExercisesList(values);


        final List<Integer> pick = new ArrayList<>();

        exerciseViewModel.getAllExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                adapter.setExercises(exercises,exercisePickViewModel.getExercisesInteger());
                exercisePickViewModel.setAllExercises(exercises);
                exercisePickViewModel.setExercisesList(values);
            }
        });

        btnAddExercisesToPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               saveExercises();
            }
        });



        adapter.setOnItemClickListener(new ExerciseAdapterPick.OnItemClickListener() {
            @Override
            public void onItemClick(Exercise exercise) {
            }

            @Override
            public void onItemViewClick(View itemView, Exercise exercise) {
                if (exercisePickViewModel.isChoosen(exercise)) {
                    exercisePickViewModel.delExercise(exercise);
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorWhite));
                    TextView name = itemView.findViewById(R.id.tv_exercise_name);
                    name.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorBlack));

                } else {
                    exercisePickViewModel.addExercise(exercise);
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
                    TextView title = itemView.findViewById(R.id.tv_exercise_name);
                    title.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorWhite));
                }
            }
        });
    }

    private void saveExercises() {
        Intent data = new Intent();
        //data.putExtra(EXTRA_EXERCISES, ExercisePickViewModel.listToString());
        data.putExtra(EXTRA_EXERCISES_ID, exercisePickViewModel.idListToString());
        setResult(RESULT_OK, data);
        finish();
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
