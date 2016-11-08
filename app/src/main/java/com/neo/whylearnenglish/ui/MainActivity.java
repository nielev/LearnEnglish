package com.neo.whylearnenglish.ui;


import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.neo.whylearnenglish.R;
import com.neo.whylearnenglish.base.BaseActivity;
import com.neo.whylearnenglish.ui.fragment.FeebackFragment;
import com.neo.whylearnenglish.ui.fragment.PlanFragment;
import com.neo.whylearnenglish.ui.fragment.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Neo on 2016/11/7.
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        //设置抽屉DrawerLayout
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();//初始化状态
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //禁止默认图标
//        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mToolbar.setNavigationIcon(R.mipmap.icon);
    }


    @Override
    protected void initListener() {
        //设置导航栏NavigationView的点击事件
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_plan:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,new PlanFragment()).commit();
                        mToolbar.setTitle("我的计划");
                        break;
                    case R.id.nav_feeback:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,new FeebackFragment()).commit();
                        mToolbar.setTitle("建议反馈");
                        break;
                    case R.id.nav_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,new SettingFragment()).commit();
                        mToolbar.setTitle("设置");
                        break;
                }
                menuItem.setChecked(true);//点击了把它设为选中状态
                mDrawerLayout.closeDrawers();//关闭抽屉
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
