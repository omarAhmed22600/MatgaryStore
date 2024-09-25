package com.brandsin.store.ui.main.marketingRequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentChooseStoriesMarketingRequestBinding
import com.brandsin.store.model.ListStoriesResponse
import com.brandsin.store.model.Story
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItemByDate
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.marketingRequest.adapter.ChooseStoriesAdapter
import com.brandsin.store.ui.main.marketingRequest.viewmodel.MarketingRequestViewModel
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.visible

class ChooseStoriesMarketingRequestFragment : BaseHomeFragment() {

    private var _binding: FragmentChooseStoriesMarketingRequestBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MarketingRequestViewModel by activityViewModels()

    private lateinit var chooseStoriesAdapter: ChooseStoriesAdapter

    private val btnChooseStoryClickCallBack: (storyItem: Story) -> Unit = { storyItem ->
        if (viewModel.storiesIds.contains(storyItem.id.toString())) { // storyItem.isSelected == true
            viewModel.storiesIds.remove(storyItem.id.toString())
            viewModel.storiesItem.remove(storyItem)
        } else {
            viewModel.storiesIds.add(storyItem.id.toString())
            viewModel.storiesItem.add(storyItem)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChooseStoriesMarketingRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding!!.viewModel = viewModel
        _binding!!.lifecycleOwner = this
        setBarName(getString(R.string.marketing_requests))

        viewModel.getListStories()

        initViews()
        initRecycler()
        setBtnListener()
        subscribeData()
    }

    private fun initViews() {
        when (viewModel.pinStoriesType.value) {
            "story_to_home" -> {

            }
            "story_to_offers" -> {

            }
        }
    }

    private fun initRecycler() {
        binding.rvChooseStories.apply {
            chooseStoriesAdapter = ChooseStoriesAdapter(
                btnChooseStoryClickCallBack
            )
            adapter = chooseStoriesAdapter
        }
    }

    private fun setBtnListener() {
        binding.btnContinuation.setOnClickListener {
            if (viewModel.storiesIds.size > 0 || viewModel.storiesIds.isNotEmpty()) {
                findNavController().navigate(R.id.reviewChosenStoriesMarketingFragment)
            } else {
                showToast(getString(R.string.please_select_stories_first), 1)
            }
        }
    }

    private fun subscribeData() {

        viewModel.getListStoriesResponse.observe(viewLifecycleOwner) { it ->
            when (it) {
                is ResponseHandler.Success -> {
                    // bind data to the view
                    if (it.data?.isEmpty() == true || it.data?.size == 0) {
                        // binding.emptyLayout.visible()
                        binding.rvChooseStories.gone()
                    } else {
                        // binding.emptyLayout.gone()
                        binding.rvChooseStories.visible()

                        // set data in adapter
                        // Assuming you have the ListStoriesResponse instance named listStoriesResponse
                        val storiesItemByDateList: List<ListStoriesResponse>? = it.data

                        if (storiesItemByDateList != null) {
                            val allStories = ArrayList<Story>()

                            for (storiesItemByDate in storiesItemByDateList) {
                                val storiesList: List<Story?> = storiesItemByDate.stories

                                storiesList?.forEach { story ->
                                    story?.let {
                                        allStories.add(it)
                                    }
                                }?.apply {
                                    chooseStoriesAdapter.submitData(allStories)
                                }
                            }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.storiesIds.clear()
        viewModel.storiesItem.clear()
        _binding = null
    }
}