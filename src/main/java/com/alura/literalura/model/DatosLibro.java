package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosLibro {

    @JsonAlias("title")
    private String titulo;

    @JsonAlias("authors")
    private List<DatosAutor> autores;

    @JsonAlias("languages")
    private List<String> languages;

    @JsonAlias("download_count")
    private Integer downloadCount;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<DatosAutor> getAutores() {
        return autores;
    }

    public void setAutores(List<DatosAutor> autores) {
        this.autores = autores;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    // Conveniencia: devuelve el primer idioma o "unknown"
    public String getIdioma() {
        if (languages != null && !languages.isEmpty()) {
            return languages.get(0);
        }
        return "unknown";
    }

    // Conveniencia: devuelve el primer autor (nombre) o "unknown"
    public String getPrimerAutorNombre() {
        if (autores != null && !autores.isEmpty() && autores.get(0) != null && autores.get(0).getNombre() != null) {
            return autores.get(0).getNombre();
        }
        return "unknown";
    }

    @Override
    public String toString() {
        String tituloMostrar = titulo != null ? titulo : "unknown";
        String autorMostrar = getPrimerAutorNombre();
        String idiomaMostrar = getIdioma();
        double descargas = downloadCount != null ? downloadCount.doubleValue() : 0.0;

        return """
               ----- LIBRO -----
               Titulo: %s
               Autor: %s
               Idioma: %s
               Numero de descargas: %s
               -------------------
               """.formatted(
                tituloMostrar,
                autorMostrar,
                idiomaMostrar,
                String.format("%.1f", descargas)
        );
    }
}