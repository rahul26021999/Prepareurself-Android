package com.prepare.prepareurself.courses.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.prepare.prepareurself.R
import com.willy.ratingbar.BaseRatingBar
import com.willy.ratingbar.BaseRatingBar.OnRatingChangeListener
import com.willy.ratingbar.ScaleRatingBar
import kotlinx.android.synthetic.main.activity_demo.*


class DemoActivity : AppCompatActivity() {
    //simpleRatingBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        //setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}
