package com.prepare.prepareurself.quizv2.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.prepare.prepareurself.R
import com.prepare.prepareurself.quizv2.data.OptionsModel
import com.prepare.prepareurself.quizv2.data.ResponsesModel
import com.prepare.prepareurself.quizv2.viewmodel.QuizViewModel
import com.prepare.prepareurself.utils.*
import kotlinx.android.synthetic.main.activity_course_detail.*
import kotlinx.android.synthetic.main.activity_quiz2.*
import java.util.ArrayList


class QuizActivity : BaseActivity(),QuizQuestionPagerAdapter.QuestionInteractor {

    private lateinit var quizViewModel:QuizViewModel
    private lateinit var timer:CountDownTimer
    private lateinit var pm:PrefManager
    private var courseId = 0
    private lateinit var responses:ArrayList<ResponsesModel>
    private var courseName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz2)

        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        pm = PrefManager(this)

        responses = ArrayList<ResponsesModel>()

        courseId = intent.getIntExtra(Constants.COURSEID,0)
        courseName = intent.getStringExtra(Constants.COURSENAME)
//        quizViewModel.fetchQuiz(pm.getString(Constants.JWTTOKEN),1,Constants.EASY)
        if (courseId!=0){
            quizViewModel.fetchQuiz(pm.getString(Constants.JWTTOKEN),courseId,Constants.EASY)
            if (courseName.isNullOrEmpty())
                tv_quiz_title.text = "Quiz"
            else
                tv_quiz_title.text = "$courseName Quiz"
        }

        timer = object :CountDownTimer(30000,1000){
            override fun onFinish() {
               btn_next.performClick() //ray why on tmer?
            }

            override fun onTick(millisUntilFinished: Long) {
                val minutes: Long = millisUntilFinished / 1000 / 60
                val seconds: Long = millisUntilFinished / 1000 % 60

                timer_quiz.text = "$minutes : $seconds"
            }
        }


        quizViewModel.quizResponseBodyLiveData?.observe(this, Observer { it ->
            if (it!=null){
                it.quiz?.let {
                    val pagerAdapter  = it.questions?.let { QuizQuestionPagerAdapter(this, it, this, quizViewModel) }
                    quiz_view_pager.adapter = pagerAdapter
                    quiz_view_pager.offscreenPageLimit = 0

                    quizId = it.id!!

                    btn_next.setOnClickListener {it1->
                        if (quiz_view_pager.currentItem +1 < it.questions?.size!!){
                            if (optionId!=-1){
                                val responsesModel = ResponsesModel()
                                responsesModel.answer_id = optionId
                                responsesModel.question_id = questionId
                                responses.add(responsesModel)
                                optionId = -1
                                quizViewModel.submitIndividualQuiz(pm.getString(Constants.JWTTOKEN), quizId,courseId, questionId, optionId)
                            }
                            quiz_view_pager.currentItem = quiz_view_pager.currentItem+1
                        }else if (quiz_view_pager.currentItem + 1 == it.questions?.size){ //ray
                            if (optionId!=-1){
                                val responsesModel = ResponsesModel()
                                responsesModel.answer_id = optionId
                                responsesModel.question_id = questionId
                                responses.add(responsesModel)
                                optionId = -1
                            }
                            quizViewModel.submitQuiz(pm.getString(Constants.JWTTOKEN), quizId,responses)
                                    ?.observe(this, Observer {
                                        if (it!=null){
                                            if (it.error_code == 0){
                                                Utility.showToast(this,"Quiz submitted successfully!")
                                                //it.score
                                                Log.d("TAGSCORE",""+it.score)

                                            }else{
                                                Utility.showToast(this,"There was an error in submitting quiz!")
                                            }
                                            finish()
                                        }else{
                                            Utility.showToast(this, Constants.SOMETHINGWENTWRONG)
                                        }
                                    })
                        }

                    }

                    quiz_view_pager.setOnPageChangeListener(object :CustomViewPager.OnPageChangeListener{
                        override fun onPageScrollStateChanged(state: Int) {

                        }

                        override fun onPageScrolled(
                                position: Int,
                                positionOffset: Float,
                                positionOffsetPixels: Int
                        ) {

                        }

                        override fun onPageSelected(position: Int) {

                            if (position+1 == it.questions?.size){
                                btn_next.text = "Submit"
                            }

                        }
                    })

                }!!
            }else{
                Utility.showToast(this@QuizActivity,Constants.SOMETHINGWENTWRONG)
                finish()
            }
        })
    }

    override fun onOptionSeleted(options: OptionsModel) {
        btn_next.isEnabled = true
        optionId = options.id!!
        questionId = options.question_id!!
    }

    override fun onQuestionLoaded(position: Int) {
        timer.cancel()
        timer.start()
        btn_next.isEnabled = false
        tv_q_no.text = "Q${quiz_view_pager.currentItem+1}"
        Log.d("question_loaded","$position question loade")
    }

    companion object{
        var quizId = 0
        var questionId = 0
        var optionId = -1
    }

}
