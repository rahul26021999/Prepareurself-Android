package com.prepare.prepareurself.trending.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.prepare.prepareurself.Apiservice.ApiClient
import com.prepare.prepareurself.Apiservice.ApiInterface
import com.prepare.prepareurself.courses.data.model.ProjectResponseModel
import com.prepare.prepareurself.courses.data.model.ProjectsModel
import com.prepare.prepareurself.courses.data.model.TopicsModel
import com.prepare.prepareurself.dashboard.data.model.GetSuggestedProjectsModel
import com.prepare.prepareurself.dashboard.data.model.GetSuggestedTopicsModel
import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrendingRepository {

    private var apiInterface:ApiInterface?=null

    init {
        apiInterface = ApiClient.getApiClient()?.create(ApiInterface::class.java)
    }


    fun fetSuggestedTopics(token: String?): LiveData<List<TopicsModel>>? {
        val data = MutableLiveData<List<TopicsModel>>()
        apiInterface!!.getSuggestedTopics(token).enqueue(object : Callback<TrendingTopicsResponseModel> {
            override fun onResponse(call: Call<TrendingTopicsResponseModel?>, response: Response<TrendingTopicsResponseModel?>) {
                val responseModel = response.body()
                if (responseModel != null && responseModel.success!!) {
                    data.value = responseModel.topics
                } else {
                    data.value = null
                }
            }

            override fun onFailure(call: Call<TrendingTopicsResponseModel?>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }

    fun fetchSuggestedProjects(token: String?): LiveData<List<ProjectsModel>> {
        val data = MutableLiveData<List<ProjectsModel>>()
        apiInterface!!.getSuggestedProjects(token).enqueue(object : Callback<TrendingProjectsReponseModel?> {
            override fun onResponse(call: Call<TrendingProjectsReponseModel?>, response: Response<TrendingProjectsReponseModel?>) {
                val responseModel = response.body()
                if (responseModel != null && responseModel.success!!) {
                    data.value = responseModel.projects
                } else {
                    data.value = null
                }
            }

            override fun onFailure(call: Call<TrendingProjectsReponseModel?>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }

}
