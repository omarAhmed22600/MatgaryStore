package com.brandsin.store.ui.profile.addedstories.storyviewer

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentStoryDisplayBinding
import com.brandsin.store.model.Story
import com.brandsin.store.ui.profile.addedstories.showstory.ShowStoryViewModel
import com.brandsin.store.ui.profile.addedstories.storyviewer.callBack.OnSwipeTouchListener
import com.brandsin.store.ui.profile.addedstories.storyviewer.callBack.PageViewOperator
import com.brandsin.store.ui.profile.addedstories.storyviewer.callBack.StoriesProgressView
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.visible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.danikula.videocache.HttpProxyCacheServer
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource

class StoryDisplayFragment(val pageViewOperator: PageViewOperator) : Fragment(),
    StoriesProgressView.StoriesListener {

    private var _binding: FragmentStoryDisplayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShowStoryViewModel by activityViewModels()

    private val position: Int by
    lazy { arguments?.getInt(EXTRA_POSITION) ?: 0 }

    private val storyUser: ArrayList<Story> by
    lazy {
        (arguments?.getParcelableArrayList<Story>(
            EXTRA_STORY_USER
        ) as ArrayList<Story>)
    }

    private val stories: ArrayList<Story> by lazy { storyUser }

    private var simpleExoPlayer: ExoPlayer? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory

    // var pageViewOperator: PageViewOperator? = null
    private var counter = 0
    private var pressTime = 0L
    private var limit = 500L
    private var onResumeCalled = false
    private var onVideoPrepared = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.storyDisplayVideo.useController = false
        setUpUi()
        updateStory()
        setBtnListener()
    }

    private fun setBtnListener() {
        binding.next.setOnClickListener {
            onResumeCalled = true
            if (counter == stories.size - 1) {
                pageViewOperator.nextPageView()
            } else {
                binding.storiesProgressView.skip()
            }
        }

        binding.previous.setOnClickListener {
            onResumeCalled = true
            if (counter == 0) {
                pageViewOperator.backPageView()
            } else {
                binding.storiesProgressView.reverse()
            }
        }


    }

    override fun onStart() {
        super.onStart()
        counter = restorePosition()
    }

    override fun onResume() {
        super.onResume()
        onResumeCalled = true
        /*if (counter < stories.size) {
            if (stories[counter].mediaUrl?.endsWith(".mp4", true) == true && !onVideoPrepared) {
                simpleExoPlayer?.playWhenReady = false
                simpleExoPlayer?.volume = volume
                return
            }
        }*/

        simpleExoPlayer?.seekTo(5)
        simpleExoPlayer?.playWhenReady = true
        if (counter == 1) {
            binding.storiesProgressView.startStories()
        } else {
            // restart animation
            binding.storiesProgressView.startStories(counter)
        }
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer?.playWhenReady = false
        binding.storiesProgressView.abandon()
    }

    override fun onComplete() {
        simpleExoPlayer?.release()
        pageViewOperator.nextPageView()
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        --counter
        savePosition(counter)
        updateStory()
    }

    override fun onNext() {
        if (stories.size <= counter + 1) {
            return
        }
        ++counter
        savePosition(counter)
        updateStory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        simpleExoPlayer?.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer?.release()
        simpleExoPlayer = null
        _binding = null
    }

