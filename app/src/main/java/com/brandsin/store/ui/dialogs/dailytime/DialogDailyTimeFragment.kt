package com.brandsin.store.ui.dialogs.dailytime

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.brandsin.store.R
import com.brandsin.store.databinding.DialogReportTimeDailyBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.dialogs.monthstime.DateViewModel
import com.brandsin.user.model.constants.Params
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DialogDailyTimeFragment  : DialogFragment(), Observer<Any?>
{
    lateinit  var  binding: DialogReportTimeDailyBinding
    lateinit var viewModel : DateViewModel
    private var calendar: DateRangeCalendarView? = null
    var from = ""
    var to = ""
    var convertDate = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = DialogReportTimeDailyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DateViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        binding.calendar.setCalendarListener(calendarListener)

        requireActivity().findViewById<View>(R.id.btn_search).setOnClickListener(View.OnClickListener { v: View? -> calendar!!.resetAllSelectedViews() })

        val startMonth = Calendar.getInstance()
        val current = startMonth.clone() as Calendar
        current.add(Calendar.MONTH, 1)
        binding.calendar.setCurrentMonth(current)
    }

    override fun onChanged(it: Any?)
    {
        if(it == null) return
        it.let {
            if (it is Int)
            {
                when (it) {
                    Codes.CONFIRM_CLICKED -> {
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

    private val calendarListener: CalendarListener = object : CalendarListener {
        override fun onFirstDateSelected(startDate: Calendar) {
            convertDate(startDate.time.toString())
            from =convertDate
            to = ""
        }

        override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
            convertDate(startDate.time.toString())
            from =convertDate
            convertDate(endDate.time.toString())
            to =convertDate
        }
    }
    fun convertDate(dateString: String): String {

        var date: Date? = null
        val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
        val temp = dateString
        try {
            date = formatter.parse(temp)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val f2: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        convertDate = f2.format(date)
        return convertDate
    }
}