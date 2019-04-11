package com.spd.qsevendemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.spd.qsevendemo.model.LoginModel;
import com.spd.qsevendemo.net.NetApi;
import com.spd.qsevendemo.net.UpdateResult;
import com.spd.qsevendemo.utils.Logcat;
import com.spd.qsevendemo.utils.SpUtils;
import com.spd.qsevendemo.utils.ToastUtils;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xuyan 神威登录界面
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUsername;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);

        mUsername.setText((String) SpUtils.get(this, LoginModel.LOGIN_USERNAME, "sw"));
        mPassword.setText((String) SpUtils.get(this, LoginModel.LOGIN_PASSWORD, "swsf0416"));
        Button mLogin = findViewById(R.id.login);
        mLogin.setOnClickListener(this);
        Button mLoginNoAccount = findViewById(R.id.login_noaccount);
        mLoginNoAccount.setOnClickListener(this);
    }


    /**
     * 预留的网络连接方法
     */
    public void checkUpdate() {
        Logcat.d("checkUpdate");
        String code = String.valueOf(1);
        String name = "com.spd.scan";

        HashMap<String, String> params = new HashMap<>();
        params.put("versionNumber", code);
        params.put("apkName", name);


        NetApi.getInstance().checkUpdate2(params).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UpdateResult>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(UpdateResult updateResult) {
                if (updateResult.isSuccess()){
                    //成功返回结果
                }
                Logcat.d("updateResult.isSuccess()");
            }

            @Override
            public void onError(Throwable e) {
                Logcat.d(e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            if ((!"".equals(mUsername.getText().toString())) && (!"".equals(mPassword.getText().toString()))) {
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                //判断登陆
                startActivity(new Intent(LoginActivity.this, FirstActivity.class));
            } else {
                ToastUtils.showLongToastSafe(R.string.please_enter);
            }
        } else if (v.getId() == R.id.login_noaccount) {
            //不判断登陆
            startActivity(new Intent(LoginActivity.this, FirstActivity.class));
        }
    }
}
