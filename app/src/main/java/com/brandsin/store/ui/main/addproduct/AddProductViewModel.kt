package com.brandsin.store.ui.main.addproduct

import androidx.appcompat.app.AlertDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brandsin.store.R
import com.google.gson.Gson
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.DataItem
import com.brandsin.store.model.main.products.ListUnitResponse
import com.brandsin.store.model.main.products.add.AddProductRequest
import com.brandsin.store.model.main.products.add.AddProductResponse

import com.brandsin.store.model.main.products.productcategories.ProductCategoriesData
import com.brandsin.store.model.main.products.productcategories.ProductCategoriesResponse
import com.brandsin.store.model.profile.updatestore.UploadRequest
import com.brandsin.store.model.profile.updatestore.UploadResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber
import java.io.File

class AddProductViewModel : BaseViewModel()
{

    //add

    val attributesList = MutableLiveData<List<ProductAttributesResponseItem>>()
    var colorList: ArrayList<Option> = ArrayList()
    var massList: ArrayList<Option> = ArrayList()
    var capacityList: ArrayList<Option> = ArrayList()
    var categoriesList: ArrayList<ProductCategoriesData> = ArrayList()
    var unitList: ArrayList<DataItem> = ArrayList()
    var addProductRequest = AddProductRequest()
    var addproductSkuAdapter = MutableLiveData<AddProductSkuAdapter>()
    var validate = false
    var skuPosition = -1
    var imageList = MutableLiveData(mutableListOf<PhotoModel>())
    var fileImageList = MutableLiveData(mutableListOf<File>())
    var uploadRequest = UploadRequest ()
    val selectedColorText = MutableLiveData("")
    val selectedMassText = MutableLiveData("")
    val selectedCapacityText = MutableLiveData("")
    // Backing property for the attributes list
     val attributes = MutableLiveData<List<SelectedAttrsWithPrice>>()

    // Publicly exposed immutable LiveData

    var canChangePRice = MutableLiveData(true)
    val selectedAttrId = MutableLiveData(mutableListOf<SelectedAttrsWithPrice>())

    fun updateSelectedItemIds(attributeId: Int, selectedIds: List<Int>) {
        Timber.e("update selected it $attributeId $selectedIds")

        // Map selectedIds to a list of Attrs
        val selectedAttrsList = selectedIds.map { id ->
            Attrs(selectionId = id)
        }
        selectedAttrId.value!!.add(
            SelectedAttrsWithPrice(
                attrId = attributeId,
                selectionIds = selectedIds
            )
        )

        // Get the current list of attributes
        val currentAttributes = attributes.value.orEmpty().toMutableList()

        // Check if the attributeId already exists in the list
        val existingAttributeIndex = currentAttributes.indexOfFirst { it.attrId == attributeId }

        if (existingAttributeIndex != -1) {
            // If it exists, update the selectedAttrId for that attribute
            val updatedAttribute = currentAttributes[existingAttributeIndex].copy(
                selectedAttrId = selectedAttrsList
            )
            currentAttributes[existingAttributeIndex] = updatedAttribute
        } else {
            // If it doesn't exist, add a new SelectedAttrsWithPrice entry
            val newAttribute = SelectedAttrsWithPrice(
                attrId = attributeId,
                selectedAttrId = selectedAttrsList
            )
            currentAttributes.add(newAttribute)
        }

        // Update the LiveData with the modified list
        attributes.value = currentAttributes
    }


    fun updateOptionPrice(attributeId: Int, optionId: Int, newPrice: Double) {
        // Update the selected price for a specific option (Attrs)
        Timber.e("update option price $attributeId, $optionId, $newPrice")
        attributes.value = attributes.value?.map { attribute ->
            if (attribute.attrId == attributeId) {
                // Update the price of the specific Attrs with the matching selectionId (optionId)
                val updatedAttrs = attribute.selectedAttrId.map { option ->
                    if (option.selectionId == optionId) {
                        option.copy(selectedPrice = newPrice) // Update selectedPrice
                    } else {
                        option
                    }
                }
                // Return the updated attribute with modified Attrs list
                attribute.copy(selectedAttrId = updatedAttrs)
            } else {
                attribute
            }
        }
    }

