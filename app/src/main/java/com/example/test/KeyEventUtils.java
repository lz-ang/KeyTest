package com.example.test;

import android.hardware.input.InputManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.KeyEvent;

public class KeyEventUtils {

    //需要反射获取IWindowManager
    public static void test() {
//        IBinder wmbinder = ServiceManager.getService("window");
//        IWindowManager m_WndManager = IWindowManager.Stub.asInterface(wmbinder);
//        long now = SystemClock.uptimeMillis();
//        KeyEvent keyDown = new KeyEvent(now, now, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_POWER, 0);
//        m_WndManager.injectKeyEvent(keyDown, false);
//        KeyEvent keyUp = new KeyEvent(now, now, KeyEvent.ACTION_UP,KeyEvent.KEYCODE_POWER, 0);
//        m_WndManager.injectKeyEvent(keyUp, false);
    }

// 需要反射获取InputManager
    public static void test2() {
        long now = SystemClock.uptimeMillis();
        KeyEvent down = new KeyEvent(now, now, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_1, 0);
//        InputManager.getInstance().injectInputEvent(down, 0);0
        KeyEvent up = new KeyEvent(now, now, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_1, 0);
//        InputManager.getInstance().injectInputEvent(up, 0);
    }
}
