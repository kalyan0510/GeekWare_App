package com.geekware.geekware;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Custom_lect_list_adapter extends BaseAdapter {
    private ArrayList<Custom_lect_list_item> listData;
    private LayoutInflater layoutInflater;

    public Custom_lect_list_adapter(Context aContext, ArrayList<Custom_lect_list_item> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.lecture_list_item, null);
            holder = new ViewHolder();
            holder.Lect  = (TextView) convertView.findViewById(R.id.lecture);
            holder.start  = (TextView) convertView.findViewById(R.id.start_time);
            holder.end = (TextView) convertView.findViewById(R.id.end_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Custom_lect_list_item li = listData.get(position);
        holder.Lect.setText(li.getLecture());
        holder.start.setText(li.getStart_t());
        holder.end.setText(li.getEnd_t());
        /*String str[] = li.getStart_t().split(":");
        String end[] = li.getEnd_t().split(":");
        int i;
        holder.Lect.setText("*" + str[0] + "-" + end[0] + "*");
        str[0].trim();
        end[0].trim();
        if(str[0]==""||end[0]==""){
            i=1;
        }else{
            i= Integer.parseInt(end[0])-
                    Integer.parseInt(str[0]);
        }
        if(i==0)
            i++;
        holder.Lect.setHeight(holder.Lect.getHeight()*i);
        holder.end.setHeight(holder.end.getHeight()*i);
        holder.start.setHeight(holder.start.getHeight()*i);
      // convertView.setLayoutParams(new ViewGroup.LayoutParams(parent.getWidth(),parent.getHeight()*i));*/
        return convertView;
    }

    static class ViewHolder {
        TextView Lect;
        TextView start;
        TextView end;
    }
}