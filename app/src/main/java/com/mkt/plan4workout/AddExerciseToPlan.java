package com.mkt.plan4workout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mkt.plan4workout.Exercise.Exercise;
import com.mkt.plan4workout.Exercise.ExerciseAdapter;
import com.mkt.plan4workout.Exercise.ExerciseViewModel;
import com.mkt.plan4workout.Plan.Plan;
import com.mkt.plan4workout.Plan.PlanAdapter;

import java.util.List;

public class AddExerciseToPlan extends AppCompatActivity {

    public static final int ADD_PLAN_REQUEST = 1;
    public static final int EDIT_PLAN_REQUEST = 2;
    private Context context;
    ExerciseViewModel exerciseViewModel;
    AddExercisesToPlanViewModel addExercisesToPlanViewModel;
    Button btnAddExercisesToPlan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercises_to_plan);

        btnAddExercisesToPlan=findViewById(R.id.button_save_choosen_exercises);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ExerciseAdapter adapter = new ExerciseAdapter();
        recyclerView.setAdapter(adapter);

        addExercisesToPlanViewModel = ViewModelProviders.of(this).get(AddExercisesToPlanViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);

        exerciseViewModel.getAllExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                adapter.setExercises(exercises);
            }
        });

        adapter.setOnItemClickListener(new ExerciseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Exercise exercise) { }

            @Override
            public void onItemViewClick(View itemView,Exercise exercise) {
                if(addExercisesToPlanViewModel.isChoosen(exercise)){
                    addExercisesToPlanViewModel.delExercise(exercise);
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorWhite));
                    TextView title = itemView.findViewById(R.id.text_view_title);
                    title.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorBlack));
                } else {
                    addExercisesToPlanViewModel.addExercise(exercise);
                    itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary));
                    TextView title = itemView.findViewById(R.id.text_view_title);
                    title.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorWhite));
                }
            }
        });
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
            case R.id.delete_all_exercises:
                exerciseViewModel.deleteAllExercises();
                Toast.makeText(this, "All exercises deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
