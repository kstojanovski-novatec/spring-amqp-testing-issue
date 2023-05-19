package com.acme.springamqp.testingissue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringAmqpTestingIssueApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringAmqpTestingIssueApplication.class, args);
  }

}
