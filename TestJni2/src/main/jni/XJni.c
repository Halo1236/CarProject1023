//
// Created by LiuYong on 2018/10/30.
//

#include "XJni.h"
#include <jni.h>
#include <stdlib.h>
#include <string.h>

JNIEXPORT jstring JNICALL
Java_com_semisky_testjni2_XJni_getStr(JNIEnv *env, jobject instance, jstring str_) {
    const char *s = (*env)->GetStringUTFChars(env, str_, 0);
    char arr[50] = "qq";
    memcpy(arr,s,20);
    // TODO returnValue

    (*env)->ReleaseStringUTFChars(env, str_, s);

    return (*env)->NewStringUTF(env, arr);
}
