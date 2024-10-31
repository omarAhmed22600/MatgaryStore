package com.brandsin.store.ui.main.products

import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.main.products.delete.DeleteProductRequest
import com.brandsin.store.model.main.products.delete.DeleteProductResponse
import com.brandsin.store.model.main.products.list.*
import com.brandsin.store.network.ApiResponse
import com.brandsin.store.network.requestCall
import com.brandsin.store.ui.main.products.category.ProductCategoriesAdapter
import com.brandsin.store.ui.main.products.products.ProductsAdapter
import com.brandsin.store.utils.PrefMethods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class MyProductsViewModel : BaseViewModel()
{

    var categoryAdapter = ProductCategoriesAdapter()
    var productsAdapter = ProductsAdapter()
    var productsList  = ArrayList<ProductsItem>()
    var categoriesList = ArrayList<ProductCategoriesData>()
    var deleteProductRequest = DeleteProductRequest()
    var categoryId = 0

    init {
        getUserStatus()
    }

    fun getUserStatus() {
        getStoreProducts()
        obsIsLogin.set(true)
    }

    fun getStoreProducts() {
        obsIsFull.set(false)
        obsIsEmpty.set(false)
        obsIsLoading.set(true)
        requestCall<ListProductsResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().getStoreProducts(
                    PrefMethods.getStoreData()!!.id!!.toInt(),
                        PrefMethods.getLanguage(),
                        0,
                        500
                )
            }
        })
        { res ->
            obsIsLoading.set(false)
            when (res!!.success) {
                true -> {
                    res.let {
                        if(it.data!!.products!!.isNotEmpty()) {
                            obsIsFull.set(true)
                            obsIsLoadingStores.set(false)
                            obsHideRecycler.set(true)
                            categoriesList = res.data!!.productsCategories as ArrayList<ProductCategoriesData>
                            productsList = res.data.products as ArrayList<ProductsItem>

                            categoryAdapter.updateList(categoriesList)
                            productsAdapter.updateList(productsList)
                            if ( categoryId == 0) {
                                 filterProducts( categoriesList[0].id)
                            } else {
                                 filterProducts( categoryId)
                            }

                            when (productsList.size) {
                                0 -> {
                                    obsHideRecycler.set(false)
                                    obsIsProductsEmpty.set(true)
                                }
                                else -> {
                                    obsHideRecycler.set(true)
                                    obsIsProductsEmpty.set(false)
                                }
                            }
                        } else {
                            obsIsEmpty.set(true)
                            obsIsFull.set(false)
                        }
                    }
                }

                else -> {}
            }
        }
    }

    fun filterProducts(catId : Int) {

        val filteredList: ArrayList<ProductsItem> = ArrayList()
        productsList.forEach { productsItem ->
            when {
                productsItem.categories!!.isEmpty() -> {
                    filteredList.add(productsItem)
                }
                else -> {
                    productsItem.categories.forEach { categoriesItem ->
                        when (categoriesItem!!.id) {
                            catId -> {
                                filteredList.add(productsItem)
                            }
                        }
                    }
                }
            }
        }

        when (filteredList.size) {
            0 -> {
                obsHideRecycler.set(false)
                obsIsProductsEmpty.set(true)
            }
            else -> {
                obsHideRecycler.set(true)
                obsIsProductsEmpty.set(false)
                productsAdapter.updateList(filteredList)
            }
        }
    }
    fun deleteProduct() {
        obsIsVisible.set(true)
        requestCall<DeleteProductResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().deleteProduct(deleteProductRequest)
            }
        })
        { res ->

            obsIsVisible.set(false)
            when (res!!.success) {
                true -> {
                    apiResponseLiveData.value = ApiResponse.success(res)
                }
                else -> {
                    apiResponseLiveData.value = ApiResponse.successMessage(res.message.toString())
                }
            }
        }
    }
}