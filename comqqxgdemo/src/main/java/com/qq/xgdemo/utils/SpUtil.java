package com.qq.xgdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/4/5.
 */
public class SpUtil {
    static SharedPreferences prefs;

    public static void init(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean isNight() {
        return prefs.getBoolean("isNight", false);
    }

    

//    public static void setPid( String pid) {
//        prefs.edit().putString("pid", pid).commit();
//    }
//
//    public static String getPid() {
//        return prefs.getString("pid", "");
//    }
    
    


  public static MsgItem getLostImage() {
        return new Gson().fromJson(prefs.getString("lostimage", ""), MsgItem.class);
    }

    public static void setLostImage(MsgItem lostimage) {
        if(lostimage!=null)
        prefs.edit().putString("lostimage", new Gson().toJson(lostimage)).commit();
    }



    	public static MsgItem getLostVideo() {
          return new Gson().fromJson(prefs.getString("lostvideo", ""), MsgItem.class);
      }

      public static void setLostVideo(MsgItem lostvideo) {
          if(lostvideo!=null)
          prefs.edit().putString("lostvideo", new Gson().toJson(lostvideo)).commit();
      }
      
   	public static MsgItem getLostWarn() {
        return new Gson().fromJson(prefs.getString("lostwarn", ""), MsgItem.class);
    }

    public static void setLostWarn(MsgItem lostwarn) {
        prefs.edit().putString("lostwarn", new Gson().toJson(lostwarn)).commit();
    }
      
      
}
