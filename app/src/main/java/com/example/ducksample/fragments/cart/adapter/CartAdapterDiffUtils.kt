package com.example.ducksample.fragments.cart.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.ducksample.utils.DuckItem

object CartAdapterDiffUtils : DiffUtil.ItemCallback<DuckItem>() {

    override fun areItemsTheSame(oldItem: DuckItem, newItem: DuckItem): Boolean {
        return oldItem == newItem
    }

    // our items are enum values, so when the items
    // are the same, the contents are also the same:
    override fun areContentsTheSame(oldItem: DuckItem, newItem: DuckItem): Boolean {
        return true
    }
}
