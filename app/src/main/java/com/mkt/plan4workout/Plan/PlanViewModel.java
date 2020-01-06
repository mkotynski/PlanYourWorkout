package com.mkt.plan4workout.Plan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PlanViewModel extends AndroidViewModel {
    private PlanRepository repository;
    private LiveData<List<Plan>> allPlans;

    public PlanViewModel(@NonNull Application application) {
        super(application);
        repository = new PlanRepository(application);
        allPlans = repository.getAllPlans();
    }

    public void insert(Plan plan) {
        repository.insert(plan);
    }

    public void update(Plan plan) {
        repository.update(plan);
    }

    public void delete(Plan plan) {
        repository.delete(plan);
    }

    public void deleteAllPlans() {
        repository.deleteAllPlans();
    }

    public LiveData<List<Plan>> getAllPlans() {
        return allPlans;
    }
}
