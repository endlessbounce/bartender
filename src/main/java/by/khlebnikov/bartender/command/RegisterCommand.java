package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.logic.UserService;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.tag.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class RegisterCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private UserService service;

    public RegisterCommand() {
        this.service = new UserService();
    }

    @Override
    public String execute(HttpServletRequest request) {
        Optional<User> userOpt;
        MessageType messageType;
        boolean success = false;

        String confirmationCode = request.getParameter(Constant.CODE);
        String email = request.getParameter(Constant.EMAIL);

        /*if user was trying to register, get his data and register him*/
        userOpt = service.checkProspectUser(email, confirmationCode);
        if(userOpt.isPresent()){
            success = service.registerUser(userOpt.get());
        }

        if(success){
            messageType = MessageType.REGISTRATION_SUCCESS;
        }else{
            messageType = MessageType.REGISTRATION_ERROR;
        }

        service.deleteProspectUser(email);
        request.setAttribute(Constant.MESSAGE_TYPE, messageType);
        logger.debug("message: " + messageType);
        return PropertyReader.getConfigProperty(Constant.PAGE_RESULT);
    }
}
