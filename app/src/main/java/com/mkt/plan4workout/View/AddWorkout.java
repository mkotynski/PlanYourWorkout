package com.mkt.plan4workout.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mkt.plan4workout.Model.Plan.Plan;
import com.mkt.plan4workout.R;
import com.mkt.plan4workout.View.Adapter.PlanAdapter;
import com.mkt.plan4workout.ViewModel.PlanViewModel;

import java.util.List;

public class AddWorkout extends AppCompatActivity {

    public static final String EXTRA_DATE = "com.mkt.plan4workout.EXTRA_DATE";
    public static final String EXTRA_PLAN_ID= "com.mkt.plan4workout.EXTRA_PLAN_ID";
    public static final String EXTRA_PLAN_ADD= "com.mkt.plan4workout.EXTRA_PLAN_ADD";

    private PlanViewModel planViewModel;
    String values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final PlanAdapter adapter = new PlanAdapter();
        recyclerView.setAdapter(adapter);

        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);

        planViewModel.getAllPlans().observe(this, new Observer<List<Plan>>() {
            @Override
            public void onChanged(List<Plan> plans) {
                adapter.setPlans(plans);
            }
        });

       values = getIntent().getExtras().getString(EXTRA_DATE);

        adapter.setOnItemClickListener(new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Plan plan) {
                saveWorkout(plan.getId());
            }
            @Override
            public void onItemViewClick(View itemView, Plan plan){

            }
        });

    }

    private void saveWorkout(int id){
        Intent data = new Intent();
        data.putExtra(EXTRA_PLAN_ID, id);
        data.putExtra(EXTRA_DATE, values);
        data.putExtra(EXTRA_PLAN_ADD, 1);
        setResult(1, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(0);
        finish();
    }
}
