package com.brandsin.store.ui.main.home.addstory

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryRequest
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryResponse
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddStoryViewModel : BaseViewModel() {
    var galleryAdapter = GalleryAdapter()
    var galleryList = ArrayList<String>()
    var request = UploadStoryRequest()

    fun onTextClicked() {
        setValue(Codes.SELECT_TEXT)
    }
    fun onImageClicked() {
        setValue(Codes.SELECT_IMAGES)
    }

    fun uploadStories() {
        request.storeId = PrefMethods.getStoreData()!!.id!!.toInt()
        request.text = ""
        requestCall<UploadStoryResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().uploadStories(request)
            }
        })
        { res ->
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
}