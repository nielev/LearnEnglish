package com.neo.whylearnenglish.ui;

import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.neo.whylearnenglish.R;
import com.neo.whylearnenglish.base.BaseActivity;
import com.neo.whylearnenglish.global.SPCode;
import com.neo.whylearnenglish.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 闪屏页，加载动画
 * Created by Neo on 2016/11/7.
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.ll_sentences)
    LinearLayout mLl_senteces;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        // 渐变
        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(3000);
        animAlpha.setFillAfter(true);
        mLl_senteces.startAnimation(animAlpha);
        animAlpha.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 判断是否需要跳到新手引导
                boolean isGuideShow = SPUtils.getBoolean(SPCode.IS_GUIDE_SHOW,
                        false, SplashActivity.this);

                if (isGuideShow) {
                    // 动画结束后跳主页面
                    startActivity(new Intent(SplashActivity.this,
                            MainActivity.class));
                } else {
                    // 跳到新手引导
                    startActivity(new Intent(SplashActivity.this,
                            GuideActivity.class));
                }

                finish();
            }
        });
    }
}
