package com.prepare.prepareurself.forum.ui

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.data.OpenForumAttachment
import com.prepare.prepareurself.forum.data.QueryModel
import com.prepare.prepareurself.forum.viewmodel.ForumViewModel
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.PrefManager
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.activity_forum2.*
import kotlinx.android.synthetic.main.activity_forum2.view.*
import kotlinx.android.synthetic.main.activity_forum_content.*
import kotlinx.android.synthetic.main.ask_query_bottom_sheet.*
import kotlinx.android.synthetic.main.ask_query_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fullscreen_image_dialog.view.*
import kotlinx.android.synthetic.main.insert_link_editor_dialog.view.*
import kotlinx.android.synthetic.main.layout_topbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.collections.ArrayList


class ForumActivity : BaseActivity(), QueriesAdapter.QueriesListener, ImageAttachedAdapter.AttachmentListener {

    private var htmlData = ""
    private lateinit var vm:ForumViewModel
    private lateinit var pm:PrefManager
    private var courseId = -1
    private var imageAttachedList = ArrayList<String>()
    private var imageNameList = ArrayList<String>()
    private lateinit var attachmentAdapter: ImageAttachedAdapter
    private lateinit var sheetBehaviour:BottomSheetBehavior<View>
    private var currentPage = 1
    private var lastPage = 1
    private var queriesList = ArrayList<QueryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum2)

        vm = ViewModelProvider(this)[ForumViewModel::class.java]

        pm = PrefManager(this)


        courseId = intent.getIntExtra(Constants.COURSEID,-1)

        initBottomSheet()

        initQueryAdapter()

        backBtn.setOnClickListener {
            finish()
        }

    }

    private fun initBottomSheet() {
//        sheetBehaviour = BottomSheetBehavior.from(bottom_sheet_ask_query)

//        btn_ask_query.setOnClickListener {
//            if (sheetBehaviour.state != BottomSheetBehavior.STATE_EXPANDED){
//                sheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
//            }else if (sheetBehaviour.state != BottomSheetBehavior.STATE_COLLAPSED){
//                sheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
//            }
//        }

        val view = LayoutInflater.from(this).inflate(R.layout.ask_query_bottom_sheet, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)

        btn_ask_query.setOnClickListener {
            dialog.show()
        }

        //initAttachmentAdapter()

        attachmentAdapter = ImageAttachedAdapter(this)
        view.rv_image_attachment.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        view.rv_image_attachment.adapter = attachmentAdapter

        view.tv_attach_image.setOnClickListener {
            uploadImage()
        }

        view.btn_send_query.setOnClickListener {
            var data = view.et_query_forum.text.toString()
            if (data.isNotEmpty()){
                if (courseId!=-1){
                    data = data.replace("\n","<br />", true)
                    htmlData = "<p>$data</p>"
                    vm.askQuery(pm.getString(Constants.JWTTOKEN),courseId,htmlData, imageNameList)
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


//        sheetBehaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//
//            }
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when(newState){
//                    BottomSheetBehavior.STATE_EXPANDED -> btn_ask_query.text = "Close Dialog"
//                    BottomSheetBehavior.STATE_COLLAPSED -> btn_ask_query.text = "Ask Query"
//                }
//            }
//        })

    }

    private fun initAttachmentAdapter() {
        attachmentAdapter = ImageAttachedAdapter(this)
        rv_image_attachment.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        rv_image_attachment.adapter = attachmentAdapter
    }

    private fun initQueryAdapter() {
        val adapter = QueriesAdapter(this, this)
        rv_queries.layoutManager = LinearLayoutManager(this)
        rv_queries.adapter = adapter

        currentPage = 1
        queriesList.clear()

        vm.getQueries(pm.getString(Constants.JWTTOKEN),courseId,currentPage)
                ?.observe(this, Observer {
                    if (it!=null){
                        currentPage += 1
                        if (it.queries?.last_page!=null){
                            lastPage = it.queries?.last_page!!
                        }
                        it.queries?.data?.let { it1 ->
                            queriesList.addAll(it1)
                            adapter.setData(queriesList)
                        }
                    }else{
                        Utility.showToast(this,Constants.SOMETHINGWENTWRONG)
                    }
                })
    }

    override fun onViewReplies(queryModel: QueryModel) {
        val intent = Intent(this@ForumActivity,RepliesActivity::class.java)
        intent.putExtra(Constants.QUERY,queryModel.query)
        intent.putExtra(Constants.QUERYID,queryModel.id)
        startActivity(intent)
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

        vm.uploadQueryImage(pm.getString(Constants.JWTTOKEN),0,body)
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

    override fun onCancelled(position: Int) {
        imageAttachedList.removeAt(position)
        imageNameList.removeAt(position)
        attachmentAdapter.notifyItemRemoved(position)
    }

    override fun onBottomReached() {
        if (currentPage<=lastPage){
            vm.getQueries(pm.getString(Constants.JWTTOKEN),courseId, currentPage)
        }
    }

    override fun onImageClicked(attachment: OpenForumAttachment) {
        val dialog = Dialog(this,android.R.style.Theme_Light)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(this).inflate(R.layout.fullscreen_image_dialog, null)
        dialog.setContentView(view)
        dialog.setCancelable(false)

        view.tv_name_fullscreen_image.text = attachment.file

        if (attachment.file!=null && attachment.file?.isNotEmpty()!!){
            val imagUrl = "${Constants.QUERYATTACHMENTBASEURL}${attachment.file}"
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
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT)

    }
}
