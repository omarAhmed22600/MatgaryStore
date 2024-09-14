package com.brandsin.store.ui.chat.views

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentMessageBinding
import com.brandsin.store.ui.activity.BaseFragment
import com.brandsin.store.ui.chat.adapter.MessagesAdapter
import com.brandsin.store.ui.chat.viewmodel.InboxViewModel
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.longToast
import com.brandsin.store.utils.visible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.Date

class MessageFragment : BaseFragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InboxViewModel by viewModels()

    private lateinit var messagesAdapter: MessagesAdapter

    private var avatarStore: String? = null
    private var avatarUser: String? = null
    private var senderId: String? = null
    private var senderName: String? = null
    private var storeId: String? = null
    private var storeName: String? = null

    private lateinit var message: String

    private val imageClickCallBack: (image: String) -> Unit = { image ->
        val bundle = Bundle()
        bundle.putString("Image_Message", image)
        findNavController().navigate(R.id.messageImagePreviewFragment, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().let {
            avatarStore = it.getString("Avatar_Store")
            avatarUser = it.getString("Avatar_User")
            senderId = it.getString("Sender_Id")
            senderName = it.getString("Sender_Name")
            storeId = it.getString("Store_Id")
            storeName = it.getString("Store_Name")
        }

        viewModel.readMessage(senderId)

        initView()
        setBtnListeners()
        initRecycler()
        subscribeData()
        subscribePermissions()
        subscribeActivityResult()
    }

    private fun initView() {
        if (PrefMethods.getLanguage() == "ar") {
            binding.imgBack.setImageResource(R.drawable.ic_left_arrow)
        } else {
            binding.imgBack.setImageResource(R.drawable.ic_lright_arrow)
        }

        Glide.with(requireContext())
            .load(avatarUser?.trim())
            .apply(RequestOptions().circleCrop())
            .into(binding.userImage)

        binding.userName.text = senderName ?: ""
    }

    private fun initRecycler() {
        binding.progressBar.gone()
        binding.rvMessages.apply {
            messagesAdapter = MessagesAdapter(imageClickCallBack)
            setHasFixedSize(true)
            adapter = messagesAdapter
        }
    }

    private fun setBtnListeners() {
        binding.btnSend.setOnClickListener {
            if (validateData())
                viewModel.sendMessage(avatarUser, senderName, senderId, message, "text")
        }

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSendImage.setOnClickListener {
            viewModel.isImageUploaded = false
            if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                performCameraAndGalleyAction()
            } else launchPermission(storagePermissions)
        }
    }

    private fun subscribeData() {
        viewModel.messagesLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.rvMessages.apply {
                    setHasFixedSize(true)
                    messagesAdapter = MessagesAdapter(imageClickCallBack)
                    if (it.isNotEmpty()) {
                        smoothScrollToPosition(it.size - 1)
                    }

                    // Sort the list of messages by date
                    val sortedMessages = it.sortedBy { it.date.toDate() }.toMutableList()

                    messagesAdapter.submitData(sortedMessages)
                    adapter = messagesAdapter
                }
                binding.progressBar.gone()
            }
        }
    }

    private fun validateData(): Boolean {
        var isValid = true
        message = binding.edtMessage.text?.trim().toString()
        if (binding.edtMessage.text.toString().trim().isEmpty()) {
            isValid = false
        } else binding.edtMessage.setText("")
        return isValid
    }

    private fun subscribeActivityResult() {
        activityResultsDataLive.observe(viewLifecycleOwner) {
            if (it != null) {
                val imagePath: Uri
                try {
                    if (it.data != null) {
                        if (!viewModel.isImageUploaded) {
                            imagePath = it.data!!
                            binding.progressBar.visible()
                            viewModel.uploadImageToSend(
                                imagePath,
                                storeId,
                                senderId,
                                avatarUser,
                                senderName
                            )
                        }
                    } else {
                        longToast(getString(R.string.someThing_went_wrong))
                        viewModel.isImageUploaded = false
                    }

                } catch (ex: Exception) {
                    longToast(getString(R.string.someThing_went_wrong))
                    viewModel.isImageUploaded = false
                }
            }
        }
    }

    private fun subscribePermissions() {
        permissionsResultsLive.observe(viewLifecycleOwner) {
            it?.keys.apply {
                this?.forEach { st ->
                    when (st) {
                        Manifest.permission.READ_EXTERNAL_STORAGE -> {
                            if (it?.get(Manifest.permission.READ_EXTERNAL_STORAGE) == true) {
                                performCameraAndGalleyAction()
                            } else {
                                showExplanation(
                                    getString(R.string.permission_needed),
                                    getString(R.string.picture_permission_explanation),
                                    storagePermissions
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun performCameraAndGalleyAction() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launchActivityForResult(i)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

// Function to convert string date to Date
// Function to convert milliseconds to Date
fun String.toDate(): Date {
    val milliseconds = this.toLong()
    return Date(milliseconds)
}