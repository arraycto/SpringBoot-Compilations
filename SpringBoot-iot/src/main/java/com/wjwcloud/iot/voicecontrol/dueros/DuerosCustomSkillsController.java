package com.wjwcloud.iot.voicecontrol.dueros;

import com.alibaba.da.coin.ide.spi.standard.ResultModel;
import com.alibaba.da.coin.ide.spi.standard.TaskResult;
import com.geer2.base.utils.SpringContextUtils;
import com.geer2.zwow.iot.voicecontrol.aligenie.entity.AligenieUtil;
import com.geer2.zwow.iot.voicecontrol.aligenie.enums.CustomSkillsType;
import com.geer2.zwow.iot.voicecontrol.service.ICustomSkillsService;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zhoulei on 2019/5/7.
 * OAuth2 百度音箱自定义技能调用
 */
//@RestController
//@RequestMapping("/dueros/customSkills")
public class DuerosCustomSkillsController {
    private static Logger logger = LoggerFactory.getLogger(DuerosCustomSkillsController.class);

    /**
     * 百度音箱自定义技能调用
     * @param request
     * @param response
     * @param br
     * @return
     * @throws IOException
     * @throws OAuthSystemException
     */
    @RequestMapping(value = "/execute")
    @ResponseBody
    public ResultModel<TaskResult> execute(@RequestBody String securityWrapperTaskQuery , HttpServletRequest request) {
        logger.info("securityWrapperTaskQuery" + securityWrapperTaskQuery);
        ApplicationContext applicationContext = SpringContextUtils.getApplicationContext();
        AligenieUtil.getAllRequestParam(request);
        Map headMap = AligenieUtil.getAllHeadParam(request);
        String beanName = null;
        if("alarmRecord".equals(headMap.get("typeparams"))){
            beanName = CustomSkillsType.ALARM_RECORD.getCode();
        }else if("openLock".equals(headMap.get("typeparams"))){
            beanName = CustomSkillsType.OPEN_LOCK.getCode();
        }else if("voiceMessage".equals(headMap.get("typeparams"))){
            beanName = CustomSkillsType.VOICE_MESSAGE.getCode();
        }else {
            return null;
        }
        ICustomSkillsService iAligenieCustomSkillsService = (ICustomSkillsService)applicationContext.getBean(beanName);
        ResultModel<TaskResult> resultModel = (ResultModel<TaskResult>)iAligenieCustomSkillsService.execute(securityWrapperTaskQuery , request);
        return resultModel;
    }

}
