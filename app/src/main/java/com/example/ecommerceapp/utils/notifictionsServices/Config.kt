package com.example.ecommerceapp.utils.notifictionsServices

object Config {
    // global topic to receive app wide push notifications
    const val TOPIC_GLOBAL = "global"

    // broadcast receiver intent filters
    const val REGISTRATION_COMPLETE = "registrationComplete"
    const val PUSH_NOTIFICATION = "pushNotification"

    // id to handle the notification in the notification tray
    const val NOTIFICATION_ID = 100
    const val NOTIFICATION_ID_BIG_IMAGE = 101
    const val SHARED_PREF = "ah_firebase"
    const val BASE: String =  "api/"
    const val reset: String =  "api/auth/forgot-pwd"
    const val BASE2 = "https://rakhis.codlop.com/api/"
    val BASE_cook: String=""

}