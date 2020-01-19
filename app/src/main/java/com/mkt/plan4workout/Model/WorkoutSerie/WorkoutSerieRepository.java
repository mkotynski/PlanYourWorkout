package com.mkt.plan4workout.Model.WorkoutSerie;

import android.app.Application;
import android.os.AsyncTask;


import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.AppDatabase;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class WorkoutSerieRepository {
    private WorkoutSerieDao workoutSerieDao;
    private LiveData<List<WorkoutSerie>> allWorkoutSerie;

    public WorkoutSerieRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        workoutSerieDao = database.workoutSerieDao();
        allWorkoutSerie = workoutSerieDao.getAllWorkoutSeries();
    }

    public Long insert(WorkoutSerie workout) throws ExecutionException, InterruptedException {
        InsertWorkoutAsyncTask asyncTask = new InsertWorkoutAsyncTask(workoutSerieDao);
        return asyncTask.execute(workout).get();
    }

    public void update(WorkoutSerie workout){
        new UpdateWorkoutAsyncTask(workoutSerieDao).execute(workout);
    }

    public void delete(WorkoutSerie workout){
        new DeleteWorkoutAsyncTask(workoutSerieDao).execute(workout);
    }

    public void deleteAllWorkoutSeries(){
        new DeleteAllWorkoutsAsyncTask(workoutSerieDao).execute();
    }

    public List<WorkoutSerie> getWorkoutSeries(int id) throws ExecutionException, InterruptedException {
        GetWorkoutAsyncTask asyncTask = new GetWorkoutAsyncTask(workoutSerieDao, id);
        return asyncTask.execute().get();
    }


    public LiveData<List<WorkoutSerie>> getAllWorkoutSeries() {
        return allWorkoutSerie;
    }

    private static class InsertWorkoutAsyncTask extends AsyncTask<WorkoutSerie, Void, Long> {
        private WorkoutSerieDao workoutDao;

        private InsertWorkoutAsyncTask(WorkoutSerieDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Long doInBackground(WorkoutSerie... workouts) {
            long id = workoutDao.insert(workouts[0]);
            return id;
        }

        @Override
        protected void onPostExecute(Long search_id) {
            //resultId = search_id;
        }
    }

    private static class UpdateWorkoutAsyncTask extends AsyncTask<WorkoutSerie, Void, Void> {
        private WorkoutSerieDao workoutDao;

        private UpdateWorkoutAsyncTask(WorkoutSerieDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(WorkoutSerie... workouts) {
            workoutDao.update(workouts[0]);
            return null;
        }
    }

    private static class DeleteWorkoutAsyncTask extends AsyncTask<WorkoutSerie, Void, Void> {
        private WorkoutSerieDao workoutDao;

        private DeleteWorkoutAsyncTask(WorkoutSerieDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(WorkoutSerie... workouts) {
            workoutDao.delete(workouts[0]);
            return null;
        }
    }

    private static class DeleteAllWorkoutsAsyncTask extends AsyncTask<Void, Void, Void> {
        private WorkoutSerieDao workoutDao;

        private DeleteAllWorkoutsAsyncTask(WorkoutSerieDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workoutDao.deleteAllWorkouts();
            return null;
        }
    }

    private static class GetWorkoutAsyncTask extends AsyncTask<Void, Void, List<WorkoutSerie>> {
        private WorkoutSerieDao workoutDao;
        private int id;

        private GetWorkoutAsyncTask(WorkoutSerieDao workoutDao, int id){
            this.workoutDao = workoutDao;
            this.id = id;
        }

        @Override
        protected List<WorkoutSerie> doInBackground(Void... voids) {
            return workoutDao.getWorkoutSeries(id);
        }
    }

}
