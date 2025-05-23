package co.edu.javeriana.easymarket.usersservice.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private static final String LOGO_URL = "https://raw.githubusercontent.com/Easy-Market-SPL/.github/refs/heads/main/Banner.png"; // Logo similar al de la imagen

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendNotification(String subject, String to, String bodyContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("[EasyMarketSPL] " + subject);

            String html = buildHtmlTemplate(subject, bodyContent);
            helper.setText(html, true); // true = HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    private String buildHtmlTemplate(String title, String content) {
        return """
            <div style="font-family: Arial, sans-serif; color: #333;">
                <div style="text-align: center; padding: 20px 0;">
                    <img src="%s" alt="Logo" style="width: 200px;">
                </div>
                <div style="padding: 20px; background-color: #f9f9f9; border-radius: 8px; max-width: 600px; margin: auto;">
                    <h2 style="color: #222;">%s</h2>
                    <p style="font-size: 16px; line-height: 1.6;">%s</p>
                </div>
                <div style="font-size: 12px; color: #777; padding: 20px; text-align: center;">
                    <hr style="margin: 20px 0;">
                    <p>Este correo fue generado automáticamente por EasyMarket.</p>
                    <p>Si no reconoces esta actividad, por favor reporta el incidente a <a href="mailto:seguridad@easymarket.com">seguridad@easymarket.com</a>.</p>
                    <p>Por favor no respondas a este correo directamente.</p>
                </div>
            </div>
            """.formatted(LOGO_URL, title, content);
    }
}
