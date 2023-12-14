package com.backend.odontologosapi.repository;


import com.backend.odontologosapi.model.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OdontologoRepository extends JpaRepository<Odontologo, Long> {
}

