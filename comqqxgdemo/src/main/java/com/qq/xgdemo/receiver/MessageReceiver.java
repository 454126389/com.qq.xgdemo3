package com.qq.xgdemo.receiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.qq.xgdemo.common.NotificationService;
import com.qq.xgdemo.po.XGNotification;
import com.qq.xgdemo.utils.Gps;
import com.qq.xgdemo.utils.MsgItem;
import com.qq.xgdemo.utils.NetworkConstants;
import com.qq.xgdemo.utils.PositionUtil;
import com.qq.xgdemo.utils.SpUtil;
import com.qq.xgdemo.utils.Utils;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import static com.qq.xgdemo.utils.Utils.up2wechat;

public class MessageReceiver extends XGPushBaseReceiver {
	private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
	public static final String LogTag = "TPushReceiver";

	private void show(Context context, String text) {
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}





	// 通知展示
	@Override
	public void onNotifactionShowedResult(Context context,
			XGPushShowedResult notifiShowedRlt) {
		if (context == null || notifiShowedRlt == null) {
			return;
		}

		Intent broadcastIntent = new Intent("com.wanma.action.PLAY_TTS");
		JSONObject jsonCommand = null;
		String type;

		try {

			jsonCommand = new JSONObject(notifiShowedRlt.getContent());
			type = jsonCommand.getString("type");
			Log.d("kjx","接收到广播type="+type);

			switch (type) {
				case NetworkConstants.COMMAND_PHOTO:
					if (Utils.getSecondExterPath() != null) {
						Intent photoIntent = new Intent(
								NetworkConstants.ASK_FOR_PICTURE);
						String openId = null;
						try {
							openId = jsonCommand.getString("openId");
						} catch (Exception e) {
							e.printStackTrace();
						}
						Bundle pBundle = new Bundle();
						pBundle.putString("OPEN_ID", openId);
						photoIntent.putExtras(pBundle);
						context.sendBroadcast(photoIntent);

						Log.d("kjx", "把广播传递给相机了");

					} else {
						broadcastIntent.putExtra("content", "sd卡读取错误，操作中断");
						context.sendBroadcast(broadcastIntent);
					}
					break;

				case NetworkConstants.COMMAND_WARN:
					if (Utils.getSecondExterPath() != null) {

						String path = null;
						try {
							path = jsonCommand.getString("path");
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

						String open_id = jsonCommand.getString("openId");

						String jsonobj = "{\"deviceId\":\"" + Utils.getPid(context)
								+ "\",\"weid\":\"" + open_id + "\",\"time\":\""
								+ System.currentTimeMillis() + "\"}";

						String ver =jsonCommand.getString("ver");

						MsgItem tempMsg = new MsgItem(path, open_id, jsonobj,"image",ver);


						up2wechat(context, tempMsg);

					} else {

						broadcastIntent.putExtra("content", "sd卡读取错误，操作中断");
						context.sendBroadcast(broadcastIntent);
					}
					break;

				case NetworkConstants.COMMAND_VIDEO:
					if (Utils.getSecondExterPath() != null) {

						Intent videoIntent = new Intent(
								NetworkConstants.ASK_FOR_VEDIO);
						String openIdStr = null;
						try {
							openIdStr = jsonCommand.getString("openId");
						} catch (Exception e) {
							e.printStackTrace();
						}
						videoIntent.putExtra("OPEN_ID", openIdStr);
						context.sendBroadcast(videoIntent);

						Log.d("kjx", "把广播传递给相机了");

					} else {
						broadcastIntent.putExtra("content", "sd卡读取错误，操作中断");
						context.sendBroadcast(broadcastIntent);
					}
					break;

				case NetworkConstants.COMMAND_SPEAK_WORDS:
					String words = null;
					try {
						words = jsonCommand.getString("words");
					} catch (Exception e) {
						e.printStackTrace();
					}

					broadcastIntent.putExtra("content", words);
					context.sendBroadcast(broadcastIntent);
					break;

				case NetworkConstants.COMMAND_COLSE_SYSTEM:
					broadcastIntent.putExtra("content", "正在关机");
					context.sendBroadcast(broadcastIntent);

					Intent closeSystemIntent = new Intent(NetworkConstants.ASK_FOR_SHUTDOWN);
					context.sendBroadcast(closeSystemIntent);
					break;

				case NetworkConstants.COMMAND_REMOTE_COMMAND:

					String text = "";
					String cLat = null,
							cLng = null;


					Intent remoteCommandIntent = new Intent(
							NetworkConstants.ASK_FOR_REMOTECOMMAND);

					try {
						cLat = jsonCommand.getString("lat");

						cLng = jsonCommand.getString("lon");
						text = jsonCommand.getString("text");

						broadcastIntent.putExtra("content", "接收到导航信息，正在为您导航到"
								+ text);
						context.sendBroadcast(broadcastIntent);

					} catch (Exception e) {
						e.printStackTrace();
					}
					remoteCommandIntent.putExtra("type", "navigation");
//					//品网用
////					Gps gcj02 = PositionUtil.bd09_To_Gcj02(Double.valueOf(cLat), Double.valueOf(cLng));
//					remoteCommandIntent.putExtra("lat",Float.parseFloat(cLat));
//					remoteCommandIntent.putExtra("lng",Float.parseFloat(cLng));


					remoteCommandIntent.putExtra("lat", cLat);
					remoteCommandIntent.putExtra("lng", cLng);



					remoteCommandIntent.putExtra("maptype", "Bd09");
					remoteCommandIntent.putExtra("address", text);
					context.sendBroadcast(remoteCommandIntent);
					break;
//				case NetworkConstants.COMMAND_SHOWWAY:
//					broadcastIntent.putExtra("content", "接收到一条导航信息，正在为您跳转导航");
//					context.sendBroadcast(broadcastIntent);
//
//					Double dLat,
//							dLng;
//					try {
//						dLat = Double.valueOf(jsonCommand.getString("lat"));
//						dLng = Double.valueOf(jsonCommand.getString("lon"));
//
//						startNavi(context, dLat, dLng, "");
////						L.i("[CogooReceiver] Step 2 : send location to Navigation..");
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					break;
				case NetworkConstants.COMMAND_SIM_FLOW:
//					broadcastIntent.putExtra("content", "流量不足");
//					context.sendBroadcast(broadcastIntent);

					Intent simflowIntent = new Intent(NetworkConstants.ASK_FOR_SIMFLOW);
					String plan = jsonCommand.getString("plan");
					String flowuse = jsonCommand.getString("flowuse");
					simflowIntent.putExtra("plan", plan);
					simflowIntent.putExtra("flowuse", flowuse);
					context.sendBroadcast(simflowIntent);
					break;



			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

/*		XGNotification notific = new XGNotification();
		notific.setMsg_id(notifiShowedRlt.getMsgId());
		notific.setTitle(notifiShowedRlt.getTitle());
		notific.setContent(notifiShowedRlt.getContent());
		// notificationActionType==1为Activity，2为url，3为intent
		notific.setNotificationActionType(notifiShowedRlt
				.getNotificationActionType());
		// Activity,url,intent都可以通过getActivity()获得
		notific.setActivity(notifiShowedRlt.getActivity());
		notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(Calendar.getInstance().getTime()));
		NotificationService.getInstance(context).save(notific);
		context.sendBroadcast(intent);
		show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());*/
	}

	@Override
	public void onUnregisterResult(Context context, int errorCode) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "反注册成功";
		} else {
			text = "反注册失败" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onSetTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"设置成功";
		} else {
			text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	@Override
	public void onDeleteTagResult(Context context, int errorCode, String tagName) {
		if (context == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = "\"" + tagName + "\"删除成功";
		} else {
			text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);

	}

	// 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
	@Override
	public void onNotifactionClickedResult(Context context,
			XGPushClickedResult message) {
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
			// 通知在通知栏被点击啦。。。。。
			// APP自己处理点击的相关动作
			// 这个动作可以在activity的onResume也能监听，请看第3点相关内容
			text = "通知被打开 :" + message;
		} else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
			// 通知被清除啦。。。。
			// APP自己处理通知被清除后的相关动作
			text = "通知被清除 :" + message;
		}
//		Toast.makeText(context, "广播接收到通知被点击:" + message.toString(),
//				Toast.LENGTH_SHORT).show();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.d(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理的过程。。。
		Log.d(LogTag, text);
		show(context, text);
	}

	@Override
	public void onRegisterResult(Context context, int errorCode,
			XGPushRegisterResult message) {
		// TODO Auto-generated method stub
		if (context == null || message == null) {
			return;
		}
		String text = "";
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			text = message + "注册成功";
			// 在这里拿token
			String token = message.getToken();
			String pid = Utils.getPid(context);
			if (!"".equals(pid)) {
				String serviceAddress= NetworkConstants.getServiceAddress(pid.substring(0,1));

				RequestParams params = new RequestParams("utf-8");
				params.addBodyParameter("type", NetworkConstants.COMMAND_XGTOKEN);
				params.addBodyParameter("did", pid);
				params.addBodyParameter("xgtoken", token);
				HttpUtils http = new HttpUtils();
				http.send(HttpRequest.HttpMethod.POST, serviceAddress,
						params,
						new RequestCallBack<String>() {

							@Override
							public void onStart() {
								Log.d("kjx","conn");
							}

							@Override
							public void onLoading(long total, long current, boolean isUploading) {
								if (isUploading) {
									Log.d("kjx","upload: " + current + "/" + total);
								} else {
									Log.d("kjx","reply: " + current + "/" + total);
								}
							}

							@Override
							public void onSuccess(ResponseInfo<String> responseInfo) {
								Log.d("kjx","xgtoken服务器保存完成");
							}

							@Override
							public void onFailure(HttpException error, String msg) {
								Log.d("kjx",error.getExceptionCode() + ":" + msg);
							}
						});

			}

		} else {
			text = message + "注册失败，错误码：" + errorCode;
		}
		Log.d(LogTag, text);
		show(context, text);
	}

