package com.geekware.geekware;

import android.graphics.Bitmap;

/**
 * Created by gkalyan0510 on 1/12/2016.
 */
public class Custom_Buy_Item {
    private Bitmap bmp;
    private String item;
    private int used;
    private int price;
    private String contact;
    private String seller;
    public Custom_Buy_Item(Bitmap b,String nam,String selinf,int p,String ct,int usd){
        bmp=b;
        item=nam;
        seller=selinf;
        price=p;
        contact=ct;
        used=usd;
    }
    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public int getPrice() {
        return price;
    }

    public int getUsed() {
        return used;
    }

    public String getContact() {
        return contact;
    }

    public String getItem() {
        return item;
    }

    public String getSeller() {
        return seller;
    }


}
