package com.geekware.geekware;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Custom_Buy_Adapter extends BaseAdapter {
    private ArrayList<Custom_Buy_Item> listData;
    private LayoutInflater layoutInflater;
    Context context;
    public Custom_Buy_Adapter(Context aContext, ArrayList<Custom_Buy_Item> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
        context=aContext;
    }

    @Override
    public int getCount() {
        if(listData.size()>20){
            return 20;
        }
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
            convertView = layoutInflater.inflate(R.layout.buy_item, null);
            holder = new ViewHolder();
            holder.contact  = (TextView) convertView.findViewById(R.id.ct);
            holder.used  = (TextView) convertView.findViewById(R.id.used);
            holder.seller = (TextView) convertView.findViewById(R.id.sellinf);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.cost=(TextView)convertView.findViewById(R.id.sf);
            holder.pic=(ImageView)convertView.findViewById(R.id.pic);




            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position<0)
            return convertView;
        final Custom_Buy_Item li = listData.get(position);
        holder.contact.setText(li.getContact()+"");
        holder.used.setText(li.getUsed()+"");
        holder.cost.setText(li.getPrice()+"");
        holder.seller.setText(li.getSeller()+"");
        holder.name.setText(li.getItem()+"");
        if(li.getBmp()!=null)
        holder.pic.setImageBitmap(li.getBmp());

       /* if(li.getCol()==1){
            holder.from.setBackgroundColor(Color.rgb(74,68,86));
            holder.to.setBackgroundColor(Color.rgb(74,68,86));
            holder.time.setBackgroundColor(Color.rgb(74,68,86));
            holder.bus.setBackgroundColor(Color.rgb(74,68,86));
        }
        else {

        }*/

        return convertView;
    }

    static class ViewHolder {
        TextView seller;
        TextView cost;
        TextView name;
        TextView contact;
        TextView used;
        ImageView pic;

    }
}