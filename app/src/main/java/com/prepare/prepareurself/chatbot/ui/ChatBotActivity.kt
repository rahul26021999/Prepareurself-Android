package com.prepare.prepareurself.chatbot.ui


import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.R
import com.prepare.prepareurself.chatbot.BotModel
import com.prepare.prepareurself.chatbot.RequestV2Task
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.activity_chat_bot.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        val intent = intent
        if (intent==null){
            finish()
        }else{
            courseName = intent.getStringExtra(Constants.COURSENAME)
        }

        findViewById<TextView>(R.id.title).text = "$courseName Chat Bot"
        findViewById<TextView>(R.id.heading_bot).text="You are asking Questions in $courseName"

        chatBotModel = ArrayList()
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
            finish()
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
        Utility.redirectUsingCustomTab(this,url)
    }
}
