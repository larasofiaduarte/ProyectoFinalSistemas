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

public class ReportManager {

    // --- CHANGE THESE TO MATCH YOUR persistence.xml ---
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/peluqueria";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    // ---------------------------------------------------

    /**
     * Generates and saves a PDF report.
     * @param parent        The parent JFrame/JPanel (for dialog positioning)
     * @param reportName    The jrxml filename e.g. "clientes.jrxml"
     * @param parameters    Any parameters to pass to the report (can be empty)
     * @param defaultFileName  The suggested filename in the save dialog e.g. "ListaClientes.pdf"
     */
    public static void generateReport(
            java.awt.Component parent,
            String reportName,
            Map<String, Object> parameters,
            String defaultFileName) {

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
                    "Archivo de Report no encontrado" + reportPath +
                    "\nAsegurar que se encuentre en src/reports/",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 4. Fill report with data
            if (parameters == null) parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

            // 5. Open Save dialog
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Report");
            fileChooser.setSelectedFile(new File(defaultFileName));
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));

            int userSelection = fileChooser.showSaveDialog(parent);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".pdf")) {
                    filePath += ".pdf";
                }

                // 6. Export to PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);

                JOptionPane.showMessageDialog(parent,
                    "Report guardado correctamente!\n" + filePath,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(parent,
                "MySQL Driver not found: " + e.getMessage(),
                "Driver Error", JOptionPane.ERROR_MESSAGE);

        } catch (JRException e) {
            JOptionPane.showMessageDialog(parent,
                "Error generando report: " + e.getMessage(),
                "Report Error", JOptionPane.ERROR_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(parent,
                "Database connection error: " + e.getMessage(),
                "DB Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }
}