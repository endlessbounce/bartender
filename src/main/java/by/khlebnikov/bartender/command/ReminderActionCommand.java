package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.logic.UserService;
import by.khlebnikov.bartender.mail.Mailer;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class ReminderActionCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private UserService service;

    public ReminderActionCommand() {
        this.service = new UserService();
    }

    @Override
    public String execute(HttpServletRequest request) {
        String emailPropertyPath = request.getServletContext().getRealPath(Constant.EMAIL_PROPERTY_PATH);
        String email = request.getParameter(Constant.EMAIL);
        boolean correctInput = Validator.checkString(email);

        if (correctInput) {
            Optional<User> userOpt = service.findUser(email);

            if (userOpt.isPresent()) {
                try {
                    Properties properties = new Properties();
                    properties.load(new FileInputStream(emailPropertyPath));

                    String locale = (String) request.getSession().getAttribute(Constant.LOCALE);
                    String subject = PropertyReader.getMessageProperty("message.lettersubjectpassword", locale);
                    String message = PropertyReader.getMessageProperty("message.password", locale) + userOpt.get().getPassword();

                    Mailer.sendEmail(email, subject, message, properties);
                } catch (MessagingException | IOException e) {
                    logger.catching(e);
                }

                request.setAttribute(Constant.MESSAGE_TYPE, MessageType.PASSWORD_SENT);
                return PropertyReader.getConfigProperty(Constant.PAGE_RESULT);
            } else {
                request.setAttribute(Constant.MESSAGE_TYPE, MessageType.USER_NOT_REGISTERED);
            }
        } else {
            request.setAttribute(Constant.MESSAGE_TYPE, MessageType.INCORRECT_EMAIL);
        }

        request.setAttribute(Constant.EMAIL, email);

        return PropertyReader.getConfigProperty(Constant.PAGE_REMINDER);
    }
}
