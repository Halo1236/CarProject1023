package com.objdb.entity;

import com.objdb.anno.DbField;
import com.objdb.anno.DbTable;

/**
 * Created by Administrator on 2019/4/22.
 */

@DbTable("tb_user")
public class User {
    @DbField("name")
    public String name;

    @DbField("password")
    public String password;

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "name: " + name + ",password: " + password;
    }

}
