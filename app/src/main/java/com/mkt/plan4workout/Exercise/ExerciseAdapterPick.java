package com.mkt.plan4workout.Exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mkt.plan4workout.R;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapterPick extends RecyclerView.Adapter<ExerciseAdapterPick.ExerciseHolder> {
    private List<Exercise> exercises = new ArrayList<>();
    private List<Integer> pick = new ArrayList<>();
    private OnItemClickListener listener;
    private Context context;

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_item, parent, false);
        context = parent.getContext();
        return new ExerciseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
        Exercise currentPlan = exercises.get(position);
        holder.tvName.setText(currentPlan.getName());
        holder.tvCategory.setText(currentPlan.getCategory());
        holder.tvType.setText(String.valueOf(currentPlan.getType()));
        holder.tvDescription.setText(String.valueOf(currentPlan.getDescription()));

        if (!pick.contains(currentPlan.getId())) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorWhite));
            TextView title = holder.itemView.findViewById(R.id.tv_exercise_name);
            title.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorBlack));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
            TextView title = holder.itemView.findViewById(R.id.tv_exercise_name);
            title.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorWhite));
        }
        System.out.println(currentPlan.getId());
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setExercises(List<Exercise> exercises, List<Integer> pick) {
        this.exercises = exercises;
        this.pick = pick;
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

        public ExerciseHolder(final View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_exercise_name);
            tvCategory = itemView.findViewById(R.id.tv_exercise_category);
            tvType = itemView.findViewById(R.id.tv_exercise_type);
            tvDescription = itemView.findViewById(R.id.tv_exercise_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(exercises.get(position));
                        listener.onItemViewClick(itemView, exercises.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Exercise exercise);

        void onItemViewClick(View itemView, Exercise exercise);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
