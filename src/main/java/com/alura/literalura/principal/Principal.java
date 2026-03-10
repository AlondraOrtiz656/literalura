package com.alura.literalura.principal;

import com.alura.literalura.service.ConsumoAPI;

public class Principal {

    public void muestraDatos() {

        var consumoAPI = new ConsumoAPI();

        String url = "https://gutendex.com/books/?search=tolkien";

        String json = consumoAPI.obtenerDatos(url);

        System.out.println(json);
    }
}
