package com.prepare.prepareurself.quizv2.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prepare.prepareurself.Apiservice.ApiClient
import com.prepare.prepareurself.Apiservice.ApiInterface
import com.prepare.prepareurself.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizRepository {

    var apiInterface : ApiInterface?=null

    init {
        apiInterface = ApiClient.getApiClient()?.create(ApiInterface::class.java)
    }

    fun fetchQuiz(token:String, courseId:Int, level:String) : LiveData<QuizResponseModel>{

        val data = MutableLiveData<QuizResponseModel>()

        apiInterface?.fetchQuiz(token,courseId,level)?.enqueue(object : Callback<QuizResponseModel>{
            override fun onFailure(call: Call<QuizResponseModel>, t: Throwable) {
                Log.d("quiz_debug","${t.localizedMessage} abc")
                data.value = null
            }

            override fun onResponse(call: Call<QuizResponseModel>, response: Response<QuizResponseModel>) {
                val res = response.body()

                if (res!=null && res.error_code == 0){
                    data.value = res
                }else{
                    data.value = null
                }

            }
        })

        return data
    }

    fun submitQuiz(token: String,quizId:Int,responses:List<ResponsesModel>):LiveData<QuizAnswerResponse>{

        val data = MutableLiveData<QuizAnswerResponse>()

        apiInterface?.submitQuiz(token, quizId, responses)?.enqueue(object : Callback<QuizAnswerResponse>{
            override fun onFailure(call: Call<QuizAnswerResponse>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<QuizAnswerResponse>, response: Response<QuizAnswerResponse>) {
                val res = response.body()
                if(res!=null){
                    data.value = res
                }else{
                    data.value = null
                }
            }
        })

        return data
    }


    fun submitIndividualQuiz(token: String,quizId:Int,courseId:Int,questionId:Int,optionId:Int){
        apiInterface?.submitIndividualQuestion(token, quizId,questionId, optionId)?.enqueue(object : Callback<QuizAnswerResponse>{
            override fun onFailure(call: Call<QuizAnswerResponse>, t: Throwable) {
                Log.d("save_individual_quiz","error ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<QuizAnswerResponse>, response: Response<QuizAnswerResponse>) {
                Log.d("save_individual_quiz","success ${response.body()}")
            }
        })
    }
}