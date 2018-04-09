package com.tajiang.ceo.common.DBUtils;

/**
 * Created by SQL on 2016/7/28.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.utils.LogUtils;
import com.tajiang.ceo.common.utils.SDCardUtils;
import com.tajiang.ceo.common.utils.ToastUtils;

public class DBManager {

    private static DBManager dbManager = null;

    //数据库存储路径
    String fileRootPath =(SDCardUtils.isSDCardEnable()? SDCardUtils.getSDCardPath()
            : SDCardUtils.getRootDirectoryPath() )
            + "/ceo_database/";

    private DBManager(){}

    public static DBManager getInstance() {
        synchronized (DBManager.class) {
            if (dbManager == null) {
                dbManager = new DBManager();
            }
        }
        return dbManager;
    }

    public SQLiteDatabase openDatabase(Context context, String dbFileName) {
        if (SDCardUtils.isSDCardEnable()) {
            LogUtils.e("SDCard可用");
        } else {
            LogUtils.e("SDCard不可用");
        }
        File jhPath = new File(fileRootPath, dbFileName);
        //查看数据库文件是否存在
        if (jhPath.exists()) {
            //存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        } else {
            //不存在先创建文件夹
            File path = new File(fileRootPath);
            if (!path.exists()) {
                if (path.mkdirs()) {
                    LogUtils.i("创建成功");
                } else {
                    LogUtils.e("创建失败");
                }
            }
            try {
                jhPath.createNewFile();
                //得到数据库的输入流
                InputStream is = context.getResources().getAssets().open(DBHelper.DB_FILE_NAME);
                //用输出流写到SDcard上面
                FileOutputStream fos = new FileOutputStream(jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                //最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            //如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
            return openDatabase(context, dbFileName);
        }
    }
}
