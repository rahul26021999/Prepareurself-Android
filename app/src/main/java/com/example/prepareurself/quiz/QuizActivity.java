package com.example.prepareurself.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prepareurself.R;
import com.example.prepareurself.quiz.adapters.OptionRecyclerViewAdapter;
import com.example.prepareurself.quiz.model.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements OptionRecyclerViewAdapter.OptionInteractor {

    TextView tvTimer, tvQuestionNo, tvQuestionText, tvNextQuestionTimer, tvFirstQuestionTimer;
    Button btnNext;
    RecyclerView optionsRv;

    CountDownTimer timer, firstQuestionTimer, nextQuestionTimer;

    int counter = 10;
    int questionIndex = 0;
    int nextQuestionCounter = 10;
    int firstQuestionCounter = 10;

    List<Question> questions;

    OptionRecyclerViewAdapter adapter;

    int selectedPosition = -1;

    ConstraintLayout consNextQuestionTimer, consQuestionDisplay, consFirstQuestionTimerDisplay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        initViews();

        initUI();

        makeQuestions();

        questionIndex = 0;

        showFirstQuestionTimer();

    }

    private void showFirstQuestionTimer() {
        consFirstQuestionTimerDisplay.setVisibility(View.VISIBLE);
        consQuestionDisplay.setVisibility(View.GONE);

        firstQuestionTimer = new CountDownTimer(10000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                --firstQuestionCounter;
                tvFirstQuestionTimer.setText(firstQuestionCounter +"");
            }

            @Override
            public void onFinish() {
                consFirstQuestionTimerDisplay.setVisibility(View.VISIBLE);
                consQuestionDisplay.setVisibility(View.VISIBLE);

                loadQuestion(questionIndex);

            }
        }.start();

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

        setUpTimer();

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
                counter=10;

                if (questionIndex>=0 && questionIndex<= questions.size() -1){
                    displayResult(questionIndex);
                    showNextQustionTimer();
                }

            }
        }.start();
    }

    private void showNextQustionTimer() {
  //      consQuestionDisplay.setVisibility(View.VISIBLE);
        consNextQuestionTimer.setVisibility(View.VISIBLE);



        nextQuestionTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                --nextQuestionCounter;
                tvNextQuestionTimer.setText(nextQuestionCounter+"");
            }

            @Override
            public void onFinish() {
                consNextQuestionTimer.setVisibility(View.GONE);
//                consQuestionDisplay.setVisibility(View.VISIBLE);

                nextQuestionCounter = 10;

                questionIndex+=1;
                if (questionIndex >= 0 && questionIndex <= questions.size() - 1){
                    loadQuestion(questionIndex);
                }

            }
        }.start();

    }

    private void displayResult(int questionIndex) {
        Question question = questions.get(questionIndex);

        Log.d("selected_position_debug",selectedPosition+"");

        if (this.selectedPosition>=0){
            View selectedView = optionsRv.findViewHolderForAdapterPosition(this.selectedPosition).itemView;
            if (selectedPosition+1 == question.correctOption)
                selectedView.setBackground(getResources().getDrawable(R.drawable.correct_option_background));
            else{
                selectedView.setBackground(getResources().getDrawable(R.drawable.incorrect_option_background));
                View correctView = optionsRv.findViewHolderForAdapterPosition(question.correctOption-1).itemView;
                correctView.setBackground(getResources().getDrawable(R.drawable.correct_option_background));
            }
            this.selectedPosition = -1;
        }else{
            View correctView = optionsRv.findViewHolderForAdapterPosition(question.correctOption-1).itemView;
            correctView.setBackground(getResources().getDrawable(R.drawable.correct_option_background));
        }

    }

    private void initUI() {
        btnNext.setVisibility(View.GONE);
        consQuestionDisplay.setVisibility(View.GONE);
        consNextQuestionTimer.setVisibility(View.GONE);
        consFirstQuestionTimerDisplay.setVisibility(View.VISIBLE);
    }

    private void initViews() {
        tvTimer = findViewById(R.id.tv_quiz_timer);
        tvQuestionNo = findViewById(R.id.tv_question_number);
        tvQuestionText = findViewById(R.id.tv_question_quiz);
        btnNext = findViewById(R.id.btn_next_quiz);
        optionsRv = findViewById(R.id.rv_options_quiz);
        consNextQuestionTimer = findViewById(R.id.cons_next_question_timer_layout);
        consQuestionDisplay = findViewById(R.id.cons_question_display);
        consFirstQuestionTimerDisplay = findViewById(R.id.cons_first_question_startsin_layout);
        tvNextQuestionTimer = findViewById(R.id.tv_counter_quiz);
        tvFirstQuestionTimer = findViewById(R.id.tv_first_counter_quiz);

    }

    @Override
    public void onItemSelected(int position) {
        this.selectedPosition = position;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (nextQuestionTimer!=null)
            nextQuestionTimer.cancel();
        if (timer!=null)
            timer.cancel();
        if (firstQuestionTimer!=null)
            firstQuestionTimer.cancel();
    }
}
