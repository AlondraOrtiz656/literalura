package com.alura.literalura.repository;

import com.alura.literalura.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByIdiomaStartingWithIgnoreCase(String idioma);

    // Nuevo: carga libros junto a su autor para evitar LazyInitializationException
    @Query("SELECT l FROM Libro l JOIN FETCH l.autor")
    List<Libro> findAllWithAutor();
}