package com.brandsin.store.ui.auth.register.password

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityRegisterPassBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.model.constants.Params

class RegisterPassActivity : AppCompatActivity(), Observer<Any?> {

    private lateinit var binding: ActivityRegisterPassBinding
    lateinit var viewModel: RegisterPassViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_pass)
        viewModel = ViewModelProvider(this).get(RegisterPassViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(this, this)

        when {
            intent.hasExtra(Params.STORE_PASSWORD) -> {
                when {
                    intent.getStringExtra(Params.STORE_PASSWORD) != null -> {
                        viewModel.obsPassword.set(intent.getStringExtra(Params.STORE_PASSWORD))
                        viewModel.obsConfirmPassword.set(intent.getStringExtra(Params.STORE_PASSWORD))
                    }
                }
            }
        }
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        value.let {
            when (it) {
                Codes.PASSWORD_EMPTY -> {
                    showToast(getString(R.string.please_enter_your_password), 1)
                }

                Codes.PASSWORD_SHORT -> {
                    showToast(getString(R.string.short_password), 1)
                }

                Codes.CONFIRM_PASS_EMPTY -> {
                    showToast(getString(R.string.enter_confirm_password), 1)
                }

                Codes.PASSWORD_NOT_MATCH -> {
                    showToast(getString(R.string.password_not_match), 1)
                }

                Codes.SUCCESS -> {
                    val intent = Intent()
                    intent.putExtra(Params.STORE_PASSWORD, viewModel.obsPassword.get())
                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                    setResult(Codes.REGISTER_PASSWORD_REQUEST_CODE, intent)
                    finish()
                }

                Codes.BACK_PRESSED -> {
                    val intent = Intent()
                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
                    setResult(Codes.REGISTER_PASSWORD_REQUEST_CODE, intent)
                    finish()
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