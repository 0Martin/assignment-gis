package com.example.martin.mapbox;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionHandler {

    public static void requestAplicationPermissions(Activity activity, String[] permissions,
                                              int requestCode){
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public static boolean checkPermissions(Context context, String[] permissions){
        for(String permission : permissions)
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED)
                return false;
        return true;
    }
}
