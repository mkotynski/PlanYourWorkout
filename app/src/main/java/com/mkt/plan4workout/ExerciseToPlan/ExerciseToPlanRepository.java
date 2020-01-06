package com.mkt.plan4workout.ExerciseToPlan;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.AppDatabase;
import com.mkt.plan4workout.Plan.Plan;
import com.mkt.plan4workout.Plan.PlanDao;

import java.util.List;

public class ExerciseToPlanRepository {
    private ExerciseToPlanDao e2pDao;
    private LiveData<List<ExerciseToPlan>> allE2p;


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

    public void deleteAllE2Ps(){
        new DeleteAllE2PsAsyncTask(e2pDao).execute();
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
}


