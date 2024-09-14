package com.brandsin.store.ui.dialogs.dailytime

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView
import com.brandsin.store.R
import com.brandsin.store.databinding.DialogReportTimeDailyBinding
import com.brandsin.store.ui.dialogs.monthstime.DateViewModel
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DialogDailyTimeFragment(
    private val btnDailyTimeCallback: (from: String, to: String) -> Unit,
) : DialogFragment() {

    private lateinit var binding: DialogReportTimeDailyBinding

    lateinit var viewModel: DateViewModel

    private var calendar: DateRangeCalendarView? = null

    var from = ""
    var to = ""
    var convertDate = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogReportTimeDailyBinding.inflate(inflater, container, false)
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

        binding.calendar.setCalendarListener(calendarListener)

        /*requireActivity().findViewById<View>(R.id.btn_search)
            .setOnClickListener { v: View? -> calendar!!.resetAllSelectedViews() }*/

        val startMonth = Calendar.getInstance()
        val current = startMonth.clone() as Calendar
        current.add(Calendar.MONTH, 0)
        binding.calendar.setCurrentMonth(current)

        binding.btnSearch.setOnClickListener {
            btnDailyTimeCallback.invoke(from, to)
            calendar?.resetAllSelectedViews()
            dismissNow()
        }
    }

    /*override fun onChanged(value: Any?) {
        if (value == null) return
        value.let {
            if (it is Int) {
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
    }*/

    private val calendarListener: CalendarListener = object : CalendarListener {
        override fun onFirstDateSelected(startDate: Calendar) {
            convertDate(startDate.time.toString())
            from = convertDate
            to = ""
        }

        override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {
            convertDate(startDate.time.toString())
            from = convertDate
            convertDate(endDate.time.toString())
            to = convertDate
        }
    }

    fun convertDate(dateString: String): String {
        var date: Date? = null
        val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH)
        try {
            date = formatter.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val f2: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        convertDate = f2.format(date)
        return convertDate
    }
}