package com.backend.odontologosapi.service.impl;



import com.backend.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.odontologosapi.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class OdontologoServiceTest {

    @Autowired
    private OdontologoService odontologoService;

    @Test
    @Order(1)
    void deberiaInsertarUnOdontologo() {
        OdontologoEntradaDto odontologoAInsertar = new OdontologoEntradaDto("AD-564656546456", "Patricia", "Lopez");
        OdontologoSalidaDto odontologoDto = odontologoService.registrarOdontologo(odontologoAInsertar);

        Assertions.assertNotNull(odontologoDto);
        Assertions.assertNotNull(odontologoDto.getId());

    }


    @Test
    @Order(2)
    void deberiaListarUnSoloOdontologo() {
        List<OdontologoSalidaDto> odontologoDtos = odontologoService.listarOdontologos();
        assertEquals(1, odontologoDtos.size());
    }

    @Test
    @Order(3)
    void deberiaEliminarElOdontologoId1() throws ResourceNotFoundException {
        odontologoService.eliminarOdontologo(1L);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> odontologoService.eliminarOdontologo(1L));
    }

    @Test
    @Order(4)
    void deberiaRetornarUnaListaVacia() {
        List<OdontologoSalidaDto> odontologoDtos = odontologoService.listarOdontologos();
        Assertions.assertTrue(odontologoDtos.isEmpty());
    }

}