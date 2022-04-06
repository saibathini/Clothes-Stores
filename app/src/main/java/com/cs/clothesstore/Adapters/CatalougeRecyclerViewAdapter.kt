package com.cs.clothesstore.Adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.cs.clothesstore.BasicApplication
import com.cs.clothesstore.R

import com.cs.clothesstore.Models.Product

class CatalougeRecyclerViewAdapter(
    private val values: List<Product>, private val clickListener:(Int)->Unit
) : RecyclerView.Adapter<CatalougeRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_catalouge_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.itemName.text = item.name
        holder.itemPrice.text = "$${item.price}"

        Glide.with(BasicApplication.getContext()!!).load(item.image).into(holder.itemImage)

        if (item.isWishlisted) {
            holder.itemWishlisted.visibility = View.VISIBLE
        }
        else {
            holder.itemWishlisted.visibility = View.INVISIBLE
        }

        holder.layout.setOnClickListener {
            clickListener(position)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.item_image)
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemPrice: TextView = view.findViewById(R.id.item_price)
        val itemWishlisted: ImageView = view.findViewById(R.id.wishlist_image)
        val layout: ConstraintLayout = view.findViewById(R.id.layout)
    }
}