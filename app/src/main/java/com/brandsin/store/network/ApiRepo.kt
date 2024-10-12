package com.brandsin.store.network

import com.brandsin.store.database.ApiInterface
import com.brandsin.store.model.auth.register.RegisterRequest
import com.brandsin.store.model.main.offers.add.AddOfferRequest
import com.brandsin.store.model.main.offers.add.OfferAddProductRequest
import com.brandsin.store.model.main.offers.delete.DeleteOfferRequest
import com.brandsin.store.model.main.offers.update.UpdateOfferRequest
import com.brandsin.store.model.main.order.updatestatus.UpdateStatusOrderRequest
import com.brandsin.store.model.main.products.add.AddProductRequest
import com.brandsin.store.model.main.products.delete.DeleteProductRequest
import com.brandsin.store.model.main.products.update.UpdateProductRequest
import com.brandsin.store.model.menu.notifications.ReadNotificationRequest
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryRequest
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryWithoutRequest
import com.brandsin.store.model.profile.updatestore.UpdateStoreRequest
import com.brandsin.store.model.profile.updatestore.UploadRequest
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.toMultiPart
import com.brandsin.store.utils.toMultiPartV


class ApiRepo(private val apiInterface: ApiInterface) {

    suspend fun createNewStore(request: RegisterRequest) = apiInterface.register(
        request.storeName!!.toRequestBodyParam(),
        request.storeLat!!.toRequestBodyParam(),
        request.storeLng!!.toRequestBodyParam(),
        request.storeAddress!!.toRequestBodyParam(),
        request.storeDeliveryPrice?.toRequestBodyParam(),
        request.storeDeliveryTime?.toRequestBodyParam(),
        request.storeDeliveryType?.toRequestBodyParam(),

        request.storeDeliveryDistance?.toRequestBodyParam(),
        request.storeMinOrderPrice!!.toRequestBodyParam(),
        request.storePhoneNumber!!.toRequestBodyParam(),
        request.storeWhatsApp!!.toRequestBodyParam(),

        request.userName!!.toRequestBodyParam(),
        request.userLastName!!.toRequestBodyParam(),
        request.userPhone!!.toRequestBodyParam(),
        request.userEmail!!.toRequestBodyParam(),
        request.userPassword!!.toRequestBodyParam(),

        request.categories!!,
        request.tags!!,
        request.hasDelivery!!.toRequestBodyParam(),
        request.pickUpFromStore!!.toRequestBodyParam(),
        request.cashOnDelivery!!.toRequestBodyParam(),

        request.storeOwnerName!!.toRequestBodyParam(),
        request.storeBankName!!.toRequestBodyParam(),
        request.storeIban!!.toRequestBodyParam(),

        request.storeThumb?.toMultiPart("store[thumbnail]")!!,
        request.storeImg?.toMultiPart("store[commercial_register]")!!,
        request.userIdImg?.toMultiPart("user[national_id_image]")!!,
    )

    suspend fun createNewStore2(request: RegisterRequest) = apiInterface.register2(
        request.storeName!!.toRequestBodyParam(),
        request.storeLat!!.toRequestBodyParam(),
        request.storeLng!!.toRequestBodyParam(),
        request.storeAddress!!.toRequestBodyParam(),
        request.storeDeliveryPrice!!.toRequestBodyParam(),
        request.storeDeliveryTime!!.toRequestBodyParam(),
        request.storeDeliveryDistance!!.toRequestBodyParam(),
        request.storeMinOrderPrice!!.toRequestBodyParam(),
        request.storePhoneNumber!!.toRequestBodyParam(),
        request.storeWhatsApp!!.toRequestBodyParam(),
        request.userName!!.toRequestBodyParam(),
        request.userLastName!!.toRequestBodyParam(),
        request.userPhone!!.toRequestBodyParam(),
        request.userEmail!!.toRequestBodyParam(),
        request.userPassword!!.toRequestBodyParam(),
        request.categories!!,
        request.tags!!,
        request.hasDelivery!!.toRequestBodyParam(),
        request.storeImg?.toMultiPart("store[commercial_register]")!!,
        request.userIdImg?.toMultiPart("user[national_id_image]")!!,
    )


    suspend fun getStoreOrders(lang: String, userId: Int, limit: Int?, status: String, page: Int?) =
        apiInterface.getStoreOrders(lang, userId, limit, status, page)
    suspend fun sendNotification(message: String, userId: Int, currentUserId: Int) =
        apiInterface.sendNotification(message,userId,"chat_id",currentUserId)
    suspend fun getNotifications(limit: Int, page: Int, userId: Int,storeId: Int) =
        apiInterface.getNotifications(limit, page, userId, storeId)

