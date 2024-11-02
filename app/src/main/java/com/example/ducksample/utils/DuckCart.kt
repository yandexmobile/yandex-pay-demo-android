package com.example.ducksample.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.EnumSet

object DuckCart {

    private val _stateFlow = MutableStateFlow(EnumSet.noneOf(DuckItem::class.java))

    // observing the content:

    val stateFlow = _stateFlow.asStateFlow()

    // managing the items:

    fun addItem(item: DuckItem) = _stateFlow.update {
        EnumSet.copyOf(it).apply { add(item) }
    }

    fun remItem(item: DuckItem) = _stateFlow.update {
        EnumSet.copyOf(it).apply { remove(item) }
    }

    fun clear() = _stateFlow.update {
        EnumSet.noneOf(DuckItem::class.java)
    }
}