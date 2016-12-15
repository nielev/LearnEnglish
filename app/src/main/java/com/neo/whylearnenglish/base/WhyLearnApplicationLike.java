package com.neo.whylearnenglish.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 *  Tinker框架必需类，代替Application
 * Created by Neo on 2016/11/7.
 */
@DefaultLifeCycle(
        application = "com.neo.whylearnenglish.base.WhyLearnApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL
)
public class WhyLearnApplicationLike extends DefaultApplicationLike {
    private final static String TAG = "WhyLearnApplicationLike";
    private static Context context;
    public static Handler mainHandler;
    public WhyLearnApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent, Resources[] resources, ClassLoader[] classLoader, AssetManager[] assetManager) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent, resources, classLoader, assetManager);
        context = application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "this application create......");
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
//        MultiDex.install(base);
        TinkerInstaller.install(this);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static Context getContext(){
        return context;
    }
}
