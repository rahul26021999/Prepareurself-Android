package com.prepare.prepareurself.forum.ui

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.data.OpenForumAttachment
import com.prepare.prepareurself.forum.viewmodel.ForumViewModel
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.PrefManager
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.activity_replies.*
import kotlinx.android.synthetic.main.fullscreen_image_dialog.view.*
import kotlinx.android.synthetic.main.layout_topbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class RepliesActivity : BaseActivity(), RepliesAdapter.RepliesListener, ImageAttachedAdapter.AttachmentListener {

    private lateinit var vm:ForumViewModel
    private lateinit var pm:PrefManager
    private var queryId = -1
    private var query = ""
    private var htmlData = ""
    private var imageAttachedList = ArrayList<String>()
    private var imageNameList = ArrayList<String>()
    private lateinit var attachmentAdapter: ImageAttachedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_replies)
        vm = ViewModelProvider(this)[ForumViewModel::class.java]
        pm = PrefManager(this)

        queryId = intent.getIntExtra(Constants.QUERYID,-1)
        query = intent.getStringExtra(Constants.QUERY)

        if(queryId!=-1){

            if (query.isNotEmpty()){
                tv_reply_question.text = Html.fromHtml(query).trim()
            }

            initAdapter()

            initAttachmentAdapter()

            tv_attach_image_reply.setOnClickListener {
                uploadImage()
            }
        }

        btn_send_reply.setOnClickListener {
            var data = et_reply_forum.text.toString()
            if (data.isNotEmpty()){
                if (queryId!=-1){
                    data = data.replace("\n","<br />", true)
                    htmlData = "<p>$data</p>"
                    vm.doReply(pm.getString(Constants.JWTTOKEN),queryId,htmlData, imageNameList)
                            ?.observe(this, Observer {
                                if (it!=null){
                                    Utility.showToast(this,it.message)
                                    htmlData = ""
                                }else{
                                    Utility.showToast(this,Constants.SOMETHINGWENTWRONG)
                                }
                            })
                }
            }else{
                Utility.showToast(this,"Please enter a valid question")
            }
        }

        backBtn.setOnClickListener {
            finish()
        }

    }

    private fun initAttachmentAdapter() {
        attachmentAdapter = ImageAttachedAdapter(this)
        rv_image_attachment_reply.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        rv_image_attachment_reply.adapter = attachmentAdapter
    }

    override fun onCancelled(position: Int) {
        imageAttachedList.removeAt(position)
        imageNameList.removeAt(position)
        attachmentAdapter.notifyItemRemoved(position)
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
                        Log.d("upload_query_image","${it.message} ${it.image} ${it.path}")
                        if (!it.image.isNullOrEmpty()){
                            imageAttachedList.add(it.image.toString())
                            imageNameList.add("${it.image}")
                            attachmentAdapter.setData(imageNameList)
                        }else{
                            Utility.showToast(this,"Cannot attach image at the moment")
                        }
                    }else{
                        Utility.showToast(this,"Cannot attach image at the moment")
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

    private fun initAdapter() {
        val adapter = RepliesAdapter(this, this)
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

    override fun onImageClicked(attachment: OpenForumAttachment) {
        val dialog = Dialog(this,android.R.style.Theme_Light)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(this).inflate(R.layout.fullscreen_image_dialog, null)
        dialog.setContentView(view)
        dialog.setCancelable(false)

        view.tv_name_fullscreen_image.text = attachment.file

        if (attachment.file!=null && attachment.file?.isNotEmpty()!!){
            val imagUrl = "${Constants.REPLYATTACHMENTBASEURL}${attachment.file}"
            if (imagUrl.endsWith(".svg")){
                Utility.loadSVGImage(this, imagUrl, view.fullscreen_image_forum)
            }else{
                Glide.with(this)
                        .load(imagUrl)
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(view.fullscreen_image_forum)
            }
        }

        dialog.setOnKeyListener { d, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK){
                d.cancel()
            }
            return@setOnKeyListener true
        }

        dialog.show()
        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    }
}
