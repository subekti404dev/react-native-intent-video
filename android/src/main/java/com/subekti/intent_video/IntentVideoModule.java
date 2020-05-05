package com.subekti.intent_video;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

/**
 * Created by subekti on 05/05/20.
 */
public class IntentVideoModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final int REQUEST_CODE = 12;
    
    
    private static final String VIDEO_URL = "videoUrl";
    private static final String SUBTITLE_URL = "subtitleUrl";
    private static final String SUBTITLE_NAME = "subtitleName";


    Promise promise;
    ReactApplicationContext reactContext;

    public IntentVideoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "IntentVideo";
    }


    @ReactMethod
    public void open(ReadableMap params, final Promise promise) {
        this.promise = promise;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");

        if (params.hasKey(VIDEO_URL)) {
            intent.setDataAndType(Uri.parse(params.getString(VIDEO_URL)), "video/*");
        } 
        if (params.hasKey(SUBTITLE_URL)) {
            intent.PutExtra("subs", Uri.parse(params.getString(SUBTITLE_URL)));
            intent.PutExtra("subs.enable", Uri.parse(params.getString(SUBTITLE_URL)));
            if (params.hasKey(SUBTITLE_NAME)) {
                intent.PutExtra("subs.name", params.getString(SUBTITLE_NAME));
                intent.PutExtra("subs.filename", params.getString(SUBTITLE_NAME) + ".SRT");
            } else {
                intent.PutExtra("subs.name", "NAME");
                intent.PutExtra("subs.filename", "NAME.SRT");
            }
        }
        getReactApplicationContext().startActivityForResult(intent, REQUEST_CODE, null);
    }


    @Override
    public void onNewIntent(Intent intent) {
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
        if (requestCode != REQUEST_CODE) {
            return;
        }
        WritableMap params = Arguments.createMap();
        if (intent != null) {
            params.putInt("resultCode", resultCode);

            Uri data = intent.getData();
            if (data != null) {
                params.putString("data", data.toString());
            }

            Bundle extras = intent.getExtras();
            if (extras != null) {
                params.putMap("extra", Arguments.fromBundle(extras));
            }
        }

        this.promise.resolve(params);
    }
}
