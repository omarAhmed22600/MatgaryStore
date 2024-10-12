package com.brandsin.store.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
import com.brandsin.store.ui.main.addproduct.PhotoModel
import com.brandsin.store.ui.main.addproduct.ProductAttributesAdapter
import com.brandsin.store.ui.main.addproduct.ProductAttributesResponseItem
import com.brandsin.store.ui.main.addproduct.ProductPhotoAdapter
import com.brandsin.store.ui.menu.storeStatistics.Product
import com.brandsin.store.ui.menu.storeStatistics.ProductAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import kotlin.math.roundToInt

@BindingAdapter("imageView_setDrawableRes")
fun ImageView.setDrawableRes(@DrawableRes drawableRes: Int?) {
    setImageResource(drawableRes ?: 0)
}

@BindingAdapter("button_setForegroundRes")
fun View.setDrawableRes(@DrawableRes drawableRes: Int?) {
    val drawable: Drawable? = drawableRes?.let { ContextCompat.getDrawable(context, it) }
    foreground = drawable
}



@BindingAdapter("view_setVisibleOrInvisible")
fun View.setVisibleOrInvisible(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("view_setVisibleOrGone")
fun View.setVisibleOrGone(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.GONE
}


@BindingAdapter("textView_setTextColorRes")
fun TextView.setTextColorRes(@ColorRes colorRes: Int?) {
    val color: Int? = colorRes?.let { ContextCompat.getColor(context, it) }
    setTextColor(color!!)
}

@BindingAdapter("view_setBackgroundRes")
fun FrameLayout.setBackgroundRes(@DrawableRes drawableRes: Int?) {
    val drawable: Drawable? = drawableRes?.let { ContextCompat.getDrawable(context, it) }
    background = drawable
}

@BindingAdapter("textView_setStartDrawableRes")
fun TextView.setStartDrawableRes(@DrawableRes drawableRes: Int?) {
    val drawable: Drawable? = drawableRes?.let { ContextCompat.getDrawable(context, it) }
    setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)

}
@BindingAdapter(
    "editText_adjustHintFont",
    "editText_adjustHintFont_sizeInSp",
    requireAll = false,
)
fun EditText.adjustHintFontBA(fontStyleResName: String?, sizeInSp: Float?) {
    postWithReceiver {
        val context = context ?: return@postWithReceiver

        @SuppressLint("DiscouragedApi")
        val id = context.resources.getIdentifier(fontStyleResName, "font", context.packageName)

        if (id == 0 || hint.toStringOrEmpty().isEmpty()) return@postWithReceiver

        val typeface = ResourcesCompat.getFont(context, id)
        hint = buildSpannedString {
            inSpans(AbsoluteSizeSpan(context.spToPx(sizeInSp ?: 11f).roundToInt())) {
                appendWithSpan(hint.toStringOrEmpty(), CustomTypefaceSpan(typeface))
            }
        }
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions().error(R.drawable.app_logo))
            .into(imgView)
    }
}


@BindingAdapter("selLocalImg")
fun setLocalImg(imgView: ImageView, imgUri: String?) {
    imgUri?.let {
        Glide.with(imgView.context)
            .load(it)
            .apply(RequestOptions().error(R.drawable.app_logo))
            .into(imgView)
    }
}


@BindingAdapter("linearLayoutManagerForScrolling")
fun RecyclerView.bindLinearLayout(
    boolean: Boolean
) {
    if (boolean.not()) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, boolean).apply {
            isMeasurementCacheEnabled = false
        }
    }
}
@BindingAdapter("linearLayoutManagerForScrollingVertical")
fun RecyclerView.bindLinearLayoutVertical(
    boolean: Boolean
) {
    if (boolean.not()) {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, boolean).apply {
            isMeasurementCacheEnabled = false
        }
    }
}

@BindingAdapter("setSelection")
fun View.setSelection(
    isViewSelected: Boolean
) {
    isSelected = isViewSelected
}

