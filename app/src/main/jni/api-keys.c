#include <jni.h>
JNIEXPORT jstring JNICALL Java_com_d2dreamdevelopers_myapplication_APIKeyLibrary_getAPIKey(JNIEnv *env, jobject instance) {
return (*env)-> NewStringUTF(env, "YOUR_AWESOME_API_GOES_HERE");
}