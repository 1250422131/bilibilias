package com.imcys.bilibilias.as;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class DownloadManagerUtil {
    private Context mContext;

    public DownloadManagerUtil(Context context) {
        mContext = context;
    }

    public long download(String url, String title, String desc,String name,String cookie,String aid) {
        Uri uri = Uri.parse(url);
        DownloadManager.Request req = new DownloadManager.Request(uri);
        //设置WIFI下进行更新，默认是所有网络都支持
        //        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //下载中和下载完后都显示通知栏
        req.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:67.0) Gecko/20100101 Firefox/67.0");
        req.addRequestHeader("referer", "https://www.bilibili.com/video/av" + aid + "/");
        req.addRequestHeader("Cookie", cookie);
        /**
         * 设置notification显示状态
         * Request.VISIBILITY_VISIBLE：在下载进行的过程中，通知栏中会一直显示该下载的Notification，当下载完成时，该Notification会被移除，这是默认的参数值。
         * Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED：在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，直到用户点击该
         * Notification或者消除该Notification。
         * Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION：只有在下载完成后该Notification才会被显示。
         * Request.VISIBILITY_HIDDEN：不显示该下载请求的Notification。如果要使用这个参数，需要在应用的清单文件中加上android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
         */
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //使用系统默认的下载路径 此处为应用内 /android/data/packages ,所以兼容7.0
        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/com.imcys.bilibilias", name);
        //通知栏标题
        req.setTitle(title);
        //通知栏描述信息
        req.setDescription(desc);
        //设置类型为.apk
        req.setMimeType("flv-application/octet-stream");
        //获取下载任务ID
        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        long requestId = dm.enqueue(req);
        //查询下载信息
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(requestId);
        return requestId;
    }

    /**
     * 下载前先移除前一个任务，防止重复下载
     *
     * @param downloadId
     */
    public void clearCurrentTask(long downloadId) {
        DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            dm.remove(downloadId);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }
}