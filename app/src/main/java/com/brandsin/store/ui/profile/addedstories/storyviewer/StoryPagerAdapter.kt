package com.brandsin.store.ui.profile.addedstories.storyviewer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.ui.profile.addedstories.storyviewer.callBack.PageViewOperator

class StoryPagerAdapter(
    private var pageViewOperator: PageViewOperator,
    fragmentManager: FragmentManager,
    private var storyList: MutableList<ArrayList<StoriesItem>>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment =
        StoryDisplayFragment.newInstance(pageViewOperator, position, storyList[position])

    override fun getCount(): Int {
        return storyList.size
    }

    fun findFragmentByPosition(viewPager: ViewPager, position: Int): Fragment? {
        try {
            val f = instantiateItem(viewPager, position)
            return f as? Fragment
        } finally {
            finishUpdate(viewPager)
        }
    }
}