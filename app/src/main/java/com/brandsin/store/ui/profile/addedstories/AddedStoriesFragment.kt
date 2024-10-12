package com.brandsin.store.ui.profile.addedstories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.ProfileFragmentAddedStoriesBinding
import com.brandsin.store.model.ListStoriesResponse
import com.brandsin.store.model.Story
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.profile.addedstories.storyviewer.StoryView
import com.brandsin.store.utils.observe
import timber.log.Timber

class AddedStoriesFragment : BaseHomeFragment(), Observer<Any?>, StoryView.StoryViewListener {

    lateinit var binding: ProfileFragmentAddedStoriesBinding
    lateinit var viewModel: AddedStoriesViewModel

    var storiesItem = Story()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.profile_fragment_added_stories,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AddedStoriesViewModel::class.java]
        binding.viewModel = viewModel

        setBarName(getString(R.string.added_stories))

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        viewModel.setShowProgress(true)
        viewModel.getListStories()

        viewModel.showProgress().observe(viewLifecycleOwner) { aBoolean ->
            if (!aBoolean!!) {
                binding.progressLayout.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                binding.progressLayout.visibility = View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                );
            }
        }
        viewModel.getListStoriesResponse.observe(viewLifecycleOwner) { it ->
            when (it) {
                is ResponseHandler.Success -> {

                    viewModel.storiesList =
                        it.data as ArrayList<com.brandsin.store.model.ListStoriesResponse>
                    viewModel.addedStoriesAdapter.updateList(viewModel.storiesList)
                    viewModel.setShowProgress(false)
                    viewModel.obsIsEmpty.set(false)
                    viewModel.obsIsFull.set(true)
                    for (item in viewModel.storiesList) {
                        for (xItem in item.stories!!) {
                            // myStory = MyStory()
                            if (xItem!!.media_url.isNullOrEmpty()) {
                                // myStory.url = ""
                            } else {
                                // myStory.url = xItem.mediaUrl
                            }
                            // myStory.date = simpleDateFormat.parse(xItem.createdAt)
                            // myStory.description = xItem.text
                            // myStoriesList.add(myStory)
                        }
                    }
                }

                is ResponseHandler.Error -> {
                    viewModel.obsIsFull.set(false)

                    // show error message
                    showToast(it.message, 1)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(true)
                    viewModel.obsIsFull.set(false)
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(false)
                    viewModel.obsIsFull.set(false)
                }

                else -> {}
            }
        }
        observe(viewModel.addedStoriesAdapter.deleteStoryData) {
            when (it) {
                is Story -> {
                    viewModel.setShowProgress(true)
                    viewModel.deleteStories(it.id)
                }
            }
        }

        observe(viewModel.addedStoriesAdapter.showStoryData) {
            when (it) {
                is Story -> {
//                    val args = Bundle().apply {
//                        putInt("storyItemId", it.id)
//                    }
//                    findNavController().navigate(R.id.nav_show_story, args)
                    val storyView = StoryView(0, mutableListOf(arrayListOf(it)))
                    storyView.setStoryViewListener(this)
                    storyView.show(childFragmentManager, "story")
                }
            }
        }

        observe(viewModel.addedStoriesAdapter.allStories) {
            val stories: MutableList<ArrayList<Story>> = ArrayList()

            it!!.forEach { value ->
                value.store = storiesItem.store
            }
            stories.add(it)
            val storyView = StoryView(0, stories)
            storyView.setStoryViewListener(this)
            storyView.show(childFragmentManager, "story")
        }
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        when (value) {
            Codes.ADD_STORIES -> {
                findNavController().navigate(R.id.add_stories_to_add_story)
            }

            else -> {
                if (value is String) {
                    showToast(value.toString(), 1)
                }
                viewModel.setShowProgress(false)
            }
        }
    }

    override fun onDoneClicked(num: Int, storiesItem: Story) {
        when (num) {
            1 -> {}
            2 -> {}

            else -> {
                view?.post {
                    findNavController().navigateUp()
                }
            }
        }
    }
}