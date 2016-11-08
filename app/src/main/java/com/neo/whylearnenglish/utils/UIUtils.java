package com.neo.whylearnenglish.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.neo.whylearnenglish.base.BaseActivity;
import com.neo.whylearnenglish.base.WhyLearnApplicationLike;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Neo on 2016/11/7.
 */

public class UIUtils {

    /**
     * 获取全局Context对象
     * @return
     */
    public static Context getContext(){
        return WhyLearnApplicationLike.getContext();
    }

    public static BaseActivity getForegroundActivity(){
        return BaseActivity.getForegroundActivity();
    }

    public static int dp2px(int dp,Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕显示的高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取屏幕的绝对高度
     * @param context
     * @return
     */
    public static int getScreenHeightAbsolute(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int dpi = 0;
        DisplayMetrics dm = new DisplayMetrics();
        Class c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display,dm);
            //绝对高度
            dpi = dm.heightPixels;
        } catch (Exception e){
            e.printStackTrace();
        }
        if(dpi == 0){
            dpi = wm.getDefaultDisplay().getHeight();
        }
        return dpi;
    }


    /**
     * 获取屏幕显示的宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕的绝对宽度
     * @param context
     * @return
     */
    public static int getScreenWidthAbsolute(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int dpi = 0;
        DisplayMetrics dm = new DisplayMetrics();
        Class c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display,dm);
            //绝对宽度
            dpi = dm.widthPixels;
        } catch (Exception e){
            e.printStackTrace();
        }
        if (dpi == 0){
            dpi = wm.getDefaultDisplay().getWidth();
        }
        return dpi;
    }

    /**
     * 获取设备dpi
     * @param context
     * @return
     */
    public static int getDPI(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.densityDpi;
    }

    /**
     * 获得星期数，星期天从0开始
     * @return
     */
    public static int getWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if(w < 0){
            w = 0;
        }
        return w;
    }
    /**
     * 获取当前年月日 yyyy-MM-dd格式
     * @return
     */
    public static String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new Date());
        return date;
    }
    /**
     * 获取版本名称
     * @return
     */
    public static String getAppVersionName(){
        String versionName = null;
        try {
            String packageName = getContext().getPackageName();
            versionName = getContext().getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }
    /**
     * 获取版本号 返回app版本号 用于检查更新时使用
     */
    public static int getVersionInt() {
        try {
            PackageManager mPackageManager = getContext().getPackageManager();
            PackageInfo packageInfo = mPackageManager.getPackageInfo(
                    getContext().getPackageName(), 0);
            // 获取到版本号
            int versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取UserAgent
     * @return
     */
    public static String getUserAgent() {
        String subversion = getAppVersionName().substring(0, 3);
        String userAgent = "SLRC/" + subversion + " (Android)";
        LogUtil.e("USERAGENT", userAgent);
        return userAgent;
    }
    public static final String FILENAME = "keepAlive.config";
    public static void saveBlackScreenStatus(boolean isKeepAlive){
        File cacheDir = UIUtils.getContext().getCacheDir().getParentFile();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(cacheDir,FILENAME));
            String saveStatus = "isKeepAlive" + ":" + isKeepAlive;
            fos.write(saveStatus.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(null != fos){
                    fos.close();
                }
            }catch (Exception e) {
            }
        }

    }
    public static void readBlack(){
        String path = UIUtils.getContext().getCacheDir().getParentFile().getAbsolutePath();
        File file = new File(path + "/"+FILENAME) ;
        if(file.exists()){
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                StringBuilder sb = new StringBuilder();
                while((len = fis.read(bytes)) > 0){
                    String s = new String(bytes, 0, len,"UTF-8");
                    sb.append(s);
                }
                fis.close();

                Log.e("CONFIG", "blackscreen:"+sb.toString()+",lenth:"+sb.length()+"path:"+file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                CloseUtils.closeQuietly(fis);
            }
        }
    }

    private static boolean getFileContent(File file) throws Exception{
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            StringBuilder sb = new StringBuilder();
            while((len = fis.read(bytes)) > 0){
                String s = new String(bytes, 0, len,"UTF-8");
                sb.append(s);
            }

            Log.e("CONFIG", "sunlogin:"+sb.toString()+",lenth:"+sb.length());

//            if(sb.toString().contains(SPCode.AUTO_START) || sb.toString().contains(SPCode.IS_MOVE_TO＿BACK)){
//                String[] split = sb.toString().split(":");
//                String isAuto = split[1];
//                if("false".equals(isAuto)){
//                    return false;
//                }else{
//                    return true;
//                }
//            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            CloseUtils.closeQuietly(fis);
        }

        return false;
    }


    public static void saveMoveToBack(boolean isMoveToBack){
        FileOutputStream fos = null;
        try{
//            File file = new File(Constant.MOVETOBACK);
//            if(!file.exists()){
//                file.getParentFile().mkdirs();
//            }
//            fos = new FileOutputStream(file);
//            String saveStatus = SPCode.IS_MOVE_TO＿BACK + ":" +isMoveToBack;
//            fos.write(saveStatus.getBytes("UTF-8"));

        }catch(Exception e){
            e.printStackTrace();
        } finally {
            CloseUtils.closeQuietly(fos);
        }
    }
    public static boolean getSaveToBackStatus(){
//        File file = new File(Constant.MOVETOBACK);
//        if(file.exists()){
//            try {
//                return getFileContent(file);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return false;
    }
    public static String map2JsonString(Map<String, String> map) {
        if(null == map || map.size() <=0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{\"");
        for (String key : map.keySet()) {
            String value = map.get(key);
            sb.append(key).append("\":").append("\"").append(value).append("\",\"");
        }
        String json = sb.toString().substring(0,sb.length()-1);
        json += "}";
        return json;
    }

    /**
     * 获取UserAgent
     * @return
     */
//	public static String getUserAgent(){
//		return "SLRC/2.0 (Android)";
//	}

    /**
     * 判断是否是手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean phoneNumberLocalValidate(String phoneNumber) {
        // 通用号段
        Pattern pcommon = Pattern.compile("^((13[0-9])|(14[5,7])|(15[0-9])|(17[0-1,6-8])|(18[0-9]))\\d{8}$");
        return  pcommon.matcher(phoneNumber).matches();
    }

    /**
     * 检测用户名是否为纯数字
     *
     * @param accountName
     * @return
     */
    public static boolean isNumberOnly(String accountName) {
        char ch = ' ';
        for (int i = 0; i < accountName.length(); i++) {
            ch = accountName.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }

    /*
     * 验证邮箱的正则表达式
     */
    public static boolean checkEmail(String email) {
        String format = ".{1,}[@].{1,}[.].{1,}";
        if (email.matches(format)) {
            return true;// 邮箱名合法，返回true
        } else {
            return false;// 邮箱名不合法，返回false
        }
    }

    /**
     * 获取Wifi Mac地址
     *   很不准确！
     * @return
     */
//    public static String getWifiMacAddr(){
//        WifiManager wifi = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = wifi.getConnectionInfo();
//        return info.getMacAddress();
//    }

    /**
     * 获取本地 Mac地址
     * @return
     */
    public static String getLocalMacAddr(){
        String macSerial = null;
        String str = "";

        try
        {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;)
            {
                str = input.readLine();
                if (str != null)
                {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }
}
