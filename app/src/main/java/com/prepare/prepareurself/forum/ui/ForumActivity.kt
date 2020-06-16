package com.prepare.prepareurself.forum.ui

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
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
import kotlinx.android.synthetic.main.activity_forum2.*
import kotlinx.android.synthetic.main.activity_forum2.view.*
import kotlinx.android.synthetic.main.forum_add_query_dialog.*
import kotlinx.android.synthetic.main.forum_add_query_dialog.view.*
import kotlinx.android.synthetic.main.insert_link_editor_dialog.view.*
import kotlinx.android.synthetic.main.layout_topbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


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

//        btn_ask_query.setOnClickListener {
//            showPostQueryDialog()
//        }

        initEditor()

        btn_send_query.setOnClickListener {
            var data = et_query_forum.text.toString()
            if (data.isNotEmpty()){
                if (courseId!=-1){
                    data = data.replace("\n","<br />", true)
                    htmlData = "<p>$data</p>"
                    vm.askQuery(pm.getString(Constants.JWTTOKEN),courseId,htmlData)
                            ?.observe(this, Observer {
                                if (it!=null){
                                    Utility.showToast(this,it.message)
                                    htmlData = ""
                                    editor.html = ""
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
                        it.queries?.data?.let { it1 ->
                            adapter.setData(it1)
                        }
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

        initEditor()

        view.btn_send_query.setOnClickListener {
            if (htmlData.isNotEmpty()){
                if (courseId!=-1){
                    vm.doReply(pm.getString(Constants.JWTTOKEN),queryModel.id,htmlData)
                            ?.observe(this, Observer {
                                if (it!=null){
                                    Utility.showToast(this,it.message)
                                    htmlData = ""
                                    editor.html = ""
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

    private fun uploadImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/jpeg"
        try {
            startActivityForResult(intent, 101)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                val uri: Uri = data?.data ?: Uri.parse("")
                try {
                    val `is`: InputStream? = contentResolver.openInputStream(uri)
                    if (`is` != null) uploadImageToServer(getBytes(`is`))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun uploadImageToServer(bytes: ByteArray?) {
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("image/jpeg"), bytes)
        val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)

        vm.uploadQueryImage(pm.getString(Constants.JWTTOKEN),1,body)
                ?.observe(this, Observer {
                    if (it!=null){
                        Log.d("upload_query_image","${it.message} ${it.image}")
                        if (!it.image.isNullOrEmpty()){
                            editor.insertImage(it.image,"image.jpg")
                        }else{
                            Utility.showToast(this,"Cannot insert image at the moment")
                        }
                    }else{
                        Utility.showToast(this,"Cannot insert image at the moment")
                    }
                })

    }

    @Throws(IOException::class)
    private fun getBytes(`is`: InputStream): ByteArray? {
        val byteBuff = ByteArrayOutputStream()
        val buffSize = 1024
        val buff = ByteArray(buffSize)
        var len = 0
        while (`is`.read(buff).also { len = it } != -1) {
            byteBuff.write(buff, 0, len)
        }
        return byteBuff.toByteArray()
    }

    private fun initEditor() {

        editor.setEditorHeight(100)
        editor.setEditorFontSize(22)
        editor.setEditorFontColor(R.color.colorPrimaryDark)
        editor.setPadding(10, 10, 10, 10)
        editor.setPlaceholder("Insert text here...")
        editor.requestFocus()

        editor.setOnTextChangeListener { text ->
            htmlData = text
        }

        tv_bold.setOnClickListener {
            editor.setBold()
        }
        tv_italic.setOnClickListener {
            editor.setItalic()
        }

        tv_attach_image.setOnClickListener {
            //editor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG","image")
            uploadImage()
            editor.setNumbers()
        }

//        action_undo.setOnClickListener {
//            editor.undo()
//        }
//
//        action_redo.setOnClickListener {
//            editor.redo()
//        }
//
//        action_subscript.setOnClickListener {
//            editor.setSubscript()
//        }
//        action_superscript.setOnClickListener {
//            editor.setSuperscript()
//        }
//        action_strikethrough.setOnClickListener {
//            editor.setStrikeThrough()
//        }
//        tv_underline.setOnClickListener {
//            editor.setUnderline()
//        }
//        action_heading1.setOnClickListener {
//            editor.setHeading(1)
//        }
//        action_heading2.setOnClickListener {
//            editor.setHeading(2)
//        }
//        action_heading3.setOnClickListener {
//            editor.setHeading(3)
//        }
//        action_heading4.setOnClickListener {
//            editor.setHeading(4)
//        }
//        action_heading5.setOnClickListener {
//            editor.setHeading(5)
//        }
//        action_heading6.setOnClickListener {
//            editor.setHeading(6)
//        }
//
//        var isTextColorChanged = true
//
//        action_txt_color.setOnClickListener {
//            if (isTextColorChanged){
//                editor.setTextColor(Color.RED)
//            }else{
//                editor.setTextColor(Color.BLACK)
//            }
//            isTextColorChanged=!isTextColorChanged
//        }
//
//        var isBgColorChanged = true
//
//        action_bg_color.setOnClickListener {
//            if (isBgColorChanged){
//                editor.setTextBackgroundColor(Color.TRANSPARENT)
//            }else{
//                editor.setTextColor(Color.YELLOW)
//            }
//            isBgColorChanged=!isBgColorChanged
//        }
//
//
//        action_indent.setOnClickListener {
//            editor.setIndent()
//        }
//
//        action_outdent.setOnClickListener {
//            editor.setOutdent()
//        }
//
//        action_align_left.setOnClickListener {
//            editor.setAlignLeft()
//        }
//
//        action_align_center.setOnClickListener {
//            editor.setAlignCenter()
//        }
//
//        action_align_right.setOnClickListener {
//            editor.setAlignRight()
//        }
//        action_blockquote.setOnClickListener {
//            editor.setBlockquote()
//        }
//        action_insert_bullets.setOnClickListener {
//            editor.setBullets()
//        }
//        action_insert_numbers.setOnClickListener {
//            editor.setNumbers()
//        }
//
//        action_insert_link.setOnClickListener {
//           // editor.insertLink("https://github.com/raystatic","github")
//            insertLink()
//        }
//        action_insert_checkbox.setOnClickListener {
//            editor.insertTodo()
//        }
    }

    private fun insertLink() {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.insert_link_editor_dialog, null)
        dialog.setContentView(view)

        view.btn_insert_link.setOnClickListener{
            val name = view.et_editor_name.text.toString()
            val link = view.et_editor_link.text.toString()

            if (name.isNotEmpty() && link.isNotEmpty()){
                editor.insertLink(link,name)
                dialog.cancel()
            }else{
                Utility.showToast(this,"Please enter data carefully")
            }

        }

        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        dialog.show()

    }
}
