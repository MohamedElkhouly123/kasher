package com.example.ecommerceapp.utils.notifictionsServices

import android.content.Context
import android.util.Log
import androidx.work.*

class MyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        Log.d(TAG, "Performing long running task in scheduled job")
        // TODO(developer): add long running task here.
        val name = inputData.getString("NAME_KEY")
        val age = inputData.getInt("AGE_KEY", -1)
        val result = name + age
        val output = Data.Builder()
            .putString("RESULT_KEY", result)
            .build()
        return Result.success(output)
        //        return Result.success();
//        Result.success: في حالة نجاح واتمام الشفرة البرمجية لعملنا.
//                Result.retry: في الفشل وهنا نطلب من نظام الاندرويد ان يقوم بإعادة المحاولة.
//                Result.failure: في الفشل وهنا نطلب من نظام الاندرويد ان لايقوم بإعادة المحاولة.
    }

    private fun scheduleJob() { // to do task by work manger in background
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]

//        Data myData = new Data.Builder()  //send data to worker
//                .putString("NAME_KEY", "Mohammad")
//                .putInt("AGE_KEY", 14)
//                .build();
//
//        Constraints constraints = new Constraints.Builder()  //work لما الجهاز يكون مفتوح وفى نت
//                .setRequiresDeviceIdle(true)
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build();
//  لتنفيذه مره واحده
//        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .setInputData(myData)
//                .setConstraints(constraints)
//                .addTag("MY_WORK_MANAGER_TAG_ONE_TIME")
//                .build();
//   لتنفيذه اكثر من مره كل 12 ساعه
//        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 12, TimeUnit.HOURS)
//                .setInputData(myData)
//                .setConstraints(constraints)
//                .addTag("MY_WORK_MANAGER_PERIODIC_TAG")
//                .build();

//        الان نستطيع ارسال العمل الى النظام من خلال الـ Work Manager حتى يقوم بجدولته كالتالي:
//
//        WorkManager.getInstance().enqueue(oneTimeWorkRequest);

//        لإلغاء العمل نحتاج الى الحصول على الـ Id الخاص به, ثم الغائه بإستخدام الدالة cancelWorkById وفق الطريقة التالية:
//
//        UUID oneTimeWorkRequestId = oneTimeWorkRequest.getId();
//        WorkManager.getInstance().cancelWorkById(oneTimeWorkRequestId);

//        نستطيع ايضاً الغاء الاعمال كلها, او بعضها بإستخدام الـ Tag كالتالي:
//
//        WorkManager.getInstance().cancelAllWork();
//        WorkManager.getInstance().cancelAllWorkByTag("MY_WORK_MANAGER_TAG_ONE_TIME");

//        بعد انجاز عملنا نستطيع استقبال النتيجة من خلال عنصر الـ LiveData (ماهي الـ LiveData؟) ,كالتالي:
//
//        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequestId).observe(this, new Observer<WorkInfo>() {
//            @Override
//            public void onChanged(@Nullable WorkInfo workInfo) {
//                if (workInfo != null && workInfo.getState().isFinished()){
//                    String result = workInfo.getOutputData().getString("RESULT");
//                    Log.d(TAG, "onChanged: " + result);
//                }
//            }
//        });

//        اذا كان يوجد لدينا اعمال نريد تشغيلهم بالتسلسل نستطيع جدولتهم هكذا:
//
//        WorkManager.getInstance()
//                .beginWith(downLoadImageWorkRequest)
//                .then(saveDownloadedImageToDatabaseWorkRequest)
//                .then(cleanTempWorkRequest)
//                .enqueue();

//        اذا اردنا تشغيل بعضهم متوازي Parallel فيكون الوضع هكذا:
//
//        WorkManager.getInstance()
//                .beginWith(Arrays.asList(downloadImagesWorkRequest, downloadTextsWorkRequest, downloadLinksWorkRequest))    // Runs in Parallel.
//                .then(cleanTempWorkRequest)
//                .enqueue();
    }

    companion object {
        private const val TAG = "MyWorker"
    }
}