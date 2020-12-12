package com.example.shopick.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopick.R
import com.example.shopick.adapters.MyGivenPreferenceAdapter
import com.example.shopick.datamodels.MyPreferencesModel
import com.example.shopick.utils.FirebaseUtil
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_settings_view_preferences.*

class SettingsViewPreferences : AppCompatActivity() {

    private val mDatabaseReference = FirebaseUtil.getDatabase().getReference("Preferences")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_view_preferences)

        PreferencesRV.layoutManager=LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        fetchRecyclerViewOFPreferences()

    }

    private fun fetchRecyclerViewOFPreferences() {

    }
}