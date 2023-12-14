package com.backend.turnosapi.service.impl;


import com.backend.dto.salida.paciente.PacienteSalidaDto;
import com.backend.turnosapi.utils.LocalDateAdapter;
import com.backend.turnosapi.utils.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class PacienteService {
    private final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);


    @Value("${config.api.pacientes}")
    private String urlPacientes;


    public PacienteSalidaDto buscarPacientePorId(Long id) throws UnirestException {


        PacienteSalidaDto paciente = Unirest.get(urlPacientes.concat("/{id}"))
                .routeParam("id", String.valueOf(id))
                .asObject(PacienteSalidaDto.class).getBody();


        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Paciente encontrado: {}", paciente);
        }

        return paciente;
    }

    static {
        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
            private final Gson gson = new GsonBuilder()
                    .disableHtmlEscaping()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();

            @Override
            public <T> T readValue(String s, Class<T> aClass) {
                return gson.fromJson(s, aClass);
            }

            @Override
            public String writeValue(Object o) {
                return gson.toJson(o);
            }
        });
    }
}
