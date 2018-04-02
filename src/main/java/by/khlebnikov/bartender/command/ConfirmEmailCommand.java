package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.exception.ControllerException;
import by.khlebnikov.bartender.logic.UserService;
import by.khlebnikov.bartender.mail.Mailer;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.utility.Password;
import by.khlebnikov.bartender.utility.Utility;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * Sends a link to user's email, after following on which it makes a new record
 * in the database or discards request for registration if registration time is out
 */
public class ConfirmEmailCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private Password passwordGenerator;
    private UserService service;

    public ConfirmEmailCommand() {
        this.service = new UserService();
        this.passwordGenerator = new Password();
    }

    @Override
    public String execute(HttpServletRequest request) {
        String name = request.getParameter(Constant.NAME);
        String email = request.getParameter(Constant.EMAIL);
        String password = request.getParameter(Constant.PASSWORD);
        String confirmation = request.getParameter(Constant.CONFIRMATION);
        long code = Utility.generateCode();

        String page = PropertyReader.getConfigProperty(Constant.PAGE_RESULT);
        String emailPropertyPath = request.getServletContext().getRealPath(Constant.EMAIL_PROPERTY_PATH);
        String locale = (String) request.getSession().getAttribute(Constant.LOCALE);
        String subject = PropertyReader.getMessageProperty("message.lettersubject", locale);
        String message = PropertyReader.getMessageProperty("message.confirmation", locale) +
                Constant.EMAIL_PARAM + email +
                Constant.CODE_PARAM + code;

        /*check if registration data is valid in case front end doesn't check it*/
        boolean correctInput = Validator.checkRegistrationData(name, email, password, confirmation, request);
        boolean alreadyRegistered = service.isUserRegistered(email);
        boolean awaitingConfirmation = service.isProspectRegistered(email);

        /*if user is not registered yet and not awaiting confirmation and all input data is correct*/
        if (correctInput && !alreadyRegistered && !awaitingConfirmation) {

            try {
                /*create hash-key and salt*/
                byte [] salt = passwordGenerator.getNextSalt();
                Optional<byte []> hashOpt = passwordGenerator.hash(password.toCharArray(), salt);

                if(!hashOpt.isPresent()){
                    request.setAttribute(Constant.MESSAGE_TYPE, MessageType.HASH_ERROR);
                    return page;
                }

                byte [] hashKey = hashOpt.get();

                service.registerProspectUser(new ProspectUser(name,
                        email,
                        hashKey,
                        salt,
                        Utility.expirationTime(),
                        code));


                /*send to user a letter with a confirmation link*/
                Properties properties = new Properties();
                properties.load(new FileInputStream(emailPropertyPath));

                Mailer.sendEmail(email, subject, message, properties);
                request.setAttribute(Constant.MESSAGE_TYPE, MessageType.EMAIL_SENT);
            } catch (IOException | MessagingException e) {
                logger.catching(e);
                request.setAttribute(Constant.MESSAGE_TYPE, MessageType.MAIL_ERROR);
            }

            return page;
        } else if(alreadyRegistered){
            request.setAttribute(Constant.MESSAGE_TYPE, MessageType.ALREADY_REGISTERED);
        } else if(awaitingConfirmation){
            request.setAttribute(Constant.MESSAGE_TYPE, MessageType.AWAITING_CONFIRMATION);
        }

        request.setAttribute(Constant.NAME, name);
        request.setAttribute(Constant.EMAIL, email);
        request.setAttribute(Constant.PASSWORD, password);
        request.setAttribute(Constant.CONFIRMATION, confirmation);

        page = PropertyReader.getConfigProperty(Constant.PAGE_REGISTRATION);
        return page;
    }
}
