package com.prism.action;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

public class MyMessageCreator implements MessageCreator {
	private String msg = "";
	
	@Override
	public Message createMessage(Session paramSession) throws JMSException {
		return paramSession.createTextMessage(msg);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
