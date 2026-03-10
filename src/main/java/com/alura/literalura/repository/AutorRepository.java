package com.alura.literalura.repository;

import com.alura.literalura.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombre(String nombre);

    // Reemplaza o deja este método en lugar del anterior findVivosEnAno
    @Query("SELECT DISTINCT a FROM Autor a LEFT JOIN FETCH a.libros " +
            "WHERE a.birthYear <= :year AND (a.deathYear IS NULL OR a.deathYear >= :year)")
    List<Autor> findVivosEnAnoWithLibros(@Param("year") Integer year);

    // opcional: mantener findAllWithLibros que ya tenías
    @Query("SELECT DISTINCT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> findAllWithLibros();
}