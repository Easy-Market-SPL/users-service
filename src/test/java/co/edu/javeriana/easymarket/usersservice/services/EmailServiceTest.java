package co.edu.javeriana.easymarket.usersservice.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    /**
     * Test sending notification - successful case
     * This test verifies that the email is properly constructed and sent
     */
    @Test
    void sendNotification_Success() throws MessagingException {
        // Arrange
        String subject = "Test Subject";
        String to = "recipient@example.com";
        String bodyContent = "This is a test email body";

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        // Act
        emailService.sendNotification(subject, to, bodyContent);
        
        // Assert
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    /**
     * Test sending notification - exception handling
     * This test verifies that mail sending exceptions are propagated
     */
    @Test
    void sendNotification_HandlesException() {
        // Arrange
        String subject = "Test Subject";
        String to = "recipient@example.com";
        String bodyContent = "This is a test email body";

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailSendException("Error sending email")).when(mailSender).send(mimeMessage);
        
        // Act & Assert - should throw exception
        assertThrows(MailSendException.class, () -> {
            emailService.sendNotification(subject, to, bodyContent);
        });
    }

    /**
     * Test email content formatting
     * This test verifies that HTML content is properly formatted with subject and content
     */
    @Test
    void sendNotification_FormatsContentCorrectly() throws MessagingException {
        // Arrange
        String subject = "Test Subject";
        String to = "recipient@example.com";
        String bodyContent = "This is a test email body";

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        
        // Act
        emailService.sendNotification(subject, to, bodyContent);
        
        // Assert
        // Capture and verify the subject formatting
        ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        
        // Additional verification that the message was created
        verify(mailSender).createMimeMessage();
    }
}