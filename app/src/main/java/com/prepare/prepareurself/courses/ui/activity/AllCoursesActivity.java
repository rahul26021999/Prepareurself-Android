package com.prepare.prepareurself.courses.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.courses.ui.adapters.AllCoursesRvAdapter;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.viewmodel.DashboardViewModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.DividerItemDecoration;
import com.prepare.prepareurself.utils.GridSpaceItemDecoration;
import com.prepare.prepareurself.utils.PrefManager;

import java.util.List;

public class AllCoursesActivity extends AppCompatActivity implements AllCoursesRvAdapter.CourseRvListener, View.OnClickListener {

    RecyclerView recyclerView;
    private DashboardViewModel mViewModel;
    private PrefManager prefManager;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);

        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        recyclerView = findViewById(R.id.rv_allcourses);
        backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(this);

        prefManager = new PrefManager(this);

        mViewModel.getCourses(prefManager.getString(Constants.JWTTOKEN));

        final AllCoursesRvAdapter adapter = new AllCoursesRvAdapter(AllCoursesActivity.this, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2,RecyclerView.VERTICAL,false);
        recyclerView.addItemDecoration(new GridSpaceItemDecoration(30));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        mViewModel.getLiveCourses().observe(this, new Observer<List<CourseModel>>() {
            @Override
            public void onChanged(List<CourseModel> courseModels) {
                adapter.setCourseModels(courseModels);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCourseClicked(CourseModel courseModel) {
        Intent intent = new Intent(AllCoursesActivity.this, CoursesActivity.class);
        intent.putExtra(Constants.COURSEID, courseModel.getId());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
