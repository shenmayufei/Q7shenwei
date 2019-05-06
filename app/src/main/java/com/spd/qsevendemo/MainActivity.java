package com.spd.qsevendemo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.serialport.DeviceControlSpd;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjimi.api.iminect.ImiCameraIntrinsic;
import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiNect;
import com.hjimi.api.iminect.ImiPixelFormat;
import com.hjimi.api.iminect.ImiPropertIds;
import com.imi.sdk.base.CameraIntrinsics;
import com.imi.sdk.volume.BoxSize;
import com.imi.sdk.volume.BoxState;
import com.imi.sdk.volume.Plane;
import com.socks.library.KLog;
import com.spd.qsevendemo.bean.BalanceBean;
import com.spd.qsevendemo.bean.BalanceResult;
import com.spd.qsevendemo.measure.CalThread;
import com.spd.qsevendemo.measure.Constants;
import com.spd.qsevendemo.measure.MeasureListener;
import com.spd.qsevendemo.measure.helper.MeasureHelper;
import com.spd.qsevendemo.measure.helper.PermissionHelper;
import com.spd.qsevendemo.measure.helper.PlaneHelper;
import com.spd.qsevendemo.measure.helper.SharedPreferenceHelper;
import com.spd.qsevendemo.measure.utils.FileUtils;
import com.spd.qsevendemo.measure.utils.ScreenUtils;
import com.spd.qsevendemo.measure.view.GLPanel;
import com.spd.qsevendemo.measure.view.MessageDialog;
import com.spd.qsevendemo.model.DataBean;
import com.spd.qsevendemo.model.DatabaseAction;
import com.spd.qsevendemo.model.SevenBean;
import com.spd.qsevendemo.net.NetApi;
import com.spd.qsevendemo.utils.Logcat;
import com.spd.qsevendemo.utils.SpUtils;
import com.spd.qsevendemo.utils.ToastUtils;
import com.spd.qsevendemo.view.EndWindow;
import com.spd.qsevendemo.view.JianshuShow;
import com.spd.qsevendemo.view.TijiShow;
import com.spd.qsevendemo.view.WorklistShow;
import com.spd.qsevendemo.weight.UploadEvent;
import com.spd.qsevendemo.weight.WeightEvent;
import com.spd.qsevendemo.weight.WeightInterface;
import com.spd.qsevendemo.weight.WeightRealize;
import com.speedata.libutils.DataConversionUtils;
import com.speedata.utils.ProgressDialogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.hjimi.api.iminect.ImiPropertIds.IMI_PROPERTY_DEPTH_MIRROR;
import static com.hjimi.api.iminect.ImiPropertIds.IMI_PROPERTY_IMAGE_MIRROR;
import static com.spd.code.CodeUtils.UnloadSD;
import static com.spd.qsevendemo.model.LoginModel.LOGIN_IS_MANAGER;
import static com.spd.qsevendemo.model.SevenModel.HEIGHT_CALIBRATION;
import static com.spd.qsevendemo.model.SevenModel.HEIGHT_INIT;
import static com.spd.qsevendemo.model.SevenModel.HEIGHT_SET;
import static com.spd.qsevendemo.model.SevenModel.PHOTO_SET;
import static com.spd.qsevendemo.model.SevenModel.PHOTO_SHOOT;
import static com.spd.qsevendemo.model.SevenModel.POWEROFF;
import static com.spd.qsevendemo.model.SevenModel.POWERON;
import static com.spd.qsevendemo.model.SevenModel.SCAN_SET;
import static com.spd.qsevendemo.model.SevenModel.UPLOAD_DATA;
import static com.spd.qsevendemo.model.SevenModel.WEIGHT;
import static com.spd.qsevendemo.model.SevenModel.WEIGHT_SET;
import static com.spd.qsevendemo.model.SevenModel.WEIGHT_STABLE;
import static com.spd.qsevendemo.model.SevenModel.ZERO;
import static com.spd.qsevendemo.utils.HnweUtils.SD_PROP_C128_ENABLED;
import static com.spd.qsevendemo.utils.HnweUtils.SD_PROP_C39_ENABLED;
import static com.spd.qsevendemo.utils.HnweUtils.SD_PROP_QR_ENABLED;
import static com.spd.qsevendemo.utils.HnweUtils.SD_PROP_UPC_ENABLED;
import static com.spd.qsevendemo.utils.Utils.copyAssetsFile2Phone;
import static com.spd.qsevendemo.utils.Utils.inputId;

/**
 * @author xuyan  主页面
 */
