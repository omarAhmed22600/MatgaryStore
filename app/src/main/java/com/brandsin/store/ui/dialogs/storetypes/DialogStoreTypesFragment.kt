package com.brandsin.store.ui.dialogs.storetypes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.databinding.DialogStoreTypeBinding
import com.brandsin.store.model.auth.StoreTypeItem
import com.brandsin.store.model.auth.StoreTypeResponse
import com.brandsin.store.model.auth.register.StoreRegister
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.updatestore.UpdateStoreRequest
import com.brandsin.user.model.constants.Params

class DialogStoreTypesFragment  : DialogFragment(), Observer<Any?>
{
    lateinit  var  binding: DialogStoreTypeBinding
    lateinit var viewModel : StoreTypesViewModel
    var response = StoreTypeResponse()

    var storeRequest = StoreRegister()
    var updateRequest = UpdateStoreRequest()
    var storeCategoryNames = ArrayList<String>()
    var storeCategoryId = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            arguments != null -> {
                when {
                    requireArguments().containsKey(Params.STORE_CATEGORIES) -> {
                        response = (requireArguments().getSerializable(Params.STORE_CATEGORIES) as StoreTypeResponse?)!!
                        if (requireArguments().getSerializable(Params.STORE_CATEGORIES_DATA)!=null){
                            storeRequest= requireArguments().getSerializable(Params.STORE_CATEGORIES_DATA) as StoreRegister
                        }
                        if (requireArguments().getSerializable(Params.STORE_CATEGORIES_DATA_EDIT)!=null){
                            updateRequest= requireArguments().getSerializable(Params.STORE_CATEGORIES_DATA_EDIT) as UpdateStoreRequest
                        }
                    }
                }
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DialogStoreTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(StoreTypesViewModel::class.java)
        binding.viewModel = viewModel

        if (response.data !=null) {
            viewModel.typesList = response.data as ArrayList<StoreTypeItem>
            viewModel.typesAdapter.updateList(
                response.data as ArrayList<StoreTypeItem>,
                storeRequest,updateRequest
            )
        }

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        viewModel.typesAdapter.orderAddonsLiveData.observe(this, Observer { o ->
            if (o is StoreTypeItem) {
                val item: StoreTypeItem = o
                if (!storeCategoryId.contains(item.id)){
                    storeCategoryId.add(item.id!!)
                    storeCategoryNames.add(item.name!!)
                }else{
                    storeCategoryId.remove(item.id!!)
                    storeCategoryNames.remove(item.name!!)
                }
            }
        })
    }

    override fun onChanged(it: Any?) {
        when (it) {
            null -> return
            else -> when (it) {
                Codes.CONFIRM_CLICKED -> {
                    val intent = Intent()
                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                    intent.putIntegerArrayListExtra("storeCategoryId",storeCategoryId)
                    intent.putExtra("storeCategoryNames", storeCategoryNames.joinToString { it -> "$it" })
                    requireActivity().setResult(Codes.DIALOG_STORE_CATEGORY_CODE, intent)
                    requireActivity().finish()
                }
                Codes.CANCEL_CLICKED -> {
                    val intent = Intent()
                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
                    requireActivity().setResult(Codes.DIALOG_STORE_CATEGORY_CODE, intent)
                    requireActivity().finish()
                }
            }
        }
    }
}