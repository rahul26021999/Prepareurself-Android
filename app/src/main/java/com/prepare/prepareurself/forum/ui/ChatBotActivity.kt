package com.prepare.prepareurself.forum.ui

import ai.api.AIListener
import ai.api.android.AIConfiguration
import ai.api.android.AIService
import ai.api.model.AIError
import ai.api.model.AIRequest
import ai.api.model.AIResponse
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.activity_chat_bot.*
import kotlin.concurrent.thread
import com.prepare.prepareurself.R


class ChatBotActivity : BaseActivity(), AIListener {

    private lateinit var config: AIConfiguration
    private lateinit var aiService:AIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)

        if (permission != PackageManager.PERMISSION_GRANTED) {

            makeRequest()
        }

        config = AIConfiguration(Constants.DIALOGFLOWCLIENTACCESSTOKEN,
                ai.api.AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System)

        aiService = AIService.getService(this,config)
        aiService.setListener(this)

        btn_send_bot.setOnClickListener {
            //aiService.startListening()
//            val query = et_bot_query.text.toString()
//            if (query.isNotEmpty()){
//                thread {
//                    aiService.textRequest(AIRequest(query))
//                   val response = aiService.textRequest(AIRequest(query))
//                    Log.d("ai_success","$response res")
//                }
//
//            }
            aiService.startListening()
        }

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                101)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                if (grantResults.isEmpty()
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    Utility.showToast(this,"Permission not granted")
                }else{
                    Utility.showToast(this,"Permission granted")
                }
                return
            }
        }
    }

    override fun onResult(result: AIResponse?) {
        val r = result?.result
        Log.d("ai_success","$r abc && ${r?.action}")
        inputTextView.text = r?.resolvedQuery
        fulfillmentTextView.text = r?.fulfillment?.speech
        actionTextView.text = r?.metadata?.intentName
    }

    override fun onListeningStarted() {

    }

    override fun onAudioLevel(level: Float) {

    }

    override fun onError(error: AIError?) {
        Log.d("ai_error","$error abc")
    }

    override fun onListeningCanceled() {

    }

    override fun onListeningFinished() {

    }
}
