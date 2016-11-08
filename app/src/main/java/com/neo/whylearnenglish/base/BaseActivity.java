package com.neo.whylearnenglish.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.neo.whylearnenglish.R;
import com.neo.whylearnenglish.ui.MainActivity;

import java.util.List;

/**
 * 所有activity的基类
 * Created by Neo on 2016/8/30.
 */
public abstract class BaseActivity extends AppCompatActivity{
    public static BaseActivity mForegroundActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();


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
}
