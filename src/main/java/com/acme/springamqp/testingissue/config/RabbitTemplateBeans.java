package com.acme.springamqp.testingissue.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTemplateBeans {

  private final MessageConverterBeans messageConverterBeans;

  public RabbitTemplateBeans(
      @Autowired MessageConverterBeans messageConverterBeans
  ) {
    this.messageConverterBeans = messageConverterBeans;
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    return new RabbitTemplate(connectionFactory);
  }

  @Bean
  public RabbitTemplate jsonRabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverterBeans.jackson2Converter());
    return template;
  }

}
