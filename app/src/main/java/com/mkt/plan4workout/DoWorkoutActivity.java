package com.mkt.plan4workout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mkt.plan4workout.DoWorkout.DoWorkout;
import com.mkt.plan4workout.DoWorkout.DoWorkoutViewModel;
import com.mkt.plan4workout.Exercise.Exercise;
import com.mkt.plan4workout.Exercise.ExerciseAdapter;
import com.mkt.plan4workout.Exercise.ExerciseViewModel;
import com.mkt.plan4workout.ExerciseToPlan.ExerciseToPlanViewModel;
import com.mkt.plan4workout.Workout.Workout;
import com.mkt.plan4workout.WorkoutSerie.WorkSerieViewModel;
import com.mkt.plan4workout.WorkoutSerie.WorkoutSerie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DoWorkoutActivity extends AppCompatActivity {

    ExerciseToPlanViewModel etpViewModel;
    DoWorkoutViewModel doWoViewModel;
    DoViewModel doViewModel;
    ExerciseViewModel exerciseViewModel;
    WorkSerieViewModel seriesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_workout);

        etpViewModel = ViewModelProviders.of(this).get(ExerciseToPlanViewModel.class);
        doWoViewModel = ViewModelProviders.of(this).get(DoWorkoutViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        doViewModel = ViewModelProviders.of(this).get(DoViewModel.class);
        seriesViewModel = ViewModelProviders.of(this).get(WorkSerieViewModel.class);

        Intent intent = getIntent();


        RecyclerView recyclerView = findViewById(R.id.recycler_view_do_workout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExerciseAdapter adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);

        int idOfWorkout = intent.getIntExtra("idOfWorkout", -1);
        int idOfPlan = intent.getIntExtra("idOfPlan", -1);

        doWoViewModel.getAllWorkouts().observe(this, new Observer<List<DoWorkout>>() {
            @Override
            public void onChanged(List<DoWorkout> doWorkouts) {
                List<Exercise> exerciseList = new ArrayList<>();
                exerciseViewModel.getAllExercises().observe(DoWorkoutActivity.this, new Observer<List<Exercise>>() {
                    @Override
                    public void onChanged(List<Exercise> exercises) {
                        for (DoWorkout doWorkout : doWorkouts) {
                            for (Exercise ex : exercises) {
                                System.out.println(doWorkout.getExerciseId() + " == " + ex.getId());
                                if (doWorkout.getExerciseId() == ex.getId()) {
                                    if (!exerciseList.contains(ex)) exerciseList.add(ex);
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
            public void onItemClick(Exercise exercise) {
            }

            @Override
            public void onItemViewClick(View itemView, Exercise exercise) {
            }

            @Override
            public void onDoWorkoutClick(View itemView, Exercise exercise, DoWorkout doWorkout) {
                Intent intent = new Intent(DoWorkoutActivity.this, DoEditWorkoutExercise.class);
                intent.putExtra("exercise_name", exercise.getName());
                intent.putExtra("id_exercise", exercise.getId());
                intent.putExtra("id_do_workout", doWorkout.getId());
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            int exerciseId = data.getIntExtra("exercise_id", -1);
            int doWorkoutId = data.getIntExtra("do_workout_id", -1);
            int countOfSeries = data.getIntExtra("series", -1);
            //List<String> reps = data.getStringArrayListExtra("reps");
            String[] reps = data.getStringArrayExtra("reps");
            String[] kg = data.getStringArrayExtra("kg");

            for (int p = 0; p < countOfSeries; p++) {
                try {

                    seriesViewModel.insert(new WorkoutSerie(doWorkoutId,exerciseId,Integer.valueOf(reps[p]), Integer.valueOf(kg[p])));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(this, "Results saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
//            int id = data.getIntExtra(AddEditPlanActivity.EXTRA_ID, -1);
//
//            if (id == -1) {
//                Toast.makeText(this, "Plan can't be updated", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            String title = data.getStringExtra(AddEditPlanActivity.EXTRA_TITLE);
//            String description = data.getStringExtra(AddEditPlanActivity.EXTRA_DESCRIPTION);
//            int priority = data.getIntExtra(AddEditPlanActivity.EXTRA_PRIORITY, 1);
//            String exercises = data.getStringExtra(AddEditPlanActivity.EXTRA_EXERCISES);
//
//            Plan plan = new Plan(title, description, "fdafds");
//            plan.setId(id);
//            planViewModel.update(plan);
//
//            mainViewModel.setPickExercises(exercises);
//            mainViewModel.makePickExercises();
//
//            System.out.println("SIZE new exercises: " + mainViewModel.getListPickExercises().size());
//            exerciseToPlanViewModel.deleteExercisesOfPlan(Integer.valueOf(plan.getId()));
//            for (Integer i : mainViewModel.getListPickExercises()) {
//                System.out.println("ADDING new exercises: " + i);
//                ExerciseToPlan e2p = new ExerciseToPlan(Integer.valueOf(plan.getId()), i);
//                exerciseToPlanViewModel.insert(e2p);
//            }

            Toast.makeText(this, "Plan updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Plan not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
