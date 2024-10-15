package com.brandsin.store.ui.main.addproduct

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginEnd
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.distinctUntilChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.ItemProductAttributeBinding
import com.brandsin.store.utils.distinctUntilChangedNew
import com.brandsin.store.utils.gone
import com.brandsin.store.utils.visible
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber

class ProductAttributesAdapter(
    private val viewModel: AddProductViewModel,
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<ProductAttributesResponseItem, ProductAttributesAdapter.ProductAttributeVH>(
    DiffCallback
) {
    init {
        // Observe the LiveData from the ViewModel
        /*viewModel.attributes.observe(lifecycleOwner) {
            if (it.isEmpty())
            {
                viewModel.canChangePRice.value = true
            }
        }*/
    }
    class ProductAttributeVH(private var binding: ItemProductAttributeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // List to keep track of views added during the last iteration

            fun bind(
                data: ProductAttributesResponseItem,
                viewModel: AddProductViewModel,
                lifecycleOwner: LifecycleOwner,
        ) {
            binding.data = data
            binding.viewModel = viewModel

            // Set up click listener for showing multi-select dialog
            binding.capacity.setOnClickListener {
                showMultiSelectDialog(data) { selectedIds ->
                    // Update ViewModel with selected option IDs
                    viewModel.updateSelectedItemIds(data.id, selectedIds)
                    addDynamicEditTexts(selectedIds, data, viewModel,lifecycleOwner)

                    binding.addPrice.setOnClickListener {
                        // Dynamically add EditText views based on the number of selected options
//                        if (viewModel.canChangePRice.value == true) {



                        // Disable further price changes initially
                        viewModel.canChangePRice.value = false

                        // Observe changes in canChangePrice
                        viewModel.canChangePRice.distinctUntilChangedNew().observe(lifecycleOwner) { canChange ->
                            if (!canChange) {
                                Timber.e("Hiding extra edit texts container")
                                binding.extraEditTextsContainer.gone()
                                viewModel.resetOptionPrices(data.id)
                                binding.extraEditTextsContainer
                            }
                        }

                        binding.extraEditTextsContainer.visible()
                        // Set the value to true only after setting up the observer
                        viewModel.canChangePRice.value = true

//                            viewModel.canChangePRice.value = false
//                        } else {
//                            Toasty.error(binding.root.context,"يمكنك إضافة السعر لصفة واحدة فقط, إذا كنت تريد تغير السعر لصفة اخري قم بمسح البينات",Toasty.LENGTH_LONG).show()
//                        }

                            // Add the newly created views to the tracking list

//                        }
                    }
                }
            }
        }

        private fun addDynamicEditTexts(
            selectedIds: List<Int>,
            data: ProductAttributesResponseItem,
            viewModel: AddProductViewModel,
            lifecycleOwner: LifecycleOwner
        ) {
            // Clear any previous EditTexts
            binding.extraEditTextsContainer.removeAllViews()
//            binding.extraEditTextsContainer.visibility = View.VISIBLE

            selectedIds.forEach { selectedId ->
                // Find the option with the matching id
                val selectedOption = data.options.find { option -> option.id == selectedId }

                // Only proceed if an option with the matching id is found
                if (selectedOption != null) {
                    // Create a horizontal LinearLayout to hold the two TextInputLayouts with TextInputEditText
                    val horizontalLayout = LinearLayout(binding.root.context).apply {
                        orientation = LinearLayout.HORIZONTAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(
                                0,
                                resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._8sdp),
                                0,
                                0
                            ) // Set marginTop to match your style
                        }
                    }

                    // Create the first TextInputLayout and TextInputEditText for label
                    val textInputLayout1 = createLabelTextInputLayout(selectedOption.label)

                    // Create the second TextInputLayout and TextInputEditText for price
                    val textInputLayout2 = createPriceTextInputLayout(binding.root.context.getString(R.string.price))

                    // Add text change listener to update selectedPrice in Option
                    val priceEditText = textInputLayout2.editText as TextInputEditText

                    priceEditText.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            val newPrice = s?.toString()?.toDoubleOrNull()
                            Timber.e("new price is $newPrice")
                            if (newPrice != null) {
                                // Update the selectedPrice in ViewModel
                                viewModel.updateOptionPrice(data.id, selectedOption.id, newPrice)
                            } else {
                                // Handle invalid input, e.g., empty or invalid number
                                viewModel.updateOptionPrice(data.id, selectedOption.id, 0.0)
                            }
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    })

                    // Set initial price if available (use the selectedOption.selectedPrice as default)
//                    priceEditText.setText(selectedOption.selectedPrice.toString())

                    // Add the TextInputLayouts to the horizontal LinearLayout
                    horizontalLayout.addView(textInputLayout1)
                    horizontalLayout.addView(textInputLayout2)
                    viewModel.canChangePRice.distinctUntilChangedNew().observe(lifecycleOwner) { canChange ->
                        if (!canChange) {
                            priceEditText.text = null
                        }
                    }
                    // Add the horizontal LinearLayout to the container
                    binding.extraEditTextsContainer.addView(horizontalLayout)
                } else {
                    // Log a warning or handle the case where no option is found for the selectedId
                    Timber.e("Option with id $selectedId not found in data.options")
                }
            }
        }



        private fun createPriceTextInputLayout(hintText: String): TextInputLayout {
            // Create TextInputLayout programmatically
            val textInputLayout = TextInputLayout(binding.root.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0, // Use weight to equally distribute space
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f // Weight 1 to distribute space equally between the TextInputLayouts
                )
                setPadding(0, 0, 0, 0)
                isHintEnabled = true
                background = null // Match transparent background as in your XML
                boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
                val color = ContextCompat.getColor(context, R.color.hint_color)
                val colorStateList = ColorStateList.valueOf(color)

                defaultHintTextColor = colorStateList
            }

            // Create TextInputEditText
            val textInputEditText = TextInputEditText(binding.root.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                inputType = InputType.TYPE_CLASS_NUMBER
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                background = null // Match transparent background as in your XML
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setHintTextColor(ContextCompat.getColor(context, R.color.hint_color))
                isFocusable = true
                hint = hintText

                // Set custom font using Typeface
                typeface = ResourcesCompat.getFont(
                    context,
                    R.font.cairoregular
                ) // Replace with your font file
            }

            // Add the EditText to the TextInputLayout
            textInputLayout.addView(textInputEditText)

            // Create a View to act as the bottom border
            val bottomBorder = View(binding.root.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2 // Height of the bottom border line
                ).apply {
                    setMargins(0, 8, 0, 0) // Add some margin above if needed
                }
                background =
                    ColorDrawable(ContextCompat.getColor(context, R.color.black)) // Border color
            }

            // Add the bottom border View to the TextInputLayout
            textInputLayout.addView(bottomBorder)

            return textInputLayout
        }

        private fun createLabelTextInputLayout(hintText: String): TextInputLayout {
            // Create TextInputLayout programmatically
            val textInputLayout = TextInputLayout(binding.root.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0, // Use weight to equally distribute space
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f // Weight 1 to distribute space equally between the TextInputLayouts
                )
                setPadding(0, 0, 0, 0)
                isHintEnabled = true
                background = null // Match transparent background as in your XML
                boxBackgroundMode = TextInputLayout.BOX_BACKGROUND_NONE
            }

            // Create TextInputEditText
            val textInputEditText = TextInputEditText(binding.root.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                inputType = InputType.TYPE_CLASS_TEXT
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                background = null // Match transparent background as in your XML
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setHintTextColor(ContextCompat.getColor(context, R.color.hint_color))
                isFocusable = false
                text = Editable.Factory.getInstance().newEditable(
                    hintText
                )

                // Set custom font using Typeface
                typeface = ResourcesCompat.getFont(
                    context,
                    R.font.cairoregular
                ) // Replace with your font file
            }

            // Add the EditText to the TextInputLayout
            textInputLayout.addView(textInputEditText)

            // Create a View to act as the bottom border
            val bottomBorder = View(binding.root.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    2 // Height of the bottom border line
                ).apply {
                    setMargins(0, 8, 0, 0) // Add some margin above if needed
                }
                background =
                    ColorDrawable(ContextCompat.getColor(context, R.color.black)) // Border color
            }

            // Add the bottom border View to the TextInputLayout
            textInputLayout.addView(bottomBorder)

            // Set the end margin for the TextInputLayout based on RTL support
            val margin = binding.root.context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._8sdp)
            val layoutParams = textInputLayout.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginEnd = margin // Optionally set marginEnd if needed

            return textInputLayout
        }


        private fun showMultiSelectDialog(
            attribute: ProductAttributesResponseItem,
            onSelected: (List<Int>) -> Unit
        ) {
            // Initialize selected option IDs
            val selectedIds = attribute.selectedOptionIds?.toMutableList() ?: mutableListOf()
            val options = attribute.options

            // Create an array of labels for the dialog options
            val optionLabels = options.map { it.label }.toTypedArray()

            // Boolean array to track selected items based on selectedOptionIds
            val checkedItems = BooleanArray(options.size) { index ->
                // Mark items as checked if their id is in the selectedIds list
                options[index].id in selectedIds
            }

            // AlertDialog for multi-select
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setTitle(attribute.label)
            builder.setMultiChoiceItems(optionLabels, checkedItems) { _, which, isChecked ->
                val selectedOptionId = options[which].id  // Get the actual Option.id

                if (isChecked) {
                    // Add the actual Option.id to selectedIds
                    if (!selectedIds.contains(selectedOptionId)) {
                        selectedIds.add(selectedOptionId)
                    }
                } else {
                    // Remove the actual Option.id from selectedIds
                    selectedIds.remove(selectedOptionId)
                }
            }

            // OK button to confirm selections
            builder.setPositiveButton(binding.root.context.getString(R.string.confirm)) { _, _ ->
                // Update the EditText with the selected option labels
                binding.capacity.text = Editable.Factory.getInstance().newEditable(
                    selectedIds.map { id ->
                        options.firstOrNull { it.id == id }?.label
                    }.joinToString(", ")
                )

                // Pass the selected option IDs to the callback
                onSelected(selectedIds)
            }

            // Cancel button
            builder.setNegativeButton(binding.root.context.getString(R.string.cancel), null)

            builder.create().show()
        }
    }

        companion object DiffCallback : DiffUtil.ItemCallback<ProductAttributesResponseItem>() {
        override fun areItemsTheSame(
            oldItem: ProductAttributesResponseItem,
            newItem: ProductAttributesResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductAttributesResponseItem,
            newItem: ProductAttributesResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAttributeVH {
        val binding =
            ItemProductAttributeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductAttributeVH(binding)
    }

    override fun onBindViewHolder(holder: ProductAttributeVH, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel,lifecycleOwner)
    }
}
