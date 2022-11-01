package com.brandsin.store.ui.dialogs.productunit

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.databinding.DialogProductUnitBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.products.DataItem
import com.brandsin.user.model.constants.Params

class DialogProductUnitFragment : DialogFragment(), Observer<Any?>
{
    lateinit  var  binding: DialogProductUnitBinding
    lateinit var viewModel : ProductUnitViewModel

    var unitList: ArrayList<DataItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            arguments != null -> {
                when {
                    requireArguments().containsKey(Params.PRODUCT_UNIT) -> {
                        unitList = (requireArguments().getSerializable(Params.PRODUCT_UNIT) as ArrayList<DataItem>)
                    }
                }
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DialogProductUnitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ProductUnitViewModel::class.java)
        binding.viewModel = viewModel

        if (unitList.isNotEmpty()) {
            viewModel.unitList = unitList
            viewModel.productUnitAdapter.updateList(unitList)
        }

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        viewModel.productUnitAdapter.orderAddonsLiveData.observe(this, Observer { o ->
            if (o is DataItem) {
                val item: DataItem = o

                val intent = Intent()
                intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                intent.putExtra("productUnitId",item.id.toString())
                intent.putExtra("productUnitName", item.name.toString())
                requireActivity().setResult(Codes.DIALOG_PRODUCT_UNIT_CODE, intent)
                requireActivity().finish()
            }
        })
    }

    override fun onChanged(it: Any?) {
        when (it) {
            null -> return
            else -> when (it) {
//                Codes.CONFIRM_CLICKED -> {
//                    val intent = Intent()
//                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
//                    intent.putIntegerArrayListExtra("productUnitId",productUnitId)
//                    intent.putExtra("productUnitNames", productUnitNames.joinToString { it -> "$it" })
//                    requireActivity().setResult(Codes.DIALOG_PRODUCT_UNIT_CODE, intent)
//                    requireActivity().finish()
//                }
//                Codes.CANCEL_CLICKED -> {
//                    val intent = Intent()
//                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
//                    requireActivity().setResult(Codes.DIALOG_PRODUCT_UNIT_CODE, intent)
//                    requireActivity().finish()
//                }
            }
        }
    }
}