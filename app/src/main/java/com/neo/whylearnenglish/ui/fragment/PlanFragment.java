package com.neo.whylearnenglish.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.neo.whylearnenglish.R;
import com.neo.whylearnenglish.base.BaseFragment;
import com.neo.whylearnenglish.global.Constant;
import com.neo.whylearnenglish.ui.MainActivity;
import com.neo.whylearnenglish.utils.HttpRequest;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;

/**
 *
 * Created by Neo on 2016/11/8.
 */

public class PlanFragment extends BaseFragment implements View.OnClickListener {

    private MainActivity mainActivity;
    private View mView;
    private ImageView mIv_return;

    @Override
    protected View initView() {
        mainActivity = (MainActivity) mActivity;
        mView = View.inflate(mainActivity, R.layout.fragment_plan, null);
        mIv_return = (ImageView) mView.findViewById(R.id.iv_return);
        return mView;
    }

    @Override
    protected void initData() {
        mainActivity.setToolBarVisibility(false);
    }

    @Override
    protected void initListener() {
        mIv_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return:
                FragmentManager fm = mainActivity.getSupportFragmentManager();
                Fragment plan = fm.findFragmentByTag("plan");
                fm.beginTransaction().remove(plan).commit();

                break;
        }
    }

    @Override
    public void onDestroy() {
        mainActivity.setToolBarVisibility(true);
        super.onDestroy();
    }
}
