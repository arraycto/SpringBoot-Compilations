package com.wjwcloud.elk.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wjwcloud.elk.commons.utils.SpringContextUtil;
import com.wjwcloud.elk.model.LoggerEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class SysLogAspect {

    Logger elkLogger = LoggerFactory.getLogger("elk_logger");

    /**
     * 开始时间
     */
    private long startTime = 0L;
    /**
     * 结束时间
     */
    private long endTime = 0L;

//  @Autowired
//  private ILogService logService;

    public static Map<String, Object> getKeyAndValue(Object obj) {
        Map<String, Object> map = new HashMap<>();
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val = new Object();
            try {
                val = f.get(obj);
                // 得到此属性的值
                map.put(f.getName(), val);// 设置键值
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    //通过该注解来判断该接口请求是否进行cut
    @Pointcut("@annotation(com.wjwcloud.elk.aspect.annotation.SysLog)")
    public void cutController() {
    }

    @Before("cutController()")
    public void doBeforeInServiceLayer(JoinPoint joinPoint) {
        log.debug("doBeforeInServiceLayer");
        startTime = System.currentTimeMillis();
    }

    @After("cutController()")
    public void doAfterInServiceLayer(JoinPoint joinPoint) {
        log.debug("doAfterInServiceLayer");
    }

    @Around("cutController()")
    public Object recordSysLog(ProceedingJoinPoint joinPoint) throws Throwable {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();

        ServletRequestAttributes sra = (ServletRequestAttributes) ra;

        HttpServletRequest request = sra.getRequest();

        HttpServletResponse response = sra.getResponse();

        //ELK日志实体类
        LoggerEntity elkLog = new LoggerEntity();

        //应用程序名称
        elkLog.setApplicationName(SpringContextUtil.getApplicationName());

        //profile.active
        elkLog.setProfileActive(SpringContextUtil.getActiveProfile());

        // 请求的类名
        elkLog.setClassName(joinPoint.getTarget().getClass().getName());

        // 请求的方法名
        elkLog.setMethodName(joinPoint.getSignature().getName());

        //请求完整地址
        elkLog.setUrl(request.getRequestURL().toString());

        //请求URI
        elkLog.setUri(request.getRequestURI());

        //请求类型
        elkLog.setRequestMethod(request.getMethod());

        String queryString = request.getQueryString();

        Object[] args = joinPoint.getArgs();

        String params = "";

        //获取请求参数集合并进行遍历拼接
        if (args.length > 0) {

            if ("POST".equals(request.getMethod())) {

                //param
                Object object = args[0];

                Map<String, Object> map = new HashMap<>();

                Map paramMap = getKeyAndValue(object);

                map.put("param", paramMap);

                if (args.length > 1) {

                    object = args[1];

                    Map bodyMap = getKeyAndValue(object);

                    map.put("body", bodyMap);
                }

                params = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss", SerializerFeature.UseSingleQuotes);

            } else if ("GET".equals(request.getMethod())) {

                params = queryString;

            }
        }

        //请求参数内容(param)
        elkLog.setRequestParamData(params);

        //客户端IP
        elkLog.setClientIp(request.getRemoteAddr());

        //终端请求方式,普通请求,ajax请求
        elkLog.setRequestType(request.getHeader("X-Requested-With"));

        //sessionId
        elkLog.setSessionId(request.getRequestedSessionId());

        //请求时间
//        elkLog.setRequestDateTime(new Date(startTime));
        elkLog.setRequestDateTime(new Date());

        Object result = null;

        try {

            // 环绕通知 ProceedingJoinPoint执行proceed方法的作用是让目标方法执行，这也是环绕通知和前置、后置通知方法的一个最大区别。
            result = joinPoint.proceed();

        } catch (Exception e) {

            endTime = System.currentTimeMillis();

            //请求耗时(单位:毫秒)
            elkLog.setSpentTime(endTime - startTime);

            //接口返回时间
            elkLog.setResponseDateTime(new Date(endTime));

            //请求时httpStatusCode代码
            elkLog.setHttpStatusCode(String.valueOf(response.getStatus()));

            String elkLogData = JSON.toJSONStringWithDateFormat(elkLog, "yyyy-MM-dd HH:mm:ss.SSS");

            elkLogger.error(elkLogData);

//            log.error(e.getMessage(), e);

            throw e;

//            return result;

        }

        endTime = System.currentTimeMillis();

        //请求耗时(单位:毫秒)
        elkLog.setSpentTime(endTime - startTime);

        if (!elkLog.getMethodName().equals("getBookOriginalContent")) {

            //接口返回数据
            elkLog.setResponseData(JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss", SerializerFeature.UseSingleQuotes));

        }

        //接口返回时间
        elkLog.setResponseDateTime(new Date(endTime));

        //请求时httpStatusCode代码
        elkLog.setHttpStatusCode(String.valueOf(response.getStatus()));

//        if (SpringContextUtil.getActiveProfile().equals("prod")) {
        String s = JSON.toJSONStringWithDateFormat(elkLog, "yyyy-MM-dd HH:mm:ss.SSS");
        elkLogger.info(s);

        return result;
    }

}