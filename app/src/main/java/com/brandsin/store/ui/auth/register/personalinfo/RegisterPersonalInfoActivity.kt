package com.brandsin.store.ui.auth.register.personalinfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityRegisterPersonalInfoBinding
import com.brandsin.store.model.auth.register.UserRegister
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.store.model.constants.Params
import com.brandsin.user.utils.map.PermissionUtil
import com.bumptech.glide.Glide
import com.fxn.pix.Options
import com.fxn.pix.Pix
import timber.log.Timber
import java.io.File

class RegisterPersonalInfoActivity : AppCompatActivity(), Observer<Any?> {

    private lateinit var binding: ActivityRegisterPersonalInfoBinding

    lateinit var viewModel: RegisterPersonalInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_personal_info)

        viewModel = ViewModelProvider(this)[RegisterPersonalInfoViewModel::class.java]
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(this, this)

        when {
            intent.hasExtra(Params.USER_REGISTER) -> {
                when {
                    intent.getSerializableExtra(Params.USER_REGISTER) != null -> {
                        val userData: UserRegister =
                            intent.extras!!.getSerializable(Params.USER_REGISTER) as UserRegister
                        viewModel.userData = userData

                        binding.notOfferImg.visibility = View.GONE
                        binding.ivPhoto.visibility = View.VISIBLE
                        Glide.with(this).load(userData.userIdImgUri).into(binding.ivUserImg)
                        viewModel.userData.userIdImg = userData.userIdImg
                        viewModel.userData.userIdImgUri = userData.userIdImgUri
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
                    Codes.SELECT_IMAGES -> {
                        pickImage(Codes.PERSONAL_IMG)
                    }

                    Codes.NAME_EMPTY -> {
                        showToast(getString(R.string.please_enter_your_first_name), 1)
                    }

                    Codes.EMPTY_LAST_NAME -> {
                        showToast(getString(R.string.please_enter_your_last_name), 1)
                    }

                    Codes.EMPTY_PHONE -> {
                        showToast(getString(R.string.please_enter_your_phone), 1)
                    }

                    Codes.INVALID_PHONE -> {
                        showToast(getString(R.string.invalid_phone), 1)
                    }

                    Codes.EMAIL_EMPTY -> {
                        showToast(getString(R.string.please_enter_your_email), 1)
                    }

                    Codes.EMAIL_INVALID -> {
                        showToast(getString(R.string.email_must_correct), 1)
                    }

                    Codes.EMPTY_IMAGE -> {
                        showToast(getString(R.string.please_enter_your_image), 1)
                    }

                    Codes.SUCCESS -> {
                        val intent = Intent()
                        intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                        intent.putExtra(Params.USER_REGISTER, viewModel.userData)
                        setResult(Codes.REGISTER_PERSONALINFO_REQUEST_CODE, intent)
                        finish()
                        Timber.e("Mou3aaaaaz : " + viewModel.userData.toString())
                    }

                    Codes.BACK_PRESSED -> {
                        val intent = Intent()
                        intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
                        setResult(Codes.REGISTER_PERSONALINFO_REQUEST_CODE, intent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Codes.PERSONAL_IMG -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notOfferImg.visibility = View.GONE
                                binding.ivPhoto.visibility = View.VISIBLE
                                binding.ivUserImg.setImageURI(array[0].toUri())
                                viewModel.userData.userIdImg = File(array[0])
                                viewModel.userData.userIdImgUri = array[0]
                            }
                        }
                    }
                }
            }
        }
    }

    private fun pickImage(requestCode: Int) {
        val options = Options.init()
            .setRequestCode(requestCode) //Request code for activity results
            .setFrontfacing(false) //Front Facing camera on start
            .setExcludeVideos(false) //Option to exclude videos
            .setMode(Options.Mode.Picture)
        if (PermissionUtil.hasImagePermission(this)) {
            Pix.start(this, options)
        } else {
            observe(
                PermissionUtil.requestPermission(
                    this,
                    PermissionUtil.getImagePermissions()
                )
            ) {
                when (it) {
                    PermissionUtil.AppPermissionResult.AllGood -> Pix.start(
                        this,
                        options
                    )

                    else -> {}
                }
            }
        }
    }
}