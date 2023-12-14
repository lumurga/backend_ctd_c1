package com.backend.pacientesapi.controller;

import com.backend.dto.entrada.paciente.DomicilioEntradaDto;
import com.backend.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.dto.salida.paciente.DomicilioSalidaDto;
import com.backend.dto.salida.paciente.PacienteSalidaDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class PacienteControllerTest {

    @Autowired
    private PacienteController pacienteController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void deberiaRegistrarUnPacienteMedianteApi() throws Exception {

        //preparamos un objeto de entrada y uno de salida
        PacienteEntradaDto pacienteEntradaDto = new PacienteEntradaDto("Nombre", "Apellido", 2222222, LocalDate.of(2023, 12, 24), new DomicilioEntradaDto("Calle", 1, "Localidad", "Provincia"));
        PacienteSalidaDto pacienteSalidaDto = new PacienteSalidaDto(1L, "Nombre", "Apellido", 2222222, LocalDate.of(2023, 12, 24), new DomicilioSalidaDto(1L, "Calle", 1, "Localidad", "Provincia"));

        //configuramos un mapper para transformar json <-> objeto <-> json segun las necesidades puntuales
        ObjectWriter writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule())
                .registerModule(new SimpleModule().addSerializer(LocalDate.class, new ToStringSerializer()))
                .writer();


        String requestPayload = writer.writeValueAsString(pacienteEntradaDto);
        String expectedResponse = writer.writeValueAsString(pacienteSalidaDto);

        //ejecutamos el cliente http mockMvc para simular y testear el comportamiento del endpoint registrarPaciente de la api de Pacientes
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/pacientes/registrar")
                        .content(requestPayload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(content().contentType("application/json"))
                .andReturn();

        //comprobamos
        Assertions.assertEquals(expectedResponse, result.getResponse().getContentAsString());


    }

    @Test
    @Order(2)
    void deberiaEncontrarPacienteConId1() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{id}", 1))
                    .andDo(print())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(status().is(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.dni").value(2222222))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.apellido").value("Apellido"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}