package com.brandsin.store.ui.auth.authways

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.AuthFragmentLoginWaysBinding
import com.brandsin.store.ui.activity.auth.BaseAuthFragment

class LoginWaysFragment : BaseAuthFragment(), Observer<Any?>
{
    lateinit var binding: AuthFragmentLoginWaysBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment_login_ways, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.ways_to_login)
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.ways_to_register)
        }
    }

    override fun onChanged(t: Any?) {
    }

}