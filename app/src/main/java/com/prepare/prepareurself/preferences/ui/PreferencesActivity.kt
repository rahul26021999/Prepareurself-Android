package com.prepare.prepareurself.preferences.ui

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prepare.prepareurself.R
import com.prepare.prepareurself.preferences.data.PreferencesModel
import com.prepare.prepareurself.preferences.viewmodel.PreferenceViewModel
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.PrefManager
import com.prepare.prepareurself.utils.Utility
import kotlinx.android.synthetic.main.activity_preferences.*
import kotlinx.android.synthetic.main.choose_pref_dialog_layout.view.*
import kotlinx.android.synthetic.main.layout_topbar.*
import kotlinx.android.synthetic.main.layout_topbar.view.*


class PreferencesActivity : BaseActivity(),PreferenceRvAdapter.PrefListener,ChoosePrefAdapter.ChoosePrefListener {

    private lateinit var adapter: PreferenceRvAdapter
    private lateinit var vm:PreferenceViewModel
    private lateinit var pm: PrefManager
    private lateinit var temList : ArrayList<PreferencesModel>
    private lateinit var chooseDialog: Dialog
    private lateinit var mylist  : ArrayList<PreferencesModel>
    val displyList = ArrayList<PreferencesModel>()
    val allAdapter = ChoosePrefAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)


        vm = ViewModelProvider(this)[PreferenceViewModel::class.java]

        pm = PrefManager(this)

        temList = ArrayList<PreferencesModel>()
        mylist = ArrayList()

        vm.getUserPreferences(pm.getString(Constants.JWTTOKEN))
                ?.observe(this, Observer {
                    if (it!=null){
                        it.preferences?.let { it1 -> mylist.addAll(it1) }
                        adapter.setData(mylist)
                    }else{
                        Utility.showToast(this,Constants.SOMETHINGWENTWRONG)
                    }
                })

        val title = findViewById<TextView>(R.id.title)

        title.text = "Preferences"

        adapter = PreferenceRvAdapter(this)
        rv_prefs.layoutManager = LinearLayoutManager(this)
        rv_prefs.adapter = adapter
        rv_prefs.adapter = adapter

//        vm.getPrefs()?.observe(this, Observer {
//            temList.clear()
//            it?.let {
//                temList.addAll(it)
//                adapter.setData(temList)
//            }
//        })

        lin_add_pref.setOnClickListener {
            showAddPrefDialog()
        }

        backBtn.setOnClickListener {
            finish()
        }

        save_pref_btn.setOnClickListener {
            progress_bar_pref.visibility = View.VISIBLE
            Log.d("tempList","$mylist")
            val sublist = ArrayList<Int>()
            mylist.forEach {
                it.id?.let { it1 -> sublist.add(it1) }
            }
            vm.updatePref(pm.getString(Constants.JWTTOKEN),sublist)
                    ?.observe(this, Observer {
                        if (it!=null){
                            Utility.showToast(this,"Preferences updated successfully")
                        }else{
                            Utility.showToast(this, Constants.SOMETHINGWENTWRONG)
                        }
                        vm.updatePrefDb(mylist)
                        progress_bar_pref.visibility = View.GONE
                    })
        }


    }

    private fun showAddPrefDialog() {

        chooseDialog = Dialog(this,R.style.DialogTheme)

        chooseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(this).inflate(R.layout.choose_pref_dialog_layout, null)
        chooseDialog.setContentView(view)
        chooseDialog.setCancelable(false)

        view.backBtn.setOnClickListener {
            chooseDialog.cancel()
        }

        view.rv_choose_pref.layoutManager = LinearLayoutManager(this)
        view.rv_choose_pref.adapter = allAdapter

        view.title.text = "Select Preference"

        view.et_pref_name.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                allAdapter.filter.filter(s)
            }
        })


        vm.fetchPreferences(pm.getString(Constants.JWTTOKEN))?.observe(this, Observer {
            it?.let {it2->
                displyList.clear()
                it2.preferences?.let {
                    it1 -> displyList.addAll(it1)
                    it1.forEach {
                        mylist.forEach {it1->
                            if (it.id == it1.id){
                                displyList.remove(it)
                            }
                        }
                    }
                }
                allAdapter.setData(displyList)
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
        prefModel?.let { displyList.add(it) }
        adapter.updateDataset()
    }

    override fun onItemClicked(prefModel: PreferencesModel) {
        mylist.add(prefModel)
        displyList.add(prefModel)
        adapter.updateDataset()
        chooseDialog.cancel()
    }
}
