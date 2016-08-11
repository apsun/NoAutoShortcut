package com.crossbowffs.noautoshortcut;

import android.content.Intent;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    private static final String INSTALL_ACTION = "com.android.launcher.action.INSTALL_SHORTCUT";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!"android".equals(lpparam.packageName)) {
            return;
        }

        Class<?> cls = XposedHelpers.findClass("com.android.server.am.ActivityManagerService", lpparam.classLoader);
        XposedBridge.hookAllMethods(cls, "broadcastIntent", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Intent intent = (Intent)param.args[1];
                if (intent != null && INSTALL_ACTION.equals(intent.getAction())) {
                    param.setResult(0);
                }
            }
        });
    }
}
