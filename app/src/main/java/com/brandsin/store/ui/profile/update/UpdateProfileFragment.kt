package com.brandsin.store.ui.profile.update

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.DatePicker
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.brandsin.store.R
import com.brandsin.store.databinding.ProfileFragmentUpdateProfileBinding
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.ui.activity.BaseHomeFragment
import com.brandsin.store.ui.activity.home.HomeActivity
import com.brandsin.store.utils.ImagePicker
import com.brandsin.store.utils.PrefMethods
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class UpdateProfileFragment : BaseHomeFragment(), Observer<Any?> {

    private lateinit var binding: ProfileFragmentUpdateProfileBinding

    lateinit var viewModel: UpdateProfileViewModel

    private var img = ""
    private var bitmap: Bitmap? = null

    private var birthDate: String? = null

    private val MY_PERMISSIONS_REQUEST_CAMERA = 200
    private val PICK_IMAGE_ID = 234
    private val STORAGE_PERMISSION_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.profile_fragment_update_profile,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[UpdateProfileViewModel::class.java]
        binding.viewModel = viewModel

        setBarName(getString(R.string.information_of_the_responsible_person))

        viewModel.mutableLiveData.observe(viewLifecycleOwner, this)

        viewModel.showProgress().observe(viewLifecycleOwner) { aBoolean ->
            if (!aBoolean!!) {
                binding.progressLayout.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                binding.progressLayout.visibility = View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                );
            }
        }

        if (PrefMethods.getUserData()?.picture != null &&
            PrefMethods.getUserData()?.picture.toString().isNotEmpty()
        ) {
            Picasso.get()
                .load(PrefMethods.getUserData()!!.picture.toString())
                .error(R.drawable.profile)
                .into(binding.ivUserImg)
            img = convertUrlToBase64(PrefMethods.getUserData()?.picture)
        } else {
            binding.ivUserImg.setImageResource(R.drawable.profile)
        }

        binding.edtBirthDate.setText(PrefMethods.getUserData()?.birthDate.toString().trim())

        if (PrefMethods.getUserData()?.gender == "male") {
            binding.radioGroupMale.isChecked = true
            binding.radioGroupFemale.isChecked = false
        } else if (PrefMethods.getUserData()?.gender == "female") {
            binding.radioGroupMale.isChecked = false
            binding.radioGroupFemale.isChecked = true
        }

        setBtnListener()
    }

    private fun setBtnListener() {
        binding.imgLayout.setOnClickListener {
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
        }

        binding.birthDateLayout.setOnClickListener {
            showBirthDatePickerDialog()
        }

        binding.edtBirthDate.setOnClickListener {
            showBirthDatePickerDialog()
        }

        binding.radioGroupMale.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.radioGroupMale.isChecked = isChecked
            binding.radioGroupFemale.isChecked = !isChecked

            viewModel.request.gender = "male"
        }

        binding.radioGroupFemale.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.radioGroupMale.isChecked = !isChecked
            binding.radioGroupFemale.isChecked = isChecked

            viewModel.request.gender = "female"
        }
    }

    private fun showBirthDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create a date picker dialog
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                // Handle the selected date
                birthDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.edtBirthDate.setText(birthDate.toString().trim())
                viewModel.request.birthDate = birthDate.toString().trim()
                binding.edtBirthDate.error = null
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

    private fun formatDate(data: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en"))

        // Parse the input date string
        // val parsedDate = inputFormat.parse(data)

        // Format the parsed date to the desired output format
        return outputFormat.format(inputFormat.parse(data)?.toString() ?: "")
    }

    override fun onChanged(value: Any?) {
        if (value == null) return
        when (value) {
            Codes.NAME_EMPTY -> {
                showToast(getString(R.string.enter_full_name), 1)
            }

            Codes.EMPTY_PHONE -> {
                showToast(getString(R.string.empty_phone), 1)
            }

            Codes.INVALID_PHONE -> {
                showToast(getString(R.string.invalid_phone), 1)
            }

            Codes.EMAIL_EMPTY -> {
                showToast(getString(R.string.enter_mail), 1)
            }

            Codes.EMAIL_INVALID -> {
                showToast(getString(R.string.email_must_correct), 1)
            }

            Codes.EDIT_PROFILE -> {
                showToast(getString(R.string.successfully_updated), 2)

                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                requireActivity().finishAffinity()
                viewModel.setShowProgress(false)
            }

            else -> {
                if (value is String) {
                    showToast(value.toString(), 1)
                }
                viewModel.setShowProgress(false)
            }
        }
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //do your stuff
                val chooseImageIntent = ImagePicker.getPickImageIntent(requireActivity())
                startActivityForResult(chooseImageIntent, PICK_IMAGE_ID)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            when (requestCode) {
                PICK_IMAGE_ID, MY_PERMISSIONS_REQUEST_CAMERA ->
                    if (resultCode != 0) {
                        bitmap = ImagePicker.getImageFromResult(requireActivity(), resultCode, data)
                        binding.ivUserImg.setImageBitmap(bitmap)
                        img = convertBitmapToString(bitmap!!)
                        viewModel.request.picture = "data:image/png;base64,$img"
                        viewModel.request.picture_type = ""
                    }

                else ->
                    super.onActivityResult(requestCode, resultCode, data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun convertBitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encoded = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return encoded.replace(" ", "").replace("\n", "")
    }

    private fun convertUrlToBase64(url: String?): String {
        val newUrl: URL
        val bitmap: Bitmap
        var base64 = ""
        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            newUrl = URL(url)
            bitmap = BitmapFactory.decodeStream(newUrl.openConnection().getInputStream())
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            base64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return base64.replace(" ", "").replace("\n", "")
    }
}