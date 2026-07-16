package com.utp.gymcontrol.utils;

import com.utp.gymcontrol.dao.MembresiaDAO;
import com.utp.gymcontrol.dao.PagoDAO;
import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.model.Membresia;
import com.utp.gymcontrol.model.Pago;
import com.utp.gymcontrol.model.Socio;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class ExcelReportGenerator {

    public static boolean generarReporteSocios() {

        try {

            SocioDAO socioDAO =
                    new SocioDAO();

            List<Socio> socios =
                    socioDAO.obtenerSocios();

            Workbook workbook =
                    new XSSFWorkbook();

            Sheet sheet =
                    workbook.createSheet(
                            "Socios"
                    );

            // =========================
            // ENCABEZADOS
            // =========================

            Row header =
                    sheet.createRow(0);

            header.createCell(0)
                    .setCellValue("ID");

            header.createCell(1)
                    .setCellValue("Nombre");

            header.createCell(2)
                    .setCellValue("DNI");

            header.createCell(3)
                    .setCellValue("Telefono");

            header.createCell(4)
                    .setCellValue("Email");

            header.createCell(5)
                    .setCellValue("Estado");

            // =========================
            // DATOS
            // =========================

            int fila = 1;

            for (Socio socio : socios) {

                Row row =
                        sheet.createRow(
                                fila++
                        );

                row.createCell(0)
                        .setCellValue(
                                socio.getId()
                        );

                row.createCell(1)
                        .setCellValue(
                                socio.getNombre()
                        );

                row.createCell(2)
                        .setCellValue(
                                socio.getDni()
                        );

                row.createCell(3)
                        .setCellValue(
                                socio.getTelefono()
                        );

                row.createCell(4)
                        .setCellValue(
                                socio.getEmail()
                        );

                row.createCell(5)
                        .setCellValue(
                                socio.getEstado()
                        );
            }

            // =========================
            // AJUSTAR COLUMNAS
            // =========================

            for (int i = 0; i < 6; i++) {

                sheet.autoSizeColumn(i);
            }

            // =========================
            // GUARDAR ARCHIVO
            // =========================

            FileOutputStream archivo =
                    new FileOutputStream(
                            "ReporteSocios.xlsx"
                    );

            workbook.write(archivo);

            archivo.close();

            workbook.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // REPORTE DE MEMBRESIAS
    // =========================

    public static boolean generarReporteMembresias() {

        try {

            MembresiaDAO membresiaDAO =
                    new MembresiaDAO();

            List<Membresia> membresias =
                    membresiaDAO.obtenerMembresias();

            Workbook workbook =
                    new XSSFWorkbook();

            Sheet sheet =
                    workbook.createSheet(
                            "Membresias"
                    );

            Row header =
                    sheet.createRow(0);

            header.createCell(0)
                    .setCellValue("ID");

            header.createCell(1)
                    .setCellValue("Socio");

            header.createCell(2)
                    .setCellValue("Tipo");

            header.createCell(3)
                    .setCellValue("Fecha inicio");

            header.createCell(4)
                    .setCellValue("Fecha fin");

            header.createCell(5)
                    .setCellValue("Estado");

            int fila = 1;

            for (Membresia membresia : membresias) {

                Row row =
                        sheet.createRow(
                                fila++
                        );

                row.createCell(0)
                        .setCellValue(
                                membresia.getId()
                        );

                String socioMostrado =
                        membresia.getNombreSocio() != null
                                ? membresia.getNombreSocio()
                                + (membresia.getDniSocio() != null
                                ? " - " + membresia.getDniSocio()
                                : "")
                                : "socio #" + membresia.getSocioId();

                row.createCell(1)
                        .setCellValue(
                                socioMostrado
                        );

                row.createCell(2)
                        .setCellValue(
                                membresia.getTipo()
                        );

                row.createCell(3)
                        .setCellValue(
                                membresia.getFechaInicio() != null
                                        ? membresia.getFechaInicio().toString()
                                        : ""
                        );

                row.createCell(4)
                        .setCellValue(
                                membresia.getFechaFin() != null
                                        ? membresia.getFechaFin().toString()
                                        : ""
                        );

                row.createCell(5)
                        .setCellValue(
                                membresia.getEstado()
                        );
            }

            for (int i = 0; i < 6; i++) {

                sheet.autoSizeColumn(i);
            }

            FileOutputStream archivo =
                    new FileOutputStream(
                            "ReporteMembresias.xlsx"
                    );

            workbook.write(archivo);

            archivo.close();

            workbook.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // REPORTE DE PAGOS (CON RANGO DE FECHAS)
    // =========================

    /**
     * Genera el reporte de pagos filtrando por fecha de pago (inclusive
     * en ambos extremos). El filtrado se hace en memoria sobre el
     * resultado de PagoDAO.obtenerPagos(), sin modificar el DAO.
     */
    public static boolean generarReportePagos(
            LocalDate desde,
            LocalDate hasta
    ) {

        try {

            PagoDAO pagoDAO =
                    new PagoDAO();

            List<Pago> pagos =
                    pagoDAO.obtenerPagos();

            Workbook workbook =
                    new XSSFWorkbook();

            Sheet sheet =
                    workbook.createSheet(
                            "Pagos"
                    );

            Row header =
                    sheet.createRow(0);

            header.createCell(0)
                    .setCellValue("ID");

            header.createCell(1)
                    .setCellValue("Socio");

            header.createCell(2)
                    .setCellValue("Monto");

            header.createCell(3)
                    .setCellValue("Metodo");

            header.createCell(4)
                    .setCellValue("Fecha");

            header.createCell(5)
                    .setCellValue("Descripcion");

            header.createCell(6)
                    .setCellValue("ID Membresia");

            int fila = 1;

            for (Pago pago : pagos) {

                if (pago.getFechaPago() == null) {
                    continue;
                }

                LocalDate fechaPago =
                        pago.getFechaPago().toLocalDate();

                boolean dentroDeRango =
                        !fechaPago.isBefore(desde)
                                && !fechaPago.isAfter(hasta);

                if (!dentroDeRango) {
                    continue;
                }

                Row row =
                        sheet.createRow(
                                fila++
                        );

                row.createCell(0)
                        .setCellValue(
                                pago.getId()
                        );

                String socioMostrado =
                        pago.getNombreSocio() != null
                                ? pago.getNombreSocio()
                                + (pago.getDniSocio() != null
                                ? " - " + pago.getDniSocio()
                                : "")
                                : "socio #" + pago.getSocioId();

                row.createCell(1)
                        .setCellValue(
                                socioMostrado
                        );

                row.createCell(2)
                        .setCellValue(
                                pago.getMonto()
                        );

                row.createCell(3)
                        .setCellValue(
                                pago.getMetodoPago()
                        );

                row.createCell(4)
                        .setCellValue(
                                pago.getFechaPago().toString()
                        );

                // Muestra el tipo de membresía elegido al registrar el
                // pago (por ejemplo "Membresía Mensual") en vez de
                // depender de la descripción libre guardada; si el pago
                // quedó sin membresía asociada, cae de vuelta al texto
                // original.
                String descripcionMostrada =
                        pago.getTipoMembresia() != null
                                && !pago.getTipoMembresia().isBlank()
                                ? "Membresía " + pago.getTipoMembresia()
                                : (pago.getDescripcion() != null
                                ? pago.getDescripcion()
                                : "");

                row.createCell(5)
                        .setCellValue(
                                descripcionMostrada
                        );

                row.createCell(6)
                        .setCellValue(
                                pago.getMembresiaId()
                        );
            }

            for (int i = 0; i < 7; i++) {

                sheet.autoSizeColumn(i);
            }

            FileOutputStream archivo =
                    new FileOutputStream(
                            "ReportePagos.xlsx"
                    );

            workbook.write(archivo);

            archivo.close();

            workbook.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}
