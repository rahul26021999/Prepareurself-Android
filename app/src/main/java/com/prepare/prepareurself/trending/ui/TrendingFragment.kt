package com.prepare.prepareurself.trending.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.prepare.prepareurself.R
import com.prepare.prepareurself.courses.data.model.ProjectsModel
import com.prepare.prepareurself.courses.data.model.TopicsModel
import com.prepare.prepareurself.courses.ui.activity.AllCoursesActivity
import com.prepare.prepareurself.courses.ui.activity.CourseDetailActivity
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity
import com.prepare.prepareurself.courses.ui.fragmentToActivity.TabProjectctivity
import com.prepare.prepareurself.courses.ui.fragmentToActivity.TabResourceActivity
import com.prepare.prepareurself.dashboard.data.model.CourseModel
import com.prepare.prepareurself.dashboard.data.model.DashboardRecyclerviewModel
import com.prepare.prepareurself.dashboard.ui.adapters.DashboardRvAdapter
import com.prepare.prepareurself.favourites.ui.FavouritesFragment.FavouritesHomeInteractor
import com.prepare.prepareurself.resources.data.model.ResourceModel
import com.prepare.prepareurself.resources.ui.activity.ResourcesActivity
import com.prepare.prepareurself.trending.viewmodel.TrendingViewModel
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.PrefManager
import com.prepare.prepareurself.utils.Utility
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui.VideoActivity
import kotlinx.android.synthetic.main.layout_topbar.*
import kotlinx.android.synthetic.main.trending_fragment.*

class TrendingFragment : Fragment(), DashboardRvAdapter.DashBoardInteractor {

    companion object {
       fun newInstance() = TrendingFragment()
    }

    private lateinit var viewModel: TrendingViewModel
    private lateinit var pm:PrefManager
    private var listener: TrendingHomeInteractor? = null

    interface TrendingHomeInteractor {
        fun onTrendingBackPressed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as TrendingHomeInteractor
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement TrendingHomeInteractor")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.trending_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TrendingViewModel::class.java)

        pm = PrefManager(requireActivity())

        title.text = "Trendings"

        backBtn.setOnClickListener(View.OnClickListener {
            //finish();
            listener?.onTrendingBackPressed()
        })

        val list = ArrayList<DashboardRecyclerviewModel>()

        val adapter = DashboardRvAdapter(requireActivity(), this)
        rv_trending_parent.layoutManager = LinearLayoutManager(requireActivity())
        rv_trending_parent.adapter = adapter

        viewModel.fetchTrendingProjects(pm.getString(Constants.JWTTOKEN))
                ?.observe(requireActivity(), Observer {
                    if (it!=null){
                        val d1 = DashboardRecyclerviewModel(it,Constants.PROJECTVIEWTYPE,"Projects", false,false,false, false)
                        list.add(d1)
                    }

                    viewModel.fetchTrendingTopics(pm.getString(Constants.JWTTOKEN))
                            ?.observe(requireActivity(), Observer { it1 ->
                                val d2 = DashboardRecyclerviewModel(Constants.TOPICVIEWTYPE,it1,"Topics",false)
                                list.add(d2)

                                adapter.setData(list)
                                adapter.notifyDataSetChanged()

                            })



                })

    }

    override fun onCourseClicked(courseModel: CourseModel?) {
        val intent = Intent( requireActivity() , CourseDetailActivity::class.java)
        intent.putExtra(Constants.COURSEID, courseModel!!.id)
        //intent.putExtra(Constants.COURSENAME,courseModel.getName());
        //intent.putExtra(Constants.COURSENAME,courseModel.getName());
        startActivity(intent)
    }

    override fun onTopicClicked(topicsModel: TopicsModel?) {
        val intent = Intent(requireActivity(), ResourcesActivity::class.java)
        intent.putExtra(Constants.TOPICID, topicsModel!!.id)
        intent.putExtra(Constants.COURSEID, topicsModel.course_id)
        startActivity(intent)
    }

