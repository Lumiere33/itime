package com.jnu.itime;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.jnu.itime.NewActivity.RESULT_NEW;
import static com.jnu.itime.TimeActivity.RESULT_DELETE;
import static com.jnu.itime.TimeActivity.RESULT_UPDATE;

import static com.jnu.itime.MainActivity.saver;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListViewFragment extends Fragment {

    public static final int REQUEST_CODE_NEW = 901;
    public static final int REQUEST_CODE_UPDATE = 902;
    public static final int REQUEST_CODE_DELETE = 903;

    static List<TimeItem> Timelist = new ArrayList<>();
    static TimeAdapter adapter;
    Timer mTimer;

    public ListViewFragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);

        ListView listViewSuper = (ListView) view.findViewById(R.id.list_view_times);
        adapter = new TimeAdapter(ListViewFragment.this.getActivity(), R.layout.list_item, Timelist);
        listViewSuper.setAdapter(adapter);

        init();

        //处理点击事件
        listViewSuper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(ListViewFragment.this.getActivity(), TimeActivity.class);
                intent.putExtra("position", position);
                startActivityForResult(intent,904);
            }
        });

        this.registerForContextMenu(listViewSuper);
        // Inflate the layout for this fragment

        mTimer = new Timer();
        startRun();

        return view;
    }

    public void init() {
        Timelist.add(new TimeItem("元旦节", "每年", 2019, 1, 1, BitmapFactory.decodeResource(getResources(),R.drawable.pic_time1)));
        Timelist.add(new TimeItem("生日", "每年", 2019, 8, 13, BitmapFactory.decodeResource(getResources(),R.drawable.pic_time2)));
        Timelist.add(new TimeItem("放寒假", "一次", 2020, 1, 9, BitmapFactory.decodeResource(getResources(),R.drawable.user_head)));
        Timelist.add(new TimeItem("运动会", "一次", 2019, 11, 28, BitmapFactory.decodeResource(getResources(),R.drawable.pic_time4)));
    }

    //点击事件回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_DELETE: {
                int position = data.getIntExtra("DeletePosition", -1);
                Timelist.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(ListViewFragment.this.getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    //ListView适配器
    class TimeAdapter extends ArrayAdapter<TimeItem> implements ListAdapter {
        private int resourceId;

        TimeAdapter(Context context, int resource, List<TimeItem> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId, null);

            ImageView img = item.findViewById(R.id.image_view_time);
            TextView count=item.findViewById(R.id.text_view_count);
            TextView name = item.findViewById(R.id.text_view_name);
            TextView date = item.findViewById(R.id.text_view_date);

            TimeItem time_item = this.getItem(position);
            String str;
            if(time_item.getCount()!=null && time_item.getCount().contains("秒")) {
                if (time_item.getCount().contains("天"))
                    str = "" + (Integer.parseInt(time_item.getCount().substring(0, time_item.getCount().indexOf("天"))) + 1) + "天";
                else str = "1天";
            }
            else str="已到期";
            img.setImageBitmap(time_item.getPicture());
            count.setText(str);
            name.setText(time_item.getName());
            date.setText(time_item.getDate());
            return item;
        }
    }

    /***
     * 获取未来时间距离当前日期的日期差值
     * 实现倒计时功能
     * @param endTime
     * @return
     */
    public String dateDiff(String endTime,int i) {
        String strTime = null;
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = sd.format(curDate);
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(str).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            if (day >= 1) {
                strTime = day + "天" + hour + "时"+ min + "分" + sec + "秒";
            } else {

                if (hour >= 1) {
                    strTime = hour + "时" + min + "分" + sec + "秒";

                } else {
                    if (min >= 1) {
                        strTime = min + "分" + sec + "秒";
                    } else {
                        if (sec >= 1) {
                            strTime = sec + "秒";
                        } else {
                            countPeriod(i);
                            strTime = "已到期";
                        }
                    }
                }
            }
            return strTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //计算周期循环
    public void countPeriod(int position){
        int cyear=ListViewFragment.Timelist.get(position).getYear();
        int cmonth=ListViewFragment.Timelist.get(position).getMonth();
        int cday=ListViewFragment.Timelist.get(position).getDay();

        switch(ListViewFragment.Timelist.get(position).getPeriod()){
            case "一次":{
                break;
            }
            case "每周":{
                //获取Calendar的实例，默认是当前的时间
                Calendar calendar = Calendar.getInstance();
                DateFormat formatter=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date=null;
                try {
                    date=formatter.parse(cyear+"-"+cmonth+"-"+cday+" 00:00:00");
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                calendar.setTime(date);
                //当前时间 加7天
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                ListViewFragment.Timelist.get(position).setYear(calendar.get(Calendar.YEAR));
                ListViewFragment.Timelist.get(position).setMonth(calendar.get(Calendar.MONTH)+1);
                ListViewFragment.Timelist.get(position).setDay(calendar.get(Calendar.DAY_OF_MONTH));
                break;
            }
            case "每月":{
                if(cmonth<12) {
                    ListViewFragment.Timelist.get(position).setMonth(cmonth + 1);
                }
                else{
                    ListViewFragment.Timelist.get(position).setYear(cyear+1);
                    ListViewFragment.Timelist.get(position).setMonth(1);
                }
                break;
            }
            case "每年":{
                ListViewFragment.Timelist.get(position).setYear(cyear+1);
                break;
            }
        }
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
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                for(int i=0;i<Timelist.size();i++){
                    String countTime;
                    countTime=Timelist.get(i).getYear()+"-"+Timelist.get(i).getMonth()+"-"+Timelist.get(i).getDay()+" 00:00:000";
                    Timelist.get(i).setCount(dateDiff(countTime,i));
                }
                adapter.notifyDataSetChanged();
            }
        }
    };
}

