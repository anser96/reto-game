package co.com.sofka.cargame.infra.services;

import co.com.sofka.cargame.domain.carril.values.CarrilId;
import co.com.sofka.cargame.domain.carro.values.CarroId;
import co.com.sofka.cargame.domain.juego.values.JuegoId;
import co.com.sofka.cargame.usecase.model.Puntaje;
import co.com.sofka.cargame.usecase.model.TiempoDesplazamiento;
import co.com.sofka.cargame.usecase.services.CarroDesplazadoService;
import co.com.sofka.cargame.usecase.services.CarroService;
import co.com.sofka.cargame.usecase.services.InformacionJuegoService;
import co.com.sofka.cargame.usecase.services.PuntajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PuntajeQueryService implements PuntajeService {

    private final MongoTemplate mongoTemplate;
    private final InformacionJuegoService idQueryService;
    private final CarroService carroQueryService;
    private final CarroDesplazadoService carroDesplazadoService;

    @Autowired
    public PuntajeQueryService(MongoTemplate mongoTemplate, InformacionJuegoQueryService idQueryService,
                               CarroQueryService carroQueryService, CarroDesplazadoService carroDesplazadoService) {
        this.mongoTemplate = mongoTemplate;
        this.idQueryService = idQueryService;
        this.carroQueryService = carroQueryService;
        this.carroDesplazadoService = carroDesplazadoService;
    }

    @Override
    public List<Puntaje> getPuntaje(JuegoId juegoId) {
        List<Puntaje> scores = new ArrayList<>();
        idQueryService
                .obtenerInformacionJuego(juegoId)
                .forEach(id -> {
                    var carroId = CarroId.of(id.getCarroId());
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
                    var score = new Puntaje(id.getJuegoId(),id.getCarrilId(),id.getCarroId(),nombre,fechaInicio,fechaFin, id.getMetros());
                    scores.add(score);
                });

        return scores;
    }

}
