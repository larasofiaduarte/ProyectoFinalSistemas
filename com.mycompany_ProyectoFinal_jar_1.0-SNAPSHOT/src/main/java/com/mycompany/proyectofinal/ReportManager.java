/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectofinal; // change to match your package

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.*;
import net.sf.jasperreports.export.*;

public class ReportManager {

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/peluqueria";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    public static void generateReport(
        java.awt.Component parent,
        String reportName,
        Map<String, Object> parameters,
        String defaultFileName,
        String format) {

        Connection conn = null;

        try {
            // 1. Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Open JDBC connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // 3. Load and compile the report
            String reportPath = "/reports/" + reportName;
            InputStream reportStream = ReportManager.class.getResourceAsStream(reportPath);
            if (reportStream == null) {
                JOptionPane.showMessageDialog(parent,
                    "Archivo de Report no encontrado: " + reportPath +
                    "\nAsegurar que se encuentre en src/reports/",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 4. Fill report with data
            if (parameters == null) parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

            // 5. File chooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Reporte");
            fileChooser.setSelectedFile(new File(defaultFileName + "." + format.toLowerCase()));
            fileChooser.setFileFilter(new FileNameExtensionFilter(format, format.toLowerCase()));

            int userSelection = fileChooser.showSaveDialog(parent);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();

                // 6. Exportar según formato
                switch (format.toUpperCase()) {
                    case "PDF" -> {
                        if (!filePath.endsWith(".pdf")) filePath += ".pdf";
                        JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
                    }
                    case "XLSX" -> {
                        if (!filePath.endsWith(".xlsx")) filePath += ".xlsx";
                        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
                        config.setOnePagePerSheet(false);
                        config.setDetectCellType(true);
                        JRXlsxExporter exporter = new JRXlsxExporter();
                        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath));
                        exporter.setConfiguration(config);
                        exporter.exportReport();
                    }
                    case "DOCX" -> {
                        if (!filePath.endsWith(".docx")) filePath += ".docx";
                        JRDocxExporter exporter = new JRDocxExporter();
                        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath));
                        exporter.exportReport();
                    }
                    case "CSV" -> {
                        if (!filePath.endsWith(".csv")) filePath += ".csv";
                        JRCsvExporter exporter = new JRCsvExporter();
                        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                        exporter.setExporterOutput(new SimpleWriterExporterOutput(filePath));
                        exporter.exportReport();
                    }
                    default -> {
                        JOptionPane.showMessageDialog(parent,
                            "Formato no soportado: " + format,
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                JOptionPane.showMessageDialog(parent,
                    "Reporte guardado!\n" + filePath,
                    "OK", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(parent,
                "MySQL Driver no encontrado: " + e.getMessage(),
                "Driver Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent,
                "Error de conexión a la base de datos: " + e.getMessage(),
                "DB Error", JOptionPane.ERROR_MESSAGE);
        } catch (JRException e) {
            JOptionPane.showMessageDialog(parent,
                "Error generando reporte: " + e.getMessage(),
                "Report Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
}

