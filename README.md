Literalura
Literalura es una aplicación de consola desarrollada en Java con Spring Boot que permite consultar información sobre libros y autores utilizando datos obtenidos desde la API pública de Gutendex (Proyecto Gutenberg).

La aplicación permite buscar libros por título, almacenarlos en una base de datos y consultar diferentes tipos de información sobre libros y autores.

Tecnologías utilizadas
Java 17+

Spring Boot

Spring Data JPA

Hibernate

PostgreSQL

Maven

API Gutendex

Jackson / Gson para manejo de JSON

Funcionalidades
La aplicación ofrece un menú interactivo en consola con las siguientes opciones:

Buscar libro por título

Consulta la API Gutendex.

Obtiene información del libro.

Guarda el libro y su autor en la base de datos.

Listar libros registrados

Muestra todos los libros guardados en la base de datos.

Incluye: título, autor, idioma y número de descargas.

Listar autores registrados

Muestra todos los autores almacenados.

Incluye: nombre, año de nacimiento, año de fallecimiento y libros registrados.

Listar autores vivos en un determinado año

Permite ingresar un año.

Muestra los autores que estaban vivos en ese momento.

Listar libros por idioma

Permite filtrar los libros por idioma.

Ejemplo de idiomas disponibles:

es -> Español

en -> Inglés

fr -> Francés

pt -> Portugués

Estructura del proyecto
Plaintext
literalura
│
├── entity
│   ├── Autor.java
│   └── Libro.java
│
├── repository
│   ├── AutorRepository.java
│   └── LibroRepository.java
│
├── service
│   └── CatalogService.java
│
├── principal
│   └── Principal.java
│
├── LiteraluraApplication.java
Base de datos
El proyecto utiliza PostgreSQL para almacenar la información.

Tablas principales:

Autor: id, nombre, birth_year, death_year.

Libro: id, titulo, idioma, download_count, autor_id.

Relación:

Autor (1) ---- (N) Libro

Configuración del proyecto
Configurar el archivo: src/main/resources/application.properties

Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=postgres
spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.format-sql=true
Cómo ejecutar el proyecto
Clonar el repositorio:

Bash
git clone https://github.com/AlondraOrtiz656/ChallengeAlura-conversor-de-monedas
Entrar al proyecto:

Bash
cd literalura
Ejecutar con Maven:

Bash
mvn spring-boot:run
O desde IntelliJ IDEA ejecutando la clase LiteraluraApplication.java.

Ejemplo de uso
Plaintext
========== Libros registrados ==========

----- LIBRO -----
Titulo: Emma
Autor: Jane Austen
Idioma: en
Numero de descargas: 13042
-------------------

----- LIBRO -----
Titulo: Don Quijote
Autor: Miguel de Cervantes
Idioma: es
Numero de descargas: 13446
-------------------
API utilizada
La información de los libros se obtiene desde Gutendex API: https://gutendex.com

Autor
Proyecto desarrollado por Alondra Ortiz como parte del programa:
Oracle Next Education (ONE) + Alura Latam
