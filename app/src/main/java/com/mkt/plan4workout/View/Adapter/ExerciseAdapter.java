package com.mkt.plan4workout.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mkt.plan4workout.Model.DoWorkout.DoWorkout;
import com.mkt.plan4workout.Model.Exercise.Exercise;
import com.mkt.plan4workout.R;
import com.mkt.plan4workout.Model.WorkoutSerie.WorkoutSerie;
import com.mkt.plan4workout.Utils.RepsAndKgTextView;
import com.mkt.plan4workout.View.DoEditWorkoutExercise;

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
        holder.tvName.setText(currentPlan.getName());
        holder.tvCategory.setText(currentPlan.getCategory());
        holder.tvType.setText(String.valueOf(currentPlan.getType()));
        holder.tvDescription.setText(String.valueOf(currentPlan.getDescription()));

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
            params.addRule(RelativeLayout.BELOW, holder.tvDescription.getId());
            LinearLayout.LayoutParams paramsInner = new LinearLayout.LayoutParams(
                    400,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            TextView tvr = new TextView(context);
            LinearLayout.LayoutParams xparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            xparams.setMargins(0, 20, 0, 0);
            tvr.setTextSize(17);
            tvr.setLayoutParams(xparams);
            tvr.setText("SERIES DETAILS");


            holder.series.setLayoutParams(params);
            holder.series.setVisibility(VISIBLE);
            series.removeAll(series);
            serie.removeAll(serie);
            holder.series.removeAllViews();
            holder.series.addView(tvr);
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
        private TextView tvName;
        private TextView tvCategory;
        private TextView tvType;
        private TextView tvDescription;
        private LinearLayout series;

        public ExerciseHolder(final View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_exercise_name);
            tvCategory = itemView.findViewById(R.id.tv_exercise_category);
            tvType = itemView.findViewById(R.id.tv_exercise_type);
            tvDescription = itemView.findViewById(R.id.tv_exercise_description);
            series = (LinearLayout) itemView.findViewById(R.id.series_details);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(exercises.get(position));
                        listener.onItemViewClick(itemView, exercises.get(position));
                        if(doWorkouts.size() > 0) listener.onDoWorkoutClick(itemView, exercises.get(position), doWorkouts.get(position));
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

    public void removeItem(int position) {
        exercises.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Exercise exercise, int position) {
        exercises.add(position, exercise);
        notifyItemRemoved(position);
        notifyItemInserted(position);
    }

    public void restoreItemNo(Exercise exercise, int position) {
        notifyItemRemoved(position);
        notifyItemInserted(position);
    }

    public List<Exercise> getData() {
        return exercises;
    }
}
