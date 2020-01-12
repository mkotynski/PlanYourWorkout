package com.mkt.plan4workout;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mkt.plan4workout.ExerciseToPlan.ExerciseToPlan;
import com.mkt.plan4workout.ExerciseToPlan.ExerciseToPlanViewModel;
import com.mkt.plan4workout.Plan.Plan;
import com.mkt.plan4workout.Plan.PlanAdapter;
import com.mkt.plan4workout.Plan.PlanViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnSelectDateListener {
    public static final int ADD_PLAN_REQUEST = 1;
    public static final int EDIT_PLAN_REQUEST = 2;
    private PlanViewModel planViewModel;
    private ExerciseToPlanViewModel exerciseToPlanViewModel;
    private MainActivityViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonExercise = findViewById(R.id.button_exercise);
        buttonExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExerciseActivity.class);
                startActivity(intent);
            }
        });

        Button buttonCalendar = findViewById(R.id.button_calendar);
        buttonCalendar.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
        });

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_plan);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditPlanActivity.class);
                startActivityForResult(intent, ADD_PLAN_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final PlanAdapter adapter = new PlanAdapter();
        recyclerView.setAdapter(adapter);

        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
        exerciseToPlanViewModel = ViewModelProviders.of(this).get(ExerciseToPlanViewModel.class);
        mainViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        planViewModel.getAllPlans().observe(this, new Observer<List<Plan>>() {
            @Override
            public void onChanged(List<Plan> plans) {
                adapter.setPlans(plans);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // drag and drop functionality
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                planViewModel.delete(adapter.getPlanAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Plan deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Plan plan) {
                Intent intent = new Intent(MainActivity.this, AddEditPlanActivity.class);
                intent.putExtra(AddEditPlanActivity.EXTRA_ID, plan.getId());
                intent.putExtra(AddEditPlanActivity.EXTRA_TITLE, plan.getName());
                intent.putExtra(AddEditPlanActivity.EXTRA_DESCRIPTION, plan.getDescription());
                intent.putExtra(AddEditPlanActivity.EXTRA_PRIORITY, plan.getCateogry());

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
            public void onItemViewClick(View itemView, Plan plan){

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PLAN_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditPlanActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPlanActivity.EXTRA_DESCRIPTION);
            String exercises = data.getStringExtra(AddEditPlanActivity.EXTRA_EXERCISES);
            int priority = data.getIntExtra(AddEditPlanActivity.EXTRA_PRIORITY, 1);
            long id = 0;
            Plan plan = new Plan(title, description, String.valueOf(priority));
            try {
                id = planViewModel.insert(plan);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainViewModel.setPickExercises(exercises);
            mainViewModel.makePickExercises();

            System.out.println("SIZE new exercises: " + mainViewModel.getListPickExercises().size());
            for(Integer i: mainViewModel.getListPickExercises()){
                System.out.println("ADDING new exercises: " + i);
                ExerciseToPlan e2p = new ExerciseToPlan((int)id,i);
                System.out.println(e2p.getPlanId() + " - " + e2p.getExerciseId());
                exerciseToPlanViewModel.insert(e2p);
            }

            Toast.makeText(this, "Plan saved", Toast.LENGTH_SHORT).show();
        } else  if (requestCode == EDIT_PLAN_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditPlanActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Plan can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditPlanActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPlanActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditPlanActivity.EXTRA_PRIORITY, 1);
            String exercises = data.getStringExtra(AddEditPlanActivity.EXTRA_EXERCISES);

            Plan plan = new Plan(title, description, "fdafds");
            plan.setId(id);
            planViewModel.update(plan);

            mainViewModel.setPickExercises(exercises);
            mainViewModel.makePickExercises();

            System.out.println("SIZE new exercises: " + mainViewModel.getListPickExercises().size());
            exerciseToPlanViewModel.deleteExercisesOfPlan(Integer.valueOf(plan.getId()));
            for(Integer i: mainViewModel.getListPickExercises()){
                System.out.println("ADDING new exercises: " + i);
                ExerciseToPlan e2p = new ExerciseToPlan(Integer.valueOf(plan.getId()),i);
                exerciseToPlanViewModel.insert(e2p);
            }

            Toast.makeText(this, "Plan updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Plan not saved", Toast.LENGTH_SHORT).show();
        }
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
            case R.id.delete_all_notes:
                planViewModel.deleteAllPlans();
                Toast.makeText(this, "All plans deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void openOneDayPicker() {
        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -5);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 3);

        DatePickerBuilder oneDayBuilder = new DatePickerBuilder(this, this)
                .setPickerType(com.applandeo.materialcalendarview.CalendarView.ONE_DAY_PICKER)
                .setDate(max)
                .setHeaderColor(R.color.colorPrimaryDark)
                .setHeaderLabelColor(R.color.currentMonthDayColor)
                .setSelectionColor(R.color.daysLabelColor)
                .setTodayLabelColor(R.color.colorAccent)
                .setDialogButtonsColor(android.R.color.holo_green_dark)
                .setDisabledDaysLabelsColor(android.R.color.holo_purple)
                .setPreviousButtonSrc(R.drawable.ic_chevron_left_black_24dp)
                .setForwardButtonSrc(R.drawable.ic_chevron_right_black_24dp)
                .setMinimumDate(min)
                .setMaximumDate(max)
                .setTodayColor(R.color.sampleLighter)
                .setHeaderVisibility(View.VISIBLE)
                .setDisabledDays(getDisabledDays());

        DatePicker oneDayPicker = oneDayBuilder.build();
        oneDayPicker.show();
    }

    private void openManyDaysPicker() {
        Calendar min = Calendar.getInstance();
        min.add(Calendar.DAY_OF_MONTH, -5);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 3);

        List<Calendar> selectedDays = new ArrayList<>(getDisabledDays());
        selectedDays.add(min);
        selectedDays.add(max);

        DatePickerBuilder manyDaysBuilder = new DatePickerBuilder(this, this)
                .setPickerType(com.applandeo.materialcalendarview.CalendarView.MANY_DAYS_PICKER)
                .setHeaderColor(android.R.color.holo_green_dark)
                .setSelectionColor(android.R.color.holo_green_dark)
                .setTodayLabelColor(android.R.color.holo_green_dark)
                .setDialogButtonsColor(android.R.color.holo_green_dark)
                .setSelectedDays(selectedDays)
                .setNavigationVisibility(View.GONE)
                .setDisabledDays(getDisabledDays());

        DatePicker manyDaysPicker = manyDaysBuilder.build();
        manyDaysPicker.show();
    }

    private void openRangePicker() {
        Calendar min = Calendar.getInstance();
        min.add(Calendar.DAY_OF_MONTH, -5);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.DAY_OF_MONTH, 3);

        List<Calendar> selectedDays = new ArrayList<>();
        selectedDays.add(min);
        selectedDays.addAll(CalendarUtils.getDatesRange(min, max));
        selectedDays.add(max);

        DatePickerBuilder rangeBuilder = new DatePickerBuilder(this, this)
                .setPickerType(CalendarView.RANGE_PICKER)
                .setHeaderColor(R.color.sampleDark)
                .setAbbreviationsBarColor(R.color.sampleLight)
                .setAbbreviationsLabelsColor(android.R.color.white)
                .setPagesColor(R.color.sampleLighter)
                .setSelectionColor(android.R.color.white)
                .setSelectionLabelColor(R.color.sampleDark)
                .setTodayLabelColor(R.color.dialogAccent)
                .setDialogButtonsColor(android.R.color.white)
                .setDaysLabelsColor(android.R.color.white)
                .setAnotherMonthsDaysLabelsColor(R.color.sampleLighter)
                .setSelectedDays(selectedDays)
                .setMaximumDaysRange(10)
                .setDisabledDays(getDisabledDays());

        DatePicker rangePicker = rangeBuilder.build();
        rangePicker.show();
    }

    private List<Calendar> getDisabledDays() {
        Calendar firstDisabled = DateUtils.getCalendar();
        firstDisabled.add(Calendar.DAY_OF_MONTH, 2);

        Calendar secondDisabled = DateUtils.getCalendar();
        secondDisabled.add(Calendar.DAY_OF_MONTH, 1);

        Calendar thirdDisabled = DateUtils.getCalendar();
        thirdDisabled.add(Calendar.DAY_OF_MONTH, 18);

        List<Calendar> calendars = new ArrayList<>();
        calendars.add(firstDisabled);
        calendars.add(secondDisabled);
        calendars.add(thirdDisabled);
        return calendars;
    }

    @Override
    public void onSelect(List<Calendar> calendars) {
        Stream.of(calendars).forEach(calendar ->
                Toast.makeText(getApplicationContext(),
                        calendar.getTime().toString(),
                        Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
