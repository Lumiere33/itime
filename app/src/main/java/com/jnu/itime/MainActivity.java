package com.jnu.itime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


import static com.jnu.itime.ListViewFragment.adapter;
import static com.jnu.itime.NewActivity.RESULT_NEW;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NEW = 901;

    //设置主页面FrameLayout
    FrameLayout mFrameLayout;
    //设置抽屉DrawerLayout
    DrawerLayout mDrawerLayout;
    //设置新建按钮buttonNew
    ImageButton buttonNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        try {
            //使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            //隐藏toolbar上的app title
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            //先启用home as up：即ToolBar左边那个打开侧边栏那个按钮（不开启点击按钮无反应的）
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //修改标题颜色
            mToolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //设置主页面
        mFrameLayout=findViewById(R.id.fragment_list_view);

        //设置抽屉DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();//初始化状态
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //设置新建按钮buttonNew
        buttonNew=findViewById(R.id.new_time);
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NewActivity.class);
                startActivityForResult(intent,REQUEST_CODE_NEW);
            }
        });

        //设置导航栏NavigationView的点击事件
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nv_menu);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.item_home:
                        //打开Fragment
                        //           getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new FragmentTwo()).commit();
                        //           mToolbar.setTitle("首页");
                        Toast.makeText(MainActivity.this,"首页~",Toast.LENGTH_LONG).show();
                        break;

                    case R.id.item_background:
                        //打开Fragment
                        //           getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new FragmentThree()).commit();
                        //           mToolbar.setTitle("关于我们");
                        Toast.makeText(MainActivity.this,"信息~",Toast.LENGTH_LONG).show();
                        break;

                    case R.id.item_help:
                    //打开Fragment
                    //           getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new FragmentOne()).commit();
                    //           mToolbar.setTitle("帮助");
                    Toast.makeText(MainActivity.this,"帮助~",Toast.LENGTH_LONG).show();
                    break;
                }

                menuItem.setChecked(true);//点击了把它设为选中状态
                mDrawerLayout.closeDrawers();//关闭抽屉
                return true;
            }
        });

        //为头布局headerLayout上的控件添加监听事件；
        //先获得头部的View，再findViewByid()即可;如：
        View headerView = mNavigationView.getHeaderView(0);
        TextView name = (TextView) headerView.findViewById(R.id.id_name);
    }


    /**
     * 打开侧滑栏（toolbar左边那个按钮）
     * （在home菜单被点击的时候，打开drawer；不写此方法点击那按钮会没反应）
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //新建item回调处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_NEW:{
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "新建成功", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

}

