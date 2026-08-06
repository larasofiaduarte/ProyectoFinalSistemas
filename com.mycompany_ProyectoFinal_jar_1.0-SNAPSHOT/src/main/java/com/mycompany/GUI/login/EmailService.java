package com.mycompany.GUI.login;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailService {

    private static final Logger logger = LogManager.getLogger(EmailService.class);

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    private static final String USERNAME;
    private static final String PASSWORD;

    static {
        // Credenciales por variable de entorno; nunca se hardcodean en el código 
        //PASSWORD debe ser una contraseña de aplicación de Gmail
        USERNAME = System.getenv("EMAIL_USERNAME");
        PASSWORD = System.getenv("EMAIL_PASSWORD");
        if (USERNAME == null || USERNAME.isEmpty() || PASSWORD == null || PASSWORD.isEmpty()) {
            throw new RuntimeException("Las variables de entorno EMAIL_USERNAME y EMAIL_PASSWORD no están configuradas");
        }
    }

    public static boolean sendRecoveryToken(String toEmail, String token) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true"); //activa tls transport layer security, encriptacion

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });
            
            //mensaje del email
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Código de recuperación");
            message.setContent(
                "<p>Tu código de recuperación es: <strong>" + token + "</strong></p>",
                "text/html; charset=UTF-8"
            );

            Transport.send(message);
            logger.info("Email enviado correctamente a {}", toEmail);
            return true;

        } catch (Exception e) {
            logger.error("Error al enviar email", e);
            return false;
        }
    }
}
