package com.brandsin.store.ui.main.refundableProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentImagePreviewBinding
import com.bumptech.glide.Glide

class ImagePreviewFragment : Fragment() {

    private var _binding: FragmentImagePreviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var receivedImage: String

    /*companion object {
        fun newInstance(bundle: Bundle): ImagePreviewFragment {
            val fragment = ImagePreviewFragment()
            fragment.arguments = bundle
            return fragment
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentImagePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receivedImage = requireArguments().getString("IMAGE") ?: ""

        initView()
        setBtnListener()
    }

    private fun initView() {
        Glide.with(requireContext())
            .load(receivedImage)
            .error(R.drawable.app_logo)
            .into(binding.photoView)
    }

    private fun setBtnListener() {
        binding.icBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}