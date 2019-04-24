package com.objdb.dao.base;

import java.util.List;

/**
 * Created by Administrator on 2019/4/22.
 */

public interface IBaseDao<T> {
    /**
     * 插入数据
     * @param entity
     * @return
     */
    long insert(T entity);

    /**
     * 更新数据
     * @param entity
     * @param where
     * @return
     */
    int update(T entity,T where);

    /**
     * 删除数据
     * @param where
     * @return
     */
    int delete(T where);

    /**
     * 查询数据
     * @param where
     * @return
     */
    List<T> query(T where);

    /**
     * 查询数据
     * @param where
     * @param orderBy
     * @param startIndex
     * @param limit
     * @return
     */
    List<T> query(T where,String orderBy,Integer startIndex,Integer limit);
}
