package com.alura.literalura.service;

import com.alura.literalura.entity.Autor;
import com.alura.literalura.entity.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.model.DatosAutor;
import com.alura.literalura.model.DatosLibro;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {

    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;



    public CatalogService(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    @Transactional
    public Libro guardarDesdeDatosLibro(DatosLibro datos) {
        if (datos == null) throw new IllegalArgumentException("DatosLibro es null");

        // Obtener título, idioma y descargas
        String titulo = datos.getTitulo();
        String idioma = datos.getIdioma();
        Double descargas = datos.getDownloadCount() != null ? datos.getDownloadCount().doubleValue() : 0.0;

        // Tomar primer autor (si existe)
        DatosAutor datosAutor = (datos.getAutores() != null && !datos.getAutores().isEmpty()) ? datos.getAutores().get(0) : null;
        String nombreAutor = datosAutor != null ? datosAutor.getNombre() : "unknown";
        Integer birth = datosAutor != null ? datosAutor.getBirthYear() : null;
        Integer death = datosAutor != null ? datosAutor.getDeathYear() : null;

        // Buscar autor por nombre, si no existe crear
        Optional<Autor> optAutor = autorRepository.findByNombre(nombreAutor);
        Autor autor = optAutor.orElseGet(() -> {
            Autor a = new Autor(nombreAutor, birth, death);
            return autorRepository.save(a);
        });

        // Actualizar años si están vacíos en BD y vienen en datos
        boolean changed = false;
        if (autor.getBirthYear() == null && birth != null) {
            autor.setBirthYear(birth);
            changed = true;
        }
        if (autor.getDeathYear() == null && death != null) {
            autor.setDeathYear(death);
            changed = true;
        }
        if (changed) autorRepository.save(autor);

        // Verificar si el libro ya existe en la base de datos
        Optional<Libro> libroExistente = libroRepository.findByTituloIgnoreCase(titulo);

        if (libroExistente.isPresent()) {
            throw new RuntimeException("Libro ya insertado en la BD.");
        }

        // Crear y guardar libro
        Libro libro = new Libro(titulo, idioma, descargas);
        libro.setAutor(autor);
        Libro saved = libroRepository.save(libro);

        // Mantener relación en memoria
        autor.getLibros().add(saved);

        return saved;
    }



    public List<Libro> listarLibros() {
        // Usamos la query con JOIN FETCH para que el autor venga inicializado
        return libroRepository.findAllWithAutor();
    }

    public List<Autor> listarAutores() {
        return autorRepository.findAllWithLibros();
    }

    public List<Autor> listarAutoresVivosEnAno(Integer ano) {
        // usamos la query con JOIN FETCH para que la colección libros venga inicializada
        return autorRepository.findVivosEnAnoWithLibros(ano);
    }

    public List<Libro> listarLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdiomaStartingWithIgnoreCase(idioma);
    }
}