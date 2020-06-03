package com.prepare.prepareurself.quizv2.ui

import android.os.Bundle
import android.os.CountDownTimer
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.prepare.prepareurself.R
import com.prepare.prepareurself.quizv2.data.OptionsModel
import com.prepare.prepareurself.quizv2.viewmodel.QuizViewModel
import com.prepare.prepareurself.utils.BaseActivity
import kotlinx.android.synthetic.main.activity_quiz2.*


class QuizActivity : BaseActivity(),QuizQuestionPagerAdapter.QuestionInteractor {

    private lateinit var quizViewModel:QuizViewModel
    private lateinit var timer:CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz2)

        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]

        quizViewModel.createData()

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


        quizViewModel.quizResponseBodyLiveData.observe(this, Observer {
            it?.let {
                val pagerAdapter  = it.questions?.let { QuizQuestionPagerAdapter(this, it, this, quizViewModel) }
                quiz_view_pager.adapter = pagerAdapter
              //  quiz_view_pager.offscreenPageLimit = it.questions?.size!!

                btn_next.setOnClickListener {it1->
                    if (quiz_view_pager.currentItem +1 < it.questions?.size!!){
                        quiz_view_pager.currentItem = quiz_view_pager.currentItem+1
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
        })
    }

    override fun onOptionSeleted(options: OptionsModel) {
        btn_next.isEnabled = true
    }

    override fun onQuestionLoaded(position: Int) {
        timer.cancel()
        timer.start()
        btn_next.isEnabled = false

    }
}
