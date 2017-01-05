package com.neo.whylearnenglish.ui;


import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.neo.whylearnenglish.R;
import com.neo.whylearnenglish.base.BaseActivity;
import com.neo.whylearnenglish.bean.Letter;
import com.neo.whylearnenglish.dao.HttpMethods;
import com.neo.whylearnenglish.ui.fragment.FeebackFragment;
import com.neo.whylearnenglish.ui.fragment.PlanFragment;
import com.neo.whylearnenglish.ui.fragment.SettingFragment;
import com.neo.whylearnenglish.utils.DownLoadRequest;
import com.neo.whylearnenglish.utils.HttpRequest;
import com.neo.whylearnenglish.utils.LogUtil;
import com.neo.whylearnenglish.utils.UIUtils;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by Neo on 2016/11/7.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ll_main)
    LinearLayout mLl_main;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.search_view)
    MaterialSearchView mSearchView;
    @BindView(R.id.btn_down_offletters)
    Button mBtn_down_offletters;
    @BindView(R.id.btn_stop)
    Button mBtn_stop;
    @BindView(R.id.btn_continue)
    Button mBtn_continue;

    private ActionBarDrawerToggle mDrawerToggle;
    private View mHeaderView;
    private DownLoadRequest request;


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

        mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        mSearchView.setEllipsize(true);
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,new PlanFragment(), "plan").commit();
                        break;
                    case R.id.nav_feeback:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,new FeebackFragment(), "feeback").commit();
                        break;
                    case R.id.nav_setting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.content,new SettingFragment(), "setting").commit();
                        break;
                }

                mDrawerLayout.closeDrawers();//关闭抽屉
                return true;
            }
        });
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //订阅查询逻辑
                subcribeQuery(query.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                // TODO: 2016/12/21
                LogUtil.i(TAG, "searchView show...");
            }

            @Override
            public void onSearchViewClosed() {
                // TODO: 2016/12/21
                LogUtil.i(TAG, "searchView close...");
            }
        });
        mBtn_down_offletters.setOnClickListener(this);
        mBtn_stop.setOnClickListener(this);
        mBtn_continue.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    public void setToolBarVisibility(boolean isVisible){
        mToolbar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 订阅单词查询的网络请求
     * @param query
     */
    private void subcribeQuery(String query) {
        Subscriber<Letter> subscriber = new Subscriber<Letter>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Letter letter) {
                Intent intent = new Intent(MainActivity.this,LetterActivity.class);
                if(null != letter &&  null != letter.posList && letter.posList.size() > 0){
                    intent.putExtra("letter",letter);
                    getForegroundActivity().startActivity(intent);
                } else {
                    UIUtils.showInMainThread("查不到单词");
                }
            }
        };
        HttpMethods.getInstance().getLetter(subscriber, query);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_down_offletters:
                request = new DownLoadRequest(HttpRequest.getInstance().mOkhttpClient);
                request.downloadRequest("http://192.168.10.200:8080/Androidnxzh.rar","Androidnxzh.rar");
//                HttpRequest.getInstance().downloadRequest("http://192.168.10.200:8080/Androidnxzh.rar","Androidnxzh.rar");
                break;
            case R.id.btn_stop:
                request.downloadStop();
                break;
            case R.id.btn_continue:
                long stopPoint = request.getStopPoint();
                Log.i(HttpRequest.TAG, "click continue...stopPoint:" + stopPoint);
                request.downloadContinueRequest("http://192.168.10.200:8080/Androidnxzh.rar","Androidnxzh.rar", stopPoint);
                break;

        }
    }
}
