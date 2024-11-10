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
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddOfferViewModel : BaseViewModel() {

    var offerAddProductRequest = OfferAddProductRequest()
    var isImage = false
    // Add
    var addOfferRequest = AddOfferRequest()
    var productsList: ArrayList<DataItem> = ArrayList()
    var productsAdapter = AddOfferProductsAdapter()

    // Update
    private var isUpdated: Boolean = false
    var updateOfferRequest = UpdateOfferRequest()
    var prevOfferProducts: ArrayList<OfferProductItem> = ArrayList()
    var productsUpdateAdapter = UpdateOfferProductsAdapter()

    private var itemOfferId = 0
    private var itemStoreId = 0

    var obsBtnText = ObservableField(getString(R.string.add))
    var obsToolBarTitle = ObservableField(getString(R.string.add))


    /*------------- PARAMETERS ------------*/
    var isImageChanged: Boolean = false
    var obsOfferName = ObservableField<String>()
    var obsOfferNameEn = ObservableField<String>()
    var obsOfferDescription = ObservableField<String>()
    var obsOfferDescriptionEn = ObservableField<String>()
    var obsStartDate = ObservableField<String>()
    var obsEndDate = ObservableField<String>()
    var obsOfferPrice = ObservableField<String>()
    var obsOfferPriceTo = ObservableField<String>()

    var offerType: String? = null

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
                if (offerType == "product") {
                    Timber.e("$isImageChanged")
                    when {
                        prevOfferProducts.size == 0 && productsList.size == 0 -> {
                            setValue(Codes.EMPTY_PRODUCTS)
                        }

                        isImageChanged -> {
                            updateOfferWithImage()
                        }

                        else -> {
                            updateOffer()

                        }
                    }
                } else {
                    when {
                        isImageChanged -> {
                            updateOfferWithImage()

                        }

                        else -> {
                            updateOffer()
                        }
                    }
                }
            }

            else -> {
                when {
                    addOfferRequest.offerImage == null && addOfferRequest.offerVideo == null -> {
                        setValue(Codes.EMPTY_IMAGE)
                    }

                    obsOfferName.get() == null || obsOfferName.get().toString().trim()
                        .isEmpty() -> {
                        setValue(Codes.EMPTY_OFFER_NAME)
                    }

                    obsOfferNameEn.get() == null || obsOfferNameEn.get().toString().trim()
                        .isEmpty() -> {
                        setValue(Codes.EMPTY_OFFER_NAME_EN)
                    }

                    obsOfferDescription.get() == null || obsOfferDescription.get().toString().trim()
                        .isEmpty() -> {
                        setValue(Codes.EMPTY_OFFER_DESCRIPTION)
                    }

                    obsOfferDescriptionEn.get() == null || obsOfferDescriptionEn.get().toString()
                        .trim().isEmpty() -> {
                        setValue(Codes.EMPTY_OFFER_DESCRIPTION_EN)
                    }

                    obsOfferPrice.get() == null || obsOfferPrice.get().toString().trim()
                        .isEmpty() -> {
                        setValue(Codes.EMPTY_OFFER_PRICE)
                    }

                    obsOfferPrice.get().toString().trim().isNotEmpty()
                            && obsOfferPriceTo.get().toString().trim().isNotEmpty()
                            && (obsOfferPrice.get()!!.toDouble() <= obsOfferPriceTo.get()!!
                        .toDouble()) -> {
                        setValue(Codes.OFFER_PRICE_LESS_OFFER_PRICE_TO)
                    }

                    obsStartDate.get() == null || obsStartDate.get().toString().trim()
                        .isEmpty() -> {
                        setValue(Codes.SELECT_START_DATE)
                    }

                    obsEndDate.get() == null || obsEndDate.get().toString().trim().isEmpty() -> {
                        setValue(Codes.SELECT_END_DATE)
                    }

                    !checkValidationDates() -> {

                    }

                    offerType == "product" && productsList.size == 0 -> {
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
        if (obsOfferPriceTo.get() == null) {
            obsOfferPriceTo.set("0")
        }
        addOfferRequest.priceTo = obsOfferPriceTo.get()
        addOfferRequest.startDate = obsStartDate.get()
        addOfferRequest.endDate = obsEndDate.get()
        addOfferRequest.active = 1

        addOfferRequest.type = offerType ?: "product"

        /*if (addOfferRequest.nameEn == null || addOfferRequest.nameEn.toString().trim().isEmpty()) {
            addOfferRequest.nameEn = ""
        }
        if (addOfferRequest.descriptionEn == null || addOfferRequest.descriptionEn.toString().trim()
                .isEmpty()
        ) {
            addOfferRequest.descriptionEn = ""
        }*/

        obsIsVisible.set(true)
        requestCall<CreateOfferResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().createNewOffer(addOfferRequest)
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
        Timber.e("${obsStartDate.get()}")
        val date1 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH) // HH:mm:ss
        d = date1.parse(obsStartDate.get().toString())
        println("date22 ==  ${obsStartDate.get().toString()}")
        println("date22 ==  $d")
        updateOfferRequest.startDate = obsStartDate.get()+" 11:00:00" // date1.format(d.toString())

        val d2: Date?
        val date2 = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH) // HH:mm:ss
        d2 = date2.parse(obsEndDate.get().toString())
        updateOfferRequest.endDate = obsEndDate.get()+" 11:00:00" // date1.format(d2.toString())

        updateOfferRequest.active = 1
        updateOfferRequest.type = offerType

        if (offerType == "product") {
            if (prevOfferProducts.size > 0) {
                prevOfferProducts.forEach {
                    if (updateOfferRequest.productsIds == null) {
                        updateOfferRequest.productsIds = ArrayList<Int>()
                    }
                    updateOfferRequest.productsIds!!.add(it.id!!)
                }
            } else {
                productsList.forEach {
                    if (updateOfferRequest.productsIds == null) {
                        updateOfferRequest.productsIds = ArrayList<Int>()
                    }
                    updateOfferRequest.productsIds!!.add(it.id!!)
                }
            }
        } else {
            updateOfferRequest.productsIds = ArrayList()
        }

        if (updateOfferRequest.nameEn == null || updateOfferRequest.nameEn.toString()
                .trim().isEmpty()
        ) {
            setValue(Codes.EMPTY_OFFER_NAME_EN)
        }
        if (updateOfferRequest.descriptionEn == null || updateOfferRequest.descriptionEn.toString()
                .trim().isEmpty()
        ) {
            setValue(Codes.EMPTY_OFFER_DESCRIPTION_EN)
        }

        println("updateOfferRequest4444 == $updateOfferRequest")

        obsIsVisible.set(true)
        requestCall<UpdateOfferResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().updateOffer(updateOfferRequest,isImage)
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
        val date1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH) // HH:mm:ss
