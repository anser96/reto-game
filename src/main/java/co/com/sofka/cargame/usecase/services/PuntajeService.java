package co.com.sofka.cargame.usecase.services;

import co.com.sofka.cargame.domain.juego.values.JuegoId;
import co.com.sofka.cargame.usecase.model.Puntaje;

import java.util.List;

public interface PuntajeService {
    List<Puntaje> getPuntajeGame(JuegoId juegoId);

}