/*
    private fun updateStory() {
        simpleExoPlayer?.volume = 1f
        simpleExoPlayer?.stop()

        if (stories[counter].media_url?.endsWith(".mp4", true) == true) {
            binding.storyDisplayVideo.visible()
            binding.storyDisplayImage.gone()
            binding.consTxtStory.gone()
            binding.tvTxtStory.gone()
            binding.storyDisplayVideoProgress.visible()
            initializePlayer()
        } else if ((stories[counter].media_url?.endsWith(".png", true) == true ||
                    stories[counter].media_url?.endsWith(".jpg", true) == true ||
                    stories[counter].media_url?.endsWith(".jpeg", true) == true)
        ) {
            binding.storyDisplayVideo.gone()
            binding.consTxtStory.gone()
            binding.tvTxtStory.visible()
            binding.storyDisplayImage.visible()
            binding.storyDisplayVideoProgress.gone()

            // set image of story
            Glide.with(requireContext())
                .load(stories[counter].media_url)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        exeption: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        pauseCurrentStory()
                        // Handle the case where image loading failed
                        // This method is called if Glide encounters an error during image loading
                        // You can show an error placeholder or handle it in another way
                        return false // Return false to allow Glide to display its default error drawable
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Handle the case where image loading is successful
                        // This method is called when the image is successfully loaded
                        // You can perform any additional actions here
                        binding.storyDisplayVideoProgress.gone()
                        resumeCurrentStory()
                        return false // Return false to allow Glide to display the loaded image
                    }

                })
                .into(binding.storyDisplayImage)

        } else if (stories[counter].media_url.isNullOrEmpty() &&
            stories[counter].text?.isNotEmpty() == true
        ) {
            binding.storyDisplayVideo.gone()
            binding.consTxtStory.visible()
            binding.tvTxtStory.visible()
            binding.storyDisplayImage.gone()
            binding.storyDisplayVideoProgress.gone()
            // set text of story with image of story
//            binding.tvTxtStory.text = stories[counter].text.toString()
        } else {
            binding.storyDisplayVideo.gone()
            binding.storyDisplayVideoProgress.gone()
            binding.storyDisplayImage.visible()
            Glide.with(requireContext())
                .load(stories[counter].media_url)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        exeption: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        pauseCurrentStory()
                        // Handle the case where image loading failed
                        // This method is called if Glide encounters an error during image loading
                        // You can show an error placeholder or handle it in another way
                        return false // Return false to allow Glide to display its default error drawable
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        // Handle the case where image loading is successful
                        // This method is called when the image is successfully loaded
                        // You can perform any additional actions here
                        binding.storyDisplayVideoProgress.gone()
                        resumeCurrentStory()
                        return false // Return false to allow Glide to display the loaded image
                    }
                })
                .into(binding.storyDisplayImage)
        }
    }
*/
private fun updateStory() {
//    simpleExoPlayer?.volume = PrefMethods.getSoundStory()

    simpleExoPlayer?.stop()

//    viewModel.updateViewStory(stories.id ?: 0)

    /*
     simpleExoPlayer null
     D  stories[counter] StoriesItem(inOffersPage=1, storeId=65, createdAt=2023-07-28 13:11:29, id=436, title=مُد للعود, text=null, store=Store(minPriceProduct=25, image=https://brandsin.net/media/user_v1oz1Yz27j/4665/conversions/image_350x350_0.jpeg, thumbnail=https://brandsin.net/media/user_v1oz1Yz27j/4665/conversions/image_350x350_0.jpeg, images=[{id=4600.0, url=https://brandsin.net/media/user_v1oz1Yz27j/4600/Certificate.jpg}], thumbnailId=4665, avgRating=5.0, name=مُـد للعود, id=65, commercialRegister={id=4666.0, url=https://brandsin.net/media/user_v1oz1Yz27j/4666/image.jpeg}, covers=[CoversItem(id=4660, url=https://brandsin.net/media/user_v1oz1Yz27j/4660/image.jpeg), CoversItem(id=4661, url=https://brandsin.net/media/user_v1oz1Yz27j/4661/image.jpeg), CoversItem(id=4662, url=https://brandsin.net/media/user_v1oz1Yz27j/4662/image.jpeg), CoversItem(id=4663, url=https://brandsin.net/media/user_v1oz1Yz27j/4663/image.jpeg), CoversItem(id=4664, url=https://brandsin.net/media/user_v1oz1Yz27j/4664/image.jpeg)]), media=null, mediaUrl=https://brandsin.net/media/user_/4731/story.mp4, views=85, isPinned=1, isPinnedHomepage=1)
     D  simpleExoPlayer null
     */
    if (stories[counter].media_url?.endsWith(".mp4", true) == true
        && stories[counter].text.isNullOrEmpty()
    ) {
        binding.storyDisplayVideo.visible()
        binding.storyDisplayImage.gone()
        binding.consTxtStory.gone()
        binding.tvTxtStory.gone()
        binding.storyDisplayVideoProgress.visible()
        initializePlayer()
    } else if (stories[counter].media_url?.isNotEmpty() == true && stories[counter].text?.isNotEmpty() == true) {
        binding.storyDisplayVideo.gone()
        binding.consTxtStory.visible()
        binding.consTxtStory.setBackgroundColor(Color.TRANSPARENT)
        binding.tvTxtStory.visible()
        binding.storyDisplayImage.visible()
        binding.storyDisplayVideoProgress.visible()
        // set text of story with image of story
//        binding.tvTxtStory.text = stories[counter].text.toString()
    } else if ((stories[counter].media_url?.endsWith(".png", true) == true ||
                stories[counter].media_url?.endsWith(".jpg", true) == true ||
                stories[counter].media_url?.endsWith(".jpeg", true) == true)
        && stories[counter].text.isNullOrEmpty()
    ) {
        binding.storyDisplayVideo.gone()
        binding.consTxtStory.gone()
        binding.tvTxtStory.visible()
        binding.storyDisplayImage.visible()
        binding.storyDisplayVideoProgress.visible()
        // set text of story with image of story
        binding.storyDisplayVideo.gone()
        binding.consTxtStory.gone()
        binding.tvTxtStory.visible()
        binding.storyDisplayImage.visible()
        binding.storyDisplayVideoProgress.gone()

        // set image of story
        Glide.with(requireContext())
            .load(stories[counter].media_url)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    exeption: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    pauseCurrentStory()
                    // Handle the case where image loading failed
                    // This method is called if Glide encounters an error during image loading
                    // You can show an error placeholder or handle it in another way
                    return false // Return false to allow Glide to display its default error drawable
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // Handle the case where image loading is successful
                    // This method is called when the image is successfully loaded
                    // You can perform any additional actions here
                    binding.storyDisplayVideoProgress.gone()
                    resumeCurrentStory()
                    return false // Return false to allow Glide to display the loaded image
                }

            })
            .into(binding.storyDisplayImage)