@BindingAdapter("setSeparatorDivider")
fun RecyclerView.setSeparatorDivider(t: Boolean) {
    if (t) {
        addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }
}




@BindingAdapter("setMarginTopBasedOnCondition")
fun View.setMarginTopBasedOnCondition(
    condition: Boolean
) {
    val params = layoutParams as ConstraintLayout.LayoutParams
    if (condition) {
        params.topMargin = context.resources.getDimension(com.intuit.sdp.R.dimen._16sdp).toInt()
    } else {
        params.topMargin = context.resources.getDimension(com.intuit.sdp.R.dimen._40sdp).toInt()
    }
    layoutParams = params
}

@BindingAdapter("set_no_padding")
fun TextView.setNoPadding(
    condition: Boolean
) {
    if (condition) {
        val typeface = Typeface.createFromAsset(resources.assets, "fonts/cairo.ttf")
        setTypeface(typeface)
        includeFontPadding = false
    }
}
@BindingAdapter("set_marquee")
fun TextView.setMarquee(
    on:Boolean
) {
    if (on)
    {
        isSelected = true
        freezesText = true
        maxLines = 1
        isSingleLine = true
    }
}
@BindingAdapter("set_redOutline")
fun MaterialButton.setRedOutline(

    on:Boolean
) {
    if (on)
    {
// Set the stroke color to red
        strokeColor = ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_red_dark))
    }
}
/*
@BindingAdapter("set_titleColor")
fun MenuItem.setTitleColor(color: Int) {
    val spannableTitle = SpannableString(this.title).apply {
        setSpan(ForegroundColorSpan(color), 0, this.length, 0)
    }
    this.title = spannableTitle
}
*/
@BindingAdapter("cornerRadius")
fun ImageView.setCornerRadius(radius: Float) {
    val roundedDrawable = this.drawable?.let { originalDrawable ->
        val bitmap = (originalDrawable as BitmapDrawable).bitmap
        Bitmap.createBitmap(bitmap).let { srcBitmap ->
            val roundedBitmap = Bitmap.createBitmap(
                srcBitmap.width,
                srcBitmap.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(roundedBitmap)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val rectF = RectF(0f, 0f, srcBitmap.width.toFloat(), srcBitmap.height.toFloat())
            canvas.drawRoundRect(rectF, radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(srcBitmap, 0f, 0f, paint)
            BitmapDrawable(resources, roundedBitmap)
        }
    }
    this.setImageDrawable(roundedDrawable)
}
@BindingAdapter("navigationButton")
fun View.setAsNavigationButton(
    isNavigationButton: Boolean
) {
    if (isNavigationButton) {
        setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
@BindingAdapter("listDataForProducts")
fun bindRecyclerViewForProducts(
    recyclerView: RecyclerView,
    data: List<Product>?
) {
    val adapter = recyclerView.adapter as? ProductAdapter
        ?: ProductAdapter().also { recyclerView.adapter = it }
    adapter.submitList(data)
}
@BindingAdapter("listDataForProductPhotos")
fun bindRecyclerViewForProductPhotos(
    recyclerView: RecyclerView,
    data: List<PhotoModel>?
) {
    val adapter = recyclerView.adapter as ProductPhotoAdapter
    adapter.submitList(data)
}
@BindingAdapter("listDataForProductAttributes")
fun bindRecyclerViewForProductAttributes(
    recyclerView: RecyclerView,
    data: List<ProductAttributesResponseItem>?
) {
    val adapter = recyclerView.adapter as ProductAttributesAdapter
    adapter.submitList(data)
}
// Binding adapter function to set a Bitmap to an ImageView
@BindingAdapter("setImageBitmap")
fun ImageView.setImageBitmap(bitmap: Bitmap?) {
    // Set the Bitmap to the ImageView if it's not null
    bitmap?.let {
        this.setImageBitmap(it)
    }
}