//
// Created by LiuYong on 2018/10/30.
//

#include <jni.h>
#include <android/log.h>
#include "serial.h"
#include "main.h"

static char send_temp_buffer[1024];
static char bw_serial_resp_buffer[2048];
static char b8_resp_buffer[2048];
static int resp_buff[2048];
jstring bw_serail_resp;

#define TAG "FscBwNative"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE,TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

void bt_log(const char* Format, ... )
{
#if 0
    char buf[512];
	va_list parms;
	int length = 0;
	va_start( parms, Format );
	length += vsnprintf(buf,512,Format, parms );
	va_end(parms);
	buf[length++] = '\0';
	fprintf(stdout, "%s\n", buf);
#endif
}

JNIEXPORT jint JNICALL
Java_com_semisky_btcarkit_service_natives_FscBwNative_openBwSerial(JNIEnv *env, jobject instance) {
    LOGI("############# JNI openBwSerial #############");
    if (0 != bt_serial_open("/dev/bw_serial", 115200)) {
        return 0;
    }
    return 1;
}

JNIEXPORT jint JNICALL
Java_com_semisky_btcarkit_service_natives_FscBwNative_closeBwSerial(JNIEnv *env, jobject instance) {
    LOGI("############# JNI closeBwSerial #############");
    bt_serial_write(0);
    return 1;
}

JNIEXPORT jint JNICALL
Java_com_semisky_btcarkit_service_natives_FscBwNative_sendCommand(JNIEnv *env, jobject instance,
                                                          jstring command) {
    int length = (*env)->GetStringLength(env, command);
    const jchar *buf = (*env)->GetStringChars(env, command, 0);

    if (length <= 0) return 0;
    LOGI("############# JNI sendCommand #############");
    LOGI("JNI sendCommand len = %d", length);
    for (int i = 0; i < length; i++) {
        send_temp_buffer[i] = buf[i];
    }
    send_temp_buffer[length] = '\0';

    bt_serial_write(send_temp_buffer);

    (*env)->ReleaseStringChars(env, command, buf);

    return length;
}

JNIEXPORT jintArray JNICALL
Java_com_semisky_btcarkit_service_natives_FscBwNative_recvResponse(JNIEnv *env, jobject instance) {
    int size = bt_serial_read(b8_resp_buffer,sizeof(b8_resp_buffer));
    LOGI("############# JNI recvResponse #############");
    LOGI("JNI_recvResponse len = %d", size);
    if(size <= 0) return 0;


    // copy from char buffer to int buffer
    for(int i = 0; i < size; i++)
    {
        resp_buff[i] = b8_resp_buffer[i];
    }

    jintArray intArray = (*env)->NewIntArray(env, size);

    (*env)->SetIntArrayRegion(env, intArray, 0, size, (jint*)resp_buff);

    return intArray;
}