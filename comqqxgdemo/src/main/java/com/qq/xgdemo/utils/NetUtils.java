package com.qq.xgdemo.utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by Administrator on 2017/4/19.
 */

public class NetUtils {


    /***
     *
     * 网络请求get
     */
    public static void get1(String url) {
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.GET,
                url,
                new RequestCallBack<String>(){
                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
//                        text1.setText(current + "/" + total);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        text1.setText(responseInfo.result);
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                    }
                });
    }

    public static void get2(String url) {
        // TODO Auto-generated method stub
        HttpUtils http = new HttpUtils(10000);//放回html数据和api数据--10000代表10s超时
        http.configCurrentHttpCacheExpiry(5000);//设置缓存5s,5秒内直接返回上次成功请求的结果。
        http.configResponseTextCharset("UTF-8");//配置HTTP响应的文本编码
        http.send(HttpRequest.HttpMethod.GET,url, new RequestCallBack<String>(){
            @Override
            public void onLoading(long total, long current, boolean isUploading) {//更新任务进度（需覆盖重写）
//                text1.setText(current + "/" + total);
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                text1.setText(responseInfo.result);//网络请求执行成功（需覆盖重写）//接口回调返回数据
            }

            @Override
            public void onStart() {//开始发送网络请求（需覆盖重写）
            }

            @Override
            public void onFailure(HttpException error, String msg) {
            }
        });
    }

    /***
     * post请求
     */


    public static void post1(String url) {
        RequestParams params = new RequestParams("utf-8");
        params.addBodyParameter("showapi_appid", "11548");
        params.addBodyParameter("showapi_sign", "bb1d15ab7ce646ec87cc89d684ca4bcb");
        params.addBodyParameter("type", "10");
        params.addBodyParameter("page", "1");
        params.addBodyParameter("maxResult", "20");
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url,
                params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
//                        text1.setText("conn...");
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isUploading) {
                        if (isUploading) {
//                            text1.setText("upload: " + current + "/" + total);
                        } else {
//                            text1.setText("reply: " + current + "/" + total);
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        text1.setText("reply: " + responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
//                        text1.setText(error.getExceptionCode() + ":" + msg);
                    }
                });
    }

}
