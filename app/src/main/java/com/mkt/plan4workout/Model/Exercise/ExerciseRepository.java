package com.mkt.plan4workout.Model.Exercise;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.mkt.plan4workout.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExerciseRepository {
    private ExerciseDao exerciseDao;
    private LiveData<List<Exercise>> allExercises;
    private LiveData<List<Exercise>> allExercisesA;


    public ExerciseRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        exerciseDao = database.exerciseDao();
        allExercisesA = exerciseDao.getAllExercisesA();
        allExercises = exerciseDao.getAllExercises();
    }

    public void insert(Exercise exercise) {
        new InsertExerciseAsyncTask(exerciseDao).execute(exercise);
    }

    public void update(Exercise exercise) {
        new UpdateExerciseAsyncTask(exerciseDao).execute(exercise);
    }

    public void delete(Exercise exercise) {
        new DeleteExerciseAsyncTask(exerciseDao).execute(exercise);
    }

    public void deleteAllExercises() {
        new DeleteAllExercisesAsyncTask(exerciseDao).execute();
    }

    public LiveData<List<Exercise>> getAllExercises() {
        return allExercises;
    }

    public LiveData<List<Exercise>> getAllExercisesA() {
        return allExercisesA;
    }

    public List<Exercise> getPlanExercises(int id) throws ExecutionException, InterruptedException {
        GetPlanExercisesAsyncTask asyncTask = new GetPlanExercisesAsyncTask(exerciseDao, id);
        return asyncTask.execute().get();
    }

    public void archive(int id) {
        new ArchiveExercisesAsyncTask(exerciseDao, id).execute();
    }


    private static class InsertExerciseAsyncTask extends AsyncTask<Exercise, Void, Void> {
        private ExerciseDao exerciseDao;

        private InsertExerciseAsyncTask(ExerciseDao exerciseDao) {
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            exerciseDao.insert(exercises[0]);
            return null;
        }
    }

    private static class UpdateExerciseAsyncTask extends AsyncTask<Exercise, Void, Void> {
        private ExerciseDao exerciseDao;

        private UpdateExerciseAsyncTask(ExerciseDao exerciseDao) {
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            exerciseDao.update(exercises[0]);
            return null;
        }
    }

    private static class DeleteExerciseAsyncTask extends AsyncTask<Exercise, Void, Void> {
        private ExerciseDao exerciseDao;

        private DeleteExerciseAsyncTask(ExerciseDao exerciseDao) {
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            exerciseDao.delete(exercises[0]);
            return null;
        }
    }

    private static class DeleteAllExercisesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ExerciseDao exerciseDao;

        private DeleteAllExercisesAsyncTask(ExerciseDao exerciseDao) {
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            exerciseDao.deleteAllExercises();
            return null;
        }
    }

    private static class GetPlanExercisesAsyncTask extends AsyncTask<Void, Void, List<Exercise>> {
        private ExerciseDao exerciseDao;
        private int id;

        private GetPlanExercisesAsyncTask(ExerciseDao exerciseDao, int id) {
            this.exerciseDao = exerciseDao;
            this.id = id;
        }

        @Override
        protected List<Exercise> doInBackground(Void... voids) {
            return exerciseDao.getPlanExercises(id);
        }
    }

    private static class ArchiveExercisesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ExerciseDao exerciseDao;
        private int id;

        private ArchiveExercisesAsyncTask(ExerciseDao exerciseDao, int id) {
            this.exerciseDao = exerciseDao;
            this.id = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            exerciseDao.archive(id);
            return null;
        }
    }
}

