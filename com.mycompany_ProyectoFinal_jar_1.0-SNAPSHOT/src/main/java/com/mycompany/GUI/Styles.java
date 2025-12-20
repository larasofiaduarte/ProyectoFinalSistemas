/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Styles {
    //background
    public static final Color bgDark = new Color(43, 43, 45);  //Petrol grey, sidebar
    public static final Color bgDarkHover = new Color(76, 76, 79);
    public static final Color bgLight = new Color(250, 250, 250); //Off White
    public static final Color bgDisabledField = new Color(189, 189, 189);
    public static final Color bgTextField = new Color(225,225,225);
    
    //font
    public static final Color fontLight = new Color(252, 252, 252);  //for dark bg, sidebar, button
    public static final Color fontLightHover = new Color(200, 171, 255); //lilac
    public static final Color fontDisabledField = new Color(148, 148, 148); //for disabled fields?
    public static final Color fontPlaceholder = new Color(150, 150, 150); //for textfield placeholder text
    public static final Color fontDark = new Color(43, 43, 45); //Petrol grey
    
    //accent colors
    public static final Color accent = new Color(127, 52, 201); //Dark Violet
    public static final Color accentHover = new Color(175, 119, 230); //Lilac
    public static final Color accentNotif = new Color(243, 112, 136); //Pink
    
    //button colors
    public static final Color btnSec = new Color(255, 255, 255); //Pure White
    public static final Color btnSecBorder = new Color(148, 148, 148); //Dark grey
    public static final Color btnSecFontCol = new Color(148,148,148);
    public static final Color btnSecHover = new Color(246, 240, 252);
    public static final Color btnSecBorHov = new Color (245,245,245);
    
    //btn sizes
    public static final Dimension btnSizeSm = new Dimension(150,35);
    public static final Dimension btnSizeWide = new Dimension(250,35);
    
    //padding
    public static final EmptyBorder padding = new EmptyBorder(50, 20, 0, 0);


    //font
    public static Font customFontMd;
    public static Font customFontSm;
    public static Font customFontXl;
    public static Font customFontL;
    
    public static Font fontTitle = new Font("Segoe UI", Font.BOLD, 20);
    public static Font fontTitleIt = new Font("Segoe UI", Font.BOLD | Font.ITALIC, 20);
    public static Font fontBtn = new Font("Segoe UI", Font.BOLD, 14);
    public static Font fontTextField = new Font("Segoe UI", Font.PLAIN, 12);
    public static Font fontLbl = new Font("Segoe UI", Font.PLAIN, 12);
    public static Font fontLblBold = new Font("Segoe UI", Font.BOLD, 12);
    
    static {
        try {
            File fontFile = new File("src/main/resources/fonts/AestheticRegular-8M5dM.ttf");
            customFontMd = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(18f);
            customFontSm = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(8f);
            customFontXl = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(58f);
            customFontL = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(40f);
            
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
}
