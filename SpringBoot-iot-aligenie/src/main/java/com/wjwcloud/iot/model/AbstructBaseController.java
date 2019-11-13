package com.wjwcloud.iot.model;


import java.util.Iterator;
import java.util.Map;

public abstract class AbstructBaseController {
    public AbstructBaseController() {
    }

    protected void initialize() {
    }

    protected boolean validateFilteParams(Map<String, Object> params) throws Exception {
        if (params.isEmpty()) {
            throw new Exception("防止全库扫描，必须传入查询条件");
        } else {
            Iterator var2 = params.keySet().iterator();

            String key;
            do {
                if (!var2.hasNext()) {
                    return true;
                }

                key = (String)var2.next();
            } while(params.get(key) != null);

            throw new Exception("防止全库扫描，传入的查询条件不能为NULL");
        }
    }
}
