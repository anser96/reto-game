package co.com.sofka.cargame.infra.services;

import co.com.sofka.cargame.domain.carril.values.CarrilId;
import co.com.sofka.cargame.domain.carro.values.CarroId;
import co.com.sofka.cargame.domain.juego.values.JuegoId;
import co.com.sofka.cargame.usecase.model.Puntaje;
import co.com.sofka.cargame.usecase.model.TiempoDesplazamiento;
import co.com.sofka.cargame.usecase.services.CarroDesplazadoService;
import co.com.sofka.cargame.usecase.services.CarroService;
import co.com.sofka.cargame.usecase.services.PuntajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PuntajeQueryServce implements PuntajeService {
    private final MongoTemplate mongoTemplate;
    private final InformacionJuegoQueryService idQueryService;
    private final CarroService carroQueryService;
    private final CarroDesplazadoService carroDesplazadoService;

    @Autowired
    public PuntajeQueryServce(MongoTemplate mongoTemplate, InformacionJuegoQueryService idQueryService, CarroQueryService carroQueryService, CarroDesplazadoService carroDesplazadoService) {
        this.mongoTemplate = mongoTemplate;
        this.idQueryService = idQueryService;
        this.carroQueryService = carroQueryService;
        this.carroDesplazadoService = carroDesplazadoService;
    }

    @Override
    public List<Puntaje> getPuntajeGame() {
        List<Puntaje> puntajes = new ArrayList<>();
        idQueryService
                .obtenerInformacionJuego()
                .forEach(id -> {
                    var carroId = CarroId.of(id.getCarroId());
                    var juegoId = JuegoId.of(id.getJuegoId());
                    var carrilId = CarrilId.of(id.getCarrilId());
                    var fechaInicio = carroDesplazadoService
                            .obtenerTiempoDesplazamiento(carrilId)
                            .stream()
                            .findFirst()
                            .get()
                            .getWhen();
                    var fechaFin = carroDesplazadoService
                            .obtenerTiempoDesplazamiento(carrilId)
                            .stream()
                            .sorted(Comparator.comparing(TiempoDesplazamiento::getWhen).reversed())
                            .findFirst()
                            .get()
                            .getWhen();

                    var nombre = carroQueryService.getNombreConductorID(carroId);
                    var puntaje = new Puntaje(id.getJuegoId(),id.getCarrilId(),id.getCarroId(),nombre,fechaInicio,fechaFin, id.getMetros());
                    puntajes.add(puntaje);
                });

        return puntajes;
    }
}
