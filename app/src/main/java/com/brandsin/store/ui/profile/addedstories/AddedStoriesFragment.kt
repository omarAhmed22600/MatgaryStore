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
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.profile.addedstories.storyviewer.StoryView
import com.brandsin.store.utils.observe

class AddedStoriesFragment : BaseHomeFragment(), Observer<Any?>, StoryView.StoryViewListener {

    lateinit var binding: ProfileFragmentAddedStoriesBinding
    lateinit var viewModel: AddedStoriesViewModel

    var storiesItem = StoriesItem()

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

        observe(viewModel.addedStoriesAdapter.deleteStoryData) {
            when (it) {
                is StoriesItem -> {
                    viewModel.setShowProgress(true)
                    viewModel.deleteStories(it.id)
                }
            }
        }

        /* observe(viewModel.addedStoriesAdapter.showStoryData) {
            when (it) {
                is StoriesItem -> {
                    findNavController().navigate(R.id.add_stories_to_show_story)
                }
            }
        }*/

        observe(viewModel.addedStoriesAdapter.allStories) {
            val stories: MutableList<ArrayList<StoriesItem>> = ArrayList()

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

    override fun onDoneClicked(num: Int, storiesItem: StoriesItem) {
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