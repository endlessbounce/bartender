package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.exception.ControllerException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.UserService;
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
    private Password passwordGenerator;
    private UserService service;

    public ConfirmEmailCommand() {
        this.service = new UserService();
        this.passwordGenerator = new Password();
    }

    @Override
    public String execute(HttpServletRequest request) throws ControllerException {
        boolean correctInput = false;
        boolean alreadyRegistered = false;
        boolean awaitingConfirmation = false;
        String name = request.getParameter(ConstParameter.NAME);
        String email = request.getParameter(ConstParameter.EMAIL);
        String password = request.getParameter(ConstParameter.PASSWORD);
        String confirmation = request.getParameter(ConstParameter.CONFIRMATION);
        long code = Utility.generateCode();

        String page = PropertyReader.getConfigProperty(ConstPage.RESULT);
        String emailPropertyPath = request.getServletContext().getRealPath(Constant.EMAIL_PROPERTY_PATH);
        String locale = (String) request.getSession().getAttribute(ConstParameter.LOCALE);
        String subject = PropertyReader.getMessageProperty("message.lettersubject", locale);
        String message = PropertyReader.getMessageProperty("message.confirmation", locale) +
                ConstParameter.EMAIL_REQ + email +
                ConstParameter.CODE_REQ + code;

        try {
            /*check if registration data is valid in case front end doesn't check it*/
            correctInput = Validator.checkRegistrationData(name, email, password, confirmation, request);
            alreadyRegistered = service.findUser(email).isPresent();
            awaitingConfirmation = service.isProspectRegistered(email);

            /*if user is not registered yet and not awaiting confirmation and all input data is correct*/
            if (correctInput && !alreadyRegistered && !awaitingConfirmation) {

                /*create hash-key and salt*/
                byte[] salt = passwordGenerator.getNextSalt();
                Optional<byte[]> hashOpt = passwordGenerator.hash(password.toCharArray(), salt);

                if (!hashOpt.isPresent()) {
                    request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.HASH_ERROR);
                    return page;
                }

                byte[] hashKey = hashOpt.get();

                service.saveProspectUser(new ProspectUser(name, email, hashKey, salt,
                        Utility.expirationTime(), code));

                /*send a letter to user with confirmation link*/
                Properties properties = new Properties();
                properties.load(new FileInputStream(emailPropertyPath));
                Mailer.sendEmail(email, subject, message, properties);
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.EMAIL_SENT);

                return page;
            } else if (alreadyRegistered) {
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.ALREADY_REGISTERED);
            } else if (awaitingConfirmation) {
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.AWAITING_CONFIRMATION);
            }
        } catch (IOException | MessagingException | ServiceException e) {
            request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.MAIL_ERROR);
            throw new ControllerException("Correct input: " + correctInput + ",\n already registered: " + alreadyRegistered +
                    ",\n awaiting confirmation: " + awaitingConfirmation, e);
        }

        request.setAttribute(ConstParameter.NAME, name);
        request.setAttribute(ConstParameter.EMAIL, email);
        request.setAttribute(ConstParameter.PASSWORD, password);
        request.setAttribute(ConstParameter.CONFIRMATION, confirmation);

        page = PropertyReader.getConfigProperty(ConstPage.REGISTRATION);
        return page;
    }
}
