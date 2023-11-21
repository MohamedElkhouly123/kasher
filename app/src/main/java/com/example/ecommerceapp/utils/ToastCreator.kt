package com.example.ecommerceapp.utils

import android.app.Activity
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.ecommerceapp.R
import com.google.android.material.snackbar.Snackbar
import com.tbuonomo.viewpagerdotsindicator.setBackgroundCompat
import de.hdodenhof.circleimageview.CircleImageView

object ToastCreator {
    private var toast: Toast? =null

    /**
     * To make custom toast in success state
     *
     * @param activity   : opened activity
     * @param toastTitle :toast text
     */
    fun onCreateSuccessToast(activity: Activity, toastTitle: String?) {
        val inflater = activity.layoutInflater
        val layout_id = R.layout.success_toast
        val layout = inflater.inflate(
            layout_id,
            activity.findViewById<View>(R.id.toast_layout_root) as? ViewGroup
        )
        cancelToast()
        val text = layout.findViewById<TextView>(R.id.toast_tv_text)
        text.text = toastTitle
        toast = Toast(activity)
        toast!!.setGravity(Gravity.CENTER_VERTICAL or Gravity.BOTTOM, 0, 50)
        toast!!.duration = Toast.LENGTH_LONG

        toast!!.view = layout
        toast!!.show()
    }

    fun onCreateSuccessToastFull(activity: Activity, toastTitle: String?) {
        val inflater = activity.layoutInflater
        val layout_id = R.layout.success_toast
        val layout = inflater.inflate(
            layout_id,
            activity.findViewById<View>(R.id.toast_layout_root) as? ViewGroup
        )
        cancelToast()
        val text = layout.findViewById<TextView>(R.id.toast_tv_text)
        text.text = toastTitle
        toast = Toast(activity)
//        toast.setMargin(10F, 0F)
        toast!!.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast!!.duration = Toast.LENGTH_LONG

        toast!!.view = layout
        toast!!.show()
    }


    fun onCreateErrorToastFull(activity: Activity, toastTitle: String?) {
        val inflater = activity.layoutInflater
        val layout_id = R.layout.toast_error
        val layout = inflater.inflate(
            layout_id,
            activity?.findViewById<View>(R.id.toast_layout_root) as? ViewGroup
        )
        cancelToast()
        val text = layout.findViewById<TextView>(R.id.toast_tv_text)
        text.text = toastTitle
        toast = Toast(activity)
        toast!!.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast!!.duration = Toast.LENGTH_LONG
        toast!!.view = layout
        toast!!.show()
    }

    /**
     * To make custom toast with custom icon  in success state
     *
     * @param activity   : opened activity
     * @param toastTitle :toast text
     * @param toastIcon  :toast icon
     */
    fun onCreateSuccessToast(activity: Activity, toastTitle: String?, toastIcon: Int) {
        val inflater = activity.layoutInflater
        val layout_id = R.layout.success_toast
        val layout = inflater.inflate(
            layout_id,
            activity.findViewById<View>(R.id.toast_layout_root) as? ViewGroup
        )
        cancelToast()
        val text = layout.findViewById<TextView>(R.id.toast_tv_text)
        text.text = toastTitle
        val icon: CircleImageView = layout.findViewById(R.id.toast_iv_icon)
        icon.setImageResource(toastIcon)
        toast = Toast(activity)
        toast!!.setGravity(Gravity.CENTER_VERTICAL or Gravity.BOTTOM, 0, 100)
        toast!!.duration = Toast.LENGTH_LONG
        toast!!.view = layout
        toast!!.show()
    }

