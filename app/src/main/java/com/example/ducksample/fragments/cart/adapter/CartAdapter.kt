package com.example.ducksample.fragments.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ducksample.databinding.ItemCartBinding
import com.example.ducksample.utils.DuckItem
import com.example.ducksample.utils.YPayUtils

class CartAdapter: ListAdapter<DuckItem, CartAdapter.ItemViewHolder>(CartAdapterDiffUtils) {

    class ItemViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: DuckItem) {
            binding.itemCartImage.setImageResource(item.itemRes)
            binding.itemCartTitle.setText(item.titleRes)
            binding.itemCartPrice.setText(YPayUtils.formatPrice(binding.root.context, item.price))
        }
    }

    // implementing adapter interface:

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}
