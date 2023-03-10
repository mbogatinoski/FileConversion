package com.mbogatinoski.fileconversion.emails;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailAuthenticator extends Authenticator {
    private final String senderEmail;
    private final String senderPassword;

    public EmailAuthenticator(String senderEmail, String senderPassword) {
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(senderEmail, senderPassword);
    }
}
