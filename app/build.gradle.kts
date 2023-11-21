
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

}

android {
    namespace = "com.example.ecommerceapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.shopping.ecommerceapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled =true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    lint {
        baseline = file("lint-baseline.xml")
    }
    lintOptions {
        isAbortOnError= false
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable =false
//            useProguard true
            isShrinkResources =true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
//        debug{
//            isMinifyEnabled = true
//            isDebuggable =false
////            useProguard true
//            isShrinkResources =true
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}



val butter_version: String= "10.2.3"
val recycler_version: String= "1.3.0"
val room_version: String= "1.1.1"
val multidex_version: String= "2.0.1"
val lottieVersion: String= "6.1.0"
val room_version2: String= "2.6.0"

dependencies {


//    implementation ("tk.zielony:carbon:0.16.0.1")
    implementation ("androidx.core:core-ktx:1.10.1")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("junit:junit:4.13.2")
    implementation ("androidx.test.ext:junit:1.1.5")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("junit:junit:4.13.2")
    implementation ("androidx.test.ext:junit:1.1.5")

    // Espresso dependencies
    implementation ("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.test.espresso:espresso-intents:3.5.1")
    implementation ("androidx.test:core-ktx:1.5.0")
    implementation ("androidx.test:runner:1.5.2")
    implementation ("androidx.test:rules:1.5.0")

    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("org.aviran.cookiebar2:cookiebar2:1.1.4")
    implementation ("com.intuit.sdp:sdp-android:1.0.6")
    implementation ("com.intuit.ssp:ssp-android:1.0.6")
    /////////////////////////////////////////////////////////////////////////////////
    implementation ("androidx.navigation:navigation-runtime-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation ("com.google.android.play:app-update-ktx:+")
    implementation ("jp.wasabeef:blurry:4.0.1")
    implementation ("com.github.zhpanvip:viewpagerindicator:1.2.2")
    implementation ("com.github.devlight.shadowlayout:library:1.0.2")
    implementation ("com.hbb20:ccp:2.7.0")
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.0.0")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0"){ isTransitive = false }
    implementation ("com.google.zxing:core:3.3.0")
    implementation ("com.android.support:multidex:1.0.3")
    implementation ("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")
    implementation ("com.github.lecho:hellocharts-library:1.5.8@aar")

    //Retrofit Dependenciesciri
    //    retrofit and okhttp
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.8.9")
    //Room
    implementation("androidx.room:room-runtime:$room_version2")
    annotationProcessor("androidx.room:room-compiler:$room_version2")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version2")
//    // To use Kotlin Symbol Processing (KSP)
//    ksp("androidx.room:room-compiler:$room_version2")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version2")
    //     ultraviewpager
    implementation ("com.alibaba.android:ultraviewpager:1.0.7.7@aar") {
        isTransitive = true
    }
//    implementation 'com.github.AhmedTeleb96:3d_buttons:1.0.0'
    implementation("com.tbuonomo:dotsindicator:4.3")
    implementation("com.github.smarteist:autoimageslider:1.4.0")
    implementation("com.airbnb.android:lottie:$lottieVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
// coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.4")
    //Circle image view
    implementation("de.hdodenhof:circleimageview:3.0.1")
    implementation("com.wdullaer:materialdatetimepicker:4.2.3")
    // glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    "kapt"("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.github.2coffees1team:GlideToVectorYou:v2.0.0")
    //Transparent statusBar
    implementation("com.jaeger.statusbarutil:library:1.5.1")
    //    swiper eveal layout
    implementation("com.chauthai.swipereveallayout:swipe-reveal-layout:1.4.1")
    //RecyclerView
    implementation("androidx.recyclerview:recyclerview:$recycler_version")
    //    google play-services    مهم
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:20.0.0")
    implementation("com.google.android.libraries.places:places:3.0.0")
    implementation("com.google.android.gms:play-services-places:17.0.0")
    implementation("com.google.maps.android:maps-utils-ktx:3.4.0")
    implementation("com.google.maps.android:android-maps-utils:3.4.0")
    implementation("com.google.maps:google-maps-services:2.2.0")

    implementation("androidx.work:work-runtime:2.8.0")
    implementation("androidx.multidex:multidex:$multidex_version")
    implementation("com.yanzhenjie:album:2.1.3")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.19")
    //////////////////////////
    implementation("com.borjabravo:readmoretextview:2.1.0")
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("androidx.fragment:fragment-ktx:1.3.3")

    implementation("com.github.hadiidbouk:ChartProgressBar-Android:2.0.6")
    implementation("com.github.blackfizz:eazegraph:1.2.5l@aar")
    implementation("com.nineoldandroids:library:2.4.0")
    implementation("com.jjoe64:graphview:4.2.2")
    implementation("com.github.thijsk:TouchImageView:1.3.1")
//    picasso
    implementation("com.squareup.picasso:picasso:2.71828")
    //////////////////////////////////kotlen///////////////////////////////
    implementation("com.github.ybq:Android-SpinKit:1.4.0")
    implementation("com.github.florent37:singledateandtimepicker:2.2.8")
    implementation("com.github.takusemba:spotlight:1.0.1")
    //    google auth
    implementation("com.google.android.gms:play-services-auth:20.6.0")
//    implementation ("com.google.android.gms:play-services-auth:20.7.0")
//    implementation("com.facebook.android:facebook-login:latest.release")
//    implementation("com.facebook.android:facebook-android-sdk:[8,9)")

    implementation("com.google.firebase:firebase-database:20.0.5")
    implementation("com.google.firebase:firebase-storage:20.0.1")
    implementation("com.google.firebase:firebase-firestore:24.2.1")
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.firebase:firebase-messaging-directboot:20.2.0")

    // Import the Firebase BoM
    implementation("com.google.firebase:firebase-messaging-ktx:23.0.2")
    implementation(platform("com.google.firebase:firebase-bom:31.1.0"))
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.github.barteksc:android-pdf-viewer:2.8.2")
    implementation("com.github.prolificinteractive:material-calendarview:2.0.0")
    implementation("com.github.msarhan:ummalqura-calendar:1.1.9")
}
