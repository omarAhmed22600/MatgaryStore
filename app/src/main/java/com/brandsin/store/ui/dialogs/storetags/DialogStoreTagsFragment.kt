package com.brandsin.store.ui.dialogs.storetags

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.databinding.DialogStoreTagBinding
import com.brandsin.store.model.auth.StoreTagsItem
import com.brandsin.store.model.auth.StoreTagsResponse
import com.brandsin.store.model.auth.register.StoreRegister
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.updatestore.UpdateStoreRequest

import com.brandsin.user.model.constants.Params

class DialogStoreTagsFragment : DialogFragment(), Observer<Any?>
{
    lateinit  var  binding: DialogStoreTagBinding
    lateinit var viewModel : StoreTagsViewModel
    var response = StoreTagsResponse()

    var storeRequest = StoreRegister()
    var updateRequest = UpdateStoreRequest()
    var storeTagNames = ArrayList<String>()
    var storeTagId = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            arguments != null -> {
                when {
                    requireArguments().containsKey(Params.STORE_TAGS) -> {
                        response = (requireArguments().getSerializable(Params.STORE_TAGS) as StoreTagsResponse?)!!
                        if (requireArguments().getSerializable(Params.STORE_TAGS_DATA)!=null){
                            storeRequest= requireArguments().getSerializable(Params.STORE_TAGS_DATA) as StoreRegister
                        }
                        if (requireArguments().getSerializable(Params.STORE_TAGS_DATA_EDIT)!=null){
                            updateRequest= requireArguments().getSerializable(Params.STORE_TAGS_DATA_EDIT) as UpdateStoreRequest
                        }
                    }
                }
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DialogStoreTagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(StoreTagsViewModel::class.java)
        binding.viewModel = viewModel

        if (response.data !=null) {
            viewModel.tagsList = response.data as ArrayList<StoreTagsItem>
            viewModel.tagsAdapter.updateList(
                    response.data as ArrayList<StoreTagsItem>,
                    storeRequest,updateRequest
            )
        }

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        viewModel.tagsAdapter.orderAddonsLiveData.observe(this, Observer { o ->
            if (o is StoreTagsItem) {
                val item: StoreTagsItem = o
                if (!storeTagId.contains(item.id)){
                    storeTagId.add(item.id!!)
                    storeTagNames.add(item.name!!)
                }else{
                    storeTagId.remove(item.id!!)
                    storeTagNames.remove(item.name!!)
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
                    intent.putIntegerArrayListExtra("storeTagId",storeTagId)
                    intent.putExtra("storeTagNames", storeTagNames.joinToString { it -> "$it" })
                    requireActivity().setResult(Codes.DIALOG_STORE_TAGS_CODE, intent)
                    requireActivity().finish()
                }
                Codes.CANCEL_CLICKED -> {
                    val intent = Intent()
                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
                    requireActivity().setResult(Codes.DIALOG_STORE_TAGS_CODE, intent)
                    requireActivity().finish()
                }
            }
        }
    }
}