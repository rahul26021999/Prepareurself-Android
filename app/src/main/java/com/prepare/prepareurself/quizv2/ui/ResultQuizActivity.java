package com.prepare.prepareurself.quizv2.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.prepare.prepareurself.R;

public class ResultQuizActivity extends AppCompatActivity {
    TextView tv_score , tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_quiz);
        tv_score=findViewById(R.id.correctsccore);
        tv_title=findViewById(R.id.title);
        tv_title.setText("Result");
        //getIntent();
        Intent intent = getIntent();
        int score=intent.getIntExtra("score",0);
        tv_score.setText(""+score+"/ 5");

    }
}
