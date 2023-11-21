package com.example.ecommerceapp.utils.Dialogs

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.TextView
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel

class DialogDone {
    var sucessMessage: TextView? = null
    var handler: Handler? = null
    fun showDialog(generalDataSendedModel: GeneralDataSendedModel, message: String?) {
        val dialog = Dialog(generalDataSendedModel.activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_success_complete_order)
        dialog.setCanceledOnTouchOutside(true)
        sucessMessage = dialog.findViewById<View>(R.id.dialog_send_success_message_tv) as TextView
        sucessMessage!!.text = message
        handler = Handler()
        val r = Runnable {
            dialog.dismiss()
            //                }
        }
        handler!!.postDelayed(r, 3000)
        dialog.show()
    }
}