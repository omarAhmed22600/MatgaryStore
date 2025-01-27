package com.brandsin.store.ui.profile.addedstories.showstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bolaware.viewstimerstory.MomentzView
import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.Story
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.addedstories.deletestory.DeleteStoryRequest
import com.brandsin.store.model.profile.addedstories.liststories.ListStoriesResponse
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItemByDate
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.network.toSingleEvent
import com.brandsin.store.ui.profile.addedstories.AddedStoriesAdapter
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class ShowStoryViewModel : BaseViewModel() {

    var storiesItemByDates: ArrayList<com.brandsin.store.model.ListStoriesResponse> = ArrayList()
    var storiesList: MutableList<ArrayList<Story>> = ArrayList()
    var addedStoriesAdapter = AddedStoriesAdapter()
    var request = DeleteStoryRequest()
    private val _getListStoriesResponse: MutableLiveData<ResponseHandler<List<com.brandsin.store.model.ListStoriesResponse>?>> =
        MutableLiveData()
    val getListStoriesResponse: LiveData<ResponseHandler<List<com.brandsin.store.model.ListStoriesResponse>?>> =
        _getListStoriesResponse.toSingleEvent()
    // var myStory = MyStory()
    // var myStoriesList: ArrayList<MyStory> = ArrayList()

    val selectedStory = MutableLiveData<Int>(-1)
    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    val listOfViews: ArrayList<MomentzView> = ArrayList()

    fun getListStories() {
        obsIsEmpty.set(false)
        obsIsFull.set(false)
        val baeRepo = BaseRepository()
        viewModelScope.launch {
            safeApiCallOmar {
                // Make your API call here using Retrofit service or similar
                baeRepo.apiInterface.getNewListStories(PrefMethods.getStoreData()!!.id!!.toInt())
            }.collect {
                _getListStoriesResponse.value = it
            }
        }
/*        val responseCall: Call<ListStoriesResponse?> = baeRepo.apiInterface
            .getListStories(PrefMethods.getStoreData()!!.id!!.toInt())
        responseCall.enqueue(object : Callback<ListStoriesResponse?> {
            override fun onResponse(
                call: Call<ListStoriesResponse?>,
                response: Response<ListStoriesResponse?>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.success == true) {
                        if (response.body()?.storiesItemByDate?.isNotEmpty() == true) {
                            storiesItemByDates =
                                response.body()?.storiesItemByDate as ArrayList<StoriesItemByDate>
                            addedStoriesAdapter.updateList(storiesItemByDates)
                            setShowProgress(false)
                            obsIsEmpty.set(false)
                            obsIsFull.set(true)

                            for (item in storiesItemByDates) {
                                for (xItem in item.stories!!) {
                                    // myStory = MyStory()
                                    if (xItem!!.mediaUrl.isNullOrEmpty()) {
                                        // myStory.url = ""
                                    } else {
                                        // myStory.url = xItem.mediaUrl
                                    }
                                    // myStory.date = simpleDateFormat.parse(xItem.createdAt.toString())
                                    // myStory.description = xItem.text
                                    // myStoriesList.add(myStory)
                                }
                            }
                            setValue(Codes.SHOW_STORY)
                        } else {
                            setShowProgress(false)
                            obsIsEmpty.set(true)
                            obsIsFull.set(false)
                        }
                    }
                } else {
                    setValue(response.message())
                }
            }

            override fun onFailure(call: Call<ListStoriesResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })*/
    }
}