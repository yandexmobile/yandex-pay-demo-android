package com.example.ducksample.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ducksample.ScreenRouter
import com.example.ducksample.databinding.FragmentMainBinding
import com.example.ducksample.fragments.main.adapter.MainAdapter
import com.example.ducksample.utils.DuckItem
import kotlinx.coroutines.launch

class MainFragment: Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var mainAdapter: MainAdapter
    private lateinit var viewBinding: FragmentMainBinding

    // implementing fragment interface:

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val adapter = MainAdapter(viewModel::addItem, viewModel::remItem, ::handleItemIconClick).apply {
            mainAdapter = this
        }
        val binding = FragmentMainBinding.inflate(inflater, container, false).apply {
            // set up main recycler:
            mainRecycler.adapter = adapter
            mainRecycler.layoutManager = GridLayoutManager(context, 2)
            mainRecycler.itemAnimator = null
            // set up the cart button:
            mainCartIcon.setOnClickListener(::handleCartIconClick)
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

    private fun renderViewModelState(state: MainViewModel.State) {
        // bind the items:
        mainAdapter.submitList(state.duckItems)
        // bind the cart counter:
        viewBinding.mainCartCount.isVisible = state.itemsInCart > 0
        viewBinding.mainCartCount.text = state.itemsInCart.toString()
    }

    private fun handleCartIconClick(view: View) {
        (activity as? ScreenRouter)?.let { viewModel.showCartScreen(it) }
    }

    private fun handleItemIconClick(item: DuckItem) {
        (activity as? ScreenRouter)?.let { viewModel.showItemScreen(it, item) }
    }
}
