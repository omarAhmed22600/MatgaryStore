package com.brandsin.store.ui.main.marketingRequest

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentReviewChosenStoriesMarketingBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.marketingRequest.adapter.ReviewChosenStoriesAdapter
import com.brandsin.store.ui.main.marketingRequest.viewmodel.MarketingRequestViewModel
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.longToast
import com.brandsin.store.utils.observe
import com.brandsin.user.utils.map.PermissionUtil
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.payment.paymentsdk.PaymentSdkActivity
import com.payment.paymentsdk.PaymentSdkConfigBuilder
import com.payment.paymentsdk.integrationmodels.PaymentSdkBillingDetails
import com.payment.paymentsdk.integrationmodels.PaymentSdkError
import com.payment.paymentsdk.integrationmodels.PaymentSdkLanguageCode
import com.payment.paymentsdk.integrationmodels.PaymentSdkShippingDetails
import com.payment.paymentsdk.integrationmodels.PaymentSdkTokenFormat
import com.payment.paymentsdk.integrationmodels.PaymentSdkTokenise
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionDetails
import com.payment.paymentsdk.integrationmodels.PaymentSdkTransactionType
import com.payment.paymentsdk.sharedclasses.interfaces.CallbackPaymentInterface
import com.payment.paymentsdk.sharedclasses.interfaces.CallbackQueryInterface
import com.payment.paymentsdk.sharedclasses.model.response.TransactionResponseBody
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@SuppressLint("SetTextI18n", "LogNotTimber")
class ReviewChosenStoriesMarketingFragment : BaseHomeFragment(), CallbackPaymentInterface,
    CallbackQueryInterface {

    private var _binding: FragmentReviewChosenStoriesMarketingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MarketingRequestViewModel by activityViewModels()

    private lateinit var reviewChosenStoriesAdapter: ReviewChosenStoriesAdapter

    private lateinit var numOfDayMarketing: String
    private lateinit var marketingValue: String
    private lateinit var startDate: Date
    private lateinit var endDate: Date
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReviewChosenStoriesMarketingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setBarName(getString(R.string.marketing_requests))

        viewModel.getPinStoriesMarketing()

        initViews()
        initRecycler()
        setBtnListener()
        subscribeData()
    }

    private fun initViews() {
        when (viewModel.pinType.value) {
            "story_to_home", "story_to_offers" -> {
                viewModel.toHomeCardString.value = getString(R.string.pin_a_story_to_the_home_page)
                viewModel.toOffersCardString.value =
                    getString(R.string.pin_a_story_to_the_offers_page)
            }

            "offer_to_home", "offer_to_offers" -> {
                viewModel.toHomeCardString.value = getString(R.string.pin_an_offer_on_home_page)
                viewModel.toOffersCardString.value = getString(R.string.show_a_on_the_store_page)
            }

            else -> ""
        }

        binding.edtNumOfDayMarketing.doAfterTextChanged {
            if (it.toString().isNotEmpty() || it != null) {
                viewModel.noOfDays.value = it.toString()
                numOfDayMarketing = it.toString()

                marketingValue =
                    (numOfDayMarketing.toInt() * viewModel.pricePinStoriesType.orEmpty()
                        .toInt()).toString()
                binding.marketingValue.text = "$marketingValue " + getString(
                    R.string.currency
                )
            } else {
                viewModel.noOfDays.value = ""
                numOfDayMarketing = ""
                binding.marketingValue.text =
                    viewModel.pricePinStoriesType.toString() + " " + getString(R.string.currency)
            }
        }
    }

    private fun initRecycler() {
        binding.rvChooseStories.apply {
            reviewChosenStoriesAdapter = ReviewChosenStoriesAdapter()
            reviewChosenStoriesAdapter.submitData(viewModel.storiesItem)
            adapter = reviewChosenStoriesAdapter
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

                                val fileUri = array[0].toUri()
                                if (array[0] != null)
                                    viewModel.isArImageChanged.value = true

                                val file = File(array[0])
                                Timber.e("uri $fileUri")

                                // Handle image selection
                                Timber.e("image")

                                binding.ivArOfferImg.setImageURI(fileUri)

                                viewModel.arOfferImage = File(array[0])


                            }
                        }
                    }
                    Codes.OFFER_IMG_UPDATE_REQUEST_CODE -> {
                        data?.let {
                            val returnValue = it.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                            returnValue?.let { array ->

                                val fileUri = array[0].toUri()
                                if (array[0] != null)
                                    viewModel.isEnImageChanged.value = true

                                val file = File(array[0])
                                Timber.e("uri $fileUri")

                                // Handle image selection
                                Timber.e("image")

                                binding.ivEnOfferImg.setImageURI(fileUri)

                                viewModel.enOfferImage = File(array[0])


                            }
                        }
                    }
                }
            }
        }
    }

            private fun setBtnListener() {
                binding.flChooseArPic.setOnClickListener {
                    //Arabic Picture
                    pickImage(Codes.OFFER_IMG_REQUEST_CODE)
                }
                binding.flChooseEnPic.setOnClickListener {
                    //English Picture
                    pickImage(Codes.OFFER_IMG_UPDATE_REQUEST_CODE)
                }
                binding.btnContinuation.setOnClickListener {
                    if (validate()) {
                        viewModel.createMarketingRequests(numOfDayMarketing)
//                configurationPayTabsPayment(marketingValue.toDouble())
                    }
                }
                binding.edtStartDateOfMarketing.setOnClickListener {
                    val picker =
                        MaterialDatePicker.Builder.datePicker()
                            .setTitleText(getString(R.string.select_start_date))
                            .setCalendarConstraints(
                                CalendarConstraints.Builder()
                                    .setValidator(DateValidatorPointForward.now())
                                    .build()
                            )
                            .build()
                    picker.addOnPositiveButtonClickListener {

                        val timeZoneUTC: TimeZone = TimeZone.getDefault()
                        val offsetFromUTC: Int = timeZoneUTC.getOffset(Date().time) * -1
                        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                        val date = Date(it + offsetFromUTC)
                        startDate = date
                        viewModel.startDate.value = simpleFormat.format(date)
                        Timber.e("date picked -> ${viewModel.startDate.value}")

                    }
                    picker.show(requireFragmentManager(), "")
                }
                binding.edtEndDateOfMarketing.setOnClickListener {
                    val picker =
                        MaterialDatePicker.Builder.datePicker()
                            .setTitleText(getString(R.string.select_end_date))
                            .setCalendarConstraints(
                                CalendarConstraints.Builder()
                                    .setValidator(DateValidatorPointForward.now())
                                    .build()
                            )
                            .build()
                    picker.addOnPositiveButtonClickListener {

                        val timeZoneUTC: TimeZone = TimeZone.getDefault()
                        val offsetFromUTC: Int = timeZoneUTC.getOffset(Date().time) * -1
                        val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                        val date = Date(it + offsetFromUTC)
                        endDate = date
                        viewModel.endDate.value = simpleFormat.format(date)
                        Timber.e("date picked -> ${viewModel.startDate.value}")

                    }
                    picker.show(requireFragmentManager(), "")
                }
            }

            private fun pickImage(requestCode: Int) {
                val options = Options.init()
                    .setRequestCode(requestCode) //Request code for activity results
                    .setFrontfacing(false) //Front Facing camera on start
                    .setMode(Options.Mode.Picture)
                if (PermissionUtil.hasImagePermission(requireActivity())) {
                    Pix.start(this, options)
                } else {
                    observe(
                        PermissionUtil.requestPermission(
                            requireActivity(),
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

            private fun subscribeData() {
                viewModel.getPinStoriesMarketingResponse.observe(viewLifecycleOwner) {
                    viewModel.obsIsLoading.set(false)
                    when (it) {
                        is ResponseHandler.Success -> {
                            viewModel.pricePinStoriesType = it.data?.value.toString()
                            binding.marketingValue.text =
                                it.data?.value.toString() + " " + getString(R.string.currency)
                        }

                        is ResponseHandler.Error -> {
                            viewModel.obsIsFull.set(false)

                            // show error message
                            showToast(it.message, 1)
                        }

                        is ResponseHandler.Loading -> {
                            // show a progress bar
                            viewModel.obsIsLoading.set(true)
                            viewModel.obsIsFull.set(false)
                        }

                        is ResponseHandler.StopLoading -> {
                            // show a progress bar
                            viewModel.obsIsLoading.set(false)
                            viewModel.obsIsFull.set(false)
                        }

                        else -> {}
                    }
                }

                viewModel.createMarketingRequestsResponse.observe(viewLifecycleOwner) {
                    when (it) {
                        is ResponseHandler.Success -> {
                            showToast(getString(R.string.success), 2)
                            findNavController().navigateUp()
                            findNavController().navigateUp()
                            findNavController().navigateUp()
                        }

                        is ResponseHandler.Error -> {
                            viewModel.obsIsFull.set(false)

                            // show error message
                            showToast(it.message, 1)
                        }

                        is ResponseHandler.Loading -> {
                            // show a progress bar
                            viewModel.obsIsLoading.set(true)
                            viewModel.obsIsFull.set(false)
                        }

                        is ResponseHandler.StopLoading -> {
                            // show a progress bar
                            viewModel.obsIsLoading.set(false)
                            viewModel.obsIsFull.set(false)
                        }

                        else -> {}
                    }
                }
            }

            private fun validate()
                : Boolean {
                var isValid = true

                fun showErrorAndToast(editText: EditText, errorMessage: String) {
                    editText.error = errorMessage
                    showToast(errorMessage, 1)
                    isValid = false
                }

                numOfDayMarketing = binding.edtNumOfDayMarketing.text.toString().trim()
                val numOfDayMarketingInt = numOfDayMarketing.toIntOrNull()
                viewModel.noOfDays.value = numOfDayMarketing
                // Calculate the difference in time between the two dates
                val diffInMillis = endDate.time - startDate.time

// Convert milliseconds to days
                val diffInDays = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()

                Timber.e("${viewModel.isArImageChanged.value} ${viewModel.isStoryType.value}")
                when {
                    numOfDayMarketing.isEmpty() -> showErrorAndToast(
                        binding.edtNumOfDayMarketing,
                        getString(R.string.enter_the_coupon_code)
                    )

                    viewModel.startDate.value.orEmpty().isEmpty() -> showErrorAndToast(
                        binding.edtStartDateOfMarketing,
                        getString(R.string.select_start_date)
                    )

                    viewModel.endDate.value.orEmpty().isEmpty() -> showErrorAndToast(
                        binding.edtEndDateOfMarketing,
                        getString(R.string.select_end_date)
                    )

                    startDate.after(endDate) -> {
                        showErrorAndToast(
                            binding.edtStartDateOfMarketing,
                            getString(R.string.start_after_end_marketing)
                        )
                        showErrorAndToast(
                            binding.edtEndDateOfMarketing,
                            getString(R.string.start_after_end_marketing)
                        )
                    }

                    startDate == endDate -> {
                        showErrorAndToast(
                            binding.edtStartDateOfMarketing,
                            getString(R.string.start_equal_end_marketing)
                        )
                        showErrorAndToast(
                            binding.edtEndDateOfMarketing,
                            getString(R.string.start_equal_end_marketing)
                        )
                    }

                    diffInDays != numOfDayMarketingInt -> {
                        showErrorAndToast(
                            binding.edtNumOfDayMarketing,
                            getString(R.string.no_of_days_invalid)
                        )
                    }
                    viewModel.isArImageChanged.value == false && viewModel.isStoryType.value == false -> {
                        Timber.e("cad")
                        showToast(
                            getString(R.string.please_choose_arabic_offer_picture),
                            1
                        )
                        return false
                    }

                }

                return isValid
            }

            private fun configurationPayTabsPayment(totalAmount: Double) {
                val profileId = "104321"// PROFILE_ID
                val serverKey = "SZJN699M9M-JHBMRW9TKG-66LHZRD9MG"
                val clientLey = "C6KMVT-MPDD6H-2NRVP7-QDMH6N"

                val locale = if (PrefMethods.getLanguage() == "ar") {
                    PaymentSdkLanguageCode.AR
                } else {
                    PaymentSdkLanguageCode.EN
                }
                // val locale = PaymentSdkLanguageCode.EN
                val screenTitle = "Pay with card"
                val cartId = "123456"
                val cartDesc = "cart description"
                val currency = "SAR"
                val amount = totalAmount

                val tokeniseType = PaymentSdkTokenise.NONE // tokenise is off
                //or PaymentSdkTokenise.USER_OPTIONAL // tokenise is optional as per user approval
                //or PaymentSdkTokenise . USER_MANDATORY // tokenise is forced as per user approval
                //or PaymentSdkTokenise . MERCHANT_MANDATORY // tokenise is forced without user approval
                //or PaymentSdkTokenise . USER_OPTIONAL_DEFAULT_ON // tokenise is optional as per user approval default value true

                val transType = PaymentSdkTransactionType.SALE // or PaymentSdkTransactionType.AUTH
                // val transType = PaymentSdkTransactionType.AUTH

                val tokenFormat = PaymentSdkTokenFormat.Hex32Format()

                val billingData = PaymentSdkBillingDetails(
                    "Dubai",
                    "ae",
                    PrefMethods.getUserData()?.email, // "email1@domain.com"
                    PrefMethods.getUserData()?.name, // "name"
                    PrefMethods.getUserData()?.phoneNumber, // "phone"
                    "Dubai",
                    "address",
                    "12345"// "zip"
                )

                val shippingData = PaymentSdkShippingDetails(
                    "Dubai", // "City",
                    "ae",
                    PrefMethods.getUserData()?.email,
                    PrefMethods.getUserData()?.name,
                    "${PrefMethods.getUserData()?.phoneNumber}",
                    "Dubai",
                    "address",
                    "12345"
                )
                val configData =
                    PaymentSdkConfigBuilder(
                        profileId,
                        serverKey,
                        clientLey,
                        amount,
                        currency
                    )
                        .setCartDescription(cartDesc)
                        .setLanguageCode(locale)
                        // .setMerchantIcon(resources.getDrawable(R.drawable.))
                        .setBillingData(billingData)
                        .setMerchantCountryCode("SA") // ISO alpha 2
                        .setShippingData(shippingData)
                        .setCartId(cartId)
                        .setTransactionType(transType)
                        .showBillingInfo(false)
                        .showShippingInfo(false) // if true show shipping details
                        .forceShippingInfo(true)
                        .setScreenTitle(screenTitle)
                        .isDigitalProduct(false)
                        .build()

                // startCardPayment(context = requireActivity(), ptConfigData = configData, callback = this)
                // or
                // startSamsungPayment(requireActivity(), configData, "samsungpay token", callback = this)

                PaymentSdkActivity.startPaymentWithSavedCards(
                    context = requireActivity(),
                    ptConfigData = configData,
                    support3DS = true,
                    callback = this
                )
            }

            override fun onCancel() {
                Toast.makeText(requireActivity(), "Payment Cancelled", Toast.LENGTH_SHORT).show()
                Log.d("TAG_PAY_TABS", "onPaymentCancel: 111")
            }

            override fun onError(error: PaymentSdkError) {
                Log.d("TAG_PAY_TABS", "onError: $error")
                Toast.makeText(requireActivity(), "${error.msg}", Toast.LENGTH_SHORT).show()
            }

            override fun onResult(transactionResponseBody: TransactionResponseBody) {
                Toast.makeText(
                    requireActivity(),
                    "${transactionResponseBody.paymentResult?.responseMessage}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("TAG_PAY_TABS", "onResult: $transactionResponseBody")
            }

            override fun onPaymentFinish(paymentSdkTransactionDetails: PaymentSdkTransactionDetails) {
                Log.d("TAG_PAY_TABS", "onPaymentFinish: $paymentSdkTransactionDetails")
                Toast.makeText(
                    requireActivity(),
                    "${paymentSdkTransactionDetails.paymentResult?.responseMessage}",
                    Toast.LENGTH_SHORT
                ).show()
                // call api
                viewModel.createMarketingRequests(numOfDayMarketing)

            }

            override fun onPaymentCancel() {
                Toast.makeText(requireActivity(), "onPaymentCancel", Toast.LENGTH_SHORT).show()
                Log.d("TAG_PAY_TABS", "onPaymentCancel:222")
            }

            override fun onDestroy() {
                super.onDestroy()
                viewModel.storiesIds.clear()
                viewModel.storiesItem.clear()
                viewModel.startDate.value = ""
                viewModel.endDate.value = ""
                viewModel.isEnImageChanged.value = false
                viewModel.isArImageChanged.value = false
                viewModel.enOfferImage = null
                viewModel.arOfferImage = null
                viewModel.noOfDays.value = "0"

                _binding = null
            }
        }