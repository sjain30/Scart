package com.example.shopick.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.shopick.R
import com.example.shopick.activities.TransactionActivity
import com.example.shopick.datamodels.Store
import java.util.*
import kotlin.collections.ArrayList

class StoresAdapter(
    private val stores: ArrayList<Store>,
    private val context: Context
) : RecyclerView.Adapter<StoresAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.storeName.text = stores[position].name
        holder.storeAddress.text = stores[position].address
        holder.itemView.setOnClickListener {
            val intent = Intent(context, TransactionActivity::class.java)
            intent.putExtra("shop",stores[position].name)
            intent.putExtra("address",stores[position].address)
            context.startActivity(intent)
        }
    }


    override fun getItemCount() = stores.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var storeName: TextView = itemView.findViewById(R.id.text_store_name)
        var storeAddress: TextView = itemView.findViewById(R.id.text_store_address)
    }
}