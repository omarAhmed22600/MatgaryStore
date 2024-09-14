package com.brandsin.store.ui.main.reports.detailed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.ReportsFragmentDetailedBinding
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.dialogs.dailytime.DialogDailyTimeFragment
import com.brandsin.store.ui.dialogs.monthstime.DialogReportTimeMonthlyFragment

class DetailedReportsFragment : BaseHomeFragment() {

    private lateinit var binding: ReportsFragmentDetailedBinding

    private lateinit var viewModel: DetailedReportsViewModel

    private var isDaily: Int = 1

    private val btnDailyTimeCallback: (from: String, to: String) -> Unit = { from, to ->
        viewModel.type = "daily"
        viewModel.from = from
        viewModel.to = to
        viewModel.getReports()
    }

    private val btnMonthlyTimeCallback: (from: String, to: String) -> Unit = { from, to ->
        viewModel.type = "monthly"
        viewModel.from = from
        viewModel.to = to
        viewModel.getReports()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.reports_fragment_detailed, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DetailedReportsViewModel::class.java]
        binding.viewModel = viewModel

        // viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        viewModel.getReports()

        viewModel.showProgress().observe(viewLifecycleOwner) { aBoolean ->
            if (!aBoolean!!) {
                binding.progressLayout.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                binding.progressLayout.visibility = View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            }
        }

        // Get radio group selected item using on checked change listener
        binding.rbChoices.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = requireActivity().findViewById(checkedId)
            if (radio.id == R.id.rb_daily) {
                isDaily = 1
                viewModel.type = "daily"
                viewModel.from = ""
                viewModel.to = ""
                viewModel.getReports()
            }
            if (radio.id == R.id.rb_monthly) {
                isDaily = 2
                viewModel.type = "monthly"
                viewModel.from = ""
                viewModel.to = ""
                viewModel.getReports()
            }
        }

        setBtnListener()
    }

    private fun setBtnListener() {
        binding.ibCalendar.setOnClickListener {
            if (isDaily == 1) {
                /*Utils.startDialogActivity(
                    requireActivity(),
                    DialogDailyTimeFragment::class.java.name,
                    Codes.SHOW_DAILY_FILTER,
                    null
                )*/

                val dialogFragment = DialogDailyTimeFragment(btnDailyTimeCallback)
                dialogFragment.show(
                    requireActivity().supportFragmentManager,
                    DialogDailyTimeFragment::class.java.simpleName
                )

            } else {
                /*Utils.startDialogActivity(
                    requireActivity(),
                    DialogReportTimeMonthlyFragment::class.java.name,
                    Codes.SHOW_MONTH_FILTER,
                    null
                )*/
                val dialogFragment = DialogReportTimeMonthlyFragment(btnMonthlyTimeCallback)
                dialogFragment.show(
                    requireActivity().supportFragmentManager,
                    DialogReportTimeMonthlyFragment::class.java.simpleName
                )
            }
        }
    }

    /* override fun onChanged(value: Any?) {
        if (value == null) return
        value.let {
            if (it is Int) {
                when (it) {
                    Codes.SHOW_CALENDAR_DIALOG -> {
                        if (isDaily == 1) {
                            Utils.startDialogActivity(
                                requireActivity(),
                                DialogDailyTimeFragment::class.java.name,
                                Codes.SHOW_DAILY_FILTER,
                                null
                            )
                        } else {
                            Utils.startDialogActivity(
                                requireActivity(),
                                DialogReportTimeMonthlyFragment::class.java.name,
                                Codes.SHOW_MONTH_FILTER,
                                null
                            )
                        }
                    }
                }
            }
        }
    } */
}