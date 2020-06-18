package com.prepare.prepareurself.quizv2.ui.instructions

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager
import com.prepare.prepareurself.R
import com.prepare.prepareurself.quizv2.ui.QuizActivity
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import kotlinx.android.synthetic.main.activity_instruction.*

class InstructionActivity : BaseActivity() , OneFragment.onNextClick , TwoFragment.onOptionClick , ThreeFragment.onOptionClickthree{
    lateinit var viewpager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instruction)
        bindviews()
    }

    private fun bindviews() {
        viewpager =findViewById(R.id.vwpager)
        val adapter =FragmentAdapter(supportFragmentManager) //in java getSupportFRAgmentManager
        vwpager.adapter=adapter
    }

    override fun onClick() {
        viewpager.currentItem=1
        Log.d("BOARDCLC","clcked")
    }

    override fun onOptionNext() {
        viewpager.currentItem=2
    }

    override fun onOptionBack() {
        viewpager.currentItem=0
        Log.d("BOARDCLC","clcked bac two")
    }

    override fun onOptionNextThree() {

        val intent= Intent(this@InstructionActivity,QuizActivity::class.java)
        intent.putExtra(Constants.COURSENAME,"Android")
        intent.putExtra(Constants.COURSEID,1)
        startActivity(intent)
    }

    override fun onOptionBackThree() {
        viewpager.currentItem=1
    }


}
