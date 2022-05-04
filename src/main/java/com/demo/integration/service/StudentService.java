package com.demo.integration.service;


import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.demo.integration.model.Student;

@Component
public class StudentService {

  @ServiceActivator(inputChannel = "integration.student.objectToJson.channel", outputChannel = "integration.student.jsonToObject.channel")
  public Message<?> receiveMessage(Message<?> message) throws MessagingException {
    System.out.println("#####################");
    System.out.println(message);
    System.out.println("#####################");
    System.out.println("Object to Json - " + message.getPayload());
    return message;
  }

  @ServiceActivator(inputChannel = "integration.student.jsonToObject.fromTransformer.channel")
  public void processJsonToObject(Message<?> message) throws MessagingException {
    MessageChannel replyChannel = (MessageChannel) message.getHeaders().getReplyChannel();
    MessageBuilder.fromMessage(message);
    System.out.println("#####################");

    System.out.println("Json To Object - " + message.getPayload());
    Student student = (Student) message.getPayload();
    Message<?> newMessage = MessageBuilder.withPayload(student.toString()).build();
    replyChannel.send(newMessage);
  }

  @ServiceActivator(inputChannel = "student.channel")
  public void receiveMessage1(Message<?> message) throws MessagingException {
    System.out.println("##########student.channel###########");
    System.out.println(message);
    System.out.println("#####################");
    System.out.println(message.getPayload());
  }

}
