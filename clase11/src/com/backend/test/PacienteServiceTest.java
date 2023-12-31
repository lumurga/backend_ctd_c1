package com.backend.test;

import com.backend.dao.impl.PacienteDaoH2;
import com.backend.model.Domicilio;
import com.backend.model.Paciente;
import com.backend.service.PacienteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PacienteServiceTest {
    private PacienteService pacienteService = new PacienteService(new PacienteDaoH2());


    @BeforeAll
    static void doBefore() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/testClase11;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "sa");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Test
    void deberiaAgregarUnPaciente(){

        Paciente paciente = new Paciente("Nombre", "Apellido", 123456, LocalDate.of(2023, 05, 02), new Domicilio("Calle", 13, "Localidad", "Provincia"));

        Paciente pacienteRegistrado = pacienteService.registrarPaciente(paciente);

        Assertions.assertTrue(pacienteRegistrado.getId() != 0);

    }

    @Test
    void deberiaRetornarUnaListaNoVacia(){

        assertFalse(pacienteService.listarPacientes().isEmpty());

    }
}