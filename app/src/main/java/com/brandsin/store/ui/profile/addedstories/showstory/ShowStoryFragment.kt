package com.brandsin.store.ui.profile.addedstories.showstory

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.ListFormatter.Width
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bolaware.viewstimerstory.Momentz
import com.bolaware.viewstimerstory.MomentzCallback
import com.bolaware.viewstimerstory.MomentzView
import com.brandsin.store.R
import com.brandsin.store.databinding.ProfileFragmentShowStoryBinding
import com.brandsin.store.model.ListStoriesResponse
import com.brandsin.store.model.Story
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.invisible
import com.brandsin.store.utils.visible
import com.bumptech.glide.Glide
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import timber.log.Timber
import toPixel

class ShowStoryFragment : BaseHomeFragment(), Observer<Any?>, MomentzCallback {

    private lateinit var binding: ProfileFragmentShowStoryBinding

    lateinit var viewModel: ShowStoryViewModel
    private val fragmentArgs: ShowStoryFragmentArgs by navArgs()
var calledOnce = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for requireActivity() fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.profile_fragment_show_story,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        viewModel = ViewModelProvider(this)[ShowStoryViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)
        viewModel.selectedStory.value = fragmentArgs.storyItemId
        viewModel.getListStoriesResponse.observe(viewLifecycleOwner) { it ->
            when (it) {
                is ResponseHandler.Success -> {
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
                                viewModel.storiesItemByDates =
                                    it.data as ArrayList<ListStoriesResponse>
                            }
                        }
                    }
                    viewModel.addedStoriesAdapter.updateList(viewModel.storiesItemByDates)
                    viewModel.setShowProgress(false)
                    viewModel.obsIsEmpty.set(false)
                    viewModel.obsIsFull.set(true)

                    for (item in viewModel.storiesItemByDates) {
                        for (xItem in item.stories!!) {
                            // myStory = MyStory()
                            if (xItem.media_url.isNullOrEmpty()) {
                                // myStory.url = ""
                            } else {
                                // myStory.url = xItem.mediaUrl
                            }
                            // myStory.date = simpleDateFormat.parse(xItem.createdAt.toString())
                            // myStory.description = xItem.text
                            // myStoriesList.add(myStory)
                        }
                    }
                    viewModel.setValue(Codes.SHOW_STORY)
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

        viewModel.setShowProgress(true)

        viewModel.getListStories()

        viewModel.showProgress().observe(viewLifecycleOwner) { aBoolean ->
            if (!aBoolean!!) {
                binding.progressLayout.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                binding.progressLayout.visibility = View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            }
        }

        Glide.with(MyApp.context)
            .load(PrefMethods.getStoreData()?.thumbnail)
            .error(R.drawable.app_logo)
            .into(binding.storyStoreImg)

        binding.storyStoreName.text = PrefMethods.getStoreData()?.name.toString()

        setBtnListener()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setBtnListener() {
        binding.storyClose.setOnClickListener {
            // done()
            findNavController().navigateUp()
        }
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        when (value) {
            Codes.SHOW_STORY -> {
                viewModel.setShowProgress(false)
                if (viewModel.selectedStory.value == -1)
                    show()
                else
                    showOneStoryOnly()
            }

            else -> {
                if (value is String) {
                    showToast(value.toString(), 1)
                }
                viewModel.setShowProgress(false)
            }
        }
    }

