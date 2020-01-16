package com.mkt.plan4workout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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


        RecyclerView recyclerView = findViewById(R.id.recycler_view_do_workout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExerciseAdapter adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);

        idOfWorkout = intent.getIntExtra("idOfWorkout", -1);
        idOfPlan = intent.getIntExtra("idOfPlan", -1);

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
                        adapter.setExercises(exerciseList);


                        seriesViewModel.getAllWorkouts().observe(DoWorkoutActivity.this, new Observer<List<WorkoutSerie>>() {
                            List<List<WorkoutSerie>> listWSeries = new ArrayList<>();

                            @Override
                            public void onChanged(List<WorkoutSerie> workoutSeries) {
                                listWSeries.removeAll(listWSeries);
                                for (DoWorkout doWorkout : doWorkouts) {
                                    List<WorkoutSerie> wSeries = new ArrayList<>();
                                    for (WorkoutSerie workoutSerie : workoutSeries) {
                                        System.out.println(workoutSerie.getWorkoutId() +"=="+ doWorkout.getId() +"&&"+ doWorkout.getExerciseId() +"=="+ workoutSerie.getExerciseId());
                                        if (workoutSerie.getWorkoutId() == doWorkout.getId() && doWorkout.getExerciseId() == workoutSerie.getExerciseId()) {
                                            if (!wSeries.contains(workoutSerie))
                                                wSeries.add(workoutSerie);
                                        }
                                    }

//                                    int pom = 0;
//                                    int cnt = 0;
//                                    for(List<WorkoutSerie> w1 : listWSeries) {
//                                        for (WorkoutSerie ws : w1) {
//                                            if(ws.getId() == wSeries.get(pom).getId()){
//                                                cnt++;
//                                            }
//                                                pom++;
//                                        }
//                                        pom=0;
//                                    }
//                                    if(cnt == 0)
                                    listWSeries.add(wSeries);
                                }

                                System.out.println("ListWseries: " + listWSeries.size());
                                adapter.setSeriesWorkout(listWSeries);


                                System.out.println("ListWseries: " + listWSeries.size());
                                for (List<WorkoutSerie> ws: listWSeries){
                                    System.out.println("Wseries: " + ws.size());
                                    for(WorkoutSerie w : ws){
                                        System.out.println(w.getWorkoutId() + " - " + w.getExerciseId() + " - " + w.getKg());
                                    }
                                }
                            }
                        });
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
                doViewModel.setEditing(false);
                List<String> idsEx = new ArrayList<>();
                List<String> repEx = new ArrayList<>();
                List<String> kgEx = new ArrayList<>();
                List<WorkoutSerie> workoutSeries = null;
                int edit = 0;
                try {
                    workoutSeries = seriesViewModel.getWorkoutSeries(doWorkout.getId());
                    for (WorkoutSerie workoutSerie : workoutSeries) {
                        System.out.println("Tutaj: " + workoutSerie.getExerciseId() + " == " + doWorkout.getExerciseId());
                        System.out.println("tutaj do work id: " + workoutSerie.getWorkoutId() + " == " + doWorkout.getWorkoutId());
                        if (doWorkout.getId() == workoutSerie.getWorkoutId() && doWorkout.getExerciseId() == workoutSerie.getExerciseId()) {
//                                Toast.makeText(DoWorkoutActivity.this, "EDITing", Toast.LENGTH_SHORT).show();
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
                System.out.println("EDIT: " + edit);
                if (edit > 0) {
                    doViewModel.setIds(idsEx);
                    doViewModel.setReps(repEx);
                    doViewModel.setKgs(kgEx);
                    doViewModel.setEditing(true);
                }

                Intent intent = new Intent(DoWorkoutActivity.this, DoEditWorkoutExercise.class);
                intent.putExtra("exercise_name", exercise.getName());
                intent.putExtra("exercise_id", exercise.getId());
                intent.putExtra("do_workout_id", doWorkout.getId());
                System.out.println("IS EDITING = " + doViewModel.isEditing());
                if (doViewModel.isEditing()) {
                    intent.putExtra("ids", doViewModel.getIds().toArray(new String[0]));
                    intent.putExtra("reps", doViewModel.getReps().toArray(new String[0]));
                    System.out.println("reps.size : " + doViewModel.getReps().toArray(new String[0]).length);
                    intent.putExtra("kgs", doViewModel.getKgs().toArray(new String[0]));
                    Toast.makeText(DoWorkoutActivity.this, "EDITing", Toast.LENGTH_SHORT).show();
                    startActivityForResult(intent, 2);
                } else startActivityForResult(intent, 1);
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
                    System.out.println("EXERCISE ID = " + exerciseId);
                    seriesViewModel.insert(new WorkoutSerie(doWorkoutId, exerciseId, Integer.valueOf(reps[p]), Integer.valueOf(kg[p])));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(this, "Results saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            int exerciseId = data.getIntExtra("exercise_id", -1);
            int doWorkoutId = data.getIntExtra("do_workout_id", -1);
            int countOfSeries = data.getIntExtra("series", -1);
            //List<String> reps = data.getStringArrayListExtra("reps");
            String[] reps = data.getStringArrayExtra("reps");
            String[] kg = data.getStringArrayExtra("kg");
            String[] ids = data.getStringArrayExtra("ids");

            System.out.println("CNT OF SERIES " + countOfSeries);
            for (int p = 0; p < countOfSeries; p++) {
                WorkoutSerie workoutSerie = new WorkoutSerie(doWorkoutId, exerciseId, Integer.valueOf(reps[p]), Integer.valueOf(kg[p]));
                workoutSerie.setId(Integer.valueOf(ids[p]));
                System.out.println("EXERCISE ID = " + ids[p]);
                seriesViewModel.update(workoutSerie);
            }
            Toast.makeText(this, "Results updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Plan not saved", Toast.LENGTH_SHORT).show();
        }
    }



    private void saveWorkout() {
        Intent data = new Intent();
        data.putExtra("idOfWorkout", idOfWorkout);
        data.putExtra("idOfPlan", idOfPlan);


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
                saveWorkout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