//        binding.tvTxtStory.text = stories[counter].text.toString()
    } else if (stories[counter].media_url.isNullOrEmpty() &&
        stories[counter].text?.isNotEmpty() == true
    ) {
        binding.storyDisplayVideo.gone()
        binding.consTxtStory.visible()
        binding.tvTxtStory.visible()
        binding.storyDisplayImage.gone()
        binding.storyDisplayVideoProgress.gone()
        // set text of story with image of story
            binding.tvTxtStory.text = stories[counter].text.toString()
    } else {
        binding.storyDisplayVideo.gone()
        binding.storyDisplayVideoProgress.gone()
        binding.storyDisplayImage.visible()
        Glide.with(requireContext())
            .load(stories[counter].media_url)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    exeption: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    pauseCurrentStory()
                    // Handle the case where image loading failed
                    // This method is called if Glide encounters an error during image loading
                    // You can show an error placeholder or handle it in another way
                    return false // Return false to allow Glide to display its default error drawable
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // Handle the case where image loading is successful
                    // This method is called when the image is successfully loaded
                    // You can perform any additional actions here
                    binding.storyDisplayVideoProgress.gone()
                    resumeCurrentStory()
                    return false // Return false to allow Glide to display the loaded image
                }
            })
            .into(binding.storyDisplayImage)
    }
}

