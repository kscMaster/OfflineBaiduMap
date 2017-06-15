package com.sby.practice.application;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by kowal on 2017/5/25.
 */

public class MapApplication extends Application
{
    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        context = getApplicationContext();
    }

    /**
     * 全局Context
     *
     * @return
     */
    public static Context getContext()
    {
        return context;
    }
}
