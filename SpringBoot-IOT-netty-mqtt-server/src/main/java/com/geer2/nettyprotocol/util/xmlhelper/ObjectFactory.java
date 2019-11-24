package com.geer2.nettyprotocol.util.xmlhelper;

import com.geer2.nettyprotocol.server.bean.forbusiness.MsgToNode;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * 
 * @author panxincheng
 * @date 2011-7-15
 */
@XmlRegistry
public class ObjectFactory {
    public MsgToNode createMsgToNode()
    {
        return new MsgToNode();
    }
    
}
