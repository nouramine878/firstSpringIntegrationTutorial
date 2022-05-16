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
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageHandler;

@Configuration
@EnableIntegration
public class FileConfig {

  @Autowired
  private Environment env;




//  @Bean
//  @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "1000"))
//  public FileReadingMessageSource fileReadingMessageSource() {
//    FileReadingMessageSource fileReadingMessageSource = new FileReadingMessageSource();
//    fileReadingMessageSource.setDirectory(new File(env.getProperty("file.input.directory")));
//    return fileReadingMessageSource;
//  }
//
//  @Bean
//  @ServiceActivator(inputChannel = "fileInputChannel")
//  public FileWritingMessageHandler fileWritingMessageHandler() {
//    FileWritingMessageHandler fileWritingMessageHandler = new FileWritingMessageHandler(new File(env.getProperty("file.output.directory")));
//    fileWritingMessageHandler.setAutoCreateDirectory(true);
//    fileWritingMessageHandler.setExpectReply(false);
//    //fileWritingMessageHandler.setDeleteSourceFiles(true);
//    return fileWritingMessageHandler;
//  }

  @Bean
  public GenericSelector<File> onlyJpgs() {
    return new GenericSelector<File>() {

      @Override
      public boolean accept(File source) {
        return source.getName().endsWith(".jpg");
      }
    };
  }

  @Bean
  public MessageSource<File> sourceDirectory() {
    FileReadingMessageSource messageSource = new FileReadingMessageSource();
    messageSource.setDirectory(new File(env.getProperty("file.input.directory")));
    return messageSource;
  }

  @Bean
  public MessageHandler targetDirectory() {
    FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(env.getProperty("file.output.directory")));
    handler.setFileExistsMode(FileExistsMode.REPLACE);
    handler.setExpectReply(false);
    return handler;
  }

  @Bean
  public IntegrationFlow fileMover() {
    return IntegrationFlows.from(sourceDirectory(), c -> c.poller(Pollers.fixedDelay(10000)))
        .filter(onlyJpgs())
        .handle(targetDirectory())
        .get();
  }

}
