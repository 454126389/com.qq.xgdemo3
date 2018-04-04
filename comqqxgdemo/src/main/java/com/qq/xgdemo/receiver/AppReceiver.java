package com.qq.xgdemo.receiver;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.qq.xgdemo.utils.MsgItem;
import com.qq.xgdemo.utils.NetworkConstants;
import com.qq.xgdemo.utils.SpUtil;
import com.qq.xgdemo.utils.Utils;
import com.tencent.android.tpush.XGPushManager;

import static com.qq.xgdemo.utils.NetworkConstants.COMMAND_BOOT_COMPLETED;
import static com.qq.xgdemo.utils.NetworkConstants.COMMAND_NET_CHANGE;
import static com.qq.xgdemo.utils.Utils.up2wechat;
import static com.qq.xgdemo.utils.Utils.upWarn;

/**
 * 自定义接收器
 * 
 * 2) 接收不到自定义消
 */

public class AppReceiver extends BroadcastReceiver {



	@Override
	public void onReceive(final Context context, Intent intent) {

		Bundle bundle = intent.getExtras();

		if(COMMAND_BOOT_COMPLETED.equals(intent.getAction()))
		{
			SpUtil.init(context.getApplicationContext());
			XGPushManager.registerPush(context.getApplicationContext());
		}
		else if(COMMAND_NET_CHANGE.equals(intent.getAction()))
		{
			Log.d("kjx", "网络变化");
			if (Utils.isNetworkAvailable(context))
			{
				SpUtil.init(context.getApplicationContext());
				XGPushManager.registerPush(context.getApplicationContext());
			}
		}
		else if (NetworkConstants.RECEIVE_PICTURE.equals(intent.getAction())) {


//				Toast.makeText(context, "接收到返回的结果"+bundle.getString("FILE_NAME"), Toast.LENGTH_SHORT).show();
				Log.d("kjx", "接收到返回的结果");
				Log.d("kjx", "url="+bundle.getString("FILE_NAME"));

//
//			RequestParams postparams = new RequestParams();
//
////			String posturl = "http://wechat.conqueror.cn/ToolsServlet";
//
//			String posturl="";
//			if(msgitem.getVer().equals("i5X_VN"))
//				posturl = "http://wechatvn.conqueror.cn/ToolsServlet";
//			else if(msgitem.getVer().equals("i5X_TW"))
//				posturl = "http://wechat.conqueror.net.cn/wechattw/ToolsServlet";
//			else
//			{
//				msgitem.setVer("i5X_CN");
//				posturl = "http://wechat.conqueror.cn/ToolsServlet";
//			}
//
//
//			postparams.addQueryStringParameter("type", "msg");
//			postparams.addBodyParameter("RESULT_STR",msgitem.getPath());
//			postparams.addBodyParameter("imei",Utils.getImei(context));
//			postparams.addBodyParameter("pid",SpUtil.getPid());
//
//			doPost(posturl, postparams);
//
//			SpUtil.setLostWarn(null);


				// 获取到Camera反馈回来的消息；
				String path = bundle.getString("FILE_NAME");
				
				String ver ="i5X_CN";
				if(bundle.getString("ver")!=null)
					ver=bundle.getString("ver");


				String lat="";
				String lon="";
				if(bundle.getString("lat")!=null)
					lat= bundle.getString("lat");
				if(bundle.getString("lon")!=null)
					lon= bundle.getString("lon");

			
				
				String open_id=bundle.getString("OPEN_ID");

				

				String jsonobj = "{\"deviceId\":\"" + Utils.getPid(context)
						+ "\",\"weid\":\""	+ open_id
						+ "\",\"time\":\""	+System.currentTimeMillis()
						+ "\",\"lat\":\""	+lat
						+ "\",\"lon\":\""	+lon
						+ "\"}";


				final MsgItem tempMsg;

				// 震动报警
				if (open_id == null) {

					tempMsg=new MsgItem(path, open_id, jsonobj, "warn",ver);

					if (Utils.isNetworkAvailable(context))
						upWarn(context, tempMsg);
					else
						// 无网络
						SpUtil.setLostWarn(tempMsg);

				} else {

					tempMsg=new MsgItem(path, open_id, jsonobj, "image",ver);

					if (Utils.isNetworkAvailable(context))
					{
						new Thread(new Runnable()
						{
							@Override
							public void run()
							{
								Log.d("kjx", "开启上传线程");
								up2wechat(context, tempMsg);
							}
						}).start();
					}

					else
						// 无网络
						SpUtil.setLostImage(tempMsg);

				}
			}
		else if (NetworkConstants.RECEIVE_VEDIO.equals(intent.getAction())) {


			String path = bundle.getString("FILE_NAME");
			String open_id = bundle.getString("OPEN_ID");

			String ver = bundle.getString("ver");
//			if (ver == null)
//				ver = "i5X_CN";

			String lat = "";
			String lon = "";
			if (bundle.getString("lat") != null)
				lat = bundle.getString("lat");
			if (bundle.getString("lon") != null)
				lon = bundle.getString("lon");


			String jsonobj = "{\"deviceId\":\"" + Utils.getPid(context)
					+ "\",\"weid\":\"" + open_id
					+ "\",\"time\":\"" + System.currentTimeMillis()
					+ "\",\"lat\":\"" + lat
					+ "\",\"lon\":\"" + lon
					+ "\"}";


			final MsgItem tempMsg ;


			// 震动报警
			if (open_id == null) {

				tempMsg = new MsgItem(path, open_id, jsonobj, "warn", ver);

				if (Utils.isNetworkAvailable(context))
					upWarn(context, tempMsg);
				else
					// 无网络
					SpUtil.setLostWarn(tempMsg);

			} else {

				tempMsg = new MsgItem(path, open_id, jsonobj, "video", ver);

				if (Utils.isNetworkAvailable(context)) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							Log.d("kjx", "开启上传线程");
							up2wechat(context, tempMsg);
						}
					}).start();
				} else
					// 无网络
					SpUtil.setLostImage(tempMsg);

			}
		} else if("com.ruiyi.ctrlright".equals(intent.getAction()))
		{
			Intent photoIntent = new Intent(
					NetworkConstants.ASK_FOR_PICTURE);
			String openId = null;
			try {
				openId = "gsn";
			} catch (Exception e) {
				e.printStackTrace();
			}
			Bundle pBundle = new Bundle();
			pBundle.putString("OPEN_ID", openId);
			photoIntent.putExtras(pBundle);
			context.sendBroadcast(photoIntent);
		}

	};


}
