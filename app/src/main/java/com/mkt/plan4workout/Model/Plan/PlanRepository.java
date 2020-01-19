package com.mkt.plan4workout.Model.Plan;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PlanRepository {
    private PlanDao planDao;
    private LiveData<List<Plan>> allPlans;
    private long resultId = 0;

    public PlanRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        planDao = database.planDao();
        allPlans = planDao.getAllPlans();
    }

    public Long insert(Plan plan) throws ExecutionException, InterruptedException {
        InsertPlanAsyncTask asyncTask = new InsertPlanAsyncTask(planDao);
        return asyncTask.execute(plan).get();
    }

    public void update(Plan plan){
        new UpdatePlanAsyncTask(planDao).execute(plan);
    }

    public void delete(Plan plan){
        new DeletePlanAsyncTask(planDao).execute(plan);
    }

    public void deleteAllPlans(){
        new DeleteAllPlansAsyncTask(planDao).execute();
    }

    public LiveData<Plan> getPlan(int id) throws ExecutionException, InterruptedException {
        GetPlanAsyncTask asyncTask = new GetPlanAsyncTask(planDao, id);
        return asyncTask.execute().get();
    }

    public Plan getxPlan(int id) throws ExecutionException, InterruptedException {
        GetxPlanAsyncTask asyncTask = new GetxPlanAsyncTask(planDao, id);
        return asyncTask.execute().get();
    }

    public LiveData<List<Plan>> getAllPlans() {
        return allPlans;
    }

    private static class InsertPlanAsyncTask extends AsyncTask<Plan, Void, Long> {
        private PlanDao planDao;

        private InsertPlanAsyncTask(PlanDao planDao){
            this.planDao = planDao;
        }

        @Override
        protected Long doInBackground(Plan... plans) {
            long id = planDao.insert(plans[0]);
            System.out.println("ID OF INSERTED ROW = " + id);
            return id;
        }

        @Override
        protected void onPostExecute(Long search_id) {
            //resultId = search_id;
        }
    }

    private static class UpdatePlanAsyncTask extends AsyncTask<Plan, Void, Void> {
        private PlanDao planDao;

        private UpdatePlanAsyncTask(PlanDao planDao){
            this.planDao = planDao;
        }

        @Override
        protected Void doInBackground(Plan... plans) {
            planDao.update(plans[0]);
            return null;
        }
    }

    private static class DeletePlanAsyncTask extends AsyncTask<Plan, Void, Void> {
        private PlanDao planDao;

        private DeletePlanAsyncTask(PlanDao planDao){
            this.planDao = planDao;
        }

        @Override
        protected Void doInBackground(Plan... plans) {
            planDao.delete(plans[0]);
            return null;
        }
    }

    private static class DeleteAllPlansAsyncTask extends AsyncTask<Void, Void, Void> {
        private PlanDao planDao;

        private DeleteAllPlansAsyncTask(PlanDao planDao){
            this.planDao = planDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            planDao.deleteAllPlans();
            return null;
        }
    }

    private static class GetPlanAsyncTask extends AsyncTask<Void, Void, LiveData<Plan>> {
        private PlanDao planDao;
        private int id;

        private GetPlanAsyncTask(PlanDao planDao, int id){
            this.planDao = planDao;
            this.id = id;
        }

        @Override
        protected LiveData<Plan> doInBackground(Void... voids) {
            return planDao.getPlan(id);
        }
    }

    private static class GetxPlanAsyncTask extends AsyncTask<Void, Void, Plan> {
        private PlanDao planDao;
        private int id;

        private GetxPlanAsyncTask(PlanDao planDao, int id){
            this.planDao = planDao;
            this.id = id;
        }

        @Override
        protected Plan doInBackground(Void... voids) {
            return planDao.getxPlan(id);
        }
    }
}

