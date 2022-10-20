package com.github.tvbox.osc.data;

import android.os.Bundle;

import com.github.tvbox.osc.base.BaseActivity;
import com.github.tvbox.osc.ui.activity.HomeActivity;
import com.github.tvbox.osc.util.AppManager;
import com.github.tvbox.osc.util.RemoteConfig;
import com.github.tvbox.osc.util.RemoteConfigName;
import com.google.gson.JsonElement;
import com.orhanobut.hawk.Hawk;

public class CustomData {
    private static CustomData instance;
    public static CustomData getInstance(){
        if (instance==null)
            instance = new CustomData();
        return instance;
    }

    // region APP模式类型
    public static final String APP_MODEL_TYPE = "APP_MODEL_TYPE"; // APP模式
    public enum AppModelType {
        YOUND, // 默认/青年版
        AGED // 老年版
    }
    public void SetAppModelType(AppModelType appModelType){
        Hawk.put(APP_MODEL_TYPE, appModelType);
        if (AppManager.getInstance().isActivity()) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("useCache", true);
            BaseActivity currActivity = (BaseActivity) AppManager.getInstance().currentActivity();
            currActivity.jumpActivity(HomeActivity.class, bundle);
        }
    }
    public AppModelType GetAppModelType(){
        return Hawk.get(APP_MODEL_TYPE, AppModelType.YOUND);
    }
    public String GetAppModelTypeName() {
        return GetAppModelTypeName(GetAppModelType());
    }
    public String GetAppModelTypeName(AppModelType appModelType){
        String showText = "未定义类型";
        switch (appModelType)
        {
            case AGED:
                showText = "老年版";
                break;
            case YOUND:
                showText = "青年版";
                break;
        }
        return showText;
    }
    // endregion

    // region 首页按钮控制
    public boolean GetHomeButtonVisition(String buttonName){
        String appModelName = RemoteConfigName.CustomData_YOUND;
        if (GetAppModelType()==AppModelType.YOUND){
            appModelName = RemoteConfigName.CustomData_YOUND;
        }else if (GetAppModelType()==AppModelType.AGED) {
            appModelName = RemoteConfigName.CustomData_AGED;
        }
        JsonElement element = RemoteConfig.GetValue(
                RemoteConfigName.CustomData,
                appModelName,
                RemoteConfigName.APPModel_HomeButtons,
                buttonName);
        return element==null||element.getAsBoolean();
    }
    // endregion
}