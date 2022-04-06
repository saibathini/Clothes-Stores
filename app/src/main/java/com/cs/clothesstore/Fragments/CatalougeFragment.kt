package com.cs.clothesstore.Fragments

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs.clothesstore.Activities.MainActivity
import com.cs.clothesstore.Adapters.CatalougeRecyclerViewAdapter
import com.cs.clothesstore.BasicApplication
import com.cs.clothesstore.Models.CatalougeResponse
import com.cs.clothesstore.R
import com.cs.clothesstore.Rest.APIInterface
import com.cs.clothesstore.Rest.ApiClient
import com.cs.clothesstore.Rest.NetworkUtil
import com.cs.clothesstore.productsList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A fragment representing a list of Items.
 */
class CatalougeFragment : Fragment() {

    private var columnCount = 2
    private lateinit var recyclerView: RecyclerView

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
        val view = inflater.inflate(R.layout.fragment_catalouge_item_list, container, false)

        recyclerView = view.findViewById(R.id.list) as RecyclerView
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (productsList.isEmpty()) {
            val progressDialog = ProgressDialog(requireContext())
            progressDialog.setMessage("Loading ...")
            progressDialog.show()

            val apiService: APIInterface = ApiClient.client!!.create(APIInterface::class.java)
            val call: Call<CatalougeResponse?>? = apiService.getData()

            call!!.enqueue(object : Callback<CatalougeResponse?> {
                override fun onResponse(
                    call: Call<CatalougeResponse?>,
                    response: Response<CatalougeResponse?>
                ) {
                    Log.d("TAG", "onResponse: " + response)
                    if (response.isSuccessful()) {
                        val response: CatalougeResponse? = response.body()
                        productsList = response!!.products
                        showRecyclerView()
                        progressDialog.dismiss()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            BasicApplication.mContext,
                            "Cannot reach server",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<CatalougeResponse?>, t: Throwable) {
                    Log.d("TAG", "onFailure: " + t.message)
                    val networkStatus: String? =
                        NetworkUtil.getConnectivityStatusString(BasicApplication.mContext)
                    if (networkStatus.equals("Not connected to Internet", ignoreCase = true)) {
                        progressDialog.dismiss()
                        Toast.makeText(
                            BasicApplication.mContext,
                            "Connection Error!! Please check your Internet Connection.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(
                            BasicApplication.mContext,
                            "Cannot reach server",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }
        else {
            showRecyclerView()
        }
    }

    private fun listItemClicked(selectedPosition: Int){
        val args = Bundle()
        args.putInt("param1", selectedPosition)

        val detailsFragment: ItemDetailsFragment = ItemDetailsFragment.newInstance(selectedPosition)
        detailsFragment.setCancelable(true)
        detailsFragment.setArguments(args)
        detailsFragment.show(childFragmentManager, "details_fragment")
        childFragmentManager.executePendingTransactions()

        detailsFragment.getDialog()
            ?.setOnDismissListener(DialogInterface.OnDismissListener {
                showRecyclerView()
                updateBadgeCount()

                if (detailsFragment != null) {
                    detailsFragment.dismiss()
                }
            })
    }

    private fun showRecyclerView() {
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = CatalougeRecyclerViewAdapter(productsList, { selectedItem: Int -> listItemClicked(selectedItem) })
            adapter!!.notifyDataSetChanged()
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CatalougeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
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
            MainActivity.navigation!!.getOrCreateBadge(R.id.navigation_wishlist).number = wishlistCount
        }
        else {
            MainActivity.navigation!!.removeBadge(R.id.navigation_wishlist)
        }

        if (cartCount > 0) {
            MainActivity.navigation!!.getOrCreateBadge(R.id.navigation_basket).number = cartCount
        }
        else {
            MainActivity.navigation!!.removeBadge(R.id.navigation_basket)
        }
    }
}