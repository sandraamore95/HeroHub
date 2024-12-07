package com.HeroHub.HeroHub.repository;

import com.HeroHub.HeroHub.models.Personaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// la capa que interactúa directamente con la base de datos utilizando JPA.
public interface PersonajeRepository  extends JpaRepository <Personaje, Long>{
    Optional<Personaje> findByNombre(String nombre);

    @Query("SELECT p.descripcion FROM Personaje p WHERE p.id = :id")
    String findDescripcionById(@Param("id") Long id);

    @Query("SELECT p.imagen FROM Personaje p WHERE p.id = :id")
    String findImagenById(@Param("id") Long id);
}
