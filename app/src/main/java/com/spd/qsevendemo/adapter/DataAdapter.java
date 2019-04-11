package com.spd.qsevendemo.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spd.qsevendemo.R;
import com.spd.qsevendemo.model.DataBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author xuyan
 */
public class DataAdapter extends BaseQuickAdapter<DataBean, BaseViewHolder> {
    public DataAdapter(@Nullable List<DataBean> data) {
        super(R.layout.view_data, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBean item) {
        helper.setText(R.id.data_one, String.valueOf(helper.getAdapterPosition() + 1));
        //年月日 时分秒
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        if (item.getTime() != 0) {
            Date date = new Date(item.getTime());
            String time = sdf.format(date);
            helper.setText(R.id.data_two, time);

        } else {
            helper.setText(R.id.data_two, "");
        }

        helper.setText(R.id.data_three, item.getBarcode());
        helper.setText(R.id.data_four, item.getWeight());
        helper.setText(R.id.data_five, item.getTiji());
    }
}

