package com.brandsin.store.ui.main.addproduct

data class ProductAttributesResponseItem(
    val id: Int,
    val label: String,
    val options: List<Option>,
    val store_id: Int,
    val type: String,
    var selectedOptionIds: List<Int>? = emptyList(), // To store selected option IDs
    var isAddPriceSelected:Boolean = false
)