package com.backend.turnosapi.service.impl;


import com.backend.dto.entrada.turno.TurnoEntradaDto;
import com.backend.dto.salida.turno.TurnoSalidaDto;
import com.backend.turnosapi.TestObjects;
import com.backend.turnosapi.entity.Turno;
import com.backend.turnosapi.exceptions.BadRequestException;
import com.backend.turnosapi.repository.TurnoRepository;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class TurnoServiceTest {

    private final TurnoRepository turnoRepository = mock(TurnoRepository.class);
    private final OdontologoService odontologoService = mock(OdontologoService.class);
    private final PacienteService pacienteService = mock(PacienteService.class);
    private final ModelMapper modelMapper = mock(ModelMapper.class);

    private final TurnoService turnoService = new TurnoService(turnoRepository, modelMapper, odontologoService, pacienteService);

    private final TestObjects testObjects = new TestObjects();

    LocalDateTime fechaYHora = LocalDateTime.of(LocalDate.of(2023, 12, 10), LocalTime.of(10, 30));

    @Test
    @Order(1)
    void deberiaInsertarUnTurno() throws BadRequestException, UnirestException {
        Turno turno = new Turno(testObjects.getOdontologo(), testObjects.getPaciente(), fechaYHora);
        Turno turnoGuardado = new Turno(1L, testObjects.getOdontologo(), testObjects.getPaciente(), fechaYHora);
        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto(1L, 1L, fechaYHora);

        when(odontologoService.buscarOdontologoPorId(1L)).thenReturn(testObjects.getOdontologoSalidaDto());
        when(pacienteService.buscarPacientePorId(1L)).thenReturn(testObjects.getPacienteSalidaDto());

        when(modelMapper.map(turnoEntradaDto, Turno.class)).thenReturn(turno);
        when(turnoRepository.save(turno)).thenReturn(turnoGuardado);
        when(modelMapper.map(turnoGuardado, TurnoSalidaDto.class)).thenReturn(new TurnoSalidaDto(1L, testObjects.getPacienteTurnoSalidaDto(), testObjects.getOdontologoTurnoSalidaDto(), fechaYHora));


        TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);


        Assertions.assertNotNull(turnoSalidaDto.getId());

    }


    @Test
    void cuandoSeIntentaRegistrarUnTurnoConPacienteNoExistente_deberiaLanzarseUnaBadRequestException() throws UnirestException {
        when(odontologoService.buscarOdontologoPorId(1L)).thenReturn(testObjects.getOdontologoSalidaDto());
        when(pacienteService.buscarPacientePorId(2L)).thenReturn(null);

        Assertions.assertThrows(BadRequestException.class, () -> turnoService.registrarTurno(new TurnoEntradaDto(2L, 1L, fechaYHora)));

    }

    @Test
    void cuandoSeIntentaRegistrarUnTurnoConOdontologoNoExistente_deberiaLanzarseUnaBadRequestException() throws UnirestException {
        when(odontologoService.buscarOdontologoPorId(2L)).thenReturn(null);
        when(pacienteService.buscarPacientePorId(1L)).thenReturn(testObjects.getPacienteSalidaDto());

        Assertions.assertThrows(BadRequestException.class, () -> turnoService.registrarTurno(new TurnoEntradaDto(1L, 2L, fechaYHora)));

    }

}
