/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.gymcontrol.model;

import java.time.LocalDate;

public class Membresia {
    private int id;
    private int socioId;
    private String tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private int tipoMembresiaId;

    // Campos de solo lectura, poblados por MembresiaDAO al hacer JOIN con
    // socio o al calcular días restantes. No son columnas propias de la
    // tabla `membresia`.
    private String nombreSocio;
    private String dniSocio;
    private long diasRestantes;

    public Membresia() {
    }

    public Membresia(int id, int socioId,
                     String tipo,
                     LocalDate fechaInicio,
                     LocalDate fechaFin,
                     String estado) {

        this.id = id;
        this.socioId = socioId;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
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


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }


    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getTipoMembresiaId() {
        return tipoMembresiaId;
    }

    public void setTipoMembresiaId(int tipoMembresiaId) {
        this.tipoMembresiaId = tipoMembresiaId;
    }

    public String getNombreSocio() {
        return nombreSocio;
    }

    public void setNombreSocio(String nombreSocio) {
        this.nombreSocio = nombreSocio;
    }

    public String getDniSocio() {
        return dniSocio;
    }

    public void setDniSocio(String dniSocio) {
        this.dniSocio = dniSocio;
    }

    public long getDiasRestantes() {
        return diasRestantes;
    }

    public void setDiasRestantes(long diasRestantes) {
        this.diasRestantes = diasRestantes;
    }
}
