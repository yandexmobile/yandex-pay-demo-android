package com.example.ducksample.fragments.info

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ducksample.R
import com.example.ducksample.databinding.FragmentInfoBinding
import com.example.ducksample.utils.YPayUtils
import kotlinx.coroutines.launch

class InfoFragment: Fragment() {

    private val viewModel: InfoViewModel by viewModels()
    private lateinit var viewBinding: FragmentInfoBinding

    // implementing fragment interface:

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = TransitionInflater.from(context).inflateTransition(R.transition.slide)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentInfoBinding.inflate(inflater, container, false).apply {
            // lateinit the binding:
            viewBinding = this
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelState()
    }

    // private helpers:

    private fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect(::renderViewModelState)
            }
        }
    }

    private fun renderViewModelState(state: InfoViewModel.State) {
        viewBinding.infoImage.setImageResource(state.duckItem.itemRes)
        viewBinding.infoTitle.setText(state.duckItem.titleRes)
        viewBinding.infoDesc.setText(state.duckItem.descRes)
        viewBinding.infoPrice.setText(YPayUtils.formatPrice(requireContext(), state.duckItem.price))
        viewBinding.infoWidget.setSum(state.duckItem.price.toBigDecimal())
        viewBinding.infoButton.isInvisible = state.isInCart
        viewBinding.infoButton.setOnClickListener { viewModel.addItem(state.duckItem) }
        viewBinding.infoInCart.isVisible = state.isInCart
        viewBinding.infoInCart.setOnClickListener { viewModel.remItem(state.duckItem) }
    }

    companion object {
        const val ARG_DUCK_ITEM_ID = "arg_duck_item_id"
    }
}