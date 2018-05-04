package by.khlebnikov.bartender.mail;

import by.khlebnikov.bartender.constant.ConstBundle;
import by.khlebnikov.bartender.constant.ConstLocale;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Class providing mailing functions to the application
 */
public class Mailer {

    // Constants ----------------------------------------------------------------------------------
    private static final String SUBJECT = PropertyReader.getMessageProperty(ConstBundle.REG_SUBJECT, ConstLocale.EN_US);
    private static final String MESSAGE = PropertyReader.getMessageProperty(ConstBundle.REG_MESSAGE, ConstLocale.EN_US);
    private static final String SUBJECT_RU = PropertyReader.getMessageProperty(ConstBundle.REG_SUBJECT, ConstLocale.RU_RU);
    private static final String MESSAGE_RU = PropertyReader.getMessageProperty(ConstBundle.REG_MESSAGE, ConstLocale.RU_RU);
    private static final String SUBJECT_RESET = PropertyReader.getMessageProperty(ConstBundle.RESET_SUBJECT, ConstLocale.EN_US);
    private static final String MESSAGE_RESET = PropertyReader.getMessageProperty(ConstBundle.RESET_MESSAGE, ConstLocale.EN_US);
    private static final String SUBJECT_RESET_RU = PropertyReader.getMessageProperty(ConstBundle.RESET_SUBJECT, ConstLocale.RU_RU);
    private static final String MESSAGE_RESET_RU = PropertyReader.getMessageProperty(ConstBundle.RESET_MESSAGE, ConstLocale.RU_RU);

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();

    // Actions ------------------------------------------------------------------------------------

    /**
     * Prepares and sends a letter to the given email
     *
     * @param toAddress  receiver's address
     * @param request    HttpServletRequest
     * @param code       unique code as a part of a confirmation link
     * @param letterType defines either registration letter or reset password letter
     * @throws MessagingException is thrown in case of mailing error
     * @throws IOException        is thrown in case of file reading failure
     */
    public static void sendEmail(String toAddress, HttpServletRequest request, String code, LetterType letterType)
            throws MessagingException, IOException {

        /* Prepare message and subject*/
        String locale = (String) request.getSession().getAttribute(ConstParameter.LOCALE);
        String subject = Constant.EMPTY;
        String message = Constant.EMPTY;
        boolean isEnglish = ConstLocale.EN_US.equals(locale);

        switch (letterType) {
            case REGISTRATION:
                subject = isEnglish ? SUBJECT : SUBJECT_RU;
                message = isEnglish ? MESSAGE : MESSAGE_RU;
                break;
            case RESET_PASSWORD:
                subject = isEnglish ? SUBJECT_RESET : SUBJECT_RESET_RU;
                message = isEnglish ? MESSAGE_RESET : MESSAGE_RESET_RU;
                break;
        }

        message += code;

        /* Load properties*/
        String emailPropertyPath = request.getServletContext().getRealPath(Constant.EMAIL_PROPERTY_PATH);
        Properties properties = new Properties();
        properties.load(new FileInputStream(emailPropertyPath));
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

