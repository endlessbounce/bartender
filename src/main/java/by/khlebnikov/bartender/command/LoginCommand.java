package by.khlebnikov.bartender.command;

import by.khlebnikov.bartender.constant.Constant;
import by.khlebnikov.bartender.entity.User;
import by.khlebnikov.bartender.logic.UserService;
import by.khlebnikov.bartender.reader.PropertyReader;
import by.khlebnikov.bartender.tag.MessageType;
import by.khlebnikov.bartender.utility.Utility;
import by.khlebnikov.bartender.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class LoginCommand implements Command {
    private Logger logger = LogManager.getLogger();
    private UserService service;
    private HttpServletResponse response;

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public LoginCommand() {
        this.service = new UserService();
    }

    @Override
    public String execute(HttpServletRequest request) {
        String email = request.getParameter(Constant.EMAIL);
        String password = request.getParameter(Constant.PASSWORD);
        String logged = request.getParameter(Constant.STAY_LOGGED);

        logger.debug("is logged: " + logged);

        boolean correctInput = Validator.checkLoginData(email, password, request);

        if(correctInput){
            Optional<User> userOpt = service.checkUser(email, password);

            if(userOpt.isPresent()){
                User user = userOpt.get();
                request.getSession().setAttribute(Constant.USER_NAME, user.getName());

                //checking stay logged in checkbox
                if(Constant.TRUE.equals(logged)){
                    String id = Utility.uniqueId();
                    user.setUniqueCookie(id);
                    service.updateUser(user);

                    Cookie cookie = Utility.persistingCookie(id);
                    response.addCookie(cookie);
                }

                return PropertyReader.getConfigProperty(Constant.PAGE_HOME);
            } else {
                request.setAttribute(Constant.MESSAGE_TYPE, MessageType.INCORRECT_EMAIL_OR_PASSWORD);
            }
        }

        request.setAttribute(Constant.EMAIL, email);
        request.setAttribute(Constant.PASSWORD, password);
//        request.setAttribute(Constant.STAY_LOGGED, logged);
        return PropertyReader.getConfigProperty(Constant.PAGE_LOGIN);
    }
}
