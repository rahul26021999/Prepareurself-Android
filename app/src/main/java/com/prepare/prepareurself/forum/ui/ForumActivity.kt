package com.prepare.prepareurself.forum.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.viewmodel.ForumViewModel
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.PrefManager
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.activity_forum2.*
import kotlinx.android.synthetic.main.richeditor_layout.*


class ForumActivity : BaseActivity() {

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

        initEditor()

        btn_post_query.setOnClickListener {
            if (htmlData.isNotEmpty()){
                if (courseId!=-1){
                    vm.askQuery(pm.getString(Constants.JWTTOKEN),courseId,htmlData)
                            ?.observe(this, Observer {
                                if (it!=null){
                                    Utility.showToast(this,it.message)
                                    htmlData = ""
                                    editor.html = ""
                                }else{
                                    Utility.showToast(this,Constants.SOMETHINGWENTWRONG)
                                }
                            })
                }
            }else{
                Utility.showToast(this,"Please enter a valid question")
            }
        }



    }

    private fun initEditor() {

        editor.setEditorHeight(200)
        editor.setEditorFontSize(22)
        editor.setEditorFontColor(Color.RED)
        editor.setPadding(10, 10, 10, 10)
        editor.setPlaceholder("Insert text here...")

        editor.setOnTextChangeListener { text ->
            htmlData = text
        }

        action_undo.setOnClickListener {
            editor.undo()
        }

        action_redo.setOnClickListener {
            editor.redo()
        }
        action_bold.setOnClickListener {
            editor.setBold()
        }
        action_italic.setOnClickListener {
            editor.setItalic()
        }
        action_subscript.setOnClickListener {
            editor.setSubscript()
        }
        action_superscript.setOnClickListener {
            editor.setSuperscript()
        }
        action_strikethrough.setOnClickListener {
            editor.setStrikeThrough()
        }
        action_underline.setOnClickListener {
            editor.setUnderline()
        }
        action_heading1.setOnClickListener {
            editor.setHeading(1)
        }
        action_heading2.setOnClickListener {
            editor.setHeading(2)
        }
        action_heading3.setOnClickListener {
            editor.setHeading(3)
        }
        action_heading4.setOnClickListener {
            editor.setHeading(4)
        }
        action_heading5.setOnClickListener {
            editor.setHeading(5)
        }
        action_heading6.setOnClickListener {
            editor.setHeading(6)
        }

        var isTextColorChanged = true

        action_txt_color.setOnClickListener {
            if (isTextColorChanged){
                editor.setTextColor(Color.RED)
            }else{
                editor.setTextColor(Color.BLACK)
            }
            isTextColorChanged=!isTextColorChanged
        }

        var isBgColorChanged = true

        action_bg_color.setOnClickListener {
            if (isBgColorChanged){
                editor.setTextBackgroundColor(Color.TRANSPARENT)
            }else{
                editor.setTextColor(Color.YELLOW)
            }
            isBgColorChanged=!isBgColorChanged
        }


        action_indent.setOnClickListener {
            editor.setIndent()
        }

        action_outdent.setOnClickListener {
            editor.setOutdent()
        }

        action_align_left.setOnClickListener {
            editor.setAlignLeft()
        }

        action_align_center.setOnClickListener {
            editor.setAlignCenter()
        }

        action_align_right.setOnClickListener {
            editor.setAlignRight()
        }
        action_blockquote.setOnClickListener {
            editor.setBlockquote()
        }
        action_insert_bullets.setOnClickListener {
            editor.setBullets()
        }
        action_insert_numbers.setOnClickListener {
            editor.setNumbers()
        }
        action_insert_image.setOnClickListener {
            editor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG","image")
        }
        action_insert_link.setOnClickListener {
            editor.insertLink("https://github.com/raystatic","github")
        }
        action_insert_checkbox.setOnClickListener {
            editor.insertTodo()
        }
    }
}
