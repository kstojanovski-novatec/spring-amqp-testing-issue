package com.acme.springamqp.testingissue.manyconsumers.config;

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

@PropertySource("classpath:many-consumers.properties")
@Configuration
public class ManyConsumersConfig {

  @Value("${many.consumers.exchange.name}")
  private String MANY_CONSUMERS_EXCHANGE_NAME;

  @Value("${many.consumers.queue.name.workers}")
  private String MANY_CONSUMERS_QUEUE_NAME_WORKERS;

  @Bean
  public Declarables manyConsumersBindings() {
    FanoutExchange simpleMessageExchange = ExchangeBuilder.fanoutExchange(MANY_CONSUMERS_EXCHANGE_NAME)
        .durable(false).build();
    Queue simpleMessageQueueWorkers = QueueBuilder.nonDurable(MANY_CONSUMERS_QUEUE_NAME_WORKERS).build();
    return new Declarables(
        simpleMessageExchange,
        simpleMessageQueueWorkers,
        BindingBuilder.bind(simpleMessageQueueWorkers).to(simpleMessageExchange)
    );
  }

}
