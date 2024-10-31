package com.brandsin.store.ui.menu.storeStatistics

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
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
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.visible
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
    private lateinit var cartesian: Cartesian
    private lateinit var column: Column
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
//        setupPieChart()
        setupYearSelector()
        setupBarChart(listOf())
        binding.rvProducts.adapter = ProductAdapter()
        viewModel.showProducts.observe(viewLifecycleOwner) {
            if (it!=null)
            {
                Timber.e("showProducts changed to $it")
            }
        }
        viewModel.selectedYear.observe(viewLifecycleOwner){
            viewModel.getData()
        }

        viewModel.statisticsResponse.observe(viewLifecycleOwner) {
            if (it!=null)
            {
                "${it.data.totalProducts} ${getString(R.string.product)}".also {products -> binding.tvProductNumber.text = products }
                binding.tvStoreFollowersCount.text = "${it.data.statistics.followersCount}"
                binding.tvStoriesViewsCount.text = it.data.statistics.storyViews.toString()
                binding.tvStoreRatingCount.text = it.data.statistics.storeRating.toString()
                binding.tvProductsRatingCount.text = it.data.statistics.productRating.toString()
                val list = mutableListOf<DataEntry>()
                it.data.cities.forEach {city ->
                    list.add(
                        ValueDataEntry(
                            city.cityName,
                            city.orderCount
                        )
                    )
                }
                Timber.e("city list is $list")
               /* val data = listOf(
                    ValueDataEntry("الخبر", 60000),  // Real value
                    ValueDataEntry("جدة", 70000),
                    ValueDataEntry("2الخبر", 200000),  // Real value
                    ValueDataEntry("جدة2", 400000),
                    ValueDataEntry("3الخبر", 60000),  // Real value
                    ValueDataEntry("3جدة", 70000),
                )
*/

                setupBarChart(list.toList())
                if (list.isEmpty())
                {
                    binding.noData.visible()
                }
                else
                {
                    binding.noData.gone()

                }
                // Create a new Cartesian (Column) chart for vertical bars

                val productList = mutableListOf<Product>()
                it.data.bestSellingProducts.forEachIndexed { index, bestSellingProduct ->
                    productList.add(
                        Product(index.toString(),bestSellingProduct.productName,bestSellingProduct.price.toString(),bestSellingProduct.image,bestSellingProduct.totalOrders.toString())
                    )
                }
                viewModel.productList.value = productList
            }
        }

    }

    private fun setupYearSelector() {
        binding.cvYearSelector.setOnClickListener {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)

            val yearPicker = NumberPicker(requireContext()).apply {
                minValue = currentYear - 10 // Set min value to 10 years before the current year
                maxValue = currentYear // Set max value to the current year
                wrapSelectorWheel = false // Disable wrapping
                value = viewModel.selectedYear.value!!.toInt()
            }

            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.choose_year))
                .setView(yearPicker)
                .setPositiveButton(getString(R.string.done)) { _, _ ->
                    val selectedYear = yearPicker.value
                    // Handle the selected year
                    // e.g., update your UI or save the selected year
                    viewModel.selectedYear.value = selectedYear.toString()
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


    private fun setupBarChart(data:List<DataEntry>) {
        val parentLayout = binding.clBarChartLayout

// Check if a view with the same ID already exists
        val existingView = parentLayout.findViewById<AnyChartView>(R.id.anyChartView)

// If the view exists, remove it
        existingView?.let {
            parentLayout.removeView(it)
        }

// Assuming 'binding.root' is the parent ConstraintLayout where you want to add the view
        val anyChartView = AnyChartView(requireContext()) // 'this' refers to an Activity or Context
        anyChartView.id = R.id.anyChartView
// Set layout parameters for the AnyChartView
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT, // Width: match_parent
            0 // Height: 0dp (will be set with constraints)
        )
        layoutParams.topMargin = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._8sdp) // Margin top: _8sdp
        anyChartView.layoutParams = layoutParams

// Add the view to your parent ConstraintLayout (e.g., binding.root or your custom layout)
        binding.clBarChartLayout.addView(anyChartView)

// Now apply the constraints using ConstraintSet
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clBarChartLayout) // Clone existing constraints

// Set the constraints as per your XML
        constraintSet.connect(anyChartView.id, ConstraintSet.TOP, R.id.cvYearSelector, ConstraintSet.BOTTOM)
        constraintSet.connect(anyChartView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(anyChartView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(anyChartView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

// Apply the constraints
        constraintSet.applyTo(binding.clBarChartLayout)
        Timber.e("setupChart in $data")

        // Create a new Cartesian (Column) chart for vertical bars
        cartesian = AnyChart.column()
        cartesian.credits().enabled(false)
        cartesian.xScroller(true)

        // Define data for the chart
        column = cartesian.column(data)

        // Customize labels and styling
        cartesian.xAxis(0).title(getString(R.string.cities))
        cartesian.yAxis(0).title(getString(R.string.orders))

        // Format the y-axis to display values in thousands with 'k'
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

        // Format the bar labels to show the real value followed by 'k'
        column.labels()
            .enabled(true)
            .format("{%Value}{groupsSeparator: }")
            .position("center")
            .offsetX(0.0)
            .offsetY(5.0)
            .fontColor("#FFFFFF")

        // Set color palette for bars (optional)
        val colorHex = String.format("#%06X", 0xFFFFFF and requireContext().getColor(R.color.color_primary))
        column.color(colorHex) // Set the color using the hex string
        cartesian.draw(true)
        // Assign the chart to the view
        anyChartView.setChart(cartesian)


    }


}