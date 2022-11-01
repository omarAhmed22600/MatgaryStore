package com.brandsin.store.model.menu.connectingmain.accept

import com.google.gson.annotations.SerializedName

data class AcceptConnectingMainRequest (
    @SerializedName("request_id")
    var requestId: Int? = null,

    @SerializedName("accept")
    var accept: Int? = null
 )