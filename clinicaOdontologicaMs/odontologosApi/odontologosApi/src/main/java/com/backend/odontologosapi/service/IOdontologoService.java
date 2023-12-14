package com.backend.odontologosapi.service;


import com.backend.dto.entrada.modificacion.OdontologoModificacionEntradaDto;
import com.backend.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.odontologosapi.exceptions.ResourceNotFoundException;

import java.util.List;

public interface IOdontologoService {
    List<OdontologoSalidaDto> listarOdontologos();


    OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo);

    OdontologoSalidaDto buscarOdontologoPorId(Long id);

    void eliminarOdontologo(Long id) throws ResourceNotFoundException;

    OdontologoSalidaDto actualizarOdontologo(OdontologoModificacionEntradaDto odontologoModificacionEntradaDto) throws ResourceNotFoundException;

}

