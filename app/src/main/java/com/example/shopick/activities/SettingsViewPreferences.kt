package com.example.shopick.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopick.R

import com.example.shopick.adapters.MyViewHolder
import com.example.shopick.datamodels.Item
import com.example.shopick.datamodels.MyPreferencesModel
import com.example.shopick.utils.FirebaseUtil
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.api.Distribution
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_settings_view_preferences.*
import kotlinx.android.synthetic.main.rv_my_preferences_item.view.*

class SettingsViewPreferences : AppCompatActivity() {

    private val mDatabaseReference = FirebaseUtil.getDatabase().getReference("Preferences")
    var listOfPreferences=ArrayList<MyPreferencesModel>()
    lateinit var adapter:FirebaseRecyclerAdapter<MyPreferencesModel,MyViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_view_preferences)
        PreferencesRV.setHasFixedSize(true)
        PreferencesRV.layoutManager=LinearLayoutManager(this)
        fetchRecyclerViewOFPreferences()
        setData()

        backButton.setOnClickListener {
            finish()
        }

    }

    private fun setData() {
        val query= FirebaseDatabase.getInstance().reference.child("Preferences")
        val options=FirebaseRecyclerOptions.Builder<MyPreferencesModel>().setQuery(query,MyPreferencesModel::class.java).build()
        adapter=object:FirebaseRecyclerAdapter<MyPreferencesModel,MyViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_my_preferences_item, parent, false)
                return MyViewHolder(v)
            }
            override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: MyPreferencesModel) {
                holder.itemView.preference_question.text=model.question
                holder.itemView.preferencer_ans.text=model.ans
            }

        }
    }

    private fun fetchRecyclerViewOFPreferences() {

       listOfPreferences.clear()
        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(preferenceSnapshot in snapshot.children){
                    val item=preferenceSnapshot.getValue(MyPreferencesModel::class.java)
                    listOfPreferences.add(item!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    override fun onStart() {
        adapter.startListening()
        super.onStart()
    }

    override fun onStop() {
        adapter.stopListening()
        super.onStop()
    }

}