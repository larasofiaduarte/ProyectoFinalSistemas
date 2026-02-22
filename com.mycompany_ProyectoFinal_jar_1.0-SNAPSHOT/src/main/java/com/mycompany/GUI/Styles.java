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
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Styles {
    //COLORS
    public static Color bgDark;  //Petrol grey, sidebar
    public static Color bgDarkHover;
    public static Color bgLight; //Off White
    public static Color bgDisabledField;
    public static Color bgTextField;
    public static Color bgSearch;
    
    //font
    public static Color fontLight;  //for dark bg, sidebar, button
    public static Color fontLightHover; //lila
    public static Color fontDisabledField; //for disabled fields?
    public static Color fontPlaceholder; //for textfield placeholder text
    public static Color fontDark; //Petrol grey
    public static Color fontDarkGrey;
    
    //accent colors
    public static Color accent; //Dark Violet
    public static Color accentHover; //Lilac
    public static Color accentNotif; //Pink
    
    //button colors
    public static Color btnSec; //Pure White
    public static Color btnSecBorder; //Dark grey
    public static Color btnSecFontCol;
    public static Color btnSecHover;
    public static Color btnSecBorHov;
    
    public static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    
    //background
    public static void applyLightTheme(){
    
        bgDark = new Color(43, 43, 45);  //Petrol grey, sidebar
        bgDarkHover = new Color(76, 76, 79);
        bgLight = new Color(250, 250, 250); //Off White
        bgDisabledField = new Color(189, 189, 189);
        bgTextField = new Color(225,225,225);
        bgSearch = new Color(245,245,245);

        //font
        fontLight = new Color(252, 252, 252);  //for dark bg, sidebar, button
        fontLightHover = new Color(200, 171, 255); //lilac
        fontDisabledField = new Color(148, 148, 148); //for disabled fields?
        fontPlaceholder = new Color(150, 150, 150); //for textfield placeholder text
        fontDark = new Color(43, 43, 45);
        fontDarkGrey = new Color(50, 50, 50);//Petrol grey

        //accent colors
        accent = new Color(127, 52, 201); //Dark Violet
        accentHover = new Color(175, 119, 230); //Lilac
        accentNotif = new Color(243, 112, 136); //Pink

        //button colors
        btnSec = new Color(255, 255, 255); //Pure White
        btnSecBorder = new Color(148, 148, 148); //Dark grey
        btnSecFontCol = new Color(148,148,148);
        btnSecHover = new Color(246, 240, 252);
        btnSecBorHov = new Color (245,245,245);
        
        UIManager.put("Panel.background", bgLight);
        UIManager.put("Label.foreground", fontDark);
        UIManager.put("Button.background", btnSec);
        UIManager.put("Button.foreground", fontDark);
        UIManager.put("ToggleButton.background", btnSec);
        UIManager.put("ToggleButton.foreground", fontDark);
        UIManager.put("TextField.background", bgTextField);
        UIManager.put("TextField.foreground", fontDark);
        UIManager.put("OptionPane.background", bgLight);
        UIManager.put("OptionPane.messageForeground", fontDark);
        UIManager.put("Button.background", bgLight);
        UIManager.put("Button.foreground", fontDark);
    
    }
    
    public static void applyDarkTheme(){
    
        bgDark = new Color(43, 43, 45);  //Petrol grey, sidebar
        bgDarkHover = new Color(76, 76, 79);
        bgLight = new Color(43, 43, 45); //Off White
        bgDisabledField = new Color(189, 189, 189);
        bgTextField = new Color(225,225,225);
        bgSearch = new Color(245,245,245);

        //font
        fontLight = new Color(252, 252, 252);  //for dark bg, sidebar, button
        fontLightHover = new Color(200, 171, 255); //lilac
        fontDisabledField = new Color(148, 148, 148); //for disabled fields?
        fontPlaceholder = new Color(150, 150, 150); //for textfield placeholder text
        fontDark = new Color(252, 252, 252); //Petrol grey

        //accent colors
        accent = new Color(127, 52, 201); //Dark Violet
        accentHover = new Color(175, 119, 230); //Lilac
        accentNotif = new Color(243, 112, 136); //Pink

        //button colors
        btnSec = new Color(255, 255, 255); //Pure White
        btnSecBorder = new Color(148, 148, 148); //Dark grey
        btnSecFontCol = new Color(148,148,148);
        btnSecHover = new Color(246, 240, 252);
        btnSecBorHov = new Color (245,245,245);
        
        UIManager.put("Panel.background", bgLight);
        UIManager.put("Label.foreground", fontDark);
        UIManager.put("Button.background", btnSec);
        UIManager.put("Button.foreground", fontDark);
        UIManager.put("ToggleButton.background", btnSec);
        UIManager.put("ToggleButton.foreground", fontDark);
        UIManager.put("TextField.background", bgTextField);
        UIManager.put("TextField.foreground", fontDark);
        UIManager.put("OptionPane.background", bgDark);
        UIManager.put("OptionPane.messageForeground", fontLight);
        UIManager.put("Button.background", bgLight);
        UIManager.put("Button.foreground", fontDark);
    }
    
    
    
    //btn sizes
    public static final Dimension btnSizeSm = new Dimension(150,35);
    public static final Dimension btnSizeWide = new Dimension(250,35);
    
    //padding  (top, left, bottom, right)
    public static final EmptyBorder padding = new EmptyBorder(50, 20, 0, 0);
    public static final EmptyBorder btnPanelPadding = new EmptyBorder(30, 0, 30, 0);
    public static final EmptyBorder searchPadding = new EmptyBorder(10, 0, 10, 0);

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
