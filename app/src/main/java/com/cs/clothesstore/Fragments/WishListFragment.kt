package com.cs.clothesstore.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs.clothesstore.Activities.MainActivity
import com.cs.clothesstore.Adapters.WishListRecyclerViewAdapter
import com.cs.clothesstore.Models.Product
import com.cs.clothesstore.R
import com.cs.clothesstore.productsList

class WishListFragment : Fragment() {

    private var columnCount = 1
    private lateinit var recyclerView: RecyclerView
    private var wishList = mutableListOf<Product>()
    private lateinit var emptyView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wishlist_item_list, container, false)

        recyclerView = view.findViewById(R.id.list) as RecyclerView
        emptyView = view.findViewById(R.id.empty_text) as TextView
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        filterData()
    }

    private fun filterData() {
        wishList!!.clear()
        for (i in 0 until productsList.size) {
            if (productsList[i].isWishlisted) {
                wishList.add(productsList[i])
            }
        }
        if (wishList.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        showRecyclerView()
        updateBadgeCount()
    }

    private fun updateBadgeCount() {
        var wishlistCount = 0
        var cartCount = 0
        for (i in 0 until productsList.size) {
            if (productsList[i].isWishlisted) {
                wishlistCount = wishlistCount + 1
            }
            if (productsList[i].isAddedToCart) {
                cartCount = cartCount + 1
            }
        }
        if (wishlistCount > 0) {
            MainActivity.navigation!!.getOrCreateBadge(R.id.navigation_wishlist).number =
                wishlistCount
        } else {
            MainActivity.navigation!!.removeBadge(R.id.navigation_wishlist)
        }

        if (cartCount > 0) {
            MainActivity.navigation!!.getOrCreateBadge(R.id.navigation_basket).number = cartCount
        } else {
            MainActivity.navigation!!.removeBadge(R.id.navigation_basket)
        }
    }

    private fun addToCartClicked(selectedPosition: Int) {
        Toast.makeText(requireContext(), "Product added to cart", Toast.LENGTH_SHORT).show()
        val productId = wishList[selectedPosition].productId
        for (i in 0 until productsList.size) {
            if (productId == productsList[i].productId) {
                productsList[i].isWishlisted = false
                productsList[i].isAddedToCart = true
                break
            }
        }
        filterData()
    }

    private fun showRecyclerView() {
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = WishListRecyclerViewAdapter(
                wishList,
                { selectedItem: Int -> addToCartClicked(selectedItem) })
            adapter!!.notifyDataSetChanged()
        }
    }

    private fun showErrorDialog() {
        lateinit var alertDialog: AlertDialog
        val builder = AlertDialog.Builder(requireContext())
        //set title for alert dialog
        builder.setTitle(R.string.app_name)
        //set message for alert dialog
        builder.setMessage("No items found.")
        builder.setPositiveButton("ok") { dialogInterface, which ->
            alertDialog.dismiss()
        }

        // Create the AlertDialog
        alertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        //performing positive action

        alertDialog.show()

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            WishListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}