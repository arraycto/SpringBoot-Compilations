package com.wjwcloud.iot.model;


import com.wjwcloud.iot.model.page.PageBean;

import java.util.List;
import java.util.Map;

public abstract class BaseService<V, E> implements IBaseService<V, E> {
    public BaseService() {
    }

    @Override
    public long save(E entity) throws Exception {
        return 0L;
    }

    @Override
    public long saveSelective(E entity) throws Exception {
        return 0L;
    }

    @Override
    public long updateByPrimaryKey(E entity) {
        return 0L;
    }

    @Override
    public long updateByPrimaryKeySelective(E entity) {
        return 0L;
    }

    @Override
    public long deleteByPrimaryKey(Long id) throws Exception {
        return 0L;
    }

    @Override
    public V selectByPrimaryKey(Long id) throws Exception {
        return null;
    }

    @Override
    public PageBean findList4Map(Map<String, Object> params, PageBean pageBean) throws Exception {
        return null;
    }

    @Override
    public List<V> findList4Map(Map<String, Object> params) throws Exception {
        return null;
    }

    @Override
    public List<V> findFieldList(List<String> fieldList, Map<String, Object> params) throws Exception {
        return null;
    }

    @Override
    public V assembleVo(E entity) throws Exception {
        return null;
    }

    protected void setRowNum(Map<String, Object> params, PageBean pageBean) {
//        int no = false;
        int no = pageBean.getRows() % pageBean.getPageSize() == 0 ? pageBean.getRows() / pageBean.getPageSize() : pageBean.getRows() / pageBean.getPageSize() + 1;
        no = no == 0 ? 1 : no;
        params.put("startRowNum", pageBean.getStartRowNum());
        params.put("endRowNum", pageBean.getEndRowNum());
    }
}
