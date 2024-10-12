package com.brandsin.store.ui.main.orderdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentOrderDetailsBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.constants.Const
import com.brandsin.store.model.main.homepage.OrderDetailsResponse
import com.brandsin.store.model.main.order.updatestatus.UpdateStatusOrderResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.dialogs.rejectOrder.DialogRejectOrderFragment
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.observe
import com.brandsin.store.utils.visible
import com.brandsin.store.model.constants.Params
import com.brandsin.user.ui.chat.model.MessageModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.bind
import timber.log.Timber
import java.util.UUID

class OrderDetailsFragment : BaseHomeFragment(), Observer<Any?>, OnMapReadyCallback {

    private lateinit var binding: HomeFragmentOrderDetailsBinding

    private lateinit var viewModel: OrderDetailsViewModel

     private val fragmentArgs: OrderDetailsFragmentArgs by navArgs()

    private val btnRejectedOrderCallback: () -> Unit = {
        viewModel.setUpdateStatusOrder("rejected_by_store")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_fragment_order_details,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[OrderDetailsViewModel::class.java]
        binding.viewModel = viewModel

        setBarName(getString(R.string.order_details))

        viewModel.orderId = fragmentArgs.orderId

        /*if (fragmentArgs.orderId == null) {
            viewModel.orderId = requireArguments().getInt(Params.ORDER_ID)
        } else {
            viewModel.orderId = fragmentArgs.orderId
        }*/

        viewModel.getOrderDetails2()

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        binding.rgAccept.setOnCheckedChangeListener { _, checkedId ->
            val rb = requireActivity().findViewById(checkedId) as RadioButton
            viewModel.obsAcceptWay.set(rb.text.toString())
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
                        is OrderDetailsResponse -> {
                            if (it.data.order!!.userNotes.isNullOrEmpty()) {
                                binding.orderNotesLayout.visibility = View.GONE
                                binding.consNoteTxt.visibility = View.GONE
                            } else {
                                binding.orderNotesLayout.visibility = View.VISIBLE
                                binding.consNoteTxt.visibility = View.VISIBLE
                            }

                            println("hasPackaging ${it.data.order.hasPackaging}")
                            println("packagingPrice ${it.data.order.packagingPrice}")
                            if (it.data.order.hasPackaging == 1) {
                                binding.txtPackagingPrice.visible()
                                binding.packagingPrice.visible()
                            } else {
                                binding.txtPackagingPrice.gone()
                                binding.packagingPrice.gone()
                            }

                            if (it.data.order.store?.hasDelivery == 1) {
                                binding.rbAcceptonly.visible()
                                binding.tvRedioDes1.visible()
                            } else {
                                binding.rbAcceptonly.gone()
                                binding.tvRedioDes1.gone()
                            }

                            if (it.data.order.status == "pending") {
                                binding.btnPrintInvoice.visibility = View.GONE
                                binding.rgAccept.visibility = View.VISIBLE
                                binding.btnAccept.visibility = View.VISIBLE
                                binding.btnReject.visibility = View.VISIBLE
//                                binding.btnConfirm.visibility = View.VISIBLE
                            } else {
                                //todo
//                                binding.btnPrintInvoice.visibility = View.VISIBLE
                                binding.rgAccept.visibility = View.GONE
                                binding.btnAccept.visibility = View.GONE
                                binding.btnReject.visibility = View.GONE
//                                binding.btnConfirm.visibility = View.GONE
                            }
                        }

                        is UpdateStatusOrderResponse -> {
                            showToast(getString(R.string.success), 2)
                            lifecycleScope.launch {
                                delay(2000)
                                requireActivity().startActivity(
                                    Intent(
                                        requireActivity(),
                                        HomeActivity::class.java
                                    )
                                )
                                requireActivity().finishAffinity()
                            }
                        }
                    }
                }

                else -> {
                    Timber.e(it.message)
                }
            }
        }

        setBtnListener()
    }

    private fun setBtnListener() {
        binding.btnReject.setOnClickListener {
            viewModel.setClickable()
            // viewModel.setUpdateStatusOrder("rejected_by_store")
            val dialogFragment = DialogRejectOrderFragment(btnRejectedOrderCallback)
            dialogFragment.show(
                requireActivity().supportFragmentManager,
                DialogRejectOrderFragment::class.java.simpleName
            )
        }
        binding.whatsApp.setOnClickListener {
            var phone = viewModel.orderDetails.order?.phoneNumber
            //+05......
            if (phone.orEmpty().startsWith("+966").not()){
                phone.orEmpty().filter { it == '+' }
                phone = "+966$phone"
            }
            val url =
                "https://api.whatsapp.com/send?phone=$phone"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.icChat.setOnClickListener {
            val messageModel = MessageModel(
                avaterstore = viewModel.orderDetails.order?.store?.thumbnail.toString(),
                avateruser = PrefMethods.getStoreData()?.thumbnail.toString(),
                messageType = "private",
                senderName = PrefMethods.getUserData()?.name.toString(),
                storename = viewModel.orderDetails.order?.store?.name.toString(),
                senderId = PrefMethods.getStoreData()?.id.toString().trim(),
                storeId = viewModel.orderDetails.order?.storeId.toString(),
                message = "",
                messageId = UUID.randomUUID().toString(),
                date = System.currentTimeMillis().toString(),
                type = "",
                typeBay = "user"
            )

            // inboxViewModel.messageItem = messageModel
            val bundle = Bundle()
            bundle.putString("Avatar_Store", messageModel.avaterstore)
            bundle.putString("Avatar_User", messageModel.avateruser)
            bundle.putString("Sender_Id", messageModel.senderId)
            bundle.putString("Sender_Name", messageModel.senderName)
            bundle.putString("Store_Id", PrefMethods.getStoreData()?.userId.toString())
            bundle.putString("Store_Name", messageModel.storename)
            findNavController().navigate(R.id.messageFragment, bundle)
        }
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        value.let {
            if (it is Int) {
                when (it) {
                    Codes.CALL_CLICKED -> {
                        Utils.callPhone(
                            requireActivity(),
                            viewModel.orderDetails.order!!.phoneNumber!!
                        )
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        observe(viewModel.isMapReady) {
            when (it) {
                true -> {
                    googleMap.clear()
                    val latLng = LatLng(
                        viewModel.orderDetails.order?.lat?.toDouble() ?: 0.0,
                        viewModel.orderDetails.order?.lng?.toDouble() ?: 0.0
                    )
                    val option = MarkerOptions().position(latLng)
                    googleMap.addMarker(option)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Const.zoomLevel))
                }

                else -> {}
            }
        }
    }
}