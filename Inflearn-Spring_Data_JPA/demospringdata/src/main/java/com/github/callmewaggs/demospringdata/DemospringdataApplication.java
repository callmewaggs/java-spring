package com.github.callmewaggs.demospringdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;

@SpringBootApplication
public class DemospringdataApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemospringdataApplication.class, args);
  }

}
