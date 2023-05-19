package com.acme.springamqp.testingissue.manyconsumers;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ManyConsumersWorkersListener {

  @Bean
  public ManyConsumersWorker receiveMessageConsumer1() {
    return new ManyConsumersWorker(1);
  }

  @Bean
  public ManyConsumersWorker receiveMessageConsumer2() {
    return new ManyConsumersWorker(2);
  }

}
