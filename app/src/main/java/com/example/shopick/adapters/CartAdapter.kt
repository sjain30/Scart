package com.example.shopick.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopick.R
import com.example.shopick.datamodels.Cart

class CartAdapter(val arrayList: ArrayList<Cart>):RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        TODO("Not yet implemented")
    }

    override fun getItemCount() = arrayList.size

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val price:TextView = itemView.findViewById(R.id.product_price)
        val cutPrice:TextView = itemView.findViewById(R.id.cut_price)
        val image:ImageView = itemView.findViewById(R.id.product_image)
    }
}