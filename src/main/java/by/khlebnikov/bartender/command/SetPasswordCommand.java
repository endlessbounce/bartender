package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.exception.CommandException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.tag.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Class to render to a user the form to setting a new password
 */
public class SetPasswordCommand implements Command {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();
    private UserService service;

    // Constructors -------------------------------------------------------------------------------
    public SetPasswordCommand() {
        this.service = new UserService();
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * Checks the user trying to change the password (code returned from email link
     * should be equal to that in the database).
     * If the user is the right one - returns a page with a form for user to enter new password
     * and its confirmation
     *
     * @param request HttpServletRequest request
     * @return either result page or reset password page
     * @throws CommandException is thrown if an exception on lower levels happens
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String page = ConstPage.SET_PASSWORD;
        String confirmationCode = request.getParameter(ConstParameter.CODE);
        String email = request.getParameter(ConstParameter.EMAIL);
        boolean correctUser = false;

        try {
            correctUser = service.isResettingUser(email, confirmationCode);
            /*attach user's email to the session for further use by SetPasswordActionCommand
            * (it will be looking for a user by this email)*/
            request.getSession().setAttribute(ConstParameter.EMAIL, email);

            if (!correctUser) {
                page = ConstPage.RESULT;
                request.setAttribute(ConstAttribute.MESSAGE_TYPE, MessageType.INCORRECT_USER);
                logger.debug("attempt to change password from email " + email + " providing incorrect code");
            }

            service.deleteProspectUser(email);
        } catch (ServiceException e) {
            throw new CommandException("User was trying to change password: " + correctUser, e);
        }

        return page;
    }

}
