package com.cs.clothesstore.Adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cs.clothesstore.BasicApplication
import com.cs.clothesstore.R

import com.cs.clothesstore.Models.Product

class BasketRecyclerViewAdapter(
    private val values: List<Product>
) : RecyclerView.Adapter<BasketRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_basket_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.itemName.text = item.name
        holder.itemQty.text = "Qty: 1"
        holder.itemPrice.text = "$${item.price}"

        Glide.with(BasicApplication.getContext()!!).load(item.image).into(holder.itemImage)

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.item_image)
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemPrice: TextView = view.findViewById(R.id.item_price)
        val itemQty: TextView = view.findViewById(R.id.item_qty)
    }
}