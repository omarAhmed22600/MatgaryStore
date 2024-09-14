package com.brandsin.store.ui.auth.register.storeinfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityRegisterStoreInfoBinding
import com.brandsin.store.model.UserLocation
import com.brandsin.store.model.auth.register.StoreRegister
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.auth.condition.ConditionActivity
import com.brandsin.store.ui.activity.map.MapsActivity
import com.brandsin.store.ui.dialogs.storeDeliveryType.DeliveryTypeDialog
import com.brandsin.store.ui.dialogs.storetags.DialogStoreTagsFragment
import com.brandsin.store.ui.dialogs.storetypes.DialogStoreTypesFragment
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.store.model.constants.Params
import com.brandsin.user.utils.map.PermissionUtil
import com.bumptech.glide.Glide
import com.fxn.pix.Options
import com.fxn.pix.Pix
import java.io.File

class RegisterStoreInfoActivity : AppCompatActivity(), Observer<Any?> {

    private lateinit var binding: ActivityRegisterStoreInfoBinding
    lateinit var viewModel: RegisterStoreInfoViewModel

    private var storeCategoryNames = ArrayList<String>()
    private var storeTagsNames = ArrayList<String>()

    private val btnChooseTypeOrderCallback: (type: String) -> Unit =
        { type ->
            when (type) {
                getString(R.string.minute) -> {
                    viewModel.storeRequest.storeDeliveryType = getString(R.string.minute)
                    viewModel.obsType.set(getString(R.string.minute))
                }

                getString(R.string.hour) -> {
                    viewModel.storeRequest.storeDeliveryType = getString(R.string.hour)
                    viewModel.obsType.set(getString(R.string.hour))
                }

                getString(R.string.day) -> {
                    viewModel.storeRequest.storeDeliveryType = getString(R.string.day)
                    viewModel.obsType.set(getString(R.string.day))
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_store_info)

        viewModel = ViewModelProvider(this)[RegisterStoreInfoViewModel::class.java]
        binding.viewModel = viewModel
        viewModel.mutableLiveData.observe(this, this)

        viewModel.getStoreTypes()

        viewModel.storeRequest.hasDelivery = "1"

        when {
            intent.hasExtra(Params.STORE_REGISTER) -> {
                when {
                    intent.getSerializableExtra(Params.STORE_REGISTER) != null -> {
                        val storeData: StoreRegister =
                            intent.extras!!.getSerializable(Params.STORE_REGISTER) as StoreRegister
                        viewModel.storeRequest = storeData

                        binding.notStoreImg.visibility = View.GONE
                        binding.ivStorePhoto.visibility = View.VISIBLE
                        Glide.with(this).load(storeData.storeThumbUri).into(binding.ivStoreImg)

                        binding.notThumbImg.visibility = View.GONE
                        binding.ivThumb.visibility = View.VISIBLE
                        Glide.with(this).load(storeData.storeImgUri).into(binding.ivStoreThumb)
                    }
                }
            }
        }

        if (viewModel.storeRequest.pickUpFromStore == "1") {
            viewModel.storeRequest.pickUpFromStore = "1"
            binding.yesPickUpFromStore.isChecked = true
            binding.noPickUpFromStore.isChecked = false
        } else {
            viewModel.storeRequest.pickUpFromStore = "0"
            binding.yesPickUpFromStore.isChecked = false
            binding.noPickUpFromStore.isChecked = true
        }

        if (viewModel.storeRequest.cashOnDelivery == "1") {
            viewModel.storeRequest.cashOnDelivery = "1"
            binding.yesCashOnDelivery.isChecked = true
            binding.noCashOnDelivery.isChecked = false
        } else {
            viewModel.storeRequest.cashOnDelivery = "0"
            binding.yesCashOnDelivery.isChecked = false
            binding.noCashOnDelivery.isChecked = true
        }

        if (viewModel.storeRequest.checkDelivery == true) {
            viewModel.storeRequest.hasDelivery = "1"
            binding.yes.isChecked = true
            binding.no.isChecked = false
            binding.priceLayout.visibility = View.VISIBLE
            binding.spaceLayout.visibility = View.VISIBLE
            binding.timeLayout.visibility = View.VISIBLE
        } else {
            viewModel.storeRequest.hasDelivery = "0"
            binding.yes.isChecked = false
            binding.no.isChecked = true
            binding.priceLayout.visibility = View.GONE
            binding.spaceLayout.visibility = View.GONE
            binding.timeLayout.visibility = View.GONE
        }

        binding.rgPickUpFromStore.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.toString() == getString(R.string.yes)) {
                viewModel.storeRequest.pickUpFromStore = "1"
            } else if (radio.text.toString() == getString(R.string.no)) {
                viewModel.storeRequest.pickUpFromStore = "0"
            }
        }

