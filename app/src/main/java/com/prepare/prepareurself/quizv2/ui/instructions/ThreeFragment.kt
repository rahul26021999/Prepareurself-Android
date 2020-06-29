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
 * https://www.flaticon.com/packs/customer-service-29?k=1592509034691
 */
class ThreeFragment : Fragment() {
    lateinit var img_next_btn : ImageView
    lateinit var img_back_btn : ImageView
    lateinit var  onoptionClick : onOptionClickthree
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onoptionClick =context as onOptionClickthree
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_three, container, false)
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
                onoptionClick.onOptionNextThree()
            }

        })
        img_back_btn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onoptionClick.onOptionBackThree()
            }

        })
    }
    interface onOptionClickthree {
        fun onOptionNextThree()
        fun onOptionBackThree()
    }

}
