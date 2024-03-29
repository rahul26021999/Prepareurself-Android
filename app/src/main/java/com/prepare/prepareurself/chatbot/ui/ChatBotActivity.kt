package com.prepare.prepareurself.chatbot.ui


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import com.prepare.prepareurself.R
import com.prepare.prepareurself.chatbot.BotModel
import com.prepare.prepareurself.chatbot.RequestV2Task
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.activity_chat_bot.*
import kotlinx.android.synthetic.main.bot_leaving_dialog.view.*
import kotlinx.android.synthetic.main.layout_topbar.*
import java.util.*
import kotlin.collections.ArrayList


class ChatBotActivity : BaseActivity(), ChatBotAdapter.BotListener {


    private lateinit var sessionsClient: SessionsClient
    private lateinit var  session:SessionName
    private lateinit var uuid:String
    private lateinit var chatBotModel:ArrayList<BotModel>
    private lateinit var adapter: ChatBotAdapter
    private var courseName = ""
    private var gradColor = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        val intent = intent
        if (intent==null){
            finish()
        }else{
            courseName = intent.getStringExtra(Constants.COURSENAME)
            gradColor = intent.getStringExtra("gradColor")
        }

        findViewById<TextView>(R.id.title).text = "$courseName Chat Bot"
        findViewById<TextView>(R.id.heading_bot).text="You are asking Questions in $courseName"

        chatBotModel = ArrayList()
        setColors(gradColor)
        initAdapter()
        uuid = UUID.randomUUID().toString()
        initChatBot()
     //   sendMessage("hi")

        btn_send_bot.setOnClickListener {
            val msg = et_bot_query.text.toString()
            if (msg.isNotEmpty()){
                sendMessage(msg)
            }else{
                Utility.showToast(this, "Cannot be empty")
            }
        }

        backBtn.setOnClickListener {
            closeDialog()
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
            rel_top_bar.background = myGradBg
            btn_send_bot.setBackgroundColor(colors[0])
        }
    }

    private fun initAdapter() {
        adapter = ChatBotAdapter(this, this)
        chat_bot_rv.layoutManager = LinearLayoutManager(this)
        chat_bot_rv.adapter = adapter
    }

    private fun initChatBot() {
       try {
           val stream = resources.openRawResource(R.raw.credential_file)
           val credentials = GoogleCredentials.fromStream(stream)
           val projectId = (credentials as ServiceAccountCredentials).projectId

           val settingsBuilder = SessionsSettings.newBuilder()
           val sessionSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                   .build()
           sessionsClient = SessionsClient.create(sessionSettings)
           session = SessionName.of(projectId, uuid)
       }catch (e:Exception){
           e.printStackTrace()
       }
    }

    private fun sendMessage(msg:String){
        val queryInput = QueryInput.newBuilder()
                .setText(TextInput.newBuilder()
                        .setText("$msg in $courseName")
                        .setLanguageCode("en-US"))
                .build()
        RequestV2Task(this@ChatBotActivity,session, sessionsClient, queryInput).execute()
        val botModel = BotModel(1,msg)
        val pos = adapter.addData(botModel)
        chat_bot_rv.smoothScrollToPosition(pos)
        et_bot_query.setText("")
    }

    fun callbackV2(response:DetectIntentResponse?){
        if (response!=null){
            val botReply = response.queryResult.fulfillmentText
            showReply(botReply)
        }else{
            Utility.showToast(this, "There was some communication issue. Please try again")
        }
    }

    private fun showReply(botReply: String?) {
        val botModel = BotModel(2,"$botReply")
        val pos = adapter.addData(botModel)
        chat_bot_rv.smoothScrollToPosition(pos)
    }

    override fun onLinkClicked(url: String) {
        //Utility.redirectUsingCustomTab(this,url)
        Utility.redirectUsingCustomTab(this@ChatBotActivity, url)
//        val uri = Uri.parse(url)
//
//        val intentBuilder = CustomTabsIntent.Builder()
//
//        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
//        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
//
//        val customTabsIntent = intentBuilder.build()
//
//        customTabsIntent.launchUrl(this, uri)
    }

    private fun closeDialog(){
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.bot_leaving_dialog, null)
        dialog.setContentView(view)

        view.bot_dialog_yes.setOnClickListener {
            finish()
        }

        view.bot_dialog_no.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        val window = dialog.window

        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

    }

    override fun onBackPressed() {
        closeDialog()
    }

}
