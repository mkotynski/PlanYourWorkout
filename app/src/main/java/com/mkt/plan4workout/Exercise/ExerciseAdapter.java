package com.mkt.plan4workout.Exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mkt.plan4workout.DoWorkout.DoWorkout;
import com.mkt.plan4workout.R;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {
    private List<Exercise> exercises = new ArrayList<>();
    private List<DoWorkout> doWorkouts = new ArrayList<>();
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

        System.out.println(currentPlan.getId());
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

    public Exercise getExerciseAt(int position) {
        return exercises.get(position);
    }

    class ExerciseHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public ExerciseHolder(final View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

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
