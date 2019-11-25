/**
 * Copyright (c) AVIT LTD (2016). All Rights Reserved.
 * Welcome to www.avit.com.cn
 */
package com.geer2.nettyprotocol.server.bean.forbusiness;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @Description:业务系统下发节点的xml格式
 * @author JiaweiWu
 * @Date:2019-11-25
 * @Version: 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "MsgToNode")
public class MsgToNode implements Serializable {

	private static final long serialVersionUID = -8158135543792324961L;

	@XmlElement(name = "MsgInfo")
	private MsgInfo msgInfo;

	@XmlElement(name = "MsgPublish")
	private MsgPublish msgPublish;

	@XmlElementWrapper(name="UserInfos") 
	@XmlElement(name = "UserInfo")
	private List<UserInfo> stbUserList;
	
	@XmlTransient
	private Date editTime;
	
	@XmlTransient
    private Integer userNumbers;

	public Integer getUserNumbers()
    {
        return userNumbers;
    }

    public void setUserNumbers(Integer userNumbers)
    {
        this.userNumbers = userNumbers;
    }

    public Date getEditTime()
    {
        return editTime;
    }

    public void setEditTime(Date editTime)
    {
        this.editTime = editTime;
    }

    public MsgInfo getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(MsgInfo msgInfo) {
		this.msgInfo = msgInfo;
	}

	public MsgPublish getMsgPublish() {
		return msgPublish;
	}

	public void setMsgPublish(MsgPublish msgPublish) {
		this.msgPublish = msgPublish;
	}

/*	public List<UserInfo> getStbUserList() {
		return stbUserList;
	}

	public void setStbUserList(List<UserInfo> stbUserList) {
		this.stbUserList = stbUserList;
	}*/

	

}
