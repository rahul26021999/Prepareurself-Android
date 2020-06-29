package com.prepare.prepareurself.quizv2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.prepare.prepareurself.R
import com.prepare.prepareurself.quizv2.data.OptionsModel
import com.prepare.prepareurself.quizv2.viewmodel.QuizViewModel
import com.prepare.prepareurself.utils.*
import kotlinx.android.synthetic.main.activity_quiz2.*

class PracticeQuizActivity : BaseActivity(),PracticeQuizQuestionPagerAdapter.QuestionInteractor {

    private lateinit var vm : QuizViewModel
    private lateinit var pm:PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz2)

        vm = ViewModelProvider(this)[QuizViewModel::class.java]
        pm = PrefManager(this)

        timer_quiz.visibility = View.GONE
        tv_quiz_title.text = "Practice Quiz"

        vm.fetchQuiz(pm.getString(Constants.JWTTOKEN),1, Constants.EASY)

        vm.quizResponseBodyLiveData?.observe(this, Observer {
            if (it!=null){
                it.quiz?.let { it1 ->
                    val pagerAdapter  = it1.questions?.let { it2 ->
                        PracticeQuizQuestionPagerAdapter(this, it2, this)
                    }
                    quiz_view_pager.adapter = pagerAdapter
                    quiz_view_pager.offscreenPageLimit = 0

                    QuizActivity.quizId = it1.id!!

                    btn_next.setOnClickListener {
                        if (quiz_view_pager.currentItem +1 < it1.questions?.size!!){
                            quiz_view_pager.currentItem = quiz_view_pager.currentItem+1
                        }else if(quiz_view_pager.currentItem + 1 == it1.questions?.size){
                            Utility.showToast(this,"Practice Quiz completed")
                        }
                    }

                    quiz_view_pager.setOnPageChangeListener(object : CustomViewPager.OnPageChangeListener{
                        override fun onPageScrollStateChanged(state: Int) {

                        }

                        override fun onPageScrolled(
                                position: Int,
                                positionOffset: Float,
                                positionOffsetPixels: Int
                        ) {

                        }

                        override fun onPageSelected(position: Int) {

                            if (position+1 == it1.questions?.size){
                                btn_next.text = "Submit"
                            }

                        }
                    })

                }!!

            }
        })


    }

    override fun onOptionSeleted(options: OptionsModel) {
        btn_next.isEnabled = true
        optionId = options.id!!
        questionId = options.question_id!!
    }

    override fun onQuestionLoaded(position: Int) {
        btn_next.isEnabled = false
        tv_q_no.text = "Q${quiz_view_pager.currentItem+1}"
    }

    companion object{
        var quizId = 0
        var questionId = 0
        var optionId = -1
    }
}
