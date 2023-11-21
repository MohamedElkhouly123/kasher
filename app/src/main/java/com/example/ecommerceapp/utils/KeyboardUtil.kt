package com.example.ecommerceapp.utils

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.R

import android.view.ViewGroup

import android.content.Intent

import androidx.localbroadcastmanager.content.LocalBroadcastManager

import android.view.Window

import android.view.ViewTreeObserver




class KeyboardUtil(act: Activity, contentView: View) {
    private val decorView: View=act.window.decorView
    private val contentView: View
    fun enable() {
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }

    fun disable() {
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }

    //a small helper to allow showing the editText focus
    var onGlobalLayoutListener = OnGlobalLayoutListener {
        val r = Rect()
        //r will be populated with the coordinates of your view that area still visible.
        decorView.getWindowVisibleDisplayFrame(r)

        //get screen height and calculate the difference with the useable area from the r
        val height = decorView.context.resources.displayMetrics.heightPixels
        val diff = height - r.bottom

        //if it could be a keyboard add the padding to the view
        if (diff != 0) {
            // if the use-able screen height differs from the total screen height we assume that it shows a keyboard now
            //check if the padding is 0 (if yes set the padding for the keyboard)
            if (contentView.paddingBottom != diff) {
                Log.d("diff", "" + diff)
                //set the padding of the contentView for the keyboard
                contentView.setPadding(0, 0, 0, diff)
            }
        } else {
            //check if the padding is != 0 (if yes reset the padding)
            if (contentView.paddingBottom != 0) {
                //reset the padding of the contentView
                contentView.setPadding(0, 0, 0, 0)
            }
        }
    }

    companion object {
        /**
         * Helper to hide the keyboard
         *
         * @param act
         */
        fun hideKeyboard(act: Activity?) {
            if (act != null && act.currentFocus != null) {
                val inputMethodManager =
                    act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(act.currentFocus!!.windowToken, 0)
            }
        }
    }

    init {
//        decorView = act.window.decorView
        this.contentView = contentView

        //only required on newer android versions. it was working on API level 19
        if (Build.VERSION.SDK_INT >= 19) {
            decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        }
    }

//    private val keyboardLayoutListener = OnGlobalLayoutListener {
//        val heightDiff = rootLayout!!.rootView.height - rootLayout!!.height
//        val contentViewTop: Int = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop()
//        val broadcastManager = LocalBroadcastManager.getInstance()
//        if (heightDiff <= contentViewTop) {
//            onHideKeyboard()
//            val intent = Intent("KeyboardWillHide")
//            broadcastManager.sendBroadcast(intent)
//        } else {
//            val keyboardHeight = heightDiff - contentViewTop
//            onShowKeyboard(keyboardHeight)
//            val intent = Intent("KeyboardWillShow")
//            intent.putExtra("KeyboardHeight", keyboardHeight)
//            broadcastManager.sendBroadcast(intent)
//        }
//    }
//
//    private var keyboardListenersAttached = false
//    private var rootLayout: ViewGroup? = null
//
//    protected fun onShowKeyboard(keyboardHeight: Int) {}
//    protected fun onHideKeyboard() {}
//
//    protected fun attachKeyboardListeners() {
//        if (keyboardListenersAttached) {
//            return
//        }
////        rootLayout = findViewById(R.id.rootLayout) as ViewGroup?
//        rootLayout!!.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)
//        keyboardListenersAttached = true
//    }

//    protected fun onDestroy() {
//        super.onDestroy()
//        if (keyboardListenersAttached) {
//            rootLayout!!.viewTreeObserver.removeGlobalOnLayoutListener(keyboardLayoutListener)
//        }
//    }
}