//        d = date1.parse(obsStartDate.get().toString())
        updateOfferRequest.startDate = obsStartDate.get().toString()+" 11:00:00" // date1.format(d.toString())

        val d2: Date?
        val date2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH) // HH:mm:ss
//        d2 = date2.parse(obsEndDate.get().toString())
        updateOfferRequest.endDate = obsEndDate.get().toString()+" 11:00:00" // date1.format(d2.toString())

        updateOfferRequest.active = 1

        if (offerType == "product") {
            if (prevOfferProducts.size > 0) {
                prevOfferProducts.forEach {
                    if (updateOfferRequest.productsIds == null) {
                        updateOfferRequest.productsIds = ArrayList<Int>()
                    }
                    updateOfferRequest.productsIds!!.add(it.id!!)
                }
            } else {
                productsList.forEach {
                    if (updateOfferRequest.productsIds == null) {
                        updateOfferRequest.productsIds = ArrayList<Int>()
                    }
                    updateOfferRequest.productsIds!!.add(it.id!!)
                }
            }
        } else {
            updateOfferRequest.productsIds = ArrayList()
        }

        println("updateOfferRequest == $updateOfferRequest")

        obsIsVisible.set(true)
        requestCall<UpdateOfferResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().updateOffer(updateOfferRequest,isImage)
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
        val sdFormat = SimpleDateFormat("yyyy-MM-dd")
        val d1 = sdFormat.parse(obsStartDate.get().toString())
        val d2 = sdFormat.parse(obsEndDate.get().toString())
        if (d1 != null) {
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
        }
        return result
    }

    fun onImageClicked() {
        setValue(Codes.SELECT_IMAGES)
    }

    fun onChangeImageClicked() {
        if (isUpdated) {
            setValue(Codes.CHANGE_IMAGES)
        } else {
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