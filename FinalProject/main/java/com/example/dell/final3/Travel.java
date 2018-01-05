package com.example.dell.final3;

//Written by Xiuqi Ye

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Travel implements Serializable {
    public   String name;
    public   String start;
    public   String end;
    public   List<String> member;
    public  List<Todo> destination;

    public Travel(String name, String start, String end)
    {
        this.name=name;
        this.start=start;
        this.end=end;
        member=new ArrayList<>();
       // Todo d1=new Todo("1","1");
      //  List<Todo> todoList=new ArrayList<Todo>();
       // todoList.add(d1);
      //  this.destination=todoList;

    }
    public Travel(String name, String start, String end,List<Todo> destination)
    {
        this.name=name;
        this.start=start;
        this.end=end;
        this.destination=destination;
        member=new ArrayList<>();

    }
    public Travel(String name, String start, String end,List<Todo> destination, ArrayList<String> member)
    {
        this.name=name;
        this.start=start;
        this.end=end;
        this.destination=destination;
        this.member=member;

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStart(String start) {
        this.start = start;
    }
    public String getStart() {
        return start;
    }
    public void setEnd(String end) {
        this.end = end;
    }
    public String getEnd() {
        return end;
    }

}