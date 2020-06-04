package com.prepare.prepareurself.preferences.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class PreferencesActivity : BaseActivity(),PreferenceRvAdapter.PrefListener,ChoosePrefAdapter.ChoosePrefListener {

    private lateinit var adapter: PreferenceRvAdapter
    private lateinit var vm:PreferenceViewModel
    private lateinit var pm: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        vm = ViewModelProvider(this)[PreferenceViewModel::class.java]

        pm = PrefManager(this)

        val title = findViewById<TextView>(R.id.title)

        title.text = "Preferences"

        adapter = PreferenceRvAdapter(this)
        rv_prefs.layoutManager = LinearLayoutManager(this)
        rv_prefs.adapter = adapter

        vm.fetchPreferences(pm.getString(Constants.JWTTOKEN))

        vm.getPrefs()?.observe(this, Observer {
            it?.let {
                adapter.setData(it)
            }
        })

        lin_add_pref.setOnClickListener {
            showAddPrefDialog()
        }

    }

    private fun showAddPrefDialog() {

        val dialog = Dialog(this,android.R.style.Theme_Light)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(this).inflate(R.layout.choose_pref_dialog_layout, null)
        dialog.setContentView(view)
        dialog.setCancelable(false)

        val adapter = ChoosePrefAdapter(this)
        view.rv_choose_pref.layoutManager = LinearLayoutManager(this)
        view.rv_choose_pref.adapter = adapter

        vm.getCourses()?.observe(this, Observer {
            it?.let {
                adapter.setData(it)
            }
        })

        dialog.setOnKeyListener { d, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK){
                d.cancel()
            }
            return@setOnKeyListener true
        }

        dialog.show()

        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT)

    }

    override fun onPrefCanceled(prefModel: PreferencesModel?) {

    }

    override fun onItemClicked(courseModel: CourseModel) {

    }
}
