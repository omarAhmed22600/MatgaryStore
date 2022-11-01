package com.brandsin.store.ui.main.addofffer

import androidx.databinding.ObservableField
import com.brandsin.store.R
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.offers.add.*
import com.brandsin.store.model.main.offers.listoffer.OfferProductItem
import com.brandsin.store.model.main.offers.listoffer.OffersItemDetails
import com.brandsin.store.model.main.offers.update.UpdateOfferRequest
import com.brandsin.store.model.main.offers.update.UpdateOfferResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddOfferViewModel : BaseViewModel() {

    var offerAddProductRequest = OfferAddProductRequest()

    //Add
    var addOfferRequest = AddOfferRequest()
    var productsList: ArrayList<DataItem> = ArrayList()
    var productsAdapter = AddOfferProductsAdapter()

    //Update
    var isUpdated: Boolean = false
    var updateOfferRequest = UpdateOfferRequest()
    var prevOfferProducts: ArrayList<OfferProductItem> = ArrayList()
    var productsUpdateAdapter = UpdateOfferProductsAdapter()
    var itemOfferId = 0
    var itemStoreId = 0

    var obsBtnText = ObservableField(getString(R.string.add))
    var obsToolBarTitle = ObservableField(getString(R.string.add))


    /*------------- PARAMETERS ------------*/
    private var isImageChanged: Boolean = false
    var obsOfferName = ObservableField<String>()
    var obsOfferNameEn = ObservableField<String>()
    var obsOfferDescription = ObservableField<String>()
    var obsOfferDescriptionEn = ObservableField<String>()
    var obsStartDate = ObservableField<String>()
    var obsEndDate = ObservableField<String>()
    var obsOfferPrice = ObservableField<String>()
    var obsOfferPriceTo = ObservableField<String>()


    fun setOfferData(flag: Int, offerItem: OffersItemDetails? = null) {

        when (flag) {
            2 -> {
                isUpdated = true
                itemOfferId = offerItem!!.id!!
                itemStoreId = offerItem.storeId!!
                obsBtnText.set(getString(R.string.save))
                obsToolBarTitle.set(getString(R.string.update_offer))

                prevOfferProducts = offerItem.offerProductsList as ArrayList<OfferProductItem>
                productsUpdateAdapter.updateList(prevOfferProducts)

                obsOfferName.set(offerItem.name)
                obsOfferNameEn.set(offerItem.nameEn)
                obsOfferDescription.set(offerItem.description)
                obsOfferDescriptionEn.set(offerItem.descriptionEn)
                obsStartDate.set(offerItem.startDate)
                obsEndDate.set(offerItem.endDate)
                obsOfferPrice.set(offerItem.price.toString())
                obsOfferPriceTo.set(offerItem.priceTo.toString())
            }
            else -> {
                isUpdated = false
                obsBtnText.set(getString(R.string.add))
                obsToolBarTitle.set(getString(R.string.add_offer))
            }
        }
    }

    fun onAddClicked() {
        when (isUpdated) {
            true -> {
                when {
                    prevOfferProducts.size == 0 && productsList.size == 0  -> {
                        setValue(Codes.EMPTY_PRODUCTS)
                    }
                    isImageChanged -> {
                        updateOffer()
                    }
                    else -> {
                        updateOfferWithImage()
                    }
                }
            }
            else -> {
                when {
                    addOfferRequest.offerImage == null -> {
                        setValue(Codes.EMPTY_IMAGE)
                    }
                    obsOfferName.get() == null  || obsOfferName.get().toString().trim().isEmpty() -> {
                        setValue(Codes.EMPTY_OFFER_NAME)
                    }
//                    obsOfferNameEn.get() == null  || obsOfferNameEn.get().toString().trim().isEmpty() -> {
//                        setValue(Codes.EMPTY_OFFER_NAME_EN)
//                    }
                    obsOfferDescription.get() == null  || obsOfferDescription.get().toString().trim().isEmpty() -> {
                        setValue(Codes.EMPTY_OFFER_DESCRIPTION)
                    }
//                    obsOfferDescriptionEn.get() == null  || obsOfferDescriptionEn.get().toString().trim().isEmpty() -> {
//                        setValue(Codes.EMPTY_OFFER_DESCRIPTION_EN)
//                    }
                    obsOfferPrice.get() == null || obsOfferPrice.get().toString().trim().isEmpty() -> {
                        setValue(Codes.EMPTY_OFFER_PRICE)
                    }
                    obsStartDate.get() == null || obsStartDate.get().toString().trim().isEmpty() -> {
                        setValue(Codes.SELECT_START_DATE)
                    }
                    obsEndDate.get() == null || obsEndDate.get().toString().trim().isEmpty() -> {
                        setValue(Codes.SELECT_END_DATE)
                    }
                    !checkValidationDates() -> {

                    }
                    productsList.size == 0 -> {
                        setValue(Codes.EMPTY_PRODUCTS)
                    }
                    else -> {
                        createNewOffer()
                    }
                }
            }
        }
    }

    private fun createNewOffer() {
        addOfferRequest.locale = PrefMethods.getLanguage()
        addOfferRequest.storeId = PrefMethods.getStoreData()!!.id!!.toInt()
        addOfferRequest.name = obsOfferName.get()
        addOfferRequest.description = obsOfferDescription.get()
        addOfferRequest.nameEn = obsOfferNameEn.get()
        addOfferRequest.descriptionEn = obsOfferDescriptionEn.get()
        addOfferRequest.price = obsOfferPrice.get()
        if (obsOfferPriceTo.get() == null){
            obsOfferPriceTo.set("0")
        }
        addOfferRequest.priceTo = obsOfferPriceTo.get()
        addOfferRequest.startDate = obsStartDate.get()
        addOfferRequest.endDate = obsEndDate.get()
        addOfferRequest.active = 1

        if ( addOfferRequest.nameEn  == null  || addOfferRequest.nameEn.toString().trim().isEmpty() ) {
            addOfferRequest.nameEn = ""
        }
        if ( addOfferRequest.descriptionEn == null  || addOfferRequest.descriptionEn.toString().trim().isEmpty() ) {
            addOfferRequest.descriptionEn = ""
        }
        obsIsVisible.set(true)
        requestCall<CreateOfferResponse?>({
            withContext(Dispatchers.IO) { return@withContext getApiRepo().createNewOffer(addOfferRequest) }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.isSuccess) {
                true -> {
                    apiResponseLiveData.value = ApiResponse.success(res)
                }
                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message.toString())
                }
            }
        }
    }
    private fun updateOfferWithImage() {
        updateOfferRequest.locale = PrefMethods.getLanguage()
        updateOfferRequest.offerId = itemOfferId
        updateOfferRequest.storeId = itemStoreId
        updateOfferRequest.name = obsOfferName.get()
        updateOfferRequest.description = obsOfferDescription.get()
        updateOfferRequest.nameEn = obsOfferNameEn.get()
        updateOfferRequest.descriptionEn = obsOfferDescriptionEn.get()
        updateOfferRequest.price = obsOfferPrice.get()
        updateOfferRequest.priceTo = obsOfferPriceTo.get()

        val d: Date?
        val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        d = date1.parse(obsStartDate.get())
        updateOfferRequest.startDate = date1.format(d)

        val d2: Date?
        val date2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        d2 = date2.parse(obsEndDate.get())
        updateOfferRequest.endDate = date1.format(d2)

        updateOfferRequest.active = 1

        if (prevOfferProducts.size>0) {
            prevOfferProducts.forEach {
                if (updateOfferRequest.productsIds == null) {
                    updateOfferRequest.productsIds = ArrayList<Int>()
                }
                updateOfferRequest.productsIds!!.add(it.id!!)
            }
        }else{
            productsList.forEach {
                if (updateOfferRequest.productsIds == null) {
                    updateOfferRequest.productsIds = ArrayList<Int>()
                }
                updateOfferRequest.productsIds!!.add(it.id!!)
            }
        }

        if ( updateOfferRequest.nameEn  == null  || updateOfferRequest.nameEn.toString().trim().isEmpty() ) {
            updateOfferRequest.nameEn = ""
        }
        if ( updateOfferRequest.descriptionEn == null  || updateOfferRequest.descriptionEn.toString().trim().isEmpty() ) {
            updateOfferRequest.descriptionEn = ""
        }
        obsIsVisible.set(true)
        requestCall<UpdateOfferResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().updateOfferWithoutImage(updateOfferRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.isSuccess) {
                true -> {
                    apiResponseLiveData.value = ApiResponse.success(res)
                }
                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message.toString())
                }
            }
        }
    }
    private fun updateOffer() {
        updateOfferRequest.locale = PrefMethods.getLanguage()
        updateOfferRequest.offerId = itemOfferId
        updateOfferRequest.storeId = itemStoreId
        updateOfferRequest.name = obsOfferName.get()
        updateOfferRequest.description = obsOfferDescription.get()
        updateOfferRequest.nameEn = obsOfferNameEn.get()
        updateOfferRequest.descriptionEn = obsOfferDescriptionEn.get()
        updateOfferRequest.price = obsOfferPrice.get()
        updateOfferRequest.priceTo = obsOfferPriceTo.get()

//        if (updateOfferRequest.offerImage == null) {
//            val uri = Uri.parse(itemOfferImage)
//            updateOfferRequest.offerImage = File(uri.path)
//        }

        val d: Date?
        val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        d = date1.parse(obsStartDate.get())
        updateOfferRequest.startDate = date1.format(d)

        val d2: Date?
        val date2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        d2 = date2.parse(obsEndDate.get())
        updateOfferRequest.endDate = date1.format(d2)

        updateOfferRequest.active = 1

        if (prevOfferProducts.size>0) {
            prevOfferProducts.forEach {
                if (updateOfferRequest.productsIds == null) {
                    updateOfferRequest.productsIds = ArrayList<Int>()
                }
                updateOfferRequest.productsIds!!.add(it.id!!)
            }
        }else{
            productsList.forEach {
                if (updateOfferRequest.productsIds == null) {
                    updateOfferRequest.productsIds = ArrayList<Int>()
                }
                updateOfferRequest.productsIds!!.add(it.id!!)
            }
        }

        obsIsVisible.set(true)
        requestCall<UpdateOfferResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().updateOffer(updateOfferRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.isSuccess) {
                true -> {
                    apiResponseLiveData.value = ApiResponse.success(res)
                }
                else -> {
                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message.toString())
                }
            }
        }
    }

    fun getStoreProducts() {
        offerAddProductRequest.store_id = PrefMethods.getStoreData()!!.id!!.toInt()
        offerAddProductRequest.autocomplete = 1
        offerAddProductRequest.lang = PrefMethods.getLanguage()
        obsIsVisible.set(true)
        requestCall<OfferAddProductResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().setOfferAddProduct(offerAddProductRequest)
            }
        })
        { res ->
            obsIsVisible.set(false)
            when (res!!.success) {
                true -> {
                    apiResponseLiveData.value = ApiResponse.success(res)
                }
                else -> {
//                    apiResponseLiveData.value = ApiResponse.errorMessage(res.message.toString())
                }
            }
        }
    }

    @Throws(ParseException::class)
    fun checkValidationDates(): Boolean {
        var result = false
        val sdformat = SimpleDateFormat("yyyy-MM-dd")
        val d1 = sdformat.parse(obsStartDate.get())
        val d2 = sdformat.parse(obsEndDate.get())
        if (d1 > d2) {
            setValue(Codes.START_AFTER_END)
            result = false
        } else if (d1 < d2) {
            setValue(Codes.VALIDATION_SUCCESS)
            result = true
        } else if (d1 == d2) {
            setValue(Codes.START_EQUAL_END)
            result = false
        }
        return result
    }

    fun onImageClicked() {
        setValue(Codes.SELECT_IMAGES)
    }

    fun onChangeImageClicked() {
        if (isUpdated) {
            setValue(Codes.CHANGE_IMAGES)
        }else{
            setValue(Codes.SELECT_IMAGES)
        }
    }

    fun onStartDateClicked() {
        setValue(Codes.SHOW_START_DATE)
    }

    fun onEndDateClicked() {
        when {
            obsStartDate.get() == null -> {
                setValue(Codes.SELECT_START_DATE)
            }
            else -> {
                setValue(Codes.SHOW_END_DATE)
            }
        }
    }
}