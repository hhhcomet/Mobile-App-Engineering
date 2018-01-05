package com.example.a18200.project2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 18200 on 2017/10/10.
 */

public class item implements Serializable {
    public String name;
    public String tel;
    public List<String> relation;
    public String path;

    public item(String name, String tel, List<String> relation, String path) {
        this.name = name;
        this.tel = tel;
        this.relation = relation;
        this.path=path;
    }

    public item(String name, String tel, List<String> relation) {
        this.name = name;
        this.tel = tel;
        this.relation = relation;
    }

    public item(String name, String tel) {
        this.name = name;
        this.tel = tel;
        this.relation=new ArrayList<>();

        //this.relation = relation;
    }

    public item(){
        relation=new ArrayList<>();
    }
}
