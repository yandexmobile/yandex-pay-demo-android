package com.example.ducksample

import com.example.ducksample.utils.DuckItem

interface ScreenRouter {
    fun handleBack()
    fun showCartScreen()
    fun showItemScreen(duckItem: DuckItem)
}