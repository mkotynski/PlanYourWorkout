package com.mkt.plan4workout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.applandeo.materialcalendarview.CalendarView;

import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.utils.DateUtils;
import com.mkt.plan4workout.Exercise.Exercise;
import com.mkt.plan4workout.Plan.Plan;
import com.mkt.plan4workout.Plan.PlanViewModel;
import com.mkt.plan4workout.Workout.Workout;
import com.mkt.plan4workout.Workout.WorkoutViewModel;
import com.mkt.plan4workout.utils.DrawableUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class CalendarActivity extends AppCompatActivity {

    WorkoutViewModel workoutViewModel;
    CalendarViewModel calendarViewModel;
    PlanViewModel planViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);

        List<EventDay> events = new ArrayList<>();

//        Calendar calendar = Calendar.getInstance();
//        events.add(new EventDay(calendar, DrawableUtils.getCircleDrawableWithText(this, "T")));

        CalendarView calendarView = findViewById(R.id.calendarView);

        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -5);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 5);

        calendarView.setMinimumDate(min);
        calendarView.setMaximumDate(max);
        calendarView.setEvents(events);

        //calendarView.setEvents(events);
        //calendarView.setDisabledDays(getDisabledDays());

        calendarView.setOnDayClickListener(eventDay -> {
            workoutViewModel.getAllWorkouts().observe(this, new Observer<List<Workout>>() {
                @Override
                public void onChanged(List<Workout> workouts) {
                    for (Workout workout : workouts) {
                        if(eventDay.getCalendar().getTime().toString().equals(workout.getDate())){
                            try {
                                Plan plan = planViewModel.getPlan(workout.getIdOfPlan());
                                Toast.makeText(CalendarActivity.this, plan.getName(), Toast.LENGTH_SHORT).show();
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
                    events.add(new EventDay(calen, R.drawable.sample_icon_2));
                    calendarView.setEvents(events);

                }
            }
        });
        Button setDateButton = (Button) findViewById(R.id.setDateButton);
        setDateButton.setOnClickListener(v -> {

            Calendar selectedDate = calendarView.getFirstSelectedDate();
            EventDay eventDay = new EventDay(selectedDate, R.drawable.sample_icon_2);
            boolean exists = false;
            for(EventDay event : events){
                System.out.println("EVENTy: " + event.getCalendar().getTime().toString());
                System.out.println("SELECTED: " + selectedDate.getTime().toString());
                if(event.getCalendar().getTime().toString().equals(selectedDate.getTime().toString())) exists = true;
            }
            if(!exists) {
                //events.add(eventDay);
                Intent intent = new Intent(CalendarActivity.this, AddWorkout.class);
                intent.putExtra(AddWorkout.EXTRA_DATE, selectedDate.getTime().toString());
                startActivityForResult(intent, 1);
                //Toast.makeText(getApplicationContext(), "Added event", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    workoutViewModel.delete(workoutViewModel.getWorkoutByDate(selectedDate.getTime().toString()));
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

        if(resultCode == 1 && requestCode == 1) {
            String date = data.getStringExtra(AddWorkout.EXTRA_DATE);
            int idOfPlan = data.getIntExtra(AddWorkout.EXTRA_PLAN_ID, -1);
            long id = 0;
            if (idOfPlan != -1 && date != "") {
                try {
                    id = workoutViewModel.insert(new Workout(idOfPlan, date, 0));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Workout saved with id = " + id, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Workout not saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Workout not saved", Toast.LENGTH_SHORT).show();
        }
    }

}
