package com.mycompany.GUI.login;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class EmailService {

    private static final String API_KEY = "re_Ai1ZVJei_FeSPEwhsN55SyWUA2a9pjLjK";
    private static final String FROM    = "onboarding@resend.dev";
    private static final String ENDPOINT = "https://api.resend.com/emails";

    public static boolean sendRecoveryToken(String toEmail, String token) {
        try {
            URL url = new URL(ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String body = "{"
                + "\"from\":\"" + FROM + "\","
                + "\"to\":[\"" + toEmail + "\"],"
                + "\"subject\":\"Código de recuperación\","
                + "\"html\":\"<p>Tu código de recuperación es: <strong>" + token + "</strong></p>\""
                + "}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            int status = conn.getResponseCode();
            if (status == 200 || status == 201) {
                System.out.println("Email enviado correctamente a " + toEmail);
                return true;
            } else {
                System.err.println("Error al enviar email. HTTP status: " + status);
                return false;
            }

        } catch (Exception e) {
            System.err.println("Error al enviar email: " + e.getMessage());
            return false;
        }
    }
}
