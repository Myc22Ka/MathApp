package pl.myc22ka.mathapp.utils.microservices.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.frontend.url}")
    private String frontendUrl;

    private final Random random = new Random();

    /**
     * Sends an HTML email to the specified recipient.
     */
    public void sendHtmlEmail(String to, String subject, @NotNull String htmlBody) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody.trim(), true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Loads an email template from the classpath resources.
     */
    public String loadTemplate(String templateName) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/email/" + templateName);
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template: " + templateName, e);
        }
    }

    /**
     * Replaces variables in the template with provided values.
     * Template variables should be in the format {{variableName}}.
     */
    public String populateTemplate(String template, @NotNull Map<String, String> variables) {
        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }

    /**
     * Generates a 6-digit numeric verification code.
     */
    public String generateVerificationCode() {
        return String.valueOf(100000 + random.nextInt(900000));
    }

    public String generateVerificationLink(String endpoint) {
        return frontendUrl + "/" + endpoint;
    }

    /**
     * Loads a template, populates it with variables, and sends it as an HTML email.
     * Automatically generates and includes a verification code if not provided.
     */
    @Async
    public void sendEmailFromTemplate(String to, String subject, String templateName, Map<String, String> variables) {
        CompletableFuture.runAsync(() -> {
            Map<String, String> modifiableVariables = new HashMap<>(variables);

            if (!modifiableVariables.containsKey("verificationCode")) {
                modifiableVariables.put("verificationCode", generateVerificationCode());
            }

            String template = loadTemplate(templateName);
            String populatedHtml = populateTemplate(template, modifiableVariables);
            sendHtmlEmail(to, subject, populatedHtml);
        });
    }

}
