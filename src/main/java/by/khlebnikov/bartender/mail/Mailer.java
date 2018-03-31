package by.khlebnikov.bartender.mail;

import by.khlebnikov.bartender.constant.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Mailer {
    private static final Logger LOGGER = LogManager.getLogger();

    public static void sendEmail(String toAddress,
                                 String subject,
                                 String message,
                                 Properties properties) throws MessagingException {

        String userName = properties.getProperty(Constant.USER);
        String password = properties.getProperty(Constant.PASSWORD);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getInstance(properties, auth);
        LOGGER.debug("Mail session started");

        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(message);
        LOGGER.debug("Message is ready");

        Transport.send(msg);
    }
}

