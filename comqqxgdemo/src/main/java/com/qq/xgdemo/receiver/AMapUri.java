package com.qq.xgdemo.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by wl on 2015/11/16.
 */
public class AMapUri {
    public static final String TAG = "AMapUriData";

    private static final String TEMPLATE = "androidamap://%1$s";

    private String mService;
    private HashMap<String, Object> mParams;

    public AMapUri(String service) {
        mService = service;
        mParams = new HashMap<String, Object>();
    }

    public void addParam(String key, Object value) {
        if (mParams.containsKey(key)) {
        }
        mParams.put(key, value);
    }

    public String getDatString() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(TEMPLATE, mService));

        String split = "?";
        Set<String> keys = mParams.keySet();
        for (String key : keys) {
            builder.append(split);
            builder.append(key);
            builder.append("=");
            builder.append(mParams.get(key));
            split = "&";
        }

        return builder.toString();
    }

    public static void openBaiduNavi(Context context,String action){
        Intent intent = new Intent();
        intent.setData(Uri.parse(action));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
