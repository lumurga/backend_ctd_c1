package com.backend.pacientesapi;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class PacientesApiApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(PacientesApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PacientesApiApplication.class, args);

        LOGGER.info("PacientesApi is now running...");
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
