package com.prepare.prepareurself.quizv2.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.quizv2.ui.instructions.InstructionActivity;

public class QuizDetailActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialButton btn_practice , btn_quiz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);
        btn_practice=findViewById(R.id.tv_btnpractice);
        btn_quiz=findViewById(R.id.tv_btnquiz);
        btn_quiz.setOnClickListener(this);
        btn_practice.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_btnquiz : Intent intent=new Intent(getApplicationContext(), InstructionActivity.class);
                                    startActivity(intent);
                                    break;

            case R.id.tv_btnpractice :
                Toast.makeText(this,"abhi nah pata",Toast.LENGTH_LONG).show();

        }
    }
}
