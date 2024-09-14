package com.brandsin.store.ui.dialogs.storeDeliveryType

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.brandsin.store.R
import com.brandsin.store.databinding.DialogDeliveryTypeBinding

class DeliveryTypeDialog(
    private val btnChooseTypeOrderCallback: (type: String) -> Unit,
) : DialogFragment() {

    private var _binding: DialogDeliveryTypeBinding? = null
    private val binding get() = _binding!!

    // private val viewModel: RegisterStoreInfoViewModel by activityViewModels()

    private lateinit var deliveryTypeAdapter: DeliveryTypeAdapter

    private val btnItemClickCallBack: (type: String) -> Unit =
        { type ->
            // viewModel.storeRequest.storeDeliveryType = type
            // viewModel.obsType.set(type)
            btnChooseTypeOrderCallback.invoke(type)
            dismiss()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DialogDeliveryTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.rvDeliveryTypes.apply {
            deliveryTypeAdapter = DeliveryTypeAdapter(btnItemClickCallBack)
            deliveryTypeAdapter.submitData(getDeliveryTypesList())
            adapter = deliveryTypeAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDeliveryTypesList(): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()

        list.add(getString(R.string.minute))
        list.add(getString(R.string.hour))
        list.add(getString(R.string.day))

        return list
    }

}