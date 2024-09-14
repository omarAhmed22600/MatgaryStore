package com.brandsin.store.database

import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brandsin.store.network.ApiRepo
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.BaseApiResponse
import com.brandsin.store.network.RetrofitBuilder
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class BaseViewModel : BaseApiResponse(), Observable {
    fun getApiRepo(): ApiRepo = ApiRepo(RetrofitBuilder.API_SERVICE)

    val obsSize = ObservableField<Int>()
    var obsIsVisible = ObservableBoolean()

    var obsIsLoading = ObservableBoolean()
    var obsIsLogin = ObservableBoolean()
    var obsIsEmpty = ObservableBoolean()
    var obsIsFull = ObservableBoolean()

    var obsIsLoadingStores = ObservableField<Boolean>()
    var obsHideRecycler = ObservableField<Boolean>()
    var obsIsProductsEmpty = ObservableField<Boolean>()

    // For network
    val apiResponseLiveData = MutableLiveData<ApiResponse<Any?>>()

    val clickableLiveData = SingleLiveEvent<Boolean>().apply { postValue(true) }
    var obsIsClickable = ObservableBoolean(true)

    val mutableLiveData = SingleLiveEvent<Any?>()

    var showProgress: SingleLiveEvent<Boolean>?
    private val message = ObservableField<String>()
    private val mCallBacks: PropertyChangeRegistry = PropertyChangeRegistry()
    var isShownOld = ObservableBoolean()
    var isShown = ObservableBoolean()
    var isConfirmShown = ObservableBoolean()

    fun setValue(item: Any?) {
        mutableLiveData.value = item
        mutableLiveData.value = null
    }

    fun postValue(item: Any?) {
        mutableLiveData.postValue(item)
        mutableLiveData.postValue(null)
    }

    fun onEyeOldClicked() {
        if (isShownOld.get()) {
            isShownOld.set(false)
        } else {
            isShownOld.set(true)
        }
    }

    fun onEyeClicked() {
        when {
            isShown.get() -> {
                isShown.set(false)
            }

            else -> {
                isShown.set(true)
            }
        }
    }

    fun onConfirmEyeClicked() {
        when {
            isConfirmShown.get() -> {
                isConfirmShown.set(false)
            }

            else -> {
                isConfirmShown.set(true)
            }
        }
    }

    protected open fun getDouble(value: String?): Double {
        return value?.toDouble() ?: 0.0
    }

    fun setShowProgress(item: Boolean) {
        showProgress!!.value = item
    }

    fun showProgress(): SingleLiveEvent<Boolean> {
        return if (showProgress == null) SingleLiveEvent<Boolean>().also {
            showProgress = it
        } else showProgress!!
    }

    fun getMessage(): String? {
        return message.get()
    }

    fun setMessage(message: Any) {
        this.message.set(message.toString())
    }

    fun setMessageFromRes(stringRes: Int) {
        message.set(getString(stringRes))
    }

    fun getString(stringRes: Int): String {
        return MyApp.getInstance().resources.getString(stringRes)
    }

    fun getStringArray(resArray: Int): Array<String> {
        return MyApp.getInstance().resources.getStringArray(resArray)
    }

    fun accessLoadingBar(visible: Int) {
        setValue(visible)
    }

    override fun addOnPropertyChangedCallback(callback: OnPropertyChangedCallback) {
        mCallBacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: OnPropertyChangedCallback) {
        mCallBacks.remove(callback)
    }

    fun notifyChange() {
        mCallBacks.notifyChange(this, 0)
    }

    fun notifyChange(propertyId: Int) {
        mCallBacks.notifyChange(this, propertyId)
    }

    fun setClickable() {
        obsIsClickable.set(false)
        viewModelScope.launch {
            delay(2000)
            obsIsClickable.set(true)
        }
    }

    init {
        showProgress = SingleLiveEvent()
    }

    fun setResult(o: ApiResponse<Any?>) {
        apiResponseLiveData.value = o
    }

    fun postResult(o: ApiResponse<Any?>) {
        apiResponseLiveData.postValue(o)
    }
}