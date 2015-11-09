package com.prism.action;

import javax.jms.Destination;
import javax.jms.TextMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class MessageReciver {
	public static void main(String args[]) throws Exception {
		String[] configLocations = new String[] {"config/baseConfig.xml"};
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
 
        JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("jmsTemplate");
        Destination destination = (Destination) context.getBean("destination");
 
        TextMessage msg = null;
        //是否继续接收消息
        boolean isContinue = true;
        while (isContinue) {
            msg = (TextMessage) jmsTemplate.receive(destination);
            System.out.println("收到消息 :" + msg.getText());
            if (msg.getText().equals("end")) {
                isContinue = false;
                System.out.println("收到退出消息，程序要退出！");
            }
        }
        System.out.println("程序退出了！");
    }
}
