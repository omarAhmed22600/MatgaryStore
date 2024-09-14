package com.brandsin.store.ui.main.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.MessageResponse
import com.brandsin.store.network.ResponseHandler
import com.brandsin.store.network.RetrofitBuilder
import com.brandsin.store.network.toSingleEvent
import com.brandsin.store.ui.main.categories.model.CategoriesListResponse
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.launch

class CategoriesViewModel : BaseViewModel() {

    val apiInterface = RetrofitBuilder.API_SERVICE

    private val _getAllCategoriesResponse: MutableLiveData<ResponseHandler<CategoriesListResponse?>> =
        MutableLiveData()
    val getAllCategoriesResponse: LiveData<ResponseHandler<CategoriesListResponse?>> =
        _getAllCategoriesResponse.toSingleEvent()

    private val _addAndEditCategoryResponse: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val addAndEditCategoryResponse: LiveData<ResponseHandler<MessageResponse?>> =
        _addAndEditCategoryResponse.toSingleEvent()

    fun addCategory(nameAr: String, nameEn: String) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.addCategory(
                    PrefMethods.getStoreData()?.id ?: 0,
                    nameAr,
                    nameEn
                )
            }.collect {
                _addAndEditCategoryResponse.value = it
            }
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.getAllCategories(
                    PrefMethods.getStoreData()?.id ?: 0,
                )
            }.collect {
                _getAllCategoriesResponse.value = it
            }
        }
    }

    fun deleteCategoryByCategoryId(categoryId: Int) {
        obsIsLoading.set(true)
        obsIsEmpty.set(false)
        obsIsFull.set(false)
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.deleteCategoryByCategoryId(categoryId)
            }.collect {
                getAllCategories()
            }
        }
    }

    fun editCategory(categoryId: Int, nameAr: String, nameEn: String) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiInterface.editCategory(
                    categoryId,
                    nameAr,
                    nameEn
                )
            }.collect {
                _addAndEditCategoryResponse.value = it
            }
        }
    }
}