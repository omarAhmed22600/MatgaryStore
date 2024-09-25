package com.brandsin.store.network

import android.util.Log
import androidx.lifecycle.ViewModel
import com.brandsin.store.model.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.Response

abstract class BaseApiResponse : ViewModel() {

    private var errorMessage = ""

    // safe Api Call function (Unit)
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<ResponseHandler<T?>> =
        flow {
            emit(ResponseHandler.Loading)
            try {
                val response = apiCall()
                val code = response.code()
                if (response.isSuccessful) {
                    emit(ResponseHandler.StopLoading)
                    emit(ResponseHandler.Success(response.body()))
                } else {
                    when {
                        code / 100 == 4 -> parseError(response.errorBody()!!.string())
                        code / 100 == 5 -> {
                            errorMessage = "something went wrong"
                        }

                        else -> {
                            errorMessage = "something went wrong"
                        }
                    }
                    emit(ResponseHandler.StopLoading)
                    emit(ResponseHandler.Error(errorMessage))
                }
            } catch (e: Exception) {
                Log.d("MyDebugData", "BaseApiResponse : safeApiCall :  " + e.localizedMessage)
                emit(ResponseHandler.StopLoading)
                emit(ResponseHandler.Error("something went wrong"))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun <T> safeApiCallOmar(apiCall: suspend () ->  BaseResponse<T>): Flow<ResponseHandler<T?>> =
        flow {
            emit(ResponseHandler.Loading)
            try {
                val response = apiCall()

                if (response.success) {
                    emit(ResponseHandler.StopLoading)
                    emit(ResponseHandler.Success(response.data))
                } else {
                    errorMessage = "something went wrong"
                    emit(ResponseHandler.StopLoading)
                    emit(ResponseHandler.Error(errorMessage))
                }
            } catch (e: Exception) {
                Log.d("MyDebugData", "BaseApiResponse : safeApiCall :  " + e.localizedMessage)
                emit(ResponseHandler.StopLoading)
                emit(ResponseHandler.Error("something went wrong"))
            }
        }.flowOn(Dispatchers.IO)
    private fun parseError(error: String) {
        val jsonObject = JSONObject(error)
        errorMessage = jsonObject.getString("message")
    }
}