/*
    private fun initializePlayer() {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = ExoPlayer.Builder(requireContext())
                .setRenderersFactory(DefaultRenderersFactory(requireContext()))
                .setLoadControl(DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                        DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                        DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                        DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                        DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
                    ).build())
                .build()
        }
        else {
            simpleExoPlayer?.release()
            simpleExoPlayer = ExoPlayer.Builder(requireContext())
                .setRenderersFactory(DefaultRenderersFactory(requireContext()))
                .setLoadControl(DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                        DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                        DefaultLoadControl.DEFAULT_MAX_BUFFER_MS,
                        DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                        DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
                    ).build())
                .build()
        }

        val proxy: HttpProxyCacheServer = MyApp.getInstance().getProxy(requireActivity())!!
        val proxyUrl = proxy.getProxyUrl(stories[counter].media_url ?: "")

        val videoUri = Uri.parse(proxyUrl)
        val mediaItem: MediaItem = MediaItem.fromUri(videoUri)
        val httpDataSourceFactory: DefaultHttpDataSource.Factory =
            DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
        val defaultDataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(requireContext(), httpDataSourceFactory)
        val cacheDataSourceFactory: DataSource.Factory = CacheDataSource.Factory()
            .setCache(MyApp.simpleCache!!)
            .setUpstreamDataSourceFactory(defaultDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        val mediaSource: MediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
            .createMediaSource(mediaItem)
        simpleExoPlayer?.prepare(mediaSource, false, false)
        if (onResumeCalled) {
            simpleExoPlayer?.playWhenReady = true
            simpleExoPlayer?.volume = 1f
        }

        binding.storyDisplayVideo.setShutterBackgroundColor(Color.BLACK)
        binding.storyDisplayVideo.player = simpleExoPlayer

        simpleExoPlayer?.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                binding.storyDisplayVideoProgress.gone()
                if (counter == stories.size.minus(1)) {
                    pageViewOperator.nextPageView()
                } else {
                    binding.storiesProgressView.skip()
                }
            }

            override fun onIsLoadingChanged(isLoading: Boolean) {
                super.onIsLoadingChanged(isLoading)
                if (isLoading) {
                    binding.storyDisplayVideoProgress.visible()
                    pressTime = System.currentTimeMillis()
                    pauseCurrentStory()
                } else {
                    binding.storyDisplayVideoProgress.gone()
                    try {
                        binding.storiesProgressView.getProgressWithIndex(counter)
                            .setDuration(simpleExoPlayer?.duration ?: 8000L)
                    } catch (exc: Exception) {
                        Log.d("StoryDisplay", "onLoadingChanged ${exc.localizedMessage}")
                    }
                    onVideoPrepared = true
                    resumeCurrentStory()
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                when (state) {
                    Player.STATE_BUFFERING -> Log.d("ExoPlayer", "Buffering...")
                    Player.STATE_READY -> Log.d("ExoPlayer", "Ready to play")
                    Player.STATE_ENDED -> Log.d("ExoPlayer", "Playback ended")
                    Player.STATE_IDLE -> Log.d("ExoPlayer", "Player idle")
                }
            }
        })
    }
*/
private fun initializePlayer() {
    if (simpleExoPlayer == null) {
        simpleExoPlayer = ExoPlayer.Builder(
            requireContext(),
            DefaultRenderersFactory(requireContext())
        ).build()
    } else {
        simpleExoPlayer?.release()
        simpleExoPlayer = null
        simpleExoPlayer = ExoPlayer.Builder(
            requireContext(),
            DefaultRenderersFactory(requireContext())
        ).build()
    }

    val proxy: HttpProxyCacheServer = MyApp.getInstance().getProxy(requireActivity())!!
    val proxyUrl = proxy.getProxyUrl(stories[counter].media_url ?: "")

    val videoUri = Uri.parse(proxyUrl)
    val mediaItem: MediaItem = MediaItem.fromUri(videoUri)
    val httpDataSourceFactory: DefaultHttpDataSource.Factory =
        DefaultHttpDataSource.Factory().setAllowCrossProtocolRedirects(true)
    val defaultDataSourceFactory: DataSource.Factory =
        DefaultDataSourceFactory(requireContext(), httpDataSourceFactory)
    val cacheDataSourceFactory: DataSource.Factory = CacheDataSource.Factory()
        .setCache(MyApp.simpleCache!!)
        .setUpstreamDataSourceFactory(defaultDataSourceFactory)
        .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

    val mediaSource: MediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
        .createMediaSource(mediaItem)
    simpleExoPlayer?.prepare(mediaSource, false, false)
    if (onResumeCalled) {
        simpleExoPlayer?.playWhenReady = true
//        simpleExoPlayer?.volume = PrefMethods.getSoundStory()
    }

    binding.storyDisplayVideo.setShutterBackgroundColor(Color.BLACK)
    binding.storyDisplayVideo.player = simpleExoPlayer

    simpleExoPlayer?.addListener(object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            binding.storyDisplayVideoProgress.gone()
            /*if (counter == stories.size.minus(1)) {
                pageViewOperator.nextPageView()
            } else {
                binding.storiesProgressView.skip()
            }*/
        }

        @Deprecated("Deprecated in Java")
        override fun onLoadingChanged(isLoading: Boolean) {
            super.onLoadingChanged(isLoading)
            if (isLoading) {
                binding.storyDisplayVideoProgress.visible()
                pressTime = System.currentTimeMillis()
                pauseCurrentStory()
            } else {
                binding.storyDisplayVideoProgress.gone()
                try {
                    binding.storiesProgressView.getProgressWithIndex(counter)
                        .setDuration(simpleExoPlayer?.duration ?: 8000L)
                } catch (exc: Exception) {
                    Log.d("StoryDisplay", "onLoadingChanged ${exc.localizedMessage}")
                }
                onVideoPrepared = true
                resumeCurrentStory()
            }
        }
    })
}




