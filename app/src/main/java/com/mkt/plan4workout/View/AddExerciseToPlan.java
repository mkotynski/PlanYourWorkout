package com.mkt.plan4workout.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mkt.plan4workout.Model.Exercise.Exercise;
import com.mkt.plan4workout.R;
import com.mkt.plan4workout.View.Adapter.ExerciseAdapterPick;
import com.mkt.plan4workout.ViewModel.ExercisePickViewModel;
import com.mkt.plan4workout.ViewModel.ExerciseViewModel;

import java.util.List;

public class AddExerciseToPlan extends AppCompatActivity {

    public static final String EXTRA_EXERCISES = "com.mkt.plan4workout.EXTRA_EXERCISES";
    public static final String EXTRA_EXERCISES_ID = "com.mkt.plan4workout.EXTRA_EXERCISES_ID";

    private Context context;
    ExerciseViewModel exerciseViewModel;
    ExercisePickViewModel exercisePickViewModel;
    Button btnAddExercisesToPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercises_to_plan);

        btnAddExercisesToPlan = findViewById(R.id.button_save_choosen_exercises);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        exercisePickViewModel = ViewModelProviders.of(this).get(ExercisePickViewModel.class);

        final ExerciseAdapterPick adapter = new ExerciseAdapterPick();
        recyclerView.setAdapter(adapter);

        final String values = getIntent().getExtras().getString(EXTRA_EXERCISES);


        exercisePickViewModel.setExercisesList(values);


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
        setResult(1, data);
        finish();
    }

}
