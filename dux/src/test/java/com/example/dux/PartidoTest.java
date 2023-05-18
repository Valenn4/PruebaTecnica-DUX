package com.example.dux;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
class PartidoTest {

    @Test
    void probabilidad(){
        Map<String, String> datosPartido = new HashMap<>();
        datosPartido.put("probabilidadJugador1","100");
        int resultado = Partido.probabilidad(datosPartido);
        Assertions.assertEquals(1, resultado);
    }
    @Test
    void games() {
        Map<String, Object> datosGames = new HashMap<>();
        datosGames.put("puntajeDeGameActualJugador1", "15");
        datosGames.put("puntajeDeGameActualJugador2", "0");
        datosGames.put("gamesGanadosJugador1", 3);
        Map<String, Object> datosSets = new HashMap<>();

        Map<String, String> datosPartido = new HashMap<>();
        datosPartido.put("probabilidadJugador1","100");

        Partido.probabilidad(datosPartido);
        Partido.games(datosPartido, datosGames, datosSets);

        Assertions.assertEquals("30", datosGames.get("puntajeDeGameActualJugador1"));
    }

    @Test
    void gameGanado(){
        int ganadorDelPunto = 2;
        Map<String, Object> datosGames = new HashMap<>();
        datosGames.put("puntajeDeGameActualJugador1", "40");
        datosGames.put("puntajeDeGameActualJugador2", "AC");
        datosGames.put("gamesGanadosJugador1", 1);
        datosGames.put("gamesGanadosJugador2", 2);
        datosGames.put("gameActual", 4);
        datosGames.put("turnoDeSaque", 1);
        Map<String, Object> datosSets = new HashMap<>();

        Map<String, String> datosPartido = new HashMap<>();
        datosPartido.put("jugador1", "Valentino");
        datosPartido.put("jugador2", "Pablo");
        datosPartido.put("probabilidadJugador1","100");

        List<Map<String, Object>> resultado = Partido.gameGanado(ganadorDelPunto, datosGames, datosSets, datosPartido);

        Assertions.assertEquals(5, resultado.get(0).get("gameActual"));
        Assertions.assertEquals(3, resultado.get(0).get("gamesGanadosJugador2"));
        Assertions.assertEquals(2, resultado.get(0).get("turnoDeSaque"));
        Assertions.assertEquals("0", resultado.get(0).get("puntajeDeGameActualJugador1"));
        Assertions.assertEquals("0", resultado.get(0).get("puntajeDeGameActualJugador2"));

    }
}