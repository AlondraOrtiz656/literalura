package com.alura.literalura.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column
    private String idioma;

    @Column(name = "download_count")
    private Double numeroDescargas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro() {}

    public Libro(String titulo, String idioma, Double numeroDescargas) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDescargas = numeroDescargas;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        String autorNombre = (autor != null ? autor.getNombre() : "unknown");
        return """
               ----- LIBRO -----
               Titulo: %s
               Autor: %s
               Idioma: %s
               Numero de descargas: %s
               -------------------
               """.formatted(
                titulo != null ? titulo : "unknown",
                autorNombre,
                idioma != null ? idioma : "unknown",
                numeroDescargas != null ? String.format("%.1f", numeroDescargas) : "0.0"
        );
    }
}