package com.prepare.prepareurself.chatbot.ui


import android.os.Bundle
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.R
import com.prepare.prepareurself.chatbot.RequestV2Task
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.activity_chat_bot.*
import java.util.*


class ChatBotActivity : BaseActivity() {


    private lateinit var sessionsClient: SessionsClient
    private lateinit var  session:SessionName
    private lateinit var uuid:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        uuid = UUID.randomUUID().toString()
        initChatBot()

        btn_send_bot.setOnClickListener {
            val msg = et_bot_query.text.toString()
            if (msg.isNotEmpty()){
                sendMessage(msg)
            }else{
                Utility.showToast(this, "Cannot be empty")
            }
        }


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
                        .setText(msg)
                        .setLanguageCode("en-US"))
                .build()
        RequestV2Task(this@ChatBotActivity,session, sessionsClient, queryInput).execute()
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
        fulfillmentTextView.text = botReply
    }

}
