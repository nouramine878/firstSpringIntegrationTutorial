package com.demo.integration.service;


import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class IntegrationService {


  @ServiceActivator(inputChannel = "integration.gateway.channel", outputChannel = "integration.gateway.channel.serviceactivator")
  public Message<String> receiveMessage(Message<String> message) throws MessagingException {
    MessageBuilder.fromMessage(message);
    Message<String> newMessage = MessageBuilder
        .withPayload(message.getPayload() + " modified in integration.gateway.channel").build();
    return  newMessage;
  }


  @ServiceActivator(inputChannel = "integration.gateway.channel.serviceactivator")
  public void anotherMessage(Message<String> message) throws MessagingException {
    MessageChannel replyChannel = (MessageChannel) message.getHeaders().getReplyChannel();
    MessageBuilder.fromMessage(message);
    Message<String> newMessage = MessageBuilder
        .withPayload(message.getPayload() + " AND RECEIVED INTO integration.gateway.channel.serviceactivator").build();
    replyChannel.send(newMessage);
  }

}
