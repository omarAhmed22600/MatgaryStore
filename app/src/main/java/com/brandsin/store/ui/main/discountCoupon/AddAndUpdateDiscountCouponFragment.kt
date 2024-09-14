package com.brandsin.store.ui.main.discountCoupon

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.brandsin.store.R
import com.brandsin.store.databinding.FragmentAddAndUpdateDiscountCouponBinding
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.main.discountCoupon.viewmodel.DiscountCouponViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddAndUpdateDiscountCouponFragment : BaseHomeFragment() {

    private var _binding: FragmentAddAndUpdateDiscountCouponBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiscountCouponViewModel by activityViewModels()

    private var couponCode: String? = null
    private var couponValue: String? = null
    private var startDate: String? = null
    private var endDate: String? = null
    private var couponType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddAndUpdateDiscountCouponBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewModel.getAllCoupons()

        setBtnListener()
        subscribeData()
    }

    private fun initViews() {
        if (viewModel.typeClicked == "AddNewCoupon") {
            setBarName(getString(R.string.create_a_new_coupon))

            binding.btnCreateAndUpdateCoupon.text = getString(R.string.create_coupon)

            couponType = "fixed"

            binding.radioFixed.isChecked = true
            binding.radioPercentage.isChecked = false

        } else if (viewModel.typeClicked == "EditCoupon") {
            setBarName(getString(R.string.edit_coupon))

            binding.btnCreateAndUpdateCoupon.text = getString(R.string.edit_coupon)

            binding.edtCouponCode.setText(viewModel.couponItem.code.toString())
            binding.edtCouponValue.setText(viewModel.couponItem.value.toString().trim())
            binding.edtStartDate.setText(formatDate(viewModel.couponItem.start.toString()))
            binding.edtEndDate.setText(formatDate(viewModel.couponItem.expiry.toString()))

            // couponType = viewModel.couponItem.type.toString()

            if (viewModel.couponItem.type.toString() == "fixed") { // fixed
                binding.radioFixed.isChecked = true
                binding.radioPercentage.isChecked = false
                couponType = "fixed"
            } else { // percentage
                binding.radioFixed.isChecked = false
                binding.radioPercentage.isChecked = true
                couponType = "percentage"
            }
        }
    }

    private fun setBtnListener() {
        binding.edtStartDate.setOnClickListener {
            showStartDatePickerDialog()
        }

        binding.edtEndDate.setOnClickListener {
            showEndDatePickerDialog()
        }

        binding.radioGroupLayout.setOnCheckedChangeListener { _, checkedId -> // buttonView, isChecked
            // Handle the checked RadioButton
            val radioButton: RadioButton = binding.root.findViewById(checkedId)
            // val selectedOption = radioButton.text
            couponType = if (radioButton.text == getString(R.string.fixed)) {
                "fixed"
            } else {
                "percentage"
            }
        }

        binding.btnCreateAndUpdateCoupon.setOnClickListener {
            if (validate()) {
                when (viewModel.typeClicked) {
                    "AddNewCoupon" -> {
                        viewModel.createNewCoupon(
                            code = couponCode!!, type = couponType!!,
                            value = couponValue!!,
                            start = startDate!!, expiry = endDate!!
                        )
                    }

                    "EditCoupon" -> {
                        viewModel.updateCouponByCouponId(
                            couponId = viewModel.couponItem.id!!.toInt(),
                            code = couponCode ?: "",
                            type = couponType ?: "",
                            value = couponValue ?: "",
                            start = startDate!!, expiry = endDate!!,
                        )
                    }
                }
            }
        }
    }

    private fun showStartDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a date picker dialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                // Handle the selected date
                startDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.edtStartDate.setText(startDate)
                binding.edtStartDate.error = null
            },
            year,
            month,
            day
        )

        // Set a maximum date if needed
        // datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        // Show the date picker dialog
        datePickerDialog.show()
    }

    private fun showEndDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a date picker dialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                // Handle the selected date
                endDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.edtEndDate.setText(endDate)
                binding.edtEndDate.error = null
            },
            year,
            month,
            day
        )

        // Set a maximum date if needed
        // datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        // Show the date picker dialog
        datePickerDialog.show()
    }

    private fun subscribeData() {
        viewModel.createAndUpdateResponse.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseHandler.Success -> {
                    // show success message
                    if (it.data?.success == true) {
                        if (viewModel.typeClicked == "AddNewCoupon") {
                            showToast(getString(R.string.coupon_added_successfully), 2)
                        } else if (viewModel.typeClicked == "EditCoupon") {
                            showToast(getString(R.string.coupon_updated_successfully), 2)
                        }
                        findNavController().navigateUp()
                    } else { // it.data?.success == false
                        // show error message
                        showToast(it.data?.message.toString(), 1)
                    }
                }

                is ResponseHandler.Error -> {
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)

                    // show error message
                    showToast(it.message, 1)
                }

                is ResponseHandler.Loading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(true)
                    viewModel.obsIsEmpty.set(false)
                    viewModel.obsIsFull.set(false)
                }

                is ResponseHandler.StopLoading -> {
                    // show a progress bar
                    viewModel.obsIsLoading.set(false)
                    viewModel.obsIsEmpty.set(true)
                    viewModel.obsIsFull.set(false)
                }

                else -> {}
            }
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        fun showErrorAndToast(editText: EditText, errorMessage: String) {
            editText.error = errorMessage
            showToast(errorMessage, 1)
            isValid = false
        }

        couponCode = binding.edtCouponCode.text.toString().trim()
        couponValue = binding.edtCouponValue.text.toString().trim()
        startDate = binding.edtStartDate.text.toString().trim()
        endDate = binding.edtEndDate.text.toString().trim()

        when {
            couponCode?.isEmpty() == true -> showErrorAndToast(
                binding.edtCouponCode,
                getString(R.string.enter_the_coupon_code)
            )

            couponValue?.isEmpty() == true && couponValue != null -> showErrorAndToast(
                binding.edtCouponValue,
                getString(R.string.enter_the_coupon_value)
            )

            couponValue != null && couponValue!!.toInt() > 100 -> showErrorAndToast(
                binding.edtCouponValue,
                getString(R.string.the_coupon_value_is_a_maximum_of_100)
            )

            startDate?.isEmpty() == true -> showErrorAndToast(
                binding.edtStartDate,
                getString(R.string.enter_the_start_date)
            )

            endDate?.isEmpty() == true -> showErrorAndToast(
                binding.edtEndDate,
                getString(R.string.enter_the_end_date)
            )
        }

        return isValid
    }

    private fun formatDate(data: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en"))

        // Parse the input date string
        // val parsedDate = inputFormat.parse(data)

        // Format the parsed date to the desired output format
        return outputFormat.format(inputFormat.parse(data) as Date)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}