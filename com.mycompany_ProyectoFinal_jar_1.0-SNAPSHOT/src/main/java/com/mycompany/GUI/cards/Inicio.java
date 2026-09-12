package com.mycompany.GUI.cards;

import com.mycompany.GUI.calendar.CalendarPanel;
import com.mycompany.GUI.Styles;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.components.ImagePanel;
import com.mycompany.GUI.components.MinimalScrollBarUI;
import com.mycompany.GUI.components.TopMenu;
import com.mycompany.proyectofinal.Caja;
import com.mycompany.controladora.Controladora;
import com.mycompany.proyectofinal.Usuario;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.BasicStroke;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Inicio extends JPanel {

    private static final Logger logger = LogManager.getLogger(Inicio.class);

    private Ventana ventana;
    private Usuario currentUser;
    protected String currentName;
    private JScrollPane scrollPane;
    private JLabel calendarLabel;
    private JLabel estadisticasLabel;
    private CalendarPanel calendarPanel;
    private JPanel statsCard;
    private final Controladora control = new Controladora();

    public Inicio(Ventana ventana, Usuario user) {
        this.ventana = ventana;
        this.currentUser = user;

        if (user != null) {
            this.currentName = user.getNombre();
        } else {
            this.currentName = "Invitado";
        }

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        add(new TopMenu(), BorderLayout.NORTH);

        // MAIN CONTENT (vertical layout)
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setOpaque(false);

        // ===== HEADER IMAGE CARD =====
        ImageIcon headerIcon = new ImageIcon(
            getClass().getResource("/images/7020304.jpg")
        );

        ImagePanel headerCard = new ImagePanel(headerIcon.getImage()) {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                super.paint(g2);
                g2.dispose();
            }
        };
        headerCard.setZoomFactor(1.0);
        headerCard.setLayout(new BorderLayout());

        headerCard.setPreferredSize(new Dimension(0, 110));
        headerCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        headerCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel overlay = new JPanel(new BorderLayout());
        overlay.setOpaque(false); // must be false for alpha compositing to work over the image

        JLabel welcomeLabel = new JLabel("Hola, " + currentName);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        overlay.add(welcomeLabel, BorderLayout.WEST);

        headerCard.add(overlay, BorderLayout.CENTER);

        content.add(headerCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // ===== CALENDAR TITLE =====
        calendarLabel = new JLabel("Mis Turnos");
        calendarLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        calendarLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));

        content.add(calendarLabel);

        // ===== CALENDAR CARD =====
        JPanel calendarCard = createCard(550);
        calendarCard.setLayout(new BorderLayout());

        calendarPanel = new CalendarPanel();
        calendarCard.add(calendarPanel, BorderLayout.CENTER);

        content.add(calendarCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // ===== STATS TITLE =====
        estadisticasLabel = new JLabel("Estadísticas");
        estadisticasLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        estadisticasLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));

        content.add(estadisticasLabel);

        // ===== STATS CARD =====
        statsCard = createCard(750);
        statsCard.setLayout(new BorderLayout());

        content.add(statsCard);

        initStatsChart();

        scrollPane = new JScrollPane(content);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUI(new MinimalScrollBarUI());
        verticalBar.setPreferredSize(new Dimension(10, 0));
        verticalBar.setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void initStatsChart() {
        statsCard.removeAll();
        statsCard.setLayout(new GridLayout(3, 1));

        statsCard.add(createBarChartEmpleados());
        statsCard.add(createLineChartIngresosGastos());
        statsCard.add(createPieChartServicios());

        statsCard.revalidate();
        statsCard.repaint();
    }

    // ── shared palette (aligned with app Styles) ───────────────────────────
    private static final Color C1 = new Color(127,  52, 201); // accent / violet
    private static final Color C2 = new Color(175, 119, 230); // accentHover / lilac
    private static final Color C3 = new Color(243, 112, 136); // accentNotif / pink
    private static final Color C4 = new Color(135, 131, 209); // soft purple
    private static final Color C5 = new Color(200, 171, 255); // light lilac
    private static final Color[] PIE_PALETTE = {C1, C2, C3, C4, C5};

    private static final Font CHART_TITLE = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font CHART_LABEL = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Color GRID_LINE   = new Color(128, 128, 128, 55);

    /** Applies global chart chrome: background, title font/color, legend. */
    private void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(new Color(0, 0, 0, 0));
        if (chart.getTitle() != null) {
            chart.getTitle().setFont(CHART_TITLE);
            chart.getTitle().setPaint(Styles.fontDark);
        }
        if (chart.getLegend() != null) {
            chart.getLegend().setFrame(BlockBorder.NONE);
            chart.getLegend().setBackgroundPaint(new Color(0, 0, 0, 0));
            chart.getLegend().setItemFont(CHART_LABEL);
            chart.getLegend().setItemPaint(Styles.fontDark);
        }
    }

    /** Wraps chart in a transparent, padded ChartPanel. */
    private ChartPanel styledPanel(JFreeChart chart) {
        ChartPanel panel = new ChartPanel(chart);
        panel.setOpaque(false);
        panel.setBackground(new Color(0, 0, 0, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return panel;
    }

    private ChartPanel createBarChartEmpleados() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            LocalDateTime corte = LocalDateTime.now().minusYears(1);
            Map<String, Long> cuentas = control.traerTurnos().stream()
                .filter(t -> t.getFecha() != null && t.getFecha().isAfter(corte))
                .filter(t -> t.getEmpleado() != null)
                .collect(Collectors.groupingBy(
                    t -> t.getEmpleado().getNombre() + " " + t.getEmpleado().getApellido(),
                    Collectors.counting()
                ));
            cuentas.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEach(e -> dataset.addValue(e.getValue(), "Turnos", e.getKey()));
        } catch (Exception ex) {
            logger.warn("[Chart] Turnos por empleado: {}", ex.getMessage());
        }

        JFreeChart chart = ChartFactory.createBarChart(
            "Turnos por empleado (último año)", "Empleado", "Cantidad de turnos", dataset
        );
        styleChart(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Styles.bgLight);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(GRID_LINE);
        plot.setDomainGridlinesVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, C1);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        renderer.setDefaultToolTipGenerator(null);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setAxisLineVisible(false);
        domainAxis.setTickLabelFont(CHART_LABEL);
        domainAxis.setTickLabelPaint(Styles.fontDark);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAxisLineVisible(false);
        rangeAxis.setTickLabelFont(CHART_LABEL);
        rangeAxis.setTickLabelPaint(Styles.fontDark);

        return styledPanel(chart);
    }

    private ChartPanel createLineChartIngresosGastos() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            YearMonth mesActual = YearMonth.now();
            YearMonth mesInicio = mesActual.minusMonths(11);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM yy", new Locale("es", "AR"));

            // Inicializar los 12 meses con 0 para garantizar orden cronológico
            Map<YearMonth, Double> ingresos = new TreeMap<>();
            Map<YearMonth, Double> gastos   = new TreeMap<>();
            for (int i = 0; i < 12; i++) {
                YearMonth mes = mesInicio.plusMonths(i);
                ingresos.put(mes, 0.0);
                gastos.put(mes, 0.0);
            }

            // Fetch único — agrupar en memoria por mes
            for (Caja c : control.traerConceptos()) {
                if (c.getFecha() == null || c.getMonto() == 0) continue;
                YearMonth mes = YearMonth.from(c.getFecha());
                if (mes.isBefore(mesInicio) || mes.isAfter(mesActual)) continue;
                String tipo = c.getTipo() != null ? c.getTipo().toLowerCase() : "";
                if (tipo.contains("ingreso")) {
                    ingresos.merge(mes, c.getMonto(), Double::sum);
                } else if (tipo.contains("egreso") || tipo.contains("gasto")) {
                    gastos.merge(mes, c.getMonto(), Double::sum);
                }
            }

            // Poblar dataset en orden cronológico (TreeMap garantiza orden)
            for (YearMonth mes : ingresos.keySet()) {
                String label = mes.format(fmt);
                dataset.addValue(ingresos.get(mes), "Ingresos", label);
                dataset.addValue(gastos.get(mes),   "Gastos",   label);
            }
        } catch (Exception ex) {
            logger.warn("[Chart] Ingresos/Gastos: {}", ex.getMessage());
        }

        JFreeChart chart = ChartFactory.createLineChart(
            "Ingresos vs Gastos (últimos 12 meses)", "Mes", "Monto ($)", dataset
        );
        styleChart(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Styles.bgLight);
        plot.setOutlineVisible(false);
        plot.setRangeGridlinePaint(GRID_LINE);
        plot.setDomainGridlinesVisible(false);

        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, C1);
        renderer.setSeriesPaint(1, C3);
        renderer.setSeriesStroke(0, new BasicStroke(2.5f));
        renderer.setSeriesStroke(1, new BasicStroke(2.5f));
        renderer.setDefaultShapesVisible(false);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setAxisLineVisible(false);
        domainAxis.setTickLabelFont(CHART_LABEL);
        domainAxis.setTickLabelPaint(Styles.fontDark);
        domainAxis.setCategoryLabelPositions(
            org.jfree.chart.axis.CategoryLabelPositions.UP_45
        );

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAxisLineVisible(false);
        rangeAxis.setTickLabelFont(CHART_LABEL);
        rangeAxis.setTickLabelPaint(Styles.fontDark);

        return styledPanel(chart);
    }

    private ChartPanel createPieChartServicios() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        try {
            control.traerTurnos().stream()
                .filter(t -> t.getServicio() != null && t.getServicio().getNombre() != null)
                .collect(Collectors.groupingBy(
                    t -> t.getServicio().getNombre(),
                    Collectors.counting()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> dataset.setValue(e.getKey(), e.getValue()));
        } catch (Exception ex) {
            logger.warn("[Chart] Servicios más pedidos: {}", ex.getMessage());
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Servicios más pedidos", dataset, true, true, false
        );
        styleChart(chart);

        @SuppressWarnings("unchecked")
        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setBackgroundPaint(Styles.bgLight);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null);
        plot.setSimpleLabels(true);
        plot.setLabelFont(CHART_LABEL);
        plot.setLabelPaint(Styles.fontDark);
        plot.setLabelBackgroundPaint(null);
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);

        int i = 0;
        for (String key : dataset.getKeys()) {
            plot.setSectionPaint(key, PIE_PALETTE[i % PIE_PALETTE.length]);
            i++;
        }

        return styledPanel(chart);
    }

    private JPanel createCard(int height) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(Styles.bgLight);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        card.setOpaque(false);
        card.setPreferredSize(new Dimension(0, height));
        card.setMinimumSize(new Dimension(0, height));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        return card;
    }

    public void applyTheme() {
        setBackground(Styles.bgLight);
        scrollPane.setBackground(Styles.bgLight);
        scrollPane.getViewport().setBackground(Styles.bgLight);
        Component view = scrollPane.getViewport().getView();
        if (view != null) {
            view.setBackground(Styles.bgLight);
            view.setForeground(Styles.fontDark);
        }
        calendarLabel.setForeground(Styles.fontDark);
        estadisticasLabel.setForeground(Styles.fontDark);
        calendarPanel.applyTheme(Styles.bgLight.getRed() < 128);
        initStatsChart(); // recrea los charts con los colores del tema actual
    }

    public void refreshStats() {
        initStatsChart();
    }

    public void refreshCalendar() {
        calendarPanel.refrescarTurnos();
    }
}