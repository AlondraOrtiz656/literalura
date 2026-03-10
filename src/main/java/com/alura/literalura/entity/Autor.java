package com.alura.literalura.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private Integer birthYear;
    private Integer deathYear;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Libro> libros = new ArrayList<>();

    public Autor() {}

    public Autor(String nombre, Integer birthYear, Integer deathYear) {
        this.nombre = nombre;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void addLibro(Libro libro) {
        libros.add(libro);
        libro.setAutor(this);
    }

    public void removeLibro(Libro libro) {
        libros.remove(libro);
        libro.setAutor(null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Autor: ").append(nombre != null ? nombre : "unknown").append("\n");
        sb.append("Fecha de nacimiento: ").append(birthYear != null ? birthYear : "unknown").append("\n");
        sb.append("Fecha de fallecimiento: ").append(deathYear != null ? deathYear : "unknown").append("\n");
        sb.append("Libros: [");
        for (int i = 0; i < libros.size(); i++) {
            sb.append(libros.get(i).getTitulo());
            if (i < libros.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}