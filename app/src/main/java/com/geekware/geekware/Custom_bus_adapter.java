package com.geekware.geekware;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Custom_bus_adapter extends BaseAdapter {
    private ArrayList<Custom_bus_Item> listData;
    private LayoutInflater layoutInflater;
    Context context;
    public Custom_bus_adapter(Context aContext, ArrayList<Custom_bus_Item> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        context=aContext;
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
            convertView = layoutInflater.inflate(R.layout.bus_list_item, null);
            holder = new ViewHolder();
            holder.from  = (TextView) convertView.findViewById(R.id.from);
            holder.to  = (TextView) convertView.findViewById(R.id.to);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.bus=(TextView)convertView.findViewById(R.id.num);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position<0)
            return convertView;
        final Custom_bus_Item li = listData.get(position);
        holder.time.setText(li.getTime());
        holder.from.setText(li.getFrom());
        holder.to.setText(li.getTo());
        holder.bus.setText(li.getBus_no()+"");
       /* if(li.getCol()==1){
            holder.from.setBackgroundColor(Color.rgb(74,68,86));
            holder.to.setBackgroundColor(Color.rgb(74,68,86));
            holder.time.setBackgroundColor(Color.rgb(74,68,86));
            holder.bus.setBackgroundColor(Color.rgb(74,68,86));
        }
        else {

        }*/
        int x=li.getCol();
        if(x!=0)
        ((LinearLayout)convertView.findViewById(R.id.busitem)).setBackgroundColor(Color.rgb(200-5*x,200-5*x,200-5*x));
        else
            ((LinearLayout)convertView.findViewById(R.id.busitem)).setBackgroundColor(Color.rgb(240,240,240));
        //convertView.notifyAll();
        ((LinearLayout)convertView.findViewById(R.id.busitem)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(li.getUsers())
                        .setTitle("Users")
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView time;
        TextView from;
        TextView to;
        TextView bus;

    }
}