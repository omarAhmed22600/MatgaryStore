package com.brandsin.store.network

import com.brandsin.store.BuildConfig
import com.brandsin.store.database.ApiInterface
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/*
 * Created by MouazSalah on 28/12/2020.
 */

//private const val BASE_URL = "https://hagaty-app.com/"
private const val BASE_URL = "https://brandsin.net"
/*
*add this if you want a static header in all requests
**/
fun getHeaderInterceptor(): Interceptor {
    return object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request =
                chain.request().newBuilder()
                    .build()
            return chain.proceed(request)
        }
    }
}

private fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
            .apply {
                if(BuildConfig.DEBUG){
                    this.addInterceptor(HttpLoggingInterceptor()
                            .setLevel(Level.BODY))
                }
            }
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()
}

private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        // .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .client(createOkHttpClient())
        .build()

object RetrofitBuilder {
    val API_SERVICE: ApiInterface by lazy {
        retrofitBuilder.create(ApiInterface::class.java)
    }
}




