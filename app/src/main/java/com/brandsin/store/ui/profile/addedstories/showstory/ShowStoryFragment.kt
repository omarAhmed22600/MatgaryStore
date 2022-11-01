package com.brandsin.store.ui.profile.addedstories.showstory

import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.*
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
import com.bumptech.glide.Glide
import com.brandsin.store.R
import com.brandsin.store.databinding.ProfileFragmentShowStoryBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.PrefMethods
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import toPixel
import java.lang.Exception

class ShowStoryFragment :  BaseHomeFragment(), Observer<Any?>, MomentzCallback
{
    lateinit var binding : ProfileFragmentShowStoryBinding
    lateinit var viewModel: ShowStoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for requireActivity() fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment_show_story, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShowStoryViewModel::class.java)

        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)
        viewModel.setShowProgress(true)
        viewModel.getListStories()
        viewModel.showProgress().observe(viewLifecycleOwner, { aBoolean ->
            if (!aBoolean!!) {
                binding.progressLayout.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                binding.progressLayout.visibility = View.VISIBLE
                requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        })

        Glide.with(MyApp.context).load(PrefMethods.getStoreData()!!.thumbnail)
            .error(R.drawable.app_logo)
            .into(binding.storyStoreImg)
        
        binding.storyStoreName.text = PrefMethods.getStoreData()!!.name

        binding.storyClose.setOnClickListener {
            done()
        }
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        when (it) {
            Codes.SHOW_STORY -> {
                viewModel.setShowProgress(false)
                show()
            }
            else -> {
                if (it is String) {
                    showToast(it.toString(), 1)
                }
                viewModel.setShowProgress(false)
            }
        }
    }

    fun show(){

        for (item in viewModel.storiesList) {
            for (xItem in item.stories!!) {
                if (xItem!!.media.isNullOrEmpty()){
                    val textView = TextView(requireActivity())
                    textView.text = xItem.text
                    textView.textSize = 20f.toPixel(requireActivity()).toFloat()
                    textView.gravity = Gravity.CENTER
                    textView.setTextColor(Color.parseColor("#ffffff"))

                    var momentz = MomentzView(textView, 5)
                    viewModel.listOfViews.add(momentz)

                }else if (xItem.media!![0]!!.mimeType!!.contains("image")){

                    val image = ImageView(requireActivity())
                    var momentz = MomentzView(image, 10)
                    Picasso.get()
                        .load(xItem.mediaUrl)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .into(image, object : Callback {
                            override fun onSuccess() {

                            }
                            override fun onError(e: Exception?) {
                                Toast.makeText(requireActivity(),e?.localizedMessage, Toast.LENGTH_LONG).show()
                                e?.printStackTrace()
                            }
                        })
                    viewModel.listOfViews.add(momentz)

                }else if (xItem.media[0]!!.mimeType!!.contains("video")){

                    val video = VideoView(requireActivity())
                    var momentz = MomentzView(video, 60)
                    val str = xItem.mediaUrl
                    val uri = Uri.parse(str)
                    video.setVideoURI(uri)
                    viewModel.listOfViews.add(momentz)

                }
            }
        }

        Momentz(requireActivity(), viewModel.listOfViews, binding.container, this).start()

    }
    override fun done() {
        findNavController().navigateUp()
//        findNavController().navigate(R.id.show_story_to_add_stories)
    }

    override fun onNextCalled(view: View, momentz: Momentz, index: Int) {
        if (view is VideoView) {
            momentz.pause(true)
            playVideo(view, index, momentz)
        } else if ((view is ImageView) && (view.drawable == null)) {
//            momentz.pause(true)

        }
    }

    fun playVideo(videoView: VideoView, index: Int, momentz: Momentz) {

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