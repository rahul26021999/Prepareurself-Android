package com.prepare.prepareurself.quizv2.ui

import android.os.Bundle
import android.os.CountDownTimer
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.prepare.prepareurself.R
import com.prepare.prepareurself.quizv2.data.OptionsModel
import com.prepare.prepareurself.quizv2.data.ResponsesModel
import com.prepare.prepareurself.quizv2.viewmodel.QuizViewModel
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.PrefManager
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_quiz2.*
import java.util.ArrayList


class QuizActivity : BaseActivity(),QuizQuestionPagerAdapter.QuestionInteractor {

    private lateinit var quizViewModel:QuizViewModel
    private lateinit var timer:CountDownTimer
    private lateinit var pm:PrefManager
    private var courseId = 0
    private lateinit var responses:ArrayList<ResponsesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz2)

        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        pm = PrefManager(this)

        responses = ArrayList<ResponsesModel>()

        courseId = intent.getIntExtra(Constants.COURSEID,0)

        quizViewModel.fetchQuiz(pm.getString(Constants.JWTTOKEN),1,Constants.EASY)

        timer = object :CountDownTimer(30000,1000){
            override fun onFinish() {
               btn_next.performClick()
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
                    //  quiz_view_pager.offscreenPageLimit = it.questions?.size!!

                    quizId = it.id!!

                    btn_next.setOnClickListener {it1->
                        if (quiz_view_pager.currentItem +1 < it.questions?.size!!){
                            val responsesModel = ResponsesModel()
                            responsesModel.optionId = optionId
                            responsesModel.questionId = questionId
                            responses.add(responsesModel)
                            quizViewModel.submitIndividualQuiz(pm.getString(Constants.JWTTOKEN), quizId,courseId, questionId, optionId)
                            quiz_view_pager.currentItem = quiz_view_pager.currentItem+1
                        }else if (quiz_view_pager.currentItem + 1 == it.questions?.size){
                            val responsesModel = ResponsesModel()
                            responsesModel.optionId = optionId
                            responsesModel.questionId = questionId
                            responses.add(responsesModel)
                            quizViewModel.submitQuiz(pm.getString(Constants.JWTTOKEN), quizId,responses)
                        }

                    }

                    quiz_view_pager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
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

                }
            }else{
                Utility.showToast(this@QuizActivity,Constants.SOMETHINGWENTWRONG)
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
    }

    companion object{
        var quizId = 0
        var questionId = 0
        var optionId = -1
    }

}
