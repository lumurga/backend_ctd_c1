package com.backend.turnosapi;

import com.backend.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.dto.salida.paciente.DomicilioSalidaDto;
import com.backend.dto.salida.paciente.PacienteSalidaDto;
import com.backend.dto.salida.turno.OdontologoTurnoSalidaDto;
import com.backend.dto.salida.turno.PacienteTurnoSalidaDto;
import com.backend.turnosapi.entity.Domicilio;
import com.backend.turnosapi.entity.Odontologo;
import com.backend.turnosapi.entity.Paciente;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDate;

public class TestObjects {
    private static final Jackson2ObjectMapperBuilder jsonBuilder = Jackson2ObjectMapperBuilder.json();


    private final PacienteSalidaDto pacienteSalidaDto = new PacienteSalidaDto(1L, "Darla", "O.", 252525, LocalDate.of(2024, 01, 01), new DomicilioSalidaDto( 1L, "Wallaby", 42, "Sydney", "Nueva Gales del Sur"));

    private final OdontologoSalidaDto odontologoSalidaDto = new OdontologoSalidaDto(1L, "AD-5415643", "P.", "Sherman");

    private final Paciente paciente = new Paciente(1L, "Darla", "O.", 252525, LocalDate.of(2024, 01, 01), new Domicilio( 1L, "Wallaby", 42, "Sydney", "Nueva Gales del Sur"));
    private final Odontologo odontologo = new Odontologo(1L, "AD-5415643", "P.", "Sherman");

    private final PacienteTurnoSalidaDto pacienteTurnoSalidaDto = new PacienteTurnoSalidaDto(1L, "Darla", "O.");
    private final OdontologoTurnoSalidaDto odontologoTurnoSalidaDto = new OdontologoTurnoSalidaDto(1L, "P.", "Sherman");



    public PacienteSalidaDto getPacienteSalidaDto() {
        return pacienteSalidaDto;
    }

    public OdontologoSalidaDto getOdontologoSalidaDto() {
        return odontologoSalidaDto;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Odontologo getOdontologo() {
        return odontologo;
    }

    public PacienteTurnoSalidaDto getPacienteTurnoSalidaDto() {
        return pacienteTurnoSalidaDto;
    }

    public OdontologoTurnoSalidaDto getOdontologoTurnoSalidaDto() {
        return odontologoTurnoSalidaDto;
    }
}
