package com.prepare.prepareurself.quizv2.ui.instructions

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.prepare.prepareurself.R
//import layout.onOptionClick

/**
 * A simple [Fragment] subclass.
 */
class TwoFragment : Fragment() {
    lateinit var img_next_btn : ImageView
    lateinit var img_back_btn : ImageView
    lateinit var  onoptionClick : onOptionClick
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onoptionClick =context as onOptionClick
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindviews(view) //whever u inflate something u need to use view
        clicklisteners()

    }
    private fun bindviews(view: View) {
        img_next_btn=view.findViewById(R.id.next_btn)
        img_back_btn=view.findViewById(R.id.back_btn)
    }

    private fun clicklisteners() {
        img_next_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onoptionClick.onOptionNext()
            }

        })
        img_back_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onoptionClick.onOptionBack()
            }

        })
    }
    interface onOptionClick {
        fun onOptionNext()
        fun onOptionBack()
    }

}
