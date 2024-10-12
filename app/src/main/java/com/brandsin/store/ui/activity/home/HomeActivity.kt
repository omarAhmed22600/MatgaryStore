package com.brandsin.store.ui.activity.home

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityHomeBinding
import com.brandsin.store.databinding.NavHeaderMainBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.homepage.StoreOrderItem
import com.brandsin.store.ui.activity.ParentActivity
import com.brandsin.store.ui.activity.auth.AuthActivity
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.SingleLiveEvent
import com.brandsin.store.utils.observe
import com.brandsin.store.model.constants.Params
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.delay
import timber.log.Timber
import java.lang.Exception

class HomeActivity : ParentActivity(), Observer<Any?> {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    var viewModel: MainViewModel? = null

    private lateinit var navController: NavController
    val orderClickLiveData = SingleLiveEvent<StoreOrderItem?>()

    private var switchBusy: SwitchCompat? = null
    private var switchClose: SwitchCompat? = null

    var orderId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init view model
        initViewModel()
        binding.viewModel = viewModel

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_home_host_fragment)

        viewModel?.mutableLiveData!!.observe(this, this)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_add_stories,
                R.id.nav_offers,
                R.id.nav_categories,
                R.id.nav_refundableProducts,
                R.id.nav_discountCoupons,
                R.id.nav_add_product,
                R.id.nav_my_products,
                R.id.nav_marketingRequest,
                R.id.nav_wallet,
                R.id.nav_offers_and_features,
                R.id.nav_reports,
                R.id.nav_subscriptions,
                R.id.nav_help,
                R.id.nav_store_code,
                R.id.nav_about,
                R.id.nav_contact,
            ), drawerLayout
        )
        // setupActionBarWithNavController(navController, appBarConfiguration)

        initConnectivityManager()

        setUpToolbarAndStatusBar()

        navView.setupWithNavController(navController)

        // setupNavHeader()
        setupNewNavHeader()

        binding.ibBack.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.nav_home -> {
                    finishAffinity()
                }

                else -> {
                    navController.navigateUp()
                }
            }
        }

        observe(orderClickLiveData) {
            when (it) {
                is StoreOrderItem -> {
                        Timber.e("it $it")
                    val args = Bundle().apply {
                        putInt("order_id", it.id?:-1)  // Pass the chat_id variable
                    }
                    navController.navigate(R.id.nav_order_details,args)
                    }
            }
        }
       /* if (intent.getStringExtra("order_id") != null) {
            orderId = intent.getIntExtra("order_id", -1)
            if (orderId != -1) {
                val bundle = Bundle()
                bundle.putInt(Params.ORDER_ID, orderId)
                navController.navigate(R.id.nav_order_details, bundle)
            }
        }*/
        if (intent.getIntExtra("chat_id",-1) != -1) {
            Timber.e("chat")
            navController.navigate(R.id.nav_chat)
        } else if (intent.getIntExtra("order_id",-1) != -1)
        {
//            orderId = intent.getIntExtra("order_id",-1)
            val id = intent.getIntExtra("order_id",-1)
            Timber.e("order\n$id")
            val args = Bundle().apply {
                putInt("order_id", intent.getIntExtra("order_id",-1))  // Pass the chat_id variable
            }
            navController.navigate(R.id.nav_order_details,args)
        }else if (intent.getIntExtra("refundable_id",-1) != -1)
        {
            Timber.e("refund")
            navController.navigate(R.id.nav_refundableProducts)
        }

        switchBusy =
            navView.menu.findItem(R.id.nav_busy).actionView?.findViewById(R.id.switch_busy)

        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            viewModel!!.onLogoutClicked()
            true
        }
        switchBusy?.isChecked = PrefMethods.getStoreData()?.isBusy == 1
        switchBusy?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel!!.setMenuBusy(1)
            } else {
                viewModel!!.setMenuBusy(0)
            }
        }
        switchClose =
            navView.menu.findItem(R.id.nav_closed).actionView?.findViewById(R.id.switch_close)

        switchClose?.isChecked = PrefMethods.getStoreData()?.isClosed == 1
        switchClose?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel?.setMenuClosed(1)
            } else {
                viewModel?.setMenuClosed(0)
            }
        }
    }

    private fun setUpToolbarAndStatusBar() {
        navController.addOnDestinationChangedListener { _, destination, _ -> // controller, arguments
            when (destination.id) {
                R.id.nav_home,R.id.storeStatisticsFragment -> {
                    customBarColor(ContextCompat.getColor(this, R.color.black))
                    binding.tvLoginTitle.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.ibBack.setColorFilter(ContextCompat.getColor(this, R.color.black))
                    viewModel?.obsShowToolbar!!.set(false)
                }

                R.id.nav_show_story, R.id.uploadStoryProductFragment, R.id.messageFragment,
                        R.id.imagePreviewFragment, R.id.previewUploadStoryPhotoAndVideoFragment, R.id.messageImagePreviewFragment -> {
                    customBarColor(ContextCompat.getColor(this, R.color.white))
                    binding.tvLoginTitle.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.ibBack.setColorFilter(ContextCompat.getColor(this, R.color.black))
                    viewModel?.obsShowToolbar!!.set(false)
                }

                R.id.nav_reports -> {
                    customBarColor(ContextCompat.getColor(this, R.color.white))
                    binding.tvLoginTitle.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.ibBack.setColorFilter(ContextCompat.getColor(this, R.color.black))
                    viewModel?.obsShowToolbar!!.set(false)
                }

                R.id.nav_wallet -> {
                    customizeToolbar(getString(R.string.wallet),
                        true,
                        ContextCompat.getColor(this, R.color.black)
                    )
                    binding.tvLoginTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
                    binding.ibBack.setColorFilter(ContextCompat.getColor(this, R.color.color_primary))
                    viewModel?.obsShowToolbar!!.set(true)
                    when {
                        PrefMethods.getUserData() != null -> {
                            customBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                        }

                        else -> {
                            customBarColor(ContextCompat.getColor(this, R.color.offers_bg_color))
                        }
                    }
                }

                R.id.nav_contact -> {
                    customBarColor(ContextCompat.getColor(this, R.color.black))
                    binding.tvLoginTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
                    binding.ibBack.setColorFilter(ContextCompat.getColor(this, R.color.white))
                    viewModel?.obsShowToolbar?.set(true)
                }

                R.id.nav_offers, R.id.nav_my_products -> {
                    //customBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    customBarColor(ContextCompat.getColor(this, R.color.white))
                    binding.tvLoginTitle.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.ibBack.setColorFilter(ContextCompat.getColor(this, R.color.black))
                    viewModel?.obsShowToolbar!!.set(true)
                }

                else -> {
                    customBarColor(ContextCompat.getColor(this, R.color.white))
                    binding.tvLoginTitle.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.ibBack.setColorFilter(ContextCompat.getColor(this, R.color.black))
                    viewModel?.obsShowToolbar!!.set(true)
                    //customBarColor(ContextCompat.getColor(this, R.color.white))
                }
            }
        }
    }

    private fun customBarColor(color: Int) {
        binding.toolbar.setBackgroundColor(color)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    private fun initViewModel() {
        if (viewModel == null) {
            viewModel = ViewModelProvider(this@HomeActivity)[MainViewModel::class.java]
        }
    }

    private fun setupNavHeader() {
        // val header: View = navView.getHeaderView(0)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE)
        val binding = NavHeaderMainBinding.inflate(inflater as LayoutInflater, navView, true)
        binding.viewModel = viewModel

        // val btnEdit = header.findViewById<View>(R.id.btn_login) as TextView

        binding.btnLogin.setOnClickListener { // btnEdit
            navController.navigate(R.id.nav_profile)
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun setupNewNavHeader() {
        // Accessing the included layout binding
        val includedBinding: NavHeaderMainBinding = binding.navHeaderMain
        includedBinding.viewModel = viewModel

        includedBinding.btnLogin.setOnClickListener { // btnEdit
            navController.navigate(R.id.nav_profile)
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    fun customizeToolbar(
        title: String,
        show: Boolean = true,
        toolBarColor: Int = ContextCompat.getColor(this, R.color.white)
    ) {
        initViewModel()
        viewModel?.obsTitle!!.set(title)
        viewModel?.obsShowToolbar!!.set(show)
        binding.toolbar.setBackgroundColor(toolBarColor)
    }

    fun customStatusBar(statusBarColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = statusBarColor
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_home_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun showDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun lockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        value.let {
            if (it is Int) {
                when (it) {
                    Codes.BUTTON_LOGIN_CLICKED -> {
                        startActivity(Intent(this, AuthActivity::class.java))
                    }

                    Codes.LOGOUT_CLICK -> {
                        startActivity(Intent(this, AuthActivity::class.java))
                        finishAffinity()
                    }

                    Codes.EDIT_CLICKED -> {
                        navController.navigate(R.id.home_to_profile)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.nav_home -> {
                finishAffinity()
            }

            else -> {
                navController.navigateUp()
            }
        }
    }

    private lateinit var networkConnectionManager: ConnectivityManager
    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback

    private fun initConnectivityManager() {
        networkConnectionManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        networkConnectionCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // there is internet
                try {
                    binding.noWifi.visibility = View.GONE
                } catch (e:Exception)
                {
                    Timber.e(e.toString())
                }
            }

            override fun onLost(network: Network) {
                // there is no internet
                lifecycleScope.launchWhenResumed {
                    delay(1000)
                    binding.noWifi.visibility = View.VISIBLE
                }
            }
        }
        networkConnectionManager.registerDefaultNetworkCallback(networkConnectionCallback)
    }
}