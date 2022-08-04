package sango.bucapps.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {

        System.setProperty("spring.main.lazy-initialization", "true");
        SpringApplication.run(ApiApplication.class, args);
    }

}
