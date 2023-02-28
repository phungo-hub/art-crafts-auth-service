package com.artcrafts.artcraftsauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication

public class ArtCraftsAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtCraftsAuthServiceApplication.class, args);
    }


}
