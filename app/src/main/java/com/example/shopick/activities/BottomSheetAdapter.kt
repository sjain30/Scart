package com.example.shopick.activities

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.shopick.R
import com.example.shopick.ShoppingListViewModel
import java.util.ArrayList

class BottomSheetAdapter(private val subjects: ArrayList<String>,
                         private val context: Context,
                         private val shoppingListViewModel: ShoppingListViewModel
) : RecyclerView.Adapter<BottomSheetAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.setText(subjects[position])
        Log.d("TAG", "onBindViewHolder: ${subjects[position]}")

        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                holder.itemName.paintFlags = holder.itemName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
                holder.itemName.paintFlags = holder.itemName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
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