package com.example.shopick.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.college.collegeconnect.utils.ImageHandler
import com.example.shopick.R
import com.example.shopick.activities.CartActivityViewModel
import com.example.shopick.datamodels.Cart

class CartAdapter(
    val context: Context,
    val arrayList: ArrayList<Cart>,
    val cartActivityViewModel: CartActivityViewModel
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.price.text = "₹${arrayList[position].price}"
        holder.cutPrice.text = "₹${arrayList[position].cutPrice}"
        holder.productTitle.text = arrayList[position].productName
        holder.quantity.text = arrayList[position].quantity

        holder.add.setOnClickListener {
            holder.quantity.text = "${arrayList[position].quantity?.toInt()?.plus(1)}"
            arrayList[position].quantity = holder.quantity.text.toString()
            cartActivityViewModel.flashList(arrayList)
        }
        holder.dec.setOnClickListener {
            if (arrayList[position].quantity?.toInt()!! > 0) {
                holder.quantity.text = "${arrayList[position].quantity?.toInt()?.minus(1)}"
                arrayList[position].quantity = holder.quantity.text.toString()
                cartActivityViewModel.flashList(arrayList)
            }
        }


        ImageHandler().getSharedInstance(context)?.load(arrayList[position].image)
            ?.into(holder.image)
    }

    override fun getItemCount() = arrayList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val price: TextView = itemView.findViewById(R.id.product_price)
        val cutPrice: TextView = itemView.findViewById(R.id.cut_price)
        val image: ImageView = itemView.findViewById(R.id.product_image)
        val productTitle: TextView = itemView.findViewById(R.id.product_title)
        val quantity: TextView = itemView.findViewById(R.id.qty)
        val dec: TextView = itemView.findViewById(R.id.dec)
        val add: TextView = itemView.findViewById(R.id.add)
    }
}