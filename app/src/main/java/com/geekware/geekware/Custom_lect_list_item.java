package com.geekware.geekware;

/**
 * Created by gkalyan0510 on 12/16/2015.
 */
public class Custom_lect_list_item {
    private String Lecture;
    private String start_t;
    private String end_t;
    public Custom_lect_list_item(String l,String s, String e){
        Lecture=l;
        start_t=s;
        end_t=e;
    }

    public void setEnd_t(String end_t) {
        this.end_t = end_t;
    }

    public void setLecture(String lecture) {
        Lecture = lecture;
    }

    public void setStart_t(String start_t) {
        this.start_t = start_t;
    }

    public String getEnd_t() {
        return end_t;
    }

    public String getLecture() {
        return Lecture;
    }

    public String getStart_t() {
        return start_t;
    }
}
