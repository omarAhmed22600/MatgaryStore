package com.brandsin.store.utils

private const val numsOnly = "0123456789"

fun String?.extractNumsOnly(): String {
	return buildString {
		for (char in this@extractNumsOnly.orEmpty()) {
			if (char in numsOnly) {
				append(char)
			}
		}
	}
}

fun String?.trimAllWhitespaces(): String {
	return this?.filterNot { it.isWhitespace() }.orEmpty()
}

fun String?.trimFirstPlusIfExistsOrEmpty(): String {
	return orEmpty().let { if (it[0] == '+' && it.length > 1) it.substring(1) else it }
}

fun Any?.toStringOrEmpty() = this?.toString() ?: ""

fun String?.orElseIfNullOrEmpty(fallback: String): String {
	return if (isNullOrEmpty()) fallback else this
}

fun String?.toNullStringIfNullOrEmpty() = if (isNullOrEmpty()) "null" else this

fun <T> String?.letIfNotNullNorEmptyOrNull(transformation: (String) -> T?): T? {
	return if (isNullOrEmpty()) null else transformation(this)
}

fun <T> String?.applyIfNotNullNorEmpty(transformation: String.() -> T?) {
	if (!isNullOrEmpty()) {
		this.transformation()
	}
}

fun String.minLengthZerosPrefix(requiredLength: Int): String {
	return if (length < requiredLength) {
		"${"0".repeat(requiredLength - length)}$this"
	}else {
		this
	}
}

fun Number.minLengthZerosPrefix(requiredLength: Int): String = toString().minLengthZerosPrefix(requiredLength)
