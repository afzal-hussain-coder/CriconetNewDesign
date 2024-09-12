package com.pb.criconet.util;
import android.os.Build;
import android.util.Log;

public class DeviceInfo {

    public static String logDeviceName() {
        String deviceName = Build.MODEL; // Device model name
        return deviceName;

    }

    public static String getAndroidVersion(){
        String androidVersion = Build.VERSION.RELEASE; // Android version
        return androidVersion;
    }
}
