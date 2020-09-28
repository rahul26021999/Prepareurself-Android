package com.prepare.prepareurself.forum.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.prepare.prepareurself.R
import com.prepare.prepareurself.forum.data.QueryModel
import com.prepare.prepareurself.forum.viewmodel.ForumViewModel
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.PrefManager
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.activity_forum2.*
import kotlinx.android.synthetic.main.activity_forum_content.*
import kotlinx.android.synthetic.main.ask_query_bottom_sheet.view.*
import kotlinx.android.synthetic.main.fullimage_dialog_container.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


class ForumActivity : BaseActivity(), QueriesAdapter.QueriesListener, ImageAttachedAdapter.AttachmentListener {


    val REQUEST_IMAGE_GALLERY = 100
    val REQUEST_IMAGE_CAMERA = 99

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
    private lateinit var queriesAdapter:QueriesAdapter
    private var bottomsheetView:View?=null
    private var courseName = ""
    private var gradColor = ""

    private lateinit var bottomSheetDialog:BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forum2)


        vm = ViewModelProvider(this)[ForumViewModel::class.java]

        pm = PrefManager(this)


        courseId = intent.getIntExtra(Constants.COURSEID,-1)
        courseName =intent.getStringExtra(Constants.COURSENAME)
        gradColor = intent.getStringExtra("gradColor")

        setColors(gradColor)

        initBottomSheet()

        initQueryAdapter()

        findViewById<TextView>(R.id.title).text = "$courseName Forum"

        backBtn.setOnClickListener {
            finish()
        }

    }

    private fun setColors(gradColor: String) {
        if (gradColor != "") {
            val list = gradColor.split(",".toRegex()).toTypedArray()
            Log.i("Colors", gradColor + list.size)
            val colors = IntArray(list.size)
            for (i in list.indices) {
                colors[i] = Color.parseColor(list[i])
            }
            val myGradBg = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
            myGradBg.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
            rel_top.background = myGradBg
            btn_ask_query.setBackgroundColor(colors[0])
        }
    }

    private fun initBottomSheet() {

        bottomsheetView = LayoutInflater.from(this).inflate(R.layout.ask_query_bottom_sheet, null)
        bottomSheetDialog = BottomSheetDialog(this, R.style.DialogStyle)
        bottomSheetDialog.setContentView(bottomsheetView!!)

        btn_ask_query.setOnClickListener {
            bottomSheetDialog.show()
        }

        //initAttachmentAdapter()

        bottomSheetDialog.setOnShowListener {
            val d = bottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet as View)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }

        bottomsheetView!!.et_query_forum.hint ="Enter your query"

        bottomsheetView!!.btn_send_query.isEnabled = false

        bottomsheetView!!.et_query_forum.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bottomsheetView!!.btn_send_query.isEnabled = bottomsheetView!!.et_query_forum.text.toString().isNotEmpty()
            }
        })

        attachmentAdapter = ImageAttachedAdapter(this)
        bottomsheetView!!.rv_image_attachment.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        bottomsheetView!!.rv_image_attachment.adapter = attachmentAdapter

        bottomsheetView!!.tv_attach_image.setOnClickListener {
            uploadImage()
        }

        bottomsheetView!!.btn_send_query.setOnClickListener {
            var data = bottomsheetView!!.et_query_forum.text.toString()
            if (data.isNotEmpty()){
                if (courseId!=-1){
                    data = data.replace("\n","<br />", true)
                    htmlData = "<p>$data</p>"
                    vm.askQuery(pm.getString(Constants.JWTTOKEN),courseId,htmlData, imageNameList)
                            ?.observe(this, Observer {
                                if (it!=null){
                                    Utility.showToast(this,"Query submitted successfully!")
                                    htmlData = ""
                                    bottomsheetView!!.et_query_forum.setText("")
                                    it.query?.let { it1 ->
                                        queriesAdapter.addData(it1)
                                        rv_queries.smoothScrollToPosition(0)
                                        attachmentAdapter.clearData()
                                        imageAttachedList.clear()
                                        imageNameList.clear()
                                        bottomsheetView!!.et_query_forum.setText("")
                                        bottomSheetDialog.cancel()
                                    }
                                }else{
                                    Utility.showToast(this,Constants.SOMETHINGWENTWRONG)
                                }
                            })
                }
            }
        }


    }

    private fun initQueryAdapter() {
        queriesAdapter = QueriesAdapter(this, this)
        rv_queries.layoutManager = LinearLayoutManager(this)
        rv_queries.adapter = queriesAdapter

        currentPage = 1
        queriesList.clear()

        //forum_progress.visibility = View.VISIBLE

        vm.getQueries(pm.getString(Constants.JWTTOKEN),courseId,currentPage)
                ?.observe(this, Observer {
                    if (it!=null){
                        currentPage += 1
                        if (it.queries?.last_page!=null){
                            lastPage = it.queries?.last_page!!
                        }
                        it.queries?.data?.let { it1 ->
                            queriesList.addAll(it1)
                            queriesAdapter.setData(queriesList, gradColor)
                        }
                    }else{
                        Utility.showToast(this,Constants.SOMETHINGWENTWRONG)
                    }
                    //forum_progress.visibility = View.GONE
                })
    }

    override fun onViewReplies(queryModel: QueryModel) {
        val intent = Intent(this@ForumActivity,RepliesActivity::class.java)
        intent.putExtra(Constants.QUERY,queryModel)
        intent.putExtra(Constants.QUERYID,queryModel.id)
        intent.putExtra(Constants.COURSENAME, courseName)
        intent.putExtra("gradColor",gradColor)
        startActivity(intent)
    }


    private fun uploadImage() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions()
                            //showImageDialog()
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest?>?,
                            token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()

    }

    private fun showImageDialog() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery")

        val builder= AlertDialog.Builder(this)
        builder.setTitle("Choose your preference")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 102)
                }
                options[item] == "Choose from Gallery" -> {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "image/jpeg"
                    try {
                        startActivityForResult(intent, 101)
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        builder.show()
    }

    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, object : ImagePickerActivity.PickerOptionListener {
            override fun onTakeCameraSelected() {
                launchCameraIntent()
            }

            override fun onChooseGallerySelected() {
                launchGalleryIntent()
            }
        })
    }

    private fun launchCameraIntent() {
        val intent = Intent(this@ForumActivity, ImagePickerActivity::class.java)
        intent.putExtra(
                ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
                ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(intent, REQUEST_IMAGE_CAMERA)
    }

    private fun launchGalleryIntent() {
        val intent = Intent(this@ForumActivity, ImagePickerActivity::class.java)
        intent.putExtra(
                ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
                ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@ForumActivity)
        builder.setTitle("Grant Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("Go to Settings") { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
                "Cancel"
        ) { dialog, which -> dialog.cancel() }
        builder.show()

    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
          //  forum_progress.visibility = View.VISIBLE
            showDialogProgress()
            bottomsheetView?.tv_attach_image?.isEnabled = false
            val uri: Uri? = data?.getParcelableExtra("path")
            try {
                val `is`: InputStream? = uri?.let {
                    contentResolver.openInputStream(it)
                }
                if (`is` != null) uploadImageToServer(getBytes(`is`))
            } catch (e: IOException) {
                e.printStackTrace()
               // forum_progress.visibility = View.GONE
                hideDialogProgress()
            }
        }
//        else{
//            forum_progress.visibility = View.GONE
//            hideDialogProgress()
//        }

        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK) {
           // forum_progress.visibility = View.VISIBLE
            showDialogProgress()
            bottomsheetView?.tv_attach_image?.isEnabled = false
            val uri: Uri? = data?.getParcelableExtra("path")
            Glide.with(this).asBitmap().load(uri)
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            //imageView.setImageBitmap(resource)
                            val pathUri = getCapturedImageUri(resource)
                            try {
                                val `is`: InputStream? = pathUri?.let {
                                    contentResolver.openInputStream(it)
                                }
                                if (`is` != null) uploadImageToServer(getBytes(`is`))
                            } catch (e: IOException) {
                                e.printStackTrace()
                               // forum_progress.visibility = View.GONE
                                hideDialogProgress()
                            }
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {
                            // this is called when imageView is cleared on lifecycle call or for
                            // some other reason.
                            // if you are referencing the bitmap somewhere else too other than this imageView
                            // clear it here as you can no longer have the bitmap
                        }
                    })
        }
