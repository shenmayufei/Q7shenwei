package com.spd.qsevendemo.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.spd.qsevendemo.AppSeven;
import com.spd.qsevendemo.R;
import com.spd.qsevendemo.adapter.EndAdapter;
import com.spd.qsevendemo.model.DatabaseAction;
import com.spd.qsevendemo.model.SevenBean;
import com.spd.qsevendemo.utils.Logcat;
import com.spd.qsevendemo.utils.SpUtils;
import com.spd.qsevendemo.utils.ToastUtils;
import com.spd.qsevendemo.weight.WeightActivity;
import com.spd.qsevendemo.weight.WeightEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.spd.qsevendemo.model.SevenModel.HEIGHT_CALIBRATION;
import static com.spd.qsevendemo.model.SevenModel.HEIGHT_SET;
import static com.spd.qsevendemo.model.SevenModel.PEELED;
import static com.spd.qsevendemo.model.SevenModel.PHOTO_SET;
import static com.spd.qsevendemo.model.SevenModel.POWEROFF;
import static com.spd.qsevendemo.model.SevenModel.POWERON;
import static com.spd.qsevendemo.model.SevenModel.SCAN_SET;
import static com.spd.qsevendemo.model.SevenModel.WEIGHT;
import static com.spd.qsevendemo.model.SevenModel.WEIGHT_SET;
import static com.spd.qsevendemo.model.SevenModel.ZERO;

/**
 * @author xuyan
 */
public class EndWindow extends PopupWindow {

    private EndAdapter mAdaper;

