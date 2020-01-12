package com.mkt.plan4workout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.mkt.plan4workout.Workout.Workout;
import com.mkt.plan4workout.Workout.WorkoutViewModel;

import java.util.concurrent.ExecutionException;

public class CalendarActivityOld extends AppCompatActivity {

    CalendarView calendarView;
    TextView myDate;

    String choosenDate;

    Button buttonAddWorkout;

    WorkoutViewModel workoutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        buttonAddWorkout = (Button) findViewById(R.id.button_add_workout);
        myDate = (TextView) findViewById(R.id.myDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = (year) + "-" + (month+1) + "-" + (dayOfMonth);
                myDate.setText(date);
                choosenDate = date;
            }
        });



        buttonAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivityOld.this, AddWorkout.class);
                intent.putExtra("date", choosenDate);
                startActivityForResult(intent,1);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            String date = data.getStringExtra(AddWorkout.EXTRA_DATE);
            int idOfPlan = data.getIntExtra(AddWorkout.EXTRA_PLAN_ID, -1);
            long id = 0;
            if(idOfPlan != -1 && date != "") {
                try {
                    id = workoutViewModel.insert(new Workout(idOfPlan,date,0));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            Toast.makeText(this, "Workout saved with id = " + id, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Workout not saved", Toast.LENGTH_SHORT).show();
        }
    }

}
