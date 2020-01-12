package com.mkt.plan4workout.ExerciseToPlan;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExerciseToPlanRepository {
    private ExerciseToPlanDao e2pDao;
    private LiveData<List<ExerciseToPlan>> allE2p;
    //private List<ExerciseToPlan> exercisesOfPlan;


    public ExerciseToPlanRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        e2pDao = database.e2pDao();
        allE2p = e2pDao.getAlle2p();
    }

    public void insert(ExerciseToPlan e2p){
        new InsertE2PAsyncTask(e2pDao).execute(e2p);
    }

    public void update(ExerciseToPlan e2p){
        new UpdateE2PAsyncTask(e2pDao).execute(e2p);
    }

    public void delete(ExerciseToPlan e2p){
        new DeleteE2PAsyncTask(e2pDao).execute(e2p);
    }

    public List<ExerciseToPlan> getExercisesOfPlan(int id) throws ExecutionException, InterruptedException {
        GetExercisesOfPlanAsyncTask asyncTask = new GetExercisesOfPlanAsyncTask(e2pDao, id);
        return asyncTask.execute().get();
    }

    public void deleteAllE2Ps(){
        new DeleteAllE2PsAsyncTask(e2pDao).execute();
    }

    public void deleteExercisesOfPlan(int id){
        new DeleteExercisesOfPlan(e2pDao, id).execute();
    }

    public LiveData<List<ExerciseToPlan>> getAllE2p() {
        return allE2p;
    }

    private static class InsertE2PAsyncTask extends AsyncTask<ExerciseToPlan, Void, Void> {
        private ExerciseToPlanDao e2pDao;

        private InsertE2PAsyncTask(ExerciseToPlanDao e2pDao){
            this.e2pDao = e2pDao;
        }

        @Override
        protected Void doInBackground(ExerciseToPlan... e2ps) {
            e2pDao.insert(e2ps[0]);
            return null;
        }
    }

    private static class UpdateE2PAsyncTask extends AsyncTask<ExerciseToPlan, Void, Void> {
        private ExerciseToPlanDao e2pDao;

        private UpdateE2PAsyncTask(ExerciseToPlanDao e2pDao){
            this.e2pDao = e2pDao;
        }

        @Override
        protected Void doInBackground(ExerciseToPlan... ep2s) {
            e2pDao.update(ep2s[0]);
            return null;
        }
    }

    private static class DeleteE2PAsyncTask extends AsyncTask<ExerciseToPlan, Void, Void> {
        private ExerciseToPlanDao e2pDao;

        private DeleteE2PAsyncTask(ExerciseToPlanDao e2pDao){
            this.e2pDao = e2pDao;
        }

        @Override
        protected Void doInBackground(ExerciseToPlan... e2ps) {
            e2pDao.delete(e2ps[0]);
            return null;
        }
    }

    private static class DeleteAllE2PsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ExerciseToPlanDao e2pDao;

        private DeleteAllE2PsAsyncTask(ExerciseToPlanDao e2pDao){
            this.e2pDao = e2pDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            e2pDao.deleteAlle2p();
            return null;
        }
    }

    private static class GetExercisesOfPlanAsyncTask extends AsyncTask<Void, Void, List<ExerciseToPlan> > {
        private ExerciseToPlanDao e2pDao;
        private int id;

        private GetExercisesOfPlanAsyncTask(ExerciseToPlanDao e2pDao, int id){
            this.e2pDao = e2pDao;
            this.id = id;
        }

        @Override
        protected List<ExerciseToPlan> doInBackground(Void... voids) {
            return e2pDao.getExercisesOfPlan(id);
        }
    }

    private static class DeleteExercisesOfPlan extends AsyncTask<Void, Void, Void> {
        private ExerciseToPlanDao e2pDao;
        private int id;

        private DeleteExercisesOfPlan(ExerciseToPlanDao e2pDao, int id){
            this.e2pDao = e2pDao;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            e2pDao.deleteExercisesOfPlan(id);
            return null;
        }
    }

}


