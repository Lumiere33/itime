package com.jnu.itime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static com.jnu.itime.ListViewFragment.REQUEST_CODE_NEW;
import static com.jnu.itime.ListViewFragment.Timelist;
import static com.jnu.itime.TimeActivity.RESULT_UPDATE;

public class NewActivity extends AppCompatActivity {

    public static final int RESULT_NEW = 901;
    ImageView editImage;
    EditText editName;
    EditText editDescription;
    TextView editDate;
    TextView editPeriod;
    LinearLayout dateLayout,periodLayout,pictureLayout;
    int[] mDate = new int[3];
    int position;//若是修改item，则传入item的位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        editImage = findViewById(R.id.edit_image);
        editName = findViewById(R.id.edit_name);
        editDescription = findViewById(R.id.edit_description);
        editDate = findViewById(R.id.edit_date);
        editPeriod = findViewById(R.id.edit_period);
        dateLayout = findViewById(R.id.layout_date);
        periodLayout = findViewById(R.id.layout_period);
        pictureLayout = findViewById(R.id.layout_picture);

        //默认背景图片
        editImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.pic4));
        //默认周期
        editPeriod.setText("一次");

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.new_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //如果是修改item则自动补充item内容
        position=getIntent().getIntExtra("EditPosition",-1);
        if(position!=-1) {
            editImage.setImageBitmap(Timelist.get(position).getPicture());
            editName.setText(ListViewFragment.Timelist.get(position).getName());
            editDate.setText(ListViewFragment.Timelist.get(position).getDate());
            editPeriod.setText(ListViewFragment.Timelist.get(position).getPeriod());
            mDate[0]=ListViewFragment.Timelist.get(position).getYear();
            mDate[1]=ListViewFragment.Timelist.get(position).getMonth();
            mDate[2]=ListViewFragment.Timelist.get(position).getDay();
        }

        //设置返回键
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置弹出日历窗口
        //点击编辑日期
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcalendar = Calendar.getInstance();     //  获取当前时间    —   年、月、日
                int year = mcalendar.get(Calendar.YEAR);         //  得到当前年
                int month = mcalendar.get(Calendar.MONTH);       //  得到当前月
                final int day = mcalendar.get(Calendar.DAY_OF_MONTH);  //  得到当前日

                new DatePickerDialog(NewActivity.this, new DatePickerDialog.OnDateSetListener() {      //  日期选择对话框
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //  这个方法是得到选择后的 年，月，日，分别对应着三个参数 — year、month、dayOfMonth
                        mDate[0] =year;
                        mDate[1]=month+1;
                        mDate[2]=dayOfMonth;
                        editDate.setText(year + "年" + (month+1) + "月" + dayOfMonth + "日");
                    }
                }, year, month, day).show();
            }//  弹出日历对话框时，默认显示 年，月，日
        });

        //点击编辑周期
        periodLayout.setOnClickListener(new View.OnClickListener() {
            AlertDialog alertDialog1;
            @Override
            public void onClick(View view) {
                final String[] items = {"一次", "每周", "每月", "每年"};
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(NewActivity.this);
                alertBuilder.setTitle("选择周期");
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editPeriod.setText(items[i]);
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });

        //点击编辑背景图片
        pictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                galleryIntent.setType("image/*");//图片
                startActivityForResult(galleryIntent,1);//跳转，传递打开相册请求码
            }
        });
    }

    //设置确认健
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_confirm) {
            //处理新建事件
            if (position == -1) {
                if (editName.length() != 0 && editDate.length() != 0) {
                    Intent intent = new Intent(NewActivity.this, MainActivity.class);
                    Timelist.add(new TimeItem(editName.getText().toString(), editPeriod.getText().toString(), mDate[0], mDate[1], mDate[2], ((BitmapDrawable) editImage.getDrawable()).getBitmap()));
                    setResult(RESULT_NEW, intent);
                    NewActivity.this.finish();
                } else
                    Toast.makeText(NewActivity.this, "标题和日期不能为空！", Toast.LENGTH_LONG).show();
            }
            //处理修改事件
            else{
                if (editName.length() != 0 && editDate.length() != 0) {
                    Intent intent = new Intent(NewActivity.this, TimeActivity.class);
                    Timelist.get(position).setPicture(((BitmapDrawable) editImage.getDrawable()).getBitmap());
                    Timelist.get(position).setName(editName.getText().toString());
                    Timelist.get(position).setYear(mDate[0]);
                    Timelist.get(position).setMonth(mDate[1]);
                    Timelist.get(position).setDay(mDate[2]);
                    Timelist.get(position).setPeriod(editPeriod.getText().toString());
                    setResult(RESULT_UPDATE, intent);
                    NewActivity.this.finish();
                } else
                    Toast.makeText(NewActivity.this, "标题和日期不能为空！", Toast.LENGTH_LONG).show();
            }
        }

            return super.onOptionsItemSelected(item);
    }

    //打开相册返回图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK) {
            return;
        }else{
            switch (requestCode){
                case 1:
                    Uri uri=data.getData();
                    editImage.setImageURI(uri);
                    break;
            }
        }
    }

}