package com.backend.turnosapi.service.impl;


import com.backend.dto.salida.odontologo.OdontologoSalidaDto;
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
public class OdontologoService {
    private final Logger LOGGER = LoggerFactory.getLogger(OdontologoService.class);


    @Value("${config.api.odontologos}")
    private String urlOdontologos;


    public OdontologoSalidaDto buscarOdontologoPorId(Long id) throws UnirestException {


        OdontologoSalidaDto odontologo = Unirest.get(urlOdontologos.concat("/{id}"))
                .routeParam("id", String.valueOf(id))
                .asObject(OdontologoSalidaDto.class).getBody();


        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Odontologo encontrado: {}", odontologo);
        }

        return odontologo;
    }

    /*

    public List<OdontologoSalidaDto> listarOdontologos() throws UnirestException {
        JSONArray jsonArray = Unirest.get(urlOdontologos).asJson().getBody().getArray();
        List<OdontologoSalidaDto> odontologos = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            OdontologoSalidaDto odontologo = new OdontologoSalidaDto(jsonObject.getLong("id"), jsonObject.getString("matricula"), jsonObject.getString("nombre"), jsonObject.getString("apellido"));
            odontologos.add(odontologo);
        }

        return odontologos;
    }
    public List<Odontologo> listarOdontologos2() throws UnirestException, JsonProcessingException {
        String json =
                Unirest.get(urlOdontologos)
                        .asString()
                        .getBody();


        return Arrays.stream(objectMapper.readValue(json, Odontologo[].class))
                .collect(Collectors.toList());
    }


    public Odontologo registrarOdontologo(Odontologo odontologo) throws UnirestException {
        Odontologo odon = Unirest.post(urlOdontologos.concat("/registrar"))
                .header("Content-Type", "application/json")
                .queryString("accept", "application/json")
                .body(odontologo)
                .asObject(Odontologo.class)
                .getBody();


        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Odontologo encontrado: {}", odon);
        }

        return odon;
    }




    public void eliminarOdontologo(int id) throws UnirestException {
        Unirest.delete(urlOdontologos.concat("/eliminar/{id}"))
                .routeParam("id", String.valueOf(id)).asJson();

    }
*/

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
