package com.mkt.plan4workout.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mkt.plan4workout.Model.DoWorkout.DoWorkout;
import com.mkt.plan4workout.R;
import com.mkt.plan4workout.ViewModel.DoViewModel;
import com.mkt.plan4workout.ViewModel.DoWorkoutViewModel;
import com.mkt.plan4workout.Model.Exercise.Exercise;
import com.mkt.plan4workout.View.Adapter.ExerciseAdapter;
import com.mkt.plan4workout.ViewModel.ExerciseViewModel;
import com.mkt.plan4workout.ViewModel.ExerciseToPlanViewModel;
import com.mkt.plan4workout.ViewModel.WorkSerieViewModel;
import com.mkt.plan4workout.Model.WorkoutSerie.WorkoutSerie;

import java.util.ArrayList;
import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {
    public static final String EXTRA_WORKOUT_ID = "com.mkt.plan4workout.EXTRA_WORKOUT_ID";
    public static final String EXTRA_PLAN_ID = "com.mkt.plan4workout.EXTRA_PLAN_ID";
    public static final String EXTRA_PLAN_NAME = "com.mkt.plan4workout.EXTRA_PLAN_NAME";

    ExerciseToPlanViewModel etpViewModel;
    DoWorkoutViewModel doWoViewModel;
    DoViewModel doViewModel;
    ExerciseViewModel exerciseViewModel;
    WorkSerieViewModel seriesViewModel;

    int workoutId;
    int planId;
    String planName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);
        etpViewModel = ViewModelProviders.of(this).get(ExerciseToPlanViewModel.class);
        doWoViewModel = ViewModelProviders.of(this).get(DoWorkoutViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        doViewModel = ViewModelProviders.of(this).get(DoViewModel.class);
        seriesViewModel = ViewModelProviders.of(this).get(WorkSerieViewModel.class);
        TextView tvPlanName = findViewById(R.id.tv_plan_name);

        Intent intent = getIntent();
        workoutId = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);
        planId = intent.getIntExtra(EXTRA_PLAN_ID, -1);
        planName = intent.getStringExtra(EXTRA_PLAN_NAME);

        tvPlanName.setText(planName);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_do_workout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExerciseAdapter adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);

        doWoViewModel.getAllWorkouts().observe(this, new Observer<List<DoWorkout>>() {
            @Override
            public void onChanged(List<DoWorkout> doWorkouts) {
                List<Exercise> exerciseList = new ArrayList<>();
                exerciseViewModel.getAllExercises().observe(WorkoutHistoryActivity.this, new Observer<List<Exercise>>() {
                    @Override
                    public void onChanged(List<Exercise> exercises) {
                        for (DoWorkout doWorkout : doWorkouts) {
                            if(doWorkout.getWorkoutId() == workoutId) {
                                for (Exercise ex : exercises) {
                                    if (doWorkout.getExerciseId() == ex.getId()) {
                                        if (!exerciseList.contains(ex)) exerciseList.add(ex);
                                    }
                                }
                            }
                        }
                        adapter.setExercises(exerciseList);

                        seriesViewModel.getAllWorkouts().observe(WorkoutHistoryActivity.this, new Observer<List<WorkoutSerie>>() {
                            List<List<WorkoutSerie>> listWSeries = new ArrayList<>();

                            @Override
                            public void onChanged(List<WorkoutSerie> workoutSeries) {
                                listWSeries.removeAll(listWSeries);
                                for (DoWorkout doWorkout : doWorkouts) {
                                    if(doWorkout.getWorkoutId() == workoutId) {
                                        List<WorkoutSerie> wSeries = new ArrayList<>();
                                        for (WorkoutSerie workoutSerie : workoutSeries) {
                                            if (workoutSerie.getWorkoutId() == doWorkout.getId() && doWorkout.getExerciseId() == workoutSerie.getExerciseId()) {
                                                if (!wSeries.contains(workoutSerie))
                                                    wSeries.add(workoutSerie);
                                            }
                                        }

                                        listWSeries.add(wSeries);
                                    }
                                }

                                adapter.setSeriesWorkout(listWSeries);
                            }
                        });
                    }
                });
                adapter.setDoWorkouts(doWorkouts);
            }
        });


    }
}
