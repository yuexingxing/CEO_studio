package com.tajiang.ceo.common.utils;

/**
 * Created by Administrator on 2016/8/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tajiang.ceo.R;
import com.tajiang.ceo.common.widget.dialog.DownAppDialog;
import com.tajiang.ceo.common.widget.dialog.UpDataTipDialog;
import com.tajiang.ceo.common.widget.progressbar.RoundCornerProgressBar;
import com.tajiang.ceo.model.AppInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManagerUtil {

    private final String TAG = this.getClass().getSimpleName();

    private static final int DOWNLOADING = 1;
    private static final int DOWNLOAD_FINISH = 2;

    private AppInfo appInfo;

    private boolean isForce = false;

    private RoundCornerProgressBar progress_version;
    private TextView tv_progress;

    private int progress;
    private boolean cancelUpdate = false;

    private String descriptionVision;

    private String apkUrl;
    private String fileName = "LeiFeng.apk";
    private String mSavePath = Environment.getExternalStorageDirectory() + File.separator + "download";

    private Context mContext;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOADING:
                    progress_version.setProgress(progress);
                    tv_progress.setText(progress + "%");
                    break;
                case DOWNLOAD_FINISH:
                    installApk();
                    break;
            }
        }
    };
    private DownAppDialog cDialog;


    public UpdateManagerUtil(Context context, AppInfo appInfo) {
        this.mContext = context;
        this.appInfo = appInfo;
        this.apkUrl = appInfo.getUrl();
        this.isForce = appInfo.getForcedVersion() > getVersionCode(context);
        this.descriptionVision = appInfo.getDescription();
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate() {
        if (isUpdate()) {
            // 显示提示对话框
            showNoticeDialog();
        } else {
            LogUtils.d(TAG, "已经是最新版了");
        }
    }

    /**
     * 检查软件是否有更新版本
     */
    private boolean isUpdate() {
        return getVersionCode(mContext) < appInfo.getLastVersion();
    }

    /**
     * 获取软件版本号
     */
    private int getVersionCode(Context context) {
        return AppUtils.instance(context).getVersionCode();
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {
        final UpDataTipDialog cDialog = new UpDataTipDialog(mContext, isForce, descriptionVision);
        cDialog.setOnUpDataClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpData();
                cDialog.dismiss();
            }
        });
        cDialog.show();
    }

    /**
     * 下载文件
     */
    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();

    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Log.e("下载地址", mSavePath);

                    URL url = new URL(apkUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);

                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }

                    File apkFile = new File(mSavePath, fileName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOADING);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);

                    } while (!cancelUpdate);// 点击取消就停止下载
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            cDialog.dismiss();
        }
    }

    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(mSavePath, fileName);
        // 通过Intent安装APK文件
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(intent);

    }

    /**
     * 执行更新操作
     */
    private void doUpData() {
        cDialog = new DownAppDialog(mContext);
        tv_progress = cDialog.findChildViewById(R.id.tv_progress);
        progress_version = cDialog.findChildViewById(R.id.progress_version);
        cDialog.show();
        // 开始下载文件
        downloadApk();
//        }
    }

    /**
     * 判断是否已经下载
     */
    private boolean hasDownLoad() {
        File file = new File(mSavePath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
