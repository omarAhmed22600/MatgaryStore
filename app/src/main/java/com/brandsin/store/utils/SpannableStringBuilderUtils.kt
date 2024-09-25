package com.brandsin.store.utils

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import androidx.core.text.set

fun SpannableStringBuilder.appendWithSpan(charSequence: CharSequence, span: Any) {
	append(charSequence, span, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
}

operator fun Spannable.set(text: String, span: Any) {
	val index = indexOf(text)

	if (index == -1) return

	val end = index + text.length

	this[index..end] = span
}
