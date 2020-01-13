package com.mkt.plan4workout.DoWorkout;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.AppDatabase;
import com.mkt.plan4workout.Workout.Workout;
import com.mkt.plan4workout.Workout.WorkoutDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DoWorkoutRepository {
    private DoWorkoutDao doWorkoutDao;
    private LiveData<List<DoWorkout>> allDoWorkouts;

    public DoWorkoutRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        doWorkoutDao = database.doWorkoutDao();
        allDoWorkouts = doWorkoutDao.getAllDoWorkouts();
    }

    public Long insert(DoWorkout doWorkout) throws ExecutionException, InterruptedException {
        InsertWorkoutAsyncTask asyncTask = new InsertWorkoutAsyncTask(doWorkoutDao);
        return asyncTask.execute(doWorkout).get();
    }

    public void update(DoWorkout doWorkout){
        new UpdateWorkoutAsyncTask(doWorkoutDao).execute(doWorkout);
    }

    public void delete(DoWorkout doWorkout){
        new DeleteWorkoutAsyncTask(doWorkoutDao).execute(doWorkout);
    }

    public void deleteAllWorkouts(){
        new DeleteAllWorkoutsAsyncTask(doWorkoutDao).execute();
    }

//    public Workout getWorkout(int id) throws ExecutionException, InterruptedException {
//        GetDoWorkoutAsyncTask asyncTask = new GetDoWorkoutAsyncTask(doWorkoutDao, id);
//        return asyncTask.execute().get();
//    }

//    public Workout getPlanByDate(String date) throws ExecutionException, InterruptedException {
//        GetWorkoutByDateAsyncTask asyncTask = new GetWorkoutByDateAsyncTask(doWorkoutDao, date);
//        return asyncTask.execute().get();
//    }

    public LiveData<List<DoWorkout>> getAllDoWorkouts() {
        return allDoWorkouts;
    }

    private static class InsertWorkoutAsyncTask extends AsyncTask<DoWorkout, Void, Long> {
        private DoWorkoutDao doWorkoutDao;

        private InsertWorkoutAsyncTask(DoWorkoutDao doWorkoutDao){
            this.doWorkoutDao = doWorkoutDao;
        }

        @Override
        protected Long doInBackground(DoWorkout... doWorkouts) {
            long id = doWorkoutDao.insert(doWorkouts[0]);
            return id;
        }

        @Override
        protected void onPostExecute(Long search_id) {
            //resultId = search_id;
        }
    }

    private static class UpdateWorkoutAsyncTask extends AsyncTask<DoWorkout, Void, Void> {
        private DoWorkoutDao doWorkoutDao;

        private UpdateWorkoutAsyncTask(DoWorkoutDao doWorkoutDao){
            this.doWorkoutDao = doWorkoutDao;
        }

        @Override
        protected Void doInBackground(DoWorkout... doWorkouts) {
            doWorkoutDao.update(doWorkouts[0]);
            return null;
        }
    }

    private static class DeleteWorkoutAsyncTask extends AsyncTask<DoWorkout, Void, Void> {
        private DoWorkoutDao doWorkoutDao;

        private DeleteWorkoutAsyncTask(DoWorkoutDao doWorkoutDao){
            this.doWorkoutDao = doWorkoutDao;
        }

        @Override
        protected Void doInBackground(DoWorkout... doWorkouts) {
            doWorkoutDao.delete(doWorkouts[0]);
            return null;
        }
    }

    private static class DeleteAllWorkoutsAsyncTask extends AsyncTask<Void, Void, Void> {
        private DoWorkoutDao doWorkoutDao;

        private DeleteAllWorkoutsAsyncTask(DoWorkoutDao doWorkoutDao){
            this.doWorkoutDao = doWorkoutDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            doWorkoutDao.deleteAllDoWorkouts();
            return null;
        }
    }

//    private static class GetDoWorkoutAsyncTask extends AsyncTask<Void, Void, Workout> {
//        private DoWorkoutDao doWorkoutDao;
//        private int id;
//
//        private GetDoWorkoutAsyncTask(DoWorkoutDao doWorkoutDao, int id){
//            this.doWorkoutDao = doWorkoutDao;
//            this.id = id;
//        }
//
//        @Override
//        protected Workout doInBackground(Void... voids) {
//            return doWorkoutDao.getDoWorkout(id);
//        }
//    }

//    private static class GetWorkoutByDateAsyncTask extends AsyncTask<Void, Void, Workout> {
//        private DoWorkoutDao workoutDao;
//        private String date;
//
//        private GetWorkoutByDateAsyncTask(DoWorkoutDao workoutDao, String date){
//            this.workoutDao = workoutDao;
//            this.date = date;
//        }
//
//        @Override
//        protected Workout doInBackground(Void... voids) {
//            return workoutDao.getWorkoutByDate(date);
//        }
//    }
}
