package com.example.prepareurself.quiz;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prepareurself.R;
import com.example.prepareurself.quiz.adapters.OptionRecyclerViewAdapter;
import com.example.prepareurself.quiz.model.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements OptionRecyclerViewAdapter.OptionInteractor {

    TextView tvTimer, tvQuestionNo, tvQuestionText;
    Button btnNext;
    RecyclerView optionsRv;

    CountDownTimer timer;

    int counter = 10;
    int questionIndex = 0;

    List<Question> questions;

    OptionRecyclerViewAdapter adapter;

    int selectedPosition = -1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        initViews();

        initUI();

       // setUpOptionClicked();

        makeQuestions();

        setUpTimer();

        questionIndex = 0;

        loadQuestion(questionIndex);

    }

    private void makeQuestions() {
        questions = new ArrayList<>();

        HashMap<Integer,String> options1 = new HashMap<>();
        options1.put(1,"This is option 1");
        options1.put(2,"This is option 2");
        options1.put(3,"This is option 3");
        options1.put(4,"This is option 4");
        Question question1 = new Question("1",
                "This is Question 1",2,options1);

        HashMap<Integer,String> options2 = new HashMap<>();
        options2.put(1,"This is option 1");
        options2.put(2,"This is option 2");
        options2.put(3,"This is option 3");
        options2.put(4,"This is option 4");
        Question question2 = new Question("2",
                "This is Question 2",3,options2);

        HashMap<Integer,String> options3 = new HashMap<>();
        options3.put(1,"This is option 1");
        options3.put(2,"This is option 2");
        options3.put(3,"This is option 3");
        options3.put(4,"This is option 4");
        Question question3 = new Question("3",
                "This is Question 3",1,options3);

        HashMap<Integer,String> options4 = new HashMap<>();
        options4.put(1,"This is option 1");
        options4.put(2,"This is option 2");
        options4.put(3,"This is option 3");
        options4.put(4,"This is option 4");
        Question question4 = new Question("4",
                "This is Question 4",4,options4);

        HashMap<Integer,String> options5 = new HashMap<>();
        options5.put(1,"This is option 1");
        options5.put(2,"This is option 2");
        options5.put(3,"This is option 3");
        options5.put(4,"This is option 4");
        Question question5 = new Question("5",
                "This is Question 5",2,options5);

        questions.clear();

        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        questions.add(question4);
        questions.add(question5);

    }

    private void loadQuestion(int i) {

        Question question = questions.get(i);
        tvQuestionNo.setText("Question "+question.questionNo);
        tvQuestionText.setText(question.questionText);

        setUpOptions(question.options);

        timer.start();

    }

    private void setUpOptions(HashMap<Integer, String> options) {
        adapter = new OptionRecyclerViewAdapter(QuizActivity.this,options, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(QuizActivity.this);
        optionsRv.setLayoutManager(layoutManager);
        optionsRv.setAdapter(adapter);
    }

    private void setUpTimer() {
        timer = new CountDownTimer(10000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                --counter;
                tvTimer.setText("Time Left : "+counter);
                Log.d("count_down",counter+"");
            }

            @Override
            public void onFinish() {

                displayResult(questionIndex);

//                counter=10;
//                questionIndex+=1;
//                if (questionIndex > 0 && questionIndex <= questions.size() - 1){
//                    loadQuestion(questionIndex);
//                }
            }
        };
    }

    private void displayResult(int questionIndex) {
        Question question = questions.get(questionIndex);

        if (this.selectedPosition!=-1){
            View selectedView = optionsRv.findViewHolderForAdapterPosition(this.selectedPosition).itemView;
            if (selectedPosition+1 == question.correctOption)
                selectedView.setBackground(getResources().getDrawable(R.drawable.correct_option_background));
            else{
                selectedView.setBackground(getResources().getDrawable(R.drawable.incorrect_option_background));
                View correctView = optionsRv.findViewHolderForAdapterPosition(question.correctOption-1).itemView;
                correctView.setBackground(getResources().getDrawable(R.drawable.correct_option_background));
            }
        }

    }

    private void initUI() {
        btnNext.setVisibility(View.GONE);
    }

    private void initViews() {
        tvTimer = findViewById(R.id.tv_quiz_timer);
        tvQuestionNo = findViewById(R.id.tv_question_number);
        tvQuestionText = findViewById(R.id.tv_question_quiz);
        btnNext = findViewById(R.id.btn_next_quiz);
        optionsRv = findViewById(R.id.rv_options_quiz);
    }

    @Override
    public void onItemSelected(int position) {
        this.selectedPosition = position;
    }
}
