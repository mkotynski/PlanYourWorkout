package com.mkt.plan4workout.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mkt.plan4workout.Model.ExerciseToPlan.ExerciseToPlan;
import com.mkt.plan4workout.Model.Plan.Plan;
import com.mkt.plan4workout.Model.Workout.Workout;
import com.mkt.plan4workout.R;
import com.mkt.plan4workout.View.Adapter.PlanAdapter;
import com.mkt.plan4workout.ViewModel.DoWorkoutViewModel;
import com.mkt.plan4workout.ViewModel.ExerciseToPlanViewModel;
import com.mkt.plan4workout.ViewModel.MainActivityViewModel;
import com.mkt.plan4workout.ViewModel.PlanViewModel;
import com.mkt.plan4workout.ViewModel.WorkoutViewModel;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PlansActivity extends Fragment implements OnSelectDateListener {
    public static final int ADD_PLAN_REQUEST = 1;
    public static final int EDIT_PLAN_REQUEST = 2;
    public static final int DO_WORKOUT_REQUEST = 3;

    private PlanViewModel planViewModel;
    private ExerciseToPlanViewModel exerciseToPlanViewModel;
    private MainActivityViewModel mainViewModel;
    private WorkoutViewModel workoutViewModel;
    private DoWorkoutViewModel doWorkoutViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_calendar, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        FloatingActionButton buttonAddNote = view.findViewById(R.id.button_add_plan);
        buttonAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getApplication(), AddEditPlanActivity.class);
            startActivityForResult(intent, ADD_PLAN_REQUEST);
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplication()));
        recyclerView.setHasFixedSize(true);

        final PlanAdapter adapter = new PlanAdapter();
        recyclerView.setAdapter(adapter);

        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
        exerciseToPlanViewModel = ViewModelProviders.of(this).get(ExerciseToPlanViewModel.class);
        mainViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        doWorkoutViewModel = ViewModelProviders.of(this).get(DoWorkoutViewModel.class);

        planViewModel.getAllPlans().observe(this, plans -> {
            adapter.setPlans(plans);
        });


        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity().getApplication()) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // drag and drop functionality
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Plan item = adapter.getPlanAt(position);
                boolean workoutsHere = false;
                try {
                    if(workoutViewModel.getWorkoutsByPlan(item.getId()).size() > 0) workoutsHere = true;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!workoutsHere) {
                    exerciseToPlanViewModel.deleteExercisesOfPlan(item.getId());
                    planViewModel.delete(adapter.getPlanAt(viewHolder.getAdapterPosition()));
                    adapter.removeItem(position);
                    Toast.makeText(getActivity().getApplication(), "Plan deleted", Toast.LENGTH_SHORT).show();

                } else {
                    adapter.restoreItemNo(item,position);
                    clearView(recyclerView, viewHolder);
                    Toast.makeText(getActivity().getApplication(), "There are workouts in Calendar connected with this plan", Toast.LENGTH_SHORT).show();
                }
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Plan plan) {
                Intent intent = new Intent(getActivity().getApplication(), AddEditPlanActivity.class);
                intent.putExtra(AddEditPlanActivity.EXTRA_PLAN_NAME, plan.getName());
                intent.putExtra(AddEditPlanActivity.EXTRA_PLAN_CATEGORY, plan.getCategory());
                intent.putExtra(AddEditPlanActivity.EXTRA_PLAN_DESCRIPTION, plan.getDescription());
                intent.putExtra(AddEditPlanActivity.EXTRA_ID, plan.getId());

                try {
                    String exercisesString = mainViewModel.makeStringFromList(exerciseToPlanViewModel.getExercisesOfPlan(plan.getId()));
                    intent.putExtra(AddEditPlanActivity.EXTRA_EXERCISES, exercisesString);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, EDIT_PLAN_REQUEST);

            }

            @Override
            public void onItemViewClick(View itemView, Plan plan) {
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PLAN_REQUEST && resultCode == 1) {
            String planName = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_NAME);
            String planCategory = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_CATEGORY);
            String planDescription = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_DESCRIPTION);
            String planExercises = data.getStringExtra(AddEditPlanActivity.EXTRA_EXERCISES);
            Plan plan = new Plan(planName, planCategory, planDescription,0);
            long id = 0;
            try {
                id = planViewModel.insert(plan);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainViewModel.setPickExercises(planExercises);
            mainViewModel.makePickExercises();

            for (Integer i : mainViewModel.getListPickExercises()) {
                ExerciseToPlan e2p = new ExerciseToPlan((int) id, i);
                exerciseToPlanViewModel.insert(e2p);
            }

            Toast.makeText(getActivity().getApplication(), "Plan saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_PLAN_REQUEST && resultCode == 1) {
            String planName = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_NAME);
            String planCategory = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_CATEGORY);
            String planDescription = data.getStringExtra(AddEditPlanActivity.EXTRA_PLAN_DESCRIPTION);
            String planExercises = data.getStringExtra(AddEditPlanActivity.EXTRA_EXERCISES);
            Plan plan = new Plan(planName, planCategory, planDescription,0);

            int id = data.getIntExtra(AddEditPlanActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(getActivity().getApplication(), "Plan can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            plan.setId(id);

            planViewModel.update(plan);
            mainViewModel.setPickExercises(planExercises);
            mainViewModel.makePickExercises();
            exerciseToPlanViewModel.deleteExercisesOfPlan(plan.getId());

            for (Integer i : mainViewModel.getListPickExercises()) {
                ExerciseToPlan e2p = new ExerciseToPlan(plan.getId(), i);
                exerciseToPlanViewModel.insert(e2p);
            }

            Toast.makeText(getActivity().getApplication(), "Plan updated", Toast.LENGTH_SHORT).show();
        } else if (requestCode == DO_WORKOUT_REQUEST && resultCode == 1) {
            assert data != null;
            int id = data.getIntExtra(DoWorkoutActivity.EXTRA_WORKOUT_ID, -1);
            int idOfPlan = data.getIntExtra(DoWorkoutActivity.EXTRA_PLAN_ID, -1);

            if (id == -1) {
                Toast.makeText(getActivity().getApplication(), "Workout can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String date = mainViewModel.getTodayDate();
            Workout workout = new Workout(idOfPlan, date, 1);
            workout.setId(id);
            workoutViewModel.update(workout);
            Toast.makeText(getActivity().getApplication(), "Workout done", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSelect(List<Calendar> calendar) {

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }

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

}
