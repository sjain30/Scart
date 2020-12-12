package com.example.shopick.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopick.R

class RecommendationAdapter(private val list:ArrayList<String>):RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recommed_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = list[position]
    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        val textView:TextView = itemView.findViewById(R.id.txt_recommend_name)
    }
}