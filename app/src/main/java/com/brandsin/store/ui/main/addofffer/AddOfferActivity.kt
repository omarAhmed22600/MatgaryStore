package com.brandsin.store.ui.main.addofffer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.brandsin.store.R
import com.brandsin.store.databinding.ActivityAddOfferBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.offers.add.CreateOfferResponse
import com.brandsin.store.model.main.offers.add.DataItem
import com.brandsin.store.model.main.offers.add.OfferAddProductResponse
import com.brandsin.store.model.main.offers.listoffer.OffersItemDetails
import com.brandsin.store.model.main.offers.update.UpdateOfferResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.dialogs.addoffer.DialogAddOfferFragment
import com.brandsin.store.ui.dialogs.offertime.DialogOfferTimeFragment
import com.brandsin.store.ui.dialogs.toast.DialogToastFragment
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.user.model.constants.Params
import com.brandsin.user.utils.map.PermissionUtil
import timber.log.Timber
import java.io.File

class AddOfferActivity : AppCompatActivity(), Observer<Any?>
{
    lateinit var binding : ActivityAddOfferBinding
    lateinit var viewModel : AddOfferViewModel
    var productsNamesList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_offer)
        viewModel = ViewModelProvider(this).get(AddOfferViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(this, this)

        binding.ibBack.setOnClickListener {
            finish()
        }

        when {
            intent.hasExtra(Params.OFFER_ITEM) -> {
                when {
                    intent.getSerializableExtra(Params.OFFER_ITEM) != null -> {
                        val offerData: OffersItemDetails = intent.extras!!.getSerializable(Params.OFFER_ITEM) as OffersItemDetails
                        viewModel.setOfferData(2, offerData)
                        binding.notOfferImg.visibility = View.GONE
                        binding.ivPhoto.visibility = View.VISIBLE
                        Glide.with(this).load(offerData.image).error(R.drawable.app_logo).into(binding.ivOfferImg)
                        viewModel.obsToolBarTitle.set(getString(R.string.update_offer))
                    }
                    else -> {
                        viewModel.setOfferData(1)
                        binding.notOfferImg.visibility = View.VISIBLE
                        binding.ivPhoto.visibility = View.GONE
                        viewModel.obsToolBarTitle.set(getString(R.string.add_offer))
                    }
                 }
             }
            else -> {
                viewModel.setOfferData(1)
                binding.notOfferImg.visibility = View.VISIBLE
                binding.ivPhoto.visibility = View.GONE
                viewModel.obsToolBarTitle.set(getString(R.string.add_offer))
            }
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
                        is OfferAddProductResponse -> {
                            if (it.data.data!!.isNotEmpty()) {
                                setAutocomplete(it.data.data)
                            }else{
//                                showToast(getString(R.string.no_results_search) , 1)
                            }
                        }
                        is CreateOfferResponse -> {
                            val bundle = Bundle()
                            bundle.putString(Params.DIALOG_CONFIRM_MESSAGE, it.data.message)
                            bundle.putInt(Params.REQUEST_CODE, Codes.ADD_OFFER)
                            Utils.startDialogActivity(
                                    this,
                                    DialogAddOfferFragment::class.java.name,
                                    Codes.DIALOG_OFFER_REQUEST,
                                    bundle
                            )
                        }
                        is UpdateOfferResponse -> {
                            val bundle = Bundle()
                            bundle.putString(Params.DIALOG_CONFIRM_MESSAGE, it.data.message)
                            bundle.putInt(Params.REQUEST_CODE, Codes.UPDATE_OFFER)
                            Utils.startDialogActivity(
                                this,
                                DialogAddOfferFragment::class.java.name,
                                Codes.DIALOG_OFFER_REQUEST,
                                bundle
                            )
                        }
                    }
                }
                else -> {
                    Timber.e(it.message)
                }
            }
        }

        viewModel.productsAdapter.deleteLiveData.observe(this, {

            viewModel.addOfferRequest.productsIds!!.remove(it!!.id!!.toInt())
            viewModel.productsList.remove(it)
            viewModel.productsAdapter.updateList(viewModel.productsList)
        })

        viewModel.productsUpdateAdapter.deleteLiveData.observe(this, {

            viewModel.prevOfferProducts.remove(it)
            viewModel.productsUpdateAdapter.updateList(viewModel.prevOfferProducts)
        })

        binding.tvAutoComplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val length: Int = binding.tvAutoComplete.length()
                viewModel.offerAddProductRequest.name = binding.tvAutoComplete.text.toString()
                if (length > 0) {
                    if (!isFinishing) {
                        viewModel.getStoreProducts()
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun setAutocomplete(data: List<DataItem?>) {

        productsNamesList = ArrayList<String>()

        data.forEach { productItem ->
            if (productItem != null) {
                productsNamesList.add(productItem.name.toString())
            }
        }
        val adapter = ArrayAdapter(
            MyApp.getInstance(),
            R.layout.raw_offer_auto_complete,
            productsNamesList
        )
        binding.tvAutoComplete.threshold = 1
        binding.tvAutoComplete.setAdapter(adapter)

        binding.tvAutoComplete.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            data.forEach { productItem ->
                if (productItem != null) {
                    if (productItem.name == binding.tvAutoComplete.text.toString()){
                        if (viewModel.addOfferRequest.productsIds ==null){
                            viewModel.addOfferRequest.productsIds = ArrayList<Int>()
                        }
                        if (!viewModel.addOfferRequest.productsIds!!.contains(productItem.id!!.toInt())) {
                            viewModel.addOfferRequest.productsIds!!.add(productItem.id.toInt())
                            viewModel.productsList.add(productItem)
                            viewModel.productsAdapter.updateList(viewModel.productsList)
                        }else{
                            showToast(getString(R.string.product_added_before) , 1)
                        }
                    }
                }
            }
        }

    }
    private fun showToast(msg: String, type: Int)
    {
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

    override fun onChanged(it: Any?) {
        when (it) {
            null -> return
            else -> it.let {
                when (it) {
                    Codes.SELECT_IMAGES -> {
                        pickImage(Codes.OFFER_IMG_REQUEST_CODE)
                    }
                    Codes.CHANGE_IMAGES -> {
                        pickImage(Codes.OFFER_IMG_UPDATE_REQUEST_CODE)
                    }
                    Codes.EMPTY_IMAGE -> {
                        showToast(getString(R.string.please_select_offer_image), 1)
                    }
                    Codes.EMPTY_OFFER_NAME -> {
                        showToast(getString(R.string.please_select_offer_name_ar), 1)
                    }
                    Codes.EMPTY_OFFER_NAME_EN -> {
                        showToast(getString(R.string.please_select_offer_name_en), 1)
                    }
                    Codes.EMPTY_OFFER_DESCRIPTION -> {
                        showToast(getString(R.string.please_select_offer_description_ar), 1)
                    }
                    Codes.EMPTY_OFFER_DESCRIPTION_EN -> {
                        showToast(getString(R.string.please_select_offer_description_en), 1)
                    }
                    Codes.EMPTY_OFFER_PRICE -> {
                        showToast(getString(R.string.please_select_offer_price), 1)
                    }
                    Codes.SELECT_START_DATE -> {
                        showToast(getString(R.string.select_start_date), 1)
                    }
                    Codes.SELECT_END_DATE -> {
                        showToast(getString(R.string.select_end_date), 1)
                    }
                    Codes.EMPTY_PRODUCTS -> {
                        showToast(getString(R.string.please_select_offer_product), 1)
                    }
                    Codes.START_AFTER_END -> {
                        showToast(getString(R.string.start_after_end), 1)
                    }
                    Codes.START_EQUAL_END -> {
                        showToast(getString(R.string.start_equal_end), 1)
                    }
                    Codes.SHOW_START_DATE -> {
                        val bundle = Bundle()
                        bundle.putString("START_DATE", viewModel.obsStartDate.get())
                        bundle.putInt(Params.DIALOG_DATE_REQUEST, Codes.SHOW_START_DATE)
                        Utils.startDialogActivity(
                            this,
                            DialogOfferTimeFragment::class.java.name,
                            Codes.DIALOG_OFFER_TIME,
                            bundle
                        )
                    }
                    Codes.SHOW_END_DATE -> {
                        val bundle = Bundle()
                        bundle.putString("END_DATE",viewModel.obsEndDate.get())
                        bundle.putInt(Params.DIALOG_DATE_REQUEST, Codes.SHOW_END_DATE)
                        Utils.startDialogActivity(
                            this,
                            DialogOfferTimeFragment::class.java.name,
                            Codes.DIALOG_OFFER_TIME,
                            bundle
                        )
                    }
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.e("$requestCode $resultCode $data")
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Codes.OFFER_IMG_REQUEST_CODE -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notOfferImg.visibility = View.GONE
                                binding.ivPhoto.visibility = View.VISIBLE
                                binding.ivOfferImg.setImageURI(array[0].toUri())
                                viewModel.addOfferRequest.offerImage  =  File(array[0])
                            }
                        }
                    }
                    Codes.OFFER_IMG_UPDATE_REQUEST_CODE -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->
                                binding.notOfferImg.visibility = View.GONE
                                binding.ivPhoto.visibility = View.VISIBLE
                                binding.ivOfferImg.setImageURI(array[0].toUri())
                                viewModel.updateOfferRequest.offerImage  =  File(array[0])
                            }
                        }
                    }
                }
            }
        }

        when {
            requestCode == Codes.DIALOG_OFFER_TIME && data != null -> {
                when {
                    data.hasExtra(Params.DIALOG_CLICK_ACTION) -> {
                        when {
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {
                                when {
                                    data.getIntExtra(Params.DIALOG_DATE_REQUEST, 0) != 0 -> {
                                        when (Codes.SHOW_START_DATE) {
                                            data.getIntExtra(Params.DIALOG_DATE_REQUEST, 0) -> {
                                                viewModel.obsStartDate.set(data.getStringExtra(Params.OFFER_TIME))
                                                if (!viewModel.obsEndDate.get().isNullOrEmpty()){
                                                    viewModel.checkValidationDates()
                                                }
                                            }
                                        }
                                        when (Codes.SHOW_END_DATE) {
                                            data.getIntExtra(Params.DIALOG_DATE_REQUEST, 0) -> {
                                                viewModel.obsEndDate.set(data.getStringExtra(Params.OFFER_TIME))
                                                viewModel.checkValidationDates()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        when {
            requestCode == Codes.DIALOG_OFFER_REQUEST && data != null -> {
                when {
                    data.hasExtra(Params.DIALOG_CLICK_ACTION) -> {
                        when {
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION, 0) == 1 -> {

                                if (data.getIntExtra(Params.REQUEST_CODE, 0) ==  Codes.ADD_OFFER) {
                                    val intent = Intent()
                                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                                    setResult(Codes.ADD_OFFER, intent)
                                    finish()
                                }
                                if (data.getIntExtra(Params.REQUEST_CODE, 0) ==  Codes.UPDATE_OFFER) {
                                    val intent = Intent()
                                    intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                                    setResult(Codes.UPDATE_OFFER, intent)
                                    finish()
                                }
                            }
                            data.getIntExtra(Params.DIALOG_CLICK_ACTION, 1) == 2 -> {
                                if (data.getIntExtra(Params.REQUEST_CODE, 0) ==  Codes.ADD_OFFER) {
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finishAffinity()
                                }
                                if (data.getIntExtra(Params.REQUEST_CODE, 0) ==  Codes.UPDATE_OFFER) {
                                    startActivity(Intent(this, HomeActivity::class.java))
                                    finishAffinity()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun pickImage(requestCode: Int) {
        val options = Options.init()
                .setRequestCode(requestCode) //Request code for activity results
                .setFrontfacing(false) //Front Facing camera on start
                .setExcludeVideos(false) //Option to exclude videos
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
                }
            }
        }
    }
}