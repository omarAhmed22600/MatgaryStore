package com.brandsin.store.ui.main.addproduct

import android.graphics.Bitmap

data class PhotoModel(
    val isPhotoOrVideo: String, // "photo", "video", "none"
    val photoOrVideoBitmap: Bitmap?
) {
    val isPhoto: Boolean get() = isPhotoOrVideo == "photo"
    val isVideo: Boolean get() = isPhotoOrVideo == "video"
    val isNone: Boolean get() = isPhotoOrVideo == "none"
}