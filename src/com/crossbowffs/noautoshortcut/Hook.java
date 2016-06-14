package com.crossbowffs.noautoshortcut;

import android.content.Intent;
import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    private static final String TAG = "NoAutoShortcut";
    private static final String INSTALL_ACTION = "com.android.launcher.action.INSTALL_SHORTCUT";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        final String packageName = lpparam.packageName;
        XposedHelpers.findAndHookMethod(
            "android.app.ContextImpl", lpparam.classLoader,
            "sendBroadcast", Intent.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Intent intent = (Intent)param.args[0];
                    if (intent != null && INSTALL_ACTION.equals(intent.getAction())) {
                        Log.i(TAG, "Blocked " + packageName + " from creating shortcut");
                        param.setResult(null);
                    }
                }
            });
    }
}
