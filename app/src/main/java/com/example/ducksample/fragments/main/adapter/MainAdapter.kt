package com.example.ducksample.fragments.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ducksample.databinding.ItemMainBinding
import com.example.ducksample.fragments.main.MainViewModel
import com.example.ducksample.utils.DuckItem
import com.example.ducksample.utils.YPayUtils
import com.yandex.pay.widgets.badge.api.model.renderdata.BadgeAlign
import com.yandex.pay.widgets.badge.api.model.renderdata.BadgeRenderData
import com.yandex.pay.widgets.badge.api.model.renderdata.BadgeTheme
import com.yandex.pay.widgets.badge.api.model.renderdata.CashbackBadgeColor
import com.yandex.pay.widgets.badge.api.model.renderdata.CashbackBadgeVariant
import com.yandex.pay.widgets.badge.api.model.renderdata.SplitBadgeColor
import com.yandex.pay.widgets.badge.api.model.renderdata.SplitBadgeVariant

class MainAdapter(
    private val onAddItem: (DuckItem) -> Unit,
    private val onRemItem: (DuckItem) -> Unit,
    private val onItemInfo: (DuckItem) -> Unit,
) : ListAdapter<MainViewModel.Item, MainAdapter.ItemViewHolder>(MainAdapterDiffUtils) {

    class ItemViewHolder(
        private val onAddItem: (DuckItem) -> Unit,
        private val onRemItem: (DuckItem) -> Unit,
        private val onItemInfo: (DuckItem) -> Unit,
        private val binding: ItemMainBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val splitRenderData = BadgeRenderData.SplitBadgeRenderData(
            theme = BadgeTheme.SYSTEM,
            align = BadgeAlign.LEFT,
            color = SplitBadgeColor.PRIMARY,
            variant = SplitBadgeVariant.SIMPLE
        )

        private val cashbackRenderData = BadgeRenderData.CashbackBadgeRenderData(
            theme = BadgeTheme.SYSTEM,
            align = BadgeAlign.LEFT,
            color = CashbackBadgeColor.PRIMARY,
            variant = CashbackBadgeVariant.DETAILED
        )

        fun bindTo(item: MainViewModel.Item) {
            binding.itemMainImage.setImageResource(item.duckItem.itemRes)
            binding.itemMainTitle.setText(item.duckItem.titleRes)
            binding.itemMainPrice.setText(YPayUtils.formatPrice(binding.root.context, item.duckItem.price))
            binding.itemMainBadgeSplit.setSum(item.duckItem.price.toBigDecimal())
            binding.itemMainBadgeSplit.setRenderData(splitRenderData)
            binding.itemMainBadgeCashback.setSum(item.duckItem.price.toBigDecimal())
            binding.itemMainBadgeCashback.setRenderData(cashbackRenderData)
            binding.itemMainButton.isInvisible = item.isInCart
            binding.itemMainButton.setOnClickListener { onAddItem(item.duckItem) }
            binding.itemMainInCart.isVisible = item.isInCart
            binding.itemMainInCart.setOnClickListener { onRemItem(item.duckItem) }
            binding.root.setOnClickListener { onItemInfo(item.duckItem) }
        }
    }

    // implementing adapter interface:

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(onAddItem, onRemItem, onItemInfo, ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}
