/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.gymcontrol.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Asistencia {
    private int id;
    private int socioId;
    private LocalDate fecha;
    private LocalTime hora;
    private LocalDateTime registradoEn;

    public Asistencia() {
    }

    public Asistencia(int id,
                      int socioId,
                      LocalDate fecha,
                      LocalTime hora,
                      LocalDateTime registradoEn) {

        this.id = id;
        this.socioId = socioId;
        this.fecha = fecha;
        this.hora = hora;
        this.registradoEn = registradoEn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getSocioId() {
        return socioId;
    }

    public void setSocioId(int socioId) {
        this.socioId = socioId;
    }


    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }


    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }


    public LocalDateTime getRegistradoEn() {
        return registradoEn;
    }

    public void setRegistradoEn(LocalDateTime registradoEn) {
        this.registradoEn = registradoEn;
    }

}
