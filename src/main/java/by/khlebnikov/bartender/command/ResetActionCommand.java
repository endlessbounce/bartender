package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.CommandException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.mail.Mailer;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.utility.CodeGenerator;
import by.khlebnikov.bartender.utility.TimeGenerator;
import by.khlebnikov.bartender.validator.Validator;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

/**
 * Class providing reset password functionality
 */
public class ResetActionCommand implements Command {

    // Vars ---------------------------------------------------------------------------------------
    private UserService service;

    // Constructors -------------------------------------------------------------------------------
    public ResetActionCommand() {
        this.service = new UserService();
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Receives an email submitted by user and validates it. If this user is registered
     * sends there a link for resetting his password
     *
     * @param request HttpServletRequest request
     * @return result page if the user was registered or reset page with a failure message
     * @throws CommandException is thrown if an exception on lower levels happens
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String emailPropertyPath = request.getServletContext().getRealPath(Constant.EMAIL_PROPERTY_PATH);
        String email = request.getParameter(ConstParameter.EMAIL);
        String page = ConstPage.RESET;
        boolean correctInput = Validator.checkString(email);
        long code = CodeGenerator.generateCode();
        User user = null;

        if (correctInput) {
            try {
                Optional<User> userOpt = service.findUserByEmail(email);

                if (userOpt.isPresent()) {
                    user = userOpt.get();

                    /*make a record to further be able to check the code in the link*/
                    service.saveProspectUser(new ProspectUser(user.getName(), email, user.getHashKey(),
                            user.getSalt(), TimeGenerator.expirationTime(), code));

                    Properties properties = new Properties();
                    properties.load(new FileInputStream(emailPropertyPath));

                    String locale = (String) request.getSession().getAttribute(ConstParameter.LOCALE);
                    String subject = PropertyReader.getMessageProperty("message.lettersubjectpassword", locale);
                    String message = PropertyReader.getMessageProperty("message.linktochangepassword", locale) +
                            ConstParameter.EMAIL_REQ + email +
                            ConstParameter.CODE_REQ + code;

                    Mailer.sendEmail(email, subject, message, properties);
                    request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.RESET_LINK_SENT);

                    page = ConstPage.RESULT;
                } else {
                    request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.USER_NOT_REGISTERED);
                }
            } catch (MessagingException | IOException | ServiceException e) {
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.MAIL_ERROR);
                throw new CommandException("User: " + user, e);
            }

        } else {
            request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.INCORRECT_EMAIL);
        }

        request.setAttribute(ConstParameter.EMAIL, email);

        return page;
    }
}
