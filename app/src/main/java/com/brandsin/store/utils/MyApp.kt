package com.brandsin.store.utils

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.danikula.videocache.HttpProxyCacheServer
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class MyApp : Application() {

    private var proxy: HttpProxyCacheServer? = null

    companion object {
        var simpleCache: SimpleCache? = null

        private lateinit var mInstance: MyApp

        lateinit var context: Context
        fun getInstance(): MyApp {
            return mInstance
        }
    }

    fun getAppContext(): Context {
        return context
    }

    fun getProxy(context: Context): HttpProxyCacheServer? {
        val app: MyApp = context.applicationContext as MyApp
        return if (app.proxy == null) app.newProxy().also { app.proxy = it } else app.proxy
    }

    private fun newProxy(): HttpProxyCacheServer? {
        return HttpProxyCacheServer.Builder(this)
            .maxCacheSize((1024 * 1024 * 1024).toLong())
            .build()
        // return new HttpProxyCacheServer(this);
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        // DataBindingUtil.setDefaultComponent(AppDataBindingComponent())

        context = applicationContext
        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().sendUnsentReports()

        val databaseProvider: DatabaseProvider = ExoDatabaseProvider(this)
        val leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(100)

        if (simpleCache == null) {
            simpleCache =
                SimpleCache(cacheDir, leastRecentlyUsedCacheEvictor, ExoDatabaseProvider(this))
        }

        /* startKoin {
             androidContext(this@MyApp)
             modules(
                 listOf(
                     setAppointmentModule,
                     salonDetailsModule
                 )
             )
         }*/

        initTimber()
    }

    private fun initTimber() {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return super.createStackElementTag(element) + " line: " + element.lineNumber
            }
        })
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}