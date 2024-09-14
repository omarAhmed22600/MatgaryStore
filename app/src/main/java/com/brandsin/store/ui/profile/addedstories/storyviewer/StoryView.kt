package com.brandsin.store.ui.profile.addedstories.storyviewer

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.DialogStoriesBinding
import com.brandsin.store.model.profile.addedstories.liststories.StoriesItem
import com.brandsin.store.ui.profile.addedstories.showstory.ShowStoryViewModel
import com.brandsin.store.ui.profile.addedstories.storyviewer.callBack.CubeOutTransformer
import com.brandsin.store.ui.profile.addedstories.storyviewer.callBack.PageChangeListener
import com.brandsin.store.ui.profile.addedstories.storyviewer.callBack.PageViewOperator
import com.brandsin.store.ui.profile.addedstories.storyviewer.callBack.TouchCallbacks
import com.brandsin.store.utils.PullDismissLayout
import com.bumptech.glide.Glide
class StoryView(
    var currentPage: Int,
    private val storiesList: MutableList<ArrayList<StoriesItem>>
) : DialogFragment(), PullDismissLayout.Listener, Observer<Any?>, PageViewOperator, TouchCallbacks {

    private lateinit var binding: DialogStoriesBinding
    private lateinit var pagerAdapter: StoryPagerAdapter

    //  var storiesItem = StoriesItem
    // private var currentPage: Int = 0
    lateinit var viewModel: ShowStoryViewModel

    //Touch Events
    private var isDownClick = false
    private var elapsedTime: Long = 0
    private var width = 0
    private var height: Int = 0
    private var xValue = 0f
    private var yValue: Float = 0f
    private var num = 0
    private val isRtl = false
    private var storyViewListener: StoryViewListener? = null

    /**
     * Change ViewPage sliding programmatically(not using reflection).
     * https://tech.dely.jp/entry/2018/12/13/110000
     * What for?
     * setCurrentItem(int, boolean) changes too fast. And it cannot set animation duration.
     */
    private var prevDragPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Inflate the layout for requireActivity() fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_stories, container, false)
        return binding.root
    }

    fun setStoryViewListener(storyViewListener: StoryViewListener?) {
        this.storyViewListener = storyViewListener
    }

    interface StoryViewListener {
        fun onDoneClicked(num: Int, storiesItem: StoriesItem)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
        height = displayMetrics.heightPixels

        viewModel = ViewModelProvider(this)[ShowStoryViewModel::class.java]
        binding.viewModel = viewModel

        // storiesItem = fragmentArgs.storiesItem
        viewModel.storiesList = ArrayList()
        viewModel.storiesList = storiesList
        // viewModel.storiesList.add(storiesItem)

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        setUpPager()

        (view.findViewById<View>(R.id.pull_dismiss_layout) as PullDismissLayout).setListener(this)

        (view.findViewById<View>(R.id.pull_dismiss_layout) as PullDismissLayout)
            .setmTouchCallbacks(this)

        setBtnListener()
    }

    private fun setBtnListener() {
        binding.close.setOnClickListener {
            dismiss()
            dismissAllowingStateLoss()
        }
    }

    override fun onDismissed() {
        dismissAllowingStateLoss()
    }

    override fun onShouldInterceptTouchEvent(): Boolean {
        return false
    }

    private fun setUpPager() {
        // val storyUserList = StoryGenerator.generateStories()
        //preLoadStories(viewModel.storiesList[currentPage])

        pagerAdapter = StoryPagerAdapter(
            this,
            childFragmentManager,
            viewModel.storiesList
        )
        binding.viewPagerFixedViewPager.adapter = pagerAdapter
        binding.viewPagerFixedViewPager.currentItem = currentPage
        binding.viewPagerFixedViewPager.setPageTransformer(
            true,
            CubeOutTransformer()
        )
        binding.viewPagerFixedViewPager.addOnPageChangeListener(object : PageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }

            override fun onPageScrollCanceled() {
                currentFragment().resumeCurrentStory()
            }
        })
    }

    override fun backPageView() {
        if (binding.viewPagerFixedViewPager.currentItem > 0) { // if (viewPagerFixedViewPager != null)
            try {
                fakeDrag(false)
            } catch (e: Exception) {
                // NO OP
            }
        }
    }

    override fun nextPageView() {
        // if (viewPagerFixedViewPager != null) {
        if (binding.viewPagerFixedViewPager.currentItem + 1 < binding.viewPagerFixedViewPager.adapter?.count ?: 0) {
            try {
                fakeDrag(true)
            } catch (e: Exception) {
                //NO OP
            }
        } else {
            dismiss()
            dismissAllowingStateLoss()
            //there is no next story
            // Toast.makeText(context, "All stories displayed.", Toast.LENGTH_LONG).show()
        }
        //   }
    }

    private fun fakeDrag(forward: Boolean) {
        //if (viewPagerFixedViewPager != null) {
        if (prevDragPosition == 0 && binding.viewPagerFixedViewPager.beginFakeDrag()) {
            ValueAnimator.ofInt(0, binding.viewPagerFixedViewPager.width).apply {
                duration = 400L
                interpolator = FastOutSlowInInterpolator()
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        removeAllUpdateListeners()
                        if (binding.viewPagerFixedViewPager.isFakeDragging) {
                            binding.viewPagerFixedViewPager.endFakeDrag()
                        }
                        prevDragPosition = 0
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        removeAllUpdateListeners()
                        if (binding.viewPagerFixedViewPager.isFakeDragging) {
                            binding.viewPagerFixedViewPager.endFakeDrag()
                        }
                        prevDragPosition = 0
                    }

                    override fun onAnimationStart(animation: Animator) {}
                })
                addUpdateListener {
                    if (!binding.viewPagerFixedViewPager.isFakeDragging) return@addUpdateListener
                    val dragPosition: Int = it.animatedValue as Int
                    val dragOffset: Float =
                        ((dragPosition - prevDragPosition) * if (forward) -1 else 1).toFloat()
                    prevDragPosition = dragPosition
                    binding.viewPagerFixedViewPager.fakeDragBy(dragOffset)
                }
            }.start()
        }
        //  }
    }

    override fun onResume() {
        super.onResume()
        val params: WindowManager.LayoutParams? = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = params
    }

    private fun preLoadImages(imageList: MutableList<StoriesItem>) {
        imageList.forEach { imageStory ->
            Glide.with(this).load(imageStory.mediaUrl).preload()
        }
    }

    private fun currentFragment(): StoryDisplayFragment {
        return pagerAdapter.findFragmentByPosition(
            binding.viewPagerFixedViewPager,
            currentPage
        ) as StoryDisplayFragment
    }


    companion object {
        val progressState = SparseIntArray()
    }

    override fun touchPull() {
        elapsedTime = 0
        //stopTimer()
        // binding.storiesProgressView.pause()
    }

    override fun touchDown(xValue: Float, yValue: Float) {
        this.xValue = xValue
        this.yValue = yValue
        if (!isDownClick) {
            //  runTimer()
        }
    }

    override fun touchUp() {
        if (isDownClick && elapsedTime < 500) {
            // stopTimer()
            if ((height - yValue).toInt() <= 0.8 * height) {
                if (!TextUtils.isEmpty("storiesList.get(counter).getDescription()")
                    && (height - yValue).toInt() >= 0.2 * height
                    || TextUtils.isEmpty("storiesList.get(counter).getDescription()")
                ) {
                    if (xValue.toInt() <= width / 2) {
                        //Left
                        if (isRtl) {
                            //nextStory()
                        } else {
                            // previousStory()
                        }
                    } else {
                        //Right
                        if (isRtl) {
                            //  previousStory()
                        } else {
                            //nextStory()
                        }
                    }
                }
            }
        } else {
            // stopTimer()
            // setHeadingVisibility(View.VISIBLE)
            // binding.storiesProgressView.resume()
        }
        elapsedTime = 0
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        when (value) {
            /*Codes.SHOW_STORY -> {
                viewModel.setShowProgress(false)
                show()
            }*/
            else -> {
                if (value is String) {
                    //   showToast(it.toString(), 1)
                }
                viewModel.setShowProgress(false)
            }
        }
    }
}