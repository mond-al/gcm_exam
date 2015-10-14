package com.zzisoo.gcmsenderapp;

/**
 * Created by yangjisoo on 15. 9. 11..
 */
public class GcmData {
    public GcmData() {
        this.to = "";
        this.data = new Data();
        this.priority = "high";
    }

    public class Data {
        public String title;
        public String body;
    }

    public String to;
    public Data data;
    public String priority;
}
