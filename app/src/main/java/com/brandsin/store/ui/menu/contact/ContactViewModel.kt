package com.brandsin.store.ui.menu.contact

import androidx.databinding.ObservableField
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.network.requestCall
import com.brandsin.store.utils.PrefMethods
import com.brandsin.user.model.menu.settings.PhoneNumberResponse
import com.brandsin.user.model.menu.settings.SocialLinks
import com.brandsin.user.model.menu.settings.SocialLinksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactViewModel : BaseViewModel()
{
    var socialLinks = SocialLinks()
    var obsPhoneNumber = ObservableField<String>()

    fun getSocialLinks()
    {
        obsIsFull.set(false)
        obsIsLoading.set(true)
        requestCall<SocialLinksResponse?>({ withContext(Dispatchers.IO) { return@withContext getApiRepo().getSocialLinks("social_links" , PrefMethods.getLanguage()) } })
        { res ->
            when (res!!.isSuccess) {
                true -> {
                    obsIsFull.set(true)
                    obsIsLoading.set(false)
                    socialLinks = res.socialLinks!!
                }
            }
        }
    }

    fun getPhoneNumber()
    {
        requestCall<PhoneNumberResponse?>({ withContext(Dispatchers.IO) { return@withContext getApiRepo().getPhoneNumber("phone_number" , PrefMethods.getLanguage()) } })
        { res ->
            when (res!!.isSuccess) {
                true -> {
                    obsPhoneNumber.set(res.phoneNumber.toString())
                }
            }
        }
    }

    fun onPhoneClicked()
    {
        setValue(Codes.PHONE_CLICKED)
    }

    fun onFaceClicked()
    {
        setValue(Codes.FACE_CLICKED)
    }

    fun onWhatsClicked()
    {
        setValue(Codes.WHATSAPP_CLICKED)
    }

    fun onTwitterClicked()
    {
        setValue(Codes.TWITTER_CLICKED)
    }

    fun onGmailClicked()
    {
        setValue(Codes.GMAIL_CLICKED)
    }

    init {
        getSocialLinks()
        getPhoneNumber()
    }
}