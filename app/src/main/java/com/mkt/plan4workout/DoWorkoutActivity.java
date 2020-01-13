package com.mkt.plan4workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mkt.plan4workout.DoWorkout.DoWorkout;
import com.mkt.plan4workout.DoWorkout.DoWorkoutViewModel;
import com.mkt.plan4workout.Exercise.Exercise;
import com.mkt.plan4workout.Exercise.ExerciseAdapter;
import com.mkt.plan4workout.Exercise.ExerciseViewModel;
import com.mkt.plan4workout.ExerciseToPlan.ExerciseToPlan;
import com.mkt.plan4workout.ExerciseToPlan.ExerciseToPlanRepository;
import com.mkt.plan4workout.ExerciseToPlan.ExerciseToPlanViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DoWorkoutActivity extends AppCompatActivity {

    ExerciseToPlanViewModel etpViewModel;
    DoWorkoutViewModel doWoViewModel;
    ExerciseViewModel exerciseViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_workout);

        etpViewModel = ViewModelProviders.of(this).get(ExerciseToPlanViewModel.class);
        doWoViewModel = ViewModelProviders.of(this).get(DoWorkoutViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);

        Intent intent = getIntent();


        RecyclerView recyclerView = findViewById(R.id.recycler_view_do_workout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExerciseAdapter adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);

        int idOfWorkout = intent.getIntExtra("idOfWorkout",-1);
        int idOfPlan = intent.getIntExtra("idOfPlan",-1);

        doWoViewModel.getAllWorkouts().observe(this, new Observer<List<DoWorkout>>() {
            @Override
            public void onChanged(List<DoWorkout> doWorkouts) {
                List<Exercise> exerciseList = new ArrayList<>();
                exerciseViewModel.getAllExercises().observe(DoWorkoutActivity.this, new Observer<List<Exercise>>(){
                    @Override
                    public void onChanged(List<Exercise> exercises) {
                        for(DoWorkout doWorkout : doWorkouts)
                        {
                            for(Exercise ex: exercises){
                                System.out.println(doWorkout.getExerciseId() + " == " + ex.getId());
                                if(doWorkout.getExerciseId() == ex.getId()){
                                    if(!exerciseList.contains(ex)) exerciseList.add(ex);
                                }
                            }
                        }
                        System.out.println("SIZE: " + exerciseList.size());
                        adapter.setExercises(exerciseList);
                    }
                });
                adapter.setDoWorkouts(doWorkouts);
            }
        });

        adapter.setOnItemClickListener(new ExerciseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Exercise exercise) { }

            @Override
            public void onItemViewClick(View itemView, Exercise exercise) { }
            @Override
            public void onDoWorkoutClick(View itemView,Exercise exercise, DoWorkout doWorkout) {
                Intent intent = new Intent(DoWorkoutActivity.this, DoEditExercise.class);
                intent.putExtra("exercise_name",exercise.getName());
                intent.putExtra("do_workout_id", doWorkout.getId());
                startActivity(intent);
            }
        });

    }
}
