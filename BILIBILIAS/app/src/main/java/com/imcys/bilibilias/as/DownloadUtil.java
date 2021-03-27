package com.imcys.bilibilias.as;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;


/**
 * 调用系统下载器下载
 */
//使用步骤1：
//AndroidManifest注册，最好重新，好监听下载完成
 /*
 <receiver android:name="aheng.tool1.utils.DownloadUtil$DownloadManagerReceiver">
<intent-filter>
<action android:name="android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED"/>
<action android:name="android.intent.action.DOWNLOAD_COMPLETE"/>
</intent-filter>
</receiver>
*/

//步骤2：
/*
DownloadUtil downloadUtil=new DownloadUtil(activity,downloadUrl);
//下载显示名字，不能是中文
downloadUtil.setDownloadFileName("apkName"+System.currentTimeMillis()+".apk");
downloadUtil.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
downloadUtil.start();*/

public class DownloadUtil {
    private Context mContext;
    private String downloadFileName = "user.flv";
    private static long myReference;
    private static DownloadManager downloadManager;
    private DownloadManager.Request downloadRequest;
    private String aid;
    private String cookie;

    public DownloadUtil(String downloadUrl,String setAid,String setCookie,Context context) {
        this.aid = setAid;
        this.cookie = setCookie;
        this.mContext = context;
        initDownload(downloadUrl);
    }


    private void initDownload(String downloadUrl) {
        
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(downloadUrl);
        downloadRequest = new DownloadManager.Request(uri);
        // 设置目标存储在外部目录，一般位置可以用
        downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,downloadFileName);
        //downloadRequest.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, downloadFileName);
        //下载的文件能被其他应用扫描到
        downloadRequest.allowScanningByMediaScanner();
        //设置被系统的Downloads应用扫描到并管理,默认true
        downloadRequest.setVisibleInDownloadsUi(true);

        //限定在WiFi还是手机网络(NETWORK_MOBILE)下进行下载
        //downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //downloadRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);

        // 设置请求头
        downloadRequest.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
        downloadRequest.addRequestHeader("referer", "https://www.bilibili.com/video/av" + aid + "/");
        downloadRequest.addRequestHeader("Cookie", cookie);

        // 设置mime类型，这里看服务器配置，一般国家化的都为utf-8编码。
        downloadRequest.setMimeType("application/vnd.android.package-archive");
        /**
         * 设置notification显示状态
         * Request.VISIBILITY_VISIBLE：在下载进行的过程中，通知栏中会一直显示该下载的Notification，当下载完成时，该Notification会被移除，这是默认的参数值。
         * Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED：在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，直到用户点击该
         * Notification或者消除该Notification。
         * Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION：只有在下载完成后该Notification才会被显示。
         * Request.VISIBILITY_HIDDEN：不显示该下载请求的Notification。如果要使用这个参数，需要在应用的清单文件中加上android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
         */
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        //设置notification的标题
        // downloadRequest.setTitle("");
        //设置notification的描述
        // downloadRequest.setDescription("");
    }


    public void start() {
        myReference = downloadManager.enqueue(downloadRequest);
    }

    /**
     * 须static，不然在AndroidManifest注册时报错：java.lang.InstantiationException has no zero argument constructor
     * 或者must be registered and unregistered inside the Parent class
     */
    public static class DownloadManagerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Notification点击
            if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
                String extraID = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
                long[] references = intent.getLongArrayExtra(extraID);
                for (long reference : references)
                    if (reference == myReference) {
                    }
            }
            //下载完成
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (completeDownloadId == myReference) {
                    Cursor cursor = downloadManager.query(new DownloadManager.Query()
                            .setFilterById(completeDownloadId));
                    cursor.moveToFirst();
                    String filePath = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    cursor.close();
                    if (filePath != null) {
                        if (filePath.contains(context.getPackageName())) {
                            if (filePath.endsWith("apk")) {
                                // AppUtils.installApk(filePath.trim().substring(7));
                                Toast.makeText(context, "已经下载过了", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(context, "网络不给力", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public void setDownloadFileName(String downloadFileName) {
        // 设置目标存储在外部目录，一般位置可以用
        downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,downloadFileName);
        //downloadRequest.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, downloadFileName);
    }

    public void setNotificationTitle(CharSequence title) {
        //设置notification的标题
        downloadRequest.setTitle(title);

    }


    public void setNotificationDescription(CharSequence description) {
        //设置notification的描述
        downloadRequest.setDescription(description);
    }

    /**
     * 设置notification显示状态
     * Request.VISIBILITY_VISIBLE：在下载进行的过程中，通知栏中会一直显示该下载的Notification，当下载完成时，该Notification会被移除，这是默认的参数值。
     * Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED：在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，直到用户点击该
     * Notification或者消除该Notification。
     * Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION：只有在下载完成后该Notification才会被显示。
     * Request.VISIBILITY_HIDDEN：不显示该下载请求的Notification。如果要使用这个参数，需要在应用的清单文件中加上android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
     *
     * @param visibility 显示标识
     */
    public void setNotificationVisibility(int visibility) {

        downloadRequest.setNotificationVisibility(visibility);
    }

    public DownloadManager.Request getDownloadRequest() {
        return downloadRequest;
    }
}

