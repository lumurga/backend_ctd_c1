package com.backend.turnosapi.service;


import com.backend.dto.entrada.turno.TurnoEntradaDto;
import com.backend.dto.salida.turno.TurnoSalidaDto;
import com.backend.turnosapi.exceptions.BadRequestException;
import com.backend.turnosapi.exceptions.ResourceNotFoundException;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public interface ITurnoService {
    TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) throws BadRequestException, UnirestException;

    List<TurnoSalidaDto> listarTurnos();

    TurnoSalidaDto buscarTurnoPorId(Long id) throws UnirestException;

    void eliminarTurno(Long id) throws ResourceNotFoundException, UnirestException;


}
