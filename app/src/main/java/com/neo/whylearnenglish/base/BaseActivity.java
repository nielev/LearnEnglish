package com.neo.whylearnenglish.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.neo.whylearnenglish.R;
import com.neo.whylearnenglish.ui.MainActivity;
import com.neo.whylearnenglish.utils.UIUtils;

import java.util.List;

import static android.os.Build.VERSION.SDK_INT;

/**
 * 所有activity的基类
 * Created by Neo on 2016/8/30.
 */
public abstract class BaseActivity extends AppCompatActivity{
    public static BaseActivity mForegroundActivity;
    private boolean isStartStatusColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
        if(isStartStatusColor){
            setColor(this,0xff30469b);
        }
        
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    public void isStartStatusColor(boolean isStartStatusColor) {
        this.isStartStatusColor = isStartStatusColor;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mForegroundActivity = this;
    }

    public static BaseActivity getForegroundActivity(){
        return mForegroundActivity;
    }

    @Override
    public void onBackPressed() {
        if(getForegroundActivity() instanceof MainActivity){
            showDialog();
        } else {
            super.onBackPressed();
        }
    }
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.tips);
        builder.setMessage(R.string.exit_message);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    public static void setColor(Activity activity, int color){

        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (color !=- 1) {
                activity.getWindow().setStatusBarColor(color);
            }
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content));
            View statusView = rootView.getChildAt(color);
            //改变颜色时避免重复添加statusBarView
            if(statusView != null && statusView.getMeasuredHeight() == UIUtils.getStatusBarHeight(activity)) {
                statusView.setBackgroundColor(color);
                return;
            }
            statusView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    UIUtils.getStatusBarHeight(activity));
            statusView.setBackgroundColor(color);
            rootView.addView(statusView, lp);
        }
    }

}
