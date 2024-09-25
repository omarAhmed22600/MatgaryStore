package com.brandsin.store.utils

import android.view.View

fun <V : View> V.postWithReceiver(action: V.() -> Unit) {
	post {
		action()
	}
}
