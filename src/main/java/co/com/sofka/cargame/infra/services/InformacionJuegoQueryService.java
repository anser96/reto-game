package co.com.sofka.cargame.infra.services;

import co.com.sofka.cargame.domain.juego.values.JuegoId;
import co.com.sofka.cargame.usecase.model.InformacionJuego;
import co.com.sofka.cargame.usecase.services.InformacionJuegoService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class InformacionJuegoQueryService implements InformacionJuegoService {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public InformacionJuegoQueryService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<InformacionJuego> obtenerInformacionJuego(JuegoId juegoId) {
        var lookup = LookupOperation.newLookup()
                .from("carril.CarroAgregadoACarrail")
                .localField("aggregateRootId")
                .foreignField("aggregateRootId")
                .as("carroAgregadoACarrail");

        var aggregation = Aggregation.newAggregation(
                lookup,
                Aggregation.match(where("juegoId.uuid").is(juegoId.value())));

        var tempo = mongoTemplate.aggregate(aggregation, "carril.CarrilCreado", String.class)
                .getMappedResults().stream().collect(Collectors.toList());

        return mongoTemplate.aggregate(aggregation, "carril.CarrilCreado", String.class)
                .getMappedResults().stream()
                .map(body -> new Gson().fromJson(body,IdRecord.class))
                .map(idRecord -> {
                    var informacionJuego = new InformacionJuego();
                    informacionJuego.setCarrilId(idRecord.getAggregateRootId());
                    informacionJuego.setCarroId(idRecord.getCarroAgregadoACarrail().get(0).getCarroId().getUuid());
                    informacionJuego.setJuegoId(idRecord.getJuegoId().getUuid());
                    informacionJuego.setMetros(idRecord.getMetros());
                    return informacionJuego;
                })
                .collect(Collectors.toList());
    }

    public static class IdRecord {
        private String aggregateRootId;
        private String metros;
        private JuegoId juegoId;
        private List<CarrilCarroQueryService.CarroSobreCarrilRecord.CarroAgregadoACarrail> carroAgregadoACarrail;

        public String getMetros() {
            return metros;
        }

        public void setMetros(String metros) {
            this.metros = metros;
        }

        public String getAggregateRootId() {
            return aggregateRootId;
        }

        public void setAggregateRootId(String aggregateRootId) {
            this.aggregateRootId = aggregateRootId;
        }

        public List<CarrilCarroQueryService.CarroSobreCarrilRecord.CarroAgregadoACarrail> getCarroAgregadoACarrail() {
            return carroAgregadoACarrail;
        }

        public void setCarroAgregadoACarrail(List<CarrilCarroQueryService.CarroSobreCarrilRecord.CarroAgregadoACarrail> carroAgregadoACarrail) {
            this.carroAgregadoACarrail = carroAgregadoACarrail;
        }

        public JuegoId getJuegoId() {
            return juegoId;
        }

        public void setJuegoId(JuegoId juegoId) {
            this.juegoId = juegoId;
        }

        public static class CarroAgregadoACarrail {
            private CarroId carroId;

            public CarroId getCarroId() {
                return carroId;
            }

            public void setCarroId(CarroId carroId) {
                this.carroId = carroId;
            }

            public static class CarroId {
                private String uuid;

                public String getUuid() {
                    return uuid;
                }

                public void setUuid(String uuid) {
                    this.uuid = uuid;
                }
            }
        }

        public static class JuegoId{
            private String uuid;

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

        }

    }
}
