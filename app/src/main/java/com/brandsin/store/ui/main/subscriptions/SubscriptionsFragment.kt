package com.brandsin.store.ui.main.subscriptions

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentSubscriptionsBinding
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.showAlertDialog
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

class SubscriptionsFragment : BaseHomeFragment(), CallbackPaymentInterface, CallbackQueryInterface {

    private var _binding: FragmentSubscriptionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubscriptionViewModel by viewModels()

    private lateinit var subscriptionsAdapter: SubscriptionsAdapter

    private var subscriptionPlansItem: SubscriptionPlansItem? = null

    private val btnItemClickCallBack: (subscriptionPlansList: List<SubscriptionPlansItem?>?, position: Int) -> Unit =
        { subscriptionPlansList, position ->
            val newList = viewModel.changeSelectedSubscriptionPlan(subscriptionPlansList, position)
            subscriptionPlansItem = subscriptionPlansList?.get(position)
            subscriptionsAdapter.submitData(newList)

            if (subscriptionPlansItem?.isSelected == true) {
                binding.beginningOfSubscription.text = subscriptionPlansItem?.startDate.toString()
                binding.endOfSubscription.text = subscriptionPlansItem?.endDate.toString()
            } else {
                binding.beginningOfSubscription.text = ""
                binding.endOfSubscription.text = ""
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSubscriptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.subscriptions))

        viewModel.getPlansSubscription()

        initRecycler()
        setBtnListener()
        subscribeData()
    }

    private fun initRecycler() {
        binding.rvSubscriptionPlan.apply {
            subscriptionsAdapter = SubscriptionsAdapter(btnItemClickCallBack)
            adapter = subscriptionsAdapter
        }
    }

    private fun setBtnListener() {
        binding.btnConfirmSubscription.setOnClickListener {
            showAlertDialog(
                message = getString(R.string.do_you_already_want_to_subscribe) + " ${subscriptionPlansItem?.name}", //
                positiveButtonAction = {
                    // Positive button action
                    configurationPayTabsPayment(subscriptionPlansItem?.price?.toDouble())
                },
            )
        }
    }


    private fun subscribeData() {
        viewModel.getPlansSubscriptionResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    it.data?.subscriptionPlans?.filter { it?.isSelected == true }.also {
                        if (!it.isNullOrEmpty()) {
                            binding.beginningOfSubscription.text = it[0]?.startDate.toString()
                            binding.endOfSubscription.text = it[0]?.endDate.toString()
                        }
                    }
                    // bind data to the view
                    subscriptionsAdapter.submitData(it.data?.subscriptionPlans)
                }

                is ResponseHandler.Error -> {
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)

                    // show error message
                    showToast(it.message, 2)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(true)
                    viewModel.obsIsEmpty.set(false)
                    viewModel.obsIsFull.set(false)
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(false)
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)
                }

                else -> {}
            }
        }
    }

    private fun configurationPayTabsPayment(totalAmount: Double?) {
        val profileId = ""// PROFILE_ID
        val serverKey = ""
        val clientLey = ""

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
        //or PaymentSdkTokenFormat . NoneFormat ()
        //or PaymentSdkTokenFormat . AlphaNum20Format ()
        //or PaymentSdkTokenFormat . Digit22Format ()
        //or PaymentSdkTokenFormat . Digit16Format ()
        //or PaymentSdkTokenFormat . AlphaNum32Format ()

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
                amount ?: 0.0,
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

    @SuppressLint("LogNotTimber")
    override fun onCancel() {
        Toast.makeText(requireActivity(), "Payment Cancelled", Toast.LENGTH_SHORT).show()
        Log.d("TAG_PAY_TABS", "onPaymentCancel: 111")
        Log.d("TAG_PAY_TABS", "onPaymentCancel: 111 ${subscriptionPlansItem?.id!!}")
    }

    @SuppressLint("LogNotTimber")
    override fun onError(error: PaymentSdkError) {
        Log.d("TAG_PAY_TABS", "onError: $error")
        Toast.makeText(requireActivity(), "${error.msg}", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("LogNotTimber")
    override fun onResult(transactionResponseBody: TransactionResponseBody) {
        Toast.makeText(
            requireActivity(),
            "${transactionResponseBody.paymentResult?.responseMessage}",
            Toast.LENGTH_SHORT
        ).show()
        Log.d("TAG_PAY_TABS", "onResult: $transactionResponseBody")
    }

    @SuppressLint("LogNotTimber")
    override fun onPaymentFinish(paymentSdkTransactionDetails: PaymentSdkTransactionDetails) {
        Log.d("TAG_PAY_TABS", "onPaymentFinish: $paymentSdkTransactionDetails")
        Toast.makeText(
            requireActivity(),
            "${paymentSdkTransactionDetails.paymentResult?.responseMessage}",
            Toast.LENGTH_SHORT
        ).show()
        // when success payment call add subscription plan by plan id
        viewModel.addSubscriptionPlan(subscriptionPlansItem?.id!!)
    }

    @SuppressLint("LogNotTimber")
    override fun onPaymentCancel() {
        Toast.makeText(requireActivity(), "onPaymentCancel", Toast.LENGTH_SHORT).show()
        Log.d("TAG_PAY_TABS", "onPaymentCancel:222")
        Log.d("TAG_PAY_TABS", "onPaymentCancel:222 ${subscriptionPlansItem?.id!!}")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}