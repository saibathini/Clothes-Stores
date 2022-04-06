package com.cs.clothesstore.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs.clothesstore.Activities.MainActivity
import com.cs.clothesstore.Adapters.BasketRecyclerViewAdapter
import com.cs.clothesstore.Models.Product
import com.cs.clothesstore.R
import com.cs.clothesstore.SwipeHelper
import com.cs.clothesstore.productsList

class BasketFragment : Fragment() {

    private var columnCount = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private var basketList = mutableListOf<Product>()


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
        val view = inflater.inflate(R.layout.fragment_basket_item_list, container, false)

        recyclerView = view.findViewById(R.id.list) as RecyclerView
        emptyView = view.findViewById(R.id.empty_text) as TextView
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        filterData()
    }

    private fun filterData() {
        basketList!!.clear()
        for (i in 0 until productsList.size) {
            if (productsList[i].isAddedToCart) {
                basketList.add(productsList[i])
            }
        }
        if (basketList.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            showRecyclerView()
        }
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

    private fun showRecyclerView() {
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = BasketRecyclerViewAdapter(basketList)
            adapter!!.notifyDataSetChanged()

            val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(recyclerView) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    var buttons = listOf<UnderlayButton>()
                    val deleteButton = deleteButton(position)
                    buttons = listOf(deleteButton)
                    return buttons
                }
            })

            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            "Remove",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    val productId = basketList[position].productId
                    for (i in 0 until productsList.size) {
                        if (productId == productsList[i].productId) {
                            productsList[i].isAddedToCart = false
                            break
                        }
                    }
                    filterData()
                }
            })
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            BasketFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

}