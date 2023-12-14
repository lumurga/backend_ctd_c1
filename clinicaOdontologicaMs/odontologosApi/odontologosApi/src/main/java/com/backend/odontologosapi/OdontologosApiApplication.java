package com.backend.odontologosapi;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class OdontologosApiApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(OdontologosApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OdontologosApiApplication.class, args);
        LOGGER.info("OdontologosApi is now running ...");
    }

    @Bean
    WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> enableDefaultServlet() {
        return factory -> factory.setRegisterDefaultServlet(true);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