    private fun showOneStoryOnly() {
        for (item in viewModel.storiesItemByDates) {
            for (xItem in item.stories!!) {
                if (xItem.id == viewModel.selectedStory.value) {
                    if (xItem?.media_url.isNullOrEmpty()) {
                        val textView = TextView(requireActivity())
                        textView.text = xItem?.text
                        textView.textSize = 20f.toPixel(requireActivity()).toFloat()
                        textView.gravity = Gravity.CENTER
                        textView.setTextColor(Color.parseColor("#ffffff"))

                        val momentz = MomentzView(textView, 5)
                        momentz.view.solidColor
                        viewModel.listOfViews.add(momentz)

                    } else if (xItem?.media_url?.endsWith(".jpeg") == true ||
                        xItem?.media_url?.endsWith(".jpg") == true ||
                        xItem?.media_url?.endsWith(".png") == true
                    ) {
//                        binding.icSound.hide()

                        val image = ImageView(requireActivity())
                        val momentz = MomentzView(image, 10)

                        Picasso.get()
                            .load(xItem.media_url)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .into(image, object : Callback {
                                override fun onSuccess() {}

                                override fun onError(e: Exception?) {
                                    Toast.makeText(
                                        requireActivity(),
                                        e?.localizedMessage,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    e?.printStackTrace()
                                }
                            })
                        viewModel.listOfViews.add(momentz)

                    } else if (xItem?.media_url?.endsWith(".mp4") == true) {
//                        binding.icSound.show()

                        val video = VideoView(requireActivity())

//                        if (PrefMethods.getSoundStory() == 0f) { // true
//                            video.isSoundEffectsEnabled = false
//                            binding.icSound.setImageResource(R.drawable.ic_sound_off)
//                        } else {
//                            video.isSoundEffectsEnabled = true
//                            binding.icSound.setImageResource(R.drawable.ic_sound_on)
//                        }

                        val momentZ = MomentzView(video, 60)
                        val str = xItem.media_url
                        val uri = Uri.parse(str)
                        video.setVideoURI(uri)
                        viewModel.listOfViews.add(momentZ)
                    }
                }
            }
        }
        val callback = object : MomentzCallback {
            override fun done() {
                try {
                    findNavController().navigateUp()
                } catch (e:Exception)
                {
                    Timber.e("$e")
                }
                println("Done method called")
            }

            override fun onNextCalled(view: View, momentz: Momentz, index: Int) {
                if (view is VideoView&&calledOnce.not()) {
                    momentz.pause(true)
                    playVideo(view, index, momentz)
                    calledOnce = true
                }
                println("onNextCalled method called with index: $index")
            }
        }
        Momentz(requireActivity(), viewModel.listOfViews, binding.container, callback).start()
    }

    fun show() {
        for (item in viewModel.storiesItemByDates) {
            for (xItem in item.stories!!) {
                if (xItem?.media_url.isNullOrEmpty()) {
                    binding.constraintTagXAndY.invisible()

                    val textView = TextView(requireActivity())
                    textView.text = xItem?.text
                    textView.textSize = 20f.toPixel(requireActivity()).toFloat()
                    textView.gravity = Gravity.CENTER
                    textView.setTextColor(Color.parseColor("#ffffff"))

                    val momentz = MomentzView(textView, 5)
                    viewModel.listOfViews.add(momentz)

                } else if (xItem?.media_url?.endsWith(".jpeg") == true ||
                    xItem?.media_url?.endsWith(".jpg") == true ||
                    xItem?.media_url?.endsWith(".png") == true
                ) {
                    binding.constraintTagXAndY.invisible()

                    val image = ImageView(requireActivity())
                    val momentz = MomentzView(image, 10)

                    Picasso.get()
                        .load(xItem.media_url)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(image, object : Callback {
                            override fun onSuccess() {}

                            override fun onError(e: Exception?) {
                                Toast.makeText(
                                    requireActivity(),
                                    e?.localizedMessage,
                                    Toast.LENGTH_LONG
                                ).show()
                                e?.printStackTrace()
                            }
                        })
                    viewModel.listOfViews.add(momentz)

                } else if (xItem?.media_url?.endsWith(".mp4") == true) {
                    binding.constraintTagXAndY.invisible()

                    val video = VideoView(requireActivity())
                    val momentz = MomentzView(video, 60)
                    val str = xItem.media_url
                    val uri = Uri.parse(str)
                    video.setVideoURI(uri)
                    viewModel.listOfViews.add(momentz)
                }
            }
        }

        Momentz(requireActivity(), viewModel.listOfViews, binding.container, this).start()
    }

    private fun setPositionWithOffset(view: View, offsetX: Float?, offsetY: Float?) {
        // Set the position of the view based on the provided offsets
        view.x = offsetX ?: 00.00f
        view.y = offsetY ?: 00.00f
    }

    override fun done() {
        // findNavController().navigateUp()
        // findNavController().navigate(R.id.show_story_to_add_stories)
    }

    override fun onNextCalled(view: View, momentz: Momentz, index: Int) {
        if (view is VideoView) {
            momentz.pause(true)
            playVideo(view, index, momentz)
        } else if ((view is ImageView) && (view.drawable == null)) {
            // momentz.pause(true)
            /*for (item in viewModel.storiesList) {
                for (xItem in item.stories!!) {
                    if (xItem?.x != 0.0 && xItem?.y != 0.0) {
                        setPositionWithOffset(
                            binding.constraintTagXAndY,
                            xItem?.x?.toFloat(), //
                            xItem?.y?.toFloat()  //
                        )
                        binding.constraintTagXAndY.visible()

                        binding.productName.text = xItem?.product?.name.toString()
                        binding.productPrice.text =
                            xItem?.product?.skus?.get(0)?.price.toString() + " " + getString(R.string.currency)
                    } else {
                        binding.constraintTagXAndY.invisible()
                    }
                }
            }*/
        }
    }


    private fun playVideo(videoView: VideoView, index: Int, momentz: Momentz) {
        videoView.requestFocus()
        videoView.start()

        videoView.setOnInfoListener(object : MediaPlayer.OnInfoListener {
            override fun onInfo(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // Here the video starts
                    momentz.editDurationAndResume(index, (videoView.duration) / 1000)
                    return true
                }
                return false
            }
        })
    }
}