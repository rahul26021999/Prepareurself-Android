package com.prepare.prepareurself.forum.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.viewmodel.ForumViewModel
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.PrefManager
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.activity_replies.*
import kotlinx.android.synthetic.main.layout_topbar.*
import okhttp3.internal.Util

class RepliesActivity : BaseActivity() {

    private lateinit var vm:ForumViewModel
    private lateinit var pm:PrefManager
    private var queryId = -1
    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_replies)
        vm = ViewModelProvider(this)[ForumViewModel::class.java]
        pm = PrefManager(this)

        queryId = intent.getIntExtra(Constants.QUERYID,-1)
        query = intent.getStringExtra(Constants.QUERY)

        if(queryId!=-1){

            if (query.isNotEmpty()){
                tv_reply_question.text = query
            }

            initAdapter()
        }

        backBtn.setOnClickListener {
            finish()
        }

    }

    private fun initAdapter() {
        val adapter = RepliesAdapter(this)
        rv_replies.layoutManager = LinearLayoutManager(this)
        rv_replies.adapter = adapter

        vm.getRelies(pm.getString(Constants.JWTTOKEN),queryId,1)
                ?.observe(this, Observer {
                    if (it!=null){
                        it.query?.data?.let { it1 -> adapter.setData(it1) }
                    }else{
                        Utility.showToast(this,Constants.SOMETHINGWENTWRONG)
                    }
                })

    }
}
