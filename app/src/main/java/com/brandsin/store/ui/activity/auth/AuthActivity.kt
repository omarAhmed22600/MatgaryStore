package com.brandsin.store.ui.activity.auth

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityAuthBinding
import com.brandsin.store.model.constants.Const
import com.brandsin.store.ui.activity.ParentActivity
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.utils.LocalUtil
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class AuthActivity : ParentActivity() {

    private lateinit var binding: ActivityAuthBinding
    var viewModel: AuthViewModel? = null
    private lateinit var navController: NavController

    private var orderId = -1
    private var chatId = -1
    private var refundableId = -1
    private var shouldNavigate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        LocalUtil.changeLanguage(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)

        // Init view model
        initViewModel()
        binding.viewModel = viewModel

        navController = findNavController(R.id.nav_auth_host_fragment)

        initConnectivityManager()

        // Data from NotificationOpenedHandler
        if (intent.getStringExtra("chat_id") != null) {
            Timber.e("chat")
            chatId = intent.getStringExtra("chat_id")?.toInt()?:-1
            shouldNavigate = true
        } else if (intent.getStringExtra("order_id") != null) {
            Timber.e("order")
            orderId = intent.getStringExtra("order_id")?.toInt()?:-1
            shouldNavigate = true

        } else if (intent.getStringExtra("refundable_id") != null) {
            Timber.e("refund")
            refundableId = intent.getStringExtra("refundable_id")?.toInt()?:-1
            shouldNavigate = true

        }

        binding.ibBack.setOnClickListener {
            navController.navigateUp()
        }

        viewModel!!.clickableLiveData.observe(this) {
            viewModel!!.obsIsClickable.set(false)
            lifecycleScope.launch {
                delay(2000)
                viewModel!!.clickableLiveData.value = false
                viewModel!!.obsIsClickable.set(true)
            }
        }

        when {
            intent.hasExtra(Const.ACCESS_LOGIN) -> {
                when {
                    intent.getBooleanExtra(Const.ACCESS_LOGIN, false) -> {

                        navController.navigate(R.id.navigation_login)
                    }
                }
            }
        }

        setUpToolbarAndStatusBar()
        startIntent()
    }

    private fun initViewModel() {
        when (viewModel) {
            null -> {
                viewModel = ViewModelProvider(this@AuthActivity).get(AuthViewModel::class.java)
            }
        }
    }

    private fun setUpToolbarAndStatusBar() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.navigation_splash, R.id.navigation_login_ways -> {
                    //customBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                    viewModel?.obsShowToolbar!!.set(false)
                }

                else -> {
                    viewModel?.obsShowToolbar!!.set(true)
                    //customBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                }
            }
        }
    }

    private fun customBarColor(color: Int) {
        binding.toolbar.setBackgroundColor(color)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val window: Window = window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = color
            }
        }
    }

    fun setBarName(title: String) {
        initViewModel()
        viewModel?.obsTitle!!.set(title)
    }

    private fun startIntent() {
        if (shouldNavigate) {
            if (PrefMethods.getLoginState()) {
                var intent = Intent(this@AuthActivity, HomeActivity::class.java)
                if (orderId != -1)
                    intent.putExtra("order_id", orderId)
                if (chatId != -1)
                    intent.putExtra("chat_id", chatId)
                if (refundableId != -1)
                    intent.putExtra("refundable_id", refundableId)
                startActivity(intent)
                finish()
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
                } catch (e:Exception) {
                    Timber.e(e.stackTraceToString())
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