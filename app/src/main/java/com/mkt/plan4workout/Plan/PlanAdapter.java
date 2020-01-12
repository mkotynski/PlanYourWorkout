package com.mkt.plan4workout.Plan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mkt.plan4workout.Exercise.Exercise;
import com.mkt.plan4workout.Exercise.ExerciseAdapterPick;
import com.mkt.plan4workout.R;

import java.util.ArrayList;
import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanHolder> {
    private List<Plan> plans = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public PlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_item, parent, false);
        return new PlanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanHolder holder, int position) {
        Plan currentPlan = plans.get(position);
        holder.textViewTitle.setText(currentPlan.getName());
        holder.textViewDescription.setText(currentPlan.getDescription());
        holder.textViewPriority.setText(String.valueOf(currentPlan.getCateogry()));
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public void setPlans(List<Plan> plans) {
        this.plans = plans;
        notifyDataSetChanged();
    }

    public Plan getPlanAt(int position) {
        return plans.get(position);
    }

    class PlanHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public PlanHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(plans.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Plan plan);
        void onItemViewClick(View itemView, Plan plan);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
