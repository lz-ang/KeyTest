package com.example.test;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class KeyService extends AccessibilityService {
    private static final String TAG = KeyService.class.getSimpleName();
    private Instrumentation instrumentation = new Instrumentation();

    public static KeyService mService;

    //实现辅助功能
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mService = this;
        Toast.makeText(this, "模拟物理按键锁定中...",Toast.LENGTH_LONG).show();
//        setAccessibilityServiceInfo();
    }

    //配置需要监听的事件类型、要监听哪个程序，最小监听间隔等属性  方式二
    private void setAccessibilityServiceInfo() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK;
        info.packageNames = new String[]{"com.tencent.mm"};
        info.notificationTimeout = 100;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "模拟物理按键功能被迫中断", Toast.LENGTH_LONG).show();
        mService = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"模拟物理按键功能已关闭", Toast.LENGTH_LONG).show();
        mService = null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 焦点
        mParams.gravity = Gravity.BOTTOM;
        //8.0以上系统使用WindowManager.LayoutParams.TYPE_PHONE 报错崩溃问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        Button recentBtn = new Button(this);
        recentBtn.setBackgroundColor(Color.TRANSPARENT);
        recentBtn.setText("最近任务");
        recentBtn.setTextColor(Color.BLUE);
        recentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "recentBtn============== ");
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_RECENTS);
            }
        });

        Button homeBtn = new Button(this);
        homeBtn.setBackgroundColor(Color.TRANSPARENT);
        homeBtn.setText("桌面");
        homeBtn.setTextColor(Color.BLUE);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "点击桌面");
                //GLOBAL_ACTION_POWER_DIALOG 重启和关机
                //GLOBAL_ACTION_NOTIFICATIONS 下拉通知栏
                //GLOBAL_ACTION_QUICK_SETTINGS 打开快速设置
                //GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN 分屏
                //GLOBAL_ACTION_LOCK_SCREEN 锁屏----无效
                //GLOBAL_ACTION_KEYCODE_HEADSETHOOK 截屏 ---无效
                //GLOBAL_ACTION_BACK 返回
                //GLOBAL_ACTION_HOME 桌面
                // GLOBAL_ACTION_RECENTS 最近任务
                performGlobalAction(KeyEvent.KEYCODE_POWER);
            }
        });

        Button backBtn = new Button(this);
        backBtn.setBackgroundColor(Color.TRANSPARENT);
        backBtn.setText("返回");
        backBtn.setTextColor(Color.BLUE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "点击返回");
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            }
        });

        linearLayout.addView(homeBtn);
        linearLayout.addView(recentBtn);
        linearLayout.addView(backBtn);
        windowManager.addView(linearLayout, mParams);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public static boolean isAccessibilitySettingsOn(Context mContext, Class<? extends AccessibilityService> clazz) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + clazz.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + KeyService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED***");
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    Log.v(TAG, "accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }
}
