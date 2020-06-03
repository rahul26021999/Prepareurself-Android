package com.prepare.prepareurself.quizv2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz2)

        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]

        quizViewModel.createData()

        chronometer_quiz.start()

        quizViewModel.quizResponseBodyLiveData.observe(this, Observer {
            it?.let {
                val pagerAdapter  = it.questions?.let { QuizQuestionPagerAdapter(this, it, this, quizViewModel) }
                quiz_view_pager.adapter = pagerAdapter
                quiz_view_pager.offscreenPageLimit = it.questions?.size!!

                card_next_q.setOnClickListener {it1->
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
                            tv_next_btn.text = "Submit"
                        }

                    }
                })

            }
        })
    }

    override fun onOptionSeleted(options: OptionsModel) {

    }
}
