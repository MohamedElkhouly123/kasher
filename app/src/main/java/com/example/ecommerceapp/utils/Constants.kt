package com.example.ecommerceapp.utils

import android.view.Gravity

object Constants {
    const val App = 1
    var BASE_URL_CONSTANT: String? = null
    var Nav_gravity = 0
    var ELM_BASE = "https://3lm.dr-mideo.co/"
    var temp_courses_image = 0
    var temp_uni_image = 0
    var login_top_image = 0
    var FaceBookLink: String? = null
    var TelegramLink: String? = null
    var youtubeLink: String? = null
    var playStoreLink: String? = null
    var isVideoBack = true
    var ImageSplash = 0
    var AUTH_TOKEN = ""
    var Correct = "0"
    var Wrong = "0"
    var USER_NAME = "null"
    var signUp = 0
    var UniverstyId = -1
    var currentQualty = 2994000
    var currentSpeed = 1f
    fun BaseConfig() {
        if (App == 1) {
            BASE_URL_CONSTANT = "https://dr-mideo.co/"
            Nav_gravity = Gravity.LEFT
            //            temp_courses_image = R.drawable.elm_libarary2;
//            temp_uni_image = R.drawable.elm_libarary2;
//            login_top_image = R.drawable.logintop_mideo;
            FaceBookLink = "https://www.facebook.com/Mideo.medical/?referrer=whatsapp"
            TelegramLink = "https://t.me/Mideo_App"
            youtubeLink = "https://www.youtube.com/channel/UCF8YxSqBT9LVk05u7qY2law"
            playStoreLink =
                "https://play.google.com/store/apps/details?id=com.codeProeLearning.codeProee3lm"
            //            ImageSplash = R.mipmap.ic_mideo_luncher_round;
        } else if (App == 2) {
            BASE_URL_CONSTANT = "https://eideo.dr-mideo.co/"
            Nav_gravity = Gravity.RIGHT
            FaceBookLink = "https://www.facebook.com/Eideo.EV"
            TelegramLink = "https://t.me/joinchat/KTrfiVHhpfe_1xhP60kwqg"
            youtubeLink = "https://www.youtube.com/channel/UCF8YxSqBT9LVk05u7qY2law"
            playStoreLink =
                "https://play.google.com/store/apps/details?id=com.codeProeLearning.codeProee3lm"
            //            ImageSplash = R.mipmap.ic_launcher_eideo_round;
        } else {
            BASE_URL_CONSTANT = "https://3lm.dr-mideo.co/"
            Nav_gravity = Gravity.RIGHT
            //            login_top_image = R.mipmap.ic_launcher_round;
            FaceBookLink = "https://www.facebook.com/3lm.app.eg"
            TelegramLink = "https://t.me/App_3lm"
            youtubeLink = "https://www.youtube.com/channel/UCF8YxSqBT9LVk05u7qY2law"
            playStoreLink =
                "https://play.google.com/store/apps/details?id=com.codeProeLearning.codeProee3lm"
            //            ImageSplash = R.mipmap.ic_launcher_round;
        }
    }
}