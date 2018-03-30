package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.logic.UserService;
import by.khlebnikov.bartender.mail.Mailer;
import by.khlebnikov.bartender.manager.PropertyManager;
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

public class ReminderCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private UserService service;

    @Override
    public String execute(HttpServletRequest request) {
        String emailPropertyPath = request.getServletContext().getRealPath(Constant.EMAIL_PROPERTY_PATH);
        String queryPropertyPath = request.getServletContext().getRealPath(Constant.QUERY_PROPERTY_PATH);
        service = new UserService(queryPropertyPath);

        String email = request.getParameter(Constant.EMAIL);
        boolean correctInput = Validator.checkString(email);

        if(correctInput){
            Optional<User> userOpt = service.findUser(email);

            if(userOpt.isPresent()){
                try {
                    Properties properties = new Properties();
                    properties.load(new FileInputStream(emailPropertyPath));

                    String subject = PropertyManager.getMessageProperty("message.lettersubjectpassword");
                    String message = PropertyManager.getMessageProperty("message.password") + userOpt.get().getPasword();

                    Mailer.sendEmail(email, subject, message, properties);
                } catch (MessagingException | IOException e) {
                    logger.catching(e);
                }

                request.setAttribute("MessageType", MessageType.PASSWORD_SENT);
                return PropertyManager.getConfigProperty("path.page.result");
            } else {
                request.setAttribute("MessageType", MessageType.USER_NOT_REGISTERED);
            }
        }else{
            request.setAttribute("MessageType", MessageType.INCORRECT_EMAIL);
        }

        request.setAttribute("email", email);

        return PropertyManager.getConfigProperty("path.page.reminder");
    }
}
