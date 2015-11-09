package com.prism.action;

import javax.jms.Destination;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

public class MessageSender  {
    public static void main(String args[]) throws Exception {
        String[] configLocations = new String[] {"config/baseConfig.xml"};
        ApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
        JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("jmsTemplate");
        Destination destination = (Destination) context.getBean("destination");
        
            //消息产生者
            MyMessageCreator myMessageCreator = new MyMessageCreator();
            myMessageCreator.setMsg("hello");
            
            jmsTemplate.send(destination, myMessageCreator);
           
        
    }
}
