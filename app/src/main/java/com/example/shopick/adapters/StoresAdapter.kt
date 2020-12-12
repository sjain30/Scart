package com.example.shopick.adapters

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.shopick.R
import com.example.shopick.ShoppingListViewModel
import com.example.shopick.datamodels.Item
import java.util.*

class StoresAdapter(
    private val subjects: ArrayList<String>
) : RecyclerView.Adapter<StoresAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.storeName.text = subjects[position]
    }


    override fun getItemCount() = subjects.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var storeName: TextView = itemView.findViewById(R.id.text_store_name)
        var storeAddress: TextView = itemView.findViewById(R.id.text_store_address)
    }
}