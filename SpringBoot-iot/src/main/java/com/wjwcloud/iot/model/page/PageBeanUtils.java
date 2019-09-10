package com.wjwcloud.iot.model.page;


import java.util.Map;

public class PageBeanUtils {
    public PageBeanUtils() {
    }

    public static PageBean toPageBean(Map<String, Object> params) {
        PageBean pb = new PageBean();
        copyProperties(params, pb);
        return pb;
    }

    public static PageBean copyProperties(Map<String, Object> params, PageBean pb) {
        if (params.containsKey("pageSize")) {
            pb.setPageSize(Integer.valueOf(params.get("pageSize").toString()));
        }

        if (params.containsKey("pageNo")) {
            pb.setPageNo(Integer.valueOf(params.get("pageNo").toString()));
        }

        return pb;
    }
}
