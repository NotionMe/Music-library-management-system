package ua.notion.musiclibrary.service.impl;

import java.util.Properties;
import java.util.Random;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import ua.notion.musiclibrary.service.contract.EmailService;

public class EmailServiceImpl implements EmailService {

    private final String password;
    private final Properties properties;
    private final Session session;
    private final String from;

    private final String host = "smtp.gmail.com";

    public EmailServiceImpl(String password, String from) {
        this.from = from;
        this.password = password;
        this.properties = new Properties();
        this.properties.put("mail.smtp.host", host);
        this.properties.put("mail.smtp.port", "587");
        this.properties.put("mail.smtp.auth", "true");
        this.properties.put("mail.smtp.starttls.enable", "true");

        this.session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
    }

    @Override
    public String sendPasswordCode(String toAddress, String username) {
        String randomCode = generateRandomCode();
        String messageForUser = String.format("Hello %s, your verification code is: %s", username, randomCode);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            message.setSubject("Verification Code - Music Library");
            message.setText(messageForUser);

            Transport.send(message);
            System.out.println("Код " + randomCode + " успішно відправлено на " + toAddress);
        } catch (MessagingException mex) {
            System.err.println("Помилка відправлення: " + mex.getMessage());
        }
        return randomCode;
    }

    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                sb.append("-");
            }
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }

        return sb.toString();
    }
}
