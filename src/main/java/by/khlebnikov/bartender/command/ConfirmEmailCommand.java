package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.logic.UserService;
import by.khlebnikov.bartender.mail.Mailer;
import by.khlebnikov.bartender.manager.PropertyManager;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.utility.ApplicationUtility;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfirmEmailCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private UserService service;

    @Override
    public String execute(HttpServletRequest request) {
        String emailPropertyPath = request.getServletContext().getRealPath(Constant.EMAIL_PROPERTY_PATH);
        String queryPropertyPath = request.getServletContext().getRealPath(Constant.QUERY_PROPERTY_PATH);
        service = new UserService(queryPropertyPath);

        String name = request.getParameter(Constant.NAME);
        String email = request.getParameter(Constant.EMAIL);
        String password = request.getParameter(Constant.PASSWORD);
        String confirmation = request.getParameter(Constant.CONFIRMATION);
        long code = ApplicationUtility.generateCode();
        String subject = PropertyManager.getMessageProperty("message.lettersubject");
        String message = PropertyManager.getMessageProperty("message.confirmation") +
                Constant.EMAIL_PARAM + email +
                Constant.CODE_PARAM + code;

        /*check if registration data is valid in case front end doesn't check it*/
        boolean correctInput = Validator.checkRegistrationData(name, email, password, confirmation, request);
        boolean alreadyRegistered = service.isUserRegistered(email);
        boolean awaitingConfirmation = service.isProspectRegistered(email);

        /*if user is not registered yet and not awaiting confirmation and all input data is correct*/
        if (correctInput && !alreadyRegistered && !awaitingConfirmation) {
            service.registerProspectUser(new ProspectUser(name, email, password, ApplicationUtility.expirationTime(), code));

            try {
                /*send to user a letter with a confirmation link*/
                Properties properties = new Properties();
                properties.load(new FileInputStream(emailPropertyPath));

                Mailer.sendEmail(email, subject, message, properties);
            } catch (IOException | MessagingException e) {
                logger.catching(e);
            }

            request.setAttribute("MessageType", MessageType.EMAIL_SENT);
            return PropertyManager.getConfigProperty("path.page.result");
        } else if(alreadyRegistered){
            request.setAttribute("MessageType", MessageType.ALREADY_REGISTERED);
        } else if(awaitingConfirmation){
            request.setAttribute("MessageType", MessageType.AWAITING_CONFIRMATION);
        }

        request.setAttribute("name", name);
        request.setAttribute("email", email);
        request.setAttribute("password", password);
        request.setAttribute("confirmation", confirmation);

        return PropertyManager.getConfigProperty("path.page.registration");
    }
}
