package com.example.shopick.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopick.R
import com.example.shopick.datamodels.MyPreferencesModel
import kotlinx.android.synthetic.main.rv_my_preferences_item.view.*

class MyGivenPreferenceAdapter(val context: Context, var list_Preferences:ArrayList<MyPreferencesModel>) : RecyclerView.Adapter<MyGivenPreferenceAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyGivenPreferenceAdapter.MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv_my_preferences_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyGivenPreferenceAdapter.MyViewHolder, position: Int) {
        holder.bindItems(list_Preferences[position])
    }

    override fun getItemCount(): Int {
        return list_Preferences.size
    }

    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(my_response:MyPreferencesModel) {
            val textQuestionPreference = itemView.preference_question
            val textAnsPreference  = itemView.preferencer_ans
            textQuestionPreference.text=my_response.question
            textAnsPreference.text=my_response.ans
        }

    }

}