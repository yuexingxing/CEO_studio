package com.tajiang.ceo.scan;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.tajiang.ceo.R;
import com.tajiang.ceo.common.fragment.BaseFragment;
import com.tajiang.ceo.common.utils.CommandTools;
import com.tajiang.ceo.common.utils.ToastUtils;
import com.zxing.view.CameraManager;
import com.zxing.view.CaptureActivityHandler;
import com.zxing.view.InactivityTimer;
import com.zxing.view.PlanarYUVLuminanceSource;
import com.zxing.view.RGBLuminanceSource;
import com.zxing.view.Utils;
import com.zxing.view.ViewfinderView;

import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import butterknife.OnClick;

/**
 * Created by Administrator on 2017-07-31.
 */

public class ScanerFragment extends BaseFragment implements SurfaceHolder.Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private static final int REQUEST_CODE = 234;
    private String photo_path;
    private Bitmap scanBitmap;
    private View mView;

    private Timer timer;
    private SurfaceHolder surfaceHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mView = rootView;

        CameraManager.init(getActivity());
        viewfinderView = (ViewfinderView) mView.findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(getActivity());
        initView();
        return rootView;
    }

    @Override
    protected void initTopBar() {

        disableNav();
    }

    @Override
    protected void initLayout() {
        setContentView(R.layout.camera);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) mView.findViewById(R.id.preview_view);
        surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }

        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        String resultString = result.getText();

        ToastUtils.showShort(resultString + "");
        if (resultString.equals("")) {
            //扫描失败
            Toast.makeText(getActivity(), "Scan failed!", Toast.LENGTH_SHORT).show();
        }else {
//			System.out.println("Result:"+resultString);
            Intent resultIntent = new Intent(getActivity(), GetGoodsActivity.class);
            resultIntent.putExtra("orderId", resultString);
            startActivity(resultIntent);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {

        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception ioe) {
            return;
        }

        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private static final long VIBRATE_DURATION = 200L;


    /*............选取相册的功能..................*/
    private void initView() {

        //选取相册中的二维码
        mView.findViewById(R.id.mo_scanner_photo).setOnClickListener(

                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        photo();
                    }
                });

        timer = new Timer(true);
        timer.schedule(task, 1000, 3000); //延时1000ms后执行，3000ms执行一次
    }

    private void photo() {

        // 激活系统图库，选择一张图片
        // 激活系统图库，选择一张图片
        Intent innerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

//		Intent innerIntent = new Intent(); // "android.intent.action.GET_CONTENT"
//		if (Build.VERSION.SDK_INT < 19) {
//			innerIntent.setAction(Intent.ACTION_GET_CONTENT);
//		} else {
//			innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//		}
//		// innerIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//		innerIntent.setType("image/*");

        Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
        startActivityForResult(wrapperIntent, REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            switch (requestCode) {

                case REQUEST_CODE:

                    String[] proj = { MediaStore.Images.Media.DATA };
                    // 获取选中图片的路径
                    Cursor cursor = getActivity().getContentResolver().query(data.getData(),
                            proj, null, null, null);

                    if (cursor.moveToFirst()) {

                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        photo_path = cursor.getString(column_index);
                        if (photo_path == null) {
                            photo_path = Utils.getPath(getActivity().getApplicationContext(),
                                    data.getData());
                            Log.i("123path  Utils", photo_path);
                        }

                        Log.i("123path", photo_path);
                    }

                    cursor.close();

                    Result result = scanningImage(photo_path);
                    // String result = decode(photo_path);

                    if (result == null) {
                        Log.i("123", "   -----------");
                        Toast.makeText(getActivity().getApplicationContext(), "图片格式有误", Toast.LENGTH_LONG)
                                .show();
                        Looper.loop();
                    } else {

                        Log.i("123result", result.toString());
                        Intent resultIntent = new Intent(getActivity(), GetGoodsActivity.class);
                        resultIntent.putExtra("orderId", result.toString());
                        startActivity(resultIntent);
                    }
                    break;

            }

        }

    }

    // TODO: 解析部分图片
    protected Result scanningImage(String path) {

        if (TextUtils.isEmpty(path)) {
            return null;
        }

        // DecodeHintType 和EncodeHintType
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 先获取原大小
        scanBitmap = BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false; // 获取新的大小

        int sampleSize = (int) (options.outHeight / (float) 200);

        if (sampleSize <= 0)
            sampleSize = 1;
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);

        // --------------测试的解析方法---PlanarYUVLuminanceSource-这几行代码对project没作功----------

        LuminanceSource source1 = new PlanarYUVLuminanceSource(
                rgb2YUV(scanBitmap), scanBitmap.getWidth(),
                scanBitmap.getHeight(), 0, 0, scanBitmap.getWidth(),
                scanBitmap.getHeight());
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                source1));
        MultiFormatReader reader1 = new MultiFormatReader();
        Result result1;
        try {
            result1 = reader1.decode(binaryBitmap);
            String content = result1.getText();
            Log.i("123content", content);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // ----------------------------

        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {

            return reader.decode(bitmap1, hints);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    /**
     * 中文乱码
     *
     * 暂时解决大部分的中文乱码 但是还有部分的乱码无法解决 .
     * @return
     */
    private String recode(String str) {
        String formart = "";

        try {
            boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
                    .canEncode(str);
            if (ISO) {
                formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
                Log.i("1234      ISO8859-1", formart);
            } else {
                formart = str;
                Log.i("1234      stringExtra", str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formart;
    }

    /**
     * //TODO: TAOTAO 将bitmap由RGB转换为YUV //TOOD: 研究中
     *
     * @param bitmap
     *            转换的图形
     * @return YUV数据
     */
    public byte[] rgb2YUV(Bitmap bitmap) {
        // 该方法来自QQ空间
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[i * width + j] & 0x00FFFFFF;

                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;

                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);

                yuv[i * width + j] = (byte) y;
                // yuv[len + (i >> 1) * width + (j & ~1) + 0] = (byte) u;
                // yuv[len + (i >> 1) * width + (j & ~1) + 1] = (byte) v;
            }
        }
        return yuv;
    }

    TimerTask task = new TimerTask(){

        public void run() {

            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    };

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg){

            if(mHandler == null || msg == null){
                return;
            }

            msg.what = R.id.restart_preview;
            if(handler != null){
                handler.dispatchMessage(msg);
            }
        }
    };

    @OnClick(R.id.camera_flash)
    public void flash() {

        CommandTools.openFlash(CameraManager.get().getCamera(), surfaceHolder);
    }
}
