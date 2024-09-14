package com.brandsin.store.ui.profile.addedstories

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItemByDate

class ItemAddedStoriesViewModel(var item: StoriesItemByDate) : BaseViewModel() {

    private var storyList: ArrayList<StoriesItem> = ArrayList()

    var storyAdapter = StoryAdapter()

    init {
        storyList = item.stories as ArrayList<StoriesItem>
        storyAdapter.updateList(storyList)
    }
}