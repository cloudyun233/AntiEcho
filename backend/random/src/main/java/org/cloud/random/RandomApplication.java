package org.cloud.random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RandomApplication {

    public static void main(String[] args) {
        SpringApplication.run(RandomApplication.class, args);
    }

}