    public EndWindow(AppCompatActivity mContext, List<SevenBean> mList) {
        Logcat.d(mList.toString());
        List<SevenBean> data = mList;
        mAdaper = new EndAdapter(data);
        Activity activity = mContext;
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        setWidth(dm.widthPixels);
        setHeight(dm.heightPixels);

        View popupView = LayoutInflater.from(activity).inflate(R.layout.view_end_dialog_layout,
                new LinearLayout(activity), false);
        RecyclerView recyclerView = popupView.findViewById(R.id.rvConditionQuery);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(mAdaper);
        setContentView(popupView);

        mAdaper.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mList.get(position).getCheck()) {
                    mList.get(position).setCheck(false);
                    com.spd.code.CodeUtils.CodeDisable(mList.get(position).getCode());
                } else {
                    mList.get(position).setCheck(true);
                    com.spd.code.CodeUtils.CodeEnable(mList.get(position).getCode());
                }
                DatabaseAction.savePrepareBox(mList);
                mAdaper.notifyDataSetChanged();
            }
        });

        TextView mCodeSet = popupView.findViewById(R.id.code_set);
        TextView mBalanceSet = popupView.findViewById(R.id.balance_set);
        TextView textView1 = popupView.findViewById(R.id.balance_set_one);
        TextView textView2 = popupView.findViewById(R.id.balance_set_two);
        TextView textView3 = popupView.findViewById(R.id.balance_set_three);
        TextView textView4 = popupView.findViewById(R.id.balance_set_four);

        TextView mOtherSet = popupView.findViewById(R.id.other_set);
        TextView mOther1 = popupView.findViewById(R.id.other_set_one);
        TextView mOther2 = popupView.findViewById(R.id.other_set_two);
        mOther1.setText(AppSeven.getInstance().getString(R.string.Fill_light_and_fan_open));

        TextView mExit = popupView.findViewById(R.id.exit_settings);


        TextView mOptionSet = popupView.findViewById(R.id.options_set);
        TextView mOption1 = popupView.findViewById(R.id.options_set_one);
        TextView mOption2 = popupView.findViewById(R.id.options_set_two);
        TextView mOption3 = popupView.findViewById(R.id.options_set_three);
        TextView mOption4 = popupView.findViewById(R.id.options_set_four);


        mCodeSet.setOnClickListener(v -> {
            if (recyclerView.getVisibility() == View.VISIBLE) {
                recyclerView.setVisibility(View.GONE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        mBalanceSet.setOnClickListener(v -> {
            if (textView1.getVisibility() == View.VISIBLE) {
                textView1.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                textView3.setVisibility(View.GONE);
                textView4.setVisibility(View.GONE);
            } else {
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                //textView4.setVisibility(View.VISIBLE);
            }
        });

        textView1.setOnClickListener(v -> {

            ToastUtils.showShortToastSafe(R.string.Click_Calibration);
            //操作都在weight页面实现
            activity.startActivity(new Intent(activity, WeightActivity.class));
            dismiss();

        });

        textView2.setOnClickListener(v -> {
            //操作在主页面实现
            ToastUtils.showShortToastSafe(R.string.Click_Zero);
            EventBus.getDefault().postSticky(new WeightEvent(WEIGHT, ZERO));
            dismiss();
        });

        textView3.setOnClickListener(v -> {
            //操作在主页面实现
            ToastUtils.showShortToastSafe(R.string.Clicked_on_the_peeled);
            EventBus.getDefault().postSticky(new WeightEvent(WEIGHT, PEELED));
            dismiss();
        });

        textView4.setOnClickListener(v -> {
            //操作在主页面实现
            ToastUtils.showShortToastSafe("点击了第四项");
        });


        mOtherSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOther1.getVisibility() == View.VISIBLE) {
                    mOther1.setVisibility(View.GONE);
                    mOther2.setVisibility(View.GONE);
                    mOption1.setVisibility(View.GONE);
                    mOption2.setVisibility(View.GONE);
                } else {
                    mOther1.setVisibility(View.VISIBLE);
                    mOther2.setVisibility(View.VISIBLE);
//                    mOption1.setVisibility(View.VISIBLE);
//                    mOption2.setVisibility(View.VISIBLE);
                }
            }
        });


        mOther1.setOnClickListener(v -> {
            //操作在主页面实现
            ToastUtils.showShortToastSafe(R.string.Clicked_on_the_fill_light_fan);
            if (AppSeven.getInstance().isPower()) {
                EventBus.getDefault().postSticky(new WeightEvent(POWEROFF, "14"));
                mOther1.setText(AppSeven.getInstance().getString(R.string.Fill_light_and_fan_off));
                EventBus.getDefault().postSticky(new WeightEvent(POWEROFF, "89"));
            } else {
                EventBus.getDefault().postSticky(new WeightEvent(POWERON, "14"));
                mOther1.setText(AppSeven.getInstance().getString(R.string.Fill_light_and_fan_open));
                EventBus.getDefault().postSticky(new WeightEvent(POWERON, "89"));
            }

        });

        mOther2.setOnClickListener(v -> {
            //操作在主页面实现
            EventBus.getDefault().postSticky(new WeightEvent(HEIGHT_CALIBRATION, ""));
        });


        mOptionSet.setOnClickListener(v -> {
            if (mOption1.getVisibility() == View.VISIBLE) {
                mOption1.setVisibility(View.GONE);
                mOption2.setVisibility(View.GONE);
                mOption3.setVisibility(View.GONE);
                mOption4.setVisibility(View.GONE);
            } else {
                mOption1.setVisibility(View.VISIBLE);
                mOption2.setVisibility(View.VISIBLE);
                mOption3.setVisibility(View.VISIBLE);
                mOption4.setVisibility(View.VISIBLE);
            }
        });

        if ((boolean) SpUtils.get(AppSeven.getInstance(), SCAN_SET, true)) {
            mOption1.setText(AppSeven.getInstance().getString(R.string.scan_code_on));
        } else {
            mOption1.setText(AppSeven.getInstance().getString(R.string.scan_code_off));
        }

        mOption1.setOnClickListener(v -> {
            //设置一个属性值用于判断
            if ((boolean) SpUtils.get(AppSeven.getInstance(), SCAN_SET, true)) {
                mOption1.setText(AppSeven.getInstance().getString(R.string.scan_code_off));
                SpUtils.put(AppSeven.getInstance(), SCAN_SET, false);
            } else {
                mOption1.setText(AppSeven.getInstance().getString(R.string.scan_code_on));
                SpUtils.put(AppSeven.getInstance(), SCAN_SET, true);
            }
        });

        if ((boolean) SpUtils.get(AppSeven.getInstance(), WEIGHT_SET, true)) {
            mOption2.setText(AppSeven.getInstance().getString(R.string.weigh_on));
        } else {
            mOption2.setText(AppSeven.getInstance().getString(R.string.weigh_off));
        }

        mOption2.setOnClickListener(v -> {
            //每个都是设置属性，然后用的时候再判断
            if ((boolean) SpUtils.get(AppSeven.getInstance(), WEIGHT_SET, true)) {
                mOption2.setText(AppSeven.getInstance().getString(R.string.weigh_off));
                SpUtils.put(AppSeven.getInstance(), WEIGHT_SET, false);
            } else {
                mOption2.setText(AppSeven.getInstance().getString(R.string.weigh_on));
                SpUtils.put(AppSeven.getInstance(), WEIGHT_SET, true);
            }
        });

        if ((boolean) SpUtils.get(AppSeven.getInstance(), HEIGHT_SET, true)) {
            mOption3.setText(AppSeven.getInstance().getString(R.string.volume_on));
        } else {
            mOption3.setText(AppSeven.getInstance().getString(R.string.volume_off));
        }

        mOption3.setOnClickListener(v -> {
            //每个都是设置属性，然后用的时候再判断
            if ((boolean) SpUtils.get(AppSeven.getInstance(), HEIGHT_SET, true)) {
                mOption3.setText(AppSeven.getInstance().getString(R.string.volume_off));
                SpUtils.put(AppSeven.getInstance(), HEIGHT_SET, false);
            } else {
                mOption3.setText(AppSeven.getInstance().getString(R.string.volume_on));
                SpUtils.put(AppSeven.getInstance(), HEIGHT_SET, true);
            }
        });

        if ((boolean) SpUtils.get(AppSeven.getInstance(), PHOTO_SET, false)) {
            mOption4.setText(AppSeven.getInstance().getString(R.string.photograph_on));
        } else {
            mOption4.setText(AppSeven.getInstance().getString(R.string.photograph_off));
        }

        mOption4.setOnClickListener(v -> {
            //每个都是设置属性，然后用的时候再判断
            if ((boolean) SpUtils.get(AppSeven.getInstance(), PHOTO_SET, false)) {
                mOption4.setText(AppSeven.getInstance().getString(R.string.photograph_off));
                SpUtils.put(AppSeven.getInstance(), PHOTO_SET, false);
            } else {
                mOption4.setText(AppSeven.getInstance().getString(R.string.photograph_on));
                SpUtils.put(AppSeven.getInstance(), PHOTO_SET, true);
            }
        });


        mExit.setOnClickListener(v -> dismiss());

    }


}
