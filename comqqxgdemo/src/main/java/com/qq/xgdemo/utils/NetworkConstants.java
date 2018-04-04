package com.qq.xgdemo.utils;

/**
 * Created by Administrator on 2017/4/19.
 */

public class NetworkConstants {
    public final static String COMMAND_XGTOKEN = "saveXGtoken";

    public final static String COMMAND_PHOTO = "getPhoto";
    public final static String COMMAND_VIDEO = "getVideo";
    public final static String COMMAND_WARN = "getWarn";

    public final static String COMMAND_REMOTE_COMMAND = "remoteCommand";
    public final static String COMMAND_COLSE_SYSTEM = "closeSystem";
    public final static String COMMAND_SPEAK_WORDS = "speakWords";
    public final static String COMMAND_SIM_FLOW= "simflow";
    public final static String COMMAND_SHOWWAY= "showWay";

    //接受到
    public final static String ASK_FOR_PICTURE = "action.colink.take.picture";
    public final static String ASK_FOR_VEDIO = "action.colink.take.video";
    public final static String ASK_FOR_REMOTECOMMAND = "action.colink.remotecommand";
    public final static String ASK_FOR_SHUTDOWN= "coogo.intent.action.SHUTDOWN_ACTION";
    public final static String ASK_FOR_SIMFLOW= "coogo.intent.action.SIMFLOW";

    public final static String RECEIVE_PICTURE = "action.colink.upload.picture";
    public final static String RECEIVE_VEDIO = "action.colink.upload.video";




    public final static String COMMAND_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    public final static String COMMAND_NET_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";


    public static String getServiceAddress(String code)
    {
        String serviceAddress;
        switch (code)
        {
            case "6":
                serviceAddress="http://wechat.conqueror.net.cn/wechattw/ToolsServlet";
                break;
            default:
                serviceAddress="http://wechat.conqueror.cn/ToolsServlet";
                break;
        }
        return serviceAddress;
    }


    public static String getUpFileAddress(String deviceType,String fileType)
    {
        String upFileAddress = null;

//        deviceType=deviceType.substring(deviceType.length()-2,deviceType.length());

        if(fileType.equals("image"))
            upFileAddress="http://cloud.conqueror.net.cn/file/imageUpload";
        else  if(fileType.equals("video"))
            upFileAddress="http://cloud.conqueror.net.cn/file/videoUpload";
        else  if(fileType.equals("warn"))
            upFileAddress="http://wechat.conqueror.net.cn/wechattw/ToolsServlet";

//        switch (deviceType)
//        {
//
//            case "CN":
//                if(fileType.equals("image"))
//                   upFileAddress="http://webservice.conqueror.cn:8181/file/imageUpload";
//                else  if(fileType.equals("video"))
//                   upFileAddress="http://webservice.conqueror.cn:8181/file/videoUpload";
//                else  if(fileType.equals("warn"))
//                    upFileAddress= "http://wechat.conqueror.cn/ToolsServlet";
//                break;
//            case "TW":
//                if(fileType.equals("image"))
//                    upFileAddress="http://cloud.conqueror.net.cn/file/imageUpload";
//                else  if(fileType.equals("video"))
//                    upFileAddress="http://cloud.conqueror.net.cn/file/videoUpload";
//                else  if(fileType.equals("warn"))
//                    upFileAddress="http://wechat.conqueror.net.cn/wechattw/ToolsServlet";
//                break;
//            default:
//                if(fileType.equals("image"))
//                    upFileAddress="http://webservice.conqueror.cn:8181/file/imageUpload";
//                else  if(fileType.equals("video"))
//                    upFileAddress="http://webservice.conqueror.cn:8181/file/videoUpload";
//                else  if(fileType.equals("warn"))
//                    upFileAddress= "http://wechat.conqueror.cn/ToolsServlet";
//                break;
//        }
        return upFileAddress;
    }


//    public static String getToWechatAddress(String deviceType,String fileType)
//    {
//        String toWechatAddress = null;
//        deviceType=deviceType.substring(deviceType.length()-2,deviceType.length());
//        switch (deviceType)
//        {
//            case "CN":
//                toWechatAddress="http://wechat.conqueror.cn/ToolsServlet";
//                break;
//            case "TW":
//                toWechatAddress= "http://wechat.conqueror.net.cn/wechattw/ToolsServlet";
//                break;
//            case "VN":
//                toWechatAddress=  "http://wechat.conqueror.net.cn/wechatvn/ToolsServlet";
//                break;
//            default:
//                toWechatAddress="http://wechat.conqueror.cn/ToolsServlet";
//                break;
//        }
//
//        return toWechatAddress;
//    }


}
