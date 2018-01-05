package com.example.dell.final3;

import java.io.Serializable;

//Written by Xiuqi Ye

public class Member implements Serializable {
    public String name;
    public boolean checked;
    public Member(String name)
    {
        this.name=name;
        this.checked=false;
    }
    public Member(String name, boolean c)
    {
        this.name=name;
        this.checked=c;
    }
}