    suspend fun readNotification(request: ReadNotificationRequest) =
        apiInterface.readNotification(request)

    suspend fun getCommonQues(type: String, lang: String) = apiInterface.getCommonQues(type, lang)

    suspend fun getHelpQues(type: String, lang: String) = apiInterface.getHelpQues(type, lang)

    suspend fun getPhoneNumber(type: String, lang: String) = apiInterface.getPhoneNumber(type, lang)

    suspend fun getConditions(type: String, lang: String) = apiInterface.getConditions(type, lang)

    suspend fun getSocialLinks(type: String, lang: String) = apiInterface.getSocialLinks(type, lang)

    suspend fun getOrderDetails(orderId: Int, lang: String) =
        apiInterface.getOrderDetails(orderId, lang)

    suspend fun setUpdateStatusOrder(request: UpdateStatusOrderRequest) =
        apiInterface.setUpdateStatusOrder(request)

    /* Offers */
    suspend fun getOffers(storeId: Int, limit: Int, page: Int, locale: String) =
        apiInterface.getOffers(storeId, limit, page, locale)

    suspend fun createNewOffer(request: AddOfferRequest) = apiInterface.createOffer(
        request.storeId!!.toRequestBodyParam(),
        request.name!!.toRequestBodyParam(),
        request.description!!.toRequestBodyParam(),
        request.nameEn!!.toRequestBodyParam(),
        request.descriptionEn!!.toRequestBodyParam(),
        request.price!!.toRequestBodyParam(),
        request.priceTo!!.toRequestBodyParam(),
        request.startDate!!.toRequestBodyParam(),
        request.endDate!!.toRequestBodyParam(),
        request.active!!.toRequestBodyParam(),
        request.type!!.toRequestBodyParam(),
        request.productsIds,
        request.offerImage?.toMultiPart("image"),
        request.offerVideo?.toMultiPart("video"),
        request.locale!!.toRequestBodyParam()
    )

    suspend fun deleteOffer(request: DeleteOfferRequest) = apiInterface.deleteOffer(request)

    suspend fun updateOfferWithoutImage(request: UpdateOfferRequest) =
        apiInterface.updateOfferWithoutImage(request)

    suspend fun updateOffer(request: UpdateOfferRequest,isImage:Boolean) = apiInterface.updateOffer(
        request.offerId!!.toRequestBodyParam(),
        request.storeId!!.toRequestBodyParam(),
        request.name!!.toRequestBodyParam(),
        request.description!!.toRequestBodyParam(),
        request.nameEn!!.toRequestBodyParam(),
        request.descriptionEn!!.toRequestBodyParam(),
        request.price!!.toRequestBodyParam(),
        request.priceTo!!.toRequestBodyParam(),
        request.startDate!!.toRequestBodyParam(),
        request.endDate!!.toRequestBodyParam(),
        request.active!!.toRequestBodyParam(),
        request.type!!.toRequestBodyParam(),
        request.productsIds,
        request.offerImage?.toMultiPart("image"),
        request.offerVideo?.toMultiPart("video"),
        request.locale!!.toRequestBodyParam()
    )

    /* PRODUCTS */

    suspend fun getStoreProducts(storeId: Int, locale: String, page: Int, limit: Int) =
        apiInterface.getStoreProducts(storeId, locale, page, limit)

    suspend fun createProduct(request: AddProductRequest) = apiInterface.createProduct(
        request.name!!.toRequestBodyParam(),
        request.description!!.toRequestBodyParam(),
        request.nameEn!!.toRequestBodyParam(),
        request.descriptionEn!!.toRequestBodyParam(),
        request.type!!.toRequestBodyParam(),
        request.status.toRequestBodyParam(),
        request.productStatus!!.toRequestBodyParam(),
        request.storeId!!.toRequestBodyParam(),
        request.skus!!.toRequestBodyParam(),
        request.categoriesIds!!,
        request.device.toRequestBodyParam(),
        request.mediaId!!,
        request.deleteMediaId!!,
        request.locale!!.toRequestBodyParam()
    )

    suspend fun deleteProduct(request: DeleteProductRequest) = apiInterface.deleteProduct(request)

    suspend fun changeProductStatus(productId:Int) = apiInterface.changeProductStatus(productId)


    suspend fun updateProduct(request: UpdateProductRequest) = apiInterface.updateProduct(
        request.id!!.toRequestBodyParam(),
        request.name!!.toRequestBodyParam(),
        request.description!!.toRequestBodyParam(),
        request.nameEn!!.toRequestBodyParam(),
        request.descriptionEn!!.toRequestBodyParam(),
        request.type!!.toRequestBodyParam(),
        request.status.toRequestBodyParam(),
        request.productStatus!!.toRequestBodyParam(),
        request.storeId!!.toRequestBodyParam(),
        request.skus!!.toRequestBodyParam(),
        request.categoriesIds!!,
        request.device.toRequestBodyParam(),
        request.mediaId!!,
        request.deleteMediaId!!,
        request.locale!!.toRequestBodyParam()
    )

