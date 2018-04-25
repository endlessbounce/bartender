package by.khlebnikov.bartender.mail;

import by.khlebnikov.bartender.constant.ConstParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Class providing mailing functions to the application
 */
public class Mailer {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();

    // Actions ------------------------------------------------------------------------------------

    /**
     * Prepares and sends a letter to a given email
     *
     * @param toAddress  receiver's address
     * @param subject    subject of the letter
     * @param message    massage of the letter
     * @param properties containing email configurations
     * @throws MessagingException is thrown in case of mailing error
     */
    public static void sendEmail(String toAddress,
                                 String subject,
                                 String message,
                                 Properties properties) throws MessagingException {

        String userName = properties.getProperty(ConstParameter.USER);
        String password = properties.getProperty(ConstParameter.PASSWORD);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getInstance(properties, auth);
        logger.debug("Mail session started");

        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(message);
        logger.debug("Message is ready");

        Transport.send(msg);
    }

}

