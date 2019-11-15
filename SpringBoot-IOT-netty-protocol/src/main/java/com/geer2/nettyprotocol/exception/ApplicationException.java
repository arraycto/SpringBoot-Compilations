/**   
 * 
 * @Title: ApplicationException.java
 * @Description:  ApplicationException 公共错误类
 * @author  JIaweiWu
 * @email   821060818@qq.com
 * @date
 * @version V1.0
 * 
 */
package com.geer2.nettyprotocol.exception;

/**
 * @ClassName: ApplicationException
 * @Description: ApplicationException 公共错误类
 * 
 * @author JiaweiWu
 * @email 821060818@qq.com
 * @date 2019-11-12
 * 
 */
public class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 3184998542145188928L;

	/**
	 * 错误码
	 */
	private int code;

	/**
	 * 错误消息
	 */
	private String codeMessage;

	public ApplicationException() {
	}

	public ApplicationException(String message) {
		super(message);
		this.codeMessage = message;
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
		this.codeMessage = message;
	}

	public ApplicationException(int code, String codeMessage) {
		super(codeMessage);
		this.code = code;
		this.codeMessage = codeMessage;
	}

	public ApplicationException(int code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public ApplicationException(int code, String codeMessage, Throwable cause) {
		super(codeMessage, cause);
		this.code = code;
		this.codeMessage = codeMessage;
	}
	
	public ApplicationException(ErrorCode ep){
		this(Integer.parseInt(ep.getCode()), ep.getMessage());
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getCodeMessage() {
		return this.codeMessage;
	}

	public void setCodeMessage(String codeMessage) {
		this.codeMessage = codeMessage;
	}
}