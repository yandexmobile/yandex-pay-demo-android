package com.example.ducksample.fragments.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ducksample.ScreenRouter
import com.example.ducksample.utils.DuckCart
import com.example.ducksample.utils.DuckItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    data class Item  (val duckItem: DuckItem, val isInCart: Boolean)
    data class State (val duckItems: List<Item>, val itemsInCart: Int)

    private val _stateFlow = MutableStateFlow(State(emptyList(), 0))

    // observing the content:

    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    // initialization:

    init {
        viewModelScope.launch {
            DuckCart.stateFlow.collect { cartItems ->
                // generate a new list of items:
                val itemsList = DuckItem.entries.map {
                    Item(duckItem = it, isInCart = cartItems.contains(it))
                }
                // propagate it to the observers:
                _stateFlow.update {
                    State(duckItems = itemsList, itemsInCart = cartItems.size)
                }
            }
        }
    }

    // public interface:

    fun addItem(item: DuckItem) {
        DuckCart.addItem(item)
    }

    fun remItem(item: DuckItem) {
        DuckCart.remItem(item)
    }

    fun showCartScreen(router: ScreenRouter) {
        router.showCartScreen()
    }

    fun showItemScreen(router: ScreenRouter, item: DuckItem) {
        router.showItemScreen(item)
    }
}