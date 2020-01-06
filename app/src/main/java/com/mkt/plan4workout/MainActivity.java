package com.mkt.plan4workout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mkt.plan4workout.Plan.Plan;
import com.mkt.plan4workout.Plan.PlanAdapter;
import com.mkt.plan4workout.Plan.PlanViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_PLAN_REQUEST = 1;
    public static final int EDIT_PLAN_REQUEST = 2;
    private PlanViewModel planViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonExercise = findViewById(R.id.button_exercise);
        buttonExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExerciseActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_plan);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditPlanActivity.class);
                startActivityForResult(intent, ADD_PLAN_REQUEST);
            }
        });

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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // drag and drop functionality
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                planViewModel.delete(adapter.getPlanAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Plan deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new PlanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Plan plan) {
                Intent intent = new Intent(MainActivity.this, AddEditPlanActivity.class);
                intent.putExtra(AddEditPlanActivity.EXTRA_ID, plan.getId());
                intent.putExtra(AddEditPlanActivity.EXTRA_TITLE, plan.getName());
                intent.putExtra(AddEditPlanActivity.EXTRA_DESCRIPTION, plan.getDescription());
                intent.putExtra(AddEditPlanActivity.EXTRA_PRIORITY, plan.getCateogry());
                startActivityForResult(intent, EDIT_PLAN_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PLAN_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditPlanActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPlanActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditPlanActivity.EXTRA_PRIORITY, 1);

            Plan plan = new Plan(title, description, String.valueOf(priority));
            planViewModel.insert(plan);

            Toast.makeText(this, "Plan saved", Toast.LENGTH_SHORT).show();
        } else  if (requestCode == EDIT_PLAN_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditPlanActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Plan can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditPlanActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditPlanActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditPlanActivity.EXTRA_PRIORITY, 1);

            Plan plan = new Plan(title, description, "fdafds");
            plan.setId(id);
            planViewModel.update(plan);

            Toast.makeText(this, "Plan updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Plan not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                planViewModel.deleteAllPlans();
                Toast.makeText(this, "All plans deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
