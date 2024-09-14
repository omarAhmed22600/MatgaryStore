package com.brandsin.store.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.AuthFragmentRegisterBinding
import com.brandsin.store.model.auth.register.RegisterResponse
import com.brandsin.store.model.auth.register.StoreRegister
import com.brandsin.store.model.auth.register.UserRegister
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.auth.BaseAuthFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.auth.register.bankAccountInfo.RegisterBankAccountInfoActivity
import com.brandsin.store.ui.auth.register.password.RegisterPassActivity
import com.brandsin.store.ui.auth.register.personalinfo.RegisterPersonalInfoActivity
import com.brandsin.store.ui.auth.register.storeinfo.RegisterStoreInfoActivity
import com.brandsin.store.utils.observe
import com.brandsin.store.model.constants.Params
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import timber.log.Timber

class RegisterFragment : BaseAuthFragment(), Observer<Any?> {

    private lateinit var binding: AuthFragmentRegisterBinding

    lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.auth_fragment_register, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding.viewModel = viewModel

        setBarName(getString(R.string.create_account))

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        observe(viewModel.apiResponseLiveData) {
            when (it.status) {
                Status.ERROR_MESSAGE -> {
                    showToast(it.message.toString(), 1)
                }

                Status.SUCCESS_MESSAGE -> {
                    showToast(it.message.toString(), 2)
                }

                Status.SUCCESS -> {
                    when (it.data) {
                        is RegisterResponse -> {
                            startActivity(Intent(requireActivity(), HomeActivity::class.java))
                            requireActivity().finishAffinity()
                        }
                    }
                }

                else -> {
                    Timber.e(it.message)
                }
            }
        }

        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result.token
                viewModel.deviceTokenRequest.token = token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
            })
        // [END retrieve_current_token]
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        value.let {
            when (it) {
                Codes.COMPLETE_STORE_DATA -> {
                    showToast(getString(R.string.please_complete_store_data), 1)
                }

                Codes.COMPLETE_USER_DATA -> {
                    showToast(getString(R.string.please_complete_user_data), 1)
                }

                Codes.COMPLETE_BANKING_DATA -> {
                    showToast(getString(R.string.please_complete_banking_data), 1)
                }

                Codes.PASSWORD_EMPTY -> {
                    showToast(getString(R.string.please_select_your_password), 1)
                }

                Codes.SHOW_CONDITIONS -> {
                    findNavController().navigate(R.id.register_to_condition)
                }

                Codes.CREATE_STORE -> {
                    requireActivity().startActivity(
                        Intent(
                            requireActivity(),
                            HomeActivity::class.java
                        )
                    )
                    requireActivity().finishAffinity()
                }

                Codes.OPEN_STORE_INFO_ACTIVITY -> {
                    val intent = Intent(requireActivity(), RegisterStoreInfoActivity::class.java)

                    when {
                        viewModel.obsIsStoreInfoEntered.get() == true -> {
                            val storeData = StoreRegister()
                            storeData.storeLat = viewModel.registerRequest.storeLat
                            storeData.storeLng = viewModel.registerRequest.storeLng
                            storeData.storeAddress = viewModel.registerRequest.storeAddress
                            storeData.storeName = viewModel.registerRequest.storeName
                            storeData.storePhoneNumber = viewModel.registerRequest.storePhoneNumber
                            storeData.storeWhatsApp = viewModel.registerRequest.storeWhatsApp
                            storeData.hasDelivery = viewModel.registerRequest.hasDelivery
                            storeData.pickUpFromStore = viewModel.registerRequest.pickUpFromStore
                            storeData.cashOnDelivery = viewModel.registerRequest.cashOnDelivery
                            storeData.storeDeliveryDistance =
                                viewModel.registerRequest.storeDeliveryDistance
                            storeData.storeDeliveryPrice =
                                viewModel.registerRequest.storeDeliveryPrice
                            storeData.storeDeliveryTime = viewModel.registerRequest.storeDeliveryTime
                            storeData.storeDeliveryType = viewModel.registerRequest.storeDeliveryType
                            storeData.categories = viewModel.registerRequest.categories
                            storeData.tags = viewModel.registerRequest.tags
                            storeData.storeMinOrderPrice =
                                viewModel.registerRequest.storeMinOrderPrice
                            storeData.storeImg = viewModel.registerRequest.storeImg
                            storeData.storeImgUri = viewModel.registerRequest.storeImgUri
                            storeData.storeThumb = viewModel.registerRequest.storeThumb
                            storeData.storeThumbUri = viewModel.registerRequest.storeThumbUri
                            storeData.checkDelivery = viewModel.registerRequest.checkDelivery
                            storeData.checkCondition = viewModel.registerRequest.checkCondition

                            Log.d("storeData", "storeData == $storeData")

                            intent.putExtra(Params.STORE_REGISTER, storeData)
                        }
                    }
                    startActivityForResult(intent, Codes.REGISTER_STOREINFO_REQUEST_CODE)
                }

                Codes.OPEN_PERSONAL_INFO_ACTIVITY -> {
                    val intent = Intent(requireActivity(), RegisterPersonalInfoActivity::class.java)
                    when {
                        viewModel.obsIsPersonalInfoEntered.get() == true -> {
                            val userData = UserRegister()
                            userData.userName = viewModel.registerRequest.userName
                            userData.userLastName = viewModel.registerRequest.userLastName
                            userData.userEmail = viewModel.registerRequest.userEmail
                            userData.userPhone = viewModel.registerRequest.userPhone
                            userData.userIdImg = viewModel.registerRequest.userIdImg
                            userData.userIdImgUri = viewModel.registerRequest.userIdImgUri
                            intent.putExtra(Params.USER_REGISTER, userData)
                        }
                    }
                    startActivityForResult(intent, Codes.REGISTER_PERSONALINFO_REQUEST_CODE)
                }

                Codes.OPEN_BANK_ACCOUNT_INFO_ACTIVITY -> {
                    val intent = Intent(requireActivity(), RegisterBankAccountInfoActivity::class.java)
                    when {
                        viewModel.obsIsBankingDataInfoEntered.get() == true -> {
                            val storeData = StoreRegister()
                            storeData.storeOwnerName = viewModel.registerRequest.storeOwnerName
                            storeData.storeBankName = viewModel.registerRequest.storeBankName
                            storeData.storeIban = viewModel.registerRequest.storeIban

                            intent.putExtra(Params.STORE_REGISTER, storeData)
                        }
                    }
                    startActivityForResult(intent, Codes.REGISTER_BANKING_DATA_INFO_REQUEST_CODE)
                }

                Codes.OPEN_PASSWORD_ACTIVITY -> {
                    val intent = Intent(requireActivity(), RegisterPassActivity::class.java)
                    when {
                        viewModel.registerRequest.userPassword != null -> {
                            intent.putExtra(
                                Params.STORE_PASSWORD,
                                viewModel.registerRequest.userPassword
                            )
                        }
                    }
                    startActivityForResult(intent, Codes.REGISTER_PASSWORD_REQUEST_CODE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == Codes.REGISTER_STOREINFO_REQUEST_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 1 -> {
                            viewModel.obsIsStoreInfoEntered.set(true)
                            val storeData =
                                data.getSerializableExtra(Params.STORE_REGISTER) as StoreRegister
                            viewModel.registerRequest.storeName = storeData.storeName
                            viewModel.registerRequest.storePhoneNumber = storeData.storePhoneNumber
                            viewModel.registerRequest.storeWhatsApp = storeData.storeWhatsApp
                            viewModel.registerRequest.storeMinOrderPrice =
                                storeData.storeMinOrderPrice
                            viewModel.registerRequest.storeDeliveryDistance =
                                storeData.storeDeliveryDistance
                            viewModel.registerRequest.storeDeliveryTime = storeData.storeDeliveryTime
                            viewModel.registerRequest.storeDeliveryType = storeData.storeDeliveryType
                            viewModel.registerRequest.storeDeliveryPrice =
                                storeData.storeDeliveryPrice
                            viewModel.registerRequest.hasDelivery = storeData.hasDelivery
                            viewModel.registerRequest.pickUpFromStore = storeData.pickUpFromStore
                            viewModel.registerRequest.cashOnDelivery = storeData.cashOnDelivery
                            viewModel.registerRequest.checkDelivery = storeData.checkDelivery
                            viewModel.registerRequest.checkCondition = storeData.checkCondition
                            viewModel.registerRequest.storeAddress = storeData.storeAddress
                            viewModel.registerRequest.storeLat = storeData.storeLat
                            viewModel.registerRequest.storeLng = storeData.storeLng
                            viewModel.registerRequest.storeImg = storeData.storeImg
                            viewModel.registerRequest.storeThumb = storeData.storeThumb
                            viewModel.registerRequest.categories = storeData.categories
                            viewModel.registerRequest.tags = storeData.tags
                            viewModel.registerRequest.storeImgUri = storeData.storeImgUri
                            viewModel.registerRequest.storeThumbUri = storeData.storeThumbUri
                            // viewModel.registerRequest.storeImg = storeData.storeImg
                            // viewModel.registerRequest.storeThumb = storeData.storeThumb

                            Log.d("storeData", "storeData == $storeData")
                        }
                        /*data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 0 -> {
                            viewModel.obsIsStoreInfoEntered.set(false)
                        }*/
                    }
                }
            }

            requestCode == Codes.REGISTER_PERSONALINFO_REQUEST_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 1 -> {
                            viewModel.obsIsPersonalInfoEntered.set(true)

                            val userData =
                                data.getSerializableExtra(Params.USER_REGISTER) as UserRegister
                            viewModel.registerRequest.userName = userData.userName
                            viewModel.registerRequest.userLastName = userData.userLastName
                            viewModel.registerRequest.userPhone = userData.userPhone
                            viewModel.registerRequest.userEmail = userData.userEmail
                            viewModel.registerRequest.userIdImg = userData.userIdImg
                            viewModel.registerRequest.userIdImgUri = userData.userIdImgUri
                        }
//                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 0 -> {
//                            viewModel.obsIsPersonalInfoEntered.set(false)
//                        }
                    }
                }
            }

            requestCode == Codes.REGISTER_BANKING_DATA_INFO_REQUEST_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 1 -> {
                            viewModel.obsIsBankingDataInfoEntered.set(true)

                            val storeData =
                                data.getSerializableExtra(Params.STORE_REGISTER) as StoreRegister

                            viewModel.registerRequest.storeOwnerName = storeData.storeOwnerName
                            viewModel.registerRequest.storeBankName = storeData.storeBankName
                            viewModel.registerRequest.storeIban = storeData.storeIban
                        }
                    }
                }
            }

            requestCode == Codes.REGISTER_PASSWORD_REQUEST_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 1 -> {
                            viewModel.obsIsPasswordEntered.set(true)
                            viewModel.registerRequest.userPassword =
                                data.getStringExtra(Params.STORE_PASSWORD)
                        }
                        /*data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 0 -> {
                            viewModel.obsIsPasswordEntered.set(false)
                        }*/
                    }
                }
            }
        }
    }
}