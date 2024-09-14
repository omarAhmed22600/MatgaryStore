package com.brandsin.store.model.profile.updatestore

import java.io.File

data class UploadRequest(
    var collection: String? = null,
    var image: File? = null
)