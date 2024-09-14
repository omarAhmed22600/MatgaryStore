package com.brandsin.store.ui.main.marketingRequest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentReviewChosenStoriesMarketingBinding
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.marketingRequest.adapter.ReviewChosenStoriesAdapter
import com.brandsin.store.ui.main.marketingRequest.viewmodel.MarketingRequestViewModel
import com.brandsin.store.utils.PrefMethods
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

@SuppressLint("SetTextI18n", "LogNotTimber")
class ReviewChosenStoriesMarketingFragment : BaseHomeFragment(), CallbackPaymentInterface,
    CallbackQueryInterface {

    private var _binding: FragmentReviewChosenStoriesMarketingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MarketingRequestViewModel by activityViewModels()

    private lateinit var reviewChosenStoriesAdapter: ReviewChosenStoriesAdapter

    private lateinit var numOfDayMarketing: String
    private lateinit var marketingValue: String

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

        setBarName(getString(R.string.marketing_requests))

        viewModel.getPinStoriesMarketing()

        initViews()
        initRecycler()
        setBtnListener()
        subscribeData()
    }

    private fun initViews() {
        when (viewModel.pinStoriesType) {
            "home" -> binding.pinStories.text = getString(R.string.pin_a_story_to_the_home_page)
            "offers" -> binding.pinStories.text = getString(R.string.pin_a_story_to_the_offers_page)
            "in_store" -> binding.pinStories.text = getString(R.string.show_a_on_the_store_page)
        }

        binding.edtNumOfDayMarketing.doAfterTextChanged {
            if (it.toString().isNotEmpty()) {
                numOfDayMarketing = it.toString()
                marketingValue =
                    (numOfDayMarketing.toInt() * viewModel.pricePinStoriesType!!.toInt()).toString()
                binding.marketingValue.text = "$marketingValue " + getString(
                    R.string.currency
                )
            } else {
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

    private fun setBtnListener() {
        binding.btnContinuation.setOnClickListener {
            if (validate()) {
                // viewModel.createMarketingRequests(numOfDayMarketing)
                configurationPayTabsPayment(marketingValue.toDouble())
            }
        }
    }

    private fun subscribeData() {
        viewModel.getPinStoriesMarketingResponse.observe(viewLifecycleOwner) {
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

    private fun validate(): Boolean {
        var isValid = true

        fun showErrorAndToast(editText: EditText, errorMessage: String) {
            editText.error = errorMessage
            showToast(errorMessage, 1)
            isValid = false
        }

        numOfDayMarketing = binding.edtNumOfDayMarketing.text.toString().trim()

        when {
            numOfDayMarketing.isEmpty() -> showErrorAndToast(
                binding.edtNumOfDayMarketing,
                getString(R.string.enter_the_coupon_code)
            )
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
    }

    override fun onPaymentCancel() {
        Toast.makeText(requireActivity(), "onPaymentCancel", Toast.LENGTH_SHORT).show()
        Log.d("TAG_PAY_TABS", "onPaymentCancel:222")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.storiesIds.clear()
        viewModel.storiesItem.clear()
        _binding = null
    }
}