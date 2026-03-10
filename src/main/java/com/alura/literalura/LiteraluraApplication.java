package com.alura.literalura;

import com.alura.literalura.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.alura.literalura.service.CatalogService;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LiteraluraApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    // Creamos un CommandLineRunner para que Spring inyecte CatalogService
    @Bean
    CommandLineRunner init(CatalogService catalogService) {
        return args -> {
            Principal principal = new Principal(catalogService);
            principal.mostrarMenu();
        };
    }
}