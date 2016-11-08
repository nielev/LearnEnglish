package com.neo.whylearnenglish.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.neo.whylearnenglish.R;
import com.neo.whylearnenglish.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 引导页面
 * Created by Neo on 2016/11/7.
 */

public class GuideActivity extends BaseActivity {

    @BindView(R.id.btn_confirm)
    Button mBtn_confirm;

    @BindView(R.id.sp_reason)
    Spinner mSp_reason;

    @BindView(R.id.sp_interval)
    Spinner mSp_interval;

    @BindView(R.id.sp_level_now)
    Spinner mSp_level_now;

    @BindView(R.id.sp_level_goal)
    Spinner mSp_level_goal;

    @BindView(R.id.tv_reason)
    TextView mTv_reason;

    @BindView(R.id.tv_interval)
    TextView mTv_interval;

    @BindView(R.id.tv_level_now)
    TextView mTv_level_now;

    @BindView(R.id.tv_level_goal)
    TextView mTv_level_goal;

    private String[] mArrays_reason;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        setTitle(R.string.guide_name);

    }

    @Override
    protected void initData() {
        mArrays_reason = getResources().getStringArray(R.array.array_reason);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, mArrays_reason);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSp_reason.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        mSp_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == mArrays_reason.length - 1){
                    mTv_reason.setVisibility(View.VISIBLE);
                } else {
                    mTv_reason.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick(R.id.btn_confirm)
    void intoMain(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
