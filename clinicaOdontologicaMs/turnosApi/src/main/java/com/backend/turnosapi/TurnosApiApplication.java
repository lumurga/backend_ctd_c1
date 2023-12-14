package com.backend.turnosapi;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class TurnosApiApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(TurnosApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TurnosApiApplication.class, args);

        LOGGER.info("TurnosApi is now running...");
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
