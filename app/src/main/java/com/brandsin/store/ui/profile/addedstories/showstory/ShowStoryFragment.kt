package com.brandsin.store.ui.profile.addedstories.showstory

import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
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
import com.bolaware.viewstimerstory.Momentz
import com.bolaware.viewstimerstory.MomentzCallback
import com.bolaware.viewstimerstory.MomentzView
import com.brandsin.store.R
import com.brandsin.store.databinding.ProfileFragmentShowStoryBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.invisible
import com.bumptech.glide.Glide
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import toPixel

class ShowStoryFragment : BaseHomeFragment(), Observer<Any?>, MomentzCallback {

    private lateinit var binding: ProfileFragmentShowStoryBinding

    lateinit var viewModel: ShowStoryViewModel

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

        viewModel = ViewModelProvider(this)[ShowStoryViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

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
                show()
            }

            else -> {
                if (value is String) {
                    showToast(value.toString(), 1)
                }
                viewModel.setShowProgress(false)
            }
        }
    }

    fun show() {
        for (item in viewModel.storiesItemByDates) {
            for (xItem in item.stories!!) {
                if (xItem?.mediaUrl.isNullOrEmpty()) {
                    binding.constraintTagXAndY.invisible()

                    val textView = TextView(requireActivity())
                    textView.text = xItem?.text
                    textView.textSize = 20f.toPixel(requireActivity()).toFloat()
                    textView.gravity = Gravity.CENTER
                    textView.setTextColor(Color.parseColor("#ffffff"))

                    val momentz = MomentzView(textView, 5)
                    viewModel.listOfViews.add(momentz)

                } else if (xItem?.mediaUrl?.endsWith(".jpeg") == true ||
                    xItem?.mediaUrl?.endsWith(".jpg") == true ||
                    xItem?.mediaUrl?.endsWith(".png") == true
                ) {
                    binding.constraintTagXAndY.invisible()

                    val image = ImageView(requireActivity())
                    val momentz = MomentzView(image, 10)

                    Picasso.get()
                        .load(xItem.mediaUrl)
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

                } else if (xItem?.mediaUrl?.endsWith(".mp4") == true) {
                    binding.constraintTagXAndY.invisible()

                    val video = VideoView(requireActivity())
                    val momentz = MomentzView(video, 60)
                    val str = xItem.mediaUrl
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