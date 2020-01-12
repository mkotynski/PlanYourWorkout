//package com.mkt.plan4workout;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.annimon.stream.Stream;
//import com.applandeo.materialcalendarview.CalendarUtils;
//import com.applandeo.materialcalendarview.CalendarView;
//import com.applandeo.materialcalendarview.DatePicker;
//import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
//import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
//import com.applandeo.materialcalendarview.utils.DateUtils;
//
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//public class MainActivityX extends AppCompatActivity implements OnSelectDateListener {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mainx);
//
//        Button openCalendarButton = (Button) findViewById(R.id.openCalendarButton);
//
//        openCalendarButton.setOnClickListener(v -> {
//            Intent intent = new Intent(this, CalendarActivity.class);
//            startActivity(intent);
//        });
//
//        Button openOneDayPickerDialog = (Button) findViewById(R.id.openOneDayPickerDialogButton);
//        openOneDayPickerDialog.setOnClickListener(v -> openOneDayPicker());
//
//        Button openManyDaysPickerDialog = (Button) findViewById(R.id.openManyDaysPickerDialogButton);
//        openManyDaysPickerDialog.setOnClickListener(v -> openManyDaysPicker());
//
//        Button openRangePickerDialog = (Button) findViewById(R.id.openRangePickerDialogButton);
//        openRangePickerDialog.setOnClickListener(v -> openRangePicker());
//    }
//
//}
