package com.mbogatinoski.fileconversion.emails;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class EmailSender {
    public void sendEmail(String recipientEmail, byte[] attachmentBytes, String attachmentType, String attachmentName) {
        String senderEmail = "mbogatinoski@citycollege.sheffield.eu";
        String senderPassword = "890bog200";
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.ssl.trust", host);

        String subject = "Your converted file";
        String messageBody = "Dear Customer, \nYou can find you converted file to this email. Thank you for using our service. \nFileConversion";

        EmailAuthenticator authenticator = new EmailAuthenticator(senderEmail, senderPassword);

        Session session = Session.getInstance(props, authenticator);

        try {
            javax.mail.Message message = new javax.mail.internet.MimeMessage(session);
            message.setRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress(recipientEmail));
            message.setSubject(subject);

            // Create the message body part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(messageBody);

            // Create the attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setFileName(attachmentName);
            attachmentPart.setContent(attachmentBytes, attachmentType);

            // Create a multipart message and add the message body part and attachment part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            // Set the content of the message to the multipart message
            message.setContent(multipart);

            // Send the message
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }


}
