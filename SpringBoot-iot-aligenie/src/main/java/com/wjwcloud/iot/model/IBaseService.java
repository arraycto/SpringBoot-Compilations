package com.wjwcloud.iot.model;


import com.wjwcloud.iot.model.page.PageBean;

import java.util.List;
import java.util.Map;

public interface IBaseService<V, E> {
    long save(E var1) throws Exception;

    long saveSelective(E var1) throws Exception;

    long updateByPrimaryKey(E var1);

    long updateByPrimaryKeySelective(E var1);

    long deleteByPrimaryKey(Long var1) throws Exception;

    V selectByPrimaryKey(Long var1) throws Exception;

    PageBean findList4Map(Map<String, Object> var1, PageBean var2) throws Exception;

    List<V> findList4Map(Map<String, Object> var1) throws Exception;

    List<V> findFieldList(List<String> var1, Map<String, Object> var2) throws Exception;

    V assembleVo(E var1) throws Exception;
}
