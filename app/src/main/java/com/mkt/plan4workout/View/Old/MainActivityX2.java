//package com.mkt.plan4workout;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProviders;
//import androidx.recyclerview.widget.ItemTouchHelper;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.annimon.stream.Stream;
//import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.mkt.plan4workout.Model.ExerciseToPlan.ExerciseToPlan;
//import com.mkt.plan4workout.Model.Plan.Plan;
//import com.mkt.plan4workout.Model.Workout.Workout;
//import com.mkt.plan4workout.View.Adapter.PlanAdapter;
//import com.mkt.plan4workout.View.AddEditPlanActivity;
//import com.mkt.plan4workout.View.CalendarActivity;
//import com.mkt.plan4workout.View.DoWorkoutActivity;
//import com.mkt.plan4workout.View.ExerciseActivity;
//import com.mkt.plan4workout.View.SwipeToDeleteCallback;
//import com.mkt.plan4workout.ViewModel.DoWorkoutViewModel;
//import com.mkt.plan4workout.ViewModel.ExerciseToPlanViewModel;
//import com.mkt.plan4workout.ViewModel.MainActivityViewModel;
//import com.mkt.plan4workout.ViewModel.PlanViewModel;
//import com.mkt.plan4workout.ViewModel.WorkoutViewModel;
//
//import java.util.Calendar;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//public class MainActivityX2 extends AppCompatActivity implements OnSelectDateListener {
//    public static final int ADD_PLAN_REQUEST = 1;
//    public static final int EDIT_PLAN_REQUEST = 2;
//    public static final int DO_WORKOUT_REQUEST = 3;
//
//    private PlanViewModel planViewModel;
//    private ExerciseToPlanViewModel exerciseToPlanViewModel;
//    private MainActivityViewModel mainViewModel;
//    private WorkoutViewModel workoutViewModel;
//    private DoWorkoutViewModel doWorkoutViewModel;
//    Button buttonDoWorkout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_calendar);
//
//        Button buttonExercise = findViewById(R.id.button_exercise);
//        buttonExercise.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivityX2.this, ExerciseActivity.class);
//            startActivity(intent);
//        });
//
//        Button buttonCalendar = findViewById(R.id.button_calendar);
//        buttonCalendar.setOnClickListener(v -> {
//            Intent intent = new Intent(this, CalendarActivity.class);
//            startActivity(intent);
//        });
//
//        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_plan);
//        buttonAddNote.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivityX2.this, AddEditPlanActivity.class);
//            startActivityForResult(intent, ADD_PLAN_REQUEST);
//        });
//
//        RecyclerView recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//
//        final PlanAdapter adapter = new PlanAdapter();
//        recyclerView.setAdapter(adapter);
//
//        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
//        exerciseToPlanViewModel = ViewModelProviders.of(this).get(ExerciseToPlanViewModel.class);
//        mainViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
//        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
//        doWorkoutViewModel = ViewModelProviders.of(this).get(DoWorkoutViewModel.class);
//
//        planViewModel.getAllPlans().observe(this, plans -> {
//            adapter.setPlans(plans);
//        });
//
//
//        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false; // drag and drop functionality
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                Plan item = adapter.getPlanAt(position);
//                boolean workoutsHere = false;
//                try {
//                    if(workoutViewModel.getWorkoutsByPlan(item.getId()).size() > 0) workoutsHere = true;
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if(!workoutsHere) {
//                    exerciseToPlanViewModel.deleteExercisesOfPlan(item.getId());
//                    planViewModel.delete(adapter.getPlanAt(viewHolder.getAdapterPosition()));
//                    adapter.removeItem(position);
//                    Toast.makeText(MainActivityX2.this, "Plan deleted", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    adapter.restoreItemNo(item,position);
//                    clearView(recyclerView, viewHolder);
//                    Toast.makeText(MainActivityX2.this, "There are workouts in Calendar connected with this plan", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
//
//        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
//        itemTouchhelper.attachToRecyclerView(recyclerView);
//
//        adapter.setOnItemClickListener(new PlanAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Plan plan) {
//                Intent intent = new Intent(MainActivityX2.this, AddEditPlanActivity.class);
//                intent.putExtra(AddEditPlanActivity.EXTRA_PLAN_NAME, plan.getName());
//                intent.putExtra(AddEditPlanActivity.EXTRA_PLAN_CATEGORY, plan.getCategory());
//                intent.putExtra(AddEditPlanActivity.EXTRA_PLAN_DESCRIPTION, plan.getDescription());
//                intent.putExtra(AddEditPlanActivity.EXTRA_ID, plan.getId());
//
//                try {
//                    String exercisesString = mainViewModel.makeStringFromList(exerciseToPlanViewModel.getExercisesOfPlan(plan.getId()));
//                    intent.putExtra(AddEditPlanActivity.EXTRA_EXERCISES, exercisesString);
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                startActivityForResult(intent, EDIT_PLAN_REQUEST);
//
//            }
//
//            @Override
//            public void onItemViewClick(View itemView, Plan plan) {
//            }
//
//        });
//
//        try {
//            String date = mainViewModel.getTodayDate();
//            workoutViewModel.getWorkoutByDateLD(date).observe(this, workout -> {
//                if (workout != null && workout.getDone() == 0) {
//                    Toast.makeText(MainActivityX2.this, "IT IS WORKOUT DAY!", Toast.LENGTH_SHORT).show();
//                    buttonDoWorkout = findViewById(R.id.button_do_workout);
//                    buttonDoWorkout.setVisibility(View.VISIBLE);
//                    ViewGroup.LayoutParams params = buttonDoWorkout.getLayoutParams();
//                    buttonDoWorkout.setLayoutParams(params);
//
//                    buttonDoWorkout.setOnClickListener(v -> {
//                        Intent intent = new Intent(MainActivityX2.this, DoWorkoutActivity.class);
//                        intent.putExtra(DoWorkoutActivity.EXTRA_PLAN_ID, Integer.valueOf(workout.getIdOfPlan()));
//                        intent.putExtra(DoWorkoutActivity.EXTRA_WORKOUT_ID, Integer.valueOf(workout.getId()));
//                        startActivityForResult(intent, DO_WORKOUT_REQUEST);
//                    });
//                }
//            });
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == ADD_PLAN_REQUEST && resultCode == RESULT_OK) {
//            String planName = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_NAME);
//            String planCategory = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_CATEGORY);
//            String planDescription = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_DESCRIPTION);
//            String planExercises = data.getStringExtra(AddEditPlanActivity.EXTRA_EXERCISES);
//            Plan plan = new Plan(planName, planCategory, planDescription);
//            long id = 0;
//            try {
//                id = planViewModel.insert(plan);
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            mainViewModel.setPickExercises(planExercises);
//            mainViewModel.makePickExercises();
//
//            for (Integer i : mainViewModel.getListPickExercises()) {
//                ExerciseToPlan e2p = new ExerciseToPlan((int) id, i);
//                exerciseToPlanViewModel.insert(e2p);
//            }
//
//            Toast.makeText(this, "Plan saved", Toast.LENGTH_SHORT).show();
//        } else if (requestCode == EDIT_PLAN_REQUEST && resultCode == RESULT_OK) {
//            String planName = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_NAME);
//            String planCategory = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_CATEGORY);
//            String planDescription = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_DESCRIPTION);
//            String planExercises = data.getStringExtra(AddEditPlanActivity.EXTRA_EXERCISES);
//            Plan plan = new Plan(planName, planCategory, planDescription);
//
//            int id = data.getIntExtra(AddEditPlanActivity.EXTRA_ID, -1);
//            if (id == -1) {
//                Toast.makeText(this, "Plan can't be updated", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            plan.setId(id);
//
//            planViewModel.update(plan);
//            mainViewModel.setPickExercises(planExercises);
//            mainViewModel.makePickExercises();
//            exerciseToPlanViewModel.deleteExercisesOfPlan(plan.getId());
//
//            for (Integer i : mainViewModel.getListPickExercises()) {
//                ExerciseToPlan e2p = new ExerciseToPlan(plan.getId(), i);
//                exerciseToPlanViewModel.insert(e2p);
//            }
//
//            Toast.makeText(this, "Plan updated", Toast.LENGTH_SHORT).show();
//        } else if (requestCode == DO_WORKOUT_REQUEST && resultCode == RESULT_OK) {
//            assert data != null;
//            int id = data.getIntExtra(DoWorkoutActivity.EXTRA_WORKOUT_ID, -1);
//            int idOfPlan = data.getIntExtra(DoWorkoutActivity.EXTRA_PLAN_ID, -1);
//
//            if (id == -1) {
//                Toast.makeText(this, "Workout can't be updated", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            String date = mainViewModel.getTodayDate();
//            Workout workout = new Workout(idOfPlan, date, 1);
//            workout.setId(id);
//            workoutViewModel.update(workout);
//            finish();
//            startActivity(getIntent());
//            Toast.makeText(this, "Workout done", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.delete_all_workouts:
//                workoutViewModel.deleteAllWorkouts();
//                doWorkoutViewModel.deleteAllWorkouts();
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
//                Toast.makeText(this, "All workouts deleted", Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onSelect(List<Calendar> calendars) {
//        Stream.of(calendars).forEach(calendar ->
//                Toast.makeText(getApplicationContext(),
//                        calendar.getTime().toString(),
//                        Toast.LENGTH_SHORT).show());
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//    }
//
//}
