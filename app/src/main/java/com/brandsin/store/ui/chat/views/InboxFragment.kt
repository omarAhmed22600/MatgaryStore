package com.brandsin.store.ui.chat.views

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentInboxBinding
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.chat.adapter.InboxAdapter
import com.brandsin.store.ui.chat.viewmodel.InboxViewModel
import com.brandsin.store.utils.BasePushNotificationService
import com.brandsin.store.utils.PrefMethods
import com.brandsin.user.ui.chat.model.MessageModel
import timber.log.Timber

class InboxFragment : BaseHomeFragment() {

    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InboxViewModel by viewModels()

    private lateinit var inboxAdapter: InboxAdapter

    private val onItemClickCallBack: (inboxItem: MessageModel) -> Unit = { inbox ->
        // viewModel.messageItem = inbox
        val bundle = Bundle()
        bundle.putString("Avatar_Store", inbox.avaterstore)
        bundle.putString("Avatar_User", inbox.avateruser)
        bundle.putString("Sender_Id", inbox.senderId)
        bundle.putString("Sender_Name", inbox.senderName)
        bundle.putString("Store_Id", PrefMethods.getStoreData()?.userId.toString())
        bundle.putString("Store_Name", inbox.storename)
        findNavController().navigate(R.id.messageFragment, bundle)
    }
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Timber.e("investigate1 -> on receive chat ")
            if (intent?.action.orEmpty() == BasePushNotificationService.REFRESH_CHAT) {
                Timber.e("investigate1 -> on receive update chat")
                viewModel.readChat()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.chat))

        viewModel.readChat()

        initRecycler()
        setBtnListener()
        subscribeData()
    }

    override fun onStart() {
        super.onStart()
        viewModel.readChat()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().registerReceiver(
                broadcastReceiver,
                IntentFilter(BasePushNotificationService.REFRESH_CHAT),
                Context.RECEIVER_EXPORTED
            )
        } else {
            requireActivity().registerReceiver(
                broadcastReceiver,
                IntentFilter(BasePushNotificationService.REFRESH_CHAT),
            )
        }
        viewModel.readChat()
    }

    private fun initRecycler() {
        binding.rvInboxes.apply {
            inboxAdapter = InboxAdapter(onItemClickCallBack)
            adapter = inboxAdapter
        }
    }

    private fun setBtnListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                inboxAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun subscribeData() {
        viewModel.usersInboxLive.observe(viewLifecycleOwner) {
            // hideLoadingIndicator()
            if (it.isNotEmpty()) {
                inboxAdapter.submitData(it)
            }
        }
    }
    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(broadcastReceiver)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}