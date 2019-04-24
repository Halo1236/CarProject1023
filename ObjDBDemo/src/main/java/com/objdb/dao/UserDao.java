package com.objdb.dao;

import com.objdb.dao.base.BaseDao;

/**
 * 测试类的数据操作，继承BaseDao，完成数据库的创建
 * Created by Administrator on 2019/4/22.
 */

public class UserDao extends BaseDao {

    @Override
    protected String createTable() {
        return "create table if not exists tb_user(name varchar(20),password varchar(10))";
    }
}
