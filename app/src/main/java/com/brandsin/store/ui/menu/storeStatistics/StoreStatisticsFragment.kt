package com.brandsin.store.ui.menu.storeStatistics

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.Position
import com.anychart.enums.HoverMode
import com.anychart.enums.TooltipPositionMode
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentStoreStatisticsBinding
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.ui.menu.storecode.StoreCodeViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class StoreStatisticsFragment : BaseHomeFragment() {
    private lateinit var binding: FragmentStoreStatisticsBinding
    private lateinit var viewModel: StoreStatisticsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_store_statistics, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[StoreStatisticsViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        (requireActivity() as HomeActivity).customizeToolbar("", false)
        setupBarChart()
        setupPieChart()
        setupYearSelector()
        binding.rvProducts.adapter = ProductAdapter()
        viewModel.showProducts.observe(viewLifecycleOwner) {
            if (it!=null)
            {
                Timber.e("showProducts changed to $it")
            }
        }

    }

    private fun setupYearSelector() {
        binding.cvYearSelector.setOnClickListener {
            val yearPicker = NumberPicker(requireContext()).apply {
                minValue = 2000 // Set to your desired start year
                maxValue = 2024 // Set to your desired end year
                wrapSelectorWheel = false // Disable wrapping
            }

            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.choose_year))
                .setView(yearPicker)
                .setPositiveButton(getString(R.string.done)) { _, _ ->
                    val selectedYear = yearPicker.value
                    // Handle the selected year
                    // e.g., update your UI or save the selected year
                    binding.tvSelectedYear.text = selectedYear.toString()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }    }

    private fun setupPieChart() {
        // Initialize PieChart
        val pieChart = binding.pieChart

        // Create the PieEntry list
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(60f, "Men"))
        entries.add(PieEntry(40f, "Women"))

        // Create PieDataSet and customize its appearance
        val dataSet = PieDataSet(entries, "Gender Ratio")
        dataSet.colors = listOf(Color.BLACK, Color.YELLOW)
        dataSet.sliceSpace = 3f // Space between slices
        dataSet.selectionShift = 5f

        // Create PieData and set it to PieChart
        val data = PieData(dataSet)
        data.setValueTextColor(Color.TRANSPARENT)

        pieChart.data = data
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 70f // Donut effect
        pieChart.setEntryLabelColor(Color.TRANSPARENT) // Label color inside the pie
        pieChart.setUsePercentValues(true) // Use percentage values for display
        pieChart.description.isEnabled = false // Disable description
        pieChart.legend.isEnabled = false // Disable legend to match your design

        // Refresh the chart
        pieChart.invalidate()
    }


    private fun setupBarChart() {
        val anyChartView = binding.anyChartView // Add AnyChartView in your XML layoutd
// Create a Cartesian (Column) chart for vertical bars
        val cartesian: Cartesian = AnyChart.column()
cartesian.credits().enabled(false)
// Define data for the chart
        val data = listOf(
            ValueDataEntry("الخبر", 60000),  // Real value
            ValueDataEntry("جدة", 70000),
            ValueDataEntry("مكة", 80000),
            ValueDataEntry("الطائف", 50000),
            ValueDataEntry("الدمام", 90000),
            ValueDataEntry("الرياض", 100000)
        )

// Set chart data
        val columnData: Column = cartesian.column(data)

// Customize labels and styling
//        cartesian.title("أكثر المدن طلبا")
        cartesian.xAxis(0).title(getString(R.string.cities))
        cartesian.yAxis(0).title(getString(R.string.orders))

// Format the y-axis to display values in thousands with 'k'
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: ")

// Format the bar labels to show the real value followed by 'k'
        columnData.labels().enabled(true)
        columnData.labels()
            .format("{%Value}{groupsSeparator: }") // Show the real value without formatting inside the bars
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)

// Hover configuration (optional for highlighting)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
// Set color palette for bars (optional)
        columnData.color("#673AB7") // Use your preferred color

// Assign the chart to the view
        anyChartView.setChart(cartesian)
    }
}