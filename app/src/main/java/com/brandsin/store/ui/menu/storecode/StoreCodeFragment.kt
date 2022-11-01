package com.brandsin.store.ui.menu.storecode

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.MenuFragmentStoreCodeBinding
import com.brandsin.store.ui.activity.BaseHomeFragment

class StoreCodeFragment: BaseHomeFragment(), Observer<Any?>
{
    private lateinit var viewModel: StoreCodeViewModel
    private lateinit var binding: MenuFragmentStoreCodeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.menu_fragment_store_code, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(StoreCodeViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.getTxt2()
        setBarName(getString(R.string.store_codes))

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        binding.scodeCopy.setOnClickListener {
            val textToCopy = viewModel.code
            val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", textToCopy)
            clipboardManager.setPrimaryClip(clipData)
            showToast("copied !!", 2)
        }

        binding.scodeShare.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, viewModel.code)
            startActivity(Intent.createChooser(shareIntent,getString(R.string.send_to)))
        }
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        when (it) {

        }
    }
}