    fun onCreateSuccessSnackBar(
        activity: Activity,
        toastTitle: String?,
        toastActionTitle: String?,
        toastIcon: Int?,
        toastBackground: Int?,
        hasAction: Boolean
    ) {
//        val inflater = activity.layoutInflater
//        val layout_id = R.layout.success_toast
//        val layout = inflater.inflate(
//            layout_id,
//            activity.findViewById<View>(R.id.toast_layout_root) as? ViewGroup
//        )
        val snackbar = Snackbar.make(
            activity.findViewById(R.id.placeSnackBar),
           "",
            Snackbar.LENGTH_INDEFINITE
        ).setTextColor(activity.resources.getColor(android.R.color.white))
//        if (hasAction){
            snackbar.setAction(toastTitle) {}
                .setActionTextColor(activity.resources.getColor(android.R.color.white))
//    }
    //            .setBackgroundTint(resources.getColor(R.color.app_color))
//        snackbar.view.setBackgroundColor(ContextCompat.getColor(this!!,R.color.app_color))
        val snackbarLayout = snackbar.view
        snackbarLayout.setBackgroundCompat(
            ContextCompat.getDrawable(activity!!,
                toastBackground!!))
        val textView =
            snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_action) as TextView
        textView!!.setCompoundDrawablesRelativeWithIntrinsicBounds(
           0,
            0,
            toastIcon!!,
            0
        )
//        textView.gravity= Gravity.CENTER_VERTICAL
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, activity.getResources().getDimension(R.dimen.my_text_size))
        textView.compoundDrawablePadding =
            activity.resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
        snackbar.show()
    }

    /**
     * To make custom toast in error state
     *
     * @param activity   : opened activity
     * @param toastTitle :toast text
     */
    fun onCreateErrorToast(activity: Activity, toastTitle: String?) {
        val inflater = activity.layoutInflater
        val layout_id = R.layout.toast_error
        val layout = inflater.inflate(
            layout_id,
            activity?.findViewById<View>(R.id.toast_layout_root) as? ViewGroup
        )
        cancelToast()
        val text = layout.findViewById<TextView>(R.id.toast_tv_text)
        text.text = toastTitle
        toast = Toast(activity)
        toast!!.setGravity(Gravity.CENTER_VERTICAL or Gravity.BOTTOM, 0, 100)
        toast!!.duration = Toast.LENGTH_LONG
        toast!!.view = layout
        toast!!.show()
    }


    /**
     * To make custom toast with custom icon in error state
     *
     * @param activity   : opened activity
     * @param toastTitle :toast text
     * @param toastIcon  :toast icon
     */

    fun onCreateErrorToast(activity: Activity, toastTitle: String?, toastIcon: Int) {
        val inflater = activity.layoutInflater
        val layout_id = R.layout.toast_error
        val layout = inflater.inflate(
            layout_id,
            activity.findViewById<View>(R.id.toast_layout_root) as? ViewGroup
        )
        cancelToast()
        val text = layout.findViewById<TextView>(R.id.toast_tv_text)
        text.text = toastTitle
        val icon: CircleImageView = layout.findViewById(R.id.toast_iv_icon)
        icon.setImageResource(toastIcon)
        toast = Toast(activity)
        toast!!.setGravity(Gravity.CENTER_VERTICAL or Gravity.BOTTOM, 0, 100)
        toast!!.duration = Toast.LENGTH_LONG
        toast!!.view = layout
        toast!!.show()
    }

    private fun cancelToast() {
        if (toast!=null){
            toast?.cancel()
        }
    }

    fun customToast(activity: Activity, ToastTitle: String?, failed: Boolean) {
        val inflater = activity.layoutInflater
        val layout_id: Int
        layout_id = if (failed) {
            R.layout.toast
        } else {
            R.layout.success_toast
        }
        cancelToast()
        val layout = inflater.inflate(layout_id, activity.findViewById(R.id.toast_layout_root))
        val text = layout.findViewById<TextView>(R.id.toast_tv_text)
        text.text = ToastTitle
        toast = Toast(activity)
        toast!!.setGravity(Gravity.CENTER_VERTICAL or Gravity.BOTTOM, 0, 100)
        toast!!.duration = Toast.LENGTH_LONG
        toast!!.view = layout
        toast!!.show()
    }
}