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

class ListAdapter(
    private val subjects: ArrayList<Item>,
    private val context: Context,
    private val shoppingListViewModel: ShoppingListViewModel
) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemName.setText(subjects[position].name)
        Log.d("TAG", "onBindViewHolder: ${subjects[position]}")

        if (subjects[position].state!!) {
            holder.checkBox.isChecked = true
            holder.itemName.paintFlags = holder.itemName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                subjects[position].state = isChecked
                shoppingListViewModel.flashList(subjects)
                holder.itemName.paintFlags =
                    holder.itemName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            else {
                subjects[position].state = isChecked
                shoppingListViewModel.flashList(subjects)
                holder.itemName.paintFlags =
                    holder.itemName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
        holder.delete.setOnClickListener {
            shoppingListViewModel.removeItem(subjects[position])
        }
    }


    override fun getItemCount() = subjects.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var checkBox: CheckBox = itemView.findViewById(R.id.checkbox)
        var itemName: EditText = itemView.findViewById(R.id.edt_list_item)
        var delete: ImageButton = itemView.findViewById(R.id.img_btn_delete)
    }
}