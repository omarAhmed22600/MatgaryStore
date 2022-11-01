package com.brandsin.store.ui.profile.addedstories

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.profile.addedstories.liststories.DataItem
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import java.util.ArrayList

class ItemAddedStoriesViewModel (var item: DataItem) : BaseViewModel(){
    var storyList: ArrayList<StoriesItem> = ArrayList()
    var storyAdapter = StoryAdapter()

    init {
        storyList = item.stories as ArrayList<StoriesItem>
        storyAdapter.updateList(storyList)
    }
}