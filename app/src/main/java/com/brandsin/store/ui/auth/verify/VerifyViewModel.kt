package com.brandsin.store.ui.auth.verify

import android.os.CountDownTimer
import androidx.databinding.ObservableField
import com.brandsin.store.R
import com.brandsin.store.database.BaseRepository
import com.brandsin.store.database.BaseViewModel
import com.brandsin.store.model.auth.resendcode.ResendCodeRequest
import com.brandsin.store.model.auth.resendcode.ResendCodeResponse
import com.brandsin.store.model.auth.verifycode.VerifyCodeRequest
import com.brandsin.store.model.auth.verifycode.VerifyCodeResponse
import com.brandsin.store.model.constants.Codes
import com.brandsin.store.utils.MyApp.Companion.context
import com.brandsin.store.utils.PrefMethods
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyViewModel : BaseViewModel()
{
    var verifyRequest = VerifyCodeRequest()
    var resendCodeRequest = ResendCodeRequest()
    var obsTimer = ObservableField<String>()
    var obsClickable = ObservableField<Boolean>()
    var newcode=""
    var userId=""

    private fun startTimer()
    {
        object : CountDownTimer(30000, 1000)
        {
            override fun onTick(millisUntilFinished: Long)
            {
                obsTimer.set("${getString(R.string.resend_in)}  ${millisUntilFinished / 1000}")
                obsClickable.set(false)
            }
            override fun onFinish()
            {
                obsTimer.set(context.getString(R.string.zero_time) )
                obsClickable.set(true)
            }
        }.start()
    }

    fun onResendClicked()
    {
        setClickable()
        setShowProgress(true)
        apiResendCode()
        startTimer()
    }

    fun onNextClicked()
    {
        when {
            verifyRequest.code == null -> {
                setValue(Codes.CODE_EMPTY)
            }
            verifyRequest.code!!.length < 4 -> {
                setValue(Codes.CODE_SHORT)
            }
            else -> {
                setShowProgress(true)
                apiVerify()
            }
        }
    }
    fun apiVerify(){
        if (verifyRequest.phone_number.isNullOrEmpty() || verifyRequest.phone_number.isNullOrBlank()) {
            verifyRequest.phone_number = PrefMethods.getUserData()!!.phoneNumber.toString()
        }
        verifyRequest.lang= PrefMethods.getLanguage()
        val baeRepo = BaseRepository()
        val responseCall: Call<VerifyCodeResponse?> = baeRepo.apiInterface.verify(verifyRequest)
        responseCall.enqueue(object : Callback<VerifyCodeResponse?> {
            override fun onResponse(call: Call<VerifyCodeResponse?>, response: Response<VerifyCodeResponse?>) {
                if (response.isSuccessful) {
                    if (response.body()!!.success!!) {
                        PrefMethods.saveUserData(response.body()!!.data)
                        userId= response.body()!!.data!!.id.toString()
                        setValue(Codes.VERIFY_SUCCESS)
                    }else{
                        setValue(response.body()!!.error.toString())
                    }
                } else {
                    setValue(response.message())
                }
            }
            override fun onFailure(call: Call<VerifyCodeResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }
    fun apiResendCode(){
        resendCodeRequest.phone = verifyRequest.phone_number.toString()
        resendCodeRequest.lang= PrefMethods.getLanguage()
        val baeRepo = BaseRepository()
        val responseCall: Call<ResendCodeResponse?> = baeRepo.apiInterface.resendCode(resendCodeRequest)
        responseCall.enqueue(object : Callback<ResendCodeResponse?> {
            override fun onResponse(call: Call<ResendCodeResponse?>, response: Response<ResendCodeResponse?>) {
                if (response.isSuccessful) {
                    if (response.body()!!.success!!) {
                        newcode= response.body()!!.code.toString()
                        userId= response.body()!!.user_id.toString()
                        setValue(Codes.CODE_RESENT)
                    }else{
                        setValue(response.body()!!.error.toString())
                    }
                } else {
                    setValue(response.message())
                }
            }
            override fun onFailure(call: Call<ResendCodeResponse?>, t: Throwable) {
                setValue(t.message!!)
                setShowProgress(false)
            }
        })
    }
    init {
        startTimer()
    }
}