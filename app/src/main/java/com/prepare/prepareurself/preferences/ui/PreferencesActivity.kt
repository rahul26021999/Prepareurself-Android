package com.prepare.prepareurself.preferences.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prepare.prepareurself.R
import com.prepare.prepareurself.dashboard.data.model.CourseModel
import com.prepare.prepareurself.preferences.data.PreferencesModel
import com.prepare.prepareurself.preferences.viewmodel.PreferenceViewModel
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.PrefManager
import kotlinx.android.synthetic.main.activity_preferences.*
import kotlinx.android.synthetic.main.choose_pref_dialog_layout.view.*
import kotlinx.android.synthetic.main.layout_topbar.view.*

class PreferencesActivity : BaseActivity(),PreferenceRvAdapter.PrefListener,ChoosePrefAdapter.ChoosePrefListener {

    private lateinit var adapter: PreferenceRvAdapter
    private lateinit var vm:PreferenceViewModel
    private lateinit var pm: PrefManager
    private lateinit var temList : ArrayList<PreferencesModel>
    private lateinit var chooseDialog: Dialog
    private lateinit var mylist  : ArrayList<PreferencesModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        vm = ViewModelProvider(this)[PreferenceViewModel::class.java]

        pm = PrefManager(this)

        temList = ArrayList<PreferencesModel>()
        mylist = ArrayList()

        val c1 = PreferencesModel()
        c1.id = 1
        c1.name = "Android"

        val c2 = PreferencesModel()
        c2.id = 3
        c2.name = "Laravel"

        mylist.add(c1)
        mylist.add(c2)

        val title = findViewById<TextView>(R.id.title)

        title.text = "Preferences"

        adapter = PreferenceRvAdapter(this)
        rv_prefs.layoutManager = LinearLayoutManager(this)
        rv_prefs.adapter = adapter

        vm.fetchPreferences(pm.getString(Constants.JWTTOKEN))

//        vm.getPrefs()?.observe(this, Observer {
//            temList.clear()
//            it?.let {
//                temList.addAll(it)
//                adapter.setData(temList)
//            }
//        })
        adapter.setData(mylist)

        lin_add_pref.setOnClickListener {
            showAddPrefDialog()
        }

        save_pref_btn.setOnClickListener {
            Log.d("tempList","$temList")
        }


    }

    private fun showAddPrefDialog() {

        chooseDialog = Dialog(this,android.R.style.Theme_Light)

        chooseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(this).inflate(R.layout.choose_pref_dialog_layout, null)
        chooseDialog.setContentView(view)
        chooseDialog.setCancelable(false)

        val adapter = ChoosePrefAdapter(this)
        view.rv_choose_pref.layoutManager = LinearLayoutManager(this)
        view.rv_choose_pref.adapter = adapter

        view.backBtn.setOnClickListener {
            chooseDialog.cancel()
        }

        view.title.text = "Select Preference"

        val displyList = ArrayList<PreferencesModel>()

        vm.getPrefs()?.observe(this, Observer {
            it?.let {it2->
                displyList.addAll(it2)
                it2.forEach {
                    mylist.forEach {it1->
                        if (it.id == it1.id){
                            displyList.remove(it)
                        }
                    }
                }
                adapter.setData(displyList)
            }
        })

        chooseDialog.setOnKeyListener { d, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK){
                d.cancel()
            }
            return@setOnKeyListener true
        }

        chooseDialog.show()

        val window = chooseDialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT)

    }

    override fun onPrefCanceled(prefModel: PreferencesModel?, position:Int) {
        mylist.removeAt(position)
        adapter.updateDataset()
    }

    override fun onItemClicked(courseModel: PreferencesModel) {
        val prefModel = PreferencesModel()
        prefModel.id = courseModel.id
        prefModel.name = courseModel.name
        mylist.add(prefModel)
        adapter.updateDataset()
        chooseDialog.cancel()
    }
}
