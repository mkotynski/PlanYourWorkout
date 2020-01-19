package com.mkt.plan4workout.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.applandeo.materialcalendarview.CalendarView;

import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.utils.DateUtils;

import com.mkt.plan4workout.Model.DoWorkout.DoWorkout;
import com.mkt.plan4workout.R;
import com.mkt.plan4workout.ViewModel.CalendarViewModel;
import com.mkt.plan4workout.ViewModel.DoWorkoutViewModel;
import com.mkt.plan4workout.Model.Exercise.Exercise;
import com.mkt.plan4workout.ViewModel.ExerciseViewModel;
import com.mkt.plan4workout.Model.Plan.Plan;
import com.mkt.plan4workout.ViewModel.PlanViewModel;
import com.mkt.plan4workout.Model.Workout.Workout;
import com.mkt.plan4workout.ViewModel.WorkoutViewModel;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class CalendarActivity extends AppCompatActivity {

    WorkoutViewModel workoutViewModel;
    CalendarViewModel calendarViewModel;
    PlanViewModel planViewModel;
    DoWorkoutViewModel doWoViewModel;
    ExerciseViewModel exerciseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
        doWoViewModel = ViewModelProviders.of(this).get(DoWorkoutViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);

        List<EventDay> events = new ArrayList<>();

        CalendarView calendarView = findViewById(R.id.calendarView);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, -24);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -1);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 1);

        calendarView.setMinimumDate(min);
        calendarView.setMaximumDate(max);
        calendarView.setEvents(events);

        calendarView.setOnDayClickListener(eventDay -> {
            workoutViewModel.getAllWorkouts().observe(this, new Observer<List<Workout>>() {
                @Override
                public void onChanged(List<Workout> workouts) {
                    for (Workout workout : workouts) {
                        if (eventDay.getCalendar().getTime().toString().equals(workout.getDate())) {
                            System.out.println(eventDay.getCalendar().getTime().toString() + " - " + workout.getDate());
                            try {
                                planViewModel.getPlan(workout.getIdOfPlan()).observe(CalendarActivity.this, new Observer<Plan>() {
                                    @Override
                                    public void onChanged(Plan plan) {
                                        Toast.makeText(CalendarActivity.this, plan.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            });
        });


        workoutViewModel.getAllWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                for (Workout workout : workouts) {
                    Calendar calen = Calendar.getInstance();
                    String dateS[] = workout.getDate().split(" ");
                    calen.set(Integer.valueOf(dateS[5]), calendarViewModel.getMonth(dateS[1]), Integer.valueOf(dateS[2]));
                    events.add(new EventDay(calen, R.drawable.sample_circle));
                    calendarView.setEvents(events);

                }
            }
        });

        Button showResults = (Button) findViewById(R.id.show_results);
        showResults.setOnClickListener(v -> {

            Calendar selectedDate = calendarView.getFirstSelectedDate();
            System.out.println(selectedDate.getTime().toString());
            EventDay eventDay = new EventDay(selectedDate, R.drawable.sample_icon_2);
            boolean exists = false;
            for (EventDay event : events) {
                if (event.getCalendar().getTime().toString().equals(selectedDate.getTime().toString()))
                    exists = true;
            }
            if (exists) {
                Workout workout = null;
                Plan plan = null;
                try {
                    workout = workoutViewModel.getWorkoutByDate(selectedDate.getTime().toString());
                    Intent intent = new Intent(CalendarActivity.this, WorkoutHistoryActivity.class);
                    intent.putExtra(WorkoutHistoryActivity.EXTRA_WORKOUT_ID, workout.getId());
                    intent.putExtra(WorkoutHistoryActivity.EXTRA_PLAN_ID, workout.getIdOfPlan());
                    plan = planViewModel.getxPlan(workout.getIdOfPlan());
                    intent.putExtra(WorkoutHistoryActivity.EXTRA_PLAN_NAME, plan.getName());
                    startActivity(intent);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Button setDateButton = (Button) findViewById(R.id.setDateButton);
        setDateButton.setOnClickListener(v -> {

            Calendar selectedDate = calendarView.getFirstSelectedDate();
            EventDay eventDay = new EventDay(selectedDate, R.drawable.sample_icon_2);
            boolean exists = false;
            for (EventDay event : events) {
                if (event.getCalendar().getTime().toString().equals(selectedDate.getTime().toString()))
                    exists = true;
            }
            if (!exists) {
                Intent intent = new Intent(CalendarActivity.this, AddWorkout.class);
                intent.putExtra(AddWorkout.EXTRA_DATE, selectedDate.getTime().toString());
                startActivityForResult(intent, 1);
            } else {
                try {
                    Workout workout = workoutViewModel.getWorkoutByDate(selectedDate.getTime().toString());
                    if (workout != null) workoutViewModel.delete(workout);
                    finish();
                    startActivity(getIntent());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Deleted workout", Toast.LENGTH_LONG).show();
            }
            calendarView.setEvents(events);

        });
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1 && requestCode == 1) {
            String date = data.getStringExtra(AddWorkout.EXTRA_DATE);
            int idOfPlan = data.getIntExtra(AddWorkout.EXTRA_PLAN_ID, -1);
            long id;
            if (idOfPlan != -1 && !date.isEmpty()) {
                try {
                    id = workoutViewModel.insert(new Workout(idOfPlan, date, 0));
                    List<Exercise> exerciseList = exerciseViewModel.getPlanExercises(idOfPlan);
                    for (Exercise exercise : exerciseList) {
                        doWoViewModel.insert(new DoWorkout((int) id, exercise.getId(), 0));
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Workout saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Workout not saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Workout not saved", Toast.LENGTH_SHORT).show();
        }
    }

}
