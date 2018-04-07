package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.mail.Mailer;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.tag.MessageType;
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
 * Receives email and validates it, then sends to user's email
 * link for changing password
 */
public class ResetActionCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private UserService service;

    public ResetActionCommand() {
        this.service = new UserService();
    }

    @Override
    public String execute(HttpServletRequest request) {
        String emailPropertyPath = request.getServletContext().getRealPath(Constant.EMAIL_PROPERTY_PATH);
        String email = request.getParameter(ConstParameter.EMAIL);
        String page = PropertyReader.getConfigProperty(ConstPage.RESET);
        long code = Utility.generateCode();

        boolean correctInput = Validator.checkString(email);

        if (correctInput) {
            Optional<User> userOpt = service.findUser(email);

            if (userOpt.isPresent()) {
                try {
                    User user = userOpt.get();

                    /*make a record to subsequently be able to check the code in the link*/
                    service.registerProspectUser(new ProspectUser(user.getName(),
                            email,
                            user.getHashKey(),
                            user.getSalt(),
                            Utility.expirationTime(),
                            code));

                    Properties properties = new Properties();
                    properties.load(new FileInputStream(emailPropertyPath));

                    String locale = (String) request.getSession().getAttribute(ConstParameter.LOCALE);
                    String subject = PropertyReader.getMessageProperty("message.lettersubjectpassword", locale);
                    String message = PropertyReader.getMessageProperty("message.linktochangepassword", locale) +
                            ConstParameter.EMAIL_REQ + email +
                            ConstParameter.CODE_REQ + code;

                    Mailer.sendEmail(email, subject, message, properties);
                    request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.RESET_LINK_SENT);
                } catch (MessagingException | IOException e) {
                    logger.catching(e);
                    request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.MAIL_ERROR);
                }

                page = PropertyReader.getConfigProperty(ConstPage.RESULT);
            } else {
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.USER_NOT_REGISTERED);
            }
        } else {
            request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.INCORRECT_EMAIL);
        }

        request.setAttribute(ConstParameter.EMAIL, email);

        return page;
    }
}
