package com.cs.clothesstore.Fragments

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cs.clothesstore.BasicApplication
import com.cs.clothesstore.R
import com.cs.clothesstore.productsList
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val ARG_PARAM1 = "param1"

class ItemDetailsFragment : BottomSheetDialogFragment() {

    private var selectedPosition: Int = 0
    private lateinit var rootContainer: ConstraintLayout
    private lateinit var itemImage: ImageView
    private lateinit var itemOldPrice: TextView
    private lateinit var itemNewPrice: TextView
    private lateinit var close: TextView
    private lateinit var itemStock: TextView
    private lateinit var itemName: TextView
    private lateinit var itemCategory: TextView
    private lateinit var itemAmountInStock: TextView
    private lateinit var btnWishList: Button
    private lateinit var btnAddToCart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedPosition = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_details, container, false)

        rootContainer = view.findViewById(R.id.layout) as ConstraintLayout
        itemImage = view.findViewById(R.id.item_image) as ImageView
        itemOldPrice = view.findViewById(R.id.old_price) as TextView
        itemNewPrice = view.findViewById(R.id.new_price) as TextView
        itemStock = view.findViewById(R.id.stock) as TextView
        itemName = view.findViewById(R.id.item_name) as TextView
        itemCategory = view.findViewById(R.id.item_category) as TextView
        itemAmountInStock = view.findViewById(R.id.amount_in_stock) as TextView
        close = view.findViewById(R.id.close) as TextView
        btnWishList = view.findViewById(R.id.btn_wishlist) as Button
        btnAddToCart = view.findViewById(R.id.btn_add_to_cart) as Button

        Glide.with(BasicApplication.getContext()!!).load(productsList[selectedPosition].image).into(itemImage)

        if (productsList[selectedPosition].oldPrice > 0) {
            itemOldPrice.text = "$${productsList[selectedPosition].oldPrice}"
            itemOldPrice.setPaintFlags(itemOldPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
        }
        itemNewPrice.text = "$${productsList[selectedPosition].price}"
        itemName.text = "${productsList[selectedPosition].name}"
        itemCategory.text = "${productsList[selectedPosition].category}"
        itemAmountInStock.text = "${productsList[selectedPosition].stock}"

        if (productsList[selectedPosition].stock == 0) {
            itemStock.text = "Out Of Stock"
        }
        else {
            itemStock.text = "In Stock"
        }

        close.setOnClickListener { dialog!!.dismiss()  }

        btnWishList.setOnClickListener {
            Toast.makeText(requireContext(), "Product added to wishlist", Toast.LENGTH_SHORT).show()
            productsList[selectedPosition].isWishlisted = true
        }

        btnAddToCart.setOnClickListener {
            Toast.makeText(requireContext(), "Product added to cart", Toast.LENGTH_SHORT).show()
            productsList[selectedPosition].isWishlisted = false
            productsList[selectedPosition].isAddedToCart = true
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    companion object {
        fun newInstance(param1: Int) =
            ItemDetailsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }
}