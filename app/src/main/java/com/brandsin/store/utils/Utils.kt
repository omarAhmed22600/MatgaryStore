package com.brandsin.store.utils

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.brandsin.store.R
import com.brandsin.store.ui.activity.DialogActivity
import com.brandsin.user.model.constants.Params
import timber.log.Timber

object Utils
{
    fun startDialogActivity(
        frag: Activity,
        fragmentName: String?,
        requestCodeForResult: Int,
        bundle: Bundle?
    ) {
        val intent = Intent(frag, DialogActivity::class.java)
        intent.putExtra(Params.INTENT_PAGE_DIALOG, fragmentName)
        if (bundle != null) intent.putExtra(Params.BUNDLE_DIALOG, bundle)
        frag.startActivityForResult(intent, requestCodeForResult)
    }

    fun replaceFragment(context: Context, fragment: Fragment?, backStackText: String) {
        try {
            val fragmentManager =
                    (context as FragmentActivity).supportFragmentManager
            val fragmentTransaction =
                    fragmentManager.beginTransaction().replace(R.id.auth_fragment, fragment!!)
            if (backStackText != "") {
                fragmentTransaction.addToBackStack(backStackText)
            }
            fragmentTransaction.commit()
        } catch (e: Exception) {
            //Timber.e(e);
        }
    }


/*    fun replaceFragment(
        context: Context,
        fragment: Fragment?,
        containerResId: Int,
        backStackText: String
    ) {
        try {
            val fragmentManager = (context as FragmentActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction().replace(
                containerResId,
                fragment!!
            )
            if (backStackText != "") {
                fragmentTransaction.addToBackStack(backStackText)
            }
            fragmentTransaction.commit()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }*/


    var couponText: String? = null
    var result = 0
    fun isEmpty(o: Any?): Boolean {
        return if (o == null) true else {
            if (o is String) {
                o.isEmpty()
            } else if (o is Int) {
                o == 0
            } else if (o is Float) {
                o == 0f
            } else { //Double
                o as Double == 0.0
            }
        }
    }

    fun fadeOut(v: View, animListener: AnimListener?) {
        v.alpha = 1.0f
        // Prepare the View for the animation
        v.animate()
                .setDuration(800)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        animListener?.onFinish()
                        super.onAnimationEnd(animation)
                    }
                })
                .alpha(0.0f)
    }

    fun openMail(context: Activity, mailTo: String) {
        try {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:$mailTo")
            context.startActivity(Intent.createChooser(emailIntent, ""))
        } catch (e: java.lang.Exception) {
            Timber.e(e)
        }
    }
    fun openTwitter(context: Activity, link: String?) {
        if (link == null) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.trim { it <= ' ' }))
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }

    fun openInstagram(context: Activity, link: String?) {
        if (link == null) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link.trim { it <= ' ' }))
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e.message)
        }
    }

    fun openLinkedIn(activity: Activity, url: String?) {
        if (url == null) return
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.setPackage("com.linkedin.android")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } catch (e: Exception) {
            try {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (es: Exception) {
                Timber.e("link isnt valid%s", es.message)
            }
        }
    }

    fun callPhone(activity: Activity, number1: String) {
        val number = "tel:$number1"
        val mIntent = Intent(Intent.ACTION_CALL)
        mIntent.data = Uri.parse(number)
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CALL_PHONE
            )
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.CALL_PHONE),
                65
            )

            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //You already have permission
            try {
                activity.startActivity(mIntent)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }
     fun openFacebook(activity: Activity, link: String?) {
        val context: Context = MyApp.getInstance()
        //the correct link:
        //i.e. https://www.facebook.com/YourPageName or www.facebook.com/YourPageName
        if (link == null) {
            //Timber.e("link is null");
            return
        }
        val url: String?
        val FACEBOOK_PAGE_ID: String
        FACEBOOK_PAGE_ID = try {
            if (link.contains("http")) link.split("/".toRegex()).toTypedArray()[3] //i.e. YourPageName
            else link.split("/".toRegex()).toTypedArray()[1] //i.e. YourPageName
        } catch (e: ArrayIndexOutOfBoundsException) {
            //Timber.e("link isn't correct");
            return
        }
        //method to get the right URL to use in the intent
        val packageManager = context.packageManager
        url = try {
            val versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            if (versionCode >= 3002850) { //newer versions of fb app
                "fb://facewebmodal/f?href=$link"
            } else { //older versions of fb app
                "fb://page/$FACEBOOK_PAGE_ID"
            }
        } catch (e: PackageManager.NameNotFoundException) {
            link //normal web url
        }
        val facebookIntent = Intent(Intent.ACTION_VIEW)
        facebookIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        facebookIntent.data = Uri.parse(url)
        try {
            context.startActivity(facebookIntent)
        } catch (e: Exception) {
            //Timber.e(e);
        }
    }

    fun openLink(activity: Activity, url1: String?){
        if (url1 == null) return
        var url=""
        if (!url1.contains("http")){
            url="https://"+url1
        }
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        } catch (e: Exception) {
            try {
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (es: Exception) {
                Timber.e("link isnt valid%s", es.message)
            }
        }
    }

    /*
    public static void openDatePicker(Context context, ObservableField<String> result) {
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(
                context,
                (view, year1, month1, dayOfMonth) -> {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year1, month1, dayOfMonth);
                    String strDate = format.format(calendar.getTime());
                    Timber.e(strDate);
                    result.set(strDate);
                },
                year, month, day);
//        mDatePickerDialog.getDatePicker().setMinDate(mCalendar.getTimeInMillis());
        mDatePickerDialog.show();
        mDatePickerDialog.getButton(mDatePickerDialog.BUTTON_POSITIVE).setTextColor(getColorFromRes(R.color.colorAccent));
        mDatePickerDialog.getButton(mDatePickerDialog.BUTTON_NEGATIVE).setTextColor(getColorFromRes(R.color.colorAccent));
    }

    public static void openTimeDialog(Context context, ObservableField<String> obsTime) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(context, (view, hourOfDay, minute1) -> {
            obsTime.set(hourOfDay + ":" + minute1);
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.show();

        mTimePicker.getButton(mTimePicker.BUTTON_POSITIVE).setTextColor(getColorFromRes(R.color.colorAccent));
        mTimePicker.getButton(mTimePicker.BUTTON_NEGATIVE).setTextColor(getColorFromRes(R.color.colorAccent));
    }
*/

    interface AnimListener {
        fun onFinish()
    }
}