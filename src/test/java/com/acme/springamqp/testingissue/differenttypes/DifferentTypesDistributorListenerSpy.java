package com.acme.springamqp.testingissue.differenttypes;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.spy;

@TestConfiguration
public class DifferentTypesDistributorListenerSpy {

  @Bean
  public DifferentTypesDistributorListener simpleMessageDistributorListener() {
    return spy(new DifferentTypesDistributorListener());
  }
}
