package com.demo.integration.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.demo.integration.model.Student;

@MessagingGateway
public interface IntegrationGateway {

  @Gateway(requestChannel = "integration.gateway.channel")
  public String sendMessage(String message);


  @Gateway(requestChannel = "integration.student.gateway.channel")
  public String processStudentDetails(Student student);

  @Gateway(requestChannel = "router.channel")
  public <T> void process(T object);

}
