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
import java.util.stream.Collectors;

public class Principal {

    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();

    // Almacenamiento en memoria (temporal). Más adelante lo cambias por JPA.
    private final List<DatosLibro> librosGuardados = new ArrayList<>();

    // método ya existente para pruebas (puedes mantenerlo o eliminarlo)
    public void muestraDatos() {
        String url = "https://gutendex.com/books/?search=tolkien";
        String json = consumoAPI.obtenerDatos(url);
        DatosRespuesta datos = conversor.obtenerDatos(json, DatosRespuesta.class);
        System.out.println(datos.getResultados());
    }

    // Método que muestra el menú y gestiona la interacción
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
                    case 1 -> buscarLibroPorTitulo(teclado);
                    case 2 -> listarLibrosGuardados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosEnAno(teclado);
                    case 5 -> listarLibrosPorIdioma(teclado);
                    case 0 -> System.out.println("Cerrando aplicación. ¡Hasta luego!");
                    default -> System.out.println("Opción no reconocida.");
                }
            }
        }
    }

    private void buscarLibroPorTitulo(Scanner teclado) {
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

            // DEBUG: puedes comentar la siguiente línea cuando esté todo ok
            System.out.println("JSON recibido:");
            System.out.println(json);

            DatosRespuesta datos = conversor.obtenerDatos(json, DatosRespuesta.class);

            if (datos.getResultados() != null && !datos.getResultados().isEmpty()) {
                DatosLibro libro = datos.getResultados().get(0); // primer resultado
                System.out.println(libro); // usa el toString formateado en DatosLibro

                // Guardarlo en la lista temporal si no está ya guardado (evita duplicados simples)
                boolean ya = librosGuardados.stream()
                        .anyMatch(l -> l.getTitulo().equalsIgnoreCase(libro.getTitulo()));
                if (!ya) {
                    librosGuardados.add(libro);
                    System.out.println("Libro guardado en catálogo local.");
                } else {
                    System.out.println("El libro ya existe en el catálogo local.");
                }
            } else {
                System.out.println("No se encontraron resultados para: " + titulo);
            }

        } catch (RuntimeException e) {
            System.out.println("Error al consumir la API: " + e.getMessage());
        }
    }

    private void listarLibrosGuardados() {
        System.out.println();
        System.out.println("========== Libros registrados ==========");
        if (librosGuardados.isEmpty()) {
            System.out.println("No hay libros registrados todavía.");
            return;
        }

        for (DatosLibro libro : librosGuardados) {
            System.out.println(libro); // usa el toString formateado
        }
    }

    private void listarAutoresRegistrados() {
        System.out.println();
        System.out.println("========== Autores registrados ==========");
        Set<String> autores = new LinkedHashSet<>();

        for (DatosLibro libro : librosGuardados) {
            if (libro.getAutores() != null) {
                for (DatosAutor a : libro.getAutores()) {
                    if (a != null && a.getNombre() != null) {
                        autores.add(a.getNombre());
                    }
                }
            }
        }

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados todavía.");
            return;
        }

        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosEnAno(Scanner teclado) {
        System.out.print("Introduce el año (ej: 1890): ");
        String linea = teclado.nextLine().trim();
        int año;
        try {
            año = Integer.parseInt(linea);
        } catch (NumberFormatException e) {
            System.out.println("Año inválido.");
            return;
        }

        // Coleccionar autores únicos con birth/death si están disponibles
        Map<String, DatosAutor> mapa = new LinkedHashMap<>();
        for (DatosLibro libro : librosGuardados) {
            if (libro.getAutores() != null) {
                for (DatosAutor a : libro.getAutores()) {
                    if (a != null && a.getNombre() != null) {
                        mapa.putIfAbsent(a.getNombre(), a);
                    }
                }
            }
        }

        List<DatosAutor> vivos = mapa.values().stream()
                .filter(a -> {
                    Integer birth = a.getBirthYear();
                    Integer death = a.getDeathYear();
                    // consideramos vivo si birth <= año y (death == null || death >= año)
                    if (birth != null && birth <= año) {
                        return death == null || death >= año;
                    }
                    return false;
                })
                .collect(Collectors.toList());

        if (vivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año: " + año);
            return;
        }

        System.out.println("Autores vivos en " + año + ":");
        vivos.forEach(a -> System.out.println(a.getNombre() + (a.getBirthYear() != null ? " (nac: " + a.getBirthYear() + ")" : "")));
    }

    private void listarLibrosPorIdioma(Scanner teclado) {
        System.out.print("Introduce el idioma (ej: en, es, fr): ");
        String idioma = teclado.nextLine().trim().toLowerCase();

        if (idioma.isEmpty()) {
            System.out.println("Idioma vacío. Cancelando.");
            return;
        }

        List<DatosLibro> filtrados = new ArrayList<>();
        for (DatosLibro libro : librosGuardados) {
            if (libro.getIdioma() != null && libro.getIdioma().toLowerCase().startsWith(idioma)) {
                filtrados.add(libro);
            }
        }

        if (filtrados.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma: " + idioma);
            return;
        }

        System.out.println("=== Libros en '" + idioma + "' ===");
        filtrados.forEach(System.out::println); // usa toString formateado
    }
}