        binding.rgCashOnDelivery.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.toString() == getString(R.string.yes)) {
                viewModel.storeRequest.cashOnDelivery = "1"
            } else if (radio.text.toString() == getString(R.string.no)) {
                viewModel.storeRequest.cashOnDelivery = "0"
            }
        }

        binding.rgDelivery.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.toString() == resources.getString(R.string.yes)) {
                viewModel.storeRequest.checkDelivery = true
                viewModel.storeRequest.hasDelivery = "1"
                binding.priceLayout.visibility = View.VISIBLE
                binding.spaceLayout.visibility = View.VISIBLE
                binding.timeLayout.visibility = View.VISIBLE

                binding.timeLayout.visibility = View.VISIBLE

                checkDeliveryType()

            } else if (radio.text.toString() == resources.getString(R.string.no)) {
                viewModel.storeRequest.checkDelivery = false
                viewModel.storeRequest.hasDelivery = "0"
                binding.priceLayout.visibility = View.GONE
                binding.spaceLayout.visibility = View.GONE
                binding.timeLayout.visibility = View.GONE

                viewModel.storeRequest.storeDeliveryType = null
                viewModel.obsType.set(null)
            }
        }

        checkDeliveryType()

        if (viewModel.storeRequest.checkCondition == true) {
            binding.checkBox.isChecked = true
        }

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.storeRequest.checkCondition = isChecked
        }

        binding.deliveryType.setOnClickListener {
            val deliveryTypeDialog = DeliveryTypeDialog(btnChooseTypeOrderCallback)
            deliveryTypeDialog.show(supportFragmentManager, "DeliveryTypeDialog")
        }
    }

    private fun checkDeliveryType() {
        when (viewModel.storeRequest.storeDeliveryType) {
            getString(R.string.minute) -> {
                viewModel.storeRequest.storeDeliveryType = getString(R.string.minute)
                viewModel.obsType.set(getString(R.string.minute))
            }

            getString(R.string.hour) -> {
                viewModel.storeRequest.storeDeliveryType = getString(R.string.hour)
                viewModel.obsType.set(getString(R.string.hour))
            }

            getString(R.string.day) -> {
                viewModel.storeRequest.storeDeliveryType = getString(R.string.day)
                viewModel.obsType.set(getString(R.string.day))
            }

            else -> {
                viewModel.storeRequest.storeDeliveryType = getString(R.string.minute)
                viewModel.obsType.set(getString(R.string.minute))
            }
        }
    }

    override fun onChanged(value: Any?) {
        when (value) {
            null -> return
            else -> value.let {
                when (it) {
                    Codes.STORE_TYPE_SUCSSES -> {
                        when {
                            intent.hasExtra(Params.STORE_REGISTER) -> {
                                when {
                                    intent.getSerializableExtra(Params.STORE_REGISTER) != null -> {
                                        val storeData: StoreRegister =
                                            intent.extras!!.getSerializable(Params.STORE_REGISTER) as StoreRegister

                                        viewModel.storeRequest.categories = storeData.categories
                                        for (item in viewModel.typesList) {
                                            if (viewModel.storeRequest.categories!!.contains(item.id!!.toInt())) {
                                                storeCategoryNames.add(item.name.toString())
                                            }
                                        }
                                        binding.etStoreType.text =
                                            storeCategoryNames.joinToString { it }
                                        // viewModel.storeRequest.tags = null
                                        // binding.etStoreTags.text = ""
                                        if (viewModel.storeRequest.categories != null && viewModel.storeRequest.categories!!.size > 0) {
                                            viewModel.getTags()
                                            viewModel.storeRequest.tags = storeData.tags
                                            for (item in viewModel.tagsList) {
                                                if (viewModel.storeRequest.tags?.contains(item.id) == true) {
                                                    storeTagsNames.add(item.name.toString())
                                                }
                                            }
                                            binding.etStoreTags.text =
                                                storeTagsNames.joinToString { it }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Codes.STORE_TAGS_SUCSSES -> {
                        when {
                            intent.hasExtra(Params.STORE_REGISTER) -> {
                                when {
                                    intent.getSerializableExtra(Params.STORE_REGISTER) != null -> {
                                        val storeData: StoreRegister =
                                            intent.extras!!.getSerializable(Params.STORE_REGISTER) as StoreRegister

                                        viewModel.storeRequest.tags = storeData.tags
                                        for (item in viewModel.tagsList) {
                                            if (viewModel.storeRequest.tags?.contains(item.id) == true) {
                                                storeTagsNames.add(item.name.toString())
                                            }
                                        }
                                        binding.etStoreTags.text =
                                            storeTagsNames.joinToString { it }
                                    }
                                }
                            }
                        }
                    }

                    Codes.STORE_CATEGORIES_CLICKED -> {
                        val bundle = Bundle()
                        bundle.putSerializable(Params.STORE_CATEGORIES, viewModel.typesResponse)
                        if (viewModel.storeRequest.categories != null) {
                            bundle.putSerializable(
                                Params.STORE_CATEGORIES_DATA,
                                viewModel.storeRequest
                            )
                        }
                        Utils.startDialogActivity(
                            this,
                            DialogStoreTypesFragment::class.java.name,
                            Codes.DIALOG_STORE_CATEGORY_CODE,
                            bundle
                        )
                    }

                    Codes.STORE_TAGS_CLICKED -> {
                        val bundle = Bundle()
                        bundle.putSerializable(Params.STORE_TAGS, viewModel.tagsResponse)
                        if (viewModel.storeRequest.tags != null) {
                            bundle.putSerializable(Params.STORE_TAGS_DATA, viewModel.storeRequest)
                        }
                        Utils.startDialogActivity(
                            this,
                            DialogStoreTagsFragment::class.java.name,
                            Codes.DIALOG_STORE_TAGS_CODE,
                            bundle
                        )
                    }

                    Codes.SELECT_STORE_IMAGE -> {
                        pickImage(Codes.STORE_IMAGE)
                    }

                    Codes.SELECT_STORE_THUMB -> {
                        pickImage(Codes.STORE_THUMBNAIL)
                    }

                    Codes.LOCATION_CLICKED -> {
                        val intent = Intent(this, MapsActivity::class.java)
                        intent.putExtra("from", Codes.GETTING_USER_LOCATION)
                        startActivityForResult(intent, Codes.GETTING_USER_LOCATION)
                    }

                    Codes.SHOW_CONDITIONS -> {
                        startActivity(Intent(this, ConditionActivity::class.java))
                    }

                    Codes.EMPTY_TYPE -> {
                        showToast(getString(R.string.choose_store_type), 1)
                    }

                    Codes.EMPTY_TAGS -> {
                        showToast(getString(R.string.choose_the_section_type), 1)
                    }

                    Codes.EMPTY_STORE_NAME -> {
                        showToast(getString(R.string.enter_your_store_name), 1)
                    }

                    Codes.EMPTY_STORE_LAT -> {
                        showToast(getString(R.string.please_enter_store_direction), 1)
                    }

                    Codes.EMPTY_STORE_ADDRESS -> {
                        showToast(getString(R.string.please_enter_store_address), 1)
                    }

                    Codes.EMPTY_STORE_MINIMUM -> {
                        showToast(getString(R.string.enter_the_minimum_order), 1)
                    }

                    Codes.EMPTY_STORE_PHONE -> {
                        showToast(getString(R.string.enter_phone_number), 1)
                    }

                    Codes.INVALID_PHONE -> {
                        showToast(getString(R.string.invalid_phone), 1)
                    }

                    Codes.EMPTY_STORE_WHATS -> {
                        showToast(getString(R.string.enter_the_whatsapp_number), 1)
                    }

                    Codes.INVALID_STORE_WHATS -> {
                        showToast(getString(R.string.invalid_whatsapp), 1)
                    }

                    Codes.EMPTY_STORE_PRICE -> {
                        showToast(getString(R.string.enter_the_delivery_price), 1)
                    }

                    Codes.EMPTY_STORE_Space -> {
                        showToast(
                            getString(R.string.enter_the_space_available_for_delivery_to_it),
                            1
                        )
                    }

                    Codes.EMPTY_STORE_TIME -> {
                        showToast(getString(R.string.enter_the_average_delivery_time), 1)
                    }

                    Codes.EMPTY_STORE_STORE_IMG -> {
                        showToast(getString(R.string.please_enter_store_image), 1)
                    }

                    Codes.EMPTY_STORE_STORE_THUMB -> {
                        showToast(getString(R.string.please_enter_store_thumbnail), 1)
                    }

                    Codes.FALSE_CHECK_CONDITION -> {
                        showToast(getString(R.string.agree_terms), 1)
                    }

                    Codes.SUCCESS -> {
                        val intent = Intent()
                        intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                        intent.putExtra(Params.STORE_REGISTER, viewModel.storeRequest)
                        setResult(Codes.REGISTER_STOREINFO_REQUEST_CODE, intent)
                        finish()
                    }

                    Codes.BACK_PRESSED -> {
                        val intent = Intent()
                        intent.putExtra(Params.DIALOG_CLICK_ACTION, 0)
                        setResult(Codes.REGISTER_STOREINFO_REQUEST_CODE, intent)
                        finish()
                    }

                    is String -> {
                        showToast(it.toString(), 1)
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
                    Codes.STORE_IMAGE -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notStoreImg.visibility = View.GONE
                                binding.ivStorePhoto.visibility = View.VISIBLE
                                binding.ivStoreImg.setImageURI(array[0].toUri())
                                viewModel.storeRequest.storeThumb = File(array[0])
                                viewModel.storeRequest.storeThumbUri = array[0]
                            }
                        }
                    }

                    Codes.STORE_THUMBNAIL -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notThumbImg.visibility = View.GONE
                                binding.ivThumb.visibility = View.VISIBLE
                                binding.ivStoreThumb.setImageURI(array[0].toUri())
                                viewModel.storeRequest.storeImg = File(array[0])
                                viewModel.storeRequest.storeImgUri = array[0]
                            }
                        }
                    }
                }
            }
        }

        /* When user select his location manually from map activity*/
        when {
            requestCode == Codes.GETTING_USER_LOCATION && data != null -> {
                when {
                    data.hasExtra(Params.USER_LOCATION) -> {
                        val locationItem =
                            data.getParcelableExtra<UserLocation>(Params.USER_LOCATION)
                        when {
                            locationItem != null -> {
                                viewModel.storeRequest.storeLat = locationItem.lat!!.toString()
                                viewModel.storeRequest.storeLng = locationItem.lng!!.toString()
                                viewModel.storeRequest.storeAddress =
                                    locationItem.address.toString()
                                binding.etAddress.setText(locationItem.address.toString())
                            }
                        }
                    }
                }
            }

            requestCode == Codes.DIALOG_STORE_CATEGORY_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                            binding.etStoreType.text = data.getStringExtra("storeCategoryNames")
                            viewModel.storeRequest.categories =
                                data.getIntegerArrayListExtra("storeCategoryId")
                            viewModel.getTags()
                        }
                    }
                }
            }

            requestCode == Codes.DIALOG_STORE_TAGS_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                            binding.etStoreTags.text = data.getStringExtra("storeTagNames")
                            viewModel.storeRequest.tags =
                                data.getIntegerArrayListExtra("storeTagId")
                        }
                    }
                }
            }
        }
    }

    private fun pickImage(requestCode: Int) {
        val options = Options.init()
            .setRequestCode(requestCode) // Request code for activity results
            .setFrontfacing(false) // Front Facing camera on start
            .setExcludeVideos(false) //Option to exclude videos
            .setMode(Options.Mode.Picture)
        when {
            PermissionUtil.hasImagePermission(this) -> {
                Pix.start(this, options)
            }

            else -> {
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
}