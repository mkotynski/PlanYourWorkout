package com.mkt.plan4workout.Workout;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WorkoutRepository {
    private WorkoutDao workoutDao;
    private LiveData<List<Workout>> allWorkouts;

    public WorkoutRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        workoutDao = database.workoutDao();
        allWorkouts = workoutDao.getAllWorkouts();
    }

    public Long insert(Workout workout) throws ExecutionException, InterruptedException {
        InsertWorkoutAsyncTask asyncTask = new InsertWorkoutAsyncTask(workoutDao);
        return asyncTask.execute(workout).get();
    }

    public void update(Workout workout){
        new UpdateWorkoutAsyncTask(workoutDao).execute(workout);
    }

    public void delete(Workout workout){
        new DeleteWorkoutAsyncTask(workoutDao).execute(workout);
    }

    public void deleteAllWorkouts(){
        new DeleteAllWorkoutsAsyncTask(workoutDao).execute();
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return allWorkouts;
    }

    private static class InsertWorkoutAsyncTask extends AsyncTask<Workout, Void, Long> {
        private WorkoutDao workoutDao;

        private InsertWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Long doInBackground(Workout... workouts) {
            long id = workoutDao.insert(workouts[0]);
            return id;
        }

        @Override
        protected void onPostExecute(Long search_id) {
            //resultId = search_id;
        }
    }

    private static class UpdateWorkoutAsyncTask extends AsyncTask<Workout, Void, Void> {
        private WorkoutDao workoutDao;

        private UpdateWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(Workout... workouts) {
            workoutDao.update(workouts[0]);
            return null;
        }
    }

    private static class DeleteWorkoutAsyncTask extends AsyncTask<Workout, Void, Void> {
        private WorkoutDao workoutDao;

        private DeleteWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(Workout... workouts) {
            workoutDao.delete(workouts[0]);
            return null;
        }
    }

    private static class DeleteAllWorkoutsAsyncTask extends AsyncTask<Void, Void, Void> {
        private WorkoutDao workoutDao;

        private DeleteAllWorkoutsAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workoutDao.deleteAllWorkouts();
            return null;
        }
    }
}
