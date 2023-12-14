package com.backend.clinicaodontologica.service.impl;

import com.backend.clinicaodontologica.dto.entrada.turno.TurnoEntradaDto;
import com.backend.clinicaodontologica.dto.modificacion.TurnoModificacionEntradaDto;
import com.backend.clinicaodontologica.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.clinicaodontologica.dto.salida.paciente.PacienteSalidaDto;
import com.backend.clinicaodontologica.dto.salida.turno.OdontologoTurnoSalidaDto;
import com.backend.clinicaodontologica.dto.salida.turno.PacienteTurnoSalidaDto;
import com.backend.clinicaodontologica.dto.salida.turno.TurnoSalidaDto;
import com.backend.clinicaodontologica.entity.Odontologo;
import com.backend.clinicaodontologica.entity.Paciente;
import com.backend.clinicaodontologica.entity.Turno;
import com.backend.clinicaodontologica.exceptions.BadRequestException;
import com.backend.clinicaodontologica.exceptions.ResourceNotFoundException;
import com.backend.clinicaodontologica.repository.TurnoRepository;
import com.backend.clinicaodontologica.service.ITurnoService;
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
    private final PacienteService pacienteService;
    private OdontologoService odontologoService;


    @Autowired
    public TurnoService(TurnoRepository turnoRepository, ModelMapper modelMapper, OdontologoService odontologoService, PacienteService pacienteService) {
        this.turnoRepository = turnoRepository;
        this.modelMapper = modelMapper;
        this.odontologoService = odontologoService;
        this.pacienteService = pacienteService;

    }

    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) throws BadRequestException {
        TurnoSalidaDto turnoSalidaDto;

        PacienteSalidaDto paciente = pacienteService.buscarPacientePorId(turnoEntradaDto.getPacienteId());
        OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(turnoEntradaDto.getOdontologoId());

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

            Turno turnoNuevo = turnoRepository.save(modelMapper.map(turnoEntradaDto, Turno.class));
            turnoSalidaDto = entidadADto(turnoNuevo);

            LOGGER.info("Nuevo turno registrado con exito: {}", turnoSalidaDto);
        }

        return turnoSalidaDto;
    }

    @Override
    public List<TurnoSalidaDto> listarTurnos() {
        List<TurnoSalidaDto> turnos = turnoRepository.findAll().stream()
                .map(this::entidadADto).toList();

        LOGGER.info("Listado de todos los turnos: {}", turnos);

        return turnos;

    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) {
        Turno turnoBuscado = turnoRepository.findById(id).orElse(null);

        TurnoSalidaDto turnoSalidaDto = null;
        if (turnoBuscado != null) {
            turnoSalidaDto = entidadADto(turnoBuscado);
            LOGGER.info("Turno encontrado: {}", turnoSalidaDto);
        } else LOGGER.error("El id no se encuentra registrado en la base de datos");

        return turnoSalidaDto;
    }

    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException {
        if (buscarTurnoPorId(id) != null) {
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id: {}", id);
        } else {
            LOGGER.error("No se ha encontrado el turno con id {}", id);
            throw new ResourceNotFoundException("No se ha encontrado el turno con id " + id);
        }


    }

    @Override
    public TurnoSalidaDto modificarTurno(TurnoModificacionEntradaDto turnoModificacionEntradaDto) throws ResourceNotFoundException {

        Turno turnoAActualizar = turnoRepository.findById(turnoModificacionEntradaDto.getId()).orElse(null);
        TurnoSalidaDto turnoSalidaDto = null;

        if (turnoAActualizar != null) {

            turnoAActualizar.setPaciente(modelMapper.map(pacienteService.buscarPacientePorId(turnoModificacionEntradaDto.getPaciente()), Paciente.class));
            turnoAActualizar.setOdontologo(modelMapper.map(odontologoService.buscarOdontologoPorId(turnoModificacionEntradaDto.getOdontologo()), Odontologo.class));
            turnoAActualizar.setFechaYHora(turnoModificacionEntradaDto.getFechaYHora());
            turnoRepository.save(turnoAActualizar);

            turnoSalidaDto = entidadADto(turnoAActualizar);

            LOGGER.warn("Turno actualizado: {}", turnoSalidaDto);

        } else {
            LOGGER.error("No fue posible actualizar los datos ya que el turno no se encuentra registrado");
            throw new ResourceNotFoundException("No fue posible actualizar los datos ya que el turno no se encuentra registrado");
        }


        return turnoSalidaDto;
    }


    private PacienteTurnoSalidaDto pacienteSalidaDtoASalidaTurnoDto(Long id) {
        return modelMapper.map(pacienteService.buscarPacientePorId(id), PacienteTurnoSalidaDto.class);
    }

    private OdontologoTurnoSalidaDto odontologoSalidaDtoASalidaTurnoDto(Long id) {
        return modelMapper.map(odontologoService.buscarOdontologoPorId(id), OdontologoTurnoSalidaDto.class);
    }

    private TurnoSalidaDto entidadADto(Turno turno) {
        TurnoSalidaDto turnoSalidaDto = modelMapper.map(turno, TurnoSalidaDto.class);
        turnoSalidaDto.setPacienteTurnoSalidaDto(pacienteSalidaDtoASalidaTurnoDto(turno.getPaciente().getId()));
        turnoSalidaDto.setOdontologoTurnoSalidaDto(odontologoSalidaDtoASalidaTurnoDto(turno.getOdontologo().getId()));
        return turnoSalidaDto;
    }

}
