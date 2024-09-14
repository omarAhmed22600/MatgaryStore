package com.brandsin.store.ui.dialogs.offertime

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.databinding.DialogOfferTimeBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.utils.wheelview.OrderTimeWheelView
import com.brandsin.store.model.constants.Params
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

class DialogOfferTimeFragment : DialogFragment(), Observer<Any?> {

    private lateinit var binding: DialogOfferTimeBinding

    lateinit var viewModel: DateViewModel

    private lateinit var rvTimes: OrderTimeWheelView
    private var datesList: ArrayList<OfferDateItem> = ArrayList()
    var requestCode: Int = 0
    var startDate = ""
    var endDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            if (requireArguments().containsKey(Params.DIALOG_DATE_REQUEST)) {
                requestCode = requireArguments().getInt(Params.DIALOG_DATE_REQUEST, 0)
                startDate = requireArguments().getString("START_DATE").toString()
                endDate = requireArguments().getString("END_DATE").toString()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogOfferTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DateViewModel::class.java]
        binding.viewModel = viewModel

        if (startDate != "null") {
            viewModel.selectedDate = startDate.substring(0, startDate.lastIndexOf(" "))
            viewModel.selectedTime = startDate.substring(startDate.lastIndexOf(" "))
        }
        if (endDate != "null") {
            viewModel.selectedDate = endDate.substring(0, endDate.lastIndexOf(" "))
            viewModel.selectedTime = endDate.substring(endDate.lastIndexOf(" "))
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        for (i in 0..30) {
            val calendar: Calendar = GregorianCalendar()
            calendar.add(Calendar.DATE, i)
            val date1: String = sdf.format(calendar.time)

            val date: Date = sdf.parse(date1)
            val outFormat = SimpleDateFormat("EEEE")
            val goal: String = outFormat.format(date)

            datesList.add(OfferDateItem(date1, goal, getDaHours()))
        }

        viewModel.setDates(datesList)

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)
        viewModel.datesAdapter.dateLiveData.observe(viewLifecycleOwner, this)

        rvTimes = binding.timeWheelView
        rvTimes.setOnSelectedStringCallback(object : OrderTimeWheelView.OnSelectedStringCallback {
            override fun onSelectedString(selectedString: String) {
                viewModel.selectedTime = selectedString
            }
        })
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        value.let {
            when (it) {
                is Int -> {
                    when (it) {
                        Codes.CONFIRM_CLICKED -> {
                            val intent = Intent()
                            intent.putExtra(Params.DIALOG_CLICK_ACTION, 1)
                            intent.putExtra(
                                Params.OFFER_TIME,
                                viewModel.selectedDate + "" + viewModel.selectedTime
                            )
                            intent.putExtra(Params.DIALOG_DATE_REQUEST, requestCode)
                            requireActivity().setResult(Codes.DIALOG_OFFER_TIME, intent)
                            requireActivity().finish()
                        }
                    }
                }

                is OfferDateItem -> {
                    rvTimes.setStringItemList(it.time as ArrayList<String>)
                    viewModel.selectedDate = it.date.toString()
                }
            }
        }
    }

    private fun getDaHours(): ArrayList<String> {
        val timesList: ArrayList<String> = ArrayList()
        timesList.add("00:00:00")
        timesList.add("01:00:00")
        timesList.add("02:00:00")
        timesList.add("03:00:00")
        timesList.add("04:00:00")
        timesList.add("05:00:00")
        timesList.add("06:00:00")
        timesList.add("07:00:00")
        timesList.add("08:00:00")
        timesList.add("09:00:00")
        timesList.add("10:00:00")
        timesList.add("11:00:00")
        timesList.add("12:00:00")
        timesList.add("13:00:00")
        timesList.add("14:00:00")
        timesList.add("15:00:00")
        timesList.add("16:00:00")
        timesList.add("17:00:00")
        timesList.add("18:00:00")
        timesList.add("19:00:00")
        timesList.add("20:00:00")
        timesList.add("21:00:00")
        timesList.add("22:00:00")
        timesList.add("23:00:00")
        return timesList
    }
}