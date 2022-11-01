package com.brandsin.store.ui.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.brandsin.store.R
import com.brandsin.store.database.BaseRepository
import com.brandsin.store.databinding.ActivityWelcomeBinding
import com.brandsin.store.model.IntroResponse
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.auth.AuthActivity
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.Utils
import com.brandsin.user.model.constants.Params
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class WelcomeActivity : AppCompatActivity()
{
    lateinit var binding : ActivityWelcomeBinding
    var MAX_STEP = 0
    lateinit var viewPager: ViewPager
    var myViewPagerAdapter: MyViewPagerAdapter? = null
    val about_title_array = ArrayList<String>()
    val about_imagesAr_array = ArrayList<String>()
    val about_imagesEn_array = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        if (PrefMethods.getWelcome()){
            val intent = Intent(applicationContext, AuthActivity::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@WelcomeActivity).toBundle())
                finish()
            }
        }
        getIntro()

        binding.btnNext.setOnClickListener(View.OnClickListener {
            val current = viewPager.currentItem + 1
            if (current < MAX_STEP)
            {
                // move to next screen
                viewPager.currentItem = current
            } else {
                PrefMethods.saveWelcome(true)
                val intent = Intent(applicationContext, AuthActivity::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@WelcomeActivity).toBundle())
                }
            }
        })

        binding.btnBack.setOnClickListener(View.OnClickListener {
            val current = viewPager.currentItem - 1
            viewPager.currentItem = current
        })
    }

    fun setSlider(){
        binding.consPage.visibility = View.VISIBLE
        viewPager = findViewById(R.id.view_pager)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        myViewPagerAdapter = MyViewPagerAdapter()
        viewPager.adapter = myViewPagerAdapter
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        binding.dotsIndicator.setViewPager(viewPager)
        viewPager.adapter!!.registerDataSetObserver(binding.dotsIndicator.dataSetObserver)
    }
    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int)
            {
                if (position == MAX_STEP)
                {
                    binding.btnNext.text = getString(R.string.login)
                }
                else
                {
                    binding.btnNext.text = getString(R.string.next)
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        }

   inner class MyViewPagerAdapter : PagerAdapter()
   {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any
        {
            layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
            val view: View = layoutInflater!!.inflate(R.layout.raw_intro_app, container, false)
            if (PrefMethods.getLanguage()=="ar"){
                Glide.with(applicationContext).load(about_imagesAr_array[position]).into((view.findViewById<View>(R.id.iv_introImg) as ImageView))
            }else{
                Glide.with(applicationContext).load(about_imagesEn_array[position]).into((view.findViewById<View>(R.id.iv_introImg) as ImageView))
            }
            (view.findViewById<View>(R.id.tv_introTitle) as TextView).text = about_title_array[position]
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return about_title_array.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    private var exitTime: Long = 0

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun doExitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            finishAffinity()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onBackPressed() {
        doExitApp()
    }

    fun getIntro(){
        val baeRepo = BaseRepository()
        val responseCall: Call<IntroResponse?> = baeRepo.apiInterface
            .getIntro("store")
        responseCall.enqueue(object : Callback<IntroResponse?> {
            override fun onResponse(call: Call<IntroResponse?>, response: Response<IntroResponse?>) {
                if (response.isSuccessful) {
                    if (response.body()!!.success!!) {
                        if (response.body()!!.data!!.isNotEmpty()) {
                            MAX_STEP = response.body()!!.data!!.size
                            for(item in response.body()!!.data!!){
                                if (item != null) {
                                    about_title_array.add(item.introduction.toString())
                                    about_imagesAr_array.add(item.imageAr.toString())
                                    about_imagesEn_array.add(item.imageEn.toString())
                                }
                            }
                            setSlider()
                        }else{
                            val intent = Intent(applicationContext, AuthActivity::class.java)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this@WelcomeActivity).toBundle())
                                finish()
                            }
                        }
                    }
                } else {
                    showToast(response.message(),1)
                }
            }
            override fun onFailure(call: Call<IntroResponse?>, t: Throwable) {
                showToast(t.message!!,1)
            }
        })
    }

    fun showToast(msg : String, type : Int) {
        //succss 2
        //false  1
        val bundle = Bundle()
        bundle.putString(Params.DIALOG_TOAST_MESSAGE, msg)
        bundle.putInt(Params.DIALOG_TOAST_TYPE, type)
        Utils.startDialogActivity(this, DialogToastFragment::class.java.name, Codes.DIALOG_TOAST_REQUEST, bundle)
    }
}