    suspend fun getProductCategories(parentsOnly: Int, lang: String, storeId: Int) =
        apiInterface.getProductCategories(parentsOnly, lang, storeId)


    suspend fun getProductAttrs(storeId: Int) =
        apiInterface.getProductAttrs(storeId)

    suspend fun getStoreTypes(lang: String) = apiInterface.getStoreTypes(lang)

    suspend fun updateStoreInfo(request: UpdateStoreRequest) = apiInterface.updateStoreInfo(
        request.storeId!!.toRequestBodyParam(),
        request.storeName!!.toRequestBodyParam(),
        request.storeLat!!.toRequestBodyParam(),
        request.storeLng!!.toRequestBodyParam(),
        request.storeAddress!!.toRequestBodyParam(),
        request.storeDeliveryPrice!!.toRequestBodyParam(),
        request.storeDeliveryTime!!.toRequestBodyParam(),
        request.storeDeliveryDistance!!.toRequestBodyParam(),
        request.storeDeliveryType!!.toRequestBodyParam(),
        request.storeMinOrderPrice!!.toRequestBodyParam(),
        request.storePhoneNumber!!.toRequestBodyParam(),
        request.storeWhatsApp!!.toRequestBodyParam(),
        request.categories!!,
        request.tags!!,
        request.pickUpFromStore!!.toRequestBodyParam(),
        request.cashOnDelivery!!.toRequestBodyParam(),
        request.hasDelivery!!.toRequestBodyParam(),

        request.storeOwnerName!!.toRequestBodyParam(),
        request.storeBankName!!.toRequestBodyParam(),
        request.storeIban!!.toRequestBodyParam(),

        request.storeMedia!!,
        request.deleteMedia!!
    )

    suspend fun updateBankAccountInfo(request: UpdateStoreRequest) =
        apiInterface.updateBankingAccountInfo(
            PrefMethods.getStoreData()!!.id!!.toRequestBodyParam(),
            PrefMethods.getStoreData()!!.name!!.toRequestBodyParam(),
            PrefMethods.getStoreData()!!.lat!!.toRequestBodyParam(),
            PrefMethods.getStoreData()!!.lng!!.toRequestBodyParam(),
            PrefMethods.getStoreData()!!.address!!.toRequestBodyParam(),
            PrefMethods.getStoreData()!!.deliveryPrice!!.toRequestBodyParam(),
            PrefMethods.getStoreData()!!.deliveryTime!!.toRequestBodyParam(),
            PrefMethods.getStoreData()!!.deliveryDistance!!.toRequestBodyParam(),
            PrefMethods.getStoreData()!!.minOrderPrice!!.toRequestBodyParam(),
            PrefMethods.getStoreData()!!.phoneNumber!!.toRequestBodyParam(),
            PrefMethods.getStoreData()!!.whatsapp!!.toRequestBodyParam(),

            request.storeOwnerName!!.toRequestBodyParam(),
            request.storeBankName!!.toRequestBodyParam(),
            request.storeIban!!.toRequestBodyParam(),
        )

    suspend fun setOfferAddProduct(request: OfferAddProductRequest) =
        apiInterface.setOfferAddProduct(request)

    suspend fun getReportsTotal(
        store_id: Int,
        limit: Int,
        page: Int,
        type: String,
        from: String,
        to: String
    ) = apiInterface.getReportsTotal(store_id, limit, page, type, from, to)

    suspend fun getReportsDetails(
        store_id: Int,
        limit: Int,
        page: Int,
        type: String,
        from: String,
        to: String
    ) = apiInterface.getReportsDetails(store_id, limit, page, type, from, to)

    suspend fun getUnitsList(categories: ArrayList<Int>, locale: String) =
        apiInterface.getUnitsList(categories, locale)

    suspend fun upload(request: UploadRequest) = apiInterface.upload(
        request.collection!!.toRequestBodyParam(),
        request.image?.toMultiPart("file")!!,
    )

    suspend fun uploadStories(request: UploadStoryRequest) =
        apiInterface.uploadStories(
            request.storeId!!.toRequestBodyParam(),
            request.text?.toRequestBodyParam(),
            request.x?.toRequestBodyParam(),
            request.y?.toRequestBodyParam(),
            request.productId?.toRequestBodyParam(),
            request.file?.toMultiPartV("file")
        )

    suspend fun uploadStoriesWithout(request: UploadStoryWithoutRequest) =
        apiInterface.uploadStoriesWithout(request)
}