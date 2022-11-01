package com.brandsin.store.ui.main.orderdetails

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentOrderDetailsBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.constants.Const
import com.brandsin.store.model.main.homepage.OrderDetailsResponse
import com.brandsin.store.model.main.order.updatestatus.UpdateStatusOrderResponse
import com.brandsin.store.network.Status
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.utils.Utils
import com.brandsin.store.utils.observe
import com.brandsin.user.model.constants.Params
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class OrderDetailsFragment : BaseHomeFragment(), Observer<Any?>, OnMapReadyCallback
{
    private lateinit var viewModel: OrderDetailsViewModel
    private lateinit var binding : HomeFragmentOrderDetailsBinding
    val fragmentArgs : OrderDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_order_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrderDetailsViewModel::class.java)
        binding.viewModel = viewModel

        setBarName(getString(R.string.order_details))

        if (requireArguments().get(Params.ORDER_ID)!=null){
            viewModel.orderId = requireArguments().get(Params.ORDER_ID) as Int
        }else{
            viewModel.orderId = fragmentArgs.orderId
        }


        viewModel.getOrderDetails2()
        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        binding.rgAccept.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                val rb = requireActivity().findViewById(checkedId) as RadioButton
                viewModel.obsAcceptWay.set(rb.text.toString())
            }
        })

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
                            if(it.data.order!!.userNotes.isNullOrEmpty()){
                                binding.orderNotesLayout.visibility = View.GONE
                                binding.consNoteTxt.visibility = View.GONE
                            }else{
                                binding.orderNotesLayout.visibility = View.VISIBLE
                                binding.consNoteTxt.visibility = View.VISIBLE
                            }

                            if (it.data.order!!.status == "pending") {
                                binding.rgAccept.visibility = View.VISIBLE
                                binding.btnConfirm.visibility = View.VISIBLE
                            }else{
                                binding.rgAccept.visibility = View.GONE
                                binding.btnConfirm.visibility = View.GONE
                            }
                        }
                        is UpdateStatusOrderResponse -> {
                            showToast(getString(R.string.request_accepted), 2)
                            lifecycleScope.launch {
                                delay(2000)
                                requireActivity().startActivity(Intent(requireActivity(), HomeActivity::class.java))
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
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        it.let {
            if (it is Int)
            {
                when (it) {
                    Codes.CALL_CLICKED -> {
                        Utils.callPhone(requireActivity(), viewModel.orderDetails.order!!.phoneNumber!!)
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?)
    {
        observe(viewModel.isMapReady) {
            when (it) {
                true -> {
                    googleMap!!.clear()
                    val latLng = LatLng(viewModel.orderDetails.order!!.lat!!.toDouble(), viewModel.orderDetails.order!!.lng!!.toDouble())
                    val option = MarkerOptions().position(latLng)
                    googleMap.addMarker(option)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Const.zoomLevel))
                }
            }
        }
    }
}