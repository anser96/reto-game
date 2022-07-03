package co.com.sofka.cargame.usecase.services;

import co.com.sofka.cargame.domain.juego.values.JuegoId;
import co.com.sofka.cargame.usecase.model.InformacionJuego;

import java.util.List;

public interface InformacionJuegoService {

    List<InformacionJuego> obtenerInformacionJuego(JuegoId juegoId);
}
