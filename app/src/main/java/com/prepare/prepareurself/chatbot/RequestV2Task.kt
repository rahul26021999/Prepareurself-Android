package com.prepare.prepareurself.chatbot

import android.app.Activity
import android.os.AsyncTask
import com.google.cloud.dialogflow.v2.*
import com.prepare.prepareurself.chatbot.ui.ChatBotActivity
import java.lang.Exception

class RequestV2Task(var activity:Activity,
                    var session:SessionName,
                    var sessionsClient: SessionsClient,
                    var queryInput: QueryInput): AsyncTask<Void, Void, DetectIntentResponse>() {


    override fun doInBackground(vararg params: Void?): DetectIntentResponse {
        val detectIntentRequest = DetectIntentRequest.newBuilder()
                .setSession(session.toString())
                .setQueryInput(queryInput)
                .build()
        return sessionsClient.detectIntent(detectIntentRequest)
    }

    override fun onPostExecute(result: DetectIntentResponse?) {
        (activity as ChatBotActivity).callbackV2(result)
    }
}