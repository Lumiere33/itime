package com.jnu.itime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static com.jnu.itime.ListViewFragment.REQUEST_CODE_UPDATE;
import static com.jnu.itime.ListViewFragment.adapter;

public class TimeActivity extends AppCompatActivity {

    public static final int RESULT_UPDATE = 902;
    public static final int RESULT_DELETE = 903;

    ImageView imageView_time;
    TextView textView_name;
    TextView textView_date;
    TextView textView_count;
    int position;
    Timer mTimer;
    Boolean exit =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.edit_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //设置返回键
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置内容页面
        imageView_time=findViewById(R.id.time_image);
        textView_name=findViewById(R.id.time_name);
        textView_date=findViewById(R.id.time_date);
        textView_count=findViewById(R.id.time_count);
        mTimer = new Timer();

        //显示TimeItem的内容
        position=getIntent().getIntExtra("position",-1);
        imageView_time.setImageBitmap(ListViewFragment.Timelist.get(position).getPicture());
        textView_name.setText(ListViewFragment.Timelist.get(position).getName());
        startRun();
    }


    //设置编辑和删除按键
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }
    //处理编辑和删除事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //处理删除事件
        if (id == R.id.action_delete) {
            new android.app.AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("询问")
                    .setMessage("你确定要删除这条吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int position=getIntent().getIntExtra("position",-1);
                            Intent intent=new Intent(TimeActivity.this,MainActivity.class);
                            intent.putExtra("DeletePosition",position);
                            exit=true;
                            setResult(RESULT_DELETE,intent);
                            TimeActivity.this.finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create().show();
        }

        //处理编辑事件
        else if(id == R.id.action_edit) {
            int position=getIntent().getIntExtra("position",-1);
            Intent intent=new Intent(TimeActivity.this,NewActivity.class);
            intent.putExtra("EditPosition",position);
            startActivityForResult(intent,REQUEST_CODE_UPDATE);
        }
        return super.onOptionsItemSelected(item);
    }


    //实时刷新倒计时
    private void startRun() {
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 1;
                timeHandler.sendMessage(message);
            }
        };
        mTimer.schedule(mTimerTask,0,1000);
    }
    @SuppressLint("HandlerLeak")
    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (exit == false && msg.what == 1) {
                textView_date.setText(ListViewFragment.Timelist.get(position).getDate());
                textView_count.setText(ListViewFragment.Timelist.get(position).getCount());
            }
        }
    };

    //编辑事件回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_UPDATE:{
                adapter.notifyDataSetChanged();
                Toast.makeText(TimeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,TimeActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
                this.finish();
                break;
            }
        }
    }
}
