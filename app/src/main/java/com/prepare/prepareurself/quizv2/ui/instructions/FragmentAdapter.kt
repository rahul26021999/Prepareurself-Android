package com.prepare.prepareurself.quizv2.ui.instructions

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FragmentAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){
    override fun getItem(position: Int): Fragment {
        //0 1 2
        when (position) {
            0 -> {
                return OneFragment()
            }
            1 -> {
                return TwoFragment()
            }
            2 -> {
                return ThreeFragment()
            }
            //null gvng error
            else -> throw IllegalStateException("position $position is invalid for this viewpager")
        }


    }

    override fun getCount(): Int {
        return 3
    }

}