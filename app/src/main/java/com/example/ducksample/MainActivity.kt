package com.example.ducksample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.ducksample.fragments.cart.CartFragment
import com.example.ducksample.fragments.info.InfoFragment
import com.example.ducksample.fragments.main.MainFragment
import com.example.ducksample.utils.DuckItem
import com.example.ducksample.utils.YPayUtils

class MainActivity: AppCompatActivity(R.layout.activity), ScreenRouter {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize the widgets/badges inventory:
        YPayUtils.initInventory(this)
        // add the initial fragment if needed:
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MainFragment>(R.id.fragment_container)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // shut down the widgets/badges inventory:
        YPayUtils.clearInventory()
    }

    // implementing screen router:

    override fun handleBack() {
        supportFragmentManager.popBackStack()
    }

    override fun showCartScreen() {
        supportFragmentManager.commit {
            addToBackStack(TAG_CART_FRAGMENT)
            setReorderingAllowed(true)
            add<CartFragment>(R.id.fragment_container)
        }
    }

    override fun showItemScreen(duckItem: DuckItem) {
        supportFragmentManager.commit {
            addToBackStack(TAG_ITEM_FRAGMENT)
            setReorderingAllowed(true)
            add<InfoFragment>(R.id.fragment_container, args = Bundle().apply {
                putString(InfoFragment.ARG_DUCK_ITEM_ID, duckItem.id)
            })
        }
    }

    companion object {
        const val TAG_CART_FRAGMENT = "tag_cart_fragment"
        const val TAG_ITEM_FRAGMENT = "tag_item_fragment"
    }
}
