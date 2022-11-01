package com.brandsin.store.ui.profile.store

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fxn.pix.Pix
import com.brandsin.store.R
import com.brandsin.store.databinding.ProfileFragmentStoreInfoBinding
import com.brandsin.store.model.UserLocation
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.profile.updatestore.UpdateStoreResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.activity.map.MapsActivity
import com.brandsin.store.ui.dialogs.storetags.DialogStoreTagsFragment
import com.brandsin.store.ui.dialogs.storetypes.DialogStoreTypesFragment
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.user.model.constants.Params
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class StoreInfoFragment : BaseHomeFragment(), Observer<Any?>
{
    lateinit var binding : ProfileFragmentStoreInfoBinding
    lateinit var viewModel : StoreInfoViewModel

    var storeCategoryNames = ArrayList<String>()
    var storeCategoryId = ArrayList<Int>()
    var storeTagNames = ArrayList<String>()
    var storeTagId = ArrayList<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment_store_info, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(StoreInfoViewModel::class.java)
        binding.viewModel = viewModel

        setBarName(getString(R.string.store_info))
        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)
        viewModel.getStoreTypes()
        
        val storeData = PrefMethods.getStoreData()
        viewModel.updateRequest.storeId = storeData!!.id.toString()
        viewModel.updateRequest.storeName = storeData.name.toString()
        viewModel.updateRequest.storeAddress = storeData.address.toString()
        viewModel.updateRequest.storeAddress = storeData.address.toString()
        viewModel.updateRequest.storeLat = storeData.lat.toString()
        viewModel.updateRequest.storeLng = storeData.lng.toString()
        viewModel.updateRequest.storeDeliveryDistance = storeData.deliveryDistance.toString()
        viewModel.updateRequest.storeDeliveryPrice = storeData.deliveryPrice.toString()
        viewModel.updateRequest.storeDeliveryTime = storeData.deliveryTime.toString()
        viewModel.updateRequest.storePhoneNumber = storeData.phoneNumber.toString()
        viewModel.updateRequest.storeWhatsApp = storeData.whatsapp.toString()
        viewModel.updateRequest.storeMinOrderPrice = storeData.minOrderPrice.toString()
        viewModel.updateRequest.hasDelivery = storeData.hasDelivery.toString()
        if (!storeData.categories.isNullOrEmpty()) {
            for (item in storeData.categories) {
                storeCategoryId.add(item!!.id!!.toInt())
                storeCategoryNames.add(item.name.toString())
            }
        }
        if (!storeData.tags.isNullOrEmpty()) {
            for (item in storeData.tags) {
                storeTagId.add(item!!.id!!.toInt())
                storeTagNames.add(item.name.toString())
            }
        }
        viewModel.updateRequest.categories = storeCategoryId
        viewModel.updateRequest.tags = storeTagId
        binding.etStoreType.text =  storeCategoryNames.joinToString { it -> "$it" }
        binding.etStoreTags.text =  storeTagNames.joinToString { it -> "$it" }


        if (viewModel.updateRequest.hasDelivery=="1"){
            viewModel.checkDelivery = true
            binding.priceLayout.visibility = View.VISIBLE
            binding.spaceLayout.visibility = View.VISIBLE
            binding.timeLayout.visibility = View.VISIBLE
        }else if ( viewModel.updateRequest.hasDelivery == "0"){
            viewModel.checkDelivery = false
            binding.priceLayout.visibility = View.GONE
            binding.spaceLayout.visibility = View.GONE
            binding.timeLayout.visibility = View.GONE
        }

        if(!storeData.thumbnail.isNullOrEmpty()) {
            binding.notStoreImg.visibility = View.GONE
            binding.ivStorePhoto.visibility = View.VISIBLE
            Glide.with(this).load(storeData.thumbnail).into(binding.ivStoreImg)
            if (viewModel.updateRequest.storeMedia == null){
                viewModel.updateRequest.storeMedia = ArrayList ()
            }
            viewModel.updateRequest.storeMedia!!.add(storeData.thumbnailId!!)
        }

        if(storeData.commercialRegister !=null) {
            binding.notThumbImg.visibility = View.GONE
            binding.ivThumb.visibility = View.VISIBLE
            Glide.with(this).load(storeData.commercialRegister.url).into(binding.ivStoreThumb)
            if (viewModel.updateRequest.storeMedia == null) {
                viewModel.updateRequest.storeMedia = ArrayList()
            }
            viewModel.updateRequest.storeMedia!!.add(storeData.commercialRegister.id!!)
        }

        if (storeData.images?.size!! >= 1) {
            binding.notPhoto1.visibility = View.GONE
            binding.ivImg1.visibility = View.VISIBLE
            Glide.with(this).load(storeData.images[0]?.url).into(binding.ivPhoto1)
            if (viewModel.updateRequest.storeMedia == null){
                viewModel.updateRequest.storeMedia = ArrayList ()
            }
            viewModel.updateRequest.storeMedia!!.add(storeData.images[0]?.id!!)
        }
        if (storeData.images.size >= 2) {
            binding.notPhoto2.visibility = View.GONE
            binding.ivImg2.visibility = View.VISIBLE
            Glide.with(this).load(storeData.images[1]?.url).into(binding.ivPhoto2)
            if (viewModel.updateRequest.storeMedia == null){
                viewModel.updateRequest.storeMedia = ArrayList ()
            }
            viewModel.updateRequest.storeMedia!!.add(storeData.images[1]?.id!!)
        }

        if (storeData.images.size >=3) {
            binding.notPhoto3.visibility = View.GONE
            binding.ivImg3.visibility = View.VISIBLE
            Glide.with(this).load(storeData.images[2]?.url).into(binding.ivPhoto3)
            if (viewModel.updateRequest.storeMedia == null){
                viewModel.updateRequest.storeMedia = ArrayList ()
            }
            viewModel.updateRequest.storeMedia!!.add(storeData.images[2]?.id!!)
        }

        if (storeData.images.size >=4) {
            binding.notPhoto4.visibility = View.GONE
            binding.ivImg4.visibility = View.VISIBLE
            Glide.with(this).load(storeData.images[3]?.url).into(binding.ivPhoto4)
            if (viewModel.updateRequest.storeMedia == null){
                viewModel.updateRequest.storeMedia = ArrayList ()
            }
            viewModel.updateRequest.storeMedia!!.add(storeData.images[3]?.id!!)
        }

        if (storeData.images.size >=5) {
            binding.notPhoto5.visibility = View.GONE
            binding.ivImg5.visibility = View.VISIBLE
            Glide.with(this).load(storeData.images[4]?.url).into(binding.ivPhoto5)
            if (viewModel.updateRequest.storeMedia == null){
                viewModel.updateRequest.storeMedia = ArrayList ()
            }
            viewModel.updateRequest.storeMedia!!.add(storeData.images[4]?.id!!)
        }

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
                        is UpdateStoreResponse -> {
                            showToast(getString(R.string.store_update_successfully), 2)
                            lifecycleScope.launch {
                                delay(2000)
                                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                            }

                        }
                    }
                }
                else -> {
                    Timber.e(it.message)
                }
            }
        }

        binding.rgDelivery.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton = view.findViewById(checkedId)
                    if (radio.text.toString() == resources.getString(R.string.yes)) {
                        viewModel.checkDelivery = true
                        viewModel.updateRequest.hasDelivery = "1"
                        binding.priceLayout.visibility = View.VISIBLE
                        binding.spaceLayout.visibility = View.VISIBLE
                        binding.timeLayout.visibility = View.VISIBLE
                    } else if (radio.text.toString() == resources.getString(R.string.no)) {
                        viewModel.checkDelivery = false
                        viewModel.updateRequest.hasDelivery = "0"
                        binding.priceLayout.visibility = View.GONE
                        binding.spaceLayout.visibility = View.GONE
                        binding.timeLayout.visibility = View.GONE
                    }
                })
    }

    override fun onChanged(it: Any?)
    {
        if (it == null) return
        it.let {
            when (it) {
                Codes.STORE_CATEGORIES_CLICKED -> {
                    val bundle = Bundle()
                    bundle.putSerializable(Params.STORE_CATEGORIES, viewModel.typesResponse)
                    if (viewModel.updateRequest.categories != null) {
                        bundle.putSerializable(Params.STORE_CATEGORIES_DATA_EDIT, viewModel.updateRequest)
                    }
                    Utils.startDialogActivity(requireActivity(), DialogStoreTypesFragment::class.java.name, Codes.DIALOG_STORE_CATEGORY_CODE, bundle)
                }
                Codes.STORE_TAGS_CLICKED -> {
                    val bundle = Bundle()
                    bundle.putSerializable(Params.STORE_TAGS, viewModel.tagsResponse)
                    if (viewModel.updateRequest.tags != null) {
                        bundle.putSerializable(Params.STORE_TAGS_DATA_EDIT, viewModel.updateRequest)
                    }
                    Utils.startDialogActivity(requireActivity(), DialogStoreTagsFragment::class.java.name, Codes.DIALOG_STORE_TAGS_CODE, bundle)
                }
                Codes.SELECT_STORE_IMAGE -> {
                    pickImage(Codes.STORE_IMAGE)
                }
                Codes.SELECT_STORE_THUMB -> {
                    pickImage(Codes.STORE_THUMBNAIL)
                }
                Codes.SELECT_PHOTO1 -> {
                    pickImage(Codes.SELECT_PHOTO1)
                }
                Codes.SELECT_PHOTO2 -> {
                    pickImage(Codes.SELECT_PHOTO2)
                }
                Codes.SELECT_PHOTO3 -> {
                    pickImage(Codes.SELECT_PHOTO3)
                }
                Codes.SELECT_PHOTO4 -> {
                    pickImage(Codes.SELECT_PHOTO4)
                }
                Codes.SELECT_PHOTO5 -> {
                    pickImage(Codes.SELECT_PHOTO5)
                }
                Codes.LOCATION_CLICKED -> {
                    val intent = Intent(requireActivity(), MapsActivity::class.java)
                    intent.putExtra("from", Codes.GETTING_USER_LOCATION)
                    startActivityForResult(intent, Codes.GETTING_USER_LOCATION)
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
                    showToast(getString(R.string.enter_the_space_available_for_delivery_to_it), 1)
                }
                Codes.EMPTY_STORE_TIME -> {
                    showToast(getString(R.string.enter_the_average_delivery_time), 1)
                }
                Codes.EMPTY_STORE_STORE_IMG -> {
                    showToast(getString(R.string.please_enter_store_image), 1)
                }
                Codes.EMPTY_STORE_STORE_THUM -> {
                    showToast(getString(R.string.please_enter_store_thumbnail), 1)
                }
                is String -> {
                    showToast(it.toString(), 1)
                }
            }
        }
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
                                if (viewModel.updateRequest.storeMedia != null ){
                                    if (viewModel.updateRequest.deleteMedia == null){
                                        viewModel.updateRequest.deleteMedia = ArrayList ()
                                    }
                                    if (viewModel.updateRequest.storeMedia!!.size >=1) {
                                        viewModel.updateRequest.deleteMedia!!.add(viewModel.updateRequest.storeMedia!![0])
                                        viewModel.updateRequest.storeMedia!!.removeAt(0)
                                    }
                                }else{
                                    viewModel.updateRequest.storeMedia = ArrayList ()
                                }
                                viewModel.uploadRequest.collection = "marketplace-store-thumbnail"
                                viewModel.uploadRequest.image  =  File(array[0])
                                viewModel.upload(0)
                            } }
                    }
                    Codes.STORE_THUMBNAIL -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notThumbImg.visibility = View.GONE
                                binding.ivThumb.visibility = View.VISIBLE
                                binding.ivStoreThumb.setImageURI(array[0].toUri())
                                if (viewModel.updateRequest.storeMedia != null ){
                                    if (viewModel.updateRequest.deleteMedia == null){
                                        viewModel.updateRequest.deleteMedia = ArrayList ()
                                    }
                                    if (viewModel.updateRequest.storeMedia!!.size >=2) {
                                        viewModel.updateRequest.deleteMedia!!.add(viewModel.updateRequest.storeMedia!![1])
                                        viewModel.updateRequest.storeMedia!!.removeAt(1)
                                    }
                                }else{
                                    viewModel.updateRequest.storeMedia = ArrayList ()
                                }
                                viewModel.uploadRequest.collection = "commercial_register"
                                viewModel.uploadRequest.image  =  File(array[0])
                                viewModel.upload(1)
                            } }
                    }
                    Codes.SELECT_PHOTO1 -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notPhoto1.visibility = View.GONE
                                binding.ivImg1.visibility = View.VISIBLE
                                binding.ivPhoto1.setImageURI(array[0].toUri())
                                if (viewModel.updateRequest.storeMedia != null ){
                                    if (viewModel.updateRequest.deleteMedia == null){
                                        viewModel.updateRequest.deleteMedia = ArrayList ()
                                    }
                                    if (viewModel.updateRequest.storeMedia!!.size >=3) {
                                        viewModel.updateRequest.deleteMedia!!.add(viewModel.updateRequest.storeMedia!![2])
                                        viewModel.updateRequest.storeMedia!!.removeAt(2)
                                    }
                                }else{
                                    viewModel.updateRequest.storeMedia = ArrayList ()
                                }
                                viewModel.uploadRequest.collection = "store-registration-images"
                                viewModel.uploadRequest.image  =  File(array[0])
                                viewModel.upload(2)
                            } }
                    }
                    Codes.SELECT_PHOTO2 -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notPhoto2.visibility = View.GONE
                                binding.ivImg2.visibility = View.VISIBLE
                                binding.ivPhoto2.setImageURI(array[0].toUri())
                                if (viewModel.updateRequest.storeMedia != null ){
                                    if (viewModel.updateRequest.deleteMedia == null){
                                        viewModel.updateRequest.deleteMedia = ArrayList ()
                                    }
                                    if (viewModel.updateRequest.storeMedia!!.size >=4) {
                                        viewModel.updateRequest.deleteMedia!!.add(viewModel.updateRequest.storeMedia!![3])
                                        viewModel.updateRequest.storeMedia!!.removeAt(3)
                                    }
                                }else{
                                    viewModel.updateRequest.storeMedia = ArrayList ()
                                }
                                viewModel.uploadRequest.collection = "store-registration-images"
                                viewModel.uploadRequest.image  =  File(array[0])
                                viewModel.upload(3)
                            } }
                    }
                    Codes.SELECT_PHOTO3 -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notPhoto3.visibility = View.GONE
                                binding.ivImg3.visibility = View.VISIBLE
                                binding.ivPhoto3.setImageURI(array[0].toUri())
                                if (viewModel.updateRequest.storeMedia != null ){
                                    if (viewModel.updateRequest.deleteMedia == null){
                                        viewModel.updateRequest.deleteMedia = ArrayList ()
                                    }
                                    if (viewModel.updateRequest.storeMedia!!.size >=5) {
                                        viewModel.updateRequest.deleteMedia!!.add(viewModel.updateRequest.storeMedia!![4])
                                        viewModel.updateRequest.storeMedia!!.removeAt(4)
                                    }
                                }else{
                                    viewModel.updateRequest.storeMedia = ArrayList ()
                                }
                                viewModel.uploadRequest.collection = "store-registration-images"
                                viewModel.uploadRequest.image  =  File(array[0])
                                viewModel.upload(4)
                            } }
                    }
                    Codes.SELECT_PHOTO4 -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notPhoto4.visibility = View.GONE
                                binding.ivImg4.visibility = View.VISIBLE
                                binding.ivPhoto4.setImageURI(array[0].toUri())
                                if (viewModel.updateRequest.storeMedia != null){
                                    if (viewModel.updateRequest.deleteMedia == null){
                                        viewModel.updateRequest.deleteMedia = ArrayList ()
                                    }
                                    if (viewModel.updateRequest.storeMedia!!.size >=6) {
                                        viewModel.updateRequest.deleteMedia!!.add(viewModel.updateRequest.storeMedia!![5])
                                        viewModel.updateRequest.storeMedia!!.removeAt(5)
                                    }
                                }else{
                                    viewModel.updateRequest.storeMedia = ArrayList ()
                                }
                                viewModel.uploadRequest.collection = "store-registration-images"
                                viewModel.uploadRequest.image  =  File(array[0])
                                viewModel.upload(5)
                            } }
                    }
                    Codes.SELECT_PHOTO5 -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notPhoto5.visibility = View.GONE
                                binding.ivImg5.visibility = View.VISIBLE
                                binding.ivPhoto5.setImageURI(array[0].toUri())
                                if (viewModel.updateRequest.storeMedia != null){
                                    if (viewModel.updateRequest.deleteMedia == null){
                                        viewModel.updateRequest.deleteMedia = ArrayList ()
                                    }
                                    if (viewModel.updateRequest.storeMedia!!.size >=7) {
                                        viewModel.updateRequest.deleteMedia!!.add(viewModel.updateRequest.storeMedia!![6])
                                        viewModel.updateRequest.storeMedia!!.removeAt(6)
                                    }
                                }else{
                                    viewModel.updateRequest.storeMedia = ArrayList ()
                                }
                                viewModel.uploadRequest.collection = "store-registration-images"
                                viewModel.uploadRequest.image  =  File(array[0])
                                viewModel.upload(6)
                            } }
                    }
                }
            } }

        /* When user select his location manually from map activity*/
        when {
            requestCode == Codes.GETTING_USER_LOCATION && data != null -> {
                when {
                    data.hasExtra(Params.USER_LOCATION) -> {
                        val locationItem =
                                data.getParcelableExtra<UserLocation>(Params.USER_LOCATION)
                        when {
                            locationItem != null -> {
                                viewModel.updateRequest.storeLat = locationItem.lat!!.toString()
                                viewModel.updateRequest.storeLng = locationItem.lng!!.toString()
                                viewModel.updateRequest.storeAddress = locationItem.address.toString()
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
                            viewModel.updateRequest.categories =  data.getIntegerArrayListExtra("storeCategoryId")
                            viewModel.updateRequest.tags = null
                            binding.etStoreTags.text = ""
                            if(viewModel.updateRequest.categories != null && viewModel.updateRequest.categories!!.size >0) {
                                viewModel.getTags()
                            }
                        }
                    }
                }
            }
            requestCode == Codes.DIALOG_STORE_TAGS_CODE && data != null -> {
                if (data.hasExtra(Params.DIALOG_CLICK_ACTION)) {
                    when {
                        data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                            binding.etStoreTags.text = data.getStringExtra("storeTagNames")
                            viewModel.updateRequest.tags =  data.getIntegerArrayListExtra("storeTagId")
                        }
                    }
                }
            }
        }
    }
}