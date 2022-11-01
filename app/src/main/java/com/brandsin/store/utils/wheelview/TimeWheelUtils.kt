package com.brandsin.store.utils.wheelview

import android.content.res.Resources
import cn.wongzhenyu.recyclerwheelview.BuildConfig

private const val TAG = "RecyclerWheelView"

internal fun dp2px(dp: Float): Float {
    val density = Resources.getSystem().displayMetrics.density
    return dp * density
}

internal fun sp2px(sp: Float): Float {
    val scaleDensity = Resources.getSystem().displayMetrics.scaledDensity
    return sp * scaleDensity
}
internal fun logDebug(message: String) {
    if (BuildConfig.DEBUG) {
      //  Log.d(cn.wongzhenyu.recyclerwheelview.util.TAG, message)
    }
}

internal fun logInfo(message: String) {
    if (BuildConfig.DEBUG) {
     //   Log.i(cn.wongzhenyu.recyclerwheelview.util.TAG, message)
    }
}

internal fun logWarn(message: String) {
    if (BuildConfig.DEBUG) {
       // Log.w(cn.wongzhenyu.recyclerwheelview.util.TAG, message)
    }
}

internal fun logError(message: String) {
    if (BuildConfig.DEBUG) {
      //  Log.e(cn.wongzhenyu.recyclerwheelview.util.TAG, message)
    }
}