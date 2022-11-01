package com.brandsin.store.ui.menu.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentNotificationsBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.auth.AuthActivity
import com.brandsin.store.utils.PrefMethods
import com.brandsin.user.model.menu.notifications.NotificationItem

class NotificationFragment : BaseHomeFragment(), Observer<Any?>
{
    private lateinit var viewModel: NotificationViewModel
    private lateinit var binding : HomeFragmentNotificationsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_notifications, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        binding.viewModel = viewModel

        setBarName(getString(R.string.notifications))

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)
        viewModel.notificationAdapter.notificationsLiveData.observe(viewLifecycleOwner, this)

        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            viewModel.getUserStatus()
        }
    }

    override fun onChanged(it: Any?)
    {
        when (it) {
            null -> return
            else -> when (it) {
                Codes.LOGIN_CLICKED -> {
                    PrefMethods.saveIsAskedToLogin(true)
                    requireActivity().startActivity(Intent(requireActivity(),
                        AuthActivity::class.java))
                }
                is NotificationItem -> {
//                    viewModel.notificationsList[viewModel.notificationsList.indexOf(it)].readAt == "read"
//                    viewModel.notificationAdapter.updateList(viewModel.notificationsList)
                    viewModel.makeReadNotification(it)
                    when {
                        it.orderId != null -> {
                            val action = NotificationFragmentDirections.notificationToOrderDetails(it.orderId)
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        when {
            PrefMethods.getIsAskedToLogin() -> {
                viewModel.getUserStatus()
                PrefMethods.saveIsAskedToLogin(false)
            }
        }
    }
}