package com.backend.pacientesapi.service;


import com.backend.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.dto.salida.paciente.PacienteSalidaDto;
import com.backend.pacientesapi.exceptions.ResourceNotFoundException;

import java.util.List;

public interface IPacienteService {
    List<PacienteSalidaDto> listarPacientes();

    PacienteSalidaDto registrarPaciente(PacienteEntradaDto paciente);

    PacienteSalidaDto buscarPacientePorId(Long id);

    void eliminarPaciente(Long id) throws ResourceNotFoundException;

    PacienteSalidaDto modificarPaciente(PacienteModificacionEntradaDto pacienteModificado) throws ResourceNotFoundException;


}
