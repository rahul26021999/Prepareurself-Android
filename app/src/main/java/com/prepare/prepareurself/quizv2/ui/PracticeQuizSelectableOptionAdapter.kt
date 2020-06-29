package com.prepare.prepareurself.quizv2.ui

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prepare.prepareurself.R
import com.prepare.prepareurself.quizv2.data.OptionsModel
import com.prepare.prepareurself.quizv2.data.QuestionModel

class PracticeQuizSelectableOptionAdapter(context: Context, interactor:OptionListener) : RecyclerView.Adapter<PracticeQuizSelectableOptionAdapter.OptiosViewHolder>() {

    private var options:List<OptionsModel>?=null
    private var listener:OptionListener?=null
    private var selectedOptionPosition =-1
    private var correctOptionId = -1
    private var correctOptionPosition = -2
    private var question:QuestionModel?=null
    private var mContext: Context?=null
    private var selected = false

    init {
        listener = interactor
        mContext = context
    }

    fun setData(data:QuestionModel){
        options = data.option
        question = data
        notifyDataSetChanged()
    }

    fun clearResponse(){
        selectedOptionPosition = -1
        correctOptionId = -1
        notifyDataSetChanged()
    }

    fun updateSelection(correctOption:Int){
        correctOptionId = correctOption
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptiosViewHolder {
        return OptiosViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.quiz_option_adapter_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return options?.size ?: 0
    }

    override fun onBindViewHolder(holder: OptiosViewHolder, position: Int) {
        options?.get(position)?.let {
            holder.bindView(it, position)
        }

        holder.view?.setOnClickListener{
            if (!selected){
                selected = true
                selectedOptionPosition = position
                when (options?.get(position)?.id) {
                    question?.answer?.option_id -> {
                        correctOptionPosition = position
                    }
                    else -> {
                        var pos = -1
                        options?.forEachIndexed { index, optionsModel ->
                            if (optionsModel.id == question?.answer?.option_id){
                                pos = index
                            }
                        }
                        correctOptionPosition = pos
                    }
                }
                options?.get(position)?.let { it1 -> listener?.onOptionSelected(it1) }
                notifyDataSetChanged()
            }
        }

        if (selectedOptionPosition == position){
            if (selectedOptionPosition != correctOptionPosition){

                holder.lin_quiz_option_base?.background = mContext?.getDrawable(R.drawable.red_rounded_stroked_rectangle)
                holder.rel_option_letter?.background = mContext?.getDrawable(R.color.lightred)
                holder.quiz_option_letter?.setTextColor(mContext?.getColor(R.color.white)!!)
                holder.tv_option_text?.setTextColor(mContext?.getColor(R.color.white)!!)
            }else{

                holder.lin_quiz_option_base?.background = mContext?.getDrawable(R.drawable.green_rounded_stroked_rectangle)
                holder.rel_option_letter?.background = mContext?.getDrawable(R.color.like_blue)
                holder.quiz_option_letter?.setTextColor(mContext?.getColor(R.color.white)!!)
                holder.tv_option_text?.setTextColor(mContext?.getColor(R.color.white)!!)
            }

        } else{
            holder.lin_quiz_option_base?.background = mContext?.getDrawable(R.drawable.grey_rounded_stroked_rectangle)
            holder.rel_option_letter?.background = mContext?.getDrawable(R.color.grey)
            holder.quiz_option_letter?.setTextColor(mContext?.getColor(R.color.colorPrimaryDark)!!)
            holder.tv_option_text?.setTextColor(mContext?.getColor(R.color.colorPrimaryDark)!!)
        }

        if (correctOptionPosition == position){
            holder.lin_quiz_option_base?.background = mContext?.getDrawable(R.drawable.green_rounded_stroked_rectangle)
            holder.rel_option_letter?.background =  mContext?.getDrawable(R.color.like_blue)
            holder.quiz_option_letter?.setTextColor(mContext?.getColor(R.color.white)!!)
            holder.tv_option_text?.setTextColor(mContext?.getColor(R.color.white)!!)
        }

    }

    class OptiosViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

        var view: View?=null
        var quiz_option_letter:TextView?=null
        var tv_option_text:TextView?=null
        var lin_quiz_option_base:LinearLayout?=null
        var rel_option_letter:RelativeLayout?=null
        init {
            view = itemView
            tv_option_text = view?.findViewById(R.id.tv_option_text)
            quiz_option_letter = view?.findViewById(R.id.quiz_option_letter)
            lin_quiz_option_base = view?.findViewById(R.id.lin_quiz_option_base)
            rel_option_letter = view?.findViewById(R.id.rel_option_letter)
        }

        fun bindView(options: OptionsModel, position: Int) {
            when(position){
                0-> quiz_option_letter?.text = "A"
                1-> quiz_option_letter?.text = "B"
                2-> quiz_option_letter?.text = "C"
                3-> quiz_option_letter?.text = "D"
            }
            tv_option_text?.text = Html.fromHtml(options.option).toString().trim()

        }

    }

    interface OptionListener{
        fun onOptionSelected(options: OptionsModel)
    }

}