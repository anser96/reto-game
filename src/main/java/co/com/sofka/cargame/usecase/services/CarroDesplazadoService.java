package co.com.sofka.cargame.usecase.services;

import co.com.sofka.cargame.domain.carril.values.CarrilId;

import java.util.List;

public interface CarroDesplazadoService {
    List<TiempoDesplazamiento> obtenerTiempoDesplazamiento(CarrilId carrilId);
}
