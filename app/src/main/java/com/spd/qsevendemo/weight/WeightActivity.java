package com.spd.qsevendemo.weight;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.spd.qsevendemo.AppSeven;
import com.spd.qsevendemo.R;
import com.speedata.libutils.DataConversionUtils;

/**
 * @author xuyan  此页面只做校准，先隐藏其他按钮。
 */
public class WeightActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 设置校正标准砝码重量值(单位：KG)
     */
    private EditText mEtdWeight;
    /**
     * Hello World!
     */
    private TextView mTvShow;
    /**
     * 打开
     */
    private Button mBtnZhengchang;
    /**
     * 去皮
     */
    private Button mQupi;
    /**
     * 置零
     */
    private Button mZhiling;
    /**
     * 量程最大
     */
    private Button mBtnZuida;
    /**
     * 标零
     */
    private Button mBtnBiaoling;
    /**
     * 校正
     */
    private Button mBtnJiaozheng;
    private WeightInterface weightInterface;
    private int flag = 0;
    /**
     * 00
     */
    private EditText mEtdWeight2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_layout);
        initView();
        initLibz();
        openWeight();
    }

    private void openWeight() {
        if (flag == 0) {
            flag = 1;
            weightInterface.initWeight();
            mBtnZhengchang.setText(getString(R.string.close));
        }
    }

    private void initLibz() {
        weightInterface = new WeightRealize(AppSeven.getInstance());
        weightInterface.setWeightStatas(new WeightInterface.DisplayWeightDatasListener() {
            @Override
            public void WeightStatas(final int i, final double weight) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        switch (i) {
                            case 0:
                                mTvShow.setTextColor(Color.RED);
                                mTvShow.setText(getString(R.string.Electronic_scale));
                                break;
                            case 1:
                                mTvShow.setTextColor(Color.GREEN);
                                mTvShow.setText(getString(R.string.weight) + weight + getString(R.string.KG));
                                break;
                            case 2:
                                mTvShow.setTextColor(Color.RED);
                                mTvShow.setText(getString(R.string.weight) + weight + getString(R.string.KG));
                                break;
                            case 3:
                                Toast.makeText(WeightActivity.this, getString(R.string.Please_zoom_in), Toast.LENGTH_SHORT).show();
                                mTvShow.setText(getString(R.string.Please_zoom_in));
                                break;
                            default:
                                break;
                        }
                    }
                });

            }
        });

    }

    private void initView() {
        mEtdWeight = findViewById(R.id.etd_weight);
        mTvShow = findViewById(R.id.tv_show);
        mBtnZhengchang = findViewById(R.id.btn_zhengchang);
        mBtnZhengchang.setOnClickListener(this);
        mQupi = findViewById(R.id.qupi);
        mQupi.setOnClickListener(this);
        mZhiling = findViewById(R.id.zhiling);
        mZhiling.setOnClickListener(this);
        mBtnZuida = findViewById(R.id.btn_zuida);
        mBtnZuida.setOnClickListener(this);
        mBtnBiaoling = findViewById(R.id.btn_biaoling);
        mBtnBiaoling.setOnClickListener(this);
        mBtnJiaozheng = findViewById(R.id.btn_jiaozheng);
        mBtnJiaozheng.setOnClickListener(this);
        mEtdWeight = (EditText) findViewById(R.id.etd_weight);
        mEtdWeight.setOnClickListener(this);
        mEtdWeight2 = (EditText) findViewById(R.id.etd_weight2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flag = 0;
        mBtnZhengchang.setText(getString(R.string.open));
        mTvShow.setTextColor(Color.RED);
        mTvShow.setText(getString(R.string.weight) + "0.00KG");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_zhengchang:
                if (flag == 0) {
                    flag = 1;
                    weightInterface.initWeight();
                    mBtnZhengchang.setText(getString(R.string.close));
                } else {
                    weightInterface.releaseWeightDev();
                    flag = 0;
                    mBtnZhengchang.setText(getString(R.string.open));
                    mTvShow.setText("");
                }
                break;
            case R.id.qupi:
                weightInterface.sendCmd(DataConversionUtils.HexString2Bytes("024154313503"));
                break;
            case R.id.zhiling:
                weightInterface.sendCmd(DataConversionUtils.HexString2Bytes("02415A314203"));
                break;
            case R.id.btn_zuida:
                weightInterface.sendCmd(DataConversionUtils.HexString2Bytes("0241742B31353030303033323903"));
                break;
            case R.id.btn_biaoling:
                weightInterface.sendCmd(DataConversionUtils.HexString2Bytes("024170333103"));
                break;
            case R.id.btn_jiaozheng:
                byte[] weight = CheckClass.checkWight(mEtdWeight.getText().toString(), mEtdWeight2.getText().toString());

                if (weight != null) {
                    weightInterface.sendCmd(weight);
                } else {
                    Toast.makeText(WeightActivity.this, getString(R.string.Please_fill_in), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.etd_weight:
                break;
        }
    }



    @Override
    protected void onPause() {
        if (flag == 1) {
            weightInterface.releaseWeightDev();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