@SuppressWarnings("AlibabaAvoidUseTimer")
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener, MeasureListener<BoxSize> {

    /**
     * 设置和码制设置的列表
     */
    private ImageView mImageView;
    private List<SevenBean> mList;

    /**
     * 下方三块数据依次排列
     */
    private WorklistShow mWeight;
    private TijiShow mVolume;
    private WorklistShow mCode;
    /**
     * title上的3个要显示的camera，scale，count
     */
    private TextView mOneCamera;
    private TextView mTwoScale;
    private TextView mThreeNumber;
    private int count = 0;

    /**
     * 中间三块数据依次排列
     */
    private JianshuShow mJianshu;
    private WorklistShow mLiebiao;
    private WorklistShow mShangchuan;

    private BalanceBean mBalanceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //风扇及补光灯
        initDevice();
        initView();
        setWorkShows();
        initScan();
        acquireWakeLock();
        //称重
        initLibz();
        openWeight();

        //神威不需要体积
        initPermission();
//        initTijiView();
        setShenweiShows();
        SpUtils.put(AppSeven.getInstance(), WEIGHT_STABLE, false);
        initButtons();
    }


    private void initButtons() {
        mWeight.setOnClickListener(v -> {
            weightInterface.sendCmd(DataConversionUtils.HexString2Bytes(ZERO));
            mWeight.setBackground(true);
            mWeight.setShow("0.00");
        });

        mLiebiao.setOnClickListener(v -> {
            if ("0.0".equals(mWeight.getShow())) {
                startActivity(new Intent(MainActivity.this, DataActivity.class));
            } else {
                ToastUtils.showShortToastSafe(R.string.please);
            }
        });

        mShangchuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().postSticky(new UploadEvent(UPLOAD_DATA));
            }
        });

    }


    private DeviceControlSpd deviceControl;

    private void initDevice() {
        try {
            deviceControl = new DeviceControlSpd(DeviceControlSpd.POWER_NEWMAIN);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void initView() {
        mList = new ArrayList<>();
        mBalanceBean = new BalanceBean();
        mImageView = findViewById(R.id.title_settings);

        mOneCamera = findViewById(R.id.title_camera_state);
        mTwoScale = findViewById(R.id.title_weight_state);
        mThreeNumber = findViewById(R.id.title_number);

        mThreeNumber.setOnClickListener(v -> {
            if ("0.0".equals(mWeight.getShow())) {
                startActivity(new Intent(MainActivity.this, DataActivity.class));
            } else {
                ToastUtils.showShortToastSafe(R.string.please);
            }
        });

        mWeight = findViewById(R.id.work_one);
        mVolume = findViewById(R.id.work_two);
        mCode = findViewById(R.id.work_three);

        //中间三个，件数、列表、上传
        mJianshu = findViewById(R.id.shenwei_one);
        mLiebiao = findViewById(R.id.shenwei_two);
        mShangchuan = findViewById(R.id.shenwei_three);

        mImageView.setOnClickListener(v -> new EndWindow(MainActivity.this, mList).showAtLocation(mImageView, Gravity.START, 0, 0));


        mList = DatabaseAction.queryShowContent();

        if (mList != null && mList.size() > 0) {
            Logcat.d(Objects.requireNonNull(mList));
        }

        if (mList == null || mList.size() == 0) {
            mList = getSevenList();
            DatabaseAction.savePrepareBox(mList);
        }

        //注册EventBus
        EventBus.getDefault().register(this);

        mLayout = findViewById(R.id.ll_add_one);
    }

    private RelativeLayout mLayout;

    private void setWorkShows() {
        mWeight.setTotalName(getString(R.string.real_weight));
        mWeight.setShow("0.00");
        mWeight.setUnit("KG");

        mVolume.setTotalName(getString(R.string.volume_identification));
        mVolume.setShow("0.0000");
        mVolume.setUnit("m³");

        mCode.setTotalName(getString(R.string.barcode_recognition));
        mCode.setShow("");
        mCode.setUnit("");

    }

    private void setShenweiShows() {
        mJianshu.setTotalName("件数");
        mJianshu.setShow("1");
        mJianshu.setUnit("");

        mLiebiao.setTotalName("数据列表");
        mLiebiao.setShow("查看数据列表");
        mLiebiao.setUnit("");

        mShangchuan.setTotalName("上传");
        mShangchuan.setShow("手动上传数据");
        mShangchuan.setUnit("");
    }

    private List<SevenBean> getSevenList() {
        List<SevenBean> mList = new ArrayList<>();
        for (int i = 0; i < codename.length; i++) {
            SevenBean sevenBean = new SevenBean();
            sevenBean.setName(codename[i]);
            sevenBean.setCode(codenumber[i]);
            sevenBean.setCheck(false);
            mList.add(sevenBean);
        }
        return mList;
    }

    /**
     * 要保存的条码及对应int值以及状态。直接整个保存。
     */
    private String[] codename = {"CODE128", "CODE39", "UPC", "QR"};

    private int[] codenumber = {SD_PROP_C128_ENABLED, SD_PROP_C39_ENABLED, SD_PROP_UPC_ENABLED, SD_PROP_QR_ENABLED};


    //返回键监听
    private long mkeyTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.ACTION_DOWN:
                if ((System.currentTimeMillis() - mkeyTime) > 2000) {
                    mkeyTime = System.currentTimeMillis();
                    boolean cn = "CN".equals(getApplicationContext().getResources().getConfiguration().locale.getCountry());
                    if (cn) {
                        ToastUtils.showShortToastSafe("再次点击返回退出");
                    } else {
                        ToastUtils.showShortToastSafe("Press the exit again");
                    }
                } else {
                    try {
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 初始化扫描相关
     */
    Camera camera = null;
    SurfaceView surfaceview;
    SurfaceHolder surfaceholder;
    boolean mScaning = false;
    List<Camera.Size> mPictureSize = null;
    List<Camera.Size> mPreviewSize = null;
    Camera.Parameters mParams = null;
    String[] mPictureSizeString = null;
    String[] mPreviewSizeString = null;
    /**
     * 选择camera
     */
    int cam = 0;
    boolean cansw = false;
    List<Integer> mZoomRatios = null;
    int[] zoom_ratio = {10, 15, 20, 30};
    int zoom_index = 0;
    SoundPool soundPool;
    int soundId;
    int soundId2;
    int max_zoom = 0;
    int g_w, g_h;
    boolean isdecode = false;

    /**
     * init
     */
    private void initScan() {
        surfaceview = findViewById(R.id.surfaceView1);
        surfaceholder = surfaceview.getHolder();
        surfaceholder.addCallback(this);

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load("/system/media/audio/ui/VideoRecord.ogg", 0);
        soundId2 = soundPool.load("/system/media/audio/ui/VideoStop.ogg", 1);

        copyAssetsFile2Phone(this);
        inputId(this);

        if (!com.spd.code.CodeUtils.SD_Loaded) {

            if (com.spd.code.CodeUtils.LoadSD() == 1) {
                ToastUtils.showShortToastSafe("SwiftDecoder Loaded");
                Logcat.d("SwiftDecoder Loaded");

                for (int i = 0; i < mList.size(); i++) {
                    if (mList.get(i).getCheck()) {
                        com.spd.code.CodeUtils.CodeEnable(mList.get(i).getCode());
                    } else {
                        com.spd.code.CodeUtils.CodeDisable(mList.get(i).getCode());
                    }
                }
                com.spd.code.CodeUtils.SD_Loaded = true;
            } else {
                ToastUtils.showShortToastSafe("SwiftDecoder Not Loaded");
                Logcat.d("SwiftDecoder Not Loaded");
                com.spd.code.CodeUtils.SD_Loaded = false;
            }
        }

    }

    private void startScan() {

        //暂时先1920*1080的预览分辨率
//        mParams.setPreviewSize(1920, 1080);
//        g_w = 1920;
//        g_h = 1080;
        //500W
//        mParams.setPreviewSize(2560, 1920);
//        g_w = 2560;
//        g_h = 1920;
        //800W
        mParams.setPreviewSize(3264, 2448);
        g_w = 3264;
        g_h = 2448;
        camera.setParameters(mParams);

        mScaning = true;
        camera.setPreviewCallback(mCallback);
        camera.startPreview();

        if (cam == 0) {
            try {
                camera.setPreviewDisplay(surfaceholder);
                camera.autoFocus((success, camera) -> {
                    Logcat.d("initcamera  setPreviewDisplay, autoFocus");
                });
            } catch (IOException e) {
                // TODO Auto-generated catch block
                camera.release();
                camera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        openCloseFlash("en");
        openCloseFlash("on");

        mOneCamera.setText(R.string.title_camera_state_true);
        //开始预览后，自动对焦

//        camera.autoFocus((success, camera) -> {
//            if (success) {
//                Logcat.d("auto foucs ok");
//            }
//        });

    }


    @Override
    protected void onDestroy() {
        ProgressDialogUtils.dismissProgressDialog();
        com.spd.code.CodeUtils.SD_Loaded = false;
        UnloadSD();

        //称重
        if (flag == 1) {
            weightInterface.releaseWeightDev();
        }
        //体积测量
//        if (mCalThread != null) {
//            mCalThread.onDestroy();
//        }
//
//        fixedThreadPool.shutdownNow();
//        mImiDevice.close();
//        mImiDevice = null;
//        ImiDevice.destroy();
//        ImiNect.destroy();
        releaseWakeLock();
        //称重等
        try {
            deviceControl.newSetGpioOff(14);
            deviceControl.newSetGpioOff(89);
            deviceControl.newSetGpioOff(16);
            deviceControl.newSetGpioOff(57);
            deviceControl.newSetGpioOff(21);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        mCallback = null;
        soundPool.release();
        super.onDestroy();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (camera == null) {
            camera = init_camera(cam);
        }
        startScan();
    }


    @SuppressLint("DefaultLocale")
    private Camera init_camera(int id) {
        Camera tca = Camera.open(id);
        if (tca == null) {
            return null;
        }
        mParams = tca.getParameters();
        mPictureSize = mParams.getSupportedPictureSizes();
        Camera.Size tcs = mParams.getPictureSize();
        mPictureSizeString = new String[mPictureSize.size()];
        Logcat.d("dismission size is " + mPictureSize.size());
        Camera.Size dfs = mParams.getPictureSize();
        Logcat.d("now use pic size " + dfs.width + " x " + dfs.height);
        for (int i = 0; i < mPictureSize.size(); i++) {
            tcs = mPictureSize.get(i);
            Logcat.d(String.format("%d: h:%d w:%d\n", i, tcs.height, tcs.width));
            mPictureSizeString[i] = String.format("%d x %d", tcs.width, tcs.height);
        }
        mParams.setPictureSize(tcs.width, tcs.height);
        List<Integer> li = mParams.getSupportedPreviewFormats();
        Logcat.d("now use preview format is " + mParams.getPreviewFormat());
        for (int i = 0; i < li.size(); i++) {
            Logcat.d("supported preview format " + String.format("%x", li.get(i)));
        }
        mParams.setPreviewFormat(ImageFormat.YV12);
        mPreviewSize = mParams.getSupportedPreviewSizes();
        Camera.Size cs = mParams.getPreviewSize();
        Logcat.d("now use preview size is " + cs.width + " x " + cs.height);
        mPreviewSizeString = new String[mPreviewSize.size()];
        for (int i = 0; i < mPreviewSize.size(); i++) {
            tcs = mPreviewSize.get(i);
            Logcat.d("supported preview size " + tcs.width + " x " + tcs.height);
            mPreviewSizeString[i] = String.format("%d x %d", tcs.width, tcs.height);
        }
        mParams.setPreviewSize(tcs.width, tcs.height);
        g_w = tcs.width;
        g_h = tcs.height;


        /*
         *  霍尼部分camera的设置
         */
        mParams.set("iso", "800");
        List<String> focusModes = mParams.getSupportedFocusModes();
        if (focusModes != null) {
            if (focusModes.contains("macro")) {
                mParams.setFocusMode("macro");
            } else if (focusModes.contains("auto")) {
                mParams.setFocusMode("auto");
            }
        }
        if (cam == 0) {
            mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

        } else {
            mParams.setFocusMode("macro");
        }

        mParams.setJpegQuality(100);
        mParams.setColorEffect(Camera.Parameters.EFFECT_MONO);


        if (mParams.isZoomSupported()) {
            max_zoom = mParams.getMaxZoom();

            mZoomRatios = mParams.getZoomRatios();
            if (mZoomRatios != null) {
                StringBuilder xsss = new StringBuilder();
                for (int i = 0, j = 0; i < mZoomRatios.size(); i++) {
                    xsss.append((float) mZoomRatios.get(i) / 100).append(", ");
                    if ((j < zoom_ratio.length) && (mZoomRatios.get(i) / 10 == zoom_ratio[j])) {
                        zoom_ratio[j] = i;
                        Logcat.d("jk", "zoom_ratio[" + j + "] is " + ((float) mZoomRatios.get(i) / 100));
                        j++;
                    }
                }

            }
        }

        if (mParams.isAutoExposureLockSupported()) {

            Logcat.d("jk", "auto exposure lock is set to " + mParams.getAutoExposureLock());
        } else {
            Logcat.d("jk", "not support to lock auto exposure");
        }

        if (mParams.isAutoWhiteBalanceLockSupported()) {
            mParams.setAutoWhiteBalanceLock(false);
            Logcat.d("jk", "auto wb lock is set to " + mParams.getAutoWhiteBalanceLock());
        } else {
            Logcat.d("jk", "not support to lock auto wb");
        }


        mParams.setExposureCompensation(-1);
        //mParams.setAutoExposureLock(true);

        tca.setParameters(mParams);

        mParams = tca.getParameters();
        //转90度
        tca.setDisplayOrientation(270);

        dfs = mParams.getPictureSize();
        Logcat.d("max expose is " + mParams.getMaxExposureCompensation() + " min expose is " + mParams.getMinExposureCompensation());
        Logcat.d("now use pic size " + dfs.width + " x " + dfs.height);
        Logcat.d("now use preview format is " + mParams.getPreviewFormat());
        cs = mParams.getPreviewSize();
        Logcat.d("now use preview size is " + cs.width + " x " + cs.height);

        //tca.setDisplayOrientation(270);

        Logcat.d(String.format("%s camera config used now\npreview %d x %d\npic %d x %d",
                (id == 0 ? "back" : "front"), cs.width, cs.height, dfs.width, dfs.height));

        try {
            tca.setPreviewDisplay(surfaceholder);
        } catch (IOException e) {

            tca.release();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tca;
    }


    private String barcode = "";

    private Camera.PreviewCallback mCallback = (new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {

            if (!isdecode) {
                isdecode = true;

                //添加扫描判断
                if (!(boolean) SpUtils.get(AppSeven.getInstance(), SCAN_SET, true)) {
                    isdecode = false;
                    return;
                }

                if (com.spd.code.CodeUtils.DecodeImageSD(data, g_w, g_h) == 0) {
                    Logcat.d("jk decode failed");
                } else {

                    byte[][] resary;
                    resary = com.spd.code.CodeUtils.GetResultSD();

                    if (resary != null) {

                        if (soundPool != null && (boolean) SpUtils.get(AppSeven.getInstance(), WEIGHT_STABLE, false)) {
                            SpUtils.put(AppSeven.getInstance(), WEIGHT_STABLE, false);
                        } else {
                            isdecode = false;
                            return;
                        }
                        int i = 0;

                        for (byte[] x : resary) {

                            String str = new String(x);
                            if (!"".equalsIgnoreCase(str)) {
                                mCode.setShow(str);
                                Logcat.d("jk", "jk result " + i + " : " + str);
                                i++;
                                count++;
                                mThreeNumber.setText(String.valueOf(count));

                                //和上一条不一样时，保存。先本地保存一条数据

                                if (!str.equals(barcode) && (boolean) SpUtils.get(AppSeven.getInstance(), LOGIN_IS_MANAGER, false)) {

                                    //网络上传数据,先拼装数据
                                    List<String> mList3 = new ArrayList<>();
                                    BalanceBean balanceBean = new BalanceBean();
                                    balanceBean.setWeight(String.valueOf(mLastWeight));
                                    balanceBean.setVolume(0);
                                    balanceBean.setPcs(1);
                                    balanceBean.setRequestType("1");
                                    balanceBean.setScanTime(String.valueOf(System.currentTimeMillis()));
                                    mList3.add(str);
                                    balanceBean.setQrCodes(mList3);
                                    //上传
                                    mBalanceBean = new BalanceBean();
                                    mBalanceBean = balanceBean;
                                    Logcat.d(mBalanceBean.toString());
                                    EventBus.getDefault().postSticky(new UploadEvent(UPLOAD_DATA));

                                }

                                if (!str.equals(barcode)) {
                                    soundPool.play(soundId, 1, 1, 0, 0, 1);
                                    barcode = str;
                                    DataBean dataBean = new DataBean();
                                    dataBean.setTiji("");
                                    dataBean.setBarcode(str);
                                    dataBean.setWeight(String.valueOf(mLastWeight));
                                    dataBean.setTime(System.currentTimeMillis());
                                    //默认件数为1
                                    dataBean.setCount("1");
                                    DatabaseAction.saveData(dataBean);

                                }

                                //条码触发体积测量
//                                if (!barcode.equals(str)) {
//                                    //触发体积测量
//                                    if (show) {
//                                        initScanTime();
//                                    }
//                                    barcode = str;
//                                }

                            }
                        }
                    } else {
                        Logcat.d("jk", "jk decode str null");
                    }
                }
                isdecode = false;
            } else {
                Logcat.d("jk", "jk skip this frame");
            }
        }
    });

    private void upload(BalanceBean balanceBean) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), balanceBean.toString());

        NetApi.getInstance().upload(requestBody).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<BalanceResult>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BalanceResult result) {
                isupload = false;
                if (result.getFalseCount() == 0) {
                    Logcat.d("上传成功");
                    //EventBus.getDefault().postSticky(new WeightEvent(UPLOAD_RESULT, "上传成功"));
                } else {
                    //EventBus.getDefault().postSticky(new WeightEvent(UPLOAD_RESULT, result.getFalseMsgs().get(0).getMsg()));
                    Logcat.d(result.getFalseMsgs().get(0).getCustomerAndQrCode() + result.getFalseMsgs().get(0).getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                isupload = false;
                Logcat.d(e.getMessage());
                ToastUtils.showShortToastSafe(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private BarcodeDrawView drawView;
    private List<BarcodeBounds> mList2;

    private void initDrawView() {
        int[][] kuang;
        BarcodeBounds barcodeBounds;
        kuang = com.spd.code.CodeUtils.GetBounds();
        if (kuang == null) {
            return;
        }
        mList2 = new ArrayList<>();
        for (int i = 0; i < kuang.length; i++) {
            barcodeBounds = new BarcodeBounds(kuang[i], g_w, g_h);
            mList2.add(barcodeBounds);
        }
        if (drawView != null) {
            mLayout.removeView(drawView);
        }
        drawView = new BarcodeDrawView(this, mList2);
        mLayout.addView(drawView);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Logcat.d("surfaceChanged is called");
        if (camera == null) {
            camera = init_camera(cam);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Logcat.d("surfaceDestroyed is called");
        if (camera != null) {
            if (mScaning) {
                Logcat.d("sfdestory stop preview");
                camera.stopPreview();
                camera.setPreviewCallback(null);
                mScaning = false;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            camera.release();
            isdecode = false;
            camera = null;
        }
    }


    private static BufferedWriter TorchFileWrite;

    /**
     * @param str 数据
     */
    public void openCloseFlash(String str) {
        switch (cam) {
            case 0:
                //后置
//                if (mParams != null) {
//                    if ("on".equals(str)) {
//                        mParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                        camera.setParameters(mParams);
//                    } else {
//                        mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                        camera.setParameters(mParams);
//                    }
//                }
                break;
            case 1:
                //前置

                break;
            case 2:
                //扫头
                Logcat.d("-print-rece-" + "openCloseFlash " + str + " start");
                File TorchFileName = new File("/sys/class/misc/lm3642/torch");
                try {
                    TorchFileWrite = new BufferedWriter(new FileWriter(TorchFileName, false));
                    TorchFileWrite.write(str);
                    TorchFileWrite.flush();
                    TorchFileWrite.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Logcat.d(e.getMessage());
                }
                Logcat.d("-print-rece-" + "openCloseFlash end");
                break;
            default:
                break;
        }
    }


    /**
     * ===================================下面是称重部分，主要方法位置区别于扫描===========================================
     * <p>
     * 整合于weightActivity源码
     */
    private WeightInterface weightInterface;
    private int flag = 0;
    private double mLastWeight = 0;
    private double mLastWeight2 = 0;
    private boolean first = true;

    private List<Double> mWeightList;

    private void initLibz() {
        mWeightList = new ArrayList<>();
        weightInterface = new WeightRealize(AppSeven.getInstance());
        weightInterface.sendCmd(DataConversionUtils.HexString2Bytes("024171333003"));
        weightInterface.setWeightStatas((i, weight) -> runOnUiThread(() -> {
            switch (i) {
                case 0:
                    //秤的状态
//                    mTwoScale.setText(getString(R.string.Electronic_scale_disconnected));
//                    SpUtils.put(AppSeven.getInstance(), WEIGHT_STABLE, false);
                    break;
                case 1:
                    //较为稳定
                    mTwoScale.setText(getString(R.string.title_weight_state_true));
                    if (weight == 0) {
                        //判断是否输出重量
                        if ((boolean) SpUtils.get(AppSeven.getInstance(), WEIGHT_SET, true)) {
                            mWeight.setBackground(true);
                            mWeight.setShow(weight + "");
                            mVolume.setShow("0.0000");
                            mVolume.setTiji("a:0.0cm;  b:0.0cm;  h:0.0cm");
                            SpUtils.put(AppSeven.getInstance(), WEIGHT_STABLE, false);
                            barcode = "";
                        }
                    } else {
                        if ((boolean) SpUtils.get(AppSeven.getInstance(), WEIGHT_SET, true)) {
                            mWeight.setBackground(false);
                            mWeight.setShow(weight + "");
                        }

                        if ((weight - mLastWeight2 <= 0.02) || (weight - mLastWeight2 >= -0.02) || first) {
                            mWeightList.add(weight);
                            int chang = mWeightList.size();
                            boolean shuju = true;
                            if (chang > 3) {
                                for (int j = chang-1; j > chang - 3; j--) {
                                    if (!mWeightList.get(j).equals(mWeightList.get(j - 1))) {
                                        shuju = false;
                                    }
                                }
                                if (shuju && weight > 0.02) {
                                    mLastWeight = weight;
                                    SpUtils.put(AppSeven.getInstance(), WEIGHT_STABLE, true);
                                }
                            }

                            //触发体积测量
//                            if (show) {
//                                initScanTime();
//                            }

                        }
                        mLastWeight2 = weight;
                    }

                    break;
                case 2:

                    //变化中
                    mTwoScale.setText(getString(R.string.title_weight_state_true));
                    if (weight == 0) {
                        //判断是否输出重量
                        if ((boolean) SpUtils.get(AppSeven.getInstance(), WEIGHT_SET, true)) {
                            mWeight.setBackground(true);
                            mWeight.setShow(weight + "");
                            mVolume.setShow("0.0000");
                            mVolume.setTiji("a:0.0cm;  b:0.0cm;  h:0.0cm");
                            SpUtils.put(AppSeven.getInstance(), WEIGHT_STABLE, false);
                            barcode = "";
                        }
                    } else {
                        if ((boolean) SpUtils.get(AppSeven.getInstance(), WEIGHT_SET, true)) {
                            mWeight.setBackground(false);
                            mWeight.setShow(weight + "");
                        }

                        if ((weight - mLastWeight2 <= 0.02) || (weight - mLastWeight2 >= -0.02) || first) {
                            mWeightList.add(weight);
                            int chang = mWeightList.size();
                            boolean shuju = true;
                            if (chang > 5) {
                                for (int j = chang - 1; j > chang - 5; j--) {
                                    if (!mWeightList.get(j).equals(mWeightList.get(j - 1))) {
                                        shuju = false;
                                    }
                                }
                                if (shuju && weight > 0.02) {
                                    mLastWeight = weight;
                                    SpUtils.put(AppSeven.getInstance(), WEIGHT_STABLE, true);
                                }
                            }
                            //触发体积测量
//                            if (show) {
//                                initScanTime();
//                            }

                        }
                        mLastWeight2 = weight;
                    }
                    break;
                default:
                    break;
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //称重
        flag = 0;
        mWeight.setBackground(true);
        mWeight.setShow("0.00");
        openWeight();
        //体积
//        if (mCalThread != null) {
//            mGLCal.onResume();
//            mCalThread.onResume();
//        }
    }

    @Override
    protected void onPause() {
        if (flag == 1) {
            weightInterface.releaseWeightDev();
        }
        ProgressDialogUtils.dismissProgressDialog();
        super.onPause();
    }

    private void openWeight() {
        if (flag == 0) {
            flag = 1;
            weightInterface.initWeight();
        }
    }

    /**
     * 接收事件，1.激活成功开始初始化
     *
     * @param event event
     */

    @SuppressLint("WrongConstant")
    @Subscribe(sticky = true, threadMode = ThreadMode.ASYNC)
    public void onEvent(WeightEvent event) {
        switch (event.getMessage()) {
            case WEIGHT:
                weightInterface.sendCmd(DataConversionUtils.HexString2Bytes(event.getData()));

                mWeight.setBackground(true);
                mWeight.setShow("0.00");

                break;

            case POWERON:
                try {
                    deviceControl.newSetGpioOn(Integer.parseInt(event.getData()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AppSeven.getInstance().setPower(true);
                break;

            case POWEROFF:
                try {
                    deviceControl.newSetGpioOff(Integer.parseInt(event.getData()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AppSeven.getInstance().setPower(false);
                break;

            case HEIGHT_CALIBRATION:
                //体积测量的高度校准
//                if (!mHeightDialog.isAdded()) {
//                    mHeightDialog.setTitle(getString(R.string.Current_height1)).setContent(mPlane == null ? "" : getString(R.string.Current_height2) + new DecimalFormat("0.0000").format(Math.abs(mPlane.getHigh() * 100)) + "cm").setProgressVisible(false).show(getFragmentManager(), "height");
//                }
//                upDateHeightDialog(Type);
                break;

            case HEIGHT_INIT:
                //体积相关部分
//                initPermission();
//                initTijiView();
                break;

            case PHOTO_SHOOT:
//                Bitmap bmp = MatUtil.toBitmap(BufferedMat.create(480, 640, Mat.Type.CV_8UC3, mCalThread.getRGBBuffer()));
//                Utils.saveImage(bmp);
                break;

            default:
                break;
        }
    }

    private boolean isupload;

    /**
     * 接收事件，1.激活成功开始初始化
     *
     * @param event event
     */

    @SuppressLint("WrongConstant")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent2(UploadEvent event) {
        switch (event.getMessage()) {
            case UPLOAD_DATA:
                if (mBalanceBean == null) {
                    ToastUtils.showShortToastSafe("没有可上传的数据");
                } else if (!(boolean) SpUtils.get(AppSeven.getInstance(), LOGIN_IS_MANAGER, false)) {
                    ToastUtils.showShortToastSafe("直接登录不可上传数据");
                } else if (isupload) {
                    ToastUtils.showShortToastSafe("正在上传数据，请重试...");
                } else {
                    ToastUtils.showShortToastSafe("上传中...");
                    isupload = true;
                    upload(mBalanceBean);
                }
                break;

            default:
                break;
        }
    }


    /**
     * ======================================================测体积相关部分源码=====================================================
     * <p>
     * 实现体积测量功能
     * <p>
     * 需要配合
     * <p>
     * 基于SPD0.8源码
     * <p>
     * 可简单实现功能。
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>
     * 体积部分准备在这里添加
     */

    private static final String planeHeight = "PLANE_HEIGHT";
    private static final String planeA = "PLANE_A";
    private static final String planeB = "PLANE_B";
    private static final String planeC = "PLANE_C";
    private static final String isPlane = "IS_PLANE";
    private static final int CALIBRATE_CAL_RESULT = 1;
    private static final int FAILED_CAL_RESULT = 2;
    private static final int SUCCESS_CAL_RESULT = 3;


    @Override
    public void onClick(View v) {
        if (mCalThread != null) {
            if (show) {
                mCalThread.setDataType(CalThread.DATATYPE.RGB);
                show = false;
            } else {
                mCalThread.setDataType(CalThread.DATATYPE.DEPTH);
                show = true;
            }
        }
    }

    @Override
    public void upDateListener(BoxSize boxSize) {
//        Message message = mMyHandler.obtainMessage(UPDATE_UI);
//        message.obj = boxSize;
//        mMyHandler.sendMessage(message);
    }


    @SuppressLint("WrongConstant")
    private @Constants.CAL_TYPE
    int Type = FAILED_CAL_RESULT;
    //IMI
    private ImiDevice mImiDevice;
    private CalThread mCalThread;
    private int mWidth = 640;
    private int mHeight = 480;
    //控件

    private MessageDialog mHeightDialog;
    private GLPanel mGLCal;

    private boolean isInitSuccess = false;
    private MeasureHelper mMeasureHelper;
    /*
     A100M 的相机内参
      */
    private CameraIntrinsics parameters;
    private MyHandler mMyHandler;
    private ProgressDialog mProgressDialog;
    private ExecutorService fixedThreadPool;
    private CalRunnable mCalRunnable1;
    private Plane mPlane;
    private String sn = "";

    static {
        //加载库并验证授权
        com.imi.sdk.base.Library.load();
        new com.imi.sdk.volume.Library();
    }


    /**
     * 权限申请，目前没用到
     */
    private void initPermission() {
        if (PermissionHelper.hasStoragePermission(this)) {
            PermissionHelper.requestStoragePermission(this);
        } else {
            //创建相应文件夹
            FileUtils.getInstance().init();
        }
    }

    private void initTijiView() {
        fixedThreadPool = Executors.newFixedThreadPool(1);
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.In_calculation));

        mGLCal = findViewById(R.id.gl_main_cal);
        mGLCal.setOnClickListener(this);
        show = true;

        initPlane();
        initHeightDialog();
        mMyHandler = new MyHandler(this);

        new Thread(new MyRunnable()).start();
        mCalRunnable1 = new CalRunnable();
    }

    private boolean show;

    private void initScanTime() {

        if (!(boolean) SpUtils.get(AppSeven.getInstance(), HEIGHT_SET, true)) {
            return;
        }

        if (first) {
            show = false;
            first = false;
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                /*
                 *要执行的操作
                 */
                Tiji();
                show = true;
                //2秒后执行Runnable中的run方法,否则初始化失败
            }, 2000);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                /*
                 *要执行的操作
                 */
                Tiji();
                show = true;
                //2秒后执行Runnable中的run方法,否则初始化失败
            }, 1000);
        }

    }


    private void Tiji() {

        if (!PermissionHelper.hasStoragePermission(this)) {
            PermissionHelper.requestStoragePermission(this);
            return;
        }
        if (isInitSuccess) {
            if (!SharedPreferenceHelper.getInstance(this, isPlane).getBoolean()) {
                calPlane();
            } else {
                calBox();
            }
        } else {
            ToastUtils.showShortToastSafe(getString(R.string.Camera_init));
        }

    }

    /**
     * 如果SP中已经保存Plane，加载以有参数
     */
    private void initPlane() {
        mPlane = PlaneHelper.getInstance(this).getPlane();
    }

    /**
     * 初始化高度的Dialog
     */
    private void initHeightDialog() {
        mHeightDialog = new MessageDialog();
        mHeightDialog.setPositiveClick(getString(R.string.calibration1), v -> calPlane()).setNegativeClick(getString(R.string.cancel), v -> mHeightDialog.dismiss());
    }


    /**
     * 更新高度Dialog的内容
     *
     * @param type 类别
     */
    @SuppressLint({"WrongConstant", "SwitchIntDef"})
    private void upDateHeightDialog(@Constants.CAL_TYPE int type) {
        switch (type) {
            case CALIBRATE_CAL_RESULT:
                mHeightDialog.setTitleText(getString(R.string.Calibrating)).setProgressBar(true).setContentText(mPlane == null ? "" : getString(R.string.Current_height) + new DecimalFormat("0.0000").format(Math.abs(mPlane.getHigh() * 100)) + "cm").setPositive(getString(R.string.calibration1)).setNegative(getString(R.string.cancel));
                break;
            case FAILED_CAL_RESULT:
                mHeightDialog.setTitleText(getString(R.string.Calibration_failed)).setProgressBar(false).setContentText(mPlane == null ? "" : getString(R.string.Current_height) + new DecimalFormat("0.0000").format(Math.abs(mPlane.getHigh() * 100)) + "cm").setPositive(getString(R.string.Recalibration)).setNegative(getString(R.string.cancel));
                break;
            case SUCCESS_CAL_RESULT:
                mHeightDialog.setTitleText(getString(R.string.Successful_calibration)).setProgressBar(false).setContentText(mPlane == null ? "" : getString(R.string.Current_height) + new DecimalFormat("0.0000").format(Math.abs(mPlane.getHigh() * 100)) + "cm").setPositive(getString(R.string.Recalibration)).setNegative(getString(R.string.cancel));
                break;
            default:
                break;
        }

        Type = type;
    }

    /**
     * 计算平面
     */
    @SuppressLint("WrongConstant")
    public void calPlane() {
        if (mCalThread != null && mCalThread.getIsInitSuccess() && mCalThread.getDepthBuffer() != null) {
            upDateHeightDialog(CALIBRATE_CAL_RESULT);
            Plane plane = mMeasureHelper.calculatorPlane(mWidth, mHeight, mCalThread.getDepthBuffer());
            if (plane.getState() == BoxState.OK) {
                double a = plane.getEle()[0];
                double b = plane.getEle()[1];
                double c = plane.getEle()[2];
                double h = plane.getEle()[3];
                SharedPreferenceHelper.getInstance(this, planeA).putFloat((float) a);
                SharedPreferenceHelper.getInstance(this, planeB).putFloat((float) b);
                SharedPreferenceHelper.getInstance(this, planeC).putFloat((float) c);
                SharedPreferenceHelper.getInstance(this, planeHeight).putFloat((float) h);
                SharedPreferenceHelper.getInstance(this, isPlane).putBoolean(true);
                mPlane = plane;
                upDateHeightDialog(SUCCESS_CAL_RESULT);
            } else {
                ToastUtils.showShortToastSafe(getString(R.string.Calculate_height_failure));
                upDateHeightDialog(FAILED_CAL_RESULT);
            }
        } else {
            ToastUtils.showShortToastSafe(getString(R.string.Camera_init));
        }
    }

    /**
     * 计算体积
     */
    public void calBox() {
        if (mCalThread != null && mCalThread.getIsInitSuccess()) {
            try {
                fixedThreadPool.execute(mCalRunnable1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mVolume.setShow("0.0000");
        }
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            ImiNect.initialize();
            mImiDevice = ImiDevice.getInstance();
            MainListener mainlistener = new MainListener();

            ImiFrameMode colorMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_IMAGE_RGB24, 640, 480, 30);
            ImiFrameMode depthMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_DEP_16BIT, 640, 480, 30);

            mImiDevice.setFrameMode(ImiDevice.ImiStreamType.COLOR, colorMode);
            mImiDevice.setFrameMode(ImiDevice.ImiStreamType.DEPTH, depthMode);
            mImiDevice.open(MainActivity.this, 0, mainlistener);
        }
    }


    /**
     * 实现设备接口
     */
    private class MainListener implements ImiDevice.OpenDeviceListener {
        @Override
        public void onOpenDeviceSuccess() {
            //自动获取内参
            Object pp = mImiDevice.getProperty(ImiPropertIds.IMI_PROPERTY_DEPTH_INTRINSIC_PARAMS);
            if (pp instanceof ImiCameraIntrinsic) {
                final ImiCameraIntrinsic intrinsic = (ImiCameraIntrinsic) pp;
                parameters = new CameraIntrinsics(intrinsic.getfxParam(), intrinsic.getfyParam(), intrinsic.getcxParam(), intrinsic.getcyParam(), mWidth, mHeight);
                showToast(getString(R.string.Read_the_int) + "\n" + intrinsic.getfxParam() + "\n" + intrinsic.getfyParam() + "\n" + intrinsic.getcxParam() + "\n" + intrinsic.getcyParam());
            } else {
                showToast(getString(R.string.Failed_to_read));
//              相机内参
                parameters = new CameraIntrinsics(495.9968f, 495.6681f, 324.0655f, 242.6749f, 640, 480);
            }

            mImiDevice.setProperty(IMI_PROPERTY_DEPTH_MIRROR, false);
            mImiDevice.setProperty(IMI_PROPERTY_IMAGE_MIRROR, false);
            //原体积设置
//            mImiDevice.setProperty(ImiPropertIds.IMI_PROPERTY_DEPTH_DENOISE, true);
//            mImiDevice.setImageRegistration(true);

            final long pDevice = mImiDevice.getDeviceHandle();
            sn = mImiDevice.getAttribute().getSerialNumber();
            showToast(sn);

            mMeasureHelper = new MeasureHelper(MainActivity.this, parameters, pDevice);
            isInitSuccess = true;
            runCalThread();
        }

        @Override
        public void onOpenDeviceFailed(final String errorMsg) {
            showToast(errorMsg);
        }
    }

    private void runCalThread() {
        mCalThread = new CalThread(this, mImiDevice, mMeasureHelper);
        mCalThread.setGLCal(mGLCal);
        mCalThread.setMeasureListener(this);
        mCalThread.onStart();
    }

    private class CalRunnable implements Runnable {
        private BoxSize mBoxSize;

        @Override
        public void run() {
            try {
                mBoxSize = mMeasureHelper.calculatorBox(mWidth, mHeight, mCalThread.getRGBBuffer(), mCalThread.getDepthBuffer(), mPlane);
                Message message = mMyHandler.obtainMessage();
                message.what = 1;
                message.obj = mBoxSize;
                mMyHandler.sendMessage(message);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String tiji;

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        BoxSize boxSize;
        WeakReference<MainActivity> activity;

        MyHandler(MainActivity activity) {
            this.activity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    boxSize = (BoxSize) msg.obj;
                    if (boxSize.getState() == BoxState.OK) {

                        mVolume.setShow(String.valueOf(new DecimalFormat("0.0000").format(boxSize.getVolume())));

                        String a = " a:" + new DecimalFormat("0.0").format(boxSize.getLong() * 100) + "cm; ";
                        String b = " b:" + new DecimalFormat("0.0").format(boxSize.getWide() * 100) + "cm; ";
                        String h = " h:" + new DecimalFormat("0.0").format(boxSize.getHigh() * 100) + "cm";

                        mVolume.setTiji(a + " " + b + " " + h);
                        KLog.e(boxSize.toString());

                        if (soundPool != null) {
                            soundPool.play(soundId2, 1, 1, 1, 0, 1);

                        }

                        //保存图片,默认不保存，否则比较消耗资源
                        if ((boolean) SpUtils.get(AppSeven.getInstance(), PHOTO_SET, false) && mVolume.getShow().equals(tiji)) {
                            EventBus.getDefault().postSticky(new WeightEvent(PHOTO_SHOOT, ""));
                        }
                        tiji = mVolume.getShow();

                    } else {
                        showFailureCode(boxSize.getState());
                    }
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                        ScreenUtils.setFullScreenOnWindowFocusChanged(MainActivity.this, true);
                    }

                    break;
                case 101:
                    ToastUtils.showShortToastSafe(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }


    public void showFailureCode(int boxState) {
        String msg = "";
        switch (boxState) {
            case BoxState.ERROR_DEPTH_IMAGE_LOSS:
                msg = getString(R.string.Camera_occlusion);
                break;
            case BoxState.ERORR_FUNCTIONAL:
                msg = getString(R.string.Algorithm_internal_function);
                break;
            case BoxState.ERROR_BOX_OUT_OF_RANGE:
                msg = getString(R.string.The_detected_object);
                break;
            case BoxState.ERROR_NO_BOX:
                msg = getString(R.string.No_object_detected);
                break;
            case BoxState.ERROR_OVERDATE:
                msg = getString(R.string.Authorization_expired);
                break;
            case BoxState.ERROR_LICENSE_NOFILE:
                msg = getString(R.string.No_license_file);
                break;
            case BoxState.ERROR_LICENSE_ERROR:
                msg = getString(R.string.Invalid_file);
                break;
            case BoxState.ERROR_HANDLE_ERROR:
                msg = getString(R.string.Incoming_handle_error);
                break;
            case BoxState.ERROR_PLATFORM_ERROR:
                msg = getString(R.string.Platform_error);
                break;
            case BoxState.ERROR_ALGORITHMVERSION_ERROR:
                msg = getString(R.string.Algorithm_version_error);
                break;
            case BoxState.ERROR_DEVICE_SN_ERROR:
                msg = getString(R.string.SN_nonauthorized_scope);
                break;
            default:
                break;
        }
        showToast(msg);
    }

    private void showToast(String msg) {
        if (Thread.currentThread() != getMainLooper().getThread()) {
            Message message = mMyHandler.obtainMessage();
            message.obj = msg;
            message.what = 101;
            mMyHandler.sendMessage(message);
        } else {
            ToastUtils.showShortToastSafe(msg);

        }
    }

    /**
     * 渲染RGB
     *
     * @param bufferRGB **
     * @param glPanel   **
     */
    private void drawColor(ByteBuffer bufferRGB, GLPanel glPanel) {

        if (glPanel != null) {
            glPanel.paint(null, bufferRGB, mWidth, mHeight);
        }
    }

    /**
     * 隐藏状态栏和导航栏，可以拉出，自动收起
     */
    public void setStatusNavigation() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //创建相应文件夹
            FileUtils.getInstance().init();
        }
    }


    PowerManager.WakeLock wakeLock = null;

    /**
     * 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
     */
    @SuppressLint({"InvalidWakeLockTag", "WakelockTimeout"})
    private void acquireWakeLock() {
        if (null == wakeLock) {
            PowerManager pm = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            }
            wakeLock = Objects.requireNonNull(pm).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "PostLocationService");
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

    /**
     * 释放设备电源锁
     */
    private void releaseWakeLock() {
        if (null != wakeLock) {
            wakeLock.release();
            wakeLock = null;
        }
    }

}
