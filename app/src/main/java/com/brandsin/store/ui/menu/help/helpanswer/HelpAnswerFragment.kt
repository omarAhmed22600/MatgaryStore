package com.brandsin.store.ui.menu.help.helpanswer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.brandsin.store.R
import com.brandsin.store.databinding.MenuFragmentHelpAnswerBinding
import com.brandsin.store.ui.activity.BaseHomeFragment
import org.jsoup.Jsoup

class HelpAnswerFragment : BaseHomeFragment() {

    private lateinit var binding: MenuFragmentHelpAnswerBinding

    private val fragmentArgs: HelpAnswerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.menu_fragment_help_answer, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarName(getString(R.string.help))

        val helpQues = fragmentArgs.helpQues
        val phone = fragmentArgs.phoneNumber
        binding.tvTitle.text = helpQues.title
        binding.tvAnswer.text = Jsoup.parse(helpQues.content.toString()).text() ?: ""

        binding.btnContactUs.setOnClickListener {
            // Utils.callPhone(requireActivity(), phone)
            //
            findNavController().navigate(R.id.nav_contact)
        }
    }
}