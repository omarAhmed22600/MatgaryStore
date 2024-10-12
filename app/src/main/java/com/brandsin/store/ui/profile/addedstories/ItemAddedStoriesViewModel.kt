package com.brandsin.store.ui.profile.addedstories

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.ListStoriesResponse
import com.brandsin.store.model.Story
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItemByDate

class ItemAddedStoriesViewModel(var item: ListStoriesResponse) : BaseViewModel() {

    private var storyList: ArrayList<Story> = ArrayList()

    var storyAdapter = StoryAdapter()

    init {
        storyList = item.stories as ArrayList<Story>
        storyAdapter.updateList(storyList)
    }
}