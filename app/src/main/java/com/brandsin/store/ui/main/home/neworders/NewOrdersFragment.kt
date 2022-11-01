package com.brandsin.store.ui.main.home.neworders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentNewOrdersBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.homepage.StoreOrderItem
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity

class NewOrdersFragment : BaseHomeFragment(), Observer<Any?>
{
    private lateinit var newOrdersViewModel: NewOrdersViewModel
    private lateinit var binding : HomeFragmentNewOrdersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_new_orders, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        newOrdersViewModel = ViewModelProvider(this).get(NewOrdersViewModel::class.java)
        binding.viewModel = newOrdersViewModel

        (requireActivity() as HomeActivity).customizeToolbar("" , false)

        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            newOrdersViewModel.getUserStatus()
        }

        newOrdersViewModel.mutableLiveData.observe(viewLifecycleOwner, this)
        newOrdersViewModel.newOrdersAdapter.newOrderItemLiveData.observe(viewLifecycleOwner, Observer {
            (requireActivity() as HomeActivity).orderClickLiveData.value = it
            (requireActivity() as HomeActivity).orderClickLiveData.value = null
        })

        binding.rvNewOrders.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            var lastX = 0
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> lastX = e.x.toInt()
                    MotionEvent.ACTION_MOVE -> {
                        val isScrollingRight = e.x < lastX
                        if (isScrollingRight && (binding.rvNewOrders.layoutManager as LinearLayoutManager).
                            findLastCompletelyVisibleItemPosition() == binding.rvNewOrders.adapter!!.itemCount - 1 ||
                            !isScrollingRight && (binding.rvNewOrders.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
                        ) {
                            //  binding.p.setUserInputEnabled(true)
                        } else {
                            //  viewPager.setUserInputEnabled(false)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        lastX = 0
                        // viewPager.setUserInputEnabled(true)
                    }
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        it.let {
            if (it is StoreOrderItem)
            {
                findNavController().navigate(R.id.new_orders_to_order_details)
            }
            if (it is Int)
            {
                when (it) {
                    Codes.RATING_SUCCESS ->
                    {
                        // do what you want
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }
}