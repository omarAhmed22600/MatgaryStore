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
    val selectedColorsItems = ArrayList<Int>()

    val selectedMassItems = ArrayList<Int>()
    val selectedCapacityItems = ArrayList<Int>()
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
    private val _attributes = MutableLiveData<List<ProductAttributesResponseItem>>()

    // Publicly exposed immutable LiveData
    val attributes: LiveData<List<ProductAttributesResponseItem>> get() = _attributes

    fun updateSelectedItemIds(attributeId: Int, selectedIds: List<Int>) {
        // Logic to update the selected item IDs in your ViewModel state
        _attributes.value = _attributes.value?.map { item ->
            if (item.id == attributeId) {
                item.copy(selectedOptionIds = selectedIds)
            } else {
                item
            }
        }
    }

    fun updateOptionPrice(attributeId: Int, optionId: Int, newPrice: Double) {
        // Logic to update the selected price in the specific Option
        _attributes.value = _attributes.value?.map { attribute ->
            if (attribute.id == attributeId) {
                val updatedOptions = attribute.options.map { option ->
                    if (option.id == optionId) {
                        option.copy(selectedPrice = newPrice) // Update the selectedPrice
                    } else {
                        option
                    }
                }
                attribute.copy(options = updatedOptions) // Update the options for this attribute
            } else {
                attribute
            }
        }
    }

    // Method to update selected item IDs based on attribute ID


    init {
        // Initialize the map to hold empty lists for each attribute ID if necessary
        _attributes.value = listOf() // Replace with actual initial data

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
               colorList = res.body()?.data.orEmpty()[0].options as ArrayList<Option>
               massList = res.body()?.data.orEmpty()[1].options as ArrayList<Option>
               capacityList = res.body()?.data.orEmpty()[2].options as ArrayList<Option>
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
        }else if (   addProductRequest.mediaId == null) {
            setValue(Codes.EMPTY_IMAGE)
        }else if (  addProductRequest.skusList == null || addProductRequest.skusList!!.size == 0 ){
            setValue(Codes.EMPTY_SKU)
        }else if (addProductRequest.skusList!!.size > 0  && !validate) {
            for (item in addProductRequest.skusList!!) {
                if (item.name == null || item.name.toString().trim().isEmpty()) {
                    setValue(Codes.EMPTY_SizeName)
                    validate =false
                    break
                } else if (item.inventory_value == null || item.inventory_value.toString().trim().isEmpty()) {
                    setValue(Codes.EMPTY_InventoryValue)
                    validate =false
                    break
                } else if (item.regular_price == null || item.regular_price.toString().trim().isEmpty()) {
                    setValue(Codes.EMPTY_Price)
                    validate =false
                    break
                } else if (item.code == null || item.code.toString().trim().isEmpty()) {
                    setValue(Codes.EMPTY_CODE)
                    validate =false
                    break
                }else{
                    validate =true
                }
            }
        }else {
            createProduct()
        }
    }
    fun createProduct() {

        var gson = Gson()
        addProductRequest.skus  = gson.toJson(addProductRequest.skusList)

        if (addProductRequest.skusList!!.size>1){
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
        requestCall<AddProductResponse?>({
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
        }
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
                    addProductRequest.mediaId!!.add(i,res.data!!.id!!)
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