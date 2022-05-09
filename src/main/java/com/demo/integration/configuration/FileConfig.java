package com.demo.integration.configuration;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;

@Configuration
@EnableIntegration
public class FileConfig {

  @Autowired
  private Environment env;




  @Bean
  @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "1000"))
  public FileReadingMessageSource fileReadingMessageSource() {
    FileReadingMessageSource fileReadingMessageSource = new FileReadingMessageSource();
    fileReadingMessageSource.setDirectory(new File(env.getProperty("file.input.directory")));
    return fileReadingMessageSource;
  }

  @Bean
  @ServiceActivator(inputChannel = "fileInputChannel")
  public FileWritingMessageHandler fileWritingMessageHandler() {
    FileWritingMessageHandler fileWritingMessageHandler = new FileWritingMessageHandler(new File(env.getProperty("file.output.directory")));
    fileWritingMessageHandler.setAutoCreateDirectory(true);
    fileWritingMessageHandler.setExpectReply(false);
    //fileWritingMessageHandler.setDeleteSourceFiles(true);
    return fileWritingMessageHandler;
  }

}
