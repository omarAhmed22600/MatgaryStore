package com.brandsin.store.ui.dialogs.monthstime

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.brandsin.store.databinding.DialogReportsMonthlyDateBinding
import java.util.Calendar
import java.util.Date

class DialogReportTimeMonthlyFragment(
    private val btnMonthlyTimeCallback: (from: String, to: String) -> Unit,
    val date: Date = Date()
) : DialogFragment() {

    private lateinit var binding: DialogReportsMonthlyDateBinding

    lateinit var viewModel: DateViewModel

    private var calendar: DateRangeCalendarView? = null

    private var month = ""
    private var year = ""
    var from = ""
    var to = ""

    companion object {
        private const val MAX_YEAR = 2099
    }

    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogReportsMonthlyDateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DateViewModel::class.java]
        binding.viewModel = viewModel

        // Set the window background to transparent
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set the window attributes for match_parent
        val params = dialog?.window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params

        val cal: Calendar = Calendar.getInstance().apply { time = date }

        binding.pickerMonth.isClickable = false

        binding.pickerMonth.run {
            minValue = 1
            maxValue = 12
            value = cal.get(Calendar.MONTH)
            displayedValues = arrayOf(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "June",
                "July",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
            )
        }

        binding.pickerYear.run {
            val year = cal.get(Calendar.YEAR)
            minValue = year-10
            maxValue = MAX_YEAR
            value = year
        }

        AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        setBtnListener()
    }

    private fun setBtnListener() {
        binding.btnSearch.setOnClickListener {
            listener?.onDateSet(
                null,
                binding.pickerYear.value,
                binding.pickerMonth.value,
                1
            )
            year = binding.pickerYear.value.toString()
            month = binding.pickerMonth.value.toString()
            if (month.length == 1) {
                month = "0$month"
            }
            from = "$year-$month-01"
            to = "$year-$month-31"
            /*val intent = Intent()
            intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
            intent.putExtra("from", from)
            intent.putExtra("to", to)
            requireActivity().setResult(Codes.DIALOG_ORDER_TIME, intent)
            requireActivity().finish()*/
            btnMonthlyTimeCallback.invoke(from, to)
            dismissNow()
        }
    }

    /*override fun onChanged(value: Any?) {
        if (value == null) return
        value.let {
            if (it is Int) {
                when (it) {
                    Codes.CONFIRM_CLICKED -> {
                        listener?.onDateSet(
                            null,
                            binding.pickerYear.value,
                            binding.pickerMonth.value,
                            1
                        )
                        year = binding.pickerYear.value.toString()
                        month = binding.pickerMonth.value.toString()
                        if (month.length == 1) {
                            month = "0$month"
                        }
                        from = "$year-$month-01"
                        to = "$year-$month-31"
                        val intent = Intent()
                        intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                        intent.putExtra("from", from)
                        intent.putExtra("to", to)
                        requireActivity().setResult(Codes.DIALOG_ORDER_TIME, intent)
                        requireActivity().finish()
                    }
                }
            }
        }
    }*/
}