package com.prepare.prepareurself.forum.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.data.QueryModel
import com.prepare.prepareurself.forum.viewmodel.ForumViewModel
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.PrefManager
import com.prepare.prepareurself.utils.Utility
import jp.wasabeef.richeditor.RichEditor
import kotlinx.android.synthetic.main.activity_forum2.*
import kotlinx.android.synthetic.main.activity_forum2.view.*
import kotlinx.android.synthetic.main.forum_add_query_dialog.*
import kotlinx.android.synthetic.main.forum_add_query_dialog.view.*
import kotlinx.android.synthetic.main.layout_topbar.*
import kotlinx.android.synthetic.main.richeditor_layout.*
import kotlinx.android.synthetic.main.richeditor_layout.view.*


class ForumActivity : BaseActivity(), QueriesAdapter.QueriesListener {

    private var htmlData = ""
    private lateinit var vm:ForumViewModel
    private lateinit var pm:PrefManager
    private var courseId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum2)

        vm = ViewModelProvider(this)[ForumViewModel::class.java]

        pm = PrefManager(this)


        courseId = intent.getIntExtra(Constants.COURSEID,-1)

        btn_ask_query.setOnClickListener {
            showPostQueryDialog()
        }

        initQueryAdapter()

        backBtn.setOnClickListener {
            finish()
        }

    }

    private fun initQueryAdapter() {
        val adapter = QueriesAdapter(this, this)
        rv_queries.layoutManager = LinearLayoutManager(this)
        rv_queries.adapter = adapter

        vm.getQueries(pm.getString(Constants.JWTTOKEN),courseId,1)
                ?.observe(this, Observer {
                    if (it!=null){
                        it.queries?.data?.let { it1 -> adapter.setData(it1) }
                    }else{
                        Utility.showToast(this,Constants.SOMETHINGWENTWRONG)
                    }
                })
    }

    private fun showPostQueryDialog() {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.forum_add_query_dialog, null)
        dialog.setContentView(view)
        dialog.setTitle("Post your query here")

        initEditor(view)

        view.btn_post_query.setOnClickListener {
            if (htmlData.isNotEmpty()){
                if (courseId!=-1){
                    vm.askQuery(pm.getString(Constants.JWTTOKEN),courseId,htmlData)
                            ?.observe(this, Observer {
                                if (it!=null){
                                    Utility.showToast(this,it.message)
                                    htmlData = ""
                                    view.editor.html = ""
                                }else{
                                    Utility.showToast(this,Constants.SOMETHINGWENTWRONG)
                                }
                                dialog.cancel()
                            })
                }
            }else{
                Utility.showToast(this,"Please enter a valid question")
            }
        }

        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)

        dialog.show()

    }

    override fun onViewReplies(queryModel: QueryModel) {
        val intent = Intent(this@ForumActivity,RepliesActivity::class.java)
        intent.putExtra(Constants.QUERY,queryModel.query)
        intent.putExtra(Constants.QUERYID,queryModel.id)
        startActivity(intent)
    }

    override fun onDoReply(queryModel: QueryModel) {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.forum_add_query_dialog, null)
        dialog.setContentView(view)
        dialog.setTitle("Post your reply here")

        initEditor(view)

        view.btn_post_query.setOnClickListener {
            if (htmlData.isNotEmpty()){
                if (courseId!=-1){
                    vm.doReply(pm.getString(Constants.JWTTOKEN),queryModel.id,htmlData)
                            ?.observe(this, Observer {
                                if (it!=null){
                                    Utility.showToast(this,it.message)
                                    htmlData = ""
                                    view.editor.html = ""
                                }else{
                                    Utility.showToast(this,Constants.SOMETHINGWENTWRONG)
                                }
                                dialog.cancel()
                            })
                }
            }else{
                Utility.showToast(this,"Please enter a valid Reply")
            }
        }

        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)

        dialog.show()
    }

    private fun initEditor(view:View) {

        view.editor.setEditorHeight(200)
        view.editor.setEditorFontSize(22)
        view.editor.setEditorFontColor(Color.RED)
        view.editor.setPadding(10, 10, 10, 10)
        view.editor.setPlaceholder("Insert text here...")

        view.editor.setOnTextChangeListener { text ->
            htmlData = text
        }

        view.action_undo.setOnClickListener {
            view.editor.undo()
        }

        view.action_redo.setOnClickListener {
            view.editor.redo()
        }
        view.action_bold.setOnClickListener {
            view.editor.setBold()
        }
        view.action_italic.setOnClickListener {
            view.editor.setItalic()
        }
        view.action_subscript.setOnClickListener {
            view.editor.setSubscript()
        }
        view.action_superscript.setOnClickListener {
            view.editor.setSuperscript()
        }
        view.action_strikethrough.setOnClickListener {
            view.editor.setStrikeThrough()
        }
        view.action_underline.setOnClickListener {
            view.editor.setUnderline()
        }
        view.action_heading1.setOnClickListener {
            view.editor.setHeading(1)
        }
        view.action_heading2.setOnClickListener {
            view.editor.setHeading(2)
        }
        view.action_heading3.setOnClickListener {
            view.editor.setHeading(3)
        }
        view.action_heading4.setOnClickListener {
            view.editor.setHeading(4)
        }
        view.action_heading5.setOnClickListener {
            view.editor.setHeading(5)
        }
        view.action_heading6.setOnClickListener {
            view.editor.setHeading(6)
        }

        var isTextColorChanged = true

        view.action_txt_color.setOnClickListener {
            if (isTextColorChanged){
                view.editor.setTextColor(Color.RED)
            }else{
                view.editor.setTextColor(Color.BLACK)
            }
            isTextColorChanged=!isTextColorChanged
        }

        var isBgColorChanged = true

        view.action_bg_color.setOnClickListener {
            if (isBgColorChanged){
                view.editor.setTextBackgroundColor(Color.TRANSPARENT)
            }else{
                view.editor.setTextColor(Color.YELLOW)
            }
            isBgColorChanged=!isBgColorChanged
        }


        view.action_indent.setOnClickListener {
            view.editor.setIndent()
        }

        view.action_outdent.setOnClickListener {
            view.editor.setOutdent()
        }

        view.action_align_left.setOnClickListener {
            view.editor.setAlignLeft()
        }

        view.action_align_center.setOnClickListener {
            view.editor.setAlignCenter()
        }

        view.action_align_right.setOnClickListener {
            view.editor.setAlignRight()
        }
        view.action_blockquote.setOnClickListener {
            view.editor.setBlockquote()
        }
        view.action_insert_bullets.setOnClickListener {
            view.editor.setBullets()
        }
        view.action_insert_numbers.setOnClickListener {
            view.editor.setNumbers()
        }
        view.action_insert_image.setOnClickListener {
            view.editor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG","image")
        }
        view.action_insert_link.setOnClickListener {
            view.editor.insertLink("https://github.com/raystatic","github")
        }
        view.action_insert_checkbox.setOnClickListener {
            view.editor.insertTodo()
        }
    }
}
