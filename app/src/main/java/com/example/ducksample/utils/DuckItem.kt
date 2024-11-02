package com.example.ducksample.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.ducksample.R

enum class DuckItem (
    val id: String,
    val price: Long,
    @DrawableRes val itemRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val descRes: Int,
) {
    GZHEL(
        id = "duck_item_id_gzhel",
        price = 2500,
        // stuff from resources:
        itemRes = R.drawable.duck_00,
        titleRes = R.string.duck_title_00,
        descRes = R.string.duck_desc_00,
    ),
    HATTER(
        id = "duck_item_id_hatter",
        price = 1800,
        // stuff from resources:
        itemRes = R.drawable.duck_01,
        titleRes = R.string.duck_title_01,
        descRes = R.string.duck_desc_01,
    ),
    SPACE(
        id = "duck_item_id_space",
        price = 800,
        // stuff from resources:
        itemRes = R.drawable.duck_02,
        titleRes = R.string.duck_title_02,
        descRes = R.string.duck_desc_02,
    ),
    PAINTED(
        id = "duck_item_id_painted",
        price = 2500,
        // stuff from resources:
        itemRes = R.drawable.duck_03,
        titleRes = R.string.duck_title_03,
        descRes = R.string.duck_desc_03,
    ),
    SAMURAI(
        id = "duck_item_id_samurai",
        price = 2000,
        // stuff from resources:
        itemRes = R.drawable.duck_04,
        titleRes = R.string.duck_title_04,
        descRes = R.string.duck_desc_04,
    ),
    STEAMPUNK(
        id = "duck_item_id_steampunk",
        price = 2000,
        // stuff from resources:
        itemRes = R.drawable.duck_05,
        titleRes = R.string.duck_title_05,
        descRes = R.string.duck_desc_05,
    )
}
