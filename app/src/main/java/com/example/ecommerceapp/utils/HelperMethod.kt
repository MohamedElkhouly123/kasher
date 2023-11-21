package com.example.ecommerceapp.utils

import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.Html
import android.text.InputFilter
import android.text.format.DateFormat
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommerceapp.R
import com.example.ecommerceapp.data.local.SharedPreferencesManger
import com.example.ecommerceapp.data.local.SharedPreferencesManger.SaveLanguage
import com.example.ecommerceapp.data.model.DateTxt
import com.example.ecommerceapp.data.model.getGeneralResponse.GeneralDataSendedModel
import com.example.ecommerceapp.utils.interfaces.TryAgainOncall
import com.example.ecommerceapp.utils.notifictionsServices.Config.BASE
import com.example.ecommerceapp.view.activity.HomeCycleActivity
import com.example.ecommerceapp.view.activity.SplashCycleActivity
import com.example.ecommerceapp.view.activity.UserCycleActivity
import com.google.android.gms.common.util.IOUtils
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.textfield.TextInputEditText
import com.yanzhenjie.album.Action
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import com.yanzhenjie.album.AlbumFile
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.aviran.cookiebar2.CookieBar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object HelperMethod {
    var progressDialog: ProgressDialog? = null
    var alertDialog: AlertDialog? = null
    fun replaceFragment(getChildFragmentManager: FragmentManager, id: Int, fragment: Fragment) {
        val transaction = getChildFragmentManager.beginTransaction()
        transaction.replace(id, fragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun replaceFragmentWithAnimation(
        getChildFragmentManager: FragmentManager,
        id: Int,
        fragment: Fragment?,
        fromWhere: String
    ) {
        val transaction = getChildFragmentManager.beginTransaction()
        if (fromWhere === "l") {
//            android.R.anim.slide_in_left
            transaction.setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right
                , R.anim.enter_from_right,
                R.anim.exit_to_left
            )
        }
        if (fromWhere === "r") {
            transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
        if (fromWhere === "t") {
            transaction.setCustomAnimations(
                R.anim.slide_out_down, R.anim.slide_in_down,
                R.anim.slide_in_up, R.anim.slide_out_up)
        }
        if (fromWhere === "b") {
            transaction.setCustomAnimations(
                R.anim.slide_in_up, R.anim.slide_out_up,
            R.anim.slide_out_down, R.anim.slide_in_down)

        }
        //        if(fromWhere=="rr"){
//            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.slide_in_left, R.anim.slide_out_right);}
//        if(fromWhere=="t"){
//            transaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up);}
//        if(fromWhere=="b"){
//            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);}
        transaction.replace(id, fragment!!)
        transaction.addToBackStack(null)
        transaction.commit()

//            }
//        }
    }

    fun editCode(fragmentEtx: EditText, activity: Activity) {
        fragmentEtx.isEnabled  = true
        fragmentEtx!!.requestFocus()
        val imm3: InputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm3.showSoftInput(fragmentEtx, InputMethodManager.SHOW_IMPLICIT)
    }

    fun getSystemLanguage(): String {
       return Resources.getSystem().getConfiguration().locale.getLanguage();
    }
    fun getAppLanguage(): String {
        val currentLang = Locale.getDefault().language
        return currentLang
    }

    fun showConfirmationDialog(
        generalDataSendedModel: GeneralDataSendedModel
    ) {
//        val dialog = DialogLogin(generalDataSendedModel)
//        dialog.show(generalDataSendedModel.activity!!.supportFragmentManager, "")
//        val intent = Intent(activity, UserCycleActivity::class.java)
//        //intent . setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        activity.startActivity(intent)

    }

    fun showDialoge(activity: Activity,
        title: String?, msg: String?, posTXt: String?, negTXT: String?,
        posAction: DialogInterface.OnClickListener?, negAction: DialogInterface.OnClickListener?
    ): android.app.AlertDialog? {
       return android.app.AlertDialog.Builder(activity)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton(posTXt, posAction)
            .setNegativeButton(negTXT, negAction)
            .show()
    }

    fun shareLink(activity: Activity, url: String) {
        try {
            val bundle = Bundle()
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            bundle.putSerializable(Intent.EXTRA_SUBJECT, "Flashbook")
            var shareMessage = "\nLet me recommend you this application\n\n Android link : \n"
            shareMessage = shareMessage + BASE.toString() + "android/redirect-link/" + url
            //                  +  "\n\n  Iphone link :\n"+BASE_URL2+ "ios/redirect-link/"+url;
//          shareMessage = shareMessage + url + BuildConfig.APPLICATION_ID +"\n\n";
            bundle.putSerializable(Intent.EXTRA_TEXT, shareMessage)
            //            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            shareIntent.putExtras(bundle)
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: java.lang.Exception) {
            //e.toString();
        }
    }

    fun showNoInternetDialogDialog(
        activity: Activity,
        tryAgainOncall: TryAgainOncall,
        type: String?
    ) {
        try {
//                final View view = activity.getLayoutInflater().inflate(R.layout.dialog_restaurant_add_category, null);
//            alertDialog = new AlertDialog.Builder(HomeFragment.this).create();
            val alertDialog: AlertDialog
            alertDialog = AlertDialog.Builder(activity).create()
            alertDialog.setTitle(activity.getString(R.string.warning))
            alertDialog.setMessage(activity.getString(R.string.error_inter_net))
            alertDialog.setCancelable(true)
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, activity.getString(R.string.try_again)
            ) { dialog, which ->
                //                    if(type.equalsIgnoreCase("recycle")) {
                var generalDataSendedModel= GeneralDataSendedModel()
                generalDataSendedModel.type=type
                tryAgainOncall.tryAgainCall(generalDataSendedModel)

                //                    }else {
                //
                //
                //                    }
            }
            alertDialog.setButton(
                AlertDialog.BUTTON_NEGATIVE, activity.getString(R.string.cancel)
            ) { dialog, which -> alertDialog.dismiss() }

//                alertDialog.setView(view);
//            alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
//                @SuppressLint("ResourceAsColor")
//                @Override
//                public void onShow(DialogInterface arg0) {
//                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.pink);
//                    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.pink);
//
//                }
//            });
            alertDialog.show()
        } catch (e: Exception) {
        }
    }

    fun replaceFragmentWithAnimation2(
        getChildFragmentManager: FragmentManager,
        id: Int,
        fragment: Fragment?,
        fromWhere: String
    ) {
//        if (fragment != null) {
        val handler = Handler()
        val transaction = getChildFragmentManager.beginTransaction()
        if (fromWhere === "l") {
//            android.R.anim.slide_in_left
            transaction.setCustomAnimations(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
        if (fromWhere === "r") {
            transaction.setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left
            )
        }
        if (fromWhere === "t") {
            transaction.setCustomAnimations(R.anim.slide_out_down, R.anim.slide_in_down)
        }
        if (fromWhere === "b") {
            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
        }
        //        if(fromWhere=="rr"){
//            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.slide_in_left, R.anim.slide_out_right);}
//        if(fromWhere=="t"){
//            transaction.setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up);}
//        if(fromWhere=="b"){
//            transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);}
        transaction.replace(id, fragment!!)
        transaction.addToBackStack(null)
        val r = Runnable {
            if (!getChildFragmentManager.isDestroyed) {
                transaction.commit()
            }
        }
        handler.postDelayed(r, 500)
        //            }
//        }
    }

    fun setInitRecyclerViewAsGridLayoutManager(
        activity: Activity?,
        recyclerView: RecyclerView,
        gridLayoutManager: GridLayoutManager?,
        numberOfColumns: Int
    ) {
        var gridLayoutManager = gridLayoutManager
        gridLayoutManager = GridLayoutManager(activity, numberOfColumns)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = gridLayoutManager
    }

    fun showCookieMsg(
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

    fun showCalender(context: Context?, title: String?, text_view_data: TextView, data1: DateTxt) {
        val mDatePicker = DatePickerDialog(
            context!!, android.app.AlertDialog.THEME_HOLO_LIGHT,
            { datepicker, selectedYear, selectedMonth, selectedDay -> //                datepicker.setMaxDate();
                val symbols = DecimalFormatSymbols(Locale.US)
                val mFormat = DecimalFormat("00", symbols)
                val data =
                    selectedYear.toString() + "-" + mFormat.format(java.lang.Double.valueOf((selectedMonth + 1).toDouble())) + "-" + mFormat.format(
                        java.lang.Double.valueOf(selectedDay.toDouble())
                    )
                data1.date_txt = data
                data1.day = mFormat.format(java.lang.Double.valueOf(selectedDay.toDouble()))
                data1.month =
                    mFormat.format(java.lang.Double.valueOf((selectedMonth + 1).toDouble()))
                data1.year = selectedYear.toString()
                text_view_data.text = data
            }, data1.year.toInt(), data1.month.toInt() - 1, data1.day.toInt()
        )
        //        mDatePicker.getDatePicker().setMinDate(minDate.getTimeInMillis());
//        mDatePicker.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        mDatePicker.setTitle(title)
        mDatePicker.show()
    }

    fun openGoogleCroomLink(generalDataSendedModel: GeneralDataSendedModel) {
        try {
            val i = Intent("android.intent.action.MAIN")
            i.component =
                ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main")
            i.addCategory("android.intent.category.LAUNCHER")
            i.data = Uri.parse(generalDataSendedModel.googleUrl)
            generalDataSendedModel.activity?.startActivity(i)
        } catch (e: ActivityNotFoundException) {
            // Chrome is not installed
            Log.i(ContentValues.TAG + "appLink2", e.message.toString())
            //            showToast(activity, e.getMessage());
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(generalDataSendedModel.googleUrl))
            generalDataSendedModel.activity?.startActivity(i)
        }
    }



    @Throws(IOException::class)
    fun getFilePathFromUri(uri: Uri?, activity: Activity): Uri? {
        val fileName: String? = getFileName(uri, activity)
        val file = File(activity.externalCacheDir, fileName)
        file.createNewFile()
        FileOutputStream(file).use { outputStream ->
            activity.contentResolver.openInputStream(
                uri!!
            ).use { inputStream ->
                IOUtils.copyStream(
                    inputStream!!,
                    outputStream
                ) //Simply reads input to output stream
                outputStream.flush()
            }
        }
        return Uri.fromFile(file)
    }

    fun getFileName(uri: Uri?, activity: Activity?): String? {
        var fileName: String? = getFileNameFromCursor(uri, activity!!)
        if (fileName == null) {
            val fileExtension: String? = getFileExtension(uri, activity)
            fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""
        } else if (!fileName.contains(".")) {
            val fileExtension: String? = getFileExtension(uri, activity)
            fileName = "$fileName.$fileExtension"
        }
        return fileName
    }

    fun getFileNameFromCursor(uri: Uri?, activity: Activity): String? {
        val fileCursor = activity.contentResolver.query(
            uri!!,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )
        var fileName: String? = null
        if (fileCursor != null && fileCursor.moveToFirst()) {
            val cIndex = fileCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cIndex != -1) {
                fileName = fileCursor.getString(cIndex)
            }
        }
        return fileName
    }

    fun getFileExtension(uri: Uri?, activity: Activity): String? {
        val fileType = activity.contentResolver.getType(uri!!)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    fun convertFileToMultipart(
        pathImageFile: String?,
        Key: String?,
        activity: FragmentActivity?
    ): MultipartBody.Part? {
        return if (pathImageFile != null) {
            val file = File(pathImageFile)
            val reqFileselect = RequestBody.create("*/*".toMediaTypeOrNull(), file)

            //            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("photo", file.getName(), reqFileselect);

            //            RequestBody reqFileselect = RequestBody.create(MediaType.parse("image/*"), file);
            //            MultipartBody.Part Imagebody = MultipartBody.Part.createFormData("photo3", file.getName(), reqFileselect);
            //            showToast(activity, String.valueOf(Imagebody));

            MultipartBody.Part.createFormData(Key!!, file.name, reqFileselect)
        } else {
            null
        }
    }

    //    public static MultipartBody.Part convertFileToMultipart(String pathImageFile, String Key) {
    //        if (pathImageFile != null) {
    //            File file = new File(pathImageFile);
    ////            RequestBody reqFileselect = RequestBody.create(MediaType.parse("image/*"), file);
    //            RequestBody reqFileselect = RequestBody.create(MediaType.parse("*/*"), file);
    ////            MultipartBody.Part Imagebody = MultipartBody.Part.createFormData(Key, file.getName(), reqFileselect);
    //            MultipartBody.Part Imagebody = MultipartBody.Part.createFormData(Key, file.getName(), reqFileselect);
    //            return Imagebody;
    //        } else {
    //            return null;
    //        }
    //    }
    //    public static MultipartBody.Part prepareFilePart(String partName, Uri images){
    //
    //        List<MultipartBody.Part> listOfImages = new ArrayList<>();
    //
    //
    //        ArrayList<Object> upFileList;
    //        for (int i = 0; i < listOfImages.size(); i++){
    ////            listOfImages.add(prepareFilePart("image[$i]", images[i]));
    ////            parts.add(prepareFilePart("my_file["+i+"]", (Uri) upFileList.get(i)));
    //        }
    //
    //        File file = new File(partName);
    ////
    //        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
    //            return MultipartBody.Part.createFormData(partName, file.getName(),requestBody);
    //        }
    fun convertToRequestBody(part: String): RequestBody? {
        return try {
            if (part != "") {
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), part)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    fun convertDateString(date: String?): Date? {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd")
            format.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    //    public static DateTxt convertStringToDateTxtModel(String date) {
    //        try {
    //            Date date1 = convertDateString(date);
    //            String day = (String) DateFormat.format("dd", date1); // 20
    //            String monthNumber = (String) DateFormat.format("MM", date1); // 06
    //            String year = (String) DateFormat.format("yyyy", date1); // 2013
    //
    //            return new DateTxt(day, monthNumber, year, date);
    //
    //        } catch (Exception e) {
    //            return null;
    //        }
    //    }
    fun onLoadImageFromUrl(imageView: ImageView?, URl: String?, context: Context?) {
//        Glide.with(context)
//                .load(URl)
//                .into(imageView);

//        Glide.with(context).load(URl)
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
//                .into(imageView);
//        Glide.with(imageView.getContext())
//                .setDefaultRequestOptions(new RequestOptions()
//                        .circleCrop())
//                .load(imageURL)
//                .placeholder(R.drawable.loading)
//                .into(imageView);
    }

    fun onLoadImageFromUrl2(imageView: ImageView?, URl: String?, context: Context?) {
//        Glide.with(context)
//                .load(URl)
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
//                .into(imageView);
    }

    fun onLoadCirImageFromUrl(cirImageView: CircleImageView?, URl: String?, context: Context?) {
        Glide.with(context!!)
            .load(URl)
            .into(cirImageView!!)
    }

    fun onLoadCirImageFromUrl2(imageView: ImageView?, URl: String?, context: Context?) {
        Glide.with(context!!)
            .load(URl) //.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .into(imageView!!)
    }

    fun showProgressDialog(activity: Activity?, title: String?) {
        try {
            progressDialog = ProgressDialog(activity)
            progressDialog!!.setMessage(title)
            progressDialog!!.isIndeterminate = false
            progressDialog!!.setCancelable(false)
            progressDialog!!.show()
        } catch (e: Exception) {
        }
    }

    fun dismissProgressDialog() {
        try {
            progressDialog!!.dismiss()
        } catch (e: Exception) {
        }
    }
    fun filterInEditText(
        textInputEditText: TextInputEditText,
        notContainTxtMessage: Int,
        notContainInTxt: String,
        generalDataSendedModel: GeneralDataSendedModel
    ) {
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!notContainInTxt.contains(source[i]))
                {
                    // إذا لم تكن الحرف إنجليزيًا أو الرقم إنجليزيًا، فلا تقبله
                    showToast(generalDataSendedModel.activity,generalDataSendedModel.activity!!.getString(notContainTxtMessage))
                }
            }
            null
        }

        // إضافة الـ InputFilter إلى TextInputEditText
        textInputEditText.filters = arrayOf(filter)
    }
    //    public static void openGallery(Activity activity) {
    //        new ImagePicker.Builder(activity)
    //                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
    //                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
    //                .directory(ImagePicker.Directory.DEFAULT)
    ////                .extension(ImagePicker.Extension.PNG)
    ////                .scale(600, 600)
    //                .allowMultipleImages(false)
    //                .enableDebuggingMode(true)
    //                .build();
    //    }
    fun openGalleryAlpom(  context:Context?,   mAlbumFiles:ArrayList<AlbumFile?>?,
    action:Action<ArrayList<AlbumFile?>?>?,   count:Int)
    {
        Album.initialize(
            AlbumConfig.newBuilder(context)
                .setAlbumLoader(MediaLoader()).build()
        )
        Album.image(context) // Image selection.
            .multipleChoice()
            .camera(true)
            .columnCount(3)
            .selectCount(count)
            .checkedList(mAlbumFiles)
            .onResult(action)
            .onCancel(object : Action<String> {
                override fun onAction(result: String) {}
            }).start()
    }

    //    public static void openGallery(Activity activity) {
    //        new ImagePicker.Builder(activity)
    //                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
    //                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
    //                .directory(ImagePicker.Directory.DEFAULT)
    ////                .extension(ImagePicker.Extension.PNG)
    ////                .scale(600, 600)
    //                .allowMultipleImages(false)
    //                .enableDebuggingMode(true)
    //                .build();
    //    }

    fun disappearKeypad(activity: Activity, v: View?) {
        try {
            if (v != null) {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        } catch (e: Exception) {
        }
    }

    fun changeLang(context: Context, lang: String?) {
        val res = context.resources
        // Change locale settings in the app.
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(lang)) // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm)
    }

    fun htmlReader(textView: TextView, s: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(s, Html.FROM_HTML_MODE_COMPACT)
        } else {
            textView.text = Html.fromHtml(s)
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
        val layout = inflater.inflate(
            layout_id,
            activity.findViewById<View>(R.id.toast_layout_root) as ViewGroup
        )

//        TextView text = layout.findViewById(R.id.toast_txt);
//        text.setText(ToastTitle);
        val toast = Toast(activity)
        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.BOTTOM, 0, 100)
        toast.duration = Toast.LENGTH_LONG
        toast.setView(layout)
        toast.show()
    }

    fun onPermission(activity: Activity?) {
        val perms = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE
        )
        ActivityCompat.requestPermissions(
            activity!!,
            perms,
            100
        )
    }

    fun showToast(activity: Activity?, message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(activity: Activity?, message: String?) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    class TimePickerFragment(private val editText: EditText) : DialogFragment(),
        TimePickerDialog.OnTimeSetListener {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker
            val c = Calendar.getInstance()
            val hour = c[Calendar.HOUR_OF_DAY]
            val minute = c[Calendar.MINUTE]

            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(
                activity, this, hour, minute,
                DateFormat.is24HourFormat(activity)
            )
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            // Do something with the time chosen by the user
            editText.setText("$hourOfDay:$minute")
        }

        companion object {
            fun bitmapDescriptorFromVector(context: Context?, vectorResId: Int): BitmapDescriptor {
                val vectorDrawable = ContextCompat.getDrawable(context!!, vectorResId)
                vectorDrawable!!.setBounds(
                    0,
                    0,
                    vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight
                )
                val bitmap = Bitmap.createBitmap(
                    vectorDrawable.intrinsicWidth,
                    vectorDrawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                //            Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);
                val canvas = Canvas(bitmap)
                vectorDrawable.draw(canvas)
                return BitmapDescriptorFactory.fromBitmap(bitmap)
            }
        }
    }

    class DatePickerFragment(private val editText: EditText) : DialogFragment(),
        DatePickerDialog.OnDateSetListener {
        private var minDate: Calendar? = null
        var year = 0
        var month = 0
        var day = 0
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            year = c[Calendar.YEAR]
            month = c[Calendar.MONTH]
            day = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(requireActivity(), this, year, month, day)
            // Create a new instance of DatePickerDialog and return it
            minDate = Calendar.getInstance()
            minDate!!.set(year, month - 1, day) //Year,Mounth -1,Day
            datePickerDialog.datePicker.minDate = minDate!!.getTimeInMillis()
            return datePickerDialog
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            // Do something with the date chosen by the user
            val symbols = DecimalFormatSymbols(Locale.US)
            val mFormat = DecimalFormat("00", symbols)
            val data =
                year.toString() + "-" + mFormat.format(java.lang.Double.valueOf((month + 1).toDouble())) + "-" + mFormat.format(
                    java.lang.Double.valueOf(day.toDouble())
                )
            editText.setText(data)
        }

        companion object {
            //        public static void setLocale(Activity activity, String languageCode, int number) {
            //            Locale locale = new Locale(languageCode);
            //            Locale.setDefault(locale);
            //            Configuration config = new Configuration();
            //            config.locale = locale;
            //            activity.getResources().updateConfiguration(config, activity.getResources().getDisplayMetrics());
            //            SaveLanguage(activity,"LANGUAGE",languageCode);
            //
            //            if(number==1) {
            //                Intent intent = new Intent(activity, UserCycleActivity.class);
            //                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //                activity.startActivity(intent);
            //            }
            //            if(number==2) {
            //                Intent intent2 = new Intent(activity, HomeCycleActivity.class);
            //                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //                activity.startActivity(intent2);
            //            }
            //        }

            fun setLocale(activity: Activity, languageCode: String?, number: Int, type: String) {
                if (!activity.isFinishing) {
                    val locale = Locale(languageCode)
                    Locale.setDefault(locale)
                    val config = Configuration()
                    config.locale = locale
                    activity.resources.updateConfiguration(
                        config,
                        activity.resources.displayMetrics
                    )
                    SaveLanguage(activity, "LANGUAGE", languageCode)
                    if (number == 1) {
                        val intent = Intent(activity, HomeCycleActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        activity.startActivity(intent)
                    }
                    if (number == 2) {
                        val intent2 = Intent(activity, HomeCycleActivity::class.java)
                        if (type !== "") {
                            intent2.putExtra("type", type)
                        }
                        //                showToast(activity,"yes ");
//                    if(userData!=null){
//                        intent2.putExtra("UserData", (Serializable) userData);
//                    }
//                    if(ownerStatusData!=null){
//                        intent2.putExtra("OwnerStatusData", ownerStatusData);
//                    }
                        intent2.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        activity.startActivity(intent2)
                    }
                    if (number == 3) {
                        val intent = Intent(activity, SplashCycleActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        activity.startActivity(intent)
                    }
                    activity.finish()
                }
            }
            fun setLocale(activity: Activity, languageCode: String?, number: Int) {
                val locale = Locale(languageCode)
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
                activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
                SharedPreferencesManger.SaveLanguage(activity, "LANGUAGE", languageCode)
                if (number == 1) {
                    val intent = Intent(activity, HomeCycleActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    activity.startActivity(intent)
                }
                if (number == 2) {
                    val intent2 = Intent(activity, HomeCycleActivity::class.java)
                    intent2.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    activity.startActivity(intent2)
                }
            }

            fun turnGPSOn(activity: Activity) {
                val provider = Settings.Secure.getString(
                    activity.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED
                )
                if (!provider.contains("gps")) { //if gps is disabled
                    val poke = Intent()
                    poke.setClassName(
                        "com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider"
                    )
                    poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
                    poke.data = Uri.parse("3")
                    activity.sendBroadcast(poke)
                }
            }

            fun turnGPSOff(activity: Activity) {
                val provider = Settings.Secure.getString(
                    activity.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED
                )
                if (provider.contains("gps")) { //if gps is enabled
                    val poke = Intent()
                    poke.setClassName(
                        "com.android.settings",
                        "com.android.settings.widget.SettingsAppWidgetProvider"
                    )
                    poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
                    poke.data = Uri.parse("3")
                    activity.sendBroadcast(poke)
                }
            }

            fun canToggleGPS(activity: Activity): Boolean {
                val pacman: PackageManager = activity.getPackageManager()
                var pacInfo: PackageInfo? = null
                pacInfo = try {
                    pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS)
                } catch (e: PackageManager.NameNotFoundException) {
                    return false //package not found
                }
                if (pacInfo != null) {
                    for (actInfo in pacInfo.receivers) {
                        //test if recevier is exported. if so, we can toggle GPS.
                        if (actInfo.name == "com.android.settings.widget.SettingsAppWidgetProvider" && actInfo.exported) {
                            return true
                        }
                    }
                }
                return false //default
            }
        }
    }
}