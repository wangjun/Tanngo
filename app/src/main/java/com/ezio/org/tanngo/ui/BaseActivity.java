package com.ezio.org.tanngo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.ezio.org.tanngo.utils.MyPreference;

import cn.bmob.v3.Bmob;

/**
 * Created by Ezio on 2015/4/11.
 */
public abstract class BaseActivity extends Activity{


    protected final static String BMOB_APP_ID = "9118cc097159952e26caf5c9b535006e";

    protected MyPreference myPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initial Bmob SDK
        Bmob.initialize(getApplicationContext(), BMOB_APP_ID);
        myPref = new MyPreference(getApplicationContext());


        setContentView();
        initViews();
        initData();
        initListeners();

    }

    /**
     * 设置布局文件
     */
    public abstract void setContentView();

    /** 进行数据初始化
     * initData
     */
    public abstract void initData();

    /**
     * 初始化布局文件中的控件
     */
    public abstract void initViews();

    /**
     * 初始化控件的监听
     */
    public abstract void initListeners();




    Toast mToast;

    public void ShowToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }
}
