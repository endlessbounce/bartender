package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.logic.UserService;
import by.khlebnikov.bartender.reader.PropertyReader;
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
    public String execute(HttpServletRequest request) {
        String page = PropertyReader.getConfigProperty(Constant.PAGE_SET_PASSWORD);
        String confirmationCode = request.getParameter(Constant.CODE);
        String email = request.getParameter(Constant.EMAIL);

        /*if user was really trying to change his password,
        let him do it and send him the form for it*/
        boolean correctUser = service.changingPasswordUser(email, confirmationCode);
        request.getSession().setAttribute(Constant.EMAIL, email);

        if(!correctUser){
            page = PropertyReader.getConfigProperty(Constant.PAGE_RESULT);
            request.setAttribute(Constant.MESSAGE_TYPE, MessageType.INCORRECT_USER);
            logger.debug("attempt to change password from email " + email + " providing incorrect code");
        }

        service.deleteProspectUser(email);
        return page;
    }
}
