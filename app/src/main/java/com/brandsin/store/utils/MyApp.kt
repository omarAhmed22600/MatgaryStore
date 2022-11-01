package com.brandsin.store.utils

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import timber.log.Timber

class MyApp : Application()
{
    companion object{
        private lateinit var mInstance: MyApp
        lateinit var context : Context
        fun getInstance(): MyApp {
            return mInstance
        }
    }

    private fun initTimber() {
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return super.createStackElementTag(element) + " line: " + element.lineNumber
            }
        })
    }

    fun getAppContext(): Context? {
        return context
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this

//        DataBindingUtil.setDefaultComponent(AppDataBindingComponent())

        context = applicationContext

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

    protected override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}