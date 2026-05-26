package com.mycompany.GUI.cards;

import com.mycompany.GUI.Styles;
import com.mycompany.GUI.Ventana;
import com.mycompany.GUI.components.ImagePanel;
import com.mycompany.GUI.components.MinimalScrollBarUI;
import com.mycompany.GUI.components.TopMenu;
import com.mycompany.proyectofinal.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Inicio extends JPanel {

    private Ventana ventana;
    private Usuario currentUser;
    protected String currentName;
    private JScrollPane scrollPane;
    private JLabel calendarLabel;
    private JLabel estadisticasLabel;

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
        calendarLabel = new JLabel("Calendario");
        calendarLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        calendarLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));

        content.add(calendarLabel);

        // ===== CALENDAR CARD =====
        JPanel calendarCard = createCard(250);
        calendarCard.setLayout(new BorderLayout());

        JLabel calendarPlaceholder = new JLabel("Aquí va el calendario");
        calendarPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);

        calendarCard.add(calendarPlaceholder, BorderLayout.CENTER);

        content.add(calendarCard);
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // ===== STATS TITLE =====
        estadisticasLabel = new JLabel("Estadísticas");
        estadisticasLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        estadisticasLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));

        content.add(estadisticasLabel);

        // ===== STATS CARD =====
        JPanel statsCard = createCard(250);
        statsCard.setLayout(new BorderLayout());

        JLabel statsPlaceholder = new JLabel("Estadísticas");
        statsPlaceholder.setHorizontalAlignment(SwingConstants.CENTER);

        statsCard.add(statsPlaceholder, BorderLayout.CENTER);

        content.add(statsCard);

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

    /**
     * Creates a reusable "card" block (like your grey rounded boxes)
     */
    private JPanel createCard(int height) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(200, 200, 200)); // gris wireframe
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        card.setOpaque(false);
        card.setPreferredSize(new Dimension(0, height));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
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
    }
}