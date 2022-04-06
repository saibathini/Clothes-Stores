package com.cs.clothesstore.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentManager
import com.cs.clothesstore.BasicApplication
import com.cs.clothesstore.Fragments.BasketFragment
import com.cs.clothesstore.Fragments.CatalougeFragment
import com.cs.clothesstore.Fragments.WishListFragment
import com.cs.clothesstore.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var fragmentManager: FragmentManager = getSupportFragmentManager()
    private lateinit var title: TextView

    companion object {
        var navigation: BottomNavigationView? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BasicApplication.mContext = this

        navigation = findViewById(R.id.navigation) as BottomNavigationView?
        title = findViewById(R.id.title) as TextView

        navigation!!.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

        val catalougeFragment = CatalougeFragment()
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_layout, catalougeFragment, "catalouge_fragment")
            .commit()
    }

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.navigation_catalouge -> {
                        title.text = "Catalouge"
                        val catalougeFragment = CatalougeFragment()
                        fragmentManager.beginTransaction()
                            .replace(R.id.fragment_layout, catalougeFragment, "catalouge_fragment")
                            .commit()
//                        item.setIcon(R.drawable.menu_home_selceted1)
                        return true
                    }
                    R.id.navigation_wishlist -> {
                        title.text = "Wishlist"
                        val wishListFragment = WishListFragment()
                        fragmentManager.beginTransaction()
                            .replace(R.id.fragment_layout, wishListFragment, "wishlist_fragment")
                            .commit()
                        return true
                    }
                    R.id.navigation_basket -> {
                        title.text = "Basket"
                        val basketFragment = BasketFragment()
                        fragmentManager.beginTransaction()
                            .replace(R.id.fragment_layout, basketFragment, "basket_fragment")
                            .commit()
                        return true
                    }
                }
                return false
            }
        }
}