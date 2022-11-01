package com.brandsin.store.ui.activity.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.androidstudy.networkmanager.Tovuti
import com.google.android.material.navigation.NavigationView
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
import com.brandsin.user.model.constants.Params


class HomeActivity : ParentActivity(), Observer<Any?> {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    var viewModel: MainViewModel? = null
    lateinit var navController: NavController
    val orderClickLiveData = SingleLiveEvent<Any?>()

    var switch_busy: SwitchCompat? = null
    var switch_close: SwitchCompat? = null

    var orderId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
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
                R.id.nav_offers,
                R.id.nav_add_product,
                R.id.nav_my_products,
                R.id.nav_wallet,
                R.id.nav_reports,
                R.id.nav_help,
                R.id.nav_store_code,
                R.id.nav_about,
                R.id.nav_contact
            ), drawerLayout
        )
        // setupActionBarWithNavController(navController, appBarConfiguration)

        Tovuti.from(this).monitor { connectionType, isConnected, isFast ->
            if (isConnected) {
                binding.noWifi.visibility = View.GONE
            } else {
                binding.noWifi.visibility = View.VISIBLE
            }
        }
        setUpToolbarAndStatusBar()
        navView.setupWithNavController(navController)
        setupNavHeader()
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
                    val bundle = Bundle()
                    bundle.putInt(Params.ORDER_ID, it.id!!.toInt())
                    orderClickLiveData.value = null
                    navController.navigate(R.id.nav_order_details, bundle)
                }
            }
        }

        //data from NotificationOpenedHandler
        if (intent.getStringExtra("order_id") != null) {
            orderId = intent.getIntExtra("order_id", -1)
            if (orderId != -1) {
                val bundle = Bundle()
                bundle.putInt(Params.ORDER_ID, orderId)
                navController.navigate(R.id.nav_order_details, bundle)
            }
        }

        switch_busy = navView.menu.findItem(R.id.nav_busy).actionView.findViewById(R.id.switch_busy)
        switch_busy!!.isChecked = PrefMethods.getStoreData()!!.isBusy == 1
        switch_busy!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel!!.setMenuBusy(1)
            } else {
                viewModel!!.setMenuBusy(0)
            }
        }
        switch_close =
            navView.menu.findItem(R.id.nav_closed).actionView.findViewById(R.id.switch_close)
        switch_close!!.isChecked = PrefMethods.getStoreData()!!.isClosed == 1
        switch_close!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel!!.setMenuClosed(1)
            } else {
                viewModel!!.setMenuClosed(0)
            }
        }
    }

    private fun setUpToolbarAndStatusBar() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.nav_home -> {
                    customBarColor(ContextCompat.getColor(this, R.color.black))
                    viewModel?.obsShowToolbar!!.set(false)
                }
                R.id.nav_show_story -> {
                    //customBarColor(ContextCompat.getColor(this, R.color.white))
                    viewModel?.obsShowToolbar!!.set(false)
                }
                R.id.nav_reports->{
                    viewModel?.obsShowToolbar!!.set(false)
                }
                R.id.nav_wallet -> {
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
                    customBarColor(ContextCompat.getColor(this, R.color.payment_color))
                    viewModel?.obsShowToolbar!!.set(true)
                }
                R.id.nav_offers, R.id.nav_my_products -> {
                    //customBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    viewModel?.obsShowToolbar!!.set(true)
                }
                else -> {
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
            viewModel = ViewModelProvider(this@HomeActivity).get(MainViewModel::class.java)
        }
    }

    private fun setupNavHeader() {
        val header: View = navView.getHeaderView(0)
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE)
        val binding = NavHeaderMainBinding.inflate(inflater as LayoutInflater, navView, true);
        binding.viewModel = viewModel

        val btnEdit = header.findViewById<View>(R.id.btn_login) as TextView

        btnEdit.setOnClickListener {
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
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    override fun onChanged(it: Any?) {
        if (it == null) return
        it.let {
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
}