package com.mkt.plan4workout;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mkt.plan4workout.Model.DoWorkout.DoWorkout;
import com.mkt.plan4workout.Model.DoWorkout.DoWorkoutDao;
import com.mkt.plan4workout.Model.Exercise.Exercise;
import com.mkt.plan4workout.Model.Exercise.ExerciseDao;
import com.mkt.plan4workout.Model.ExerciseToPlan.ExerciseToPlan;
import com.mkt.plan4workout.Model.ExerciseToPlan.ExerciseToPlanDao;
import com.mkt.plan4workout.Model.Plan.Plan;
import com.mkt.plan4workout.Model.Plan.PlanDao;
import com.mkt.plan4workout.Model.Workout.Workout;
import com.mkt.plan4workout.Model.Workout.WorkoutDao;
import com.mkt.plan4workout.Model.WorkoutSerie.WorkoutSerie;
import com.mkt.plan4workout.Model.WorkoutSerie.WorkoutSerieDao;

@Database(entities = {Plan.class, Exercise.class, ExerciseToPlan.class, Workout.class, DoWorkout.class, WorkoutSerie.class}, version = 11, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract PlanDao planDao();
    public abstract ExerciseDao exerciseDao();
    public abstract ExerciseToPlanDao e2pDao();
    public abstract WorkoutDao workoutDao();
    public abstract DoWorkoutDao doWorkoutDao();
    public abstract WorkoutSerieDao workoutSerieDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private PlanDao planDao;
        private ExerciseDao exerciseDao;
        private ExerciseToPlanDao e2pDao;
        private WorkoutDao workoutDao;
        private DoWorkoutDao doWorkoutDao;
        private WorkoutSerieDao workoutSerieDao;

        private PopulateDbAsyncTask(AppDatabase db){

            planDao = db.planDao();
            exerciseDao = db.exerciseDao();
            e2pDao = db.e2pDao();
            workoutDao = db.workoutDao();
            doWorkoutDao = db.doWorkoutDao();
            workoutSerieDao = db.workoutSerieDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            planDao.deleteAllPlans();
            planDao.insert(new Plan("FBW", "fbw","Description",0));
            planDao.insert(new Plan("MONDAY PUSH", "push","Description",0));
            planDao.insert(new Plan("FBW part 2", "fbw","Description 2",0));

            exerciseDao.insert(new Exercise("Crunch","core","push",  "..",0));
            exerciseDao.insert(new Exercise("Bicep Curl","arms","pull", "With barbell",0));
            exerciseDao.insert(new Exercise("Chin Up","back","pull", "..",0));
            exerciseDao.insert(new Exercise("Bench Press","chest","push", "Wide Grip",0));
            exerciseDao.insert(new Exercise("Deadlift","legs","pull", "With barbell",0));
            exerciseDao.insert(new Exercise("Arnold Press","shoulders","push", "Dumbbell",0));
            exerciseDao.insert(new Exercise("Stretching","other","other", "..",0));
            return null;
        }
    }
}

