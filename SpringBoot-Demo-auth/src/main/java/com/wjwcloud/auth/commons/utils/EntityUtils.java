package com.wjwcloud.auth.commons.utils;

import com.github.wjw0315.wtool.core.util.ReflectionUtils;
import com.wjwcloud.auth.commons.handle.BaseContextHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;


/**
 * 实体类相关工具类
 * 解决问题： 1、快速对实体的常驻字段，如：crtUser、crtHost、updUser等值快速注入
 */
public class EntityUtils {
    /**
     * 快速将bean的"createPerson", "companyId", "userHost", "createTime", "updatePerson", "updHost", "updateTime"附上相关值
     *
     * @param entity 实体bean
     */
    public static <T> void setCreatAndUpdatInfo(T entity) {
        setCreateInfo(entity);
        setUpdatedInfo(entity);
    }

    /**
     * 快速将bean的"createPerson", "companyId", "userHost", "createTime"附上相关值
     *
     * @param entity 实体bean
     */
    public static <T> void setCreateInfo(T entity) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String hostIp = "";
        if (request != null) {
            hostIp = StringUtils.defaultIfBlank(request.getHeader("userHost"), ClientUtil.getClientIp(request));
        }

        String id = BaseContextHandler.getUserID();
        Long comId = null;
        if ("null".equals(BaseContextHandler.getComId())) {
            comId = 0L;
        }else {
            comId = StringUtils.isEmpty(BaseContextHandler.getComId()) ? 0 : Long.parseLong(BaseContextHandler.getComId());
        }
        Long createPerson = StringUtils.isEmpty(id) ? 0 : Long.parseLong(id);
        // 默认属性
        String[] fields = {"createPerson", "comId", "userHost", "createTime"};

        Field field = ReflectionUtils.getAccessibleField(entity, "createTime");
        // 默认值
        Object[] value = null;
        if (field != null && field.getType().equals(Date.class)) {
            value = new Object[]{createPerson, comId, hostIp, new Date()};
        }
        // 填充默认属性值
        setDefaultValues(entity, fields, value);
    }

    /**
     * 快速将bean的"updatePerson", "updHost", "updateTime"附上相关值
     *
     * @param entity 实体bean
     */
    public static <T> void setUpdatedInfo(T entity) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String hostIp = "";
        if (request != null) {
            hostIp = StringUtils.defaultIfBlank(request.getHeader("userHost"), ClientUtil.getClientIp(request));
        }
        String id = BaseContextHandler.getUserID();
        Long updatePerson = StringUtils.isEmpty(id) ? 0 : Long.parseLong(id);
        // 默认属性
        String[] fields = {"updatePerson", "updHost", "updateTime"};
        Field field = ReflectionUtils.getAccessibleField(entity, "updateTime");
        Object[] value = null;
        if (field != null && field.getType().equals(Date.class)) {
            value = new Object[]{updatePerson, hostIp, new Date()};
        }
        // 填充默认属性值
        setDefaultValues(entity, fields, value);
    }

    /**
     * 依据对象的属性数组和值数组对对象的属性进行赋值
     *
     * @param entity 对象
     * @param fields 属性数组
     * @param value  值数组
     * @author 王浩彬
     */
    private static <T> void setDefaultValues(T entity, String[] fields, Object[] value) {
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if (ReflectionUtils.hasField(entity, field)) {
                ReflectionUtils.invokeSetter(entity, field, value[i]);
            }
        }
    }

    /**
     * 根据主键属性，判断主键是否值为空
     *
     * @param entity
     * @param field
     * @return 主键为空，则返回false；主键有值，返回true
     * @date 2016年4月28日
     */
    public static <T> boolean isPKNotNull(T entity, String field) {
        if (!ReflectionUtils.hasField(entity, field)) {
            return false;
        }
        Object value = ReflectionUtils.getFieldValue(entity, field);
        return value != null && !"".equals(value);
    }
}
