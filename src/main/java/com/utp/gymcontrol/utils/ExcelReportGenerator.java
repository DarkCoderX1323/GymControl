package com.utp.gymcontrol.utils;

import com.utp.gymcontrol.dao.SocioDAO;
import com.utp.gymcontrol.model.Socio;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;

public class ExcelReportGenerator {

    public static boolean generarReporteSocios() {

        try {

            SocioDAO socioDAO = new SocioDAO();

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
}
