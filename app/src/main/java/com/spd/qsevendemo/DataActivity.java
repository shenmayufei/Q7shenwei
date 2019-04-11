package com.spd.qsevendemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.spd.qsevendemo.adapter.DataAdapter;
import com.spd.qsevendemo.model.DataBean;
import com.spd.qsevendemo.model.DatabaseAction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuyan  展示画面
 */
public class DataActivity extends AppCompatActivity {

    private DataAdapter mAdapter;
    private List<DataBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        initView();
    }

    private void initView() {
        mList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.rv_content);

        mList = DatabaseAction.queryData();

        mAdapter = new DataAdapter(mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(AppSeven.getInstance()));
        recyclerView.setAdapter(mAdapter);
    }


}
