# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#   new
-keep class com.example.ecommerceapp.** { *; }
-keep class com.example.package.** { *; }
-keepattributes !LocalVariableTable,!LocalVariableTypeTable
-dontwarn com.dropbox.**
-keep class com.dropbox.** { *; }

-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile
#-flattenpackagehierarchy
-flattenpackagehierarchy 'mxx'
-repackageclasses 'mxx'
-allowaccessmodification
#carbon
-dontwarn carbon.BR
-dontwarn carbon.internal**
-dontwarn java.lang.invoke**

-dontwarn android.databinding.**
-keep class android.databinding.** { *; }
# test thoroughly if you go this route.
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizations !class/merging/vertical*,!class/merging/horizontal*,!code/simplification/arithmetic,!field/*,!code/allocation/variable

-optimizationpasses 5
-allowaccessmodification
-dontpreverify
# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
#-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
#-keepclasseswithmembernames class * {
#    native <methods>;
#}

## keep setters in Views so that animations can still work.
## see http://proguard.sourceforge.net/manual/examples.html#beans
#-keepclassmembers public class * extends android.view.View {
#   void set*(***);
#   *** get*();
#}
## We want to keep methods in Activity that could be used in the XML attribute onClick
#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}
## For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#-keepclassmembers class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator CREATOR;
#}
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**
#-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
#  public static void checkExpressionValueIsNotNull(java.lang.Object, java.lang.String);
#  public static void checkFieldIsNotNull(java.lang.Object, java.lang.String);
#  public static void checkFieldIsNotNull(java.lang.Object, java.lang.String, java.lang.String);
#  public static void checkNotNull(java.lang.Object);
#  public static void checkNotNull(java.lang.Object, java.lang.String);
#  public static void checkNotNullExpressionValue(java.lang.Object, java.lang.String);
#  public static void checkNotNullParameter(java.lang.Object, java.lang.String);
#  public static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
#  public static void checkReturnedValueIsNotNull(java.lang.Object, java.lang.String);
#  public static void checkReturnedValueIsNotNull(java.lang.Object, java.lang.String, java.lang.String);
#  public static void throwUninitializedPropertyAccessException(java.lang.String);
#}

# new

# Enable proguard with Cordova
-keep class org.apache.cordova.** { *; }
-keep public class * extends org.apache.cordova.CordovaPlugin

-keep class com.google.android.gms.dynamite.DynamiteModule$DynamiteLoaderClassLoader { java.lang.ClassLoader sClassLoader; }

-keep class org.apache.cordova.CordovaBridge { org.apache.cordova.PluginManager pluginManager; }
-keep class org.apache.cordova.CordovaInterfaceImpl { org.apache.cordova.PluginManager pluginManager; }
-keep class org.apache.cordova.CordovaResourceApi { org.apache.cordova.PluginManager pluginManager; }
-keep class org.apache.cordova.CordovaWebViewImpl { org.apache.cordova.PluginManager pluginManager; }
-keep class org.apache.cordova.ResumeCallback { org.apache.cordova.PluginManager pluginManager; }
-keep class org.apache.cordova.engine.SystemWebViewEngine { org.apache.cordova.PluginManager pluginManager; }

-dontnote org.apache.harmony.xnet.provider.jsse.NativeCrypto
-dontnote sun.misc.Unsafe

-keep class com.worklight.androidgap.push.** { *; }
-keep class com.worklight.wlclient.push.** { *; }
# Enable proguard with Google libs
-keep class com.google.** { *; }
-dontwarn com.google.common.**
-dontwarn com.google.ads.**
-dontwarn com.google.android.gms.**

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** w(...);
    public static *** i(...);
    public static *** e(...);

}
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-keep class androidx.lifecycle.** { *; }

# Android data binding
-dontwarn androidx.databinding.**
-keep class androidx.databinding.** { *; }

# Android data binding
-dontwarn com.xxxx.xx.databinding.**
-keep class com.xxxx.xx.databinding.** { *; }
-keepclassmembers class com.xxxx.xx.databinding.** { *; }

-keep class com.xxxx.xx.BindingHelpers.** { *; }
-keepclassmembers class com.xxxx.xx.BindingHelpers.** { *; }
#-keep class com.xxxx.xx.DataBinderMapperImpl { *; }

-keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }
 -keepclasseswithmembernames class * {
        @butterknife.* <fields>;
  }
 -keepclasseswithmembernames class * {
        @butterknife.* <methods>;
  }

#AndroidX View Model

-keep class * extends androidx.lifecycle.ViewModel { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel { *; }

#AndroidX AttributeSet
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#AndroidX Annotation
-keepattributes *Annotation*

#AndroidX Lifecycle
-keepclassmembers enum androidx.lifecycle.Lifecycle$Event {
    <fields>;
}
-keep !interface * implements androidx.lifecycle.LifecycleObserver {
}
-keep class * implements androidx.lifecycle.GeneratedAdapter {
    <init>(...);
}
-keepclassmembers class ** {
    @androidx.lifecycle.OnLifecycleEvent *;
}


#AndroidX Arch
-keepclassmembers class android.arch.** { *; }
-keep class android.arch.** { *; }
-dontwarn android.arch.**

# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

-keep class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keep class kotlinx.coroutines.CoroutineExceptionHandler {}
-keep class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory {}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {
    volatile <fields>;


}






# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
#################
 -keepattributes Signature
 -keepattributes Exceptions
 -dontwarn okio.**
##### Moshi for JSON #####
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}

#-keep @com.squareup.moshi.JsonQualifier interface *

#-keepclassmembers class kotlin.Metadata {
#    public <methods>;
#}

##### Glide #####
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
#-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}





# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit



##### OkHttp, Retrofit #####
-dontwarn okhttp3.**
-dontwarn com.scottyab.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keepattributes *annotation*
 -keepattributes Signature
 -keepattributes *Annotation*
 -keep class okhttp3.** { *; }
 -keep interface okhttp3.** { *; }
# -keep class sun.misc.Unsafe { *; }
 -dontwarn java.nio.file.*
 -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# -keep class sun.misc.Unsafe { *; }
 -keep class com.google.gson.stream.** { *; }

#Code obfuscation
 -keepclassmembers class com.yourname.models** { <fields>; }


# Retrofit
-dontwarn retrofit2.**
-dontwarn org.codehaus.mojo.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes *Annotation*
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

-keepattributes EnclosingMethod

-keepclasseswithmembers class * {
    @retrofit2.* <methods>;
}

-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform.IOS.MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform.Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

#-keep class com.elmasry.hoba.authentication.login.*
#-keep class com.elmasry.hoba.authentication.register.networking.RegisterResponse
#-keep class com.elmasry.hoba.authentication.login.networking.LoginResponse
#-keep class com.elmasry.hoba.core.home.networking.SectionsItem
#-keep class com.elmasry.hoba.core.courses.model.CoursesItem
#-keep class com.elmasry.hoba.core.videos.model.VideosItem
#-keep class com.elmasry.hoba.core.quizes.Questions.QuizesItem
#-keep class com.elmasry.hoba.splash.versionHandler.VersionResponse
#-keep class com.elmasry.hoba.splash.versionHandler.Version



##---------------Begin: proguard configuration for Gson  -----------------------------------------------------------------------------------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes-----------------------------------------------------
-dontwarn sun.misc.**
-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
 -keep class mypersonalclass.data.model.** { *; }

 ##---------------Begin: proguard configuration for Gson  ----------
 # Gson uses generic type information stored in a class file when working with fields. Proguard
 # removes such information by default, so configure it to keep all of it.
 -keepattributes Signature

 # For using GSON @Expose annotation
 -keepattributes *Annotation*

 # Gson specific classes
 -dontwarn sun.misc.**
 #-keep class com.google.gson.stream.** { *; }
 # Application classes that will be serialized/deserialized over Gson
 -keep class com.google.gson.examples.android.model.** { <fields>; }
 # Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
 # JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
 -keep class * extends com.google.gson.TypeAdapter
 -keep class * implements com.google.gson.TypeAdapterFactory
 -keep class * implements com.google.gson.JsonSerializer
 -keep class * implements com.google.gson.JsonDeserializer
 # Prevent R8 from leaving Data object members always null
 -keepclassmembers,allowobfuscation class * {
   @com.google.gson.annotations.SerializedName <fields>;
 }

 # Retain service method parameters when optimizing.
 -keepclassmembers,allowshrinking,allowobfuscation,allowoptimization interface * {
     @retrofit2.http.* <methods>;
 }
 # Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
 -keep,allowobfuscation,allowshrinking ,allowoptimization class com.google.gson.reflect.TypeToken
 -keep,allowobfuscation,allowshrinking,allowoptimization  class * extends com.google.gson.reflect.TypeToken

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking,allowoptimization interface retrofit2.Call
-keep,allowobfuscation,allowshrinking,allowoptimization class retrofit2.Response

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking,allowoptimization class kotlin.coroutines.Continuation


-keep,allowobfuscation,allowshrinking class com.example.SomeClass


-dontwarn org.apache.commons.**
-dontwarn com.google.**
-dontwarn com.j256.ormlite**
-dontwarn org.apache.http**

-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }

-keepattributes Signature
# GSON Library
# For using GSON @Expose annotation
-keepattributes Annotation

# Gson specific classes
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Google Map
-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }

-keepattributes Signature

-keepclassmembers class com.example.YourModelClass.** {
  *;
}
# @Keep
-keep class com.google.firebase.** { *; }


-keep class kotlin.reflect.jvm.internal.** { *; }
-keep class kotlin.Metadata { *; }
-keep class com.facebook.** { *; }

#-dontwarn **CompatHoneycomb
#-dontwarn **CompatHoneycombMR2
#-dontwarn **CompatHoneyCreatorcombMR2
# -keepclasseswithmembernames class * {
#native<methods>;
#}
# -keepclasseswithmembernames class * {
#  public<init>(android.content.Context,android.util.AttributeSet);
# }

 ##---------------End: proguard configuration for Gson  -----------------------------------------------------------------------------------