	// 消息透传
	@Override
	public void onTextMessage(Context context, XGPushTextMessage message) {
		// TODO Auto-generated method stub
		String text = "收到消息:" + message.toString();
		// 获取自定义key-value
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			try {
				JSONObject obj = new JSONObject(customContent);
				// key1为前台配置的key
				if (!obj.isNull("key")) {
					String value = obj.getString("key");
					Log.d(LogTag, "get custom value:" + value);
				}
				// ...
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// APP自主处理消息的过程...
		Log.d(LogTag, text);
		show(context, text);
	}



	private void startNavi(Context mContext, double lat, double lng,
						   String address) {
		int mapIndex = Settings.System.getInt(mContext.getContentResolver(),
				"MAP_INDEX", 1);
		switch (mapIndex) {
			case 1:
				Gps gcj02 = PositionUtil.bd09_To_Gcj02(lat, lng);
				startNavi(mContext, gcj02.getWgLat(), gcj02.getWgLon(), address, 2,
						0);
				break;
			case 3:
			/*
			 * Gps gcj = PositionUtil.bd09_To_Gcj02(lat, lng);
			 * KLDUriApi.startNavi(mContext,gcj.getWgLat(), gcj.getWgLon(),
			 * address);
			 */
				break;
			default:
				Intent intent = new Intent();
				intent.setData(Uri.parse("bdnavi://plan?dest=" + lat + "," + lng
						+ "," + address + ")"));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				mContext.startActivity(intent);
				break;
		}
	}

	public static void startNavi(Context context, double toLat, double toLng,
								 String name, int style, int dev) {

		String cat = Intent.CATEGORY_DEFAULT;
		String pkg = "com.autonavi.minimap";

		AMapUri uri = new AMapUri("navi");
		uri.addParam("sourceApplication", "uniCarSolution");
		uri.addParam("poiname", name);
		uri.addParam("lat", toLat);
		uri.addParam("lon", toLng);
		uri.addParam("dev", dev);
		uri.addParam("style", style);
		String dat = uri.getDatString();
		Intent intent = new Intent();
		intent.addCategory(cat);
		intent.setPackage(pkg);
		intent.setData(Uri.parse(dat));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}


}
