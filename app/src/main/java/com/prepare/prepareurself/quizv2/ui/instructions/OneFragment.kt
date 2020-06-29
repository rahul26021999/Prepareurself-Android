package com.prepare.prepareurself.quizv2.ui.instructions

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.prepare.prepareurself.R

/**
 * A simple [Fragment] subclass.
 */
class OneFragment : Fragment() {
    lateinit var btn_next : ImageView
    lateinit var onnextClick: onNextClick
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onnextClick=context as onNextClick //typecastng

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindviews(view)
    }
    private fun bindviews(view: View) {
        btn_next=view.findViewById(R.id.arrow_next)
        clicklisteners()
    }
    private fun clicklisteners() {
        btn_next.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onnextClick.onClick()
            }

        })
    }
    interface onNextClick {
        fun onClick()
    }

}
