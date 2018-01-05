package com.example.dell.final3;
//Written by Jingxuan Chen

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    public   String name;
    public   String password;
    public   String email;
    public   List<String> relationships;
    public boolean pic;
    public User(String name, String password, String email)
    {
        this.name=name;
        this.password=password;
        this.email=email;
        relationships=new ArrayList<>();
        this.pic=false;
    }
    public User(String name, String password, String email, ArrayList<String> relationships)
    {
        this.name=name;
        this.password=password;
        this.email=email;
        this.relationships=relationships;
        this.pic=false;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
    public boolean getPic() {
        return pic;
    }
    public void setPic() {
        this.pic=true;
    }
}
