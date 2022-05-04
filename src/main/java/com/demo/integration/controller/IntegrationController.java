package com.demo.integration.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.integration.gateway.IntegrationGateway;
import com.demo.integration.model.Address;
import com.demo.integration.model.Student;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/integrate")
public class IntegrationController {

  @Autowired
  private IntegrationGateway integrationGateway;

  @GetMapping(path="/{name}")
  public String getMessageFromIntegrationService(@PathVariable("name") String name) {
    log.info("some information here");
    return this.integrationGateway.sendMessage(name);
  }


  @PostMapping
  public String processStudentDetails(@RequestBody Student student) {
    return this.integrationGateway.processStudentDetails(student);
  }


  @PostMapping(value = "/student")
  public void processStudentDetailsWithRouter(@RequestBody Student student) {
    this.integrationGateway.process(student);
  }

  @PostMapping(value = "/address")
  public void processAddressDetailsWithRouter(@RequestBody Address address) {
    this.integrationGateway.process(address);
  }


}
