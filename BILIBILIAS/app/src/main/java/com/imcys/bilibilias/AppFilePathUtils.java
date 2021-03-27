package com.imcys.bilibilias;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AppFilePathUtils {

    public final String SDUnMount = "存储卡未装载";
    public final String SDError = "存储卡转置异常";
    public final String UsbError = "Usb转置异常";

    private Context mContext;
    private String mPackageName;
    private static final File dataFile = Environment.getDataDirectory();
    private static final File sdcardFile = Environment.getExternalStorageDirectory();

    public AppFilePathUtils(Context context, String packageName) {
        mContext = context;
        mPackageName = packageName;
    }

    public  File getCache() {
        File file;
        try {
            file = new File(mContext.getCacheDir().getPath().replace(mContext.getPackageName(), mPackageName));
        } catch (Exception e) {
            file = new File(dataFile, "/data/" + mPackageName + "/cache");
        }
        //获取内部缓存路径
        return file;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public File getCodeCache() {
        File file;
        try {
            file = new File(mContext.getCodeCacheDir().getPath().replace(mContext.getPackageName(), mPackageName));
            return file;

        } catch (Exception e) {
            file = new File(dataFile, "/data/" + mPackageName + "/code_cache");


        }

//获取内部Code缓存路径
        return file;
    }

    public File getData() {
        File file;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                file = new File(mContext.getDataDir().getPath().replace(mContext.getPackageName(), mPackageName));
            } else {
                PackageInfo packInfo = mContext.getPackageManager().getPackageInfo(mPackageName, PackageManager.GET_UNINSTALLED_PACKAGES);
                ApplicationInfo applicationInfo = packInfo.applicationInfo;
                file = new File(applicationInfo.dataDir);
            }
            return file;

        } catch (Exception e) {
            file = new File(dataFile, "/data/" + mPackageName);


        }

//获取应用数据路径路径
        return file;
    }

    public File getAppPackagSource() {
        File file;
        try {
            file = new File(getPackagSourceDir(mPackageName)).getParentFile();
            return file;

        } catch (Exception e) {
            file = new File(dataFile, "/data/app/" + mPackageName + "-1");


        }

//获取应用数据路径路径//最好返回空判断
        return file;
    }

    public File getAppPackagArm() {
        File file;
        try {

            PackageInfo packInfo = mContext.getPackageManager().getPackageInfo(mPackageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            ApplicationInfo applicationInfo = packInfo.applicationInfo;
            file = new File(applicationInfo.nativeLibraryDir);
            return file;

        } catch (Exception e) {
            file = new File(dataFile, "app/" + mPackageName + "-1/lib");
        }
        //获取Arm文件路径//最好返回空判断

        return file;
    }

    public File getExternalCache() {
        File file;
        try {
            file = new File(mContext.getExternalCacheDir().getPath().replace(mContext.getPackageName(), mPackageName));
            return file;

        } catch (Exception e) {
            file = new File(sdcardFile, "Android/data/" + mPackageName + "/cache");


        }

//获取外部缓存路径

        return file;
    }


    public File getExternal() {
        File file;
        try {
            file = new File(mContext.getExternalFilesDir(null).getParent().replace(mContext.getPackageName(), mPackageName));

            return file;

        } catch (Exception e) {
            file = new File(sdcardFile, "Android/data/" + mPackageName);


        }

//获取外部缓存路径

        return file;
    }

    public File getExternalFiles() {
        File file;
        try {
            file = new File(mContext.getExternalFilesDir(null).getPath().replace(mContext.getPackageName(), mPackageName));


            return file;

        } catch (Exception e) {
            file = new File(sdcardFile, "Android/data/" + mPackageName + "files");


        }

//获取外部文件路径

        return file;
    }

    public File getObb() {
        File file;
        try {
            file = new File(mContext.getObbDir().getPath().replace(mContext.getPackageName(), mPackageName));
            return file;

        } catch (Exception e) {
            file = new File(sdcardFile, "Android/obb/" + mPackageName);


        }

//获取OBB路径

        return file;
    }

    public File getFiles() {
        File file;
        try {
            file = new File(mContext.getFilesDir().getPath().replace(mContext.getPackageName(), mPackageName));
            return file;

        } catch (Exception e) {
            //file=new File(sdcardFile,"Android/data/"+mPackageName+"files");
            file = new File(dataFile, "data/" + mPackageName + "files");

        }

        //获取内部文件路径

        return file;
    }


    public File getSharedPreferences() {
        File file;
        try {
            String SharedPreferencePath = new File(mContext.getFilesDir().getParentFile(), "shared_prefs").getPath().replace(mContext.getPackageName(), mPackageName);
            file = new File(SharedPreferencePath);
            return file;

        } catch (Exception e) {
            file = new File(dataFile, "data/" + mPackageName + "shared_prefs");


        }
        //获取SP文件路径


        return file;
    }


    public File getDataBases() {
        File file;
        try {
            String SharedPreferencePath = new File(mContext.getFilesDir().getParentFile(), "databases").getPath().replace(mContext.getPackageName(), mPackageName);
            file = new File(SharedPreferencePath);
            return file;

        } catch (Exception e) {
            file = new File(dataFile, "data/" + mPackageName + "databases");


        }

        //获取DATABASES文件路径

        return file;
    }

    public File getDataDirectory() {

        return Environment.getDataDirectory();

        //获取DATA根路径


    }

    public File getRootDirectory() {

        return Environment.getRootDirectory();

        //获取系统根路径

    }


    public File getExternalDirectory() {

        return Environment.getExternalStorageDirectory();

        //获取内部存储根路径

    }

    public String getSdCardDirectory() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return SDUnMount;
        } else {

            /**
             * 这一句取的还是内置卡的目录。
             * /storage/emulated/0/Android/data/com.newayte.nvideo.phone/cache
             * 神奇的是，加上这一句，这个可移动卡就能访问了。
             * 猜测是相当于执行了某种初始化动作。
             */
            StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);

            Class<?> storageVolumeClazz;
            try {
                storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
                Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
                Method getPath = storageVolumeClazz.getMethod("getPath");
                Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
                Object result = getVolumeList.invoke(mStorageManager);
                final int length = Array.getLength(result);
                for (int i = 0; i < length; i++) {
                    Object storageVolumeElement = Array.get(result, i);
                    String path = (String) getPath.invoke(storageVolumeElement);
                    if ((Boolean) isRemovable.invoke(storageVolumeElement)) {
                        return path;
                    }

                }
            } catch (Exception e) {

                e.printStackTrace();

            }
            return SDError;
        }
    }

    public String getUsbDirectory() {
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);//storage
        try {
            Method mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumePaths");
            String[] paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
            List<String> resultList = new ArrayList<>(paths.length);

            resultList.addAll(Arrays.asList(paths));

            for (int i = 0; i < resultList.size(); i++) {
                if (resultList.get(i).contains("emulated")) {
                    resultList.remove(i);

                }

                if (resultList.get(i).contains("internal")) {
                    resultList.remove(i);

                }
                if (resultList.get(i).contains(getSdCardDirectory())) {
                    resultList.remove(i);

                }
            }

            if (!resultList.isEmpty()) {
                return resultList.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return UsbError;
    }


    public File getDCIMDirectory() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        //获取公共相册根路径

    }

    public File getPicturesDirectory() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //获取公共图片根路径

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public File getDocumentsDirectory() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        //获取公共文档根路径

    }

    public File getDownloadDirectory() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        //获取公共下载根路径

    }

    public File getMoviesDirectory() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

        //获取公共电影根路径

    }

    public File getMusicDirectory() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        //获取公共音乐根路径

    }

    public File getPodcastsDirectory() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);

        //获取公共系统广播根路径

    }


    public File getRingtonesDirectory() {

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);

        //获取公共系统铃声根路径

    }

    public File getAlarmsDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);


        //获取公共系统提醒铃声根路径

    }

    public File getNotificationsDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS);


        //获取公共系统通知铃声根路径

    }

    private String getPackagSourceDir(String PackageName) {

        ApplicationInfo localPackageInfo = getApplicationInfo(PackageName);
        return localPackageInfo == null ? null : localPackageInfo.sourceDir;


    }
    private ApplicationInfo getApplicationInfo(String PackageName) {
        try {
            return mContext.getPackageManager().getApplicationInfo(PackageName, PackageManager.GET_META_DATA);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    // 递归方式 计算文件的大小
    public static long getTotalSizeOfFilesInDir(final File file) {
        if (file.isFile())
            return file.length();
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null)
            for (final File child : children)
                total += getTotalSizeOfFilesInDir(child);
        return total;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }



}