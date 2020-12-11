package com.example.shopick

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ListAdapter(
    private val subjects: ArrayList<String>,
    private val context: Context) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }


    override fun getItemCount() = subjects.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        var itemName: EditText = itemView.findViewById(R.id.edt_list_item)
        var delete: ImageButton = itemView.findViewById(R.id.img_btn_delete)
    }
}