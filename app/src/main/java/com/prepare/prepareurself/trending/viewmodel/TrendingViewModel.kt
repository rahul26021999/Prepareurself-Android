package com.prepare.prepareurself.trending.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.prepare.prepareurself.courses.data.model.ProjectsModel
import com.prepare.prepareurself.courses.data.model.TopicsModel
import com.prepare.prepareurself.resources.data.db.repository.ResourcesDbRepository
import com.prepare.prepareurself.resources.data.model.ResourceModel
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse
import com.prepare.prepareurself.resources.data.repository.ResourceRespository
import com.prepare.prepareurself.trending.data.TrendingRepository

class TrendingViewModel(application: Application) : AndroidViewModel(application) {

    private var trendingRepository : TrendingRepository?=null
    private var resourceRespository: ResourceRespository? = null
    private var resourcesDbRepository: ResourcesDbRepository? = null

    init {
        trendingRepository = TrendingRepository()
        resourceRespository = ResourceRespository(application)
        resourcesDbRepository = ResourcesDbRepository(application)
    }

    fun fetchTrendingProjects(token:String): LiveData<List<ProjectsModel>>? {
        return trendingRepository?.fetchSuggestedProjects(token)
    }

    fun fetchTrendingTopics(token:String): LiveData<List<TopicsModel>>? {
        return trendingRepository?.fetSuggestedTopics(token)
    }

    fun resourceViewed(token: String?, resourceId: Int): LiveData<ResourceViewsResponse>? {
        return resourceRespository!!.resourceViewed(token, resourceId)
    }

    fun saveResource(videoResources: ResourceModel?) {
        resourcesDbRepository!!.insertResource(videoResources)
    }
}
