package com.example.ducksample.fragments.info

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ducksample.utils.DuckCart
import com.example.ducksample.utils.DuckItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InfoViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    data class State (val duckItem: DuckItem, val isInCart: Boolean)

    private val itemFromArgs = resolveDuckItemFromArgs()
    private val _stateFlow = MutableStateFlow(State(itemFromArgs, false))

    // observing the content:

    val stateFlow: StateFlow<State> = _stateFlow.asStateFlow()

    // initialization:

    init {
        viewModelScope.launch {
            DuckCart.stateFlow.collect { addedItems ->
                // propagate new state to observers:
                _stateFlow.update {
                    State(duckItem = itemFromArgs, isInCart = addedItems.contains(itemFromArgs))
                }
            }
        }
    }

    private fun resolveDuckItemFromArgs(): DuckItem {
        val itemID: String? = savedStateHandle[InfoFragment.ARG_DUCK_ITEM_ID]
        return DuckItem.entries.first { it.id == itemID }
    }

    // public interface:

    fun addItem(item: DuckItem) {
        DuckCart.addItem(item)
    }

    fun remItem(item: DuckItem) {
        DuckCart.remItem(item)
    }
}