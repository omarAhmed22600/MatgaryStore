package com.brandsin.store.ui.main.home

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.brandsin.store.R
import com.brandsin.store.databinding.HomeFragmentHomeBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.main.homepage.StoreOrderItem
import com.brandsin.store.ui.activity.BaseFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.main.home.completedorders.CompletedOrdersFragment
import com.brandsin.store.ui.main.home.neworders.NewOrdersFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.iid.FirebaseInstanceId

class HomeFragment : BaseFragment(), Observer<Any?> {

    private lateinit var binding: HomeFragmentHomeBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment_home, container, false)
        // binding = HomeFragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        binding.viewModel = viewModel

        binding.ibMenu.setOnClickListener {
            (activity as HomeActivity).showDrawer()
        }

        val pagerAdapter = HomeOrdersPagerAdapter(this)
        binding.pager.adapter = pagerAdapter

        val tabLayout = binding.tabLayout

        TabLayoutMediator(tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    val customView =
                        requireActivity().layoutInflater.inflate(R.layout.raw_report_tab_item, null)
                    val itemName = customView.findViewById<TextView>(R.id.tv_itemName)
                    itemName.text = getString(R.string.new_orders)
                    itemName.setTextColor(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.color_primary
                        )
                    )
                    tab.customView = customView
                    customView.tag = 0

                    val viewName = customView!!.findViewById<TextView>(R.id.tv_itemName)
                    viewName.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                    viewName.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        resources.getDimension(R.dimen.tab_text_size)
                    )
                    viewName.background =
                        ContextCompat.getDrawable(requireActivity(), R.drawable.btn_orders_selected)
                    val myCustomFont: Typeface? =
                        ResourcesCompat.getFont(requireActivity(), R.font.cairo_semibold)
                    viewName.typeface = myCustomFont
                }

                1 -> {
                    val customView =
                        requireActivity().layoutInflater.inflate(R.layout.raw_report_tab_item, null)
                    val itemName = customView.findViewById<TextView>(R.id.tv_itemName)
                    itemName.text = getString(R.string.completed_orders)
                    tab.customView = customView
                    customView.tag = 1
                }
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                val viewName = p0!!.customView!!.findViewById<TextView>(R.id.tv_itemName)
                viewName.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.grey_subcategory
                    )
                )
                viewName.background =
                    ContextCompat.getDrawable(requireActivity(), R.drawable.btn_orders_unselected)
                val myCustomFont: Typeface? =
                    ResourcesCompat.getFont(requireActivity(), R.font.cairo_semibold)
                viewName.typeface = myCustomFont
                viewName.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.tab_text_size)
                )
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                val viewName = p0!!.customView!!.findViewById<TextView>(R.id.tv_itemName)
                viewName.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
                viewName.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.tab_text_size)
                )
                viewName.background =
                    ContextCompat.getDrawable(requireActivity(), R.drawable.btn_orders_selected)
                val myCustomFont: Typeface? =
                    ResourcesCompat.getFont(requireActivity(), R.font.cairo_semibold)
                viewName.typeface = myCustomFont
            }
        })

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)
        /*  (requireActivity() as HomeActivity).orderClickLiveData.observe(viewLifecycleOwner, Observer {
             // findNavController().navigate(R.id.home_to_order_details)
             // (requireActivity() as HomeActivity).navController.navigate(R.id.home_to_order_details)
              Navigation.findNavController(binding.root).navigate(R.id.completed_orders_to_order_details)
          })*/

        // Get token
        // [START retrieve_current_token]
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result.token
                viewModel.deviceTokenRequest.token = token
                viewModel.deviceToken()

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
            })
        // [END retrieve_current_token]

        binding.addStory.setOnClickListener {
            findNavController().navigate(R.id.home_to_add_story)
        }

        binding.consLink.setOnClickListener {
            binding.consLink.visibility = View.GONE
            val action = HomeFragmentDirections
                .homeToAcceptConnectingMain(viewModel.requestId, viewModel.storeName)
            findNavController().navigate(action)
        }
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        value.let {
            when (it) {
                is StoreOrderItem -> {
                    //  Toasty.success(requireActivity(), "success").show()
                    //  findNavController().navigate(R.id.home_to_order_details)
                }

                is Int -> {
                    when (it) {
                        Codes.NOTIFICATION_CLICK -> {
                            findNavController().navigate(R.id.home_to_notifications)
                        }

                        Codes.SHOW_Link -> {
                            binding.storeName.text = viewModel.storeName
                            binding.consLink.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as HomeActivity).customizeToolbar("", false)
    }

    override fun onPause() {
        super.onPause()
        (activity as HomeActivity).lockDrawer()
    }

    class HomeOrdersPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> NewOrdersFragment()
                else -> CompletedOrdersFragment()
            }
        }
    }
}

