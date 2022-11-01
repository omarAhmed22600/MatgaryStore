package com.brandsin.store.ui.main.home.completedorders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentCompletedOrdersBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.auth.AuthActivity
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.utils.PrefMethods
import com.brandsin.store.ui.activity.BaseFragment

class CompletedOrdersFragment : BaseFragment(), Observer<Any?>
{
    private lateinit var viewModel: CompletedOrdersViewModel
    private lateinit var binding : HomeFragmentCompletedOrdersBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_fragment_completed_orders,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CompletedOrdersViewModel::class.java)
        binding.viewModel = viewModel

        (requireActivity() as HomeActivity).customizeToolbar("", false)

        binding.swipeLayout.setOnRefreshListener {
            binding.swipeLayout.isRefreshing = false
            viewModel.getUserStatus()
        }
        
        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)
        viewModel.completedOrdersAdapter.completedOrderItemLiveData.observe(
            viewLifecycleOwner,
            Observer {
                (requireActivity() as HomeActivity).orderClickLiveData.value = it
                (requireActivity() as HomeActivity).orderClickLiveData.value = null
            })

        binding.rvOrders.addOnItemTouchListener(object : OnItemTouchListener {
            var lastX = 0
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> lastX = e.x.toInt()
                    MotionEvent.ACTION_MOVE -> {
                        val isScrollingRight = e.x < lastX
                        if (isScrollingRight && (binding.rvOrders.layoutManager as LinearLayoutManager).
                            findLastCompletelyVisibleItemPosition() == binding.rvOrders.adapter!!.itemCount - 1 ||
                            !isScrollingRight && (binding.rvOrders.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
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
            when (it) {
                Codes.LOGIN_CLICKED -> {
                    PrefMethods.saveIsAskedToLogin(true)
                    requireActivity().startActivity(Intent(requireActivity(),
                            AuthActivity::class.java))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        when {
            PrefMethods.getIsAskedToLogin() -> {
                viewModel.getUserStatus()
                PrefMethods.saveIsAskedToLogin(false)
            }
        }
    }
}