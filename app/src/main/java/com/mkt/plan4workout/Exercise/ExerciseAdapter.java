package com.mkt.plan4workout.Exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mkt.plan4workout.DoWorkout.DoWorkout;
import com.mkt.plan4workout.R;
import com.mkt.plan4workout.Workout.Workout;
import com.mkt.plan4workout.Workout.WorkoutRepository;
import com.mkt.plan4workout.WorkoutSerie.WorkoutSerie;
import com.mkt.plan4workout.utils.RepsAndKgEditText;
import com.mkt.plan4workout.utils.RepsAndKgTextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {
    private List<Exercise> exercises = new ArrayList<>();
    private List<DoWorkout> doWorkouts = new ArrayList<>();
    private List<List<WorkoutSerie>> seriesWorkout = new ArrayList<>();
    private OnItemClickListener listener;
    private Context context;

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_item, parent, false);
        context = parent.getContext();
        //addExercisesToPlanViewModel = ViewModelProviders.of().get(AddExercisesToPlanViewModel.class);

        return new ExerciseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
        Exercise currentPlan = exercises.get(position);
        holder.textViewTitle.setText(currentPlan.getName());
        holder.textViewDescription.setText(currentPlan.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentPlan.getType()));

        List<WorkoutSerie> exWorkoutSeries = new ArrayList<>();
        for (List<WorkoutSerie> ws : seriesWorkout) {
            for (WorkoutSerie workoutSerie : ws) {
                if (workoutSerie.getWorkoutId() == doWorkouts.get(position).getId()) {
                    if (workoutSerie.getExerciseId() == exercises.get(position).getId()) {
                        exWorkoutSeries.add(workoutSerie);
                    }
                }
            }
        }

        if (exWorkoutSeries.size() > 0) {
            List<RepsAndKgTextView> series = new ArrayList<>();
            List<LinearLayout> serie = new ArrayList<>();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.BELOW, holder.textViewDescription.getId());
            LinearLayout.LayoutParams paramsInner = new LinearLayout.LayoutParams(
                    400,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            holder.series.setLayoutParams(params);
            holder.series.setVisibility(VISIBLE);
            series.removeAll(series);
            serie.removeAll(serie);
            holder.series.removeAllViews();
            for (int j = 1; j <= exWorkoutSeries.size(); j++) {
                serie.add(new LinearLayout(context));
                series.add(new RepsAndKgTextView(new TextView(context), new TextView(context)));
                series.get(j - 1).getReps().setText(exWorkoutSeries.get(j-1).getReps()+" reps");
                series.get(j - 1).getReps().setId(j + 20);
                series.get(j - 1).getKg().setText(exWorkoutSeries.get(j-1).getKg() + " kg");
                series.get(j - 1).getKg().setId(j + 120);

                series.get(j - 1).getReps().setLayoutParams(paramsInner);
                series.get(j - 1).getKg().setLayoutParams(paramsInner);

                serie.get(j - 1).addView(series.get(j - 1).getReps());
                serie.get(j - 1).addView(series.get(j - 1).getKg());
                serie.get(j - 1).setLayoutParams(params);

                holder.series.addView(serie.get(j - 1));
            }
        }


    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    public void setDoWorkouts(List<DoWorkout> doWorkouts) {
        this.doWorkouts = doWorkouts;
        notifyDataSetChanged();
    }

    public void setSeriesWorkout(List<List<WorkoutSerie>> seriesWorkout) {
        this.seriesWorkout = seriesWorkout;
        notifyDataSetChanged();
    }

    public Exercise getExerciseAt(int position) {
        return exercises.get(position);
    }

    class ExerciseHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;
        private LinearLayout series;

        public ExerciseHolder(final View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            series = (LinearLayout) itemView.findViewById(R.id.series_details);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(exercises.get(position));
                        listener.onItemViewClick(itemView, exercises.get(position));
                        listener.onDoWorkoutClick(itemView, exercises.get(position), doWorkouts.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Exercise exercise);

        void onItemViewClick(View itemView, Exercise exercise);

        void onDoWorkoutClick(View itemView, Exercise exercise, DoWorkout doWorkout);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
