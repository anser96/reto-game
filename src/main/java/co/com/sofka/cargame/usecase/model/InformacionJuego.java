package co.com.sofka.cargame.usecase.model;

public class InformacionJuego {

    private String juegoId;
    private String carroId;
    private String carrilId;
    private String metros;


    public String getJuegoId() {
        return juegoId;
    }

    public String getCarroId() {
        return carroId;
    }

    public String getCarrilId() {
        return carrilId;
    }

    public String getMetros() {
        return metros;
    }

    public InformacionJuego() {
    }

    public void setJuegoId(String juegoId) {
        this.juegoId = juegoId;
    }

    public void setCarroId(String carroId) {
        this.carroId = carroId;
    }

    public void setCarrilId(String carrilId) {
        this.carrilId = carrilId;
    }

    public void setMetros(String metros) {
        this.metros = metros;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DatosJuego{");
        sb.append("juegoId='").append(juegoId).append('\'');
        sb.append(", carroId='").append(carroId).append('\'');
        sb.append(", carrilId='").append(carrilId).append('\'');
        sb.append(", metros='").append(metros).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public InformacionJuego(String juegoId, String carroId, String carrilId, String metros) {
        this.juegoId = juegoId;
        this.carroId = carroId;
        this.carrilId = carrilId;
        this.metros = metros;
    }
}
