package com.example.ducksample.fragments.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.ducksample.fragments.main.MainViewModel

object MainAdapterDiffUtils : DiffUtil.ItemCallback<MainViewModel.Item>() {

    override fun areItemsTheSame(oldItem: MainViewModel.Item, newItem: MainViewModel.Item): Boolean {
        return oldItem.duckItem == newItem.duckItem
    }

    override fun areContentsTheSame(oldItem: MainViewModel.Item, newItem: MainViewModel.Item): Boolean {
        return oldItem.isInCart == newItem.isInCart
    }
}
