package com.objdb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.objdb.dao.UserDao;
import com.objdb.dao.base.BaseDaoFactory;
import com.objdb.dao.base.IBaseDao;
import com.objdb.entity.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "cj5785";
    IBaseDao<User> baseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseDao = BaseDaoFactory.getInstance().getDataHelper(UserDao.class, User.class);
    }

    public void onSave(View view) {
        User user = new User("jack", "123456");
        baseDao.insert(user);
    }

    public void onUpdate(View view) {
        User where = new User();
        where.setName("jack");
        User user = new User("json", "654321");
        baseDao.update(user, where);
    }

    public void onDelete(View view) {
        User user = new User();
        user.setName("json");
        baseDao.delete(user);
    }

    public void onQuery(View view) {
        User user = new User();
        user.setName("jack");
        List<User> list = baseDao.query(user);
        Log.d(TAG, "查询到数据条目：" + list.size());
        for (User u : list) {
            Log.d(TAG, u.toString());
        }
    }

}
