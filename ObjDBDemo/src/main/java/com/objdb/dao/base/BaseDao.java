package com.objdb.dao.base;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.objdb.anno.DbField;
import com.objdb.anno.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 实现接口声明的方法，通过反射和注解得到对象的值，然后进行相应操作
 * Created by Administrator on 2019/4/22.
 */

public abstract class BaseDao<T> implements IBaseDao<T>{
    private static final String TAG = "BaseDao";
    private SQLiteDatabase database;//持有数据库操作类的引用
    private boolean isInit = false;//保证只实例化一次
    private Class<T> entityClass;//持有数据库表所对应的java类型
    private String tableName;
    private HashMap<String, Field> cacheMap;//维护表名与成员变量的映射关系

    //实例化
    protected boolean init(Class<T> entity, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            database = sqLiteDatabase;
            entityClass = entity;
            cacheMap = new HashMap<>();
            //拿到注解中的表名
            if (entity.getAnnotation(DbTable.class) != null) {
                tableName = entity.getAnnotation(DbTable.class).value();
            } else {
                tableName = entity.getClass().getSimpleName();
            }
            if (!database.isOpen()) {
                return false;
            }
            if (!TextUtils.isEmpty(createTable())) {
                database.execSQL(createTable());
            }
            initCacheMap();
            isInit = true;
        }
        return isInit;
    }

    //创建表，在子类中完成
    protected abstract String createTable();

    //维护映射关系
    private void initCacheMap() {
        String sql = "select * from " + this.tableName + " limit 1 , 0";
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
            String[] columnNames = cursor.getColumnNames();//表的列名数组
            Field[] columnFields = entityClass.getFields();//拿到Filed数组
            for (Field field : columnFields) {
                field.setAccessible(true);
            }
            //查找对应关系
            for (String columnName : columnNames) {
                Field columnField = null;
                for (Field field : columnFields) {
                    String fieldName = null;
                    if (field.getAnnotation(DbField.class) != null) {
                        fieldName = field.getAnnotation(DbField.class).value();
                    } else {
                        fieldName = field.getName();
                    }
                    if (columnName.equals(fieldName)) {
                        columnField = field;
                        break;
                    }
                }
                //找到对应关系，存入关系表
                if (columnField != null) {
                    cacheMap.put(columnName, columnField);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

    }

    @Override
    public long insert(T entity) {
        Map<String, String> map = getValues(entity);
        ContentValues values = getContentValues(map);
        long result = database.insert(tableName, null, values);
        Log.d(TAG, "insert: result = " + result);
        return result;
    }

    @Override
    public int update(T entity, T where) {
        int result = -1;
        Map values = getValues(entity);
        ContentValues contentValues = getContentValues(values);
        Map whereClause = getValues(where);
        Condition condition = new Condition(whereClause);
        result = database.update(tableName, contentValues, condition.getWhereClause(), condition.getWhereArgs());
        return result;

    }

    @Override
    public int delete(T where) {
        int result = -1;
        Map values = getValues(where);
        Condition condition = new Condition(values);
        result = database.delete(tableName, condition.getWhereClause(), condition.getWhereArgs());
        return result;
    }

    @Override
    public List<T> query(T where) {
        return query(where, null, null, null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        Map values = getValues(where);
        String limitString = null;
        if (startIndex != null && limit != null) {
            limitString = startIndex + " , " + limit;
        }
        Condition condition = new Condition(values);
        Cursor cursor = database.query(tableName, null, condition.getWhereClause(),
                condition.getWhereArgs(), null, null, orderBy, limitString);
        List<T> result = getResult(cursor, where);
        return result;
    }

    //通过Map构建ContentValues
    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }

    //获取value值，存入Map
    private Map<String, String> getValues(T entity) {
        HashMap<String, String> result = new HashMap<>();
        Iterator<Field> filedsIterator = cacheMap.values().iterator();
        //循环遍历映射map的Filed
        while (filedsIterator.hasNext()) {
            Field colmunToField = filedsIterator.next();
            String cacheKey = null;
            String cacheValue = null;
            if (colmunToField.getAnnotation(DbField.class) != null) {
                cacheKey = colmunToField.getAnnotation(DbField.class).value();
            } else {
                cacheKey = colmunToField.getName();
            }
            try {
                if (colmunToField.get(entity) == null) {
                    continue;
                }
                cacheValue = colmunToField.get(entity).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            result.put(cacheKey, cacheValue);
        }
        return result;
    }

    //构建查询条件
    class Condition {
        //查询条件
        private String whereClause;
        private String[] whereArgs;

        public Condition(Map<String, String> whereClause) {
            ArrayList arrayList = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" 1=1 ");
            Set keys = whereClause.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = whereClause.get(key);
                if (value != null) {
                    //拼接条件查询语句 1=1 and name=? and password=?
                    stringBuilder.append(" and " + key + " =?");
                    arrayList.add(value);
                }
            }
            this.whereClause = stringBuilder.toString();
            this.whereArgs = (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        public String getWhereClause() {
            return whereClause;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }
    }

    //获取查询结果
    private List<T> getResult(Cursor cursor, T where) {
        List list = new ArrayList();
        Object item;
        while (cursor.moveToNext()) {
            try {
                item = where.getClass().newInstance();
                Iterator iterator = cacheMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    String columnName = (String) entry.getKey(); //得到列名
                    Integer columnIndex = cursor.getColumnIndex(columnName); //拿到位置
                    Field field = (Field) entry.getValue();
                    Class type = field.getType();
                    if (columnIndex != -1) {
                        if (type == String.class) {
                            //反射方式赋值
                            field.set(item, cursor.getString(columnIndex));
                        } else if (type == Integer.class) {
                            field.set(item, cursor.getInt(columnIndex));
                        } else if (type == Double.class) {
                            field.set(item, cursor.getDouble(columnIndex));
                        } else if (type == Long.class) {
                            field.set(item, cursor.getLong(columnIndex));
                        } else if (type == byte[].class) {
                            field.set(item, cursor.getBlob(columnIndex));
                        } else {
                            continue;
                        }
                    }

                }
                list.add(item);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }




}
