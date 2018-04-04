package com.qq.xgdemo.utils;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class Config {
    private static final String TAG = "Config";
    public static final String DEVICE_ID    = "deviceId";
    public static final String PLATFORM     = "platform";
    public static final String VOICE_ENGINE = "voiceEngine";
    public static final String PLATFORM_PINWANG = "pinWang";
    public static final String PLATFORM_CONQUEROR = "conqueror";
    public static final String VOICE_ENGINE_PINWANG = "pinWang";
    public static final String VOICE_ENGINE_CONQUEROR = "conqueror";
    public static final String VOICE_ENGINE_AUTO = "auto";

    public String deviceId;     // 设备号
    public String platform;     // 平台
    public String voiceEngine;  // 语音引擎

    public Config(){

    }

    /**
     * 从uri中获取配置值
     * @param context 上下文
     * @param uri uri
     */
    public Config(Context context, Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, new String[]{Config.DEVICE_ID, Config.PLATFORM, Config.VOICE_ENGINE}, null, null, null);
        if ((null != cursor) && (cursor.moveToNext())) {
            deviceId = cursor.getString(cursor.getColumnIndex(Config.DEVICE_ID));
            platform = cursor.getString(cursor.getColumnIndex(Config.PLATFORM));
            voiceEngine = cursor.getString(cursor.getColumnIndex(Config.VOICE_ENGINE));
            cursor.close();
        }
    }

    /**
     * 检查配置是否合法
     * @return 0-合法 负数-不合法
     */
    public int checkValid(){
        // todo: 检查id 平台 语音引擎的有效性，有效返回
        if((null==deviceId)||(null==platform)||(null==voiceEngine))
            return -1;

        return 0;
    }

    /**
     * 保存配置到文件
     * @return 是否保存成功
     */
    public boolean save(Context context, Uri uri){
        if(checkValid()<0) {
            Log.w(TAG,"config is invalid");
            return false;
        }
        ContentValues cv = new ContentValues();
        cv.put(Config.DEVICE_ID,deviceId);
        cv.put(Config.PLATFORM,platform);
        cv.put(Config.VOICE_ENGINE,voiceEngine);
        return context.getContentResolver().update(uri,cv,null,null)==1;
    }



}
