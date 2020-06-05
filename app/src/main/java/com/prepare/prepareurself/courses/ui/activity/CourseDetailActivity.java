package com.prepare.prepareurself.courses.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.prepare.prepareurself.R;

public class CourseDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        backBtn=findViewById(R.id.backBtn);
        //backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:

                break;
        }
    }
}
