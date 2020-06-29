package com.prepare.prepareurself.quizv2.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import com.prepare.prepareurself.R
import com.prepare.prepareurself.quiz.model.Question
import com.prepare.prepareurself.quizv2.data.OptionsModel
import com.prepare.prepareurself.quizv2.data.QuestionModel
import com.prepare.prepareurself.quizv2.viewmodel.QuizViewModel
import kotlinx.android.synthetic.main.quiz_pager_adapter_layout.view.*

class PracticeQuizQuestionPagerAdapter(var context: Context,
                                       var questions:List<QuestionModel>,
                                       var listener:QuestionInteractor
                ): PagerAdapter(),
                    PracticeQuizSelectableOptionAdapter.OptionListener {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return questions.size
    }
    //ray like bindvew?
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.quiz_pager_adapter_layout,container,false)
        val question = questions[position] //ray

        questionId = question.id!!

        view.tv_quiz_q_no.text = "${position+1}"

        view.tv_quiz_q_text.loadData(question.question,"text/html","UTF-8")


        val optionAdapter = PracticeQuizSelectableOptionAdapter(context,this)
        view.rv_options_quiz.layoutManager = LinearLayoutManager(context)
        view.rv_options_quiz.adapter = optionAdapter

        question.option?.let { optionAdapter.setData(question) }

        if (selectedOption!=-1){
            question.answer?.option_id?.let { optionAdapter.updateSelection(it) }
        }

        listener.onQuestionLoaded(position)

//        view.tv_clear_response.setOnClickListener {
//            listener.onClearResponse(question)
//            optionAdapter.clearResponse()
//        }
//
//        listener.onQuestionDisplayed(question)

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun onOptionSelected(options: OptionsModel) {
        listener.onOptionSeleted(options)
    }

    interface QuestionInteractor{
        fun onOptionSeleted(options: OptionsModel)
        fun onQuestionLoaded(position: Int)
    }

    companion object{
        var questionId = -1
        var selectedOption = -1
    }

}