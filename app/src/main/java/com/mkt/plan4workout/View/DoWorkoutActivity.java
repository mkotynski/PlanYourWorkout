package com.mkt.plan4workout.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;

public class DoWorkoutActivity extends AppCompatActivity {

    public static final String EXTRA_WORKOUT_ID = "com.mkt.plan4workout.EXTRA_WORKOUT_ID";
    public static final String EXTRA_PLAN_ID = "com.mkt.plan4workout.EXTRA_PLAN_ID";

    ExerciseToPlanViewModel etpViewModel;
    DoWorkoutViewModel doWoViewModel;
    DoViewModel doViewModel;
    ExerciseViewModel exerciseViewModel;
    WorkSerieViewModel seriesViewModel;

    int idOfWorkout;
    int idOfPlan;

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
        idOfWorkout = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);
        idOfPlan = intent.getIntExtra(EXTRA_PLAN_ID, -1);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_do_workout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExerciseAdapter adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);

        doWoViewModel.getAllWorkouts().observe(this, new Observer<List<DoWorkout>>() {
            @Override
            public void onChanged(List<DoWorkout> doWorkouts) {
                List<Exercise> exerciseList = new ArrayList<>();
                List<DoWorkout> doW = new ArrayList<>();
                exerciseViewModel.getAllExercises().observe(DoWorkoutActivity.this, new Observer<List<Exercise>>() {
                    @Override
                    public void onChanged(List<Exercise> exercises) {
                        for (DoWorkout doWorkout : doWorkouts) {
                            if(doWorkout.getWorkoutId() == idOfWorkout) {
                                for (Exercise ex : exercises) {
                                    if (doWorkout.getExerciseId() == ex.getId()) {
                                        if (!exerciseList.contains(ex)) exerciseList.add(ex);
                                    }
                                }
                            }
                        }
                        adapter.setExercises(exerciseList);

                        seriesViewModel.getAllWorkouts().observe(DoWorkoutActivity.this, new Observer<List<WorkoutSerie>>() {
                            List<List<WorkoutSerie>> listWSeries = new ArrayList<>();
                            @Override
                            public void onChanged(List<WorkoutSerie> workoutSeries) {
                                listWSeries.removeAll(listWSeries);
                                listWSeries.removeAll(doW);
                                for (DoWorkout doWorkout : doWorkouts) {
                                    if(doWorkout.getWorkoutId() == idOfWorkout) {
                                        doW.add(doWorkout);
                                        List<WorkoutSerie> wSeries = new ArrayList<>();
                                        for (WorkoutSerie workoutSerie : workoutSeries) {
                                            if (workoutSerie.getWorkoutId() == doWorkout.getId() && doWorkout.getExerciseId() == workoutSerie.getExerciseId()) {
                                                if (!wSeries.contains(workoutSerie)) {
                                                    wSeries.add(workoutSerie);
                                                }
                                            }
                                        }

                                        listWSeries.add(wSeries);
                                    }
                                }

                                adapter.setSeriesWorkout(listWSeries);
                                adapter.setDoWorkouts(doW);
                            }
                        });
                    }
                });

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
                doViewModel.setEditing(false);
                List<String> idsEx = new ArrayList<>();
                List<String> repEx = new ArrayList<>();
                List<String> kgEx = new ArrayList<>();
                List<WorkoutSerie> workoutSeries = null;
                int edit = 0;
                try {
                    workoutSeries = seriesViewModel.getWorkoutSeries(doWorkout.getId());
                    for (WorkoutSerie workoutSerie : workoutSeries) {
                        if (doWorkout.getId() == workoutSerie.getWorkoutId() && doWorkout.getExerciseId() == workoutSerie.getExerciseId()) {
                            idsEx.add(String.valueOf(workoutSerie.getId()));
                            repEx.add(String.valueOf(workoutSerie.getReps()));
                            kgEx.add(String.valueOf(workoutSerie.getKg()));
                            edit++;
                        }
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (edit > 0) {
                    doViewModel.setIds(idsEx);
                    doViewModel.setReps(repEx);
                    doViewModel.setKgs(kgEx);
                    doViewModel.setEditing(true);
                }

                Intent intent = new Intent(DoWorkoutActivity.this, DoEditWorkoutExercise.class);
                intent.putExtra(DoEditWorkoutExercise.EXTRA_NAME, exercise.getName());
                intent.putExtra(DoEditWorkoutExercise.EXTRA_EXERCISE_ID, exercise.getId());
                intent.putExtra(DoEditWorkoutExercise.EXTRA_WORKOUT_ID, doWorkout.getId());
                if (doViewModel.isEditing()) {
                    intent.putExtra(DoEditWorkoutExercise.EXTRA_IDS, doViewModel.getIds().toArray(new String[0]));
                    intent.putExtra(DoEditWorkoutExercise.EXTRA_REPS, doViewModel.getReps().toArray(new String[0]));
                    intent.putExtra(DoEditWorkoutExercise.EXTRA_KG, doViewModel.getKgs().toArray(new String[0]));
                    startActivityForResult(intent, 2);
                } else startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 1) {
            int exerciseId = data.getIntExtra(DoEditWorkoutExercise.EXTRA_EXERCISE_ID, -1);
            int doWorkoutId = data.getIntExtra(DoEditWorkoutExercise.EXTRA_WORKOUT_ID, -1);
            int countOfSeries = data.getIntExtra(DoEditWorkoutExercise.EXTRA_SERIES, -1);
            String[] reps = data.getStringArrayExtra(DoEditWorkoutExercise.EXTRA_REPS);
            String[] kg = data.getStringArrayExtra(DoEditWorkoutExercise.EXTRA_KG);

            for (int p = 0; p < countOfSeries; p++) {
                try {
                    seriesViewModel.insert(new WorkoutSerie(doWorkoutId, exerciseId, Integer.valueOf(reps[p]), Integer.valueOf(kg[p])));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(this, "Results saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2 && resultCode == 1) {
            int exerciseId = data.getIntExtra(DoEditWorkoutExercise.EXTRA_EXERCISE_ID, -1);
            int doWorkoutId = data.getIntExtra(DoEditWorkoutExercise.EXTRA_WORKOUT_ID, -1);
            int countOfSeries = data.getIntExtra(DoEditWorkoutExercise.EXTRA_SERIES, -1);
            String[] reps = data.getStringArrayExtra(DoEditWorkoutExercise.EXTRA_REPS);
            String[] kg = data.getStringArrayExtra(DoEditWorkoutExercise.EXTRA_KG);
            String[] ids = data.getStringArrayExtra(DoEditWorkoutExercise.EXTRA_IDS);

            for (int p = 0; p < countOfSeries; p++) {
                WorkoutSerie workoutSerie = new WorkoutSerie(doWorkoutId, exerciseId, Integer.valueOf(reps[p]), Integer.valueOf(kg[p]));
                workoutSerie.setId(Integer.valueOf(ids[p]));
                seriesViewModel.update(workoutSerie);
            }
            Toast.makeText(this, "Results updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveWorkout() {
        Intent data = new Intent();
        data.putExtra(EXTRA_WORKOUT_ID, idOfWorkout);
        data.putExtra(EXTRA_PLAN_ID, idOfPlan);


        setResult(1, data);
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
                saveWorkout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
