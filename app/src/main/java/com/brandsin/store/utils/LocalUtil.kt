package com.brandsin.store.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.brandsin.store.ui.activity.home.HomeActivity
import java.util.*

/**
 * Created by MouazSalah on 18/11/2020.
 **/
object LocalUtil {
    fun onAttach(base: Context): Context {
        return setLocale(base, PrefMethods.getLanguage(base))
    }

    fun setLocale(context: Context, language: String): Context {
        PrefMethods.setLanguage(language, context)
        return if (isBiggerThanOrEqualAndroidN24()) {
            updateResources(context, language)
        } else updateResourcesLegacy(context, language)
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }
    @Suppress("DEPRECATION")
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
    private fun isBiggerThanOrEqualAndroidN24(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    fun changeLanguage(activity: AppCompatActivity) {
        val locale = Locale(PrefMethods.getLanguage())
        val activityRes: Resources = activity.resources
        val activityConfig = activityRes.configuration
        activityConfig.setLocale(locale)
        activityRes.updateConfiguration(activityConfig, activityRes.displayMetrics)
        val appRes: Resources = MyApp.getInstance().applicationContext.resources
        val appConfig = appRes.configuration
        appConfig.setLocale(locale)
        appRes.updateConfiguration(
            appConfig,
            appRes.displayMetrics
        )

        activity.window.decorView.layoutDirection = when (PrefMethods.getLanguage()) {
            "ar" -> View.LAYOUT_DIRECTION_RTL
            "en" -> View.LAYOUT_DIRECTION_LTR
            else -> View.LAYOUT_DIRECTION_LOCALE
        }
    }

    //when u click on button change lang put this method
    fun bla(act:FragmentActivity){
        PrefMethods.setLanguage(
                when (PrefMethods.getString(Const.PREF_LANG, Const.DEFAULT_LANG)) {
                    "ar" -> "en"
                    else -> "ar"
                }
        )
        //restart app here
        act.startActivity(Intent(act, HomeActivity::class.java))
        act.finishAffinity()
    }

}