    override fun onProjectClicked(projectsModel: ProjectsModel?) {
        val intent = Intent(requireActivity(), ProjectsActivity::class.java)
        intent.putExtra(Constants.PROJECTID, projectsModel!!.id)
        startActivity(intent)
    }

    override fun onTopicSeeAll(courseId: Int, name: String?) {
        val intent = Intent(activity, TabResourceActivity::class.java)
        intent.putExtra(Constants.COURSEID, courseId)
        startActivity(intent)
    }

    override fun onProjectSeeAll(courseId: Int, name: String?) {
        val intent = Intent(activity, TabProjectctivity::class.java)
        intent.putExtra(Constants.COURSEID, courseId)
        startActivity(intent)
    }

    override fun onCourseSeeAll() {
        startActivity(Intent(activity, AllCoursesActivity::class.java))
    }

    override fun onResourceClicked(resourceModel: ResourceModel?) {
        if (resourceModel!!.type.equals("video", ignoreCase = true)) {
            if (resourceModel.link.contains("youtu.be") || resourceModel.link.contains("youtube")) {
                val intent = Intent(requireActivity(), VideoActivity::class.java)
                intent.putExtra(Constants.VIDEOCODE, Utility.getVideoCode(resourceModel.link))
                intent.putExtra(Constants.VIDEOTITLE, resourceModel.title)
                intent.putExtra(Constants.RESOURCEID, resourceModel.id)
                intent.putExtra(Constants.VIDEODESCRIPTION, resourceModel.description)
                intent.putExtra(Constants.RESOURCEVIDEO, true)
                startActivity(intent)
            } else {
                Utility.redirectUsingCustomTab(requireActivity(), resourceModel.link)
                Log.d("resource_viewed", "beforeliked : " + resourceModel.view + ", " + resourceModel.total_views + ", " + resourceModel.id)
                if (resourceModel.view == 0) {
                    viewModel.resourceViewed(pm.getString(Constants.JWTTOKEN), resourceModel.id)
                            ?.observeForever { resourceViewsResponse ->
                                if (resourceViewsResponse.error_code == 0) {
                                    resourceModel.view = 1
                                    Log.d("resource_viewed", "onliked begore: " + resourceModel.view + ", " + resourceModel.total_views + ", " + resourceModel.id)
                                    resourceModel.total_views = resourceModel.total_views + 1
                                    Log.d("resource_viewed", "onliked : " + resourceModel.view + ", " + resourceModel.total_views + ", " + resourceModel.id)
                                    viewModel.saveResource(resourceModel)
                                }
                            }
                }
            }
        } else if (resourceModel.type.equals("theory", ignoreCase = true)) {
            Utility.redirectUsingCustomTab(requireActivity(), resourceModel.link)
            Log.d("resource_viewed", "beforeliked : " + resourceModel.view + ", " + resourceModel.total_views + ", " + resourceModel.id)
            if (resourceModel.view == 0) {
                viewModel.resourceViewed(pm.getString(Constants.JWTTOKEN), resourceModel.id)
                        ?.observeForever { resourceViewsResponse ->
                            if (resourceViewsResponse.error_code == 0) {
                                resourceModel.view = 1
                                Log.d("resource_viewed", "onliked begore: " + resourceModel.view + ", " + resourceModel.total_views + ", " + resourceModel.id)
                                resourceModel.total_views = resourceModel.total_views + 1
                                Log.d("resource_viewed", "onliked : " + resourceModel.view + ", " + resourceModel.total_views + ", " + resourceModel.id)
                                viewModel.saveResource(resourceModel)
                            }
                        }
            }
        }
    }

    override fun onResourceSeeAllClicked(course_topic_id: Int) {
        val intent = Intent(activity, ResourcesActivity::class.java)
        intent.putExtra(Constants.TOPICID, course_topic_id)
        startActivity(intent)
    }
}
