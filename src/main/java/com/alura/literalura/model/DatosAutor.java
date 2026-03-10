package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosAutor {

    @JsonAlias("name")
    private String nombre;

    // campos opcionales que la API puede devolver
    @JsonAlias("birth_year")
    private Integer birthYear;

    @JsonAlias("death_year")
    private Integer deathYear;

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

    @Override
    public String toString() {
        return nombre != null ? nombre : "unknown";
    }
}