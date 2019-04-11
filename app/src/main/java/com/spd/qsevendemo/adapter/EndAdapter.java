package com.spd.qsevendemo.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.spd.qsevendemo.AppSeven;
import com.spd.qsevendemo.R;
import com.spd.qsevendemo.model.SevenBean;

import java.util.List;

public class EndAdapter extends BaseQuickAdapter<SevenBean, BaseViewHolder> {
    public EndAdapter(@Nullable List<SevenBean> data) {
        super(R.layout.view_item_duty_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SevenBean item) {
        helper.setText(R.id.tvDuty, item.getName());

        helper.setImageDrawable(R.id.iv_choose, item.getCheck() ? AppSeven.getInstance().getDrawable(R.drawable.code_choose)
                :  AppSeven.getInstance().getDrawable(R.drawable.code_unchoose));

    }
}
