package com.jnu.itime;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Saver {
    Context context;
    ArrayList<TimeItem> times=new ArrayList<TimeItem>();

    public Saver(Context context) {
        this.context = context;
    }
    public ArrayList<TimeItem> getTimes() {
        return times;
    }
    public void setTimes(ArrayList<TimeItem> times) {
        this.times = times;
    }

    public void save() {
        try{
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("Serializable.txt",Context.MODE_PRIVATE)
            );
            outputStream.writeObject(times);
            outputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<TimeItem> load(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("Serializable.txt"));
            times = (ArrayList<TimeItem>) inputStream.readObject();
            inputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return times;
    }

}
