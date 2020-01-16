package com.mkt.plan4workout.Plan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.tvPlanName.setText(currentPlan.getName());
        holder.tvPlanDescription.setText(currentPlan.getDescription());
        holder.tvPlanCategory.setText(String.valueOf(currentPlan.getCategory()));
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
        private TextView tvPlanName;
        private TextView tvPlanCategory;
        private TextView tvPlanDescription;

        public PlanHolder(View itemView) {
            super(itemView);
            tvPlanName = itemView.findViewById(R.id.tv_plan_name);
            tvPlanCategory = itemView.findViewById(R.id.tv_plan_category);
            tvPlanDescription = itemView.findViewById(R.id.tv_plan_description);

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
