package com.brandsin.store.ui.chat.views

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
import com.brandsin.user.ui.chat.model.MessageModel

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
        bundle.putString("Store_Id", inbox.storeId)
        bundle.putString("Store_Name", inbox.storename)
        findNavController().navigate(R.id.messageFragment, bundle)
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

    override fun onResume() {
        super.onResume()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}