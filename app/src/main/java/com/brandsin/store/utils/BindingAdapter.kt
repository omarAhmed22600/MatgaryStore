package com.brandsin.store.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.style.AbsoluteSizeSpan
import android.view.View
import android.widget.EditText
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brandsin.store.R
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
fun View.setBackgroundRes(@DrawableRes drawableRes: Int?) {
    val drawable: Drawable? = drawableRes?.let { ContextCompat.getDrawable(context, it) }
    background = drawable
}

@BindingAdapter("textView_setStartDrawableRes")
fun TextView.setStartDrawableRes(@DrawableRes drawableRes: Int?) {
    val drawable: Drawable? = drawableRes?.let { ContextCompat.getDrawable(context, it) }
    setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)

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





@BindingAdapter("linearLayoutManagerForScrolling")
fun RecyclerView.bindLinearLayout(
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
