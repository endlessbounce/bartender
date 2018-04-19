package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.exception.ControllerException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.tag.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Checks if correct user is trying to change the password (code returned from email link
 * should be equal to that in the database).
 * If user is the right one - returns a page with a form for user to enter new password and its
 * confirmation
 */
public class SetPasswordCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private UserService service;

    public SetPasswordCommand() {
        this.service = new UserService();
    }

    @Override
    public String execute(HttpServletRequest request) throws ControllerException {
        String page = PropertyReader.getConfigProperty(ConstPage.SET_PASSWORD);
        String confirmationCode = request.getParameter(ConstParameter.CODE);
        String email = request.getParameter(ConstParameter.EMAIL);
        boolean correctUser = false;

        /*if user was really trying to change his password,
        let him do it and send him the form for it*/
        try {
            correctUser = service.changingPasswordUser(email, confirmationCode);

            request.getSession().setAttribute(ConstParameter.EMAIL, email);

            if(!correctUser){
                page = PropertyReader.getConfigProperty(ConstPage.RESULT);
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.INCORRECT_USER);
                logger.debug("attempt to change password from email " + email + " providing incorrect code");
            }

            service.deleteProspectUser(email);
        } catch (ServiceException e) {
            throw new ControllerException("User was trying to change password: " + correctUser, e);
        }

        return page;
    }
}
