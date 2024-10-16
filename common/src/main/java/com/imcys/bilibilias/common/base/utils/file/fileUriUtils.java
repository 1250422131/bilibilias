package com.imcys.bilibilias.common.base.utils.file;

/**
 * @author:imcys
 * @create: 2022-12-31 15:22
 * @Description: uri文件处理类
 */

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;

//此类来源
//https://blog.csdn.net/qq_26280383/article/details/113995727

public class fileUriUtils {

    private static final String TAG = "fileUriUtils";
    public static String root = Environment.getExternalStorageDirectory().getPath() + "/";
    String Ps = "此类来源：https://blog.csdn.net/qq_26280383/article/details/113995727";


    //转换至uriTree的路径
    public static String changeToUri(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        String path2 = path.replace("/storage/emulated/0/", "").replace("/", "%2F");
        return "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A" + path2;
    }


    //获取指定目录的权限
    public static void startFor(String path, Activity context, int REQUEST_CODE_FOR_DIR) {
        String uri = changeToUri(path);
        Uri parse = Uri.parse(uri);

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);


        intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                        | Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, parse);
        }
        context.startActivityForResult(intent, REQUEST_CODE_FOR_DIR);

    }


}