/*
    private fun setUpUi() {
        val touchListener = object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeTop() {
                //  Toast.makeText(activity, "onSwipeTop", Toast.LENGTH_LONG).visible()
            }

            override fun onSwipeBottom() {
                //  Toast.makeText(activity, "onSwipeBottom", Toast.LENGTH_LONG).show()
            }

            override fun onClick(view: View) {
                when (view) {
                    binding.next -> {
                        onResumeCalled = true
                        binding.constraintTagXAndY.gone()
                        if (counter == stories.size - 1) {
                            pageViewOperator.nextPageView()
                        } else {
                            binding.storiesProgressView.skip()
                        }
                    }

                    binding.previous -> {
                        onResumeCalled = true
                        binding.constraintTagXAndY.gone()
                        if (counter == 0) {
                            pageViewOperator.backPageView()
                        } else {
                            binding.storiesProgressView.reverse()
                        }
                    }
                }
            }

            override fun onLongClick() {
                hideStoryOverlay()
                if ((stories[counter].media_url?.endsWith(".png", true) == true ||
                            stories[counter].media_url?.endsWith(".jpg", true) == true ||
                            stories[counter].media_url?.endsWith(".jpeg", true) == true)
                    && stories[counter].text.isNullOrEmpty() && stories[counter].x != null && stories[counter].y != null
                ) {
                    // pauseCurrentStory()
                    onResumeCalled = false
                    binding.storiesProgressView.pause()

                    println("show == else if else if else if else if else if") // X Offset: 216, Y Offset: 930
                    println("show == X: ${stories[counter].x?.toFloat() ?: 0.0f}, Y: ${stories[counter].y?.toFloat() ?: 0.0f}")
                    binding.constraintTagXAndY.visible()
                    setPositionWithOffset(
                        binding.constraintTagXAndY,
                        stories[counter].x?.toFloat() ?: 0.0f,
                        stories[counter].y?.toFloat() ?: 0.0f
                    )
                    binding.productName.text = stories[counter].product?.name.toString()
                    binding.productPrice.text =
                        stories[counter].product?.skus?.get(0)?.price.toString() + " " + getString(R.string.currency)

                    Glide.with(requireContext())
                        .load(stories[counter].media_url)
                        .error(R.drawable.app_logo)
                        .into(binding.storyDisplayImage)
                }
            }

            override fun onTouchView(view: View, event: MotionEvent): Boolean {
                super.onTouchView(view, event)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressTime = System.currentTimeMillis()
                        pauseCurrentStory()
                        return false
                    }

                    MotionEvent.ACTION_UP -> {
                        showStoryOverlay()
                        resumeCurrentStory()
                        return limit < System.currentTimeMillis() - pressTime
                    }
                }
                return false
            }
        }
        binding.previous.setOnTouchListener(touchListener)
        binding.next.setOnTouchListener(touchListener)

        binding.storiesProgressView.setStoriesCountDebug(
            stories.size, position = arguments?.getInt(EXTRA_POSITION) ?: -1
        )
        binding.storiesProgressView.setAllStoryDuration(4000L)
        binding.storiesProgressView.setStoriesListener(this)

        Glide.with(this)
            .load(
                storyUser[if (position < storyUser.size) position else storyUser.size - 1].store?.thumbnail
            )
            .circleCrop()
            .into(binding.storyDisplayProfilePicture)

        binding.storyDisplayNick.text =
            storyUser[if (position < storyUser.size) position else storyUser.size - 1].store?.name
    }
*/
    private fun setUpUi() {
        val touchListener = object : OnSwipeTouchListener(requireActivity()) {
            override fun onSwipeTop() {
                //  Toast.makeText(activity, "onSwipeTop", Toast.LENGTH_LONG).show()
            }

            override fun onSwipeBottom() {
                //  Toast.makeText(activity, "onSwipeBottom", Toast.LENGTH_LONG).show()
            }

            override fun onClick(view: View) {
                when (view) {
                    binding.next -> {
                        if (counter == 2 - 1) {
                            pageViewOperator.nextPageView()
                        } else {
                            binding.storiesProgressView.skip()
                        }
                    }

                    binding.previous -> {
                        if (counter == 0) {
                            pageViewOperator.backPageView()
                        } else {
                            binding.storiesProgressView.reverse()
                        }
                    }
                }
            }

            override fun onLongClick() {
                hideStoryOverlay()
            }

            override fun onTouchView(view: View, event: MotionEvent): Boolean {
                super.onTouchView(view, event)
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressTime = System.currentTimeMillis()
                        pauseCurrentStory()
                        return false
                    }

                    MotionEvent.ACTION_UP -> {
                        showStoryOverlay()
                        resumeCurrentStory()
                        return limit < System.currentTimeMillis() - pressTime
                    }
                }
                return false
            }
        }
        binding.previous.setOnTouchListener(touchListener)
        binding.next.setOnTouchListener(touchListener)

        binding.storiesProgressView.setStoriesCountDebug(
            1, position = arguments?.getInt(EXTRA_POSITION) ?: -1
        )
        binding.storiesProgressView.setAllStoryDuration(4000L)
        binding.storiesProgressView.setStoriesListener(this)





        binding.storyOverlay.setOnLongClickListener {
            // pauseCurrentStory()
            onResumeCalled = false
            binding.storiesProgressView.pause()
            binding.storiesProgressView.abandon()

            if ((stories[counter].media_url?.endsWith(".png", true) == true ||
                        stories[counter].media_url?.endsWith(".jpg", true) == true ||
                        stories[counter].media_url?.endsWith(".jpeg", true) == true)
                && stories[counter].text.isNullOrEmpty() && stories[counter].x != null && stories[counter].y != null
            ) {

                println("show == else if else if else if else if else if") // X Offset: 216, Y Offset: 930
                println("show == X: ${stories[counter].x?.toFloat() ?: 0.0f}, Y: ${stories[counter].y?.toFloat() ?: 0.0f}")
                binding.constraintTagXAndY.visible()
                setPositionWithOffset(
                    binding.constraintTagXAndY,
                    stories[counter].x?.toFloat() ?: 0.0f,
                    stories[counter].y?.toFloat() ?: 0.0f
                )
                binding.productName.text = stories[counter].product?.name.toString()
                binding.productPrice.text =
                    stories[counter].product?.skus?.get(0)?.price.toString() + " " + getString(R.string.currency)

                Glide.with(requireContext())
                    .load(stories[counter].media_url)
                    .error(R.drawable.app_logo)
                    .into(binding.storyDisplayImage)

            }
            true
        }
    }


    private fun setPositionWithOffset(view: View, offsetX: Float, offsetY: Float) {
        // Set the position of the view based on the provided offsets
        view.x = offsetX
        view.y = offsetY
    }

    private fun showStoryOverlay() {
        if (binding.storyOverlay.alpha != 0F) return

        binding.storyOverlay.animate()
            .setDuration(100)
            .alpha(1F)
            .start()
    }

    private fun hideStoryOverlay() {
        if (binding.storyOverlay.alpha != 1F) return

        binding.storyOverlay.animate()
            .setDuration(200)
            .alpha(0F)
            .start()
    }

    private fun savePosition(pos: Int) {
        StoryView.progressState.put(position, pos)
    }

    private fun restorePosition(): Int {
        return StoryView.progressState.get(position) // return 0 //
    }

    fun pauseCurrentStory() {
        binding.storiesProgressView.pause()
    }

    fun resumeCurrentStory() {
        if (onResumeCalled) {
            showStoryOverlay()
            binding.storiesProgressView.resume()
        }
    }

    companion object {
        private const val EXTRA_POSITION = "EXTRA_POSITION"
        private const val EXTRA_STORY_USER = "EXTRA_STORY_USER"
        fun newInstance(
            pageViewOperator: PageViewOperator,
            position: Int,
            story: ArrayList<Story>
        ): StoryDisplayFragment {
            return StoryDisplayFragment(pageViewOperator).apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putParcelableArrayList(EXTRA_STORY_USER, story)
                }
            }
        }
    }
}