package com.brandsin.store.ui.dialogs.monthstime

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.brandsin.store.databinding.DialogReportsMonthlyDateBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.user.model.constants.Params
import java.util.*

class DialogReportTimeMonthlyFragment (val date: Date = Date())  : DialogFragment(), Observer<Any?>
{
    lateinit  var  binding: DialogReportsMonthlyDateBinding
    lateinit var viewModel : DateViewModel
    private var calendar: DateRangeCalendarView? = null
    var month = ""
    var year = ""
    var from = ""
    var to = ""

    companion object {
        private const val MAX_YEAR = 2099
    }
    private var listener: DatePickerDialog.OnDateSetListener? = null

    fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DialogReportsMonthlyDateBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DateViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        val cal: Calendar = Calendar.getInstance().apply { time = date }

        binding.pickerMonth.run {
            minValue = 1
            maxValue = 12
            value = cal.get(Calendar.MONTH)
            displayedValues = arrayOf("Jan","Feb","Mar","Apr","May","June","July", "Aug","Sep","Oct","Nov","Dec") }

        binding.pickerYear.run {
            val year = cal.get(Calendar.YEAR)
            minValue = year
            maxValue = MAX_YEAR
            value = year
        }

        AlertDialog.Builder(requireContext())
                .setView(binding.root)
                .create()
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        it.let {
            if (it is Int)
            {
                when (it) {
                    Codes.CONFIRM_CLICKED -> {
                        listener?.onDateSet(null, binding.pickerYear.value, binding.pickerMonth.value, 1)
                        year = binding.pickerYear.value.toString()
                        month = binding.pickerMonth.value.toString()
                        if (month.length ==1){
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
    }
}