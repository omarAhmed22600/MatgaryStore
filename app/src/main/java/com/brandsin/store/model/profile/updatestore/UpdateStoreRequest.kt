package com.brandsin.store.model.profile.updatestore

import java.io.Serializable

data class UpdateStoreRequest(
    var storeId: String? = null,
    var storeName: String? = null,
    var storeLat: String? = null,
    var storeLng: String? = null,
    var storeAddress: String? = null,
    var storeDeliveryPrice: String? = null,
    var storeDeliveryTime: String? = null,
    var storeDeliveryType: String? = null,
    var storeDeliveryDistance: String? = null,
    var storeMinOrderPrice: String? = null,
    var storePhoneNumber: String? = null,
    var storeWhatsApp: String? = null,
    var categories: ArrayList<Int>? = null,
    var tags: ArrayList<Int>? = null,
    var hasDelivery: String? = null,
    var pickUpFromStore: String? = null,
    var cashOnDelivery: String? = null,
    var storeMedia: ArrayList<Int>? = ArrayList(),
    var deleteMedia: ArrayList<Int>? = ArrayList(),

    var storeOwnerName: String? = null,
    var storeBankName: String? = null,
    var storeIban: String? = null,
) : Serializable