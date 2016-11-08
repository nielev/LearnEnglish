package com.neo.whylearnenglish.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import com.neo.whylearnenglish.R;
import com.neo.whylearnenglish.base.BaseFragment;

/**
 * Created by Neo on 2016/11/8.
 */

public class SettingFragment extends BaseFragment {

    @Override
    protected View initView() {
        return View.inflate(mActivity, R.layout.fragment_setting, null);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
