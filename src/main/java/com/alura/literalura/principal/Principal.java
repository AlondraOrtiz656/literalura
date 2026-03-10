package com.alura.literalura.principal;
import com.alura.literalura.model.DatosAutor;
import com.alura.literalura.model.DatosLibro;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import com.alura.literalura.model.DatosRespuesta;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import com.alura.literalura.service.CatalogService;
import com.alura.literalura.entity.Libro;

public class Principal {

    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final CatalogService catalogService;

    public Principal(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public void mostrarMenu() {
        try (Scanner teclado = new Scanner(System.in)) {

            int opcion = -1;

            while (opcion != 0) {
                System.out.println();
                System.out.println("----------");
                System.out.println("Elija la opción a través de su número:");
                System.out.println("1- buscar libro por título");
                System.out.println("2- listar libros registrados");
                System.out.println("3- listar autores registrados");
                System.out.println("4- listar autores vivos en un determinado año");
                System.out.println("5- listar libros por idioma");
                System.out.println("0 - salir");
                System.out.print("Elige una opción: ");

                String linea = teclado.nextLine().trim();
                if (linea.isEmpty()) {
                    System.out.println("Opción vacía. Intenta de nuevo.");
                    continue;
                }

                try {
                    opcion = Integer.parseInt(linea);
                } catch (NumberFormatException e) {
                    System.out.println("Opción inválida. Ingresa un número.");
                    continue;
                }

                switch (opcion) {
                    case 1 -> buscarYGuardarLibro(teclado);
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosEnAno(teclado);
                    case 5 -> listarLibrosPorIdioma(teclado);
                    case 0 -> System.out.println("Cerrando aplicación. ¡Hasta luego!");
                    default -> System.out.println("Opción no reconocida.");
                }
            }
        }
    }

    private void buscarYGuardarLibro(Scanner teclado) {
        System.out.print("Escribe el título (o parte del título) a buscar: ");
        String titulo = teclado.nextLine().trim();

        if (titulo.isEmpty()) {
            System.out.println("Título vacío. Cancelando búsqueda.");
            return;
        }

        String consulta = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        String url = "https://gutendex.com/books/?search=" + consulta;

        try {
            String json = consumoAPI.obtenerDatos(url);
            System.out.println("JSON recibido:");
            System.out.println(json);

            DatosRespuesta datos = conversor.obtenerDatos(json, DatosRespuesta.class);

            if (datos.getResultados() != null && !datos.getResultados().isEmpty()) {
                DatosLibro datoLibro = datos.getResultados().get(0); // primer resultado
                Libro saved = catalogService.guardarDesdeDatosLibro(datoLibro);
                System.out.println("Libro guardado en BD:");
                System.out.println(saved); // usa toString() de entidad Libro
            } else {
                System.out.println("No se encontraron resultados para: " + titulo);
            }

        } catch (RuntimeException e) {
            System.out.println("Error al consumir la API: " + e.getMessage());
        }
    }

    private void listarLibrosRegistrados() {
        System.out.println();
        System.out.println("========== Libros registrados ==========");
        List<Libro> libros = catalogService.listarLibros();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados todavía.");
            return;
        }
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        System.out.println();
        System.out.println("========== Autores registrados ==========");
        catalogService.listarAutores().forEach(a -> System.out.println(a.toString()));
    }

    private void listarAutoresVivosEnAno(Scanner teclado) {
        System.out.print("Introduce el año (ej: 1890): ");
        String linea = teclado.nextLine().trim();
        int year;
        try {
            year = Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            System.out.println("Año inválido.");
            return;
        }

        var vivos = catalogService.listarAutoresVivosEnAno(year);
        if (vivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año: " + year);
            return;
        }
        vivos.forEach(a -> System.out.println(a.toString()));
    }

    private void listarLibrosPorIdioma(Scanner teclado) {
        System.out.print("Introduce el idioma (ej: en, es, fr): ");
        String idioma = teclado.nextLine().trim().toLowerCase();

        if (idioma.isEmpty()) {
            System.out.println("Idioma vacío. Cancelando.");
            return;
        }

        var filtrados = catalogService.listarLibrosPorIdioma(idioma);
        if (filtrados.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma: " + idioma);
            return;
        }

        System.out.println("=== Libros en '" + idioma + "' ===");
        filtrados.forEach(System.out::println);
    }
}