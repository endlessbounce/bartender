package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.entity.ProspectUser;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.CommandException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.utility.TimeGenerator;
import by.khlebnikov.bartender.validator.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Class to register users in the system
 */
public class RegisterActionCommand implements Command {

    // Vars ---------------------------------------------------------------------------------------
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
        String confirmationCode = request.getParameter(ConstParameter.CODE);
        boolean success = false;

        try {
            if (Validator.checkString(confirmationCode)) {
                /*if user was trying to register, get his data and register him*/
                Optional<ProspectUser> prospectUserOpt = service.findProspectByCode(confirmationCode);

                if (prospectUserOpt.isPresent()) {
                    ProspectUser prospectUser = prospectUserOpt.get();

                    if (prospectUser.getExpiration() > TimeGenerator.currentTime()) {
                        success = service.saveUser(new User(prospectUser.getName(),
                                prospectUser.getEmail(),
                                prospectUser.getHashKey(),
                                prospectUser.getSalt()));
                    }
                }
            }

            MessageType messageType = success
                    ? MessageType.REGISTRATION_SUCCESS
                    : MessageType.REGISTRATION_ERROR;
            request.setAttribute(ConstAttribute.MESSAGE_TYPE, messageType);

            service.deleteProspectUser(confirmationCode);
        } catch (ServiceException e) {
            throw new CommandException("Registration result: " + success, e);
        }

        return ConstPage.RESULT;
    }
}