//        else{
//            forum_progress.visibility = View.GONE
//            hideDialogProgress()
//        }

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
              //  forum_progress.visibility = View.VISIBLE
                showDialogProgress()
                bottomsheetView?.tv_attach_image?.isEnabled = false
                val uri: Uri = data?.data ?: Uri.parse("")
                try {
                    val `is`: InputStream? = contentResolver.openInputStream(uri)
                    if (`is` != null) uploadImageToServer(getBytes(`is`))
                } catch (e: IOException) {
                    e.printStackTrace()
                   // forum_progress.visibility = View.GONE
                    hideDialogProgress()
                }
            }
        }

        if (requestCode == 102){
            if (resultCode == Activity.RESULT_OK) {
              //  forum_progress.visibility = View.VISIBLE
                showDialogProgress()
                bottomsheetView?.tv_attach_image?.isEnabled = false
                Log.d("camera_intent","$data ${data?.extras?.get("data")} abc ")
                val photo = data?.extras?.get("data") as Bitmap

                val uri: Uri = getCapturedImageUri(photo) ?: Uri.parse("")
                try {
                    val `is`: InputStream? = contentResolver.openInputStream(uri)
                    if (`is` != null) uploadImageToServer(getBytes(`is`))
                } catch (e: IOException) {
                    e.printStackTrace()
                  //  forum_progress.visibility = View.GONE
                    hideDialogProgress()
                }
            }
        }

    }

    private fun hideDialogProgress() {
        if (bottomSheetDialog.isShowing){
            bottomsheetView?.findViewById<ProgressBar>(R.id.dialog_progress)?.visibility = View.GONE
        }
    }

    private fun showDialogProgress() {
        if (bottomSheetDialog.isShowing){
            bottomsheetView!!.dialog_progress?.visibility = View.VISIBLE
        }
    }

    private fun getCapturedImageUri(photo: Bitmap): Uri? {
        val path: String = MediaStore.Images.Media.insertImage(this.contentResolver, photo, "${Date().time}", null)
        return Uri.parse(path)
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
                 //   forum_progress.visibility = View.GONE
                    hideDialogProgress()
                    bottomsheetView?.tv_attach_image?.isEnabled = true
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

    override fun onItemClicked(list: ArrayList<String>, position: Int) {
        onImageClicked(list,position)
    }

    override fun onBottomReached() {
        if (currentPage<=lastPage){
            vm.getQueries(pm.getString(Constants.JWTTOKEN),courseId, currentPage)
        }
    }

    override fun onImageClicked(attachment: List<String>, position: Int) {
        val dialog = Dialog(this,android.R.style.Theme_Light)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(this).inflate(R.layout.fullimage_dialog_container, null)
        dialog.setContentView(view)
        dialog.setCancelable(false)

        val pagerAdapter = ForumImageViewPagerAdapter(attachment, this,1)
        view.fullscreen_pager.adapter = pagerAdapter
        view.fullscreen_pager.offscreenPageLimit  = attachment.size

        view.fullscreen_pager.currentItem = position

        view.fullscreen_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
//                if (position == 0){
//                    view.img_left_swipe.visibility = View.GONE
//                }
//
//                if (position+1 == attachment.size){
//                    view.img_right_swipe.visibility = View.GONE
//                }
//
//                if (position>0 && position< attachment.size){
//                    view.img_left_swipe.visibility = View.VISIBLE
//                    view.img_right_swipe.visibility = View.VISIBLE
//                }

            }
        })

        view.img_right_swipe.setOnClickListener {
            view.fullscreen_pager.currentItem = view.fullscreen_pager.currentItem+1
        }

        view.img_left_swipe.setOnClickListener {
            view.fullscreen_pager.currentItem = view.fullscreen_pager.currentItem-1
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
