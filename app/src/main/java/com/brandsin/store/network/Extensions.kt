package com.brandsin.store.network

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.ContextWrapper
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import android.webkit.URLUtil
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.network.ExceptionUtil.getExceptionMessage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun Context.shortToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.shortToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, this.resources.getText(resId), duration).show()
}

fun Fragment.clearAllFragments() {
    //this will clear the back stack and displays no animation on the screen
    this.parentFragmentManager.popBackStackImmediate(
        null,
        FragmentManager.POP_BACK_STACK_INCLUSIVE
    )
}

fun Fragment.shortToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    requireActivity().shortToast(message, length)
}

fun Fragment.shortToast(@StringRes resId: Int, length: Int = Toast.LENGTH_SHORT) {
    requireActivity().shortToast(getString(resId), length)
}

fun Fragment.longToast(message: String?) {
    requireActivity().shortToast(message, Toast.LENGTH_LONG)
}

inline fun <reified T : ViewModel> ViewModelStoreOwner.initViewModel(
    body: T.() -> Unit
): T {
    val vm = ViewModelProvider(this)[T::class.java]
    vm.body()
    return vm
}

inline fun <reified T : AppCompatActivity> Fragment.castActivity(
    callback: (T?) -> Unit
): T? {
    return if (requireActivity() is T) {
        callback(requireActivity() as T)
        requireActivity() as T
    } else {
        Timber.e("class cast exception")
        callback(null)
        null
    }
}

inline fun <reified T : ViewModel> ViewModelStoreOwner.initViewModel(
    factory: ViewModelProvider.Factory,
    body: T.() -> Unit
): T {
    val vm = ViewModelProvider(this, factory)[T::class.java]
    vm.body()
    return vm
}

fun getRandomString(): String? {
    return try {
        val mPattern = "yyyy_MM_dd_HH_mm_ss.SSSSS"
        val date = Date();
        val formatter = SimpleDateFormat(mPattern, Locale.ROOT)
        val answer: String = formatter.format(date)
        answer
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun <T : Any> T.toRequestBodyParam(): RequestBody =
    this.toString().toRequestBody("text/plain".toMediaTypeOrNull())


fun <responseBody : Any?> BaseViewModel.requestCall(networkCall: suspend () -> responseBody?, callback: (responseBody?) -> Unit = {})
{
    viewModelScope.launch {
        setResult(ApiResponse.loading(null))
        try {
            val res = networkCall()
            callback(res)
        }
        catch (e: Exception) {
            postResult(ApiResponse.errorMessage(e.getExceptionMessage()))
        }
    }
}

inline fun <reified T : Any?, L : LiveData<T>> LifecycleOwner.observe(liveData: L, noinline body: (T) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun String.isValidUrl(): Boolean {
    return try {
        URLUtil.isValidUrl(this) && Patterns.WEB_URL.matcher(this).matches()
    } catch (e: Exception) {
        Timber.e(e)
        false
    }
}

fun AppCompatActivity.findFragmentById(id: Int): Fragment {
    supportFragmentManager.let {
        return it.findFragmentById(id)!!
    }
}

fun Context.getActivity(): AppCompatActivity? {
    return when (this) {
        is AppCompatActivity -> this
        is Activity -> this as AppCompatActivity
        is ContextWrapper -> this.baseContext.getActivity()
        is Fragment -> this.requireActivity() as AppCompatActivity
        else -> null
    }
}

fun Context.showKeyboard(view: View, show: Boolean) {
    with(view) {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (show)
            inputMethodManager.toggleSoftInput(SHOW_FORCED, HIDE_IMPLICIT_ONLY)
        else
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun Fragment.showKeyboard(show: Boolean) {
    view?.let { activity?.showKeyboard(it, show) }
}

fun Activity.showKeyboard(show: Boolean) {
    showKeyboard(currentFocus ?: View(this), show)
}

inline fun <reified T : Any> Any.castTo(): T? {
    return if (this is T) {
        this
    } else {
        Timber.e("class cast exception")
        null
    }
}




