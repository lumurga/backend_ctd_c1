package com.backend.odontologosapi.controller;


import com.backend.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.odontologosapi.model.Odontologo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class OdontologoControllerTest {

    @Autowired
    private OdontologoController odontologoController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void deberiaResgistrarUnOdontologoMedianteApi() throws Exception {
        Odontologo odontologo = new Odontologo("AD-564656546456", "Patricia", "Lopez");
        OdontologoSalidaDto odontologoSalidaDto = new OdontologoSalidaDto(1L, "AD-564656546456", "Patricia", "Lopez");

        ObjectWriter writer = new ObjectMapper().configure(SerializationFeature.WRAP_ROOT_VALUE, false).writer();
        String payload = writer.writeValueAsString(odontologo);
        String expectedResponse = writer.writeValueAsString(odontologoSalidaDto);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/odontologos/registrar")
                        .content(String.valueOf(payload))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType("application/json"))
                .andReturn();

        Assertions.assertEquals(expectedResponse, response.getResponse().getContentAsString());

    }

    @Test
    @Order(2)
    void deberiaEncontrarOdontologoConId1PorApi() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/odontologos/{id}", 1))
                    .andDo(print())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(status().is(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.matricula").value("AD-564656546456"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.apellido").value("Lopez"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}