    fun resetOptionPrices(attributeId: Int) {
        // Reset the price for all options (Attrs) of the specified attributeId to 0.0
        Timber.e("reset option prices for attribute $attributeId")
        attributes.value = attributes.value?.map { attribute ->
            if (attribute.attrId == attributeId) {
                // Reset the price of all Attrs for the matching attributeId
                val resetAttrs = attribute.selectedAttrId.map { option ->
                    option.copy(selectedPrice = 0.0) // Reset selectedPrice to 0.0
                }
                // Return the updated attribute with modified Attrs list
                attribute.copy(selectedAttrId = resetAttrs)
            } else {
                attribute
            }
        }
    }
    // Method to update selected item IDs based on attribute ID


    init {
        // Initialize the map to hold empty lists for each attribute ID if necessary
        attributes.value = listOf() // Replace with actual initial data

        getProductCategories()
        getProductAttributes()
    }
    fun getLabelsFromModel(list :ArrayList<Option>):Array<String>
    {
        val newList = arrayListOf<String>()
        list.forEach {
            newList.add(it.label)
        }
        return newList.toTypedArray()
    }
/*    fun showColorSelectionDialog(context: Context){
        // Build a multi-select dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.select_color))

        val items = getLabelsFromModel(colorList)
        // Use a boolean array to track selected items
        val checkedItems = BooleanArray(colorList.size)

        // Create multi-choice dialog
        builder.setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
            if (isChecked) {
                selectedColorsItems.add(which) // Add selected item index
            } else {
                selectedColorsItems.remove(which) // Remove deselected item index
            }
        }

        // Set up the OK button to update the dropdown
        builder.setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
            val selectedStrings = selectedColorsItems.map { items[it] } // Get selected item names
            selectedColorText.value = selectedStrings.joinToString(", ") // Display selected items
        }

        // Cancel button to close the dialog
        builder.setNegativeButton(getString(R.string.cancel), null)

        // Show the dialog
        val dialog = builder.create()
        dialog.show()
    }
    fun showMassSelectionDialog(context: Context){
        // Build a multi-select dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.select_mass))

        val items = getLabelsFromModel(massList)
        // Use a boolean array to track selected items
        val checkedItems = BooleanArray(massList.size)

        // Create multi-choice dialog
        builder.setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
            if (isChecked) {
                selectedMassItems.add(which) // Add selected item index
            } else {
                selectedMassItems.remove(which) // Remove deselected item index
            }
        }

        // Set up the OK button to update the dropdown
        builder.setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
            val selectedStrings = selectedMassItems.map { items[it] } // Get selected item names
            selectedMassText.value = selectedStrings.joinToString(", ") // Display selected items
        }

        // Cancel button to close the dialog
        builder.setNegativeButton(getString(R.string.cancel), null)

        // Show the dialog
        val dialog = builder.create()
        dialog.show()
    }*/
/*
    fun showCapacitySelectionDialog(context: Context){
        // Build a multi-select dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.select_capacity))

        val items = getLabelsFromModel(capacityList)
        // Use a boolean array to track selected items
        val checkedItems = BooleanArray(capacityList.size)

        // Create multi-choice dialog
        builder.setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
            if (isChecked) {
                selectedCapacityItems.add(which) // Add selected item index
            } else {
                selectedCapacityItems.remove(which) // Remove deselected item index
            }
        }

        // Set up the OK button to update the dropdown
        builder.setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
            val selectedStrings = selectedCapacityItems.map { items[it] } // Get selected item names
            selectedCapacityText.value = selectedStrings.joinToString(", ") // Display selected items
        }

        // Cancel button to close the dialog
        builder.setNegativeButton(getString(R.string.cancel), null)

        // Show the dialog
        val dialog = builder.create()
        dialog.show()
    }
*/

