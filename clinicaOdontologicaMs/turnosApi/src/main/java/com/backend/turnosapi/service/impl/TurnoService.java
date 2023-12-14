package com.backend.turnosapi.service.impl;


import com.backend.dto.entrada.turno.TurnoEntradaDto;
import com.backend.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.dto.salida.paciente.PacienteSalidaDto;
import com.backend.dto.salida.turno.OdontologoTurnoSalidaDto;
import com.backend.dto.salida.turno.PacienteTurnoSalidaDto;
import com.backend.dto.salida.turno.TurnoSalidaDto;
import com.backend.turnosapi.entity.Turno;
import com.backend.turnosapi.exceptions.BadRequestException;
import com.backend.turnosapi.exceptions.ResourceNotFoundException;
import com.backend.turnosapi.repository.TurnoRepository;
import com.backend.turnosapi.service.ITurnoService;
import com.backend.turnosapi.utils.JsonPrinter;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoService implements ITurnoService {

    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);

    private final TurnoRepository turnoRepository;
    private final ModelMapper modelMapper;
    private final OdontologoService odontologoService;
    private final PacienteService pacienteService;

    @Autowired
    public TurnoService(TurnoRepository turnoRepository, ModelMapper modelMapper, OdontologoService odontologoService, PacienteService pacienteService) {
        this.turnoRepository = turnoRepository;
        this.modelMapper = modelMapper;
        this.odontologoService = odontologoService;
        this.pacienteService = pacienteService;

    }

    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) throws BadRequestException, UnirestException {
        TurnoSalidaDto turnoSalidaDto;

        PacienteSalidaDto paciente = pacienteService.buscarPacientePorId(turnoEntradaDto.getPacienteId());
        LOGGER.info("Paciente encontrado: {}", JsonPrinter.toString(paciente));
        OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(turnoEntradaDto.getOdontologoId());
        LOGGER.info("Odontologo encontrado: {}", JsonPrinter.toString(odontologo));

        String pacienteNoEnBdd = "El paciente no se encuentra en nuestra base de datos";
        String odontologoNoEnBdd = "El odontologo no se encuentra en nuestra base de datos";

        if (paciente == null || odontologo == null) {
            if (paciente == null && odontologo == null) {
                LOGGER.error("El paciente y el odontologo no se encuentran en nuestra base de datos");
                throw new BadRequestException("El paciente y el odontologo no se encuentran en nuestra base de datos");
            } else if (paciente == null) {
                LOGGER.error(pacienteNoEnBdd);
                throw new BadRequestException(pacienteNoEnBdd);
            } else {
                LOGGER.error(odontologoNoEnBdd);
                throw new BadRequestException(odontologoNoEnBdd);
            }
        } else {

            Turno turnoAGuardar = modelMapper.map(turnoEntradaDto, Turno.class);
            Turno turnoNuevo = turnoRepository.save(turnoAGuardar);
            turnoSalidaDto = entidadADto(turnoNuevo);

            LOGGER.info("Nuevo turno registrado con exito: {}", JsonPrinter.toString(turnoSalidaDto));
        }

        return turnoSalidaDto;
    }

    @Override
    public List<TurnoSalidaDto> listarTurnos() {

        List<TurnoSalidaDto> turnos = turnoRepository.findAll().stream()
                .map(t -> {
                    try {
                        return entidadADto(t);
                    } catch (UnirestException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        LOGGER.info("Listado de todos los turnos: {}", JsonPrinter.toString(turnos));

        return turnos;

    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) throws UnirestException {
        Turno turnoBuscado = turnoRepository.findById(id).orElse(null);

        TurnoSalidaDto turnoSalidaDto = null;
        if (turnoBuscado != null) {
            turnoSalidaDto = entidadADto(turnoBuscado);
            LOGGER.info("Turno encontrado: {}", JsonPrinter.toString(turnoSalidaDto));
        } else LOGGER.error("El id no se encuentra registrado en la base de datos");

        return turnoSalidaDto;
    }

    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException, UnirestException {
        if (buscarTurnoPorId(id) != null) {
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id: {}", id);
        } else {
            LOGGER.error("No se ha encontrado el turno con id {}", id);
            throw new ResourceNotFoundException("No se ha encontrado el turno con id " + id);
        }


    }


    public PacienteTurnoSalidaDto pacienteSalidaDtoASalidaTurnoDto(Long id) throws UnirestException {
        return modelMapper.map(pacienteService.buscarPacientePorId(id), PacienteTurnoSalidaDto.class);
    }

    public OdontologoTurnoSalidaDto odontologoSalidaDtoASalidaTurnoDto(Long id) throws UnirestException {
        return modelMapper.map(odontologoService.buscarOdontologoPorId(id), OdontologoTurnoSalidaDto.class);
    }

    public TurnoSalidaDto entidadADto(Turno turno) throws UnirestException {
        TurnoSalidaDto turnoSalidaDto = modelMapper.map(turno, TurnoSalidaDto.class);
        turnoSalidaDto.setPacienteTurnoSalidaDto(pacienteSalidaDtoASalidaTurnoDto(turno.getPaciente().getId()));
        turnoSalidaDto.setOdontologoTurnoSalidaDto(odontologoSalidaDtoASalidaTurnoDto(turno.getOdontologo().getId()));
        return turnoSalidaDto;
    }


}