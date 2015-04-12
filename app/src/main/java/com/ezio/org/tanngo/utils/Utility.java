package com.ezio.org.tanngo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;

/**
 * Created by Ezio on 2015/4/7.
 */
public class Utility {

    public final static String LOG_TAG = "Ezio";




    public static void randomSet(int min, int max, int n, int exceptionNub, HashSet<Integer> set) {
        if (n > (max - min + 1 + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            if (num == exceptionNub) {
                i--;
            } else {
                set.add(num);// 将不同的数存入HashSet中
            }


        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            randomSet(min, max, n - setSize, exceptionNub, set);// 递归
        }
    }

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min)) + min;
    }


    public static void ShowToast(String text,Context mContext) {

        if (!TextUtils.isEmpty(text)) {

            Toast.makeText(mContext, text,Toast.LENGTH_SHORT).show();

        }
    }

    public static void ShowDebugLog(String title,String text){
        if (!TextUtils.isEmpty(text)&&!TextUtils.isEmpty(title)){
            Log.d(LOG_TAG,title+"----->"+text);
        }
    }


}
