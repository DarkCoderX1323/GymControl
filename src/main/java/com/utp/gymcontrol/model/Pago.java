/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.gymcontrol.model;

import java.time.LocalDateTime;

public class Pago {
    private int id;
    private int socioId;
    private double monto;
    private String metodoPago;
    private LocalDateTime fechaPago;
    private String descripcion;
    private int membresiaId;

    // Campos de solo lectura, poblados por consultas con JOIN
    // (obtenerPagos/filtrarPagos/filtrarPagosPorNombre) para mostrar
    // nombre de socio y tipo de membresía sin tener que hacer consultas
    // adicionales desde la vista.
    private String nombreSocio;
    private String dniSocio;
    private String tipoMembresia;

    public Pago() {
    }

    public Pago(int id, int socioId,
                double monto,
                String metodoPago,
                LocalDateTime fechaPago,
                String descripcion) {

        this.id = id;
        this.socioId = socioId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fechaPago = fechaPago;
        this.descripcion = descripcion;
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


    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }


    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }


    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public int getMembresiaId() {
        return membresiaId;
    }

    public void setMembresiaId(int membresiaId) {
        this.membresiaId = membresiaId;
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

    public String getTipoMembresia() {
        return tipoMembresia;
    }

    public void setTipoMembresia(String tipoMembresia) {
        this.tipoMembresia = tipoMembresia;
    }
}
