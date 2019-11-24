package com.geer2.nettyprotocol.task;

import cn.hutool.setting.dialect.Props;
import com.geer2.nettyprotocol.server.mqtt.handler.HttpServerHandler;
import com.geer2.nettyprotocol.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 
 * 定期清理消息缓存、离线用户缓存
 * @author JiaweiWu
 */
public class CleaOutOfTimeMsgTask implements Runnable
{
    public static Logger log = LogManager.getLogger(CleaOutOfTimeMsgTask.class);


    @Override
    public void run()
    {
        List<String> deleteKey = new ArrayList<String>();
        log.debug("清理过期消息 start");
        String deletemsg="";
        for (String key : HttpServerHandler.messageMap.keySet())
        {
            Props props = new Props("config.properties");
            String msgbeforeDate = props.get("msgBeforeDate").toString();
            int cleanDate = -1;
            Date lastUpateDate = HttpServerHandler.messageMap.get(key).getEditTime();
            if (!StringUtils.isEmpty(msgbeforeDate))
            {
                cleanDate = Integer.parseInt(msgbeforeDate);
            }
            /**
             * 指定时间以前的消息，清除消息缓存、离线用户缓存
             */
            if (DateUtil.addDay(new Date(), cleanDate).getTime() > lastUpateDate.getTime())
            {
                deleteKey.add(key);
            }
        }
        /**
         * 删除过期资产
         */
        if (deleteKey.size() > 0)
        {
            for (int i = 0; i < deleteKey.size(); i++)
            {
                deletemsg += deleteKey.get(i) + ",";
                HttpServerHandler.messageMap.remove(deleteKey.get(i));
                HttpServerHandler.OffLineUserMsgMap.remove(deleteKey.get(i));
            }
        }
        
        log.debug("清理过期消息 end,清理掉的消息如下： " + deletemsg);
        
    }
    
}
