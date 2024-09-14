package com.brandsin.store.ui.auth.register.bankAccountInfo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityRegisterBankAccountInfoBinding
import com.brandsin.store.model.auth.register.StoreRegister
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.model.constants.Params

class RegisterBankAccountInfoActivity : AppCompatActivity(), Observer<Any?> {

    private lateinit var binding: ActivityRegisterBankAccountInfoBinding

    lateinit var viewModel: RegisterBankAccountInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_bank_account_info)

        // Initialize view model
        viewModel = ViewModelProvider(this)[RegisterBankAccountInfoViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.mutableLiveData.observe(this, this)

        when {
            intent.hasExtra(Params.STORE_REGISTER) -> {
                when {
                    intent.getSerializableExtra(Params.STORE_REGISTER) != null -> {
                        val storeData: StoreRegister =
                            intent.extras?.getSerializable(Params.STORE_REGISTER) as StoreRegister
                        viewModel.storeRequest = storeData
                    }
                }
            }
        }
    }

    override fun onChanged(value: Any?) {
        when (value) {
            null -> return
            else -> value.let {
                when (it) {
                    Codes.EMPTY_OWNER_NAME -> {
                        showToast(getString(R.string.enter_the_name_of_the_account_holder), 1)
                    }

                    Codes.EMPTY_BANK_NAME -> {
                        showToast(getString(R.string.enter_the_bank_name), 1)
                    }

                    Codes.EMPTY_IBAN -> {
                        showToast(getString(R.string.enter_iban), 1)
                    }

                    Codes.SUCCESS -> {
                        val intent = Intent()
                        intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                        intent.putExtra(Params.STORE_REGISTER, viewModel.storeRequest)
                        setResult(Codes.REGISTER_BANKING_DATA_INFO_REQUEST_CODE, intent)
                        finish()
                    }

                    Codes.BACK_PRESSED -> {
                        val intent = Intent()
                        intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
                        setResult(Codes.REGISTER_BANKING_DATA_INFO_REQUEST_CODE, intent)
                        finish()
                    }
                }
            }
        }
    }

    fun showToast(msg: String, type: Int) {
        val bundle = Bundle()
        bundle.putString(Params.DIALOG_TOAST_MESSAGE, msg)
        bundle.putInt(Params.DIALOG_TOAST_TYPE, type)
        Utils.startDialogActivity(
            this,
            DialogToastFragment::class.java.name,
            Codes.DIALOG_TOAST_REQUEST,
            bundle
        )
    }
}