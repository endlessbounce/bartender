package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.ConstAttribute;
import by.khlebnikov.bartender.constant.ConstParameter;
import by.khlebnikov.bartender.constant.ConstPage;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.exception.ControllerException;
import by.khlebnikov.bartender.exception.ServiceException;
import by.khlebnikov.bartender.service.UserService;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.tag.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class RegisterActionCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private UserService service;

    public RegisterActionCommand() {
        this.service = new UserService();
    }

    @Override
    public String execute(HttpServletRequest request) throws ControllerException {
        Optional<User> userOpt;
        MessageType messageType;
        boolean success = false;

        String confirmationCode = request.getParameter(ConstParameter.CODE);
        String email = request.getParameter(ConstParameter.EMAIL);

        try{
            /*if user was trying to register, get his data and register him*/
            userOpt = service.checkProspectUser(email, confirmationCode);
            if(userOpt.isPresent()){
                success = service.saveUser(userOpt.get());
            }

            if(success){
                messageType = MessageType.REGISTRATION_SUCCESS;
            }else{
                messageType = MessageType.REGISTRATION_ERROR;
            }

            service.deleteProspectUser(email);
        } catch (ServiceException e) {
            throw new ControllerException("Registration success: " + success, e);
        }

        request.setAttribute(ConstAttribute.MESSAGE_TYPE, messageType);
        logger.debug("message: " + messageType);
        return PropertyReader.getConfigProperty(ConstPage.RESULT);
    }
}
