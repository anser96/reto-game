package co.com.sofka.cargame.usecase.model;

import java.util.Date;

public class Puntaje {
    private String juegoId;
    private String carrilId;
    private String carroId;
    private String nombre;
    private Date fechaInicio;
    private Date fechaFin;
    private String distanciaJuego;
    private String tiempoRecorrido;



    public Puntaje() {
    }

    public Puntaje(String juegoId, String carrilId, String carroId, String nombre, Date fechaInicio, Date fechaFin, String distanciaJuego) {
        this.juegoId = juegoId;
        this.carrilId = carrilId;
        this.carroId = carroId;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.distanciaJuego = distanciaJuego;
        this.tiempoRecorrido = calcularTiempoRecorrido();

    }

    public String calcularTiempoRecorrido(){
        long difference_In_Time = fechaFin.getTime() - fechaInicio.getTime();
        long difference_In_Seconds = (difference_In_Time / 1000) % 60;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;
        return convert(difference_In_Hours)+":"+convert(difference_In_Minutes)+":"+convert(difference_In_Seconds);
    }


}
