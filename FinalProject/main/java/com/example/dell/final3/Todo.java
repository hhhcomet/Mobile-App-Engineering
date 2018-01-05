package com.example.dell.final3;

//Written by Jingxuan Chen

import java.io.Serializable;

public class Todo implements Serializable {
    public   String name;
    public   String time;

    public boolean pic;
    public Todo(String name, String time)
    {
        this.name=name;
        this.time=time;
        this.pic=false;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time=time;
    }
    public boolean getPic() {
        return pic;
    }
    public void setPic() {
        this.pic=true;
    }
}
