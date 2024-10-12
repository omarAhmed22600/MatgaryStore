package com.brandsin.store.ui.main.addproduct

data class Option(
    val id: Int,
    val label: String,
    val value: String,
    var selectedPrice: Double = 0.0
)