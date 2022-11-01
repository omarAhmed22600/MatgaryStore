package com.brandsin.store.ui.main.home.addstory.addstorytxt

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryResponse
import com.brandsin.store.model.profile.addedstories.uploadstory.UploadStoryWithoutRequest
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddStoryTxtViewModel : BaseViewModel()  {

    var request = UploadStoryWithoutRequest()

    fun onAddClicked() {
        setValue(Codes.ADD_STORIES)
    }

    fun uploadStoriesWithout() {
        request.storeId = PrefMethods.getStoreData()!!.id!!.toInt()
        requestCall<UploadStoryResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().uploadStoriesWithout(request)
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