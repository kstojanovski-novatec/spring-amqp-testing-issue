package com.acme.springamqp.testingissue.differenttypes.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:different-types.properties")
@Configuration
public class DifferentTypesConfig {

  @Value("${different.types.exchange.name}")
  private String DIFFERENT_TYPES_EXCHANGE_NAME;

  @Value("${different.types.queue.name.message-types}")
  private String DIFFERENT_TYPES_QUEUE_NAME;

  @Bean
  public Declarables differentTypesBindings() {
    FanoutExchange simpleMessageExchange = ExchangeBuilder.fanoutExchange(DIFFERENT_TYPES_EXCHANGE_NAME)
        .durable(false).build();
    Queue simpleMessageQueueWorkers = QueueBuilder.nonDurable(DIFFERENT_TYPES_QUEUE_NAME).build();
    return new Declarables(
        simpleMessageExchange,
        simpleMessageQueueWorkers,
        BindingBuilder.bind(simpleMessageQueueWorkers).to(simpleMessageExchange)
    );
  }

}
