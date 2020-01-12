package com.mkt.plan4workout.Plan;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PlanViewModel extends AndroidViewModel {
    private PlanRepository repository;
    private LiveData<List<Plan>> allPlans;

    public PlanViewModel(@NonNull Application application) {
        super(application);
        repository = new PlanRepository(application);
        allPlans = repository.getAllPlans();
    }

    public Long insert(Plan plan) throws ExecutionException, InterruptedException {
        return repository.insert(plan);
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

    public Plan getPlan(int id) throws ExecutionException, InterruptedException {
        return repository.getPlan(id);
    }

    public LiveData<List<Plan>> getAllPlans() {
        return allPlans;
    }
}
