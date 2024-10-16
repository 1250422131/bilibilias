package com.imcys.bilibilias.common.base.utils.file;

/**
 * @author:imcys
 * @create: 2022-12-30 16:17
 * @Description:
 */

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

public class AppFilePathUtils {

    private static final File dataFile = Environment.getDataDirectory();
    private static final File sdcardFile = Environment.getExternalStorageDirectory();
    public final String SDUnMount = "存储卡未装载";
    public final String SDError = "存储卡转置异常";
    public final String UsbError = "Usb转置异常";
    private final Context mContext;
    private final String mPackageName;

    public AppFilePathUtils(Context context, String packageName) {
        mContext = context;
        mPackageName = packageName;
    }

    /**
     * 判断手机是否安装某个应用
     *
     * @param context
     * @param appPackageName 应用包名
     * @return true：安装，false：未安装
     */
    public static boolean isInstallApp(Context context, String appPackageName) {
        return context.getPackageManager().getLaunchIntentForPackage(appPackageName) != null;
    }

    /**
     * 获取文件大小
     *
     * @param path 路径
     * @return 大小
     */
    public static int getFileSize(String path) {
        FileInputStream fis = null;
        int Size = 0;
        try {
            File f = new File(path);
            fis = new FileInputStream(f);
            Size = fis.available();
            fis.close();
        } catch (Exception ignored) {

        }
        return Size;

    }

    /**
     * 复制单个文件
     * 代码来源：https://blog.csdn.net/u013642500/article/details/80067680
     *
     * @param oldPath$Name String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
     * @param newPath$Name String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
     * @return <code>true</code> if and only if the file was copied;
     * <code>false</code> otherwise
     */
    public static boolean copyFile(String oldPath$Name, String newPath$Name) {
        try {
            File oldFile = new File(oldPath$Name);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }
            /* 如果不需要打log，可以使用下面的语句
            if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
                return false;
            }
            */
            FileInputStream fileInputStream = new FileInputStream(oldPath$Name);
            FileOutputStream fileOutputStream = new FileOutputStream(newPath$Name);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 复制文件 适配安卓Q及以上
     *
     * @param oldPath$Name 原路径 + 文件名称
     * @param uri          被覆盖文件/文件夹
     * @return
     */
    public static boolean copySafFile(String oldPath$Name, Uri uri, Context context) {
        File oldFile = new File(oldPath$Name);

        if (!oldFile.exists()) {
            Log.e("--Method--", "copyFile: oldFile does not exist.");
            return false;
        }

        if (!oldFile.isFile()) {
            Log.e("--Method--", "copyFile: oldFile is not a file.");
            return false;
        }

        if (!oldFile.canRead()) {
            Log.e("--Method--", "copyFile: oldFile cannot be read.");
            return false;
        }

        try (InputStream inputStream = new FileInputStream(oldFile);
             OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {

            if (outputStream == null) {
                Log.e("--Method--", "copyFile: outputStream is null.");
                return false;
            }

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("--Method--", "copySafFile: " + e.getMessage());
            return false;
        }
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
}