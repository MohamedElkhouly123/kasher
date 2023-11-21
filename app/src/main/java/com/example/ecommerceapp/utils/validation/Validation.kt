package com.example.ecommerceapp.utils.validation

import android.app.Activity
import android.content.Context
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Spinner
import com.example.ecommerceapp.R
import com.example.ecommerceapp.utils.ToastCreator
import com.google.android.material.textfield.TextInputLayout
import org.aviran.cookiebar2.CookieBar
import java.util.regex.Matcher
import java.util.regex.Pattern


object Validation {
    private var x: Boolean=true
    private const val STRING_PATTERN = "^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$"
    private var pattern: Pattern=Pattern.compile(STRING_PATTERN)
    private lateinit var matcher: Matcher
    private const val EMAIL_PATTERN =
        "^[_A-Za-z0-9-%+]+(\\.[_A-Za-z0-9-%+]+)*@[_A-Za-z0-9-%+]+(\\.[_A-Za-z0-9-%+]+)*(\\.[A-Za-z]{2,})$"

    //    private static String EMAIL_PATTERN = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{1,63}$";
    //private static String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    fun cleanError(textInputLayoutList: List<TextInputLayout>) {
        for (i in textInputLayoutList.indices) {
            textInputLayoutList[i].isErrorEnabled = false

        }
    }

    fun cleanError2(editTextList: List<EditText>) {
        for (i in editTextList.indices) {
            editTextList[i].error = null

        }
    }


    //    public static void cleanEditTextError(List<EditText> editTexts) {
    //
    //        for (int i = 0; i < editTexts.size(); i++) {
    //            editTexts.get(i).setError(false);
    //        }
    //    }
    fun validationLength(activity: Activity?, text: String, errorText: String?): Boolean {
        return if (text.length <= 0) {
            ToastCreator.onCreateErrorToast(activity!!, errorText)
            false
        } else {
            true
        }
    }

    fun validationLength(text: EditText, errorText: String?): Boolean {
        return if (text.length() <= 0) {
            validationLengthClearError(text,errorText,0)
            text.error = errorText
            false
        } else {
            true
        }
    }

    fun cleanEditTextError(editTexts: List<EditText>) {
        for (i in editTexts.indices) {
            editTexts[i].error = null
        }
    }

    fun validationEmail2(activity: Activity, email: EditText): Boolean {
        return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = activity.getString(R.string.invalid_email)
            validationLengthClearEmailError(email,activity.getString(R.string.invalid_email),0)
            false
        } else {
            true
        }
    }

    fun validationPassword2(password: EditText, length: Int, errorText: String?): Boolean {
        validationLength(password, errorText, length)
        validationStringIsCharAndNumber(password, errorText)
        return true
    }

    fun validationLength(text: TextInputLayout, errorText: String?): Boolean {
        return if (text.editText!!.length() <= 0) {
            validationLengthClearError(text,errorText)
            text.error = errorText
            false
        } else {
            true
        }
    }

    fun validationLengthInput(text: TextInputLayout, errorText: String?): Boolean {
        return if (text.editText!!.length() <= 0) {
            validationLengthClearError(text,errorText)
            text.error = errorText
            false
        } else {
            true
        }
    }

    fun validationLength(
        activity: Activity?,
        text: String,
        errorText: String?,
        length: Int
    ): Boolean {
        return if (text.length <= length) {
            ToastCreator.onCreateErrorToast(activity!!, errorText)
            false
        } else {
            true
        }
    }

    fun validationLength(text: EditText, errorText: String?, length: Int): Boolean {
        return if (text.length() <= length) {
            validationLengthClearError(text,errorText,length)
            text.error = errorText
            false
        } else {
            true
        }
    }

    fun validationLength(text: TextInputLayout, errorText: String?, length: Int): Boolean {
        return if (text.editText!!.length() < length) {
            validationLengthClearError(text,errorText,length)
            text.error = errorText
            false
        } else {
            true
        }
    }

    fun validationLengthClearError(textLy: TextInputLayout, errorText: String?, length: Int){
        textLy.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(text: Editable) {
                if (text.trim().length < length) {
                    textLy.error = errorText
                } else {
                    textLy.isErrorEnabled = false
                }
            }
        })

    }

    fun validationLengthClearPhoneError(textLy: TextInputLayout, errorText: String?, length: Int):Boolean{
        checkIfCorrectPhone(textLy,errorText,textLy.editText!!.text,length)
        textLy.editText?.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(text: Editable) {
                checkIfCorrectPhone(textLy,errorText,text,length)
            }
        })
        return x
    }

    private fun checkIfCorrectPhone(
        textLy: TextInputLayout,
        errorText: String?,
        text: Editable,
        length: Int
    ) {
        if (text.trim().length == length&&PhoneNumberUtils.isGlobalPhoneNumber(text.trim().toString())&&android.util.Patterns.PHONE.matcher(text.trim().toString()).matches()) {
            textLy.isErrorEnabled = false
            x= true
        } else {
            textLy.error = errorText
            x= false
        }
    }

    fun validationLengthClearError(textLy: TextInputLayout, errorText: String?){
        textLy.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(text: Editable) {
                if (text.trim().length <= 0) {
                    textLy.error = errorText
                } else {
                    textLy.isErrorEnabled = false
                }
            }
        })

    }

    fun validationLengthClearError(textLy: EditText, errorText: String?, length: Int){
        textLy?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(text: Editable) {
                if (text.trim().length < length) {
                    textLy.error = errorText
                } else {
                    textLy.error = null
                }
            }
        })

    }


    fun validationLengthClearEmailError(textLy: EditText, errorText: String?, length: Int){
        textLy?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(text: Editable) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(textLy.text.toString()).matches()) {
                    textLy.error = errorText
                } else {
                    textLy.error = null
                }
            }
        })

    }

    fun validationLengthClearEmailError(textLy: TextInputLayout, errorText: String?, length: Int){
        textLy?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(text: Editable) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(textLy.editText?.text.toString()).matches()) {
                    textLy.error = errorText
                } else {
                    textLy.isErrorEnabled = false
                }
            }
        })

    }


    fun validationLengthClearError(textLy: EditText, errorText: String?){
        textLy?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(text: Editable) {
                if (text.trim().length <= 0) {
                    textLy.error = errorText
                } else {
                    textLy.error = null
                }
            }
        })

    }


    fun validationEmailLength(text: TextInputLayout, errorText: String?, length: Int): Boolean {
        return if (text.editText!!.length() < length) {
            text.editText!!.error = errorText
            validationLengthClearError(text,errorText,length)
            false
        } else {
            true
        }
    }

    fun validationLengthZero(text: TextInputLayout, errorText: String?, length: Int): Boolean {
        return if (text.editText!!.length() == length) {
            //            text.setError(errorText);
            false
        } else {
            true
        }
    }

    fun validationStringIsCharAndNumber(
        activity: Activity?,
        text: String,
        errorText: String?
    ): Boolean {
        matcher = pattern.matcher(text.toString())
        return if (!matcher.matches()) {
            ToastCreator.onCreateErrorToast(activity!!, errorText)
            false
        } else {
            true
        }
    }

    fun validationStringIsCharAndNumber(text: EditText, errorText: String?): Boolean {
        matcher = pattern.matcher(text.toString())
        return if (!matcher.matches()) {
            text.error = errorText
            false
        } else {
            true
        }
    }

    fun validationStringIsCharAndNumber(text: TextInputLayout, errorText: String?): Boolean {
        matcher = pattern.matcher(text.toString())
        return if (!matcher.matches()) {
            text.error = errorText
            false
        } else {
            true
        }
    }

    fun validationStringIsNumber(activity: Activity?, text: String, errorText: String?): Boolean {
        return try {
            val convert = text.toInt()
            true
        } catch (e: Exception) {
            ToastCreator.onCreateErrorToast(activity!!, errorText)
            false
        }
    }

    fun validationStringIsNumber(text: EditText, errorText: String?): Boolean {
        return try {
            val convert = text.text.toString().toInt()
            true
        } catch (e: Exception) {
            text.error = errorText
            false
        }
    }

    fun validationStringIsNumber(text: TextInputLayout, errorText: String?): Boolean {
        return try {
            val convert = text.editText!!.text.toString().toInt()
            true
        } catch (e: Exception) {
            text.editText!!.error = errorText
            false
        }
    }

    fun validationEditTextsEmpty(editTexts: List<EditText>, errorText: String?): Boolean {
        val booleans: MutableList<Boolean> = ArrayList()
            for (i in 0 until editTexts.size) {
                if (!validationLength(editTexts[i], errorText)) {
                booleans.add(false)
            } else {
                booleans.add(true)
            }
        }
        return if (booleans.contains(false) && !booleans.contains(true)) {
            false
        } else {
            true
        }
    }

    fun validationTextInputLayoutListEmpty(
        textInputLayoutList: List<TextInputLayout?>,
        errorText: String?
    ): Boolean {
        for (i in 0 until textInputLayoutList.size) {
         return if (!validationLengthInput(
                    textInputLayoutList[i]!!, errorText
                )
            ) {
                false
            } else {
                true
            }
        }
       return true
    }

        fun validationSpinnersEmpty(spinners: List<Spinner>): Boolean {
            val booleans: MutableList<Boolean> = ArrayList()
            for (i in spinners.indices) {
                if (spinners[i].selectedItemPosition == 0) {
                    booleans.add(false)
                } else {
                    booleans.add(true)
                }
            }
            return if (booleans.contains(false) && !booleans.contains(true)) {
                false
            } else {
                true
            }
        }

        fun validationAllEmpty(
            editTexts: List<EditText>?,
            textInputLayouts: List<TextInputLayout?>?,
            spinners: List<Spinner>,
            errorText: String?
        ): Boolean {
            return if (validationTextInputLayoutListEmpty(textInputLayouts!!, errorText)
                && validationSpinnersEmpty(spinners)
            ) {
                true
            } else {
                false
            }
        }


        fun validationPhone(activity: Activity, phone: String): Boolean {
            val manager = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val locale = manager.simCountryIso.toUpperCase()
            val country = Country()
            val country1 = country.getCountry(locale)
            val phone1 = phone.replace("+2", "")
            return if (phone1.length >= country1!!.length_min && phone.length <= country1.length_max) {
                true
            } else {
                ToastCreator.onCreateErrorToast(
                    activity,
                    activity.getString(R.string.invalid_phone1) + " " + country1.length_min
                            + " " + activity.getString(R.string.invalid_phone2)
                )
                false
            }
        }

        fun validationPhone(activity: Activity, phone: EditText): Boolean {
            val manager = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val locale = manager.simCountryIso.toUpperCase()
            val country = Country()
            val country1 = country.getCountry(locale)
            val phone1 = phone.text.toString().replace("+2", "")
            return if (phone1.length >= country1!!.length_min && phone.text.length <= country1.length_max) {
                true
            } else {
                //            phone.setError(activity.getString(R.string.invalid_phone1) + " " + country1.getLength_min()
                //                    + " " + activity.getString(R.string.invalid_phone2));
                phone.error = activity.getString(R.string.invalid_phone1)
                false
            }
            //        String phone1 = phone.getText().toString().replace("+1", "");

//        if (phone1.length() >= country1.getLength_min() && phone.getEditText().getText().length() <= country1.getLength_max()) {
////        if (phone.getText().length() == 8||phone.getText().length() == 10){
//            return true;
//        } else {
//            phone.setError(activity.getString(R.string.invalid_phone1));
//            return false;
//        }
        }

        fun validationPhone(activity: Activity, phone: TextInputLayout): Boolean {
            val manager = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val locale = manager.simCountryIso.toUpperCase()
            val country = Country()
            val country1 = country.getCountry(locale)
            val phone1 = phone.editText!!.text.toString().replace("+2", "")

//        if (phone1.length() >= country1.getLength_min() && phone.getEditText().getText().length() <= country1.getLength_max()) {
            return if (phone.editText!!.text.length == 11) {
                true
            } else {
                validationLengthClearPhoneError(phone,activity.getString(R.string.invalid_phone1),0)
                phone.error = activity.getString(R.string.invalid_phone1)
                false
            }
        }

        fun validationEmail(activity: Activity, email: String): Boolean {
            return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()) {
                ToastCreator.onCreateErrorToast(
                    activity,
                    activity.getString(R.string.invalid_email)
                )
                false
            } else {
                true
            }
        }

        fun validationEmail(activity: Activity, email: EditText): Boolean {
            return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text!!.toString())
                    .matches()
            ) {
                validationLengthClearEmailError(email,activity.getString(R.string.invalid_email),0)

                email.error = activity.getString(R.string.invalid_email)
                false
            } else {
                true
            }
        }

        fun validationEmail(activity: Activity, email: TextInputLayout): Boolean {
            return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.editText!!.text.toString())
                    .matches()
            ) {
                validationLengthClearEmailError(email,activity.getString(R.string.invalid_email),0)
                email.error = activity.getString(R.string.invalid_email)
                false
            } else {
                true
            }
        }

        fun validationPassword(
            activity: Activity?,
            password: String,
            length: Int,
            errorText: String?
        ): Boolean {
            validationLength(activity, password, errorText, length)
            validationStringIsCharAndNumber(activity, password, errorText)
            return true
        }

        fun validationPassword(password: EditText, length: Int, errorText: String?): Boolean {
//        validationLength(password, errorText, length)
//        validationStringIsCharAndNumber(password, errorText)
//        return true
            return if (validationLength(password, errorText, length)) {
                true
            } else {
                false
            }
        }

        fun validationPassword(
            password: TextInputLayout,
            length: Int,
            errorText: String?
        ): Boolean {
            return if (validationLength(password, errorText, length)) {
                true
            } else {
                false
            }
        }

        fun validationConfirmPassword(
            activity: Activity,
            password: String,
            confirmPassword: String
        ): Boolean {
            return if (password == confirmPassword) {
                true
            } else {
                ToastCreator.onCreateErrorToast(
                    activity,
                    activity.getString(R.string.invalid_confirm_password)
                )
                false
            }
        }

        fun validationConfirmPassword(
            activity: Activity,
            password: EditText,
            confirmPassword: EditText
        ): Boolean {
            return if (password.text.toString() == confirmPassword.text.toString()) {
                true
            } else {
                confirmPassword.error = activity.getString(R.string.invalid_confirm_password)
                false
            }
        }

        fun validationConfirmPassword(
            activity: Activity,
            password: TextInputLayout,
            confirmPassword: TextInputLayout
        ): Boolean {
            return if (password.editText!!.text.toString() == confirmPassword.editText!!.text.toString()) {
                true
            } else {
                confirmPassword.error = activity.getString(R.string.invalid_confirm_password)
                false
            }
        }

        fun validationAllEmpty2(
            editTexts: List<EditText>,
            spinners: List<Spinner?>?,
            errorText: String?
        ): Boolean {
            val booleans: MutableList<Boolean> = ArrayList()
            for (i in editTexts.indices) {
                if (!validationLength(editTexts[i], errorText)) {
                    booleans.add(false)
                } else {
                    booleans.add(true)
                }
            }
            return if (booleans.contains(false) && !booleans.contains(true)) {
                false
            } else {
                true
            }
        }

        fun showCookieMsg2(
            title: String?,
            msg: String?,
            activity: Activity?,
            color: Int,
            popUpPosition: Int
        ) {
            CookieBar.build(activity)
                .setTitle(title)
                .setMessage(msg)
                .setBackgroundColor(color)
                .setCookiePosition(popUpPosition)
                .setDuration(4000)
                .show()
        }

    }