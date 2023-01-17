package io.veronymous.sfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StaticFileServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaticFileServerApplication.class, args);
    }
}
