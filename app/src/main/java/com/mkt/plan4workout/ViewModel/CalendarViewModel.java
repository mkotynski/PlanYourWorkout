package com.mkt.plan4workout.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class CalendarViewModel extends AndroidViewModel {
    public CalendarViewModel(@NonNull Application application) {
        super(application);
    }

    public int getMonth(String m) {
        String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 0; i < months.length; i++) {
            if (m.equals(months[i])) return (i);
        }
        return 1;
    }
}
