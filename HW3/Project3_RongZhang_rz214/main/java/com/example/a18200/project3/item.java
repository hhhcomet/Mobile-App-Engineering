package com.example.a18200.project3;

import java.io.Serializable;

/**
 * Created by 18200 on 2017/11/9.
 */

public class item implements Serializable {
    public double longi;
    public double lati;
    public String time;
    public String addr;
    public String name;

    public item(double longi, double lati, String time, String addr, String name) {
        this.longi = longi;
        this.lati = lati;
        this.time = time;
        this.addr = addr;
        this.name=name;
    }
}
