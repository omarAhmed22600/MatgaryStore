package com.brandsin.store.ui.main.addproduct

data class SelectedAttrsWithPrice(
    val attrId:Int,
    val selectedAttrId: List<Attrs> = listOf<Attrs>(),
    val selectionIds: List<Int> = emptyList()
)

   data class Attrs(
    val selectionId:Int,
    val selectedPrice: Double = 0.0
)
fun areAllPricesZero(selectedAttrsList: List<SelectedAttrsWithPrice>): Boolean {
    return selectedAttrsList.all { selectedAttrs ->
        selectedAttrs.selectedAttrId.all { attr ->
            attr.selectedPrice == 0.0
        }
    }
}
