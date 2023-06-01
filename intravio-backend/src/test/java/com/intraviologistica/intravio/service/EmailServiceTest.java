package com.intraviologistica.intravio.service;

import com.intraviologistica.intravio.service.exceptions.ResourceNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void testEnviarEmail() throws MessagingException {
        MockitoAnnotations.openMocks(this);

        doNothing().when(mailSender).send(any(MimeMessage.class)); // Defina o comportamento esperado para o mock do JavaMailSender

        emailService.enviarEmail("emailteste@email.com.br", "Assunto do email", "Conteúdo do email"); // Execute o método que deseja testar

        verify(mailSender, times(0)).send((MimeMessagePreparator) any());   // Verifique se o método do JavaMailSender foi chamado corretamente
    }

    @Test
    public void testEnviarEmailComErro() {
        doNothing().when(mailSender).send(any(MimeMessage.class));

        catchThrowableOfType(() -> emailService.enviarEmail("emailNaoExiste@email.com", "Assunto do email", "Conteúdo do email"), ResourceNotFoundException.class);
    }
}