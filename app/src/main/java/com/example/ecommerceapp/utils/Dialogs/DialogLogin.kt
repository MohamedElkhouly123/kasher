package com.example.ecommerceapp.utils.Dialogs

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.view.activity.UserCycleActivity


class DialogLogin(var generalDataSendedModel: GeneralDataSendedModel) : DialogFragment() {
    var activity: Activity?
    var updateValue: EditText? = null
    var title: TextView? = null
    var login: TextView? = null
    var skip: TextView? = null
    var close: ImageView? = null
    var newValue: String? = null
//    var dialog: Dialog? = null
    private var navController: NavController? = null

    init {
        activity = generalDataSendedModel.activity
        //        this.navController = navController;
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        val inflater = requireActivity().layoutInflater
        val rootView = inflater.inflate(R.layout.dialog_making_login, null)
        navController = findNavController(requireActivity(), R.id.home_activity_fragment_normal)
//        View rootView= getActivity().getLayoutInflater().inflate(R.layout.dialog_login, null);
        login = rootView.findViewById(R.id.login)
        skip = rootView.findViewById(R.id.skip)
        //        close=rootView.findViewById(R.id.dialog_edit_frofile_close_btn);
//        title=rootView.findViewById(R.id.dialogTitle);
//        EditProfileFragment editProfileFragment=new EditProfileFragment();
        skip?.setOnClickListener(View.OnClickListener { // TODO Auto-generated method stub
         doOnback()
        })
        //        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//
//            }
//        });
        login?.setOnClickListener(View.OnClickListener {
            generalDataSendedModel.navController!!.navigate(R.id.loginFragment)
//            val i = Intent(getActivity(), UserCycleActivity::class.java)
//            requireActivity().startActivity(i)
            //                getActivity().finish();\

            dismiss()
        })
        isCancelable = false
        builder.setView(rootView)
        val dialog: Dialog = builder.create()
        dialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action === KeyEvent.ACTION_UP) {
             doOnback()
            }
            true
        }
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        return dialog
        //        return new AlertDialog.Builder(getActivity())
//                .setView(rootView)
//                .setCancelable(true)
//                .create();
    }

    private fun doOnback() {
        generalDataSendedModel.from="dialogLoginBack"
        generalDataSendedModel.makeChangeInFragment!!.doChanges(generalDataSendedModel)
        dismiss()
    }


    companion object {
        var type: String? = null
        var value: String? = null
    }
}