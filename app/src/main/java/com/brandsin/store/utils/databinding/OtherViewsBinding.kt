package com.brandsin.store.utils.databinding

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.brandsin.store.R
import com.brandsin.store.utils.MyApp
import com.brandsin.store.utils.PrefMethods

class OtherViewsBinding {
    companion object {
        @JvmStatic
        @BindingAdapter("showLogoutBtn")
        fun showLogoutBtn(view: View, state: Boolean?)
        {
            if (PrefMethods.getUserData() != null)
            {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("setImage")
        fun setImage(view: ImageView, url: String?) {
            when {
                url.isNullOrEmpty() -> {
                    Glide.with(view.context).load(R.drawable.app_logo).error(R.drawable.app_logo).into(view)
                }
                else -> {
                    Glide.with(view.context).load(url).error(R.drawable.app_logo).into(view)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("isLogin")
        fun isLogin(view: View, state: Boolean?)
        {
            when (state) {
                true -> {
                    view.visibility = View.GONE
                }
                else -> {
                    view.visibility = View.VISIBLE
                }
            }
        }

        /* Showing Shimmer Layouts while requesting API calls */
        @JvmStatic
        @BindingAdapter("isLoading")
        fun isLoading(view: View, value: Boolean?) {
            when (value) {
                false -> {
                    view.visibility = View.GONE
                }
                else -> {
                    view.visibility = View.VISIBLE
                }
            }
        }

        /* This Method for showing RecyclerView If passed Size Not equal zero */
        @JvmStatic
        @BindingAdapter("isFull")
        fun isFull(view: View, state: Boolean?) {
            when (state) {
                true -> {
                    view.visibility = View.VISIBLE
                }
                else -> {
                    view.visibility = View.GONE
                }
            }
        }

        /* This Method for showing Empty layouts If passed Size equal zero */
        @JvmStatic
        @BindingAdapter("isEmpty")
        fun isEmpty(view: View, state: Boolean?) {
            when (state) {
                true -> {
                    view.visibility = View.VISIBLE
                }
                else -> {
                    view.visibility = View.GONE
                }
            }
        }
        @JvmStatic
        @BindingAdapter("setDeliveryTime")
        fun setDeliveryTime(view: TextView, time: String?) {
            when (time) {
                "0000-00-00 00:00:00" -> {
                    view.visibility = View.GONE
                    view.text = ""
                }
                else -> {
                    view.visibility = View.VISIBLE
                    view.text = time
                }
            }
        }
        @JvmStatic
        @BindingAdapter("setStatus")
        fun setStatus(view: TextView, status: String?) {
            when (status) {
                "pending" -> {
                    view.setTextColor(ContextCompat.getColor(view.context, R.color.order_prepared_color))
                    view.text = view.context.getString(R.string.pending)
                }
                "accepted" -> {
                    view.setTextColor(ContextCompat.getColor(view.context, R.color.order_prepared_color))
                    view.text = view.context.getString(R.string.accepted)
                }
                "accepted_by_delivery" -> {
                    view.setTextColor(ContextCompat.getColor(view.context, R.color.order_prepared_color))
                    view.text = view.context.getString(R.string.accepted)
                }
                "accepted_with_delivery" -> {
                    view.setTextColor(ContextCompat.getColor(view.context, R.color.order_prepared_color))
                    view.text = view.context.getString(R.string.accepted)
                }
                "shipping" -> {
                    view.setTextColor(ContextCompat.getColor(view.context, R.color.order_accepted_color))
                    view.text = view.context.getString(R.string.on_way)
                }
                "shipped" -> {
                    view.setTextColor(ContextCompat.getColor(view.context, R.color.order_accepted_color))
                    view.text = view.context.getString(R.string.on_way)
                }
                "completed" -> {
                    view.setTextColor(ContextCompat.getColor(view.context, R.color.order_completed_color))
                    view.text = view.context.getString(R.string.completed)
                }
                "cancelled" -> {
                    view.setTextColor(ContextCompat.getColor(view.context, R.color.order_rejected_color))
                    view.text = view.context.getString(R.string.cancelled_by_user)
                }
                "rejected_by_store" -> {
                    view.setTextColor(ContextCompat.getColor(view.context, R.color.order_rejected_color))
                    view.text = view.context.getString(R.string.cancelled_by_store)
                }
                else -> {
                    view.setTextColor(ContextCompat.getColor(view.context, R.color.order_rejected_color))
                    view.text = view.context.getString(R.string.cancelled)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("setStatusImg")
        fun setStatusImg(view: ImageView, status: String?) {
            when (status) {
                "pending" -> {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.order_prepared_color))
                }
                "accepted" -> {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.order_prepared_color))
                }
                "accepted_by_delivery" -> {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.order_prepared_color))
                }
                "accepted_with_delivery" -> {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.order_prepared_color))
                }
                "shipping" -> {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.order_accepted_color))
                }
                "shipped" -> {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.order_accepted_color))
                }
                "completed" -> {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.order_completed_color))
                }
                "cancelled" -> {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.order_rejected_color))
                }
                "rejected_by_store" -> {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.order_rejected_color))
                }
                else -> {
                    view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.order_rejected_color))
                }
            }
        }

        @JvmStatic
        @BindingAdapter("registerState")
        fun registerState(view: ImageView, isEntered: Boolean?) {
            when (isEntered == false) {
                true -> {
                    Glide.with(view.context).load(R.drawable.ic_step_not_selected).error(R.drawable.app_logo).into(view)
                }
                else -> {
                    Glide.with(view.context).load(R.drawable.ic_step_selected).error(R.drawable.app_logo).into(view)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("registerLineState")
        fun registerLineState(view: ImageView, isEntered: Boolean?) {
            when (isEntered == false) {
                true -> {
                    Glide.with(view.context).load(R.drawable.ic_line_not_selected).error(R.drawable.app_logo).into(view)
                }
                else -> {
                    Glide.with(view.context).load(R.drawable.ic_line_selected).error(R.drawable.app_logo).into(view)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("offerImg")
        fun offerImg(view: ImageView, url: Int?) {
            when (url) {
                1 -> {
                    Glide.with(view.context).load(url).error(R.drawable.french_fries).into(view)
                }
                2 -> {
                    Glide.with(view.context).load(url).error(R.drawable.coffer_img).into(view)
                }
                3 -> {
                    Glide.with(view.context).load(url).error(R.drawable.fuman_img).into(view)
                }
                4 -> {
                    Glide.with(view.context).load(url).error(R.drawable.new_icon).into(view)
                }
                5 -> {
                    Glide.with(view.context).load(url).error(R.drawable.offer_icon).into(view)
                }
                6 -> {
                    Glide.with(view.context).load(url).error(R.drawable.fruites_img).into(view)
                }
                else -> {
                    view.setImageResource(R.drawable.salsa_img)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("paymentImg")
        fun paymentImg(view: ImageView, url: Int?) {
            when (url) {
                1 -> {
                    Glide.with(view.context).load(R.drawable.ic_visa).error(R.drawable.ic_visa).into(view)
                }
                2 -> {
                    Glide.with(view.context).load(R.drawable.ic_cash).error(R.drawable.ic_cash).into(view)
                }
                3 -> {
                    Glide.with(view.context).load(R.drawable.ic_qr_code).error(R.drawable.ic_qr_code).into(view)
                }
                4 -> {
                    Glide.with(view.context).load(R.drawable.ic_wallet).error(R.drawable.ic_wallet).into(view)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("categoryImg")
        fun categoryImg(view: ImageView, url: String?) {
            if (url != null && url.isNotEmpty()) {
                Glide.with(view.context).load(url).error(R.drawable.user_default_img).into(view)
            } else {
                view.setImageResource(R.drawable.user_default_img)
            }
        }

        @JvmStatic
        @BindingAdapter("offersDesc")
        fun visibleGone(view: TextView, state: String?) {
            if (state == null) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
                view.text = state
            }
        }

        @JvmStatic
        @BindingAdapter("notifyRead")
        fun notifyRead(view: CardView, state: String?) {
            if (state !=null ) {
                view.setBackgroundColor(ContextCompat.getColor(MyApp.context, R.color.white))
            } else {
                if (PrefMethods.getLanguage() == "en") {
                    view.setBackgroundResource(R.drawable.notify_unread_bg_ar)

                } else {
                    view.setBackgroundResource(R.drawable.notify_unread_bg_en)
                }

                view.radius = 8F
            }
        }

        @JvmStatic
        @BindingAdapter("headerbg")
        fun headerg(view: LinearLayout, state: Boolean?) {
            if (PrefMethods.getLanguage() == "en") {
                view.setBackgroundResource(R.drawable.notify_unread_bg_ar)
            } else {
                view.setBackgroundResource(R.drawable.notify_unread_bg_en)
            }
        }

        @JvmStatic
        @BindingAdapter("notifyImg")
        fun notifyImg(view: CardView, state: String?) {
            if (state !=null) {
                view.setBackgroundColor(ContextCompat.getColor(MyApp.context, R.color.white))
                view.cardElevation = 0f
            }
        }

        @JvmStatic
        @BindingAdapter("visibleGone")
        fun visibleGone(view: View, state: Boolean?) {
            if (state == true) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("isCollapse")
        fun isCollapse(view: ImageButton, state: Boolean?) {
            if (state == true) {
                Glide.with(view.context).load(R.drawable.ic_sow_prices).error(R.drawable.user_default_img).into(view)
            } else {
                Glide.with(view.context).load(R.drawable.ic_arrow_up).error(R.drawable.user_default_img).into(view)
            }
        }

        @JvmStatic
        @BindingAdapter("userImg")
        fun userImg(view: ImageView, isLogin: Boolean?) {
            if (isLogin!!) {
                Glide.with(view.context).load(PrefMethods.getStoreData()!!.thumbnail).error(R.drawable.app_logo).into(view)
            } else {
                Glide.with(view.context).load(R.drawable.app_logo).error(R.drawable.app_logo).into(view)
            }
        }

        /* This Method for showing RecyclerView */
        @JvmStatic
        @BindingAdapter("isFull")
        fun isFull(view: View, state: Int?) {
            if (state == 0) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
            }
        }

        /* This Method for showing Empty layouts in every Screens */
        @JvmStatic
        @BindingAdapter("isEmpty")
        fun isEmpty(view: View, count: Int?) {
            if (count == 0) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("setBorder")
        fun isEmpty(view: MaterialCardView, show: Boolean?) {
            if (show == true) {
                view.strokeWidth = 1
            } else {
                view.strokeWidth = 0
            }
        }

        @JvmStatic
        @BindingAdapter("orderType" , "recyclerType")
        fun orderType(view: View, type: Int?, recyclerType : Int?)
        {
            /* Show completed orders */
            if (type == 1 && recyclerType == 1)
            {
                view.visibility = View.VISIBLE
            }
            else
            {
                view.visibility = View.GONE
            }
        }

        @JvmStatic
        @BindingAdapter("showPass")
        fun showPass(view : EditText, isShown: Boolean?)
        {
            if(isShown == true)
            {
                view.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
            else
            {
                view.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        @JvmStatic
        @BindingAdapter("showEyeIcon")
        fun showEyeIcon(view : ImageButton, isShown: Boolean?)
        {
            when (isShown) {
                true -> {
                    view.setImageResource(R.drawable.ic_eye)
                }
                else -> {
                    view.setImageResource(R.drawable.ic_visibility_off_24px)
                }
            }
        }

    }
}