    private fun getProductAttributes() {
        requestCall<Response<ProductAttributesResponse>>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo()
                    .getProductAttrs(PrefMethods.getStoreData()!!.id!!.toInt())
            }
        })
        { res ->
            Timber.e("res is ${res!!.body()}")

            if (res!!.isSuccessful)
           {
                attributesList.value = res.body()?.data.orEmpty()
               /*colorList = res.body()?.data.orEmpty()[0].options as ArrayList<Option>
               massList = res.body()?.data.orEmpty()[1].options as ArrayList<Option>
               capacityList = res.body()?.data.orEmpty()[2].options as ArrayList<Option>*/
               Timber.e("$colorList\n$massList$capacityList")
           }
        }
    }

    private fun getProductCategories() {
        requestCall<ProductCategoriesResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo()
                    .getProductCategories(0, PrefMethods.getLanguage(), PrefMethods.getStoreData()!!.id!!.toInt())
            }
        })
        { res ->
            if (res!!.data!!.isNotEmpty()) {
                categoriesList = res.data as ArrayList<ProductCategoriesData>
            }
        }
    }

     fun getUnitsList(categoriesIds: ArrayList<Int>) {
        requestCall<ListUnitResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo()
                        .getUnitsList(categoriesIds, PrefMethods.getLanguage())
            }
        })
        { res ->
            if (res!!.data!!.isNotEmpty()) {
                unitList = res.data as ArrayList<DataItem>
            }
        }
    }

    fun onAddSizeClicked() {
        addproductSkuAdapter.value?.updateList("","",skuPosition)
    }

    fun onPhoto1Clicked() {
        setValue(Codes.SELECT_PHOTO1)
    }
    fun onPhoto2Clicked() {
        setValue(Codes.SELECT_PHOTO2)
    }
    fun onPhoto3Clicked() {
        setValue(Codes.SELECT_PHOTO3)
    }

    fun onPhoto4Clicked() {
        setValue(Codes.SELECT_PHOTO4)
    }

    fun onAddClicked() {
        addproductSkuAdapter.value?.getData()
    }

    fun validation(){
        addProductRequest.imagesList = fileImageList.value.orEmpty().filter { it.extension == "jpg" || it.extension == "png" ||it.extension == "jpeg" }
        addProductRequest.videosList = fileImageList.value.orEmpty().filter { it.extension != "jpg" && it.extension != "png" && it.extension != "jpeg" }
        if (addProductRequest.categoriesIds == null || addProductRequest.categoriesIds!!.size == 0) {
            setValue(Codes.EMPTY_TYPE)
        }else if ( addProductRequest.name  == null  || addProductRequest.name.toString().trim().isEmpty() ) {
            setValue(Codes.NAME_EMPTY)
//        }else if ( addProductRequest.nameEn  == null  || addProductRequest.nameEn.toString().trim().isEmpty() ) {
//            setValue(Codes.EMPTY_PRODUCT_NAME_EN)
        }else if (  addProductRequest.description == null  || addProductRequest.description.toString().trim().isEmpty() ) {
            setValue(Codes.EMPTY_DESCRIPTION)
//        }else if (  addProductRequest.descriptionEn == null  || addProductRequest.descriptionEn.toString().trim().isEmpty() ) {
//            setValue(Codes.EMPTY_PRODUCT_DESCRIPTION_EN)
        }else if (   addProductRequest.imagesList.orEmpty().isEmpty() && addProductRequest.videosList.orEmpty().isEmpty()) {
            setValue(Codes.EMPTY_IMAGE)
        }
        else {
            createProduct()
        }
    }
    fun createProduct() {

        var gson = Gson()

        if (attributes.value.orEmpty().size>1){
            addProductRequest.type = "variable"
        }else{
            addProductRequest.type = "simple"
        }

        if ( addProductRequest.nameEn  == null  || addProductRequest.nameEn.toString().trim().isEmpty() ) {
            addProductRequest.nameEn = ""
        }
        if ( addProductRequest.descriptionEn == null  || addProductRequest.descriptionEn.toString().trim().isEmpty() ) {
            addProductRequest.descriptionEn = ""
        }

        addProductRequest.storeId = PrefMethods.getStoreData()!!.id!!.toInt()
        addProductRequest.locale = PrefMethods.getLanguage()

        obsIsVisible.set(true)
        /*requestCall<AddProductResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().createProduct(addProductRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.success) {
                true -> {
                    apiResponseLiveData.value = ApiResponse.success(res)
                }
                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message.toString())
                }
            }
        }*/
    }

    fun upload(i: Int){
        obsIsVisible.set(true)
        requestCall<UploadResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().upload(uploadRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.success) {
                true -> {
//                    addProductRequest.mediaId!!.add(i,res.data!!.id!!)
                }
                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message)
                }
            }
        }
    }
    fun onCategoriesClicked() {
        if (categoriesList.isNotEmpty()) {
            setValue(Codes.PRODUCT_CATEGORIES_CLICKED)
        }else{
            getProductCategories()
        }
    }
}