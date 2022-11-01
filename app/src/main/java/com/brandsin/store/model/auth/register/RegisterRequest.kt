package com.brandsin.store.model.auth.register

import java.io.File
import java.io.Serializable

data class RegisterRequest(
        var storeName: String? = null,
        var storeLat: String? = null,
        var storeLng: String? = null,
        var storeAddress: String? = null,
        var storeDeliveryPrice: String? = null,
        var storeDeliveryTime: String? = null,
        var storeDeliveryDistance: String? = null,
        var storeMinOrderPrice: String? = null,
        var storePhoneNumber: String? = null,
        var storeWhatsApp: String? = null,
        var userName: String? = null,
        var userLastName: String? = null,
        var userPhone: String? = null,
        var userEmail: String? = null,
        var userPassword: String? = null,
        var categories: ArrayList<Int>? = null,
        var tags: ArrayList<Int>? = null,
        var hasDelivery: String? = null,
        var checkDelivery: Boolean? = true,
        var checkCondition: Boolean? = false,
        var storeThumb: File? = null,
        var storeImg: File? = null,
        var userIdImg: File? = null,
        var storeThumbUri: String? = null,
        var storeImgUri: String? = null,
        var userIdImgUri: String? = null,
) : Serializable

data class StoreRegister(
        var storeName: String? = null,
        var storeLat: String? = null,
        var storeLng: String? = null,
        var storeAddress: String? = null,
        var storePhoneNumber: String? = null,
        var storeWhatsApp: String? = null,
        var storeDeliveryPrice: String? = null,
        var storeDeliveryTime: String? = null,
        var storeDeliveryDistance: String? = null,
        var storeMinOrderPrice: String? = null,
        var categories: ArrayList<Int>? = null,
        var tags: ArrayList<Int>? = null,
        var hasDelivery: String? = null,
        var storeThumb: File? = null,
        var storeImg: File? = null,
        var storeThumbUri: String? = null,
        var storeImgUri: String? = null,
        var checkDelivery: Boolean? = true,
        var checkCondition: Boolean? = false
)  : Serializable

data class UserRegister(
        var userName: String? = null,
        var userLastName: String? = null,
        var userPhone: String? = null,
        var userEmail: String? = null,
        var userIdImg: File? = null,
        var userIdImgUri: String? = null,
)  : Serializable
