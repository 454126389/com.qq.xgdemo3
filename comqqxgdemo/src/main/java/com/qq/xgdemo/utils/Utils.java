package com.qq.xgdemo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Utils {


	public static String imei;

	public static String getImei(Context pContext) {
		if (null == imei || imei.equals("")) {
			imei = ((TelephonyManager) pContext
					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		}
		return imei;
	}


	public static boolean ExistSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	public static long getSDAllSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 获取所有数据块数
		long allBlocks = sf.getBlockCount();
		// 返回SD卡大小
		// return allBlocks * blockSize; //单位Byte
		// return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
	}


	public static String getSecondExterPath() {
		List<String> paths = getAllExterSdcardPath();

		if (paths.size() == 2) {

			for (String path : paths) {
				if (path != null && !path.equals(getFirstExterPath())) {
					return path;
				}
			}

			return null;

		} else {
			return null;
		}
	}

	public static List<String> getAllExterSdcardPath() {
		List<String> SdList = new ArrayList<String>();

		String firstPath = getFirstExterPath();

		// 得到路径
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				// 将常见的linux分区过滤掉
				if (line.contains("secure"))
					continue;
				if (line.contains("asec"))
					continue;
				if (line.contains("media"))
					continue;
				if (line.contains("system") || line.contains("cache")
						|| line.contains("sys") || line.contains("data")
						|| line.contains("tmpfs") || line.contains("shell")
						|| line.contains("root") || line.contains("acct")
						|| line.contains("proc") || line.contains("misc")
						|| line.contains("obb")) {
					continue;
				}

				if (line.contains("fat") || line.contains("fuse") || (line
						.contains("ntfs"))) {

					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						String path = columns[1];
						if (path!=null&&!SdList.contains(path)&&path.contains("sd"))
							SdList.add(columns[1]);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!SdList.contains(firstPath)) {
			SdList.add(firstPath);
		}

		return SdList;
	}

	public static String getFirstExterPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}


	public static boolean isNetAble(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	//读取电狗ID
	public static String getPid(Context context) {
		String pid = "";
		try {

			Config platformConfig = null;
			Uri URI = Uri
					.parse("content://tk.huayu.edog.ContentProvider.Config/Config");
			ContentResolver contentResolver = context.getContentResolver();
			Cursor cursor = contentResolver.query(URI,
					new String[] { Config.DEVICE_ID }, null, null, null);
			if ((null != cursor) && (cursor.moveToNext())) {
				platformConfig = new Config();
				platformConfig.deviceId = cursor.getString(cursor
						.getColumnIndex(Config.DEVICE_ID));
				cursor.close();

				pid = platformConfig.deviceId;
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return pid;
	}


	public static boolean isPkgInstalled(Context context, String packageName) {

		if (packageName == null || "".equals(packageName))
			return false;
		android.content.pm.ApplicationInfo info = null;
		try {
			info = context.getPackageManager().getApplicationInfo(packageName,0);

			return info != null;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}


//判断网络是否可用
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
		} else {
			//如果仅仅是用来判断网络连接
			//则可以使用 cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}



	public static void upWarn(Context context, MsgItem msgitem) {

		String upFileAddress=NetworkConstants.getUpFileAddress(msgitem.getVer(),msgitem.getType());
		RequestParams params = new RequestParams(); // 默认编码UTF-8
		params.addBodyParameter("type", "msg");

		params.addBodyParameter("RESULT_STR",msgitem.getPath());
		params.addBodyParameter("imei",Utils.getImei(context));
		params.addBodyParameter("pid",Utils.getPid(context));

		uploadMethod(context,params, upFileAddress, msgitem);

	}


	public static void up2wechat(Context context, MsgItem msgitem) {


		RequestParams params = new RequestParams(); // 默认编码UTF-8
		params.addBodyParameter("file", new File(msgitem.getPath()));
		params.addBodyParameter("params", msgitem.getJsonobj());
		String upFileAddress=NetworkConstants.getUpFileAddress(msgitem.getVer(),msgitem.getType());
		uploadMethod(context,params, upFileAddress, msgitem);

	}


	public static void uploadMethod(final Context context, RequestParams params, String uploadHost,
									final MsgItem msgitem) {

		HttpUtils http = new HttpUtils(60*1000); // 超时
		http.configCurrentHttpCacheExpiry(1);
		http.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						Log.d("kjx", "start");
					}

					@Override
					public void onLoading(long total, long current,
										  boolean isUploading) {
						if (isUploading) {
							Log.d("kjx", "upload: " + current + "/" + total);
						} else {
							Log.d("kjx", "reply: " + current + "/" + total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						try {
							JSONObject json = new JSONObject(result);
							if (json.getBoolean("success")) {

								if (msgitem.getType().equals("image"))
									SpUtil.setLostImage(null);
								else if (msgitem.getType().equals("video"))
									SpUtil.setLostVideo(null);

							}

						} catch (JSONException e) {
							Log.e("JSONException", e.getMessage());
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.d("logcat", "error:" + error.getExceptionCode()
								+ ":" + msg);
						if (msgitem.getType().equals("image"))
							SpUtil.setLostImage(msgitem);
						else if (msgitem.getType().equals("video"))
							SpUtil.setLostVideo(msgitem);
					}
				});

	}





}
