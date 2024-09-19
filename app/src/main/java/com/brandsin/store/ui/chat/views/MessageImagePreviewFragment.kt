package com.brandsin.store.ui.chat.views

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentMessageImagePreviewBinding
import com.bumptech.glide.Glide

class MessageImagePreviewFragment : Fragment() {

    private var _binding: FragmentMessageImagePreviewBinding? = null
    private val binding get() = _binding!!

    private var image: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMessageImagePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image = requireArguments().getString("Image_Message") ?: ""

        initView()
        setBtnListener()
    }

    private fun setBtnListener() {
        binding.icBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initView() {
        Glide.with(requireContext())
            .load(image)
            .error(R.drawable.app_logo)
            .into(binding.imageView)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
