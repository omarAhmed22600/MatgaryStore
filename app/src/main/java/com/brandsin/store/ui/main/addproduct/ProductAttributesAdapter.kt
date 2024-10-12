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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.databinding.ItemProductAttributeBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProductAttributesAdapter(
    private val viewModel: AddProductViewModel
) : ListAdapter<ProductAttributesResponseItem, ProductAttributesAdapter.ProductAttributeVH>(
    DiffCallback
) {

    class ProductAttributeVH(private var binding: ItemProductAttributeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ProductAttributesResponseItem, viewModel: AddProductViewModel) {
            binding.data = data

            // Check if the attribute label is "الحجم"
            if (data.label == "الحجم") {
                // Set up click listener for showing multi-select dialog
                binding.capacity.setOnClickListener {
                    showMultiSelectDialog(data) { selectedIds ->
                        // Update ViewModel with selected option IDs
                        viewModel.updateSelectedItemIds(data.id, selectedIds)

                        // Dynamically add EditText views based on the number of selected options
                        addDynamicEditTexts(selectedIds, data,viewModel)
                    }
                }
            } else {
                // Normal behavior for other attributes
                binding.capacity.setOnClickListener {
                    showMultiSelectDialog(data) { selectedIds ->
                        // Update ViewModel with selected option IDs
                        viewModel.updateSelectedItemIds(data.id, selectedIds)
                    }
                }
            }
        }

        private fun addDynamicEditTexts(
            selectedIds: List<Int>,
            data: ProductAttributesResponseItem,
            viewModel: AddProductViewModel
        ) {
            // Clear any previous EditTexts
            binding.extraEditTextsContainer.removeAllViews()
            binding.extraEditTextsContainer.visibility = View.VISIBLE

            selectedIds.forEach { selectedIndex ->
                val selectedOption = data.options[selectedIndex]

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
                        val newPrice = s.toString().toDoubleOrNull() ?: 0.0
                        // Update the selectedPrice in ViewModel
                        viewModel.updateOptionPrice(data.id, selectedOption.id, newPrice)
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })

                // Add the TextInputLayouts to the horizontal LinearLayout
                horizontalLayout.addView(textInputLayout1)
                horizontalLayout.addView(textInputLayout2)

                // Add the horizontal LinearLayout to the container
                binding.extraEditTextsContainer.addView(horizontalLayout)
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

            return textInputLayout
        }

        private fun showMultiSelectDialog(
            attribute: ProductAttributesResponseItem,
            onSelected: (List<Int>) -> Unit
        ) {
            // Initialize selected option IDs
            val selectedIds = attribute.selectedOptionIds?.toMutableList() ?: mutableListOf()
            val options = attribute.options.map { it.label }.toTypedArray()

            // Boolean array to track selected items
            val checkedItems = BooleanArray(options.size)

            // AlertDialog for multi-select
            val builder = AlertDialog.Builder(binding.root.context)
            builder.setTitle(attribute.label)
            builder.setMultiChoiceItems(options, checkedItems) { _, which, isChecked ->
                if (isChecked) {
                    selectedIds.add(which)
                } else {
                    selectedIds.remove(which)
                }
            }

            // OK button to confirm selections
            builder.setPositiveButton(binding.root.context.getString(R.string.confirm)) { _, _ ->
                // Update the EditText with selected items
                binding.capacity.text = Editable.Factory.getInstance().newEditable(
                    selectedIds.map { options[it] }.joinToString(", ")
                )
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
        holder.bind(item, viewModel)
    }
}
