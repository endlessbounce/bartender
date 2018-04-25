package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.CommandException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.tag.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Class to register users in the system
 */
public class RegisterActionCommand implements Command {

    // Vars ---------------------------------------------------------------------------------------
    private static Logger logger = LogManager.getLogger();
    private UserService service;

    // Constructors -------------------------------------------------------------------------------
    public RegisterActionCommand() {
        this.service = new UserService();
    }

    // Actions ------------------------------------------------------------------------------------

    /**
     * First checks the 'prospect user' table to check if the user was trying to register. If so,
     * saves this user into the 'user' table and deletes him from prospects.
     *
     * @param request HttpServletRequest request
     * @return the result page with a message
     * @throws CommandException is thrown if an exception on lower levels happens
     */
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        Optional<User> userOpt;
        MessageType messageType;
        boolean success = false;

        String confirmationCode = request.getParameter(ConstParameter.CODE);
        String email = request.getParameter(ConstParameter.EMAIL);

        try {
            /*if user was trying to register, get his data and register him*/
            userOpt = service.checkProspectUser(email, confirmationCode);

            if (userOpt.isPresent()) {
                success = service.saveUser(userOpt.get());
            }

            if (success) {
                messageType = MessageType.REGISTRATION_SUCCESS;
            } else {
                messageType = MessageType.REGISTRATION_ERROR;
            }

            service.deleteProspectUser(email);
        } catch (ServiceException e) {
            throw new CommandException("Registration is successful: " + success, e);
        }

        request.setAttribute(ConstAttribute.MESSAGE_TYPE, messageType);
        logger.debug("message: " + messageType);
        return ConstPage.RESULT;
    }
}
