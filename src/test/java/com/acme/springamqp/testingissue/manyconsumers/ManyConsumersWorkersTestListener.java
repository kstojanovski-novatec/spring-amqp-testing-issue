package com.acme.springamqp.testingissue.manyconsumers;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import static org.mockito.Mockito.spy;

@Service
public class ManyConsumersWorkersTestListener {

  @Bean
  public ManyConsumersWorker receiveMessageConsumer1() {
    return spy(new ManyConsumersWorker(1));
  }

  @Bean
  public ManyConsumersWorker receiveMessageConsumer2() {
    return spy(new ManyConsumersWorker(2));
  }

}
