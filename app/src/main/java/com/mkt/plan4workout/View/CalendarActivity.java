package com.mkt.plan4workout.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.applandeo.materialcalendarview.CalendarView;

import android.widget.Toast;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.utils.DateUtils;

import com.mkt.plan4workout.Model.DoWorkout.DoWorkout;
import com.mkt.plan4workout.R;
import com.mkt.plan4workout.View.AddWorkout;
import com.mkt.plan4workout.View.DoWorkoutActivity;
import com.mkt.plan4workout.View.ExerciseActivity;
import com.mkt.plan4workout.View.PlansActivity;
import com.mkt.plan4workout.View.WorkoutHistoryActivity;
import com.mkt.plan4workout.ViewModel.CalendarViewModel;
import com.mkt.plan4workout.ViewModel.DoWorkoutViewModel;
import com.mkt.plan4workout.Model.Exercise.Exercise;
import com.mkt.plan4workout.ViewModel.ExerciseViewModel;
import com.mkt.plan4workout.Model.Plan.Plan;
import com.mkt.plan4workout.ViewModel.MainActivityViewModel;
import com.mkt.plan4workout.ViewModel.PlanViewModel;
import com.mkt.plan4workout.Model.Workout.Workout;
import com.mkt.plan4workout.ViewModel.WorkoutViewModel;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;
import static com.mkt.plan4workout.View.PlansActivity.DO_WORKOUT_REQUEST;


public class CalendarActivity extends Fragment {

    WorkoutViewModel workoutViewModel;
    CalendarViewModel calendarViewModel;
    PlanViewModel planViewModel;
    DoWorkoutViewModel doWoViewModel;
    ExerciseViewModel exerciseViewModel;
    MainActivityViewModel mainViewModel;

    Button buttonDoWorkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_activity, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
        doWoViewModel = ViewModelProviders.of(this).get(DoWorkoutViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        mainViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        List<EventDay> events = new ArrayList<>();

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, -24);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -3);

        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 3);

        calendarView.setMinimumDate(min);
        calendarView.setMaximumDate(max);
        calendarView.setEvents(events);

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

        Button showResults = (Button) view.findViewById(R.id.show_results);
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
                    Intent intent = new Intent(getActivity().getApplication(), WorkoutHistoryActivity.class);
                    intent.putExtra(WorkoutHistoryActivity.EXTRA_WORKOUT_ID, workout.getId());
                    intent.putExtra(WorkoutHistoryActivity.EXTRA_PLAN_ID, workout.getIdOfPlan());
                    plan = planViewModel.getxPlan(workout.getIdOfPlan());
                    intent.putExtra(WorkoutHistoryActivity.EXTRA_PLAN_NAME, plan.getName());
                    System.out.println("Get history");
                    System.out.println(workout.getId());
                    System.out.println(workout.getIdOfPlan());
                    System.out.println(plan.getName());
                    startActivity(intent);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Button setDateButton = (Button) view.findViewById(R.id.setDateButton);
        setDateButton.setOnClickListener(v -> {

            for(EventDay e: events){
                System.out.println(e.getCalendar().getTime());
            }
            Calendar selectedDate = calendarView.getFirstSelectedDate();
            System.out.println(selectedDate.getTime());
            EventDay eventDay = new EventDay(selectedDate);
            boolean exists = false;
            for (EventDay event : events) {
                if (event.getCalendar().getTime().toString().equals(selectedDate.getTime().toString()))
                    exists = true;
            }
            if (!exists) {
                Intent intent = new Intent(getActivity().getApplication(), AddWorkout.class);
                intent.putExtra(AddWorkout.EXTRA_DATE, selectedDate.getTime().toString());
                startActivityForResult(intent, 1);
            } else {
                try {
                    Workout workout = workoutViewModel.getWorkoutByDate(selectedDate.getTime().toString());
                    if (workout != null){
                        int i = 0;
                        for (EventDay event : events) {
                            if (!event.getCalendar().getTime().toString().equals(workout.getDate())) i++;
                        }
                        workoutViewModel.delete(workout);
                    }
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity().getApplication(), "Deleted workout", Toast.LENGTH_LONG).show();
            }
            calendarView.setEvents(events);
        });



        try {
            String date = mainViewModel.getTodayDate();
            workoutViewModel.getWorkoutByDateLD(date).observe(this, workout -> {
                if (workout != null && workout.getDone() == 0) {
                    buttonDoWorkout = view.findViewById(R.id.button_do_workout);
                    buttonDoWorkout.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams params = buttonDoWorkout.getLayoutParams();
                    buttonDoWorkout.setLayoutParams(params);

                    buttonDoWorkout.setOnClickListener(v -> {
                        Intent intent = new Intent(getActivity().getApplication(), DoWorkoutActivity.class);
                        intent.putExtra(DoWorkoutActivity.EXTRA_PLAN_ID, workout.getIdOfPlan());
                        intent.putExtra(DoWorkoutActivity.EXTRA_WORKOUT_ID, workout.getId());
                        System.out.println(workout.getId());
                        startActivityForResult(intent, DO_WORKOUT_REQUEST);
                    });
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                Toast.makeText(getActivity().getApplication(), "Workout saved", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity().getApplication(), "Workout not saved", Toast.LENGTH_SHORT).show();
            }
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
        }else {
            Toast.makeText(getActivity().getApplication(), "Workout not saved", Toast.LENGTH_SHORT).show();
        }
    }

}
