package com.prepare.prepareurself.preferences.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prepare.prepareurself.R
import com.prepare.prepareurself.preferences.data.PreferencesModel
import com.prepare.prepareurself.preferences.viewmodel.PreferenceViewModel
import com.prepare.prepareurself.utils.BaseActivity
import com.prepare.prepareurself.utils.Constants
import com.prepare.prepareurself.utils.PrefManager
import kotlinx.android.synthetic.main.activity_preferences.*

class PreferencesActivity : BaseActivity(),PreferenceRvAdapter.PrefListener {

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

    }

    override fun onPrefCanceled(prefModel: PreferencesModel?) {

    }
}
