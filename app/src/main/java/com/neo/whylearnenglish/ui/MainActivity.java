package com.neo.whylearnenglish.ui;


import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.neo.whylearnenglish.R;
import com.neo.whylearnenglish.base.BaseActivity;
import com.neo.whylearnenglish.bean.Letter;
import com.neo.whylearnenglish.dao.HttpMethods;
import com.neo.whylearnenglish.ui.fragment.FeebackFragment;
import com.neo.whylearnenglish.ui.fragment.PlanFragment;
import com.neo.whylearnenglish.ui.fragment.SettingFragment;
import com.neo.whylearnenglish.utils.LogUtil;
import com.neo.whylearnenglish.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by Neo on 2016/11/7.
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ll_main)
    LinearLayout mLl_main;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;


    private ActionBarDrawerToggle mDrawerToggle;
    private View mHeaderView;
    private SearchView mSearchView;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        isStartStatusColor(true);
    }

    @Override
    protected void initData() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        //设置抽屉DrawerLayout
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();//初始化状态
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        //禁止默认图标
//        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mToolbar.setNavigationIcon(R.mipmap.icon_nav);

        mHeaderView = mNavigationView.getHeaderView(0);

    }


    @Override
    protected void initListener() {
        mHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MAINACTIVITY","头部点击");
            }
        });
        //设置导航栏NavigationView的点击事件
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_plan:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,new PlanFragment()).commit();
//                        mToolbar.setNavigationIcon();
//                        Toast.makeText(MainActivity.this, "我的计划", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_feeback:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,new FeebackFragment()).commit();
                        break;
                    case R.id.nav_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,new SettingFragment()).commit();
                        break;
                }

//                menuItem.setChecked(true);//点击了把它设为选中状态

                mDrawerLayout.closeDrawers();//关闭抽屉
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                UIUtils.showInMainThread("查询"+query);
                Subscriber<Letter> subscriber = new Subscriber<Letter>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "Get Letter Completed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Letter letter) {
                        LogUtil.i("LETTER",letter.toString());
                    }
                };
//                HttpMethods.getInstance().getLetter(subscriber, "subscriber");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                break;
//            case R.id.action_share:
//                Toast.makeText(this,"share",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.action_settings:
//                Toast.makeText(this,"settings",Toast.LENGTH_SHORT).show();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
