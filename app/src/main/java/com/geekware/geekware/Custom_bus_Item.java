package com.geekware.geekware;

/**
 * Created by gkalyan0510 on 12/19/2015.
 */
public class Custom_bus_Item {
    private String time;
    private String day_avail;
    private int bus_no;
    private String from;
    private String to;
    private String users;
    int Col;
    Custom_bus_Item(String time,String from,String to,String day_avail,int num,String user,int col){
        this.time=time;
        this.from=from;
        this.to=to;
        this.day_avail=day_avail;
        this.bus_no=num;
        this.users=user;
        Col=col;
    }

    public String getTime() {
        return time;
    }

    public String getDay_avail() {
        return day_avail;
    }

    public int getCol() { return Col; }

    public int getBus_no() {
        return bus_no;
    }

    public String getFrom() {
        return from;
    }

    public String getUsers() {
        return users;
    }

    public String getTo() {
        return to;
    }
}
