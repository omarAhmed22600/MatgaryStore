package com.brandsin.store.ui.profile.addedstories.showstory

import com.bolaware.viewstimerstory.MomentzView
import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.addedstories.deletestory.DeleteStoryRequest
import com.brandsin.store.model.profile.addedstories.liststories.DataItem
import com.brandsin.store.model.profile.addedstories.liststories.ListStoriesResponse
import com.brandsin.store.ui.profile.addedstories.AddedStoriesAdapter
import com.brandsin.store.utils.PrefMethods
import omari.hamza.storyview.model.MyStory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.ArrayList

class ShowStoryViewModel : BaseViewModel() {

    var storiesList: ArrayList<DataItem> = ArrayList()
    var addedStoriesAdapter = AddedStoriesAdapter()
    var request = DeleteStoryRequest()

    var myStory = MyStory()
    var myStoriesList: ArrayList<MyStory> = ArrayList()
    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    val listOfViews : ArrayList<MomentzView> = ArrayList()


    fun getListStories(){
        obsIsEmpty.set(false)
        obsIsFull.set(false)
        val baeRepo = BaseRepository()
        val responseCall: Call<ListStoriesResponse?> = baeRepo.apiInterface
            .getListStories(PrefMethods.getStoreData()!!.id!!.toInt())
        responseCall.enqueue(object : Callback<ListStoriesResponse?> {
            override fun onResponse(call: Call<ListStoriesResponse?>, response: Response<ListStoriesResponse?>) {
                if (response.isSuccessful) {
                    if (response.body()!!.success!!) {
                        if (response.body()!!.data!!.isNotEmpty()) {
                            storiesList = response.body()!!.data as ArrayList<DataItem>
                            addedStoriesAdapter.updateList(storiesList)
                            setShowProgress(false)
                            obsIsEmpty.set(false)
                            obsIsFull.set(true)

                            for (item in storiesList) {
                                for (xItem in item.stories!!) {
                                    myStory = MyStory()
                                    if (xItem!!.media.isNullOrEmpty()){
                                        myStory.url = ""
                                    }else{
                                        myStory.url = xItem.mediaUrl
                                    }
                                    myStory.date = simpleDateFormat.parse(xItem.createdAt)
                                    myStory.description = xItem.text
                                    myStoriesList.add(myStory)
                                }
                            }
                            setValue(Codes.SHOW_STORY)
                        }else{
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
        })
    }
}