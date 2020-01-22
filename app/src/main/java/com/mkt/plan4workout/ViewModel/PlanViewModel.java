package com.mkt.plan4workout.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.Model.Plan.Plan;
import com.mkt.plan4workout.Model.Plan.PlanRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PlanViewModel extends AndroidViewModel {
    private PlanRepository repository;
    private LiveData<List<Plan>> allPlans;
    private LiveData<List<Plan>> allPlansA;

    public PlanViewModel(@NonNull Application application) {
        super(application);
        repository = new PlanRepository(application);
        allPlans = repository.getAllPlans();
        allPlansA = repository.getAllPlansA();
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

    public void archive(int id) {
        repository.archive(id);
    }

    public void deleteAllPlans() {
        repository.deleteAllPlans();
    }

    public LiveData<Plan> getPlan(int id) throws ExecutionException, InterruptedException {
        return repository.getPlan(id);
    }
    public Plan getxPlan(int id) throws ExecutionException, InterruptedException {
        return repository.getxPlan(id);
    }

    public LiveData<List<Plan>> getAllPlans() {
        return allPlans;
    }

    public LiveData<List<Plan>> getAllPlansA() {
        return allPlansA;
    }
}
