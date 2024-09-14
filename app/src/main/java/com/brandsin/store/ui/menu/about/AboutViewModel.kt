package com.brandsin.store.ui.menu.about

import androidx.databinding.ObservableField
import com.brandsin.store.R
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.model.menu.about.AboutItem
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.MyApp.Companion.context
import com.brandsin.store.utils.PrefMethods
import com.brandsin.user.model.menu.settings.PhoneNumberResponse
import com.brandsin.user.model.menu.settings.SocialLinks
import com.brandsin.user.model.menu.settings.SocialLinksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AboutViewModel : BaseViewModel() {

    var aboutAdapter = AboutAdapter()

    var obsVersion = ObservableField<String>()
    var obsPhoneNumber = ObservableField<String>()

    var socialLinks = SocialLinks()

    init {
        getAboutItems()
    }

    private fun getAboutItems() {
        getSocialLinks()
        getPhoneNumber()
        val aboutList = arrayListOf(
            AboutItem(1, context.getString(R.string.common_questions)),
            AboutItem(2, context.getString(R.string.share_your_rate_about_app))
        )
        aboutAdapter.updateList(aboutList)
    }

    fun onFaceClicked() {
        setValue(Codes.OPEN_FACE)
    }

    fun onInstaClicked() {
        setValue(Codes.OPEN_INSTA)
    }

    fun onTwitterClicked() {
        setValue(Codes.OPEN_TWITTER)
    }

    fun onTikTokClicked() {
        setValue(Codes.TIKTOK_CLICKED)
    }

    fun onWhatsClicked() {
        setValue(Codes.WHATSAPP_CLICKED)
    }

    private fun getSocialLinks() {
        obsIsFull.set(false)
        obsIsLoading.set(true)
        requestCall<SocialLinksResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().getSocialLinks(
                    "social_links",
                    PrefMethods.getLanguage()
                )
            }
        })
        { res ->
            when (res!!.isSuccess) {
                true -> {
                    obsIsFull.set(true)
                    obsIsLoading.set(false)
                    socialLinks = res.socialLinks!!
                }

                else -> {}
            }
        }
    }

    fun getPhoneNumber() {
        requestCall<PhoneNumberResponse?>({
            withContext(Dispatchers.IO) {
                return@withContext getApiRepo().getPhoneNumber(
                    "phone_number",
                    PrefMethods.getLanguage()
                )
            }
        })
        { res ->
            when (res!!.isSuccess) {
                true -> {
                    obsPhoneNumber.set(res.phoneNumber.toString())
                }

                else -> {}
            }
        }
    }
}