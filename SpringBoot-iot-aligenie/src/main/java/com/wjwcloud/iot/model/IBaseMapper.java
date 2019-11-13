package com.wjwcloud.iot.model;


import java.util.List;
import java.util.Map;

public interface IBaseMapper<E> {
    int deleteByPrimaryKey(Long var1);

    int insert(E var1);

    long selectLastId();

    int insertSelective(E var1);

    int updateByPrimaryKeySelective(E var1);

    E selectByPrimaryKey(Long var1);

    int findCount(E var1);

    int findCount(Map<String, Object> var1);

    int updateByPrimaryKey(E var1);

    List<E> findList(E var1);

    List<E> findList(Map<String, Object